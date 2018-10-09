package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        if (intent != null) {
            String text = intent.getStringExtra("text");
            if (!TextUtils.isEmpty(text)) {
                TextView textView = (TextView) findViewById(R.id.textview);
                textView.setText(text);
            }
        }
    }
}
