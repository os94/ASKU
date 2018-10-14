package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();

        TextView email = (TextView) findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());

        Log.d("getPhotoUrl", "" + mFirebaseAuth.getCurrentUser().getPhotoUrl());
        Log.d("getDisplayName", "" + mFirebaseAuth.getCurrentUser().getDisplayName());
        Log.d("getPhoneNumber", "" + mFirebaseAuth.getCurrentUser().getPhoneNumber());

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        Button LaunchWebView = (Button) findViewById(R.id.LaunchWebView);
        LaunchWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            }
        });
    }
}
