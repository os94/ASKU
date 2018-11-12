package kr.ac.korea.lecturestalk.kulecturestalk.message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class MsgDbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String receiver, CursorFactory factory, int version) {
            super(context, receiver, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(MsgDataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+MsgDataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public MsgDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public MsgDbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn(String sender, String receiver, String message){
        ContentValues values = new ContentValues();
        values.put(MsgDataBases.CreateDB.SENDER, sender);
        values.put(MsgDataBases.CreateDB.RECEIVER, receiver);
        values.put(MsgDataBases.CreateDB.MESSAGE, message);
        values.put(MsgDataBases.CreateDB.SENT_AT, Calendar.getInstance().getTime().toString());
        return mDB.insert(MsgDataBases.CreateDB._TABLENAME0, null, values);
    }

    // Update DB
    public boolean updateColumn(long id, String sender, String receiver, String message){
        ContentValues values = new ContentValues();
        values.put(MsgDataBases.CreateDB.SENDER, sender);
        values.put(MsgDataBases.CreateDB.RECEIVER, receiver);
        values.put(MsgDataBases.CreateDB.MESSAGE, message);
        values.put(MsgDataBases.CreateDB.SENT_AT, Calendar.getInstance().getTime().toString());
        return mDB.update(MsgDataBases.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(MsgDataBases.CreateDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(MsgDataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(MsgDataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM tbl_msg ORDER BY " + sort + ";", null);
        return c;
    }

    // 받은 쪽지 목록
    public Cursor getRecvMsgList(String strReceiver){
        Cursor c = mDB.rawQuery( "SELECT * FROM tbl_msg WHERE receiver = '" + strReceiver + "' ORDER BY sent_at DESC;", null);
        return c;
    }

    // 보낸 쪽지 목록
    public Cursor getSentMsgList(String strSender){
        Cursor c = mDB.rawQuery( "SELECT * FROM tbl_msg WHERE sender = '" + strSender + "' ORDER BY sent_at DESC;", null);
        return c;
    }
}
