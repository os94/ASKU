package kr.ac.korea.lecturestalk.kulecturestalk.schedule;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import kr.ac.korea.lecturestalk.kulecturestalk.IntroActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.LoginActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.MainActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.R;

public class ScheduleWebViewClient extends WebViewClient {
    private ProgressBar progressBar;

    public ScheduleWebViewClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        Log.d("ScheduleWebViewClient", "url - " + url);
        return true;
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        // This call inject JavaScript into the page which just finished loading.
        Log.d("ScheduleWebViewClient 2", "url - " + url);

        if (WebViewActivity.EVERYTIME_URL.equals(url)) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d("ScheduleWebViewClient 2", "This is EVERYTIME_URL. ");
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (view != null) {
                        view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            };
            handler.sendEmptyMessageDelayed(0, 3000);
        }

        super.onPageFinished(view, url);

    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        Log.d("ScheduleWebViewClient 3", "url - " + url);
        super.onPageCommitVisible(view, url);
    }
}
