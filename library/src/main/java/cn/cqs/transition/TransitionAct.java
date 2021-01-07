package cn.cqs.transition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

public class TransitionAct extends AppCompatActivity {
    /**
     * 基本配置
     */
    private static final String TRANSITION_NAME = "transition_name";
    private static final String TRANSITION_MASK = "transition_mask";
    private static final String TRANSITION_ACTIVITY_FINISH = "activity_finish";
    private boolean isFinish = false;
    //页面跳转的时间
    private long mTransitionDuration = 300;
    private MaskView mMaskView;
    private String mFromActivityTransitionName;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCurrentTransition();
    }
    /**
     * 当前的Transition信息初始化
     */
    private void initCurrentTransition() {
        Intent intent = getIntent();
        if (intent != null){
            mFromActivityTransitionName = intent.getStringExtra(TRANSITION_NAME);
        }
        if (TextUtils.isEmpty(mFromActivityTransitionName)){
            //默认格式: MainActivity_1609989621692
            mFromActivityTransitionName = getClass().getSimpleName() +"_"+ System.currentTimeMillis();
        }
    }

    /**
     * 是否需要滑动删除
     * @return
     */
    protected boolean canSlideFinishPage(){
        return false;
    }

    /**
     * 执行页面跳转动画
     */
    private void startTransitionAnim() {
        final MTransition transition = MTransitionManager.getInstance().getTransition(mFromActivityTransitionName);
        if (transition != null && mMaskView != null) {
            transition.toPage().setContainer(mMaskView, new ITransitPrepareListener() {
                @Override
                public void onPrepare(MTransitionView container) {
                    int width = container.getWidth();
                    transition.fromPage().getContainer().translateX(0, -width / 4);
                    if (transition.fromPage().getTransitionView(TRANSITION_MASK) != null) {
                        transition.fromPage().getTransitionView(TRANSITION_MASK).alpha(0f, 1f);
                    }
                    container.translateX(container.getWidth(), 0);
                    transition.setDuration(mTransitionDuration);
                    transition.start();
                }
            });
            transition.setOnTransitListener(new TransitListenerAdapter() {
                @Override
                public void onTransitEnd(MTransition transition, boolean reverse) {
                    if (reverse) {
                        finish();
                        MTranstionUtil.removeActivityAnimation(TransitionAct.this);
                    } else {
                        //解决关闭当前的Activity的做法
                        final MTransition formTransition = MTransitionManager.getInstance().getTransition(mFromActivityTransitionName);
                        if (formTransition != null){
                            boolean isFinish = formTransition.getBundle().getBoolean(TRANSITION_ACTIVITY_FINISH, false);
                            if (isFinish && formTransition.fromPage().getContainer().getSourceView().getContext() instanceof Activity){
                                Activity activity = (Activity) formTransition.fromPage().getContainer().getSourceView().getContext();
                                if (activity != null && !activity.isFinishing()){
                                    activity.finish();
                                    MTransitionManager.getInstance().destoryTransition(mFromActivityTransitionName);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initAfterSetContentView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initAfterSetContentView();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        initAfterSetContentView();
    }

    public void startActivity(Intent intent,boolean isFinish) {
        this.isFinish = isFinish;
        this.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        beforeStartActivity(intent);
        super.startActivityForResult(intent, requestCode, options);
        MTranstionUtil.removeActivityAnimation(this);
    }

    private void beforeStartActivity(Intent intent) {
        intent.putExtra(TRANSITION_NAME,mFromActivityTransitionName);
        if (mMaskView != null) {
            final MTransition transition = MTransitionManager.getInstance().createTransition(mFromActivityTransitionName);
            transition.getBundle().putBoolean(TRANSITION_ACTIVITY_FINISH,isFinish);
            transition.fromPage().setContainer(mMaskView, new ITransitPrepareListener() {
                @Override
                public void onPrepare(MTransitionView container) {
                    if (mMaskView.getMaskView() != null) {
                        transition.fromPage().addTransitionView(TRANSITION_MASK, mMaskView.getMaskView());
                    }
                }
            });
        }
    }

    private void initAfterSetContentView() {
        ViewGroup rootView = (ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null && rootView.getChildCount() != 0) {
            View container = rootView.getChildAt(0);
            rootView.removeView(container);
            mMaskView = new MaskView(this,mFromActivityTransitionName,container);
            //是否支持侧滑删除页面
            mMaskView.setSupportSlide(canSlideFinishPage());
            rootView.addView(mMaskView);
            startTransitionAnim();
        }
    }

    @Override
    public void onBackPressed() {
        final MTransition transition = MTransitionManager.getInstance().getTransition(mFromActivityTransitionName);
        if (transition != null) {
            transition.reverse();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition(mFromActivityTransitionName);
    }
}
