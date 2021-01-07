package cn.cqs.transition;

import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionView;

/**
 * @author bingo
 */
@Deprecated
public interface OnTransitionPrepareListener {
    void onPrepare(MTransitionView container, MTransition transition);
}
