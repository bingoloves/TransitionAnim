package cn.cqs.transition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;

/**
 * Created by bingo on 2021/1/7.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 侧滑遮罩层
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/7
 */
public class MaskView extends FrameLayout {
    private String mTransitionName;
    private int mDownX = 0;
    private boolean mHasBeginDrag = false;
    /**
     * 遮罩层背景
     */
    private View mMaskBackgroundView;
    /**
     * 外部正常的页面容器
     */
    private View mContainer;
    /**
     * 是否支持滑动
     */
    private boolean isSupportSlide = false;

    public MaskView(Context context,String transitionName,View container) {
        super(context);
        this.mTransitionName = transitionName;
        this.mContainer = container;
        wrapperView();
    }

    /**
     * View组合
     */
    private void wrapperView() {
        mMaskBackgroundView = new View(getContext());
        mMaskBackgroundView.setBackgroundColor(0xfffffea0);//0x88000000
        mMaskBackgroundView.setAlpha(0f);
        this.addView(mContainer);
        this.addView(mMaskBackgroundView);
    }

    public View getMaskView() {
        return mMaskBackgroundView;
    }

    public void setSupportSlide(boolean supportSlide) {
        isSupportSlide = supportSlide;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final MTransition transition = MTransitionManager.getInstance().getTransition(mTransitionName);
        if (transition != null && isSupportSlide) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                mHasBeginDrag = false;
                mDownX = (int) event.getX();
                if (mDownX < 100) {
                    transition.onBeginDrag();
                    mHasBeginDrag = true;
                }
            }
            if (mHasBeginDrag) {
                return true;
            } else {
                return super.onInterceptTouchEvent(event);
            }
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final MTransition transition = MTransitionManager.getInstance().getTransition(mTransitionName);
        if (transition != null && isSupportSlide) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_MOVE) {
                if (mHasBeginDrag) {
                    int delta = (int)(event.getX() - mDownX);
                    transition.setProgress(1f - delta / (float)getMeasuredWidth());
                }
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                if (mHasBeginDrag) {
                    int delta = (int)(event.getX() - mDownX);
                    float progress = 1f - delta / (float)getMeasuredWidth();
                    if (progress < 0.7f) {
                        transition.gotoCeil();
                    } else {
                        transition.gotoFloor();
                    }
                }
            }
            if (mHasBeginDrag) {
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        } else {
            return super.onTouchEvent(event);
        }
    }
}
