package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebViewActivity extends AppCompatActivity {
    public static final String EVERYTIME_URL = "https://everytime.kr/timetable";
    private WebView mWebView;
    private String mText = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new HelloWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        final MyJavaScriptInterface javaScriptInterface = new MyJavaScriptInterface();
        mWebView.addJavascriptInterface(javaScriptInterface, "Android");
        mWebView.loadUrl(EVERYTIME_URL);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebViewActivity.this, TestActivity.class);
                intent.putExtra("text", javaScriptInterface.getText());
                startActivity(intent);
            }
        });
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
    String mText;

    MyJavaScriptInterface() {
    }

    @android.webkit.JavascriptInterface
    public void getHtml(String html) {
        Log.d("MyJavaScriptInterface", "Here is the value from html::" + html);

        Document doc = Jsoup.parse(html);
        Elements tablebody = doc.select("div[class=tablebody");
        Elements cols = tablebody.select("div[class=cols");
        Elements sub1 = cols.select("div[class=subject color1");
        mText = sub1.get(0).text();

        Elements sub2 = cols.select("div[class=subject color2");
        mText += "\n\n";
        mText += sub2.get(0).text();

        Elements sub3 = cols.select("div[class=subject color3");
        mText += "\n\n";
        mText += sub3.get(0).text();

        Elements sub4 = cols.select("div[class=subject color4");
        mText += "\n\n";
        mText += sub4.get(0).text();

        Elements sub5 = cols.select("div[class=subject color5");
        mText += "\n\n";
        mText += sub5.get(0).text();
    }

    public String getText() {
        return mText;
    }
}