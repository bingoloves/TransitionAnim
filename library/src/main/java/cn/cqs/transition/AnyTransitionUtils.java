package cn.cqs.transition;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;

/**
 * Created by bingo on 2021/1/7.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 不依赖Activity的转场动画  意义不大，废弃
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/7
 */
@Deprecated
public class AnyTransitionUtils {
    private static AnyTransitionUtils transitionUtils;
    private AnyTransitionUtils(){}
    public static AnyTransitionUtils getInstance(){
        if (transitionUtils == null){
            synchronized (AnyTransitionUtils.class){
                if (transitionUtils == null){
                    transitionUtils = new AnyTransitionUtils();
                }
            }
        }
        return transitionUtils;
    }

    /**
     * 获取Activity容器
     * @param activity
     * @return
     */
    public View getActivityContainer(Activity activity){
        ViewGroup rootView = (ViewGroup)activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null && rootView.getChildCount() != 0) {
            return rootView.getChildAt(0);
        }
        return null;
    }
    /**
     * 执行创建Transition
     * @param name
     * @param container
     * @param prepareListener
     */
    public void start(String name, View container, final OnTransitionPrepareListener prepareListener){
        if (name == null) return;
        final MTransition transition = MTransitionManager.getInstance().createTransition(name);
        transition.fromPage().setContainer(container, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                if (prepareListener != null) prepareListener.onPrepare(container,transition);
            }
        });
    }

    /**
     * 目标页面获取Transition
     * @param name
     * @param container
     * @param prepareListener
     */
    public void receive(String name, View container, final OnTransitionPrepareListener prepareListener){
        if (name == null) return;
        final MTransition transition = MTransitionManager.getInstance().getTransition(name);
        if (transition != null){
            transition.toPage().setContainer(container, new ITransitPrepareListener() {
                @Override
                public void onPrepare(MTransitionView container) {
                    if (prepareListener != null) prepareListener.onPrepare(container,transition);
                }
            });
        }
    }

    /**
     * 反向执行动画
     */
    public void reverse(String name) {
        MTransition transition = MTransitionManager.getInstance().getTransition(name);
        if (transition != null)transition.reverse();
    }

    /**
     * 销毁
     * @param name
     */
    public void destory(String name) {
        if (name != null)MTransitionManager.getInstance().destoryTransition(name);
    }
}
