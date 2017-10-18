package me.ledge.link.sdk.ui.views.link;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.views.LoadingView;
import me.ledge.link.sdk.ui.views.ViewWithIndeterminateLoading;
import me.ledge.link.sdk.ui.views.ViewWithToolbar;
import me.ledge.link.sdk.ui.views.userdata.NextButtonListener;
import me.ledge.link.sdk.ui.views.userdata.UserDataView;
import me.ledge.link.sdk.ui.widgets.steppers.StepperListener;
import us.feras.mdv.MarkdownView;

/**
 * Displays the welcome screen.
 * @author Adrian
 */
public class WelcomeView extends UserDataView<WelcomeView.ViewListener>
        implements ViewWithToolbar, ViewWithIndeterminateLoading {

    /**
     * Callbacks this {@link View} will invoke.
     */
    public interface ViewListener extends StepperListener, NextButtonListener {}

    private LoadingView mLoadingView;
    private MarkdownView mMarkdownView;

    /**
     * @see UserDataView#UserDataView
     * @param context See {@link UserDataView#UserDataView}.
     */
    public WelcomeView(Context context) {
        super(context);
    }

    /**
     * @see UserDataView#UserDataView
     * @param context See {@link UserDataView#UserDataView}.
     * @param attrs See {@link UserDataView#UserDataView}.
     */
    public WelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** {@inheritDoc} */
    @Override
    protected void findAllViews() {
        super.findAllViews();

        mMarkdownView = (MarkdownView) findViewById(R.id.md_content);
        mLoadingView = (LoadingView) findViewById(R.id.rl_loading_overlay);
    }

    @Override
    public LoadingView getLoadingView() {
        return mLoadingView;
    }

    public void setMarkdown(String markdown) {
        mMarkdownView.loadMarkdown(markdown);
    }
}
