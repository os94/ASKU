package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kr.ac.korea.lecturestalk.kulecturestalk.R;


public class MsgTabFragment extends Fragment implements View.OnClickListener {
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;

    private ViewGroup bt_tabMsgRecv, bt_tabMsgSent;
    private View bt_tabMsgRecv_selected, bt_tabMsgSent_selected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_msg_tab, container, false);

        //setTitle("받은 쪽지함");

        // 위젯에 대한 참조
        bt_tabMsgRecv = view.findViewById(R.id.bt_tabMsgRecv);
        bt_tabMsgSent = view.findViewById(R.id.bt_tabMsgSent);
        bt_tabMsgRecv_selected = view.findViewById(R.id.bt_tabMsgRecv_selected);
        bt_tabMsgSent_selected = view.findViewById(R.id.bt_tabMsgSent_selected);

        // 탭 버튼에 대한 리스너 연결
        bt_tabMsgRecv.setOnClickListener(this);
        bt_tabMsgSent.setOnClickListener(this);

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        bt_tabMsgRecv.callOnClick();

        return view;
    }

    private void callFragment(int frament_no) {
        if (getActivity() == null) {
            return;
        }

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '받은 쪽지함' 호출
                MsgRecvActivity fragmentMsgRecv = new MsgRecvActivity();
                transaction.replace(R.id.msg_fragment_container, fragmentMsgRecv);
                transaction.commit();
                //this.setTitle("받은 쪽지함");
                break;

            case 2:
                // '보낸 쪽지함' 호출
                MsgSentActivity fragmentMsgSent = new MsgSentActivity();
                transaction.replace(R.id.msg_fragment_container, fragmentMsgSent);
                transaction.commit();
                //this.setTitle("보낸 쪽지함");
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_tabMsgRecv:
                // '버튼1' 클릭 시 '받은 쪽지함' 호출
                callFragment(FRAGMENT1);
                bt_tabMsgRecv_selected.setVisibility(View.VISIBLE);
                bt_tabMsgSent_selected.setVisibility(View.GONE);
                break;

            case R.id.bt_tabMsgSent:
                // '버튼2' 클릭 시 '보낸 쪽지함' 호출
                callFragment(FRAGMENT2);
                bt_tabMsgRecv_selected.setVisibility(View.GONE);
                bt_tabMsgSent_selected.setVisibility(View.VISIBLE);
                break;
        }
    }
}