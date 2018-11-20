package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.MainActivity;

import kr.ac.korea.lecturestalk.kulecturestalk.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MsgNewActivity extends AppCompatActivity {
    private static final String TAG = MsgNewActivity.class.getSimpleName();

    //private MsgDbOpenHelper mDbOpenHelper;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_new);

        this.setTitle("쪽지 보내기");

        //mDbOpenHelper = new MsgDbOpenHelper(this);
        //mDbOpenHelper.open();
        //mDbOpenHelper.create();

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
                /*
                mDbOpenHelper.open();
                mDbOpenHelper.insertColumn(sender, receiver, message);

                //현재 화면을 닫고, 쪽지함 목록으로 이동.
                Intent intent = new Intent(MsgNewActivity.this, MainActivity.class);
                intent.putExtra("initTab", "message");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(intent));
                */
                try {

                    MsgItem input = new MsgItem();
                    input.id = 0L;
                    input.sender = sender;
                    input.receiver = receiver;
                    input.message = message;

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(MsgService.URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MsgService msgService = retrofit.create(MsgService.class);
                    msgService.newMsg(input).enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            boolean bSuccess = response.isSuccessful();
                            Map<String, Object> body = new HashMap<String, Object>();
                            String result = "";
                            if (bSuccess) {
                                body = response.body();
                                result = (String) body.get("result");
                            }
                            if (response.isSuccessful() && result.equalsIgnoreCase("success")) {
                                //현재 화면을 닫고, 쪽지함 목록으로 이동.
                                Intent intent = new Intent(MsgNewActivity.this, MainActivity.class);
                                intent.putExtra("initTab", "message");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(new Intent(intent));
                            } else {
                                Toast.makeText(getApplicationContext(), "[오류] 메시지 등록 실패.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "메시지 등록 실패.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                } catch (Exception ex) {
                    Log.e("Insert Log", ex.toString());
                    Toast.makeText(getApplicationContext(), "메시지 등록 실패.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
