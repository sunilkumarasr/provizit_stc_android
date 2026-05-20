package com.provizit.Services;

import static com.provizit.Conversions.encrypt;

import android.content.Context;

import com.google.gson.JsonObject;
import com.provizit.AdapterAndModel.BusySchedules.BusySchedulesModel;
import com.provizit.AdapterAndModel.CompanyDetailsModel;
import com.provizit.AdapterAndModel.GetSearchEmployeesModel;
import com.provizit.AdapterAndModel.GetdocumentsModel;
import com.provizit.AdapterAndModel.GetsubhierarchysModel;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsModel;
import com.provizit.AdapterAndModel.MaterialModel;
import com.provizit.AdapterAndModel.WorkPermitModal;
import com.provizit.AdapterAndModel.WorkVisitTypeModel;
import com.provizit.AdapterAndModel.WorkingDaysModal;
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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API {

    @GET("masters/getversions")
    Call<Model1> getVersions(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("useractions/checkuser")
    Call<Model1> checkuser(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("usertype") String usertype, @Query("val") String val);

    @GET("masters/getuserDetails")
    Call<Model> getuserDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("company/getEmployeeDetails")
    Call<JsonObject> getEmployeeDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @GET("company/getamenities")
    Call<Inviteemodelclass> getamenities(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("visitor/inviteData")
    Call<Model1> getInviteData(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("usertype") String usertype, @Query("val") String val);

    @POST("uploads/qrindex.php")
    Call<String> qrindex(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body QrIndexModelRequest qrIndexModelRequest);

    @Multipart
    @POST("uploads/pdfupload.php")
    Call<JsonObject> pdfupload(@Part MultipartBody.Part file, @Part("key") RequestBody key, @Part("cid") RequestBody c_id);

    @POST("visitor/iresend")
    Call<Model> iresend(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("forms/employeeformdetails")
    Call<Inviteemodelclass> getemployeeformdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @POST("login/userlogin")
    Call<Model> getuserLogin(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("setup/otpsendemailclient")
    Call<Model> otpsendemail(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body OtpSendEmaiModelRequest otpSendEmaiModelRequest);

    @POST("setup/actionnotification")
    Call<Model> actionnotification(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body ActionNotificationModelRequest actionNotificationModelRequest);

    @POST("setup/setuplogin")
    Call<Model> setuplogin(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body SetUpLoginModelRequest setUpLoginModelRequest);

    @POST("setup/checkSetup")
    Call<Model> checkSetup(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body CheckSetupModelRequest checksetupsodelrequest);

    @POST("login/appuserlogin")
    Call<Model> appuserlogin(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("login/userADlogin")
    Call<Model> userADlogin(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @PUT("useractions/updatepwd")
    Call<Model> updatepwd(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body UpdatePwdModelRequest updatePwdModelRequest);

    @GET("forms/getazureaddetails")
    Call<Model> getazureaddetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("cid") String comp_id);

    @POST("meeting/actionmeetings")
    Call<Model> actionmeetings(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("meeting/actionmeetings")
    Call<TotalModelCount> actionmeetings_total_count(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("checkinout/actioncheckinout")
    Call<Model> actioncheckinout(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("checkinout/getcvisitordetails")
    Call<Model> getCVisitorDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id, @Query("emp_id") String emp_id, @Query("id") String id, @Query("l_id") String l_id, @Query("date") Long date, @Query("datetime") Long datetime);

    @POST("company/actionEmployees")
    Call<Model> actionEmployees(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @PUT("meeting/updatemeetings")
    Call<Model> updatemeetings(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("meeting/reschedulemeeting")
    Call<Model> reschedulemeeting(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @POST("visitor/addcovisitor")
    Call<Model> addcovisitor(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("company/gettrainingTitles")
    Call<Model1> getTrainingTitles(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("company/getmeetingrooms")
    Call<Model1> getmeetingrooms(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("location") String location);

    @GET("company/getentrypoints")
    Call<Model1> getentrypoints(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("location") String location);

    @GET("meeting/gethostslots")
    Call<HostSlotsModel> gethostslots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("email") String email, @Query("start") Long start, @Query("end") Long end);

    @GET("slots/getuserslotdetails")
    Call<Model> getuserslotdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("comp_id") String comp_id);

    @GET("meeting/getrmslots")
    Call<Model1> getrmslots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("start") Long start, @Query("end") Long end, @Query("rm_id") String rm_id);

    @GET("checkinout/getvisitorslist")
    Call<Model1> getvisitorslist(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("l_id") String l_id, @Query("id") String id, @Query("type") String type, @Query("maintype") String maintype, @Query("newmaintype") String newmaintype);

    @GET("checkinout/gethistorydetails")
    Call<Model> gethistorydetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("comp_id") String comp_id);

    @GET("outside/getoappointments")
    Call<Model1> getoappointments(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("l_id") String l_id, @Query("type") String type, @Query("h_id") String h_id, @Query("emp_id") String emp_id);

    @GET("outside/getappointmentdetails")
    Call<AppointmentDetailsModel> getappointmentsdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @POST("outside/updateappointment")
    Call<Model> updateappointment(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonObject);

    @POST("outside/viewStatusUpdated")
    Call<Model> readorunread(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("meeting/getmeetings")
    Call<Model1> getmeetings(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("start") String start, @Query("end") String end);

    @GET("outlook/getumeetings")
    Call<Model1> getumeetings(@Query("type") String type, @Query("comp_id") String comp_id, @Query("emp_id") String emp_id, @Query("start") String start, @Query("end") String end);

    @GET("outlook/getsumeetings")
    Call<Model1> getsumeetings(@Query("type") String type, @Query("comp_id") String comp_id, @Query("emp_id") String emp_id, @Query("start") String start, @Query("end") String end);

    @GET("outlook/getameetings")
    Call<Model1> getameetings(@Query("type") String type, @Query("comp_id") String comp_id, @Query("emp_id") String emp_id, @Query("start") String start, @Query("end") String end);

    @GET("meeting/getmeetingdetails")
    Call<Model> getmeetingdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @GET("meeting/getmeetingrequests")
    Call<Model1> getmeetingrequests(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("start") String start, @Query("end") String end);

    @GET("meeting/getmeetingroomapprovals")
    Call<Model1> getmeetingroomapprovals(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("l_id") String l_id, @Query("h_id") String h_id, @Query("start") String start, @Query("end") String end);

    @GET("setup/getoutlookappointments")
    Call<Model1> getoutlookappointments(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("email_id") String email, @Query("start") String start, @Query("end") String end, @Query("comp_id") String comp_id);

    @GET("meeting/getmeetingapprovals")
    Call<Model1> getmeetingapprovals(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("role_id") String role_id, @Query("l_id") String l_id, @Query("h_id") String h_id, @Query("start") String start, @Query("end") String end);

    @GET("masters/getsubhierarchys")
    Call<Model1> getsubhierarchys(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("indexid") String indexid);

    @GET("company/getsearchemployees")
    Call<Model1> getsearchemployees(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("l_id") String l_id, @Query("h_id") String h_id);

    @GET("masters/getRoles")
    Call<Model1> getRoles(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("meeting/getmrmslots")
    Call<Model1> getmrmslots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("start") Long start, @Query("end") Long end, @Query("l_id") String l_id);

    @GET("reports/allreports?")
    Call<Model1> reportsList(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("l_id") String l_id, @Query("h_id") String h_id, @Query("emp_id") String emp_id, @Query("from") String from, @Query("to") String to);

    @GET("meeting/getnotifications")
    Call<Notifications_model> getnotifications(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id, @Query("email") String email);

    @POST("meeting/actionnotifications")
    Call<Model> notificationsStatus_Change(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body NotificationsStatusChangeModelRequest notificationsStatusChangeModelRequest);

    @GET("company/getcategories")
    Call<Model1> getcategories(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @GET("parking/getpslotcategories")
    Call<Model1> getpslotcategories(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id);

    @GET("parking/getcatlots")
    Call<Model1> getcatlots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("l_id") String l_id, @Query("cat_id") String cat_id, @Query("start") Long start, @Query("end") Long end);

    @GET("parking/gettimepslots")
    Call<Model1> gettimepslots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("l_id") String l_id, @Query("lot_id") String lot_id, @Query("cat_id") String cat_id, @Query("type") String type, @Query("start") Long start, @Query("end") Long end);

    @GET("parking/getpslots")
    Call<Model1> getpslots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("cat_id") String cat_id, @Query("lot_id") String lot_id, @Query("start") String start, @Query("end") String end);

    @GET("slots/getemployeeslots")
    Call<Model1> getemployeeslots(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id, @Query("l_id") String l_id, @Query("Purpose") String Purpose, @Query("emp_id") String emp_id, @Query("type") String type, @Query("Start") Long Start, @Query("End") Long End, @Query("date") Long date);

    @GET("meeting/getnotifications")
    Call<Notifications_model> notificationsList(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id, @Query("email") String email);

    @GET("company/getEmployees")
    Call<Model1> getEmployees(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id);

    @GET("company/getbusyScheduledetails")
    Call<BusySchedulesModel> getbusyScheduledetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("cid") String comp_id, @Query("id") String emp_id, @Query("type") String type);

    @POST("company/actionbusySchedule")
    Call<BusySchedulesModel> actionbusySchedule(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

//    work and material
    @GET("masters/getuserDetails")
    Call<CompanyDetailsModel> getuserDetailsworkMeterial(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type);

    @GET("workpermits/getworktypes")
    Call<WorkVisitTypeModel> getworktypes(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @GET("workpermits/getworklocations")
    Call<WorkVisitTypeModel> getworklocation(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @GET("workpermits/getworkpurposes")
    Call<WorkVisitTypeModel> getworkpurposes(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @POST("workpermits/actionworkpermita")
    Call<WorkVisitTypeModel> actionworkpermita(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("entry/getrefdocuments")
    Call<MaterialModel> getrefdocuments(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @GET("entry/getentrypurposes")
    Call<MaterialModel> getentrypurposes(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @GET("entry/getexitpurposes")
    Call<MaterialModel> getexitpurposes(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @GET("masters/getsubhierarchys")
    Call<GetsubhierarchysModel> getsubhierarchysmaterial(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String comp_id, @Query("indexid") String indexid);

    @GET("company/getsearchemployees")
    Call<GetSearchEmployeesModel> getsearchemployeesmaterial(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("l_id") String l_id, @Query("h_id") String h_id, @Query("type") String type);

    @POST("entry/actionentrypermitrequest")
    Call<MaterialModel> actionentrypermitrequest(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("company/getdocuments")
    Call<GetdocumentsModel> getdocuments(@Header("Authorization") String Bearer, @Header("DeviceId") String header);

    @GET("company/getworkingdays")
    Call<WorkingDaysModal> getworkingdays(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("comp_id") String id);

    @GET("workpermits/getworkpermits")
    Call<Model1> getworkpermits(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("email") String email, @Query("start") String start, @Query("end") String end);

    @GET("workpermits/getworkpermitapprovals")
    Call<Model1> getworkpermitapprovals(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("email") String email, @Query("start") String start, @Query("end") String end);

    @GET("workpermits/getworkpermitDetails")
    Call<WorkPermitModal> getworkpermitDetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @POST("workpermits/updateworkpermita")
    Call<WorkPermitModal> updateworkpermita(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("entry/getmaterialpermitapprovals")
    Call<Model1> getmaterialpermitapprovals(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("start") String start, @Query("end") String end);

    @GET("entry/getentrypermitdetails")
    Call<WorkPermitModal> getentrypermitdetails(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("id") String id);

    @PUT("entry/updatematerialpermit")
    Call<WorkPermitModal> updatematerialpermit(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Body JsonObject jsonBody);

    @GET("entry/getentrypermitrequests")
    Call<Model1> getentrypermitrequests(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type, @Query("emp_id") String emp_id, @Query("email") String email, @Query("start") String start, @Query("end") String end);


    @GET("masters/getuserDetails")
    Call<CompanyDetailsModel> getuserDetailssetUp(@Header("Authorization") String Bearer, @Header("DeviceId") String header, @Query("type") String type);


}
