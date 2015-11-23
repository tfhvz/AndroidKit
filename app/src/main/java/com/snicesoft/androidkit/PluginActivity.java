package com.snicesoft.androidkit;

import android.app.ProgressDialog;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snicesoft.basekit.LogKit;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.pluginkit.PluginManager;
import com.snicesoft.pluginkit.environment.PlugInfo;
import com.snicesoft.viewbind.annotation.Id;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;

import java.io.File;
import java.util.Collection;

/**
 * Created by zhuzhe on 15/11/23.
 */
@Layout(R.layout.activity_plugin)
public class PluginActivity extends AKActivity<PluginActivity.Holder, Void> {
    private static final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

    public class Holder extends IHolder {
        @Id(R.id.pluglist)
        ListView plugListView;
        @Id(R.id.pluginDirTxt)
        TextView pluginDirTxt;
        PlugListViewAdapter adapter;
        private PluginManager plugMgr;

        @Override
        public void initViewParams() {
            plugMgr = PluginManager.getSingleton();
            String pluginSrcDir = sdcard + "/Plugin/";
            pluginDirTxt.setText(pluginSrcDir);
            adapter = new PlugListViewAdapter(getBaseContext());
            LogKit.d("===init");
            plugListView.setAdapter(adapter);
            plugListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    plugItemClick(position);
                }
            });
        }

        private void plugItemClick(int position) {
            PlugInfo plug = adapter.getItem(position);
            plugMgr.startMainActivity(getBaseContext(), plug);
        }

        private void setPlugins(final Collection<PlugInfo> plugs) {
            if (plugs == null || plugs.isEmpty()) {
                return;
            }
            adapter.setDataList(plugs);
        }
    }

    private boolean plugLoading = false;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pluginLoader:
                final String dirText = _holder.pluginDirTxt.getText().toString().trim();
                if (TextUtils.isEmpty(dirText)) {
                    Toast.makeText(this, getString(R.string.pl_dir), Toast.LENGTH_LONG).show();
                    return;
                }
                if (plugLoading) {
                    Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_LONG).show();
                    return;
                }
                String strDialogTitle = getString(R.string.dialod_loading_title);
                String strDialogBody = getString(R.string.dialod_loading_body);
                final ProgressDialog dialogLoading = ProgressDialog.show(getBaseContext(), strDialogTitle, strDialogBody, true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        plugLoading = true;
                        try {
                            Collection<PlugInfo> plugs = _holder.plugMgr.loadPlugin(new File(dirText));
                            _holder.setPlugins(plugs);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dialogLoading.dismiss();
                        }
                        plugLoading = false;
                    }
                }).start();
                break;
        }
    }
}
