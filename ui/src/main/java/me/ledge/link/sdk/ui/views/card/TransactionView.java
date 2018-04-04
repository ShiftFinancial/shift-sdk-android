package me.ledge.link.sdk.ui.views.card;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import me.ledge.link.sdk.ui.R;


/**
 * Displays a single transaction
 * @author Adrian
 */
public class TransactionView extends FrameLayout {

    private ImageView mIconView;
    private TextView mTitleField;
    private TextView mDescriptionField;
    //private Transaction mData;

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     */
    public TransactionView(Context context) {
        super(context);
    }

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     * @param attrs See {@link CardView#CardView}.
     */
    public TransactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Finds all references to child Views.
     */
    private void findAllViews() {
        mIconView = (ImageView) findViewById(R.id.iv_icon);
        mTitleField = (TextView) findViewById(R.id.tv_title);
        mDescriptionField = (TextView) findViewById(R.id.tv_description);
    }

    /** {@inheritDoc} */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllViews();
    }
}