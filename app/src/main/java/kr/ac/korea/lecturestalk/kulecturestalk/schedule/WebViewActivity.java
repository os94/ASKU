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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.ac.korea.lecturestalk.kulecturestalk.MainActivity;
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
        mWebView.setWebViewClient(new ScheduleWebViewClient(WebViewActivity.this));
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

                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(WebViewActivity.this, "시간표가 보여질 때 누르세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

class ScheduleWebViewClient extends WebViewClient {
    Activity activity;

    public ScheduleWebViewClient(Activity activity) {
        this.activity = activity;
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

class MyJavaScriptInterface {
    StringBuilder mText;

    MyJavaScriptInterface() {
    }

    @android.webkit.JavascriptInterface
    public void getHtml(String html) {
        mText = new StringBuilder();
        Log.d("MyJavaScriptInterface", "Here is the value from html::" + html);

        Document doc = Jsoup.parse(html);
        Elements tablebody = doc.select("div[class=tablebody");
        Elements cols = tablebody.select("div[class=cols");
        for (int i = 1; i <= 10; i++) {
            Element subject = cols.select("div[class=subject color" + i).get(0);
            mText.append(subject.select("h3").text()); // subject
            mText.append("/");
            mText.append(subject.select("em").text()); // professor
            mText.append("/");
            mText.append(subject.select("span").text()); // room
            mText.append("\n");
        }

    }

    public String getText() {
        if (mText == null) {
            return "";
        }
        return mText.toString();
    }
}