package kr.ac.korea.lecturestalk.kulecturestalk.schedule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import kr.ac.korea.lecturestalk.kulecturestalk.R;

public class WebViewActivity extends AppCompatActivity {
    public static final String EVERYTIME_URL = "https://everytime.kr/timetable";
    private WebView mWebView;
    private MyJavaScriptInterface mJavaScriptInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mJavaScriptInterface = new MyJavaScriptInterface();

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new HelloWebViewClient(WebViewActivity.this));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(mJavaScriptInterface, "Android");
        mWebView.loadUrl(EVERYTIME_URL);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                String scheduleText = mJavaScriptInterface.getText();
                if (!TextUtils.isEmpty(scheduleText)) {
                    SharedPreferences setting = getSharedPreferences("setting", 0);
                    setting.edit().putString("scheduleList", scheduleText).commit();

                    Intent intent = new Intent(WebViewActivity.this, TestActivity.class);
                    intent.putExtra("text", scheduleText);
                    startActivity(intent);
                } else {
                    Toast.makeText(WebViewActivity.this, "시간표가 보여질 때 누르세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

class HelloWebViewClient extends WebViewClient {
    Activity activity;

    public HelloWebViewClient(Activity activity) {
        this.activity = activity;
    }

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