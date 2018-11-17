package kr.ac.korea.lecturestalk.kulecturestalk.schedule;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScheduleJavascriptInterface {
    String mText;

    @android.webkit.JavascriptInterface
    public void getHtml(String html) {
        //Log.d("MyJavaScriptInterface", "Here is the value from html::" + html);

        Document doc = Jsoup.parse(html);
        Elements cols = doc.select("div.subject");
        Log.d("MyJavaScriptInterface", "cols size : " + cols.size());
        if (cols.size() == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        boolean failed = false;
        for (int i = 0; i < cols.size(); i++) {
            Elements el = cols.get(i).getAllElements();
            Log.d("MyJavaScriptInterface", "el " + i + " : " + el.size());
            String subject = el.select("h3").text();
            String professor = el.select("p em").text();
            String room = el.select("p span").text();

            if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(professor) && !TextUtils.isEmpty(room)) {
                sb.append(subject);
                sb.append("/");
                sb.append(professor);
                sb.append("/");
                sb.append(room);
                sb.append("\n");
            } else {
                // all of subjects 중에 하나라도 missing data가 있다면 mText 업데이트하지 않음.
                failed = true;
            }
        }

        if (!failed) {
            mText = sb.toString();
        }
    }

    public String getText() {
        if (mText == null) {
            return "";
        }
        return mText;
    }
}