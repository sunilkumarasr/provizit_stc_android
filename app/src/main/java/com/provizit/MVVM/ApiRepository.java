package com.provizit.MVVM;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.provizit.MVVM.RequestModels.ActionNotificationModelRequest;
import com.provizit.MVVM.RequestModels.CheckSetupModelRequest;
import com.provizit.MVVM.RequestModels.NotificationsStatusChangeModelRequest;
import com.provizit.MVVM.RequestModels.OtpSendEmaiModelRequest;
import com.provizit.MVVM.RequestModels.QrIndexModelRequest;
import com.provizit.MVVM.RequestModels.SetUpLoginModelRequest;
import com.provizit.MVVM.RequestModels.UpdatePwdModelRequest;
import com.provizit.Services.AppointmentDetails.AppointmentDetailsModel;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Services.Notifications.Notifications_model;
import com.provizit.Services.TotalModelCount;
import com.provizit.Utilities.Inviteemodelclass;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private static final String TAG = "ApiRepository";


    //getVersions
    //Version Update
    public void getVersions(Model1Response logresponse, Context context){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getVersions(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getVersions0" );
                }else {
                    Log.e(TAG, "onResp"+"getVersions1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getVersions2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context);
    }


    //InitialActivity
    //login
    public void checkSetup(ModelResponse logresponse, Context context, CheckSetupModelRequest checkSetupmodelrequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.checkSetup(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"checkSetup0" );
                }else {
                    Log.e(TAG, "onResp"+"checkSetup1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"checkSetup2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,checkSetupmodelrequest);
    }

    //InitialActivity
    //login
    public void appuserlogin(ModelResponse logresponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.appuserlogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"appuserlogin0" );
                }else {
                    Log.e(TAG, "onResp"+"appuserlogin1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"appuserlogin2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,jsonObject);
    }

    //InitialActivity
    //login
    public void userADlogin(ModelResponse logresponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.userADlogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"userADlogin0" );
                }else {
                    Log.e(TAG, "onResp"+"userADlogin1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"userADlogin2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,jsonObject);
    }


    //InitialActivity
    //send otp
    public void otpsendemail(ModelResponse logresponse, Context context, OtpSendEmaiModelRequest otpSendEmaiModelRequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.otpsendemail(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"otpsendemail0" );
                }else {
                    Log.e(TAG, "onResp"+"otpsendemail1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"otpsendemail2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,otpSendEmaiModelRequest);
    }

    //OtpActivity
    //setup login
    public void setuplogin(ModelResponse logresponse, Context context, SetUpLoginModelRequest setUpLoginModelRequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.setuplogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"otpsendemail0" );
                }else {
                    Log.e(TAG, "onResp"+"otpsendemail1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"otpsendemail2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,setUpLoginModelRequest);
    }

    //OtpActivity,SetPasswordActivity
    //action notification login
    public void actionnotification(ModelResponse logresponse, Context context, ActionNotificationModelRequest actionNotificationModelRequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actionnotification(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"actionnotification0" );
                }else {
                    Log.e(TAG, "onResp"+"actionnotification1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"actionnotification2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,actionNotificationModelRequest);
    }

    //SetPasswordActivity
    //update pwd
    public void updatepwd(ModelResponse logresponse, Context context, UpdatePwdModelRequest updatePwdModelRequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updatepwd(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"updatepwd0" );
                }else {
                    Log.e(TAG, "onResp"+"updatepwd1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"updatepwd2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,updatePwdModelRequest);
    }

    //SetPasswordActivity
    //getazureaddetails
    public void getazureaddetails(ModelResponse logresponse, Context context, String camp_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getazureaddetails(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getazureaddetails0" );
                }else {
                    Log.e(TAG, "onResp"+"getazureaddetails1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"getazureaddetails2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,camp_id);
    }

    //NavigationActivity
    //getamenities
    public void getamenities(InviteemodelclassResponse Inviteemodel_R, Context context){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getamenities(new Callback<Inviteemodelclass>() {
            @Override
            public void onResponse(Call<Inviteemodelclass> call, Response<Inviteemodelclass> response) {
                if (response.isSuccessful()){
                    Inviteemodel_R.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getamenities0" );
                }else {
                    Log.e(TAG, "onResp"+"getamenities1" );
                    Inviteemodel_R.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Inviteemodelclass> call, Throwable t) {
                Log.e(TAG, "onResp"+"getamenities2" );
                Inviteemodel_R.onFailure(new Throwable(t));
            }
        },context);
    }

    //NavigationActivity, NotificationsActivity
    //notifications List
    public void getnotifications(Notifications_modelResponse n_logresponse, Context context, String comp_id, String email){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getnotifications(new Callback<Notifications_model>() {
            @Override
            public void onResponse(Call<Notifications_model> call, Response<Notifications_model> response) {
                if (response.isSuccessful()){
                    n_logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"notificationsList0" );
                }else {
                    Log.e(TAG, "onResp"+"notificationsList1" );
                    n_logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Notifications_model> call, Throwable t) {
                Log.e(TAG, "onResp"+"notificationsList2" );
                n_logresponse.onFailure(new Throwable(t));
            }
        },context,comp_id,email);
    }

    //NotificationsActivity
    //notifications Status Change
    public void notificationsStatus_Change(ModelResponse logresponse, Context context, NotificationsStatusChangeModelRequest notificationsStatusChangeModelRequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.notificationsStatus_Change(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"notificationsStatus_Change0" );
                }else {
                    Log.e(TAG, "onResp"+"notificationsStatus_Change1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"notificationsStatus_Change2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,notificationsStatusChangeModelRequest);
    }

    //VisitorDetailsActivity
    //employee form details
    public void getemployeeformdetails(InviteemodelclassResponse i_loginResponse, Context context){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getemployeeformdetails(new Callback<Inviteemodelclass>() {
            @Override
            public void onResponse(Call<Inviteemodelclass> call, Response<Inviteemodelclass> response) {
                if (response.isSuccessful()){
                    i_loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getemployeeformdetails0" );
                }else {
                    Log.e(TAG, "onResp"+"getemployeeformdetails1" );
                    i_loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Inviteemodelclass> call, Throwable t) {
                Log.e(TAG, "onResp"+"getemployeeformdetails2" );
                i_loginResponse.onFailure(new Throwable(t));
            }
        },context);
    }

    //VisitorDetailsActivity,MeetingDescriptionActivity
    //Employee Details
    public void getEmployeeDetails(JsonObjectResponse jsonObjectResponse, Context context,String Emp_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getEmployeeDetails(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    jsonObjectResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getEmployeeDetails0" );
                }else {
                    Log.e(TAG, "onResp"+"getEmployeeDetails1" );
                    jsonObjectResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onResp"+"getEmployeeDetails2" );
                jsonObjectResponse.onFailure(new Throwable(t));
            }
        },context,Emp_id);
    }

    //VisitorDetailsActivity
    //qrindex
    public void qrindex(StringResponses stringResponse, Context context, QrIndexModelRequest qrIndexModelRequest){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.qrindex(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    stringResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"qrindex0" );
                }else {
                    Log.e(TAG, "onResp"+"qrindex1" );
                    stringResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onResp"+"qrindex2" );
                stringResponse.onFailure(new Throwable(t));
            }
        },context,qrIndexModelRequest);
    }

    //VisitorDetailsActivity
    //action Employees
    public void actionEmployees(ModelResponse logresponse, Context context,JsonObject gsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actionEmployees(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"actionEmployees0" );
                }else {
                    Log.e(TAG, "onResp"+"actionEmployees1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"actionEmployees2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,gsonObject);
    }

    //AppointmentDetailsActivity, SetUpMeetingActivity
    //getappointmentsdetails
    public void getappointmentsdetails(getappointmentsdetails_modelResponse getappointmentsdetails_modelResponse, Context context,String m_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getappointmentsdetails(new Callback<AppointmentDetailsModel>() {
            @Override
            public void onResponse(Call<AppointmentDetailsModel> call, Response<AppointmentDetailsModel> response) {
                if (response.isSuccessful()){
                    getappointmentsdetails_modelResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getappointmentsdetails0" );
                }else {
                    Log.e(TAG, "onResp"+"getappointmentsdetails1" );
                    getappointmentsdetails_modelResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<AppointmentDetailsModel> call, Throwable t) {
                Log.e(TAG, "onResp"+"getappointmentsdetails2" );
                getappointmentsdetails_modelResponse.onFailure(new Throwable(t));
            }
        },context,m_id);
    }

    //AppointmentDetailsActivity,MeetingDescriptionActivity,UpComingMeetingsNewFragment
    //getsubhierarchys
    public void getsubhierarchys(Model1Response model1Response, Context context,String l_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getsubhierarchys(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getsubhierarchys0" );
                }else {
                    Log.e(TAG, "onResp"+"getsubhierarchys1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getsubhierarchys2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context,l_id);
    }

    //AppointmentDetailsActivity,MeetingDescriptionActivity,UpComingMeetingsNewFragment
    //getsearchemployees
    public void getsearchemployees(Model1Response model1Response, Context context,String Location,String id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getsearchemployees(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getsearchemployees0" );
                }else {
                    Log.e(TAG, "onResp"+"getsearchemployees1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getsearchemployees2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context,Location,id);
    }

    //AppointmentDetailsActivity
    //updateappointment
    public void updateappointment(ModelResponse modelResponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updateappointment(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    modelResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getsearchemployees0" );
                }else {
                    Log.e(TAG, "onResp"+"getsearchemployees1" );
                    modelResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"getsearchemployees2" );
                modelResponse.onFailure(new Throwable(t));
            }
        },context,jsonObject);
    }

    //UpComingMeetingsNewFragment,MeetingDescriptionActivity,SetUpMeetingActivity
    //getmeetingdetails
    public void getmeetingdetails(ModelResponse loginResponse, Context context, String meetingsId){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingdetails(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getmeetingdetails0" );
                }else {
                    Log.e(TAG, "onResp"+"getmeetingdetails1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"getmeetingdetails2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, meetingsId);
    }

    //UpComingMeetingsNewFragment
    //getoappointments
    public void getoappointments(Model1Response loginResponse, Context context, String meetingsId, String type, String host, String Emp_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getoappointments(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getoappointments0" );
                }else {
                    Log.e(TAG, "onResp"+"getoappointments1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getoappointments2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, meetingsId, type, host, Emp_id);
    }

    //UpComingMeetingsNewFragment
    //getvisitorslist
    public void getvisitorslist(Model1Response loginResponse, Context context, String l_id, String id, String type, String maintype, String newmaintype){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getvisitorslist(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getvisitorslist0" );
                }else {
                    Log.e(TAG, "onResp"+"getvisitorslist1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getvisitorslist2" );
                loginResponse.onFailure(new Throwable(t));
            }
        }, context, l_id, id, type, maintype, newmaintype);
    }

    //CheckinDetails
    //gethistorydetails
    public void gethistorydetails(ModelResponse loginResponse, Context context, String id, String comp_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.gethistorydetails(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"gethistorydetails0" );
                }else {
                    Log.e(TAG, "onResp"+"gethistorydetails1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"gethistorydetails2"+t );
                loginResponse.onFailure(new Throwable(t));
            }
        }, context, id, comp_id);
    }

    //UpComingMeetingsNewFragment
    //get meetings
    public void getmeetings(Model1Response loginResponse, Context context, String type, String emp_id, String start, String end){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetings(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getmeetings0" );
                }else {
                    Log.e(TAG, "onResp"+"getmeetings1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getmeetings2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, type, emp_id, start, end);
    }

    //UpComingMeetingsNewFragment
    //get meeting requests
    public void getmeetingrequests(Model1Response loginResponse, Context context, String type, String emp_id, String start, String end){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingrequests(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getmeetingrequests0" );
                }else {
                    Log.e(TAG, "onResp"+"getmeetingrequests1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getmeetingrequest2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, type, emp_id, start, end);
    }

    //UpComingMeetingsNewFragment
    //get meeting approvals
    public void getmeetingapprovals(Model1Response loginResponse, Context context, String type, String Emp_id, String Roleid, String Location, String Hierarchy_id, String start, String end){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingapprovals(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getmeetingapprovals0" );
                }else {
                    Log.e(TAG, "onResp"+"getmeetingapprovals1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getmeetingapprovals2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, type, Emp_id, Roleid, Location, Hierarchy_id, start, end);
    }

    //UpComingMeetingsNewFragment
    //action checkin out
    public void actioncheckinout(ModelResponse loginResponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actioncheckinout(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"actioncheckinout0" );
                }else {
                    Log.e(TAG, "onResp"+"actioncheckinout1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"actioncheckinout2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }

    //SlotsActivity
    //get userslot details
    public void getuserslotdetails(ModelResponse loginResponse, Context context, String id, String comp_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getuserslotdetails(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getuserslotdetails0" );
                }else {
                    Log.e(TAG, "onResp"+"getuserslotdetails1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"getuserslotdetails2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, id, comp_id);
    }

    //MeetingRoomFragment, SetUpMeetingActivity
    //get rmslots
    public void getrmslots(Model1Response loginResponse, Context context, String type, Long start, Long end, String rm_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getrmslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getrmslots0" );
                }else {
                    Log.e(TAG, "onResp"+"getrmslots1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getrmslots2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, type, start, end, rm_id);
    }

    //MeetingRoomFragment, SetUpMeetingActivity
    //get meeting rooms
    public void getmeetingrooms(Model1Response loginResponse, Context context, String locationId){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingrooms(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getmeetingrooms0" );
                }else {
                    Log.e(TAG, "onResp"+"getmeetingrooms1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getmeetingrooms2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, locationId);
    }

    //MeetingRoomEngagedActivity
    //get mrm slots
    public void getmrmslots(Model1Response loginResponse, Context context, String type, Long start, Long end, String l_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmrmslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getmrmslots0" );
                }else {
                    Log.e(TAG, "onResp"+"getmrmslots1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getmrmslots2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, type, start, end, l_id);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //update meetings
    public void updatemeetings(ModelResponse loginResponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updatemeetings(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"updatemeetings0" );
                }else {
                    Log.e(TAG, "onResp"+"updatemeetings1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"updatemeetings2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }

    //Reccurence Date time
    //reschedule meetings
    public void reschedulemeeting(ModelResponse loginResponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.reschedulemeeting(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"reschedulemeeting0" );
                }else {
                    Log.e(TAG, "onResp"+"reschedulemeeting1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"reschedulemeeting2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }

    //MeetingDescriptionActivity
    //iresend
    public void iresend(ModelResponse loginResponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.iresend(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"iResend0" );
                }else {
                    Log.e(TAG, "onResp"+"iResend1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"iResend2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //addcovisitor
    public void addcovisitor(ModelResponse loginResponse, Context context, JsonObject jsonObject){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.addcovisitor(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"addcovisitor0" );
                }else {
                    Log.e(TAG, "onResp"+"addcovisitor1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"addcovisitor2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //get Invite Data
    public void getInviteData(Model1Response model1Response, Context context, String type, String usertype, String S_value){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getInviteData(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getInviteData0" );
                }else {
                    Log.e(TAG, "onResp"+"getInviteData1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getInviteData2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context, type,usertype,S_value);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //pdf upload
    public void pdfupload(JsonObjectResponse model1Response, Context context, MultipartBody.Part file , RequestBody fname, RequestBody cid){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.pdfupload(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"pdfupload0" );
                }else {
                    Log.e(TAG, "onResp"+"pdfupload1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onResp"+"pdfupload2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context,file,fname,cid);
    }

    //MeetingDescriptionActivity
    //read orunread
    public void readorunread(ModelResponse loginResponse, Context context, JsonObject jsonObject ){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.readorunread(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    loginResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"readorunread0" );
                }else {
                    Log.e(TAG, "onResp"+"readorunread1" );
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"readorunread2" );
                loginResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }


    //MeetingDescriptionActivity, SetUpMeetingActivity
    //get entry points
    public void getentrypoints(Model1Response model1Response, Context context, String locationId ){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getentrypoints(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getentrypoints0" );
                }else {
                    Log.e(TAG, "onResp"+"getentrypoints1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getentrypoints2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context, locationId);
    }

    //SetUpMeetingActivity
    //get host slots
    public void gethostslots(Model1Response model1Response, Context context, String type, String emp_id, String email, Long start, Long end){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.gethostslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"gethostslots0" );
                }else {
                    Log.e(TAG, "onResp"+"gethostslots1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"gethostslots2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context,type, emp_id, email, start, end);
    }

    //SetUpMeetingActivity
    //read orunread
    public void actionmeetings_total_count(TotalModelCountResponse totalModelCountResponse, Context context, JsonObject jsonObject ){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actionmeetings_total_count(new Callback<TotalModelCount>() {
            @Override
            public void onResponse(Call<TotalModelCount> call, Response<TotalModelCount> response) {
                if (response.isSuccessful()){
                    totalModelCountResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"actionmeetings_total_count0" );
                }else {
                    Log.e(TAG, "onResp"+"actionmeetings_total_count1" );
                    totalModelCountResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<TotalModelCount> call, Throwable t) {
                Log.e(TAG, "onResp"+"actionmeetings_total_count2" );
                totalModelCountResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }


    //SetUpMeetingActivity
    //action meetings
    public void actionmeetings(ModelResponse modelResponse, Context context, JsonObject jsonObject ){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actionmeetings(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()){
                    modelResponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"actionmeetings0" );
                }else {
                    Log.e(TAG, "onResp"+"actionmeetings1" );
                    modelResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onResp"+"actionmeetings2" );
                modelResponse.onFailure(new Throwable(t));
            }
        },context, jsonObject);
    }

    //SetUpMeetingActivity
    //get categories
    public void getcategories(Model1Response model1Response, Context context, String id ){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getcategories(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    model1Response.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getcategories0" );
                }else {
                    Log.e(TAG, "onResp"+"getcategories1" );
                    model1Response.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getcategories2" );
                model1Response.onFailure(new Throwable(t));
            }
        },context, id);
    }

    //getEmployees
    public void getEmployees(Model1Response logresponse, Context context,String comp_id){
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getEmployees(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if (response.isSuccessful()){
                    logresponse.onResponse(response.body());
                    Log.e(TAG, "onResp"+"getEmployees0" );
                }else {
                    Log.e(TAG, "onResp"+"getEmployees1" );
                    logresponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e(TAG, "onResp"+"getEmployees2" );
                logresponse.onFailure(new Throwable(t));
            }
        },context,comp_id);
    }


    public interface ModelResponse{
        void onResponse(Model loginResponse);
        void onFailure(Throwable t);
    }

    public interface InviteemodelclassResponse{
        void onResponse(Inviteemodelclass i_loginResponse);
        void onFailure(Throwable t);
    }

    public interface Notifications_modelResponse{
        void onResponse(Notifications_model n_logresponse);
        void onFailure(Throwable t);
    }

    public interface JsonObjectResponse{
        void onResponse(JsonObject jsonObjectResponse);
        void onFailure(Throwable t);
    }


    public interface StringResponses{
        void onResponse(String jsonObjectResponse);
        void onFailure(Throwable t);
    }

    public interface getappointmentsdetails_modelResponse{
        void onResponse(AppointmentDetailsModel appointmentDetailsModel);
        void onFailure(Throwable t);
    }

    public interface Model1Response{
        void onResponse(Model1 model1Response);
        void onFailure(Throwable t);
    }

    public interface TotalModelCountResponse{
        void onResponse(TotalModelCount totalmodelCountResponse);
        void onFailure(Throwable t);
    }


}
