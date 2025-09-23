package com.provizit.MVVM;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.provizit.AdapterAndModel.BusySchedules.BusySchedulesModel;
import com.provizit.AdapterAndModel.CompanyDetailsModel;
import com.provizit.AdapterAndModel.GetSearchEmployeesModel;
import com.provizit.AdapterAndModel.GetdocumentsModel;
import com.provizit.AdapterAndModel.GetsubhierarchysModel;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsModel;
import com.provizit.AdapterAndModel.MaterialModel;
import com.provizit.AdapterAndModel.WorkVisitTypeModel;
import com.provizit.AdapterAndModel.WorkingDaysModal;
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
    MutableLiveData<Model1> getTrainingTitles_response = new MutableLiveData<>();
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
    MutableLiveData<HostSlotsModel> gethostslots_response = new MutableLiveData<>();
    MutableLiveData<TotalModelCount> actionmeetings_total_count_response = new MutableLiveData<>();
    MutableLiveData<Model> actionmeetings_response = new MutableLiveData<>();
    MutableLiveData<Model1> getcategories_response = new MutableLiveData<>();
    MutableLiveData<Model1> getEmployees_response = new MutableLiveData<>();
    MutableLiveData<BusySchedulesModel> getbusySchedulesdetails_response = new MutableLiveData<>();
    MutableLiveData<BusySchedulesModel> actionbusySchedule_response = new MutableLiveData<>();

    MutableLiveData<CompanyDetailsModel> getuserDetailsworkMeterial_response = new MutableLiveData<>();
    MutableLiveData<WorkVisitTypeModel> getworktypes_response = new MutableLiveData<>();
    MutableLiveData<WorkVisitTypeModel> getworklocation_response = new MutableLiveData<>();
    MutableLiveData<WorkVisitTypeModel> getworkpurposes_response = new MutableLiveData<>();
    MutableLiveData<WorkVisitTypeModel> actionworkpermita_response = new MutableLiveData<>();
    MutableLiveData<MaterialModel> getrefdocuments_response = new MutableLiveData<>();
    MutableLiveData<MaterialModel> getentrypurposes_response = new MutableLiveData<>();
    MutableLiveData<MaterialModel> getexitpurposes_response = new MutableLiveData<>();
    MutableLiveData<GetsubhierarchysModel> getsubhierarchysmaterial_response = new MutableLiveData<>();
    MutableLiveData<MaterialModel> actionentrypermitrequest_response = new MutableLiveData<>();
    MutableLiveData<GetSearchEmployeesModel> getsearchemployeesmaterial_response = new MutableLiveData<>();
    MutableLiveData<GetdocumentsModel> getdocuments_response = new MutableLiveData<>();
    MutableLiveData<WorkingDaysModal> getworkingdays_response = new MutableLiveData<>();
    MutableLiveData<CompanyDetailsModel> getuserDetails_response = new MutableLiveData<>();

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
                getVersions_response.postValue(null);
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
                Log.e(TAG, "onResponse:checkSetup " + t.getMessage());
                checkSetup_response.postValue(null);
            }
        }, context, checkSetupmodelrequest);
    }

    //InitialActivity
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
                appuserlogin_response.postValue(null);
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
                otpsendemail_response.postValue(null);
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
                setuplogin_response.postValue(null);
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
                actionnotification_response.postValue(null);
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
                updatepwd_response.postValue(null);
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
                getazureaddetails_response.postValue(null);
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
                getamenities_response.postValue(null);
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
                notificationsList_response.postValue(null);
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
                notificationsstatuschange_response.postValue(null);
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
                ViewController.DismissProgressBar();
                employeeformdetails_response.postValue(null);
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
                Log.e(TAG, "onResponse: " + "failed");
                ViewController.DismissProgressBar();
                employeeDetails_response.postValue(null);
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
                qrindex_response.postValue(null);
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
                actionEmployees_response.postValue(null);
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
                getappointmentsdetails_response.postValue(null);
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
                getsubhierarchys_response.postValue(null);
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
                getsearchemployees_response.postValue(null);
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
                Conversions.errroScreen((Activity) context, t.getMessage() + "");
                Log.e(TAG, "onResponse: " + "failed");
                updateappointment_response.postValue(null);
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
                getoappointments_response.postValue(null);
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
                getvisitorslist_response.postValue(null);
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
                gethistorydetails_response.postValue(null);
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
                getmeetings_response.postValue(null);
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
                getmeetingrequests_response.postValue(null);
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
                getmeetingapprovals_response.postValue(null);
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
                actioncheckinout_response.postValue(null);
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
                getuserslotdetails_response.postValue(null);
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
                getrmslots_response.postValue(null);
            }
        }, context, type, start, end, rm_id);
    }

    //MeetingTrainingTitles,SetUpTrainingActivity
    //get TrainingTitles
    public void getTrainingTitles(Context context) {
        apiRepository.getTrainingTitles(new ApiRepository.Model1Response() {
            @Override
            public void onResponse(Model1 l_loginResponse) {
                getTrainingTitles_response.postValue(l_loginResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
                getTrainingTitles_response.postValue(null);
            }
        }, context);
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
                getmeetingrooms_response.postValue(null);
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
                getmrmslots_response.postValue(null);
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
                Log.e(TAG, "onerror:updatemeetings " + t.getMessage() + "");
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
                reschedulemeeting_response.postValue(null);
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
                iresend_response.postValue(null);
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
                addcovisitor_response.postValue(null);
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
                getInviteData_response.postValue(null);
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
                pdfupload_response.postValue(null);
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
                readorunread_response.postValue(null);
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
                getentrypoints_response.postValue(null);
            }
        }, context, locationId);
    }


    //SetUpMeetingActivity
    //get host slots
    public void gethostslots(Context context, String type, String emp_id, String email, Long start, Long end) {
        apiRepository.gethostslots(new ApiRepository.HostSlotsModelResponse() {
            @Override
            public void onResponse(HostSlotsModel response) {
                gethostslots_response.postValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onerrorgethostslots " + t.getMessage());
                gethostslots_response.postValue(null);
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
                actionmeetings_total_count_response.postValue(null);
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
                actionmeetings_response.postValue(null);
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
                getcategories_response.postValue(null);
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
                getEmployees_response.postValue(null);
            }
        }, context, comp_id);
    }

    //BusyScheduleActivity
    //busy Schedules
    public void getbusyScheduledetails(Context context, String comp_id, String emp_id, String type) {
        apiRepository.getbusyScheduledetails(new ApiRepository.BusySchedulesResponse() {
            @Override
            public void onResponse(BusySchedulesModel l_loginResponse) {
                getbusySchedulesdetails_response.postValue(l_loginResponse);
            }
            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
                getbusySchedulesdetails_response.postValue(null);
            }
        }, context, comp_id, emp_id, type);
    }

    //BusyScheduleActivity
    //busy Schedules delete
    public void actionbusySchedule(Context context, JsonObject jsonObject) {
        apiRepository.actionbusySchedule(new ApiRepository.BusySchedulesResponse() {
            @Override
            public void onResponse(BusySchedulesModel busySchedulesModel) {
                actionbusySchedule_response.postValue(busySchedulesModel);
            }
            @Override
            public void onFailure(Throwable t) {
                ViewController.DismissProgressBar();
                Log.e(TAG, "onResponse: " + "failed");
                actionbusySchedule_response.postValue(null);
            }
        }, context, jsonObject);
    }


    //work and material
    public void getuserDetailsworkMeterial(Context context, String type) {
        apiRepository.getuserDetailsworkMeterial(new ApiRepository.getuserDetailsworkMeterial_ModelResponse() {
            @Override
            public void onResponse(CompanyDetailsModel entryPermitModel) {
                getuserDetailsworkMeterial_response.postValue(entryPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, type);

    }


    public void getworktypes(Context context, String id) {
        apiRepository.getworktypes(new ApiRepository.getworktypes_ModelResponse() {
            @Override
            public void onResponse(WorkVisitTypeModel entryPermitModel) {
                getworktypes_response.postValue(entryPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id);

    }


    public void getworklocation(Context context, String id) {
        apiRepository.getworklocation(new ApiRepository.getworktypes_ModelResponse() {
            @Override
            public void onResponse(WorkVisitTypeModel entryPermitModel) {
                getworklocation_response.postValue(entryPermitModel);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id);

    }


    public void getworkpurposes(Context context, String id) {
        apiRepository.getworkpurposes(new ApiRepository.getworktypes_ModelResponse() {
            @Override
            public void onResponse(WorkVisitTypeModel entryPermitModel) {
                getworkpurposes_response.postValue(entryPermitModel);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id);

    }

    public void actionworkpermita(Context context, JsonObject jsonObject) {
        apiRepository.actionworkpermita(new ApiRepository.actionworkpermita_ModelResponse() {
            @Override
            public void onResponse(WorkVisitTypeModel entryPermitModel) {
                actionworkpermita_response.postValue(entryPermitModel);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        }, context, jsonObject);
    }


    public void getrefdocuments(Context context, String id) {
        apiRepository.getrefdocuments(new ApiRepository.getMaterialModel_ModelResponse() {
            @Override
            public void onResponse(MaterialModel model) {
                getrefdocuments_response.postValue(model);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id);

    }

    public void getentrypurposes(Context context, String id) {
        apiRepository.getentrypurposes(new ApiRepository.getMaterialModel_ModelResponse() {
            @Override
            public void onResponse(MaterialModel model) {
                getentrypurposes_response.postValue(model);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id);

    }

    public void getexitpurposes(Context context, String id) {
        apiRepository.getexitpurposes(new ApiRepository.getMaterialModel_ModelResponse() {
            @Override
            public void onResponse(MaterialModel model) {
                getexitpurposes_response.postValue(model);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, id);

    }


    public void getsubhierarchysmaterial(Context context, String id, String indexid) {
        apiRepository.getsubhierarchysmaterial(new ApiRepository.GetsubhierarchysResponse() {
            @Override
            public void onResponse(GetsubhierarchysModel getsubhierarchysModel) {
                getsubhierarchysmaterial_response.postValue(getsubhierarchysModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, id, indexid);
    }

    public void getsearchemployeesmaterial(Context context, String l_id, String h_id, String type) {
        apiRepository.getsearchemployeesmaterial(new ApiRepository.SearchEmployeesResponse() {
            @Override
            public void onResponse(GetSearchEmployeesModel getSearchEmployeesModel) {
                getsearchemployeesmaterial_response.postValue(getSearchEmployeesModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context, l_id, h_id, type);

    }

    public void actionentrypermitrequest(Context context, JsonObject jsonObject) {
        apiRepository.actionentrypermitrequest(new ApiRepository.actionentrypermitrequest_ModelResponse() {
            @Override
            public void onResponse(MaterialModel entryPermitModel) {
                actionentrypermitrequest_response.postValue(entryPermitModel);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        }, context, jsonObject);
    }


    public void getdocuments(Context context) {
        apiRepository.getdocuments(new ApiRepository.SelectedId_listResponse() {
            @Override
            public void onResponse(GetdocumentsModel getdocumentsModel) {
                getdocuments_response.postValue(getdocumentsModel);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, context);
    }

    public void getworkingdays(Context context, String comp_id) {
        apiRepository.getworkingdays(new ApiRepository.getworkingdays_ModelResponse() {
            @Override
            public void onResponse(WorkingDaysModal entryPermitModel) {
                getworkingdays_response.postValue(entryPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, comp_id);

    }

    //suppliers
    public void getuserDetails(Context context, String type) {
        apiRepository.getuserDetails(new ApiRepository.getuserDetails_ModelResponse() {
            @Override
            public void onResponse(CompanyDetailsModel entryPermitModel) {
                getuserDetails_response.postValue(entryPermitModel);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "progress: " + t);
            }
        }, context, type);

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

    //get Training Titles
    public LiveData<Model1> getTrainingTitles_response() {
        return getTrainingTitles_response;
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
    public LiveData<HostSlotsModel> gethostslots_response() {
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


    //busy response
    public LiveData<BusySchedulesModel> getbusySchedulesdetails_response() {
        return getbusySchedulesdetails_response;
    }

    //delete busy response
    public LiveData<BusySchedulesModel> actionbusySchedule_response() {
        return actionbusySchedule_response;
    }

    //work and material
    public LiveData<CompanyDetailsModel> getuserDetailsworkMeterial_response(){
        return getuserDetailsworkMeterial_response;
    }

    public LiveData<WorkVisitTypeModel> getworktypes_response(){
        return getworktypes_response;
    }

    public LiveData<WorkVisitTypeModel> getworklocation_response(){
        return getworklocation_response;
    }

    public LiveData<WorkVisitTypeModel> getworkpurposes_response(){
        return getworkpurposes_response;
    }

    public LiveData<WorkVisitTypeModel> actionworkpermita_response(){
        return actionworkpermita_response;
    }

    public LiveData<MaterialModel> getrefdocuments_response(){
        return getrefdocuments_response;
    }

    public LiveData<MaterialModel> getentrypurposes_response(){
        return getentrypurposes_response;
    }

    public LiveData<MaterialModel> getexitpurposes_response(){
        return getexitpurposes_response;
    }

    public LiveData<GetsubhierarchysModel> getsubhierarchysmaterial_response() {
        return getsubhierarchysmaterial_response;
    }

    public LiveData<MaterialModel> actionentrypermitrequest_response() {
        return actionentrypermitrequest_response;
    }

    public LiveData<GetSearchEmployeesModel> getsearchemployeesmaterial_response() {
        return getsearchemployeesmaterial_response;
    }

    public LiveData<GetdocumentsModel> getResponseforSelectedId_list() {
        return getdocuments_response;
    }

    public LiveData<WorkingDaysModal> getworkingdays_response(){
        return getworkingdays_response;
    }

    public LiveData<CompanyDetailsModel> getuserDetails_response(){
        return getuserDetails_response;
    }

}
