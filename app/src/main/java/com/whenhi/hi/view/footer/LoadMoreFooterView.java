package com.whenhi.hi.view.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.whenhi.hi.R;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class LoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView loadMore;
    private ImageView success;
    private ProgressBar progressBar;

    private int mFooterHeight;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        loadMore = (TextView) findViewById(R.id.loadMore);
        success = (ImageView) findViewById(R.id.success);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        success.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            success.setVisibility(GONE);
            progressBar.setVisibility(GONE);
            if (-y >= mFooterHeight) {
                loadMore.setText("准备加载更多");
            } else {
                loadMore.setText("点击加载更多");
            }
        }
    }

    @Override
    public void onLoadMore() {
        loadMore.setText("加载更多");
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(GONE);
        success.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        success.setVisibility(GONE);
    }
}
