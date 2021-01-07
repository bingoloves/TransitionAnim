package cn.cqs.transitiondemo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.cqs.transition.TransitionAct;

public class TwoActivity extends TransitionAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean canSlideFinishPage() {
        return true;
    }

    public void onBack(View view){
        onBackPressed();
    }
}
