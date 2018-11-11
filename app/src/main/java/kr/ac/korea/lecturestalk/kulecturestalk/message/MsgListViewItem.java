package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import java.util.Calendar;
import java.util.Date;

public class MsgListViewItem {
    private  long id;
    private Drawable iconDrawable ;
    private String strPerson ;
    private String strDateTime ;
    private String strMsg ;
    private boolean chkSelect;


    public MsgListViewItem() {
        id = 0;
        iconDrawable = null ;
        strPerson = "" ;
        strMsg = "" ;
        strDateTime =  "" ;
        chkSelect = false;
    }
    public MsgListViewItem(long id, Drawable icon, String person, String msg, String dt, boolean bChecked) {
        this.id = id;
        iconDrawable = icon;
        strPerson = person;
        strMsg = msg;
        strDateTime = dt;
        chkSelect = bChecked;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getPerson() { return this.strPerson; }
    public String getDateTime() { return this.strDateTime; }
    public String getMsg() {
        return this.strMsg ;
    }
    public boolean isChkSelect() {
        return chkSelect;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setPerson(String person) {
        strPerson = person ;
    }
    public void setDateTime(String dt) {
        strDateTime = dt ;
    }
    public void setMsg(String msg) {
        strMsg = msg ;
    }
    public void setChkSelect(boolean chkSelect) {
        this.chkSelect = chkSelect;
    }
}

