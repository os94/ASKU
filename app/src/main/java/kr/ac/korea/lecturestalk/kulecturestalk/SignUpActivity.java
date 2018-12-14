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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final int PASSWORD_CHARACTERS_AT_LEAST = 6;
    private FirebaseAuth mFirebaseAuth;
    private static String TAG = "setInitialPoint";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        final EditText edittext_login_id = (EditText) findViewById(R.id.edittext_login_id);
        final EditText edittext_password = (EditText) findViewById(R.id.signup_edittext_password);
        edittext_password.setHint(getString(R.string.signup_password_edittext_hint, PASSWORD_CHARACTERS_AT_LEAST));

        final EditText edittext_password_confirm = (EditText) findViewById(R.id.edittext_password_confirm);

        Button signup_button = (Button) findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idText = edittext_login_id.getText().toString().trim();
                String passText = edittext_password.getText().toString();
                String confirmPassText = edittext_password_confirm.getText().toString();

                if (TextUtils.isEmpty(idText)) {
                    // 아이디 입력을 아무것도 안했으면 토스트 출력
                    Toast.makeText(SignUpActivity.this, R.string.signup_toast_input_id, Toast.LENGTH_SHORT).show();
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
                                        //Default로 UserID의 이름 설정.
                                        String[] nameList = idText.split("@");
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nameList[0])
                                                .build();
                                        mFirebaseAuth.getCurrentUser().updateProfile(profileUpdates);

                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        Toast.makeText(SignUpActivity.this, "가입 완료", Toast.LENGTH_SHORT).show();
                                        setInitialPoint(idText);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            Toast.makeText(SignUpActivity.this, "비밀번호가 간단해요..", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            Toast.makeText(SignUpActivity.this, "email 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            Toast.makeText(SignUpActivity.this, R.string.signup_toast_already_used_id, Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();}
                                    }
                                }
                            });
                }
            }
        });

    }

    public void setInitialPoint(String mail) {
        Map<String, Object> data = new HashMap<>();
        data.put("point", 200);
        int idx = mail.indexOf("@");
        String id = mail.substring(0, idx);
        db.collection("Point").document(id).set(data);
    }

}
