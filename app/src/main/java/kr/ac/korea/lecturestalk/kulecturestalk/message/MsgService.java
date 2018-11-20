package kr.ac.korea.lecturestalk.kulecturestalk.message;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MsgService {

    String URL = "http://54.180.95.113:8080/asku/api/";

    @POST("getMsgList.do")
    Call<Map<String, Object>> getSentList(@Body MsgItem param);

    @POST("getMsgList.do")
    Call<Map<String, Object>> getRecvList(@Body MsgItem param);

    @POST("sendMsgProc.do")
    Call<Map<String, Object>> newMsg(@Body MsgItem param);

    @POST("deleteMsgProc.do")
    Call<Map<String, Object>> delMsg(@Body MsgItem param);
}
