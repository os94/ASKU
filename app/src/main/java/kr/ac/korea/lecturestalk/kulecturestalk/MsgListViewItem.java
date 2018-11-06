package kr.ac.korea.lecturestalk.kulecturestalk;

import android.graphics.drawable.Drawable;

import java.util.Calendar;
import java.util.Date;

public class MsgListViewItem {
    private Drawable iconDrawable ;
    private String strPerson ;
    private String strDateTime ;
    private String strMsg ;

    public MsgListViewItem() {
        iconDrawable = null ;
        strPerson = "" ;
        strMsg = "" ;
        strDateTime =  "" ;
    }
    public MsgListViewItem(Drawable icon, String person, String msg, String dt) {
        iconDrawable = icon;
        strPerson = person;
        strMsg = msg;
        strDateTime = dt;
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

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getPerson() { return this.strPerson; }
    public String getDateTime() { return this.strDateTime; }
    public String getMsg() {
        return this.strMsg ;
    }
}

