package cn.hugeterry.updatefun.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.hugeterry.updatefun.R;
import cn.hugeterry.updatefun.config.DownloadKey;
import cn.hugeterry.updatefun.module.Download;

/**
 * Created by hugeterry(http://hugeterry.cn)
 */

public class DownLoadDialog extends Activity {
    private ImageView close;
    public ProgressBar progressBar;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_dialog);

        close = (ImageView) findViewById(R.id.downloaddialog_close);
        progressBar = (ProgressBar) findViewById(R.id.downloaddialog_progress);
        textView = (TextView) findViewById(R.id.downloaddialog_count);

        if (DownloadKey.interceptFlag) DownloadKey.interceptFlag = false;
        new Download(this).start();

       /* close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DownloadKey.TOShowDownloadView = 1;
                DownloadKey.interceptFlag = true;
                if (DownloadKey.ISManual) {
                    DownloadKey.LoadManual = false;
                }
                finish();
            }
        })*/;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}