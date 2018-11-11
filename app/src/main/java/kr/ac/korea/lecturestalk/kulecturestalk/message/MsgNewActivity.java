package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.korea.lecturestalk.kulecturestalk.R;

public class MsgNewActivity extends AppCompatActivity {
    private static final String TAG = MsgNewActivity.class.getSimpleName();

    private MsgDbOpenHelper mDbOpenHelper;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_new);

        this.setTitle("쪽지 보내기");

        mDbOpenHelper = new MsgDbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        //쪽지 보내기.
        final EditText editReceiver = (EditText) findViewById(R.id.id_edit_receiver);
        final EditText editMsg = (EditText) findViewById(R.id.id_edit_msg);

        mFirebaseAuth = FirebaseAuth.getInstance();
        String displayName = mFirebaseAuth.getCurrentUser().getDisplayName();
        if (displayName == null) displayName = "";
        final String sender = displayName;

        (this.findViewById(R.id.id_msg_send_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //쪽지 등록.
                String receiver = editReceiver.getText().toString();
                String  message = editMsg.getText().toString();
                mDbOpenHelper.open();
                mDbOpenHelper.insertColumn(sender, receiver, message);

                //현재 화면을 닫고, 쪽지함 목록으로 이동.
                Intent intent = new Intent(MsgNewActivity.this, MsgTabActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(intent));
            }
        });
    }
}
