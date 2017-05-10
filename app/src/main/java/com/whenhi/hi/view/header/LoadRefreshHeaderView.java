package com.whenhi.hi.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.whenhi.hi.R;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class LoadRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private ImageView arrow;

    private ImageView success;

    private TextView refresh;

    private ProgressBar progressBar;

    private int mHeaderHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public LoadRefreshHeaderView(Context context) {
        this(context, null);
    }

    public LoadRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        refresh = (TextView) findViewById(R.id.refresh);
        arrow = (ImageView) findViewById(R.id.arrow);
        success = (ImageView) findViewById(R.id.success);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void onRefresh() {
        success.setVisibility(GONE);
        arrow.clearAnimation();
        arrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        refresh.setText("正在更新");
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            arrow.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            success.setVisibility(GONE);
            refresh.setVisibility(VISIBLE);
            if (y > mHeaderHeight) {
                refresh.setText("准备更新");
                if (!rotated) {
                    arrow.clearAnimation();
                    arrow.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    arrow.clearAnimation();
                    arrow.startAnimation(rotateDown);
                    rotated = false;
                }

                refresh.setText("点击刷新");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        rotated = false;
        refresh.setVisibility(VISIBLE);
        arrow.clearAnimation();
        arrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        refresh.setText("更新完成");
    }

    @Override
    public void onReset() {
        rotated = false;
        refresh.setVisibility(GONE);
        arrow.clearAnimation();
        arrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

}
