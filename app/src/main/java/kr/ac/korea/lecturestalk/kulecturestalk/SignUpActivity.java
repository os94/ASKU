package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private static final int PASSWORD_CHARACTERS_AT_LEAST = 6;
    private boolean mIsConfirmedId = false;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();


        final EditText edittext_login_id = (EditText) findViewById(R.id.edittext_login_id);
        edittext_login_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIsConfirmedId = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText edittext_password = (EditText) findViewById(R.id.signup_edittext_password);
        edittext_password.setHint(getString(R.string.signup_password_edittext_hint, PASSWORD_CHARACTERS_AT_LEAST));

        final EditText edittext_password_confirm = (EditText) findViewById(R.id.edittext_password_confirm);

        Button signup_id_check_button = (Button) findViewById(R.id.signup_id_check_button);
        signup_id_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edittext_login_id.getText().toString().trim())) {
                    // 아이디 입력을 아무것도 안했으면 토스트 출력
                    Toast.makeText(SignUpActivity.this, R.string.signup_toast_input_id, Toast.LENGTH_SHORT).show();
                    mIsConfirmedId = false;
                } else {
                    //todo request id check
                    boolean isIdInUse = false;
                    if (isIdInUse) {
                        Toast.makeText(SignUpActivity.this, R.string.signup_toast_already_used_id, Toast.LENGTH_SHORT).show();
                        mIsConfirmedId = false;
                    } else {
                        Toast.makeText(SignUpActivity.this, R.string.signup_toast_usable_used_id, Toast.LENGTH_SHORT).show();
                        mIsConfirmedId = true;
                    }
                }

            }
        });

        Button signup_button = (Button) findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = edittext_login_id.getText().toString().trim();
                String passText = edittext_password.getText().toString();
                String confirmPassText = edittext_password_confirm.getText().toString();

                if (TextUtils.isEmpty(idText)) {
                    // 아이디 입력을 아무것도 안했으면 토스트 출력
                    Toast.makeText(SignUpActivity.this, R.string.signup_toast_input_id, Toast.LENGTH_SHORT).show();
                } else if (!mIsConfirmedId) {
                    //아이디 중복확인이 안되었으면 중복확인 버튼 누르도록 유도하는 토스트 출력
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_toast_need_to_confirm_id,
                            getString(R.string.signup_id_duplication_check_button)), Toast.LENGTH_SHORT).show();
                } else if (passText.length() < PASSWORD_CHARACTERS_AT_LEAST) {
                    // 비밀번호 입력이 6자리 미만이면 토스트 출력
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_toast_password_chars, PASSWORD_CHARACTERS_AT_LEAST), Toast.LENGTH_SHORT).show();
                } else if (!passText.equals(confirmPassText)) {
                    // 비밀번호와 재확인용 비밀번호가 일치하지 않으면 토스트 출력
                    Toast.makeText(SignUpActivity.this, R.string.signup_toast_password_reconfirm, Toast.LENGTH_SHORT).show();
                } else {
                    mFirebaseAuth.createUserWithEmailAndPassword(idText, passText)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        Toast.makeText(SignUpActivity.this, "가입 완료", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                }
            }
        });

    }
}
