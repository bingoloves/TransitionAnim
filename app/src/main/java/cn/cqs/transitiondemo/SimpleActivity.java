package cn.cqs.transitiondemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import cn.cqs.transition.AnyTransitionUtils;
import cn.cqs.transition.OnTransitionPrepareListener;

public class SimpleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
    }
    public void moveTwo(View view){
//        AnyTransitionUtils.getInstance().start("simple", findViewById(R.id.content), new OnTransitionPrepareListener() {
//            @Override
//            public void onPrepare(MTransitionView container, MTransition transition) {
//
//            }
//        });
        final MTransition transition = MTransitionManager.getInstance().createTransition("simple");
        transition.fromPage().setContainer(findViewById(R.id.content), new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
            }
        });
        startActivity(new Intent(this,SimpleTwoActivity.class));
        MTranstionUtil.removeActivityAnimation(this);
    }
}
