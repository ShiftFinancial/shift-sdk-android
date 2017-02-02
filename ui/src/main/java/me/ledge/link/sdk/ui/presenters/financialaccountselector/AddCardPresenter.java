package me.ledge.link.sdk.ui.presenters.financialaccountselector;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.verygoodsecurity.vaultsdk.VaultAPIError;
import com.verygoodsecurity.vaultsdk.VaultAPIUICallback;

import java.net.MalformedURLException;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.models.financialaccountselector.AddCardModel;
import me.ledge.link.sdk.ui.presenters.ActivityPresenter;
import me.ledge.link.sdk.ui.presenters.Presenter;
import me.ledge.link.sdk.ui.storages.PCIVaultStorage;
import me.ledge.link.sdk.ui.views.financialaccountselector.AddCardView;


/**
 * Concrete {@link Presenter} for the add card screen.
 * @author Adrian
 */
public class AddCardPresenter
    extends ActivityPresenter<AddCardModel, AddCardView>
    implements Presenter<AddCardModel, AddCardView>, AddCardView.ViewListener {

    private AddCardDelegate mDelegate;
    private static final int CARD_IO_SCAN_INTENT_CODE = 5432;
    private static final int REQUEST_CAMERA_PERMISSION_INTENT_CODE = 9870;

    /**
     * Creates a new {@link ActivityPresenter} instance.
     *
     * @param activity Activity.
     */
    public AddCardPresenter(AppCompatActivity activity, AddCardDelegate delegate) {
        super(activity);
        mDelegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(AddCardView view) {
        super.attachView(view);
        view.setListener(this);
        view.setCardName(mModel.getCardHolderName());
    }


    @Override
    public void onBack() {
        mDelegate.addCardOnBackPressed();
    }

    @Override
    public AddCardModel createModel() {
        return new AddCardModel();
    }

    @Override
    public void addCardClickHandler() {
        if(mView.isCreditCardInputValid()) {
            storeCardAdditionalInfo(mView.getCardType(), mView.getLastFourDigits(), mView.getExpirationDate());
            tokenizeCard(mView.getCardNumber(), mView.getSecurityCode());
        }
        else {
            mView.displayErrorMessage(mActivity.getString(R.string.add_card_error));
        }
    }

    @Override
    public void scanClickHandler() {
        if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION_INTENT_CODE);
        }

        startScanCardActivity();
    }

    private void startScanCardActivity() {
        Intent scanIntent = new Intent(mActivity.getApplicationContext(), CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);

        mActivity.startActivityForResult(scanIntent, CARD_IO_SCAN_INTENT_CODE);
    }

    public void handleRequestPermissionsResult(int requestCode, int[] grantResults) {
        if(grantResults == null || grantResults.length == 0) {
            return;
        }

        if (requestCode == REQUEST_CAMERA_PERMISSION_INTENT_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
               mView.displayErrorMessage("Unable to scan card if camera permissions are not granted!");
               return;
            }
            startScanCardActivity();
        }
    }

    /**
     * Parses the received Activity result.
     * @param requestCode Request code.
     * @param resultCode Result code.
     * @param data Result data.
     */
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode != CardIOActivity.RESULT_CARD_INFO) {
            return;
        }

        if(requestCode == CARD_IO_SCAN_INTENT_CODE) {
            if (data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                storeCardAdditionalInfo(scanResult.getCardType().toString(), scanResult.getLastFourDigitsOfCardNumber(), formatExpiryDate(scanResult.expiryMonth, scanResult.expiryYear));
                tokenizeCard(scanResult.cardNumber, scanResult.cvv);
            }
            else {
                mView.displayErrorMessage("Scan was canceled.");
            }
        }
    }

    private String formatExpiryDate(int month, int year) {
        return String.valueOf(month) + "/" + String.valueOf(year);
    }

    private void storeCardAdditionalInfo(String cardType, String lastFourDigits, String expirationDate) {
        mModel.setCardType(cardType);
        mModel.setLastFourDigits(lastFourDigits);
        mModel.setExpirationDate(expirationDate);
    }

    private void tokenizeCard(String cardNumber, String securityCode) {
        VaultAPIUICallback storeSecurityCodeInVaultCallback = new VaultAPIUICallback() {
            @Override
            public void onSuccess(String token) {
                mModel.setCVVToken(token);
                mDelegate.cardAdded(mModel.getCard());
            }
            @Override
            public void onFailure(VaultAPIError error) {
                mView.displayErrorMessage("Error tokenizing cvv: " + error.name());
            }
        };

        VaultAPIUICallback storeCardInVaultCallback = new VaultAPIUICallback() {
            @Override
            public void onSuccess(String token) {
                mModel.setPANToken(token);

                try {
                    PCIVaultStorage.getInstance().storeData(mActivity, securityCode, storeSecurityCodeInVaultCallback);
                } catch (MalformedURLException e) {
                    mView.displayErrorMessage("VGS vault URL is incorrect: " + e.getMessage());
                }
            }
            @Override
            public void onFailure(VaultAPIError error) {
                mView.displayErrorMessage("Error tokenizing card: " + error.name());
            }
        };

        try {
            PCIVaultStorage.getInstance().storeData(mActivity, cardNumber, storeCardInVaultCallback);
        } catch (MalformedURLException e) {
            mView.displayErrorMessage("VGS vault URL is incorrect: " + e.getMessage());
        }
    }
}
