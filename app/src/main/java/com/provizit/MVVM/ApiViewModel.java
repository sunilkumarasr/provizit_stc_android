package com.provizit.MVVM;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.RequestModels.ActionNotificationModelRequest;
import com.provizit.MVVM.RequestModels.CheckSetupModelRequest;
import com.provizit.MVVM.RequestModels.NotificationsStatusChangeModelRequest;
import com.provizit.MVVM.RequestModels.OtpSendEmaiModelRequest;
import com.provizit.MVVM.RequestModels.QrIndexModelRequest;
import com.provizit.MVVM.RequestModels.SetUpLoginModelRequest;
import com.provizit.MVVM.RequestModels.UpdatePwdModelRequest;
import com.provizit.Services.AppointmentDetails.AppointmentDetailsModel;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Services.Notifications.Notifications_model;
import com.provizit.Services.TotalModelCount;
import com.provizit.Utilities.Inviteemodelclass;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ApiViewModel extends ViewModel {
    private static final String TAG = "ApiViewModel";

    //Versions
    MutableLiveData<Model1> getVersions_response = new MutableLiveData<>();
    //login
    MutableLiveData<Model> checkSetup_response = new MutableLiveData<>();
    MutableLiveData<Model> appuserlogin_response = new MutableLiveData<>();
    MutableLiveData<Model> userADlogin_response = new MutableLiveData<>();
    //send otp
    MutableLiveData<Model> otpsendemail_response = new MutableLiveData<>();
    //setuplogin
    MutableLiveData<Model> setuplogin_response = new MutableLiveData<>();
    //actionnotification
    MutableLiveData<Model> actionnotification_response = new MutableLiveData<>();
    //updatepwd
    MutableLiveData<Model> updatepwd_response = new MutableLiveData<>();
    //getamenities
    MutableLiveData<Inviteemodelclass> getamenities_response = new MutableLiveData<>();
    //getazureaddetails
    MutableLiveData<Model> getazureaddetails_response = new MutableLiveData<>();
    //notificationsList
    MutableLiveData<Notifications_model> notificationsList_response = new MutableLiveData<>();
    //notifications status change
    MutableLiveData<Model> notificationsstatuschange_response = new MutableLiveData<>();
    //emp_form_details
    MutableLiveData<Inviteemodelclass> employeeformdetails_response = new MutableLiveData<>();
    //employee Details
    MutableLiveData<JsonObject> employeeDetails_response = new MutableLiveData<>();
    //qrindex
    MutableLiveData<String> qrindex_response = new MutableLiveData<>();
    MutableLiveData<Model> actionEmployees_response = new MutableLiveData<>();
    MutableLiveData<AppointmentDetailsModel> getappointmentsdetails_response = new MutableLiveData<>();
    MutableLiveData<Model1> getsubhierarchys_response = new MutableLiveData<>();
    MutableLiveData<Model1> getsearchemployees_response = new MutableLiveData<>();
    MutableLiveData<Model> updateappointment_response = new MutableLiveData<>();
    MutableLiveData<Model> getmeetingdetails_response = new MutableLiveData<>();
    MutableLiveData<Model1> getoappointments_response = new MutableLiveData<>();

    MutableLiveData<Model1> getvisitorslist_response = new MutableLiveData<>();
    MutableLiveData<Model> gethistorydetails_response = new MutableLiveData<>();
    MutableLiveData<Model> getuserslotdetails_response = new MutableLiveData<>();
    MutableLiveData<Model1> getmeetings_response = new MutableLiveData<>();
    MutableLiveData<Model1> getmeetingrequests_response = new MutableLiveData<>();
    MutableLiveData<Model1> getmeetingapprovals_response = new MutableLiveData<>();
    MutableLiveData<Model> actioncheckinout_response = new MutableLiveData<>();
    MutableLiveData<Model1> getrmslots_response = new MutableLiveData<>();
    MutableLiveData<Model1> getmeetingrooms_response = new MutableLiveData<>();
    MutableLiveData<Model1> getmrmslots_response = new MutableLiveData<>();
    MutableLiveData<Model> updatemeetings_response = new MutableLiveData<>();
    MutableLiveData<Model> reschedulemeeting_response = new MutableLiveData<>();
    MutableLiveData<Model> iresend_response = new MutableLiveData<>();
    MutableLiveData<Model> addcovisitor_response = new MutableLiveData<>();
    MutableLiveData<Model1> getInviteData_response = new MutableLiveData<>();
    MutableLiveData<JsonObject> pdfupload_response = new MutableLiveData<>();
    MutableLiveData<Model> readorunread_response = new MutableLiveData<>();
    MutableLiveData<Model1> getentrypoints_response = new MutableLiveData<>();
    MutableLiveData<Model1> gethostslots_response = new MutableLiveData<>();
    MutableLiveData<TotalModelCount> actionmeetings_total_count_response = new MutableLiveData<>();
    MutableLiveData<Model> actionmeetings_response = new MutableLiveData<>();
    MutableLiveData<Model1> getcategories_response = new MutableLiveData<>();
    MutableLiveData<Model1> getEmployees_response = new MutableLiveData<>();

    ApiRepository apiRepository;

    public ApiViewModel() {
        apiRepository = new ApiRepository();
    }


    //NavigationActivity
    //Versions check
    public void getVersions(Context context) {
        apiRepository.getVersions(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 loginResponse) {
                getVersions_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onResponse: " + "failed");
                ViewController.DismissProgressBar();
            }
        }, context);
    }

    //InitialActivity,OtpActivity
    //login
    public void checkSetup(Context context, CheckSetupModelRequest checkSetupmodelrequest) {
        apiRepository.checkSetup(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                checkSetup_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, checkSetupmodelrequest);
    }

    //InitialActivity,OtpActivity
    //login
    public void appuserlogin(Context context, JsonObject jsonObject) {
        apiRepository.appuserlogin(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                appuserlogin_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }

    //MicrosoftActivity
    //login
    public void userADlogin(Context context, JsonObject jsonObject) {
        apiRepository.userADlogin(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                userADlogin_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onResponse: " + "failed");
                userADlogin_response.postValue(null);
                ViewController.DismissProgressBar();
            }
        }, context, jsonObject);
    }


    //InitialActivity
    //send otp
    public void otpsendemail(Context context, OtpSendEmaiModelRequest otpSendEmaiModelRequest) {
        apiRepository.otpsendemail(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                otpsendemail_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onResponse: " + "failed");
                ViewController.DismissProgressBar();
            }
        }, context, otpSendEmaiModelRequest);
    }

    //OtpActivity,SetPasswordActivity
    //setup login
    public void setuplogin(Context context, SetUpLoginModelRequest setUpLoginModelRequest) {
        apiRepository.setuplogin(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                setuplogin_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, setUpLoginModelRequest);
    }

    //OtpActivity
    //action notification login
    public void actionnotification(Context context, ActionNotificationModelRequest actionNotificationModelRequest) {
        apiRepository.actionnotification(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                actionnotification_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, actionNotificationModelRequest);
    }

    //SetPasswordActivity
    //update pwd
    public void updatepwd(Context context, UpdatePwdModelRequest updatePwdModelRequest) {
        apiRepository.updatepwd(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model loginResponse) {
                updatepwd_response.postValue(loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, updatePwdModelRequest);
    }

    //NavigationActivity
    //getamenities
    public void getazureaddetails(Context context, String camp_id) {
        apiRepository.getazureaddetails(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                getazureaddetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, camp_id);
    }

    //NavigationActivity
    //getamenities
    public void getamenities(Context context) {
        apiRepository.getamenities(new ApiRepository.InviteemodelclassResponse() {
            @Override
            public void onResponse(Inviteemodelclass l_loginResponse) {
                getamenities_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context);
    }

    //NavigationActivity,NotificationsActivity
    //notifications List
    public void getnotifications(Context context, String comp_id, String email) {
        apiRepository.getnotifications(new ApiRepository.Notifications_modelResponse() {
            @Override
            public void onResponse(Notifications_model l_loginResponse) {
                notificationsList_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, comp_id, email);
    }

    //NotificationsActivity
    //notifications Status
    public void notificationsStatus_Change(Context context, NotificationsStatusChangeModelRequest notificationsStatusChangeModelRequest) {
        apiRepository.notificationsStatus_Change(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                notificationsstatuschange_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, notificationsStatusChangeModelRequest);
    }

    //VisitorDetailsActivity
    //employeeformdetails
    public void getemployeeformdetails(Context context) {
        apiRepository.getemployeeformdetails(new ApiRepository.InviteemodelclassResponse() {
            @Override
            public void onResponse(Inviteemodelclass l_loginResponse) {
                employeeformdetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure: failed", t.getMessage() + "");
                Conversions.errroScreen((Activity) context);
                ViewController.DismissProgressBar();
            }
        }, context);
    }

    //VisitorDetailsActivity,MeetingDescriptionActivity
    //Employee Details
    public void getEmployeeDetails(Context context, String Emp_id) {
        apiRepository.getEmployeeDetails(new ApiRepository.JsonObjectResponse() {
            @Override
            public void onResponse(JsonObject l_loginResponse) {
                employeeDetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, Emp_id);
    }

    //VisitorDetailsActivity
    //qrindex
    public void qrindex(Context context, QrIndexModelRequest qrIndexModelRequest) {
        apiRepository.qrindex(new ApiRepository.StringResponses() {
            @Override
            public void onResponse(String qrindex_loginResponse) {
                qrindex_response.postValue(qrindex_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, qrIndexModelRequest);
    }

    //VisitorDetailsActivity
    //qrindex
    public void actionEmployees(Context context, JsonObject gsonObject) {
        apiRepository.actionEmployees(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model response) {
                actionEmployees_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, gsonObject);
    }

    //AppointmentDetailsActivity, SetUpMeetingActivity
    //getappointmentsdetails
    public void getappointmentsdetails(Context context, String m_id) {
        apiRepository.getappointmentsdetails(new ApiRepository.getappointmentsdetails_modelResponse() {
            @Override
            public void onResponse(AppointmentDetailsModel l_loginResponse) {
                getappointmentsdetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, m_id);
    }

    //AppointmentDetailsActivity,MeetingDescriptionActivity,UpComingMeetingsNewFragment
    //get subhierarchys
    public void getsubhierarchys(Context context, String l_id) {
        apiRepository.getsubhierarchys(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getsubhierarchys_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, l_id);
    }

    //AppointmentDetailsActivity,MeetingDescriptionActivity,UpComingMeetingsNewFragment
    //get search employees
    public void getsearchemployees(Context context, String Location, String id) {
        apiRepository.getsearchemployees(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getsearchemployees_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, Location, id);
    }

    //AppointmentDetailsActivity
    //update appointment
    public void updateappointment(Context context, JsonObject jsonObject) {
        apiRepository.updateappointment(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                updateappointment_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }


    //UpComingMeetingsNewFragment,MeetingDescriptionActivity,SetUpMeetingActivity
    //getmeetingdetails
    public void getmeetingdetails(Context context, String meetingsId) {
        apiRepository.getmeetingdetails(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                getmeetingdetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onResponse: " + "failed");
                getmeetingdetails_response.postValue(null);
                ViewController.DismissProgressBar();
            }
        }, context, meetingsId);
    }

    //UpComingMeetingsNewFragment
    //getoappointments
    public void getoappointments(Context context, String meetingsId, String type, String host, String Emp_id) {
        apiRepository.getoappointments(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getoappointments_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, meetingsId, type, host, Emp_id);
    }

    //UpComingMeetingsNewFragment
    //getvisitorslist
    public void getvisitorslist(Context context, String l_id, String id, String type, String maintype, String newmaintype) {
        apiRepository.getvisitorslist(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getvisitorslist_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, l_id, id, type, maintype, newmaintype);
    }


    //CheckinDetails
    //gethistorydetails
    public void gethistorydetails(Context context, String id, String comp_id) {
        apiRepository.gethistorydetails(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                gethistorydetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, id, comp_id);
    }

    //UpComingMeetingsNewFragment, SelectedDateMeetingsActivity
    //getmeetings
    public void getmeetings(Context context, String type, String emp_id, String start, String end) {
        apiRepository.getmeetings(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getmeetings_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, emp_id, start, end);
    }

    //UpComingMeetingsNewFragment
    //get meeting approvals
    public void getmeetingrequests(Context context, String type, String emp_id, String start, String end) {
        apiRepository.getmeetingrequests(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getmeetingrequests_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, emp_id, start, end);
    }


    //UpComingMeetingsNewFragment
    //get meeting approvals
    public void getmeetingapprovals(Context context, String type, String Emp_id, String Roleid, String Location, String Hierarchy_id, String start, String end) {
        apiRepository.getmeetingapprovals(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getmeetingapprovals_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, Emp_id, Roleid, Location, Hierarchy_id, start, end);
    }


    //UpComingMeetingsNewFragment
    //action checkin out
    public void actioncheckinout(Context context, JsonObject jsonObject) {
        apiRepository.actioncheckinout(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                actioncheckinout_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }

    //SlotsActivity
    //getuserslotdetails
    public void getuserslotdetails(Context context, String id, String comp_id) {
        apiRepository.getuserslotdetails(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                getuserslotdetails_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, id, comp_id);
    }


    //MeetingRoomFragment, SetUpMeetingActivity
    //get rmslotss
    public void getrmslots(Context context, String type, Long start, Long end, String rm_id) {
        apiRepository.getrmslots(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getrmslots_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, start, end, rm_id);
    }

    //MeetingRoomFragment,SetUpMeetingActivity
    //get meeting rooms
    public void getmeetingrooms(Context context, String locationId) {
        apiRepository.getmeetingrooms(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getmeetingrooms_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, locationId);
    }


    //MeetingRoomEngagedActivity
    //get mrm slots
    public void getmrmslots(Context context, String type, Long start, Long end, String l_id) {
        apiRepository.getmrmslots(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getmrmslots_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, start, end, l_id);
    }


    //MeetingDescriptionActivity, SetUpMeetingActivity
    //update meetings
    public void updatemeetings(Context context, JsonObject jsonObject) {
        apiRepository.updatemeetings(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                updatemeetings_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onResponse: " + "failed");
                updatemeetings_response.postValue(null);
                ViewController.DismissProgressBar();
            }
        }, context, jsonObject);
    }

    //Reccurence date time
    //reschedule meeting
    public void reschedulemeeting(Context context, JsonObject jsonObject) {
        apiRepository.reschedulemeeting(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                reschedulemeeting_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }


    //MeetingDescriptionActivity
    //iresend
    public void iresend(Context context, JsonObject jsonObject) {
        apiRepository.iresend(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                iresend_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //addcovisitor
    public void addcovisitor(Context context, JsonObject jsonObject) {
        apiRepository.addcovisitor(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model l_loginResponse) {
                addcovisitor_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //getInviteData
    public void getInviteData(Context context, String type, String usertype, String S_value) {
        apiRepository.getInviteData(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getInviteData_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, usertype, S_value);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //pdfupload
    public void pdfupload(Context context, MultipartBody.Part file, RequestBody fname, RequestBody cid) {
        apiRepository.pdfupload(new ApiRepository.JsonObjectResponse() {
            @Override
            public void onResponse(JsonObject response) {
                pdfupload_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, file, fname, cid);
    }

    //MeetingDescriptionActivity
    //readorunread
    public void readorunread(Context context, JsonObject jsonObject) {
        apiRepository.readorunread(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model response) {
                readorunread_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }

    //SetUpMeetingActivity
    //get entry points
    public void getentrypoints(Context context, String locationId) {
        apiRepository.getentrypoints(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 response) {
                getentrypoints_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, locationId);
    }


    //SetUpMeetingActivity
    //get host slots
    public void gethostslots(Context context, String type, String emp_id, String email, Long start, Long end) {
        apiRepository.gethostslots(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 response) {
                gethostslots_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, type, emp_id, email, start, end);
    }


    //SetUpMeetingActivity
    //action meetings total count
    public void actionmeetings_total_count(Context context, JsonObject jsonObject) {
        apiRepository.actionmeetings_total_count(new ApiRepository.TotalModelCountResponse() {
            @Override
            public void onResponse(TotalModelCount response) {
                actionmeetings_total_count_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }


    //SetUpMeetingActivity
    //action meetings
    public void actionmeetings(Context context, JsonObject jsonObject) {
        apiRepository.actionmeetings(new ApiRepository.ModelResponse() {
            @Override
            public void onResponse(Model response) {
                actionmeetings_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, jsonObject);
    }

    //SetUpMeetingActivity
    //get categories
    public void getcategories(Context context, String id) {
        apiRepository.getcategories(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 response) {
                getcategories_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, id);
    }

    //MeetingDescriptionActivity, SetUpMeetingActivity
    //update meetings
    public void getEmployees(Context context, String comp_id) {
        apiRepository.getEmployees(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getEmployees_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
            }
        }, context, comp_id);
    }


    //Versions Response
    public LiveData<Model1> getVersions_response() {
        return getVersions_response;
    }


    //login Response
    public LiveData<Model> getcheckSetupResponse() {
        return checkSetup_response;
    }

    //login Response
    public LiveData<Model> getappuserloginResponse() {
        return appuserlogin_response;
    }

    //MicrosoftAd Response
    public LiveData<Model> getuserADloginResponse() {
        return userADlogin_response;
    }


    //otpsend response
    public LiveData<Model> getotpsendemailResponse() {
        return otpsendemail_response;
    }

    //setup login response
    public LiveData<Model> getsetuploginResponse() {
        return setuplogin_response;
    }

    //action notification response
    public LiveData<Model> getactionnotificationResponse() {
        return actionnotification_response;
    }

    //getup date pwd response
    public LiveData<Model> getupdatepwdResponse() {
        return updatepwd_response;
    }

    //get amenities response
    public LiveData<Inviteemodelclass> getamenitiesResponse() {
        return getamenities_response;
    }


    //get azuread details response
    public LiveData<Model> getazureaddetailsResponse() {
        return getazureaddetails_response;
    }

    //get notifications List response
    public LiveData<Notifications_model> getnotificationsListResponse() {
        return notificationsList_response;
    }

    //get notification status change
    public LiveData<Model> getnotificationsstatuschangeResponse() {
        return notificationsstatuschange_response;
    }

    //get employee form details
    public LiveData<Inviteemodelclass> getemployeeformdetailsResponse() {
        return employeeformdetails_response;
    }

    //get Employee Details
    public LiveData<JsonObject> getEmployeeDetailsResponse() {
        return employeeDetails_response;
    }

    //qrindex
    public LiveData<String> qrindexResponse() {
        return qrindex_response;
    }

    //action Employees
    public LiveData<Model> actionEmployeesResponse() {
        return actionEmployees_response;
    }

    //appointments details
    public LiveData<AppointmentDetailsModel> getappointmentsdetails_response() {
        return getappointmentsdetails_response;
    }

    //subhierarchys
    public LiveData<Model1> getsubhierarchys_response() {
        return getsubhierarchys_response;
    }

    //search employees
    public LiveData<Model1> getsearchemployees_response() {
        return getsearchemployees_response;
    }

    //update appointment
    public LiveData<Model> updateappointment_response() {
        return updateappointment_response;
    }

    //update appointment
    public LiveData<Model> getmeetingdetails_response() {
        return getmeetingdetails_response;
    }

    //get appointments
    public LiveData<Model1> getoappointments_response() {
        return getoappointments_response;
    }

    //get visitors list
    public LiveData<Model1> getvisitorslist_response() {
        return getvisitorslist_response;
    }

    //get visitors list
    public LiveData<Model> gethistorydetails_response() {
        return gethistorydetails_response;
    }

    //get userslot details
    public LiveData<Model> getuserslotdetails_response() {
        return getuserslotdetails_response;
    }

    //get meetings
    public LiveData<Model1> getmeetings_response() {
        return getmeetings_response;
    }

    //get meeting requests
    public LiveData<Model1> getmeetingrequests_response() {
        return getmeetingrequests_response;
    }

    //get meeting approvals
    public LiveData<Model1> getmeetingapprovals_response() {
        return getmeetingapprovals_response;
    }

    //action checkin out
    public LiveData<Model> actioncheckinout_response() {
        return actioncheckinout_response;
    }

    //get rmslots
    public LiveData<Model1> getrmslots_response() {
        return getrmslots_response;
    }

    //get meeting rooms
    public LiveData<Model1> getmeetingrooms_response() {
        return getmeetingrooms_response;
    }

    //get meeting rooms
    public LiveData<Model1> getmrmslots_response() {
        return getmrmslots_response;
    }

    //update meetings
    public LiveData<Model> updatemeetings_response() {
        return updatemeetings_response;
    }

    //update meetings
    public LiveData<Model> reschedulemeeting_response() {
        return reschedulemeeting_response;
    }


    //iResend
    public LiveData<Model> iresend_response() {
        return iresend_response;
    }

    //add covisitor
    public LiveData<Model> addcovisitor_response() {
        return addcovisitor_response;
    }

    //get Invite Data
    public LiveData<Model1> getInviteData_response() {
        return getInviteData_response;
    }

    //get Invite Data
    public LiveData<JsonObject> pdfupload_response() {
        return pdfupload_response;
    }

    //read orunread
    public LiveData<Model> readorunread_response() {
        return readorunread_response;
    }


    //get entry points
    public LiveData<Model1> getentrypoints_response() {
        return getentrypoints_response;
    }

    //get host slots
    public LiveData<Model1> gethostslots_response() {
        return gethostslots_response;
    }

    //action meetings total count
    public LiveData<TotalModelCount> actionmeetings_total_count_response() {
        return actionmeetings_total_count_response;
    }

    //action meetings
    public LiveData<Model> actionmeetings_response() {
        return actionmeetings_response;
    }


    //get categories
    public LiveData<Model1> getcategories_response() {
        return getcategories_response;
    }


    //otpsend response
    public LiveData<Model1> getEmployees_response() {
        return getEmployees_response;
    }

}
