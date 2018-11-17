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
    private ScheduleJavascriptInterface mJavaScriptInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mJavaScriptInterface = new ScheduleJavascriptInterface();

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new ScheduleWebViewClient());
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
