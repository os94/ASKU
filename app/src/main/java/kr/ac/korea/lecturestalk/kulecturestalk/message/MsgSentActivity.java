package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MsgSentActivity extends Fragment {

    long nowIndex;
    String sort = "sent_at";

    MsgListViewAdapter arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    //private MsgDbOpenHelper mDbOpenHelper;
    private Retrofit retrofit;

    private FirebaseAuth mFirebaseAuth;
    private String mStrSender = "";

    public MsgSentActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.activity_msg_sent, container, false);
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_msg_sent);
        final View view = inflater.inflate(R.layout.activity_msg_sent, container, false);

        //사용자 이름 추출.
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStrSender = mFirebaseAuth.getCurrentUser().getDisplayName();

        //List 구성.
        ListView listview ;
        //MsgListViewAdapter arrayAdapter;

        // Adapter 생성
        arrayAdapter = new MsgListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.id_listview);
        listview.setAdapter(arrayAdapter);

        //DB에서 쪽지 목록을 읽어서 화면에 출력.
        //mDbOpenHelper = new MsgDbOpenHelper(view.getContext());
        //mDbOpenHelper.open();
        //mDbOpenHelper.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(MsgService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        showDatabase(view, listview, mStrSender);

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        final MsgListViewAdapter farrayAdapter = arrayAdapter;
        final ListView flistview = listview;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(getActivity(), MsgNewActivity.class));
                //super.onListItemClick(parent, v, position, id);

                //Get related checkbox and change flag status..
                CheckBox cb = (CheckBox) view.findViewById(R.id.id_check_box);
                cb.setChecked(!cb.isChecked());

                MsgListViewItem item = (MsgListViewItem) farrayAdapter.getItem(position);
                item.setChkSelect(cb.isChecked());
                //Toast.makeText(getActivity(), "Click item", Toast.LENGTH_SHORT).show();
            }
        }) ;


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
                String strMsg = "";
                ArrayList<Long> listID = new ArrayList<Long>();
                for(int i = farrayAdapter.getCount() - 1; i >= 0; i--) {
                    MsgListViewItem item = (MsgListViewItem) farrayAdapter.getItem(i);
                    boolean bSel = item.isChkSelect();
                    if (bSel) {
                        long id = item.getId();
                        listID.add(id);
                        strMsg += ">> " + bSel + ", " + id;

                        //DB에서 항목 삭제.
                        //mDbOpenHelper.deleteColumn(id);
                        deleteColumn(view, id);

                        //Adapter에서 항목 삭제.
                        item.setChkSelect(false);
                        farrayAdapter.remove(i);
                    }
                }
                if(strMsg != "") {
                    //Toast.makeText(getActivity(), "Selected item:" + strMsg, Toast.LENGTH_SHORT).show();
                    farrayAdapter.notifyDataSetChanged();
                    //flistview.invalidate();
                }}
        });

        return view;
    }

    protected void updateView(View view) {
        int countItem = arrayAdapter.getCount();

        //쪽지 없음 표시.
        TextView txtView = (TextView) view.findViewById(R.id.id_no_msg_sent);
        if (countItem > 0)
            txtView.setVisibility(View.INVISIBLE);
        else
            txtView.setVisibility(View.VISIBLE);
    }

    public void deleteColumn(final View view, long id) {
        try {
            MsgItem input = new MsgItem();
            input.id = id;
            input.sender = "";
            input.receiver = "";
            input.message = "";

            MsgService msgService = retrofit.create(MsgService.class);
            msgService.delMsg(input).enqueue(new Callback<Map<String, Object>>() {
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
                    } else {
                        Toast.makeText(getActivity(), "[오류] 보낸 메시지 삭제 실패.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //화면상태 update.
                    updateView(view);
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(getActivity(), "보낸 메시지 삭제 실패.", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        } catch (Exception ex) {
            Log.e("Insert Log", ex.toString());
            Toast.makeText(getActivity(), "보낸 메시지 삭제 실패.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /*
    public void showDatabase(ListView listview, String strSender) {
        Resources res = getActivity().getResources();

        //arrayAdapter.clear();
        arrayAdapter = new MsgListViewAdapter() ;
        listview.setAdapter(arrayAdapter);

        Cursor iCursor = mDbOpenHelper.getSentMsgList(strSender);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            long tempIndex = iCursor.getLong(iCursor.getColumnIndex("_id"));
            //String strSender = iCursor.getString(iCursor.getColumnIndex("sender"));
            //strSender = setTextLength(strSender,20);
            String strReceiver = iCursor.getString(iCursor.getColumnIndex("receiver"));
            strReceiver = setTextLength(strReceiver,20);
            String strMsg = iCursor.getString(iCursor.getColumnIndex("message"));
            strMsg = setTextLength(strMsg,1000);
            //Date dtSentAt = iCursor.get(iCursor.getColumnIndex("sent_at"));
            //String strSentAt = DateFormat.format("yyyy/MM/dd HH:mm", dtSentAt).toString();
            String strSentAt = iCursor.getString(iCursor.getColumnIndex("sent_at"));

            //String Result = strSender + strReceiver + strMsg + strSentAt;
            //arrayData.add(Result);
            //arrayIndex.add(tempIndex);

            arrayAdapter.addItem(tempIndex, res.getDrawable(R.drawable.baseline_account_circle_black_18dp), strReceiver, strSentAt, strMsg);
        }
        //arrayAdapter.addAll(arrayData);
    }
    */
    public void showDatabase(View view, ListView listview, String strSender) {
        final Resources res = getActivity().getResources();

        //arrayAdapter.clear();
        arrayAdapter = new MsgListViewAdapter() ;
        listview.setAdapter(arrayAdapter);

        //Cursor iCursor = mDbOpenHelper.getSentMsgList(strSender);
        //Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        final View fview = view;
        try {
            MsgItem input = new MsgItem();
            input.id = 0L;
            input.sender = strSender;
            input.receiver = "";
            input.message = "";

            MsgService msgService = retrofit.create(MsgService.class);
            msgService.getSentList(input).enqueue(new Callback<Map<String, Object>>() {
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
                        arrayData.clear();
                        arrayIndex.clear();

                        List<Object> list = (List<Object>) body.get("rows");
                        for(int i = 0; i < list.size(); i++ ) {
                            LinkedTreeMap<String, Object> item = (LinkedTreeMap<String, Object>) list.get(i);
                            int tempIndex =  ((Double) item.get("id")).intValue();
                            //String strSender = item.sender;
                            String strReceiver = (String) item.get("receiver");
                            //strReceiver = setTextLength(strReceiver,20);
                            String strMsg = (String) item.get("message");
                            String strSentAt = (String) item.get("sent_at");

                            //String Result = strSender + strReceiver + strMsg + strSentAt;
                            //arrayData.add(Result);
                            //arrayIndex.add(tempIndex);

                            //arrayAdapter.addItem(tempIndex, res.getDrawable(R.drawable.baseline_account_circle_black_18dp), strReceiver, strSentAt, strMsg);
                            arrayAdapter.addItem(tempIndex, res.getDrawable(R.drawable.ic_profile), strReceiver, strSentAt, strMsg);
                        }
                        //arrayAdapter.addAll(arrayData);
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "[오류] 보낸 메시지 목록 검색 실패.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //화면상태 update.
                    updateView(fview);
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(getActivity(), "보낸 메시지 목록 검색 실패.", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        } catch (Exception ex) {
            Log.e("Insert Log", ex.toString());
            Toast.makeText(getActivity(), "보낸 메시지 목록 검색 실패.", Toast.LENGTH_SHORT).show();
            return;
        }
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