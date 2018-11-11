package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.korea.lecturestalk.kulecturestalk.R;

public class MsgListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MsgListViewItem> listViewItemList = new ArrayList<MsgListViewItem>() ;

    // MsgListViewAdaptor의 생성자
    public MsgListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "msg_listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.msg_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView personTextView = (TextView) convertView.findViewById(R.id.id_person) ;
        TextView dtTextView = (TextView) convertView.findViewById(R.id.id_datetime) ;
        TextView msgTextView = (TextView) convertView.findViewById(R.id.id_msg) ;
        CheckBox selCheck = (CheckBox) convertView.findViewById(R.id.id_check_box) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MsgListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        personTextView.setText(listViewItem.getPerson());
        //dtTextView.setText(DateFormat..format("yyyy/MM/dd HH:mm", listViewItem.getDateTime()).toString());
        dtTextView.setText(listViewItem.getDateTime());
        msgTextView.setText(listViewItem.getMsg());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(long id, Drawable icon, String person, String strDateTime, String msg) {
        MsgListViewItem item = new MsgListViewItem();

        item.setId(id);
        item.setIcon(icon);
        item.setPerson(person);
        item.setDateTime(strDateTime);
        item.setMsg(msg);
        item.setChkSelect(false);

        listViewItemList.add(item);
    }

    public void remove(int position){
        listViewItemList.remove(listViewItemList.get(position));;
    }
}
