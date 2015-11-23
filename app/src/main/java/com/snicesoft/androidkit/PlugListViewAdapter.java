package com.snicesoft.androidkit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.snicesoft.pluginkit.environment.PlugInfo;
import com.snicesoft.viewbind.annotation.Id;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;
import com.snicesoft.viewbind.widget.AVAdapter;

/**
 * Created by zhuzhe on 15/11/23.
 */
@Layout(R.layout.plug_item)
public class PlugListViewAdapter extends AVAdapter<PlugListViewAdapter.Holder, PlugInfo> {
    public PlugListViewAdapter(Context context) {
        super(context);
    }

    public class Holder extends IHolder<PlugInfo> {
        @Id(R.id.plug_title)
        TextView plug_title;
        @Id(R.id.plug_description)
        TextView plug_description;
        @Id(R.id.plug_icon)
        ImageView plug_icon;

        @Override
        public void initViewParams() {
            PlugInfo plug = getTag();
            {
                int labelRes = plug.getPackageInfo().applicationInfo.labelRes;
                if (labelRes != 0) {
                    String label = plug.getResources().getString(labelRes);
                    plug_title.setText(label);
                } else {
                    CharSequence label = plug.getPackageInfo().applicationInfo
                            .loadLabel(getContext().getPackageManager());
                    if (label != null) {
                        plug_title.setText(label);
                    }
                }
            }
            Drawable drawable = plug.getResources().getDrawable(
                    plug.getPackageInfo().applicationInfo.icon);
            plug_icon.setImageDrawable(drawable);
            String descText;
            int descId = plug.getPackageInfo().applicationInfo.descriptionRes;
            if (descId == 0) {
                descText = plug.getId();
            } else {
                descText = plug.getResources().getString(descId);
            }
            plug_description.setText(descText);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().hashCode();
    }
}
