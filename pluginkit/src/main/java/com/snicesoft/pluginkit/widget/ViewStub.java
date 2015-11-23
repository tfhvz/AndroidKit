/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snicesoft.pluginkit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RemoteViews.RemoteView;

import java.lang.ref.WeakReference;

@RemoteView
public final class ViewStub extends View {
    private int mLayoutResource = 0;
    private int mInflatedId;

    private WeakReference<View> mInflatedViewRef;

    private LayoutInflater mInflater;
    private OnInflateListener mInflateListener;

    public ViewStub(Context context) {
        super(context);
        initialize(context);
    }

    /**
     * Creates a new ViewStub with the specified layout resource.
     *
     * @param context        The application's environment.
     * @param layoutResource The reference to a layout resource that will be inflated.
     */
    public ViewStub(Context context, int layoutResource) {
        super(context);
        mLayoutResource = layoutResource;
        initialize(context);
    }

    public ViewStub(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewStub(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        int[] attrsArr = null;
        int index = 0;
        int layoutId = 0;
        int[] attrViewArr = null;
        int viewId = 0;
        try {
            Class<?> styleableClass = Class
                    .forName("com.android.internal.R$styleable");
            attrsArr = (int[]) styleableClass.getField("ViewStub").get(null);
            index = styleableClass.getField("ViewStub_inflatedId").getInt(null);
            layoutId = styleableClass.getField("ViewStub_layout").getInt(null);
            attrViewArr = (int[]) styleableClass.getField("View").get(null);
            viewId = styleableClass.getField("View_id").getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TypedArray a = context.obtainStyledAttributes(attrs, attrsArr,
                defStyle, 0);

        mInflatedId = a.getResourceId(index, NO_ID);
        mLayoutResource = a.getResourceId(layoutId, 0);

        a.recycle();
        a = context.obtainStyledAttributes(attrs, attrViewArr, defStyle, 0);
        setId(a.getResourceId(viewId, NO_ID));
        a.recycle();

        initialize(context);
    }

    private void initialize(Context context) {
        setVisibility(GONE);
        setWillNotDraw(true);
    }

    public int getInflatedId() {
        return mInflatedId;
    }

    public void setInflatedId(int inflatedId) {
        mInflatedId = inflatedId;
    }

    public int getLayoutResource() {
        return mLayoutResource;
    }

    public void setLayoutResource(int layoutResource) {
        mLayoutResource = layoutResource;
    }

    public void setLayoutInflater(LayoutInflater inflater) {
        mInflater = inflater;
    }

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
    }

    /**
     * When visibility is set to {@link #VISIBLE} or {@link #INVISIBLE},
     * {@link #inflate()} is invoked and this StubbedView is replaced in its
     * parent by the inflated layout resource. After that calls to this function
     * are passed through to the inflated view.
     *
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     * @see #inflate()
     */
    @Override
    // @android.view.RemotableViewMethod
    public void setVisibility(int visibility) {
        if (mInflatedViewRef != null) {
            View view = mInflatedViewRef.get();
            if (view != null) {
                view.setVisibility(visibility);
            } else {
                throw new IllegalStateException(
                        "setVisibility called on un-referenced view");
            }
        } else {
            super.setVisibility(visibility);
            if (visibility == VISIBLE || visibility == INVISIBLE) {
                inflate();
            }
        }
    }

    /**
     * Inflates the layout resource identified by {@link #getLayoutResource()}
     * and replaces this StubbedView in its parent by the inflated layout
     * resource.
     *
     * @return The inflated layout resource.
     */
    public View inflate() {
        final ViewParent viewParent = getParent();

        if (viewParent != null && viewParent instanceof ViewGroup) {
            if (mLayoutResource != 0) {
                final ViewGroup parent = (ViewGroup) viewParent;
                final LayoutInflater factory;
                if (mInflater != null) {
                    factory = mInflater;
                } else {
                    factory = LayoutInflater.from(getContext());
                }
                final View view = factory.inflate(mLayoutResource, parent,
                        false);

                if (mInflatedId != NO_ID) {
                    view.setId(mInflatedId);
                }

                final int index = parent.indexOfChild(this);
                parent.removeViewInLayout(this);

                final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    parent.addView(view, index, layoutParams);
                } else {
                    parent.addView(view, index);
                }

                mInflatedViewRef = new WeakReference<View>(view);

                if (mInflateListener != null) {
                    mInflateListener.onInflate(this, view);
                }

                return view;
            } else {
                throw new IllegalArgumentException(
                        "ViewStub must have a valid layoutResource");
            }
        } else {
            throw new IllegalStateException(
                    "ViewStub must have a non-null ViewGroup viewParent");
        }
    }

    /**
     * Specifies the inflate listener to be notified after this ViewStub
     * successfully inflated its layout resource.
     *
     * @param inflateListener The OnInflateListener to notify of successful inflation.
     * @see android.view.ViewStub.OnInflateListener
     */
    public void setOnInflateListener(OnInflateListener inflateListener) {
        mInflateListener = inflateListener;
    }

    /**
     * Listener used to receive a notification after a ViewStub has successfully
     * inflated its layout resource.
     *
     * @see android.view.ViewStub#setOnInflateListener(android.view.ViewStub.OnInflateListener)
     */
    public static interface OnInflateListener {
        /**
         * Invoked after a ViewStub successfully inflated its layout resource.
         * This method is invoked after the inflated view was added to the
         * hierarchy but before the layout pass.
         *
         * @param stub     The ViewStub that initiated the inflation.
         * @param inflated The inflated View.
         */
        void onInflate(ViewStub stub, View inflated);
    }
}
