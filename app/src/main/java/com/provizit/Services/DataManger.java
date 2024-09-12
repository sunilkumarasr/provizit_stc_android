package com.provizit.Services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.provizit.AESUtil;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsModel;
import com.provizit.Conversions;
import com.provizit.MVVM.RequestModels.ActionNotificationModelRequest;
import com.provizit.MVVM.RequestModels.CheckSetupModelRequest;
import com.provizit.MVVM.RequestModels.NotificationsStatusChangeModelRequest;
import com.provizit.MVVM.RequestModels.OtpSendEmaiModelRequest;
import com.provizit.MVVM.RequestModels.QrIndexModelRequest;
import com.provizit.MVVM.RequestModels.SetUpLoginModelRequest;
import com.provizit.MVVM.RequestModels.UpdatePwdModelRequest;
import com.provizit.Services.AppointmentDetails.AppointmentDetailsModel;
import com.provizit.Services.Notifications.Notifications_model;
import com.provizit.Utilities.Inviteemodelclass;
import java.util.concurrent.TimeUnit;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManger {
    private static final String TAG = "DataManger";

//    public static final String ROOT_URL1 = "https://devapi.provizit.com/provizit/resources/";
//    public static final String ROOT_URL1 = "http://95.177.171.127:8080/provizit/resources/";
    public static final String ROOT_URL1 = "https://liveapi.provizit.com/provizit/resources/";

//    public static final String ROOT_URL1 = "http://192.168.1.6:4200/provizit/resources/";
//    public static final String ROOT_URL1 = "https://stcapi.provizit.com/provizit/resources/";
    public static final String IMAGE_URL = "https://provizit.com";
    public static String appLanguage = "en";
    private static DataManger dataManager;
    private Retrofit retrofit1,retrofit2,retrofitSecurity;
    private DataManger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        HttpLoggingInterceptor logging1 = new HttpLoggingInterceptor();
        logging1.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();
        httpClient1.connectTimeout(30, TimeUnit.SECONDS);
        httpClient1.readTimeout(120, TimeUnit.SECONDS);
        httpClient1.writeTimeout(120, TimeUnit.SECONDS);
        httpClient1.addInterceptor(logging1);


        HttpLoggingInterceptor loggingSecurity = new HttpLoggingInterceptor();
        loggingSecurity.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientSecurity = new OkHttpClient.Builder();

        httpClientSecurity.connectTimeout(30, TimeUnit.SECONDS);
        httpClientSecurity.readTimeout(30, TimeUnit.SECONDS);
        httpClientSecurity.writeTimeout(30, TimeUnit.SECONDS);
        httpClientSecurity.addInterceptor(loggingSecurity);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofitSecurity = new Retrofit.Builder().baseUrl(ROOT_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientSecurity.build())
                .build();
        retrofit1 = new Retrofit.Builder().baseUrl(IMAGE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        retrofit2 = new Retrofit.Builder().baseUrl(ROOT_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient1.build())
                .build();

    }

    public static DataManger getDataManager() {
        if (dataManager == null) {
            dataManager = new DataManger();
        }
        return dataManager;
    }

    public static String Pwd_encrypt(Context context, String pwd, String val) {
        AESUtil aesUtil = new AESUtil(context);
        if (context != null) {
            return aesUtil.encrypt(pwd, val);
        }
        return "";
    }

    public void iresend(Callback<Model> cb, Context context, JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.iresend(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void qrindex(Callback<String> cb, Context context, QrIndexModelRequest qrIndexModelRequest) {
        API apiService = retrofit1.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<String> call = apiService.qrindex(bearer,newEncrypt,qrIndexModelRequest);
        call.enqueue((Callback<String>) cb);
    }
    public void pdfupload(Callback<JsonObject> cb, Context context, MultipartBody.Part file , RequestBody key, RequestBody cid) {
        API apiService = retrofit1.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<JsonObject> call = apiService.pdfupload(file,key,cid);
        call.enqueue((Callback<JsonObject>) cb);
    }

    public void checkuser(Callback<Model1> cb, Context context,String type, String usertype, String val) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.checkuser(bearer,newEncrypt,type, usertype, val);
        call.enqueue((Callback<Model1>) cb);
    }
    public void userLogin(Callback<Model> cb, Context context,JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getuserLogin(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void getVersions(Callback<Model1> cb, Context context) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        System.out.println("getVersions "+newEncrypt);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getVersions(bearer,newEncrypt);
        call.enqueue((Callback<Model1>) cb);
    }

    public void checkSetup(Callback<Model> cb, Context context, CheckSetupModelRequest data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        System.out.println("checkSetup "+newEncrypt);
        String bearer = "Bearer" + newEncrypt;
        Log.e(TAG, "checkSetup:newEncrypt "+newEncrypt );
        Log.e(TAG, "checkSetup:bearer "+bearer );
        Call<Model> call = apiService.checkSetup(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void appuserlogin(Callback<Model> cb, Context context, JsonObject jsonObject) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        System.out.println("appuserlogin "+newEncrypt);
        String bearer = "Bearer" + newEncrypt;
        Log.e(TAG, "appuserlogin:newEncrypt "+newEncrypt );
        Log.e(TAG, "appuserlogin:bearer "+bearer );
        Call<Model> call = apiService.appuserlogin(bearer,newEncrypt,jsonObject);
        call.enqueue((Callback<Model>) cb);
    }

    public void userADlogin(Callback<Model> cb, Context context, JsonObject jsonObject) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        System.out.println("userADlogin "+newEncrypt);
        String bearer = "Bearer" + newEncrypt;
        Log.e(TAG, "userADlogin:newEncrypt "+newEncrypt );
        Log.e(TAG, "userADlogin:bearer "+bearer );
        Log.e("jsonObjectJJ",jsonObject.get("id").toString());
        Call<Model> call = apiService.userADlogin(bearer,newEncrypt,jsonObject);
        call.enqueue((Callback<Model>) cb);
    }

    public void otpsendemail(Callback<Model> cb, Context context, OtpSendEmaiModelRequest otpSendEmaiModelRequest) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.otpsendemail(bearer,newEncrypt,otpSendEmaiModelRequest);
        call.enqueue((Callback<Model>) cb);
    }
    public void setuplogin(Callback<Model> cb, Context context, SetUpLoginModelRequest setUpLoginModelRequest) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.setuplogin(bearer,newEncrypt,setUpLoginModelRequest);
        call.enqueue((Callback<Model>) cb);
    }
    public void updatepwd(Callback<Model> cb, Context context, UpdatePwdModelRequest updatePwdModelRequest) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.updatepwd(bearer,newEncrypt,updatePwdModelRequest);
        call.enqueue((Callback<Model>) cb);
    }

    public void getazureaddetails(Callback<Model> cb, Context context, String camp_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getazureaddetails(bearer,newEncrypt,camp_id);
        call.enqueue((Callback<Model>) cb);
    }

    public void actionmeetings(Callback<Model> cb, Context context,JsonObject data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.actionmeetings(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void actionmeetings_total_count(Callback<TotalModelCount> cb, Context context,JsonObject data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<TotalModelCount> call = apiService.actionmeetings_total_count(bearer,newEncrypt,data);
        call.enqueue((Callback<TotalModelCount>) cb);
    }

    public void actioncheckinout(Callback<Model> cb, Context context,JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.actioncheckinout(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void actionEmployees(Callback<Model> cb, Context context,JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.actionEmployees(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void updatemeetings(Callback<Model> cb, Context context,JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.updatemeetings(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void reschedulemeeting(Callback<Model> cb, Context context,JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.reschedulemeeting(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }

    public void addcovisitor(Callback<Model> cb,Context context, JsonObject data) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.addcovisitor(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }
    public void getuserDetails(Callback<Model> cb, Context context) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getuserDetails(bearer,newEncrypt);
        call.enqueue((Callback<Model>) cb);
    }
    public void getInviteData(Callback<Model1> cb,Context context, String type, String usertype, String val) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getInviteData(bearer,newEncrypt,type,usertype,val);
        call.enqueue((Callback<Model1>) cb);
    }
    public void getemployeeformdetails(Callback<Inviteemodelclass> cb, Context context) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Inviteemodelclass> call = apiService.getemployeeformdetails(bearer,newEncrypt);
        call.enqueue((Callback<Inviteemodelclass>) cb);
    }
    public void getEmployeeDetails(Callback<JsonObject> cb, Context context, String id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<JsonObject> call = apiService.getEmployeeDetails(bearer,newEncrypt,id);
        call.enqueue((Callback<JsonObject>) cb);
    }
    public void getamenities(Callback<Inviteemodelclass> cb, Context context) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Inviteemodelclass> call = apiService.getamenities(bearer,newEncrypt);
        call.enqueue((Callback<Inviteemodelclass>) cb);
    }
    public void getmeetingrooms(Callback<Model1> cb, Context context, String location) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getmeetingrooms(bearer,newEncrypt,location);
        call.enqueue((Callback<Model1>) cb);
    }
    public void actionnotification(Callback<Model> cb, Context context, ActionNotificationModelRequest actionNotificationModelRequest) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.actionnotification(bearer,newEncrypt,actionNotificationModelRequest);
        call.enqueue((Callback<Model>) cb);
    }
    public void getentrypoints(Callback<Model1> cb,Context context, String location) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getentrypoints(bearer,newEncrypt,location);
        call.enqueue((Callback<Model1>) cb);
    }
    public void gethostslots(Callback<HostSlotsModel> cb, Context context, String type, String emp_id, String email, Long start, Long end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<HostSlotsModel> call = apiService.gethostslots(bearer,newEncrypt,type,emp_id,email,start,end);
        call.enqueue((Callback<HostSlotsModel>) cb);
    }

    public void getuserslotdetails(Callback<Model> cb, Context context,String id,String comp_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getuserslotdetails(bearer,newEncrypt,id,comp_id);
        call.enqueue((Callback<Model>) cb);
    }

    public void getrmslots(Callback<Model1> cb, Context context,String type,Long start, Long end,String rm_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getrmslots(bearer,newEncrypt,type,start,end,rm_id);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getmrmslots(Callback<Model1> cb, Context context,String type,Long start, Long end,String l_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getmrmslots(bearer,newEncrypt,type,start,end,l_id);
        call.enqueue((Callback<Model1>) cb);
    }
    public void getmeetingdetails(Callback<Model> cb, Context context, String id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getmeetingdetails(bearer,newEncrypt,id);
        call.enqueue((Callback<Model>) cb);
    }
    public void getmeetings(Callback<Model1> cb,Context context,String type, String emp_id,String start,String end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getmeetings(bearer,newEncrypt,type,emp_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }
    public void getumeetings(Callback<Model1> cb, String type, String comp_id, String emp_id,String start,String end) {
        API apiService = retrofit2.create(API.class);
        Call<Model1> call = apiService.getumeetings(type,comp_id,emp_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getsumeetings(Callback<Model1> cb, String type, String comp_id, String emp_id,String start,String end) {
        API apiService = retrofit2.create(API.class);
        Call<Model1> call = apiService.getsumeetings(type,comp_id,emp_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }
    //    http://18.221.211.226:8080/egems/resources/checkinout/getvisitorslist?comp_id=PROAA03&l_id=&id=5ff59db31f09f639294fcdd3&type=today&maintype=checkinn&newmaintype=checkin

    public void getvisitorslist(Callback<Model1> cb, Context context,String l_id,String id,String type, String maintype,String newmaintype) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getvisitorslist(bearer,newEncrypt,l_id,id,type,maintype,newmaintype);
        call.enqueue((Callback<Model1>) cb);
    }

    public void gethistorydetails(Callback<Model> cb, Context context,String id,String comp_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.gethistorydetails(bearer,newEncrypt,id,comp_id);
        call.enqueue((Callback<Model>) cb);
    }



    public void getameetings(Callback<Model1> cb, String type, String comp_id, String emp_id,String start,String end) {
        API apiService = retrofit2.create(API.class);
        Call<Model1> call = apiService.getameetings(type,comp_id,emp_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }


    public void getoappointments(Callback<Model1> cb, Context context,String l_id,String type, String h_id,String emp_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getoappointments(bearer,newEncrypt,l_id,type,h_id,emp_id);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getappointmentsdetails(Callback<AppointmentDetailsModel> cb, Context context, String id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<AppointmentDetailsModel> call = apiService.getappointmentsdetails(bearer,newEncrypt,id);
        call.enqueue((Callback<AppointmentDetailsModel>) cb);
    }

    public void updateappointment(Callback<Model> cb, Context context, JsonObject jsonObject) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.updateappointment(bearer,newEncrypt,jsonObject);
        call.enqueue((Callback<Model>) cb);
    }

    public void getmeetingrequests(Callback<Model1> cb, Context context,String type, String emp_id,String start,String end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getmeetingrequests(bearer,newEncrypt,type,emp_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getemployeeslots(Callback<Model1> cb, Context context,String id, String l_id,String Purpose,String emp_id,String type,Long Start,Long End,Long date) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getemployeeslots(bearer,newEncrypt,id,l_id,Purpose,emp_id,type,Start,End,date);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getmeetingroomapprovals(Callback<Model1> cb, Context context,String type, String emp_id, String l_id, String h_id, String start,String end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getmeetingroomapprovals(bearer,newEncrypt,type,emp_id,l_id,h_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getoutlookappointments(Callback<Model1> cb, Context context,String type, String email, String start, String end, String companyID) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getoutlookappointments(bearer,newEncrypt,type,email,start,end,companyID);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getmeetingapprovals(Callback<Model1> cb, Context context,String type, String emp_id, String role_id, String l_id, String h_id,String start,String end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getmeetingapprovals(bearer,newEncrypt,type,emp_id,role_id,l_id,h_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getsubhierarchys(Callback<Model1> cb,Context context,String indexid) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getsubhierarchys(bearer,newEncrypt,indexid);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getsearchemployees(Callback<Model1> cb, Context context, String l_id, String h_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getsearchemployees(bearer,newEncrypt,l_id,h_id);
        call.enqueue((Callback<Model1>) cb);
    }

    public void readorunread(Callback<Model> cb, Context context,JsonObject jsonObject) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.readorunread(bearer,newEncrypt,jsonObject);
        call.enqueue((Callback<Model>) cb);
    }

    public void reportsList(Callback<Model1> cb, Context context,String type, String l_id, String h_id, String emp_id, String from, String to) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.reportsList(bearer,newEncrypt,type, l_id, h_id,emp_id,from,to);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getnotifications(Callback<Notifications_model> cb, Context context, String comp_id, String email) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Notifications_model> call = apiService.getnotifications(bearer,newEncrypt,comp_id, email);
        call.enqueue((Callback<Notifications_model>) cb);
    }

    public void notificationsStatus_Change(Callback<Model> cb, Context context, NotificationsStatusChangeModelRequest notificationsStatusChangeModelRequest) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.notificationsStatus_Change(bearer,newEncrypt,notificationsStatusChangeModelRequest);
        call.enqueue((Callback<Model>) cb);
    }

    public void getcategories(Callback<Model1> cb, Context context, String id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getcategories(bearer,newEncrypt,id);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getpslotcategories(Callback<Model1> cb, Context context, String comp_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getpslotcategories(bearer,newEncrypt,comp_id);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getcatlots(Callback<Model1> cb, Context context, String id, String l_id, String cat_id, Long start, Long end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getcatlots(bearer,newEncrypt,id,l_id,cat_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }

    public void gettimepslots(Callback<Model1> cb, Context context, String id, String l_id, String lot_id, String cat_id,String type, Long start, Long end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.gettimepslots(bearer,newEncrypt,id,l_id,lot_id,cat_id,type,start,end);
        call.enqueue((Callback<Model1>) cb);
    }


    public void getpslots(Callback<Model1> cb, Context context, String cat_id,String lot_id,String start,String end) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getpslots(bearer,newEncrypt,cat_id,lot_id,start,end);
        call.enqueue((Callback<Model1>) cb);
    }

    public void getEmployees(Callback<Model1> cb, Context context, String comp_id) {
        API apiService = retrofitSecurity.create(API.class);
        String newEncrypt = Conversions.encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model1> call = apiService.getEmployees(bearer,newEncrypt,comp_id);
        call.enqueue((Callback<Model1>) cb);
    }


}
