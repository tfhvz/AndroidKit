package com.snicesoft.viewbind.rule;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;

public class RecyclerHolder extends RecyclerView.ViewHolder {
    public ViewFinder finder;

    public RecyclerHolder(View itemView) {
        super(itemView);
        finder = new ViewFinder(itemView);
        AVKit.bind(this, finder);
    }
}
