package us.ledge.line.sdk.sdk.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import us.ledge.line.sdk.sdk.models.Model;
import us.ledge.line.sdk.sdk.presenters.Presenter;

/**
 * Generic MVP Activity, wires up the MVP parts.
 *
 * @param <M> {@link Model} type.
 * @param <V> {@link View} type.
 * @param <P> {@link Presenter} type.
 *
 * @author Wijnand
 */
public abstract class MvpActivity<M extends Model, V extends View, P extends Presenter<M, V>>
        extends AppCompatActivity {

    protected V mView;
    protected P mPresenter;

    /**
     * @return New {@link View} instance.
     */
    protected abstract V createView();

    /**
     * @return New {@link Presenter} instance.
     */
    protected abstract P createPresenter();

    /**
     * Creates the {@link View} and {@link Presenter} and wires everything.<br/>
     * Assumes that the {@link Presenter} will instantiate the {@link Model}.<br/>
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = createView();
        setContentView(mView);

        mPresenter = createPresenter();
        mPresenter.attachView(mView);
    }

    /**
     * Detaches the {@link View} from the {@link Presenter}. Avoids memory leaks.<br/>
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
