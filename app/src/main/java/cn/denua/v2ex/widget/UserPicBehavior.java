/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.widget;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/*
 * 用于CoordinateLayout 的用户头像移动 behavior
 *
 * @author denua
 * @date 2018/11/19 20
 */
public class UserPicBehavior extends CoordinatorLayout.Behavior<CircleImageView> {


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
