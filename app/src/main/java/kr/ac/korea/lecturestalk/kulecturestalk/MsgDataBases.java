package kr.ac.korea.lecturestalk.kulecturestalk;

import android.provider.BaseColumns;

public final class MsgDataBases {

    public static final class CreateDB implements BaseColumns{
        public static final String SENDER = "sender";
        public static final String RECEIVER = "receiver";
        public static final String MESSAGE = "message";
        public static final String SENT_AT = "sent_at";
        public static final String _TABLENAME0 = "tbl_msg";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                + _ID + " integer primary key autoincrement, "
                + SENDER + " text not null , "
                + RECEIVER + " text not null , "
                + MESSAGE + " text not null , "
                + SENT_AT + " date not null );";
    }
}