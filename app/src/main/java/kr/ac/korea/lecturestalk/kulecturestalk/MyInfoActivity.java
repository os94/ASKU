package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MyInfoActivity extends AppCompatActivity {
    private static final String TAG = MyInfoActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        this.setTitle("내 정보 수정");

        mFirebaseAuth = FirebaseAuth.getInstance();

        final EditText edittext_name = (EditText) findViewById(R.id.edittext_name);
        edittext_name.setText(mFirebaseAuth.getCurrentUser().getDisplayName());

        final EditText edittext_login_id = (EditText) findViewById(R.id.edittext_login_id);
        edittext_login_id.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());
        edittext_login_id.setEnabled(false);

        final EditText edittext_uid = (EditText) findViewById(R.id.edittext_uid);
        edittext_uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());
        edittext_uid.setEnabled(false);

        final EditText edittext_password = (EditText) findViewById(R.id.edittext_password);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = edittext_name.getText().toString().trim();
                if (TextUtils.isEmpty(nameText)) {
                    Toast.makeText(MyInfoActivity.this, "사용자 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nameText)
                            .build();
                    mFirebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                    String passwordText = edittext_password.getText().toString().trim();
                    if (!TextUtils.isEmpty(passwordText)) {
                        try {
                            mFirebaseAuth.getCurrentUser().updatePassword(passwordText);
                            Toast.makeText(MyInfoActivity.this, "이름과 비밀번호를 성공적으로 수정했습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MyInfoActivity.this, MainActivity.class));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            Toast.makeText(MyInfoActivity.this, "비밀번호를 변경하는과정에서오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyInfoActivity.this, "이름을 성공적으로 수정했습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MyInfoActivity.this, MainActivity.class));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyInfoActivity.this, "이름을 변경하는과정에서오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                };
            }
        });

    }
}
