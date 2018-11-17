package kr.ac.korea.lecturestalk.kulecturestalk.schedule;


import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ScheduleWebViewClient extends WebViewClient {

    public ScheduleWebViewClient() {
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        Log.d("ScheduleWebViewClient", "url - " + url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // This call inject JavaScript into the page which just finished loading.

        Log.d("ScheduleWebViewClient 2", "url - " + url);
        if (WebViewActivity.EVERYTIME_URL.equals(url)) {
            Log.d("ScheduleWebViewClient 2", "This is EVERYTIME_URL. ");
            view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
        }

        super.onPageFinished(view, url);

    }

}
