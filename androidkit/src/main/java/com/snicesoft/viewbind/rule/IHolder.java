package com.snicesoft.viewbind.rule;

import android.view.View;

/**
 * @author zhe
 * @since 2015年4月15日 上午9:54:17
 * @version V1.0
 */
public abstract class IHolder<D> {
	public abstract void initViewParams();

	private int position = -1;
	private D tag;
	private View parent;

	public final void setTag(D tag) {
		this.tag = tag;
	}

	public final D getTag() {
		return tag;
	}

	public final void setPosition(int position) {
		this.position = position;
	}

	public final int getPosition() {
		return position;
	}

	public final View getParent() {
		return parent;
	}

	public final void setParent(View parent) {
		this.parent = parent;
	}
}