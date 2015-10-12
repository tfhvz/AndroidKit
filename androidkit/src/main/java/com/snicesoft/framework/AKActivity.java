package com.snicesoft.framework;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.snicesoft.viewbind.base.AvAppCompatActivity;
import com.snicesoft.viewbind.base.AvFragment;
import com.snicesoft.viewbind.rule.IHolder;

/**
 * Created by zhuzhe on 15/10/12.
 */
public class AKActivity<H extends IHolder, D> extends AvAppCompatActivity<H, D> {
    protected AvFragment<?, ?, ?> curFragment;

    public void openFragment(int id, AvFragment<?, ?, ?> targetFragment) {
        if (curFragment != null && curFragment == targetFragment)
            return;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(id, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onRefresh();
        }
        if (curFragment != null && curFragment.isVisible()) {
            transaction.hide(curFragment);
        }
        curFragment = targetFragment;
        transaction.commit();
    }

    public void replaceFragment(int id, AvFragment<?, ?, ?> targetFragment, boolean backStack) {
        FragmentUtil.replaceFragment(id, targetFragment, getSupportFragmentManager(), backStack);
        curFragment = targetFragment;
    }

    /**
     * click时间是否传递到Fragment
     *
     * @return
     */
    public boolean isClickFragment() {
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (curFragment != null && isClickFragment()) {
            curFragment.onClick(v);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMgr.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityMgr.removeActivity(this);
    }
}