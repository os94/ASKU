package kr.ac.korea.lecturestalk.kulecturestalk;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebViewActivity extends AppCompatActivity {
    public static final String EVERYTIME_URL = "https://everytime.kr/timetable";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.addJavascriptInterface(new MyJavaScriptInterface(),"HTMLOUT");
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
        webView.loadUrl(EVERYTIME_URL);
    }
}

class HelloWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        view.loadUrl(url);
        Log.d("HelloWebViewClient", "url - " + url);
//        if (WebViewActivity.EVERYTIME_URL.equals(url)) {
//            Log.d("HelloWebViewClient", "This is EVERYTIME_URL. ");
//            view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
//        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // This call inject JavaScript into the page which just finished loading.

        Log.d("HelloWebViewClient 2", "url - " + url);
        if (WebViewActivity.EVERYTIME_URL.equals(url)) {
            Log.d("HelloWebViewClient 2", "This is EVERYTIME_URL. ");
            view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
//
        }

        super.onPageFinished(view, url);

    }

}


class MyJavaScriptInterface {
    MyJavaScriptInterface() {

    }

    @android.webkit.JavascriptInterface
    public void getHtml(String html) {
        Log.d("MyJavaScriptInterface", "Here is the value from html::" + html);

        /** DROPBOX */
        Document doc = Jsoup.parse(html);
        // <div id="email">다음 이메일로 로그인함: lhjnano@gmail.com</div> Elements emails = doc.select("div#email");
//        Elements emails = doc.select("div#container");
        Elements emails = doc.select("div[class=tablebody");
        Log.d("tablebody", emails.text());

    }

}