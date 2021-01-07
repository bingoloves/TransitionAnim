package cn.cqs.transitiondemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.cqs.transition.TransitionAct;

public class MainActivity extends TransitionAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void moveTwo(View view){
        startActivity(new Intent(this,TwoActivity.class));
//        startActivity(new Intent(this,TwoActivity.class),true);
    }
}
