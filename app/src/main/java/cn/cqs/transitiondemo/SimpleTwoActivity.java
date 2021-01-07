package cn.cqs.transitiondemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

import cn.cqs.transition.AnyTransitionUtils;

public class SimpleTwoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_two);
        findViewById(R.id.content).setBackgroundColor(0xfffffea0);
        initTransition();
    }
    private String transitionName = "simple";
    private int mDownX = 0;
    private void initTransition() {
        final MTransition transition = MTransitionManager.getInstance().getTransition(transitionName);
        if (transition != null){
            transition.toPage().setContainer(findViewById(R.id.wrapper), new ITransitPrepareListener() {
                @Override
                public void onPrepare(MTransitionView container) {
                    final MTransitionView content = transition.toPage().addTransitionView("content", findViewById(R.id.content));
                    int width = container.getWidth();
                    transition.fromPage().getContainer().translateX(0, -width / 4);
                    content.translateX(container.getWidth(), 0);
                    container.alpha(0f, 1f);
                }
            });
            transition.setDuration(500);
            transition.start();
            transition.setOnTransitListener(new TransitListenerAdapter() {
                @Override
                public void onTransitEnd(MTransition transition, boolean reverse) {
                    if (reverse) {
                        finish();
                        MTranstionUtil.removeActivityAnimation(SimpleTwoActivity.this);
                    }
                }
            });
        }
        // 滑动返回
        findViewById(R.id.wrapper).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    transition.onBeginDrag();
                    mDownX = (int) event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int delta = (int) (event.getX() - mDownX);
                    transition.setProgress(1f - delta / (float) findViewById(R.id.wrapper).getMeasuredWidth());
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    int delta = (int) (event.getX() - mDownX);
                    float progress = 1f - delta / (float) findViewById(R.id.wrapper).getMeasuredWidth();
                    if (progress < 0.5f) {
                        transition.gotoCeil();
                    } else {
                        transition.gotoFloor();
                    }
                }
                return true;
            }
        });
    }

    public void onBack(View view){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnyTransitionUtils.getInstance().destory(transitionName);
    }

    @Override
    public void onBackPressed() {
        AnyTransitionUtils.getInstance().reverse(transitionName);
    }
}
