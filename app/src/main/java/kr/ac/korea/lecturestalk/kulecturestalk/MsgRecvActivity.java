package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MsgRecvActivity extends Fragment {

    long nowIndex;
    String sort = "sent_at";

    MsgListViewAdapter arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private MsgDbOpenHelper mDbOpenHelper;

    private FirebaseAuth mFirebaseAuth;
    private String mStrReceiver = "";

    public MsgRecvActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.activity_msg_recv, container, false);
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_msg_recv);
        View view = inflater.inflate(R.layout.activity_msg_recv, container, false);

        //사용자 이름 추출.
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStrReceiver = mFirebaseAuth.getCurrentUser().getDisplayName();

        //쪽지 보내기.
        (view.findViewById(R.id.id_new_msg_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), MsgNewActivity.class));
            }
        });

        //쪽지 삭제.
        (view.findViewById(R.id.id_delete_msg_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(ToDo)
            }
        });

        //검색.
        //(ToDo)


        //List 구성.
        ListView listview ;
        //MsgListViewAdapter arrayAdapter;

        // Adapter 생성
        arrayAdapter = new MsgListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.id_listview);
        listview.setAdapter(arrayAdapter);

        //DB에서 쪽지 목록을 읽어서 화면에 출력.
        final MsgTabActivity activity = (MsgTabActivity) getActivity();
        Resources res = ((Context)(activity)).getResources();

        mDbOpenHelper = new MsgDbOpenHelper(view.getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase(listview, mStrReceiver);
        int countItem = arrayAdapter.getCount();
        /*
        // 첫 번째 아이템 추가.
        arrayAdapter.addItem(res.getDrawable( R.drawable.baseline_accessibility_new_black_18dp),
                "Box", Calendar.getInstance().getTime(), "Account Box Black 36dp") ;
        // 두 번째 아이템 추가.
        arrayAdapter.addItem(res.getDrawable(R.drawable.baseline_account_box_black_18dp),
                "Circle", Calendar.getInstance().getTime(), "Account\n Circle\n Black\n 36dp") ;
        // 세 번째 아이템 추가.
        arrayAdapter.addItem(res.getDrawable(R.drawable.baseline_account_circle_black_18dp),
                "Ind", Calendar.getInstance().getTime(), "Assignment Ind Black 36dp") ;
        */

        //쪽지 없음 표시.
        TextView txtView = (TextView) view.findViewById(R.id.id_no_msg_recv);
        if (countItem > 0)
            txtView.setVisibility(View.INVISIBLE);
        else
            txtView.setVisibility(View.VISIBLE);

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                startActivity(new Intent(getActivity(), MsgNewActivity.class));
            }
        }) ;

        return view;
    }


    public void showDatabase(ListView listview, String strReceiver) {
        final MsgTabActivity activity = (MsgTabActivity) getActivity();
        Resources res = ((Context)(activity)).getResources();

        //arrayAdapter.clear();
        arrayAdapter = new MsgListViewAdapter() ;
        listview.setAdapter(arrayAdapter);

        Cursor iCursor = mDbOpenHelper.getRecvMsgList(strReceiver);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String strSender = iCursor.getString(iCursor.getColumnIndex("sender"));
            strSender = setTextLength(strSender,20);
            //String strReceiver = iCursor.getString(iCursor.getColumnIndex("receiver"));
            //strReceiver = setTextLength(strReceiver,20);
            String strMsg = iCursor.getString(iCursor.getColumnIndex("message"));
            strMsg = setTextLength(strMsg,1000);
            //Date dtSentAt = iCursor.get(iCursor.getColumnIndex("sent_at"));
            //String strSentAt = DateFormat.format("yyyy/MM/dd HH:mm", dtSentAt).toString();
            String strSentAt = iCursor.getString(iCursor.getColumnIndex("sent_at"));

            //String Result = strSender + strReceiver + strMsg + strSentAt;
            //arrayData.add(Result);
            //arrayIndex.add(tempIndex);

            arrayAdapter.addItem(res.getDrawable(R.drawable.baseline_account_circle_black_18dp), strSender, strSentAt, strMsg);
        }
        //arrayAdapter.addAll(arrayData);
     }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }
}