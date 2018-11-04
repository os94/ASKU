package kr.ac.korea.lecturestalk.kulecturestalk;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MsgTabActivity extends AppCompatActivity implements View.OnClickListener{

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;

    private Button bt_tabMsgRecv, bt_tabMsgSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_tab);

        this.setTitle("받은 쪽지함");

        // 위젯에 대한 참조
        bt_tabMsgRecv = (Button)findViewById(R.id.bt_tabMsgRecv);
        bt_tabMsgSent = (Button)findViewById(R.id.bt_tabMsgSent);

        // 탭 버튼에 대한 리스너 연결
        bt_tabMsgRecv.setOnClickListener(this);
        bt_tabMsgSent.setOnClickListener(this);

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tabMsgRecv :
                // '버튼1' 클릭 시 '받은 쪽지함' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.bt_tabMsgSent :
                // '버튼2' 클릭 시 '보낸 쪽지함' 호출
                callFragment(FRAGMENT2);
                break;
        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '받은 쪽지함' 호출
                MsgRecvActivity fragmentMsgRecv = new MsgRecvActivity();
                transaction.replace(R.id.fragment_container, fragmentMsgRecv);
                transaction.commit();
                this.setTitle("받은 쪽지함");
                break;

            case 2:
                // '보낸 쪽지함' 호출
                MsgSentActivity fragmentMsgSent = new MsgSentActivity();
                transaction.replace(R.id.fragment_container, fragmentMsgSent);
                transaction.commit();
                this.setTitle("보낸 쪽지함");
                break;
        }

    }
}
