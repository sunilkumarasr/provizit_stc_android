package com.provizit.Activities;

import static android.view.View.GONE;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.provizit.AdapterAndModel.ContractorAdapter;
import com.provizit.AdapterAndModel.Getdocuments;
import com.provizit.AdapterAndModel.Getnationality;
import com.provizit.AdapterAndModel.LocationAddressList;
import com.provizit.AdapterAndModel.NationalityStaticFile;
import com.provizit.AdapterAndModel.SubContractorAdapter;
import com.provizit.AdapterAndModel.WorkVisitTypeList;
import com.provizit.Config.DateRangeTimestamps;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Utilities.Contractor;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.MobileData;
import com.provizit.Utilities.RoleDetails;
import com.provizit.Utilities.SubContractor;
import com.provizit.Utilities.WorkingDaysList;
import com.provizit.databinding.ActivityWorkPermitSetUpBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WorkPermitSetUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityWorkPermitSetUpBinding binding;

    BroadcastReceiver broadcastReceiver;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;
    SharedPreferences.Editor editor1;

    String compId = "";

    RoleDetails roleDetails;
    AnimationSet animation;

    //Contractor
    String ContractorSelectId;
    String ContractorPosition;
    String ContractorNationality;
    List<Contractor> contractorList = new ArrayList<>();

    //subContractor
    String SubContractorSelectId;
    String SubContractorPosition;
    String SubContractorNationality;
    List<SubContractor> subContractorList = new ArrayList<>();

    String locationItem;
    String locationIndexPosition;


    String StatusEditScopeOfWork = "";
    String StatusEditPlaceOfWork = "";
    String workVisitType = "";
    String placeOfWorkVisitType = "";
    String scopeOfWorkTextArea = "";

    //Date
    Calendar fromDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    Calendar toDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    String fromSelectDate = "";
    String toSelectDate = "";
    String fromDateTimeStamp = "";
    String toDateTimeStamp = "";

    //checkBox
    ArrayList<WorkingDaysList> WorkingHRWorkingList = new ArrayList<>();

    //time List
    String fromSelectTime = "";
    String toSelectTime = "";
    List<String> allTimeList = new ArrayList<>();
    List<String> filteredStartList = new ArrayList<>(allTimeList);

    ApiViewModel apiViewModel;

    String contractorCompName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkPermitSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(WorkPermitSetUpActivity.this, R.color.colorPrimary));



        inits();
    }

    private void inits() {
        ViewController.barPrimaryColor(WorkPermitSetUpActivity.this);
        ViewController.internetConnection(broadcastReceiver, WorkPermitSetUpActivity.this);
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        editor1 = sharedPreferences1.edit();
        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        roleDetails = new RoleDetails();
        empData = myDb.getEmpdata();
        roleDetails = myDb.getRole();
        animation = Conversions.animation();

        compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");

        apiViewModel = new ViewModelProvider(WorkPermitSetUpActivity.this).get(ApiViewModel.class);


        //Spinners List Set
        SpinnersListApis();
        //Time List start and End
        StartAndEndTimeList(true);


        //current Date from
        fromDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int fromYear  = fromDateCalendar.get(Calendar.YEAR);
        int fromMonth = fromDateCalendar.get(Calendar.MONTH);
        int fromDay   = fromDateCalendar.get(Calendar.DAY_OF_MONTH);
        //date
        fromSelectDate = fromDay + "/" + (fromMonth + 1) + "/" + fromYear;
        binding.txtFromDate.setText(fromSelectDate);
        //(12:00 AM UTC)
        fromDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromDateCalendar.set(Calendar.MINUTE, 0);
        fromDateCalendar.set(Calendar.SECOND, 0);
        fromDateCalendar.set(Calendar.MILLISECOND, 0);
         //timestamp (seconds)
        long fromtimestamp = fromDateCalendar.getTimeInMillis() / 1000;
        fromDateTimeStamp = String.valueOf(fromtimestamp);
        Log.e("fromTimestampUTC", fromDateTimeStamp);


        //toDate
        toDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int year  = toDateCalendar.get(Calendar.YEAR);
        int month = toDateCalendar.get(Calendar.MONTH);
        int day   = toDateCalendar.get(Calendar.DAY_OF_MONTH);
        toSelectDate = day + "/" + (month + 1) + "/" + year;
        binding.txtToDate.setText(toSelectDate);
        //(11:59:59 PM UTC)
        toDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        toDateCalendar.set(Calendar.MINUTE, 59);
        toDateCalendar.set(Calendar.SECOND, 59);
        toDateCalendar.set(Calendar.MILLISECOND, 999);
        // Save calendar reference if needed
        toDateCalendar = (Calendar) toDateCalendar.clone();
        //timestamp (seconds)
        long timestamp = toDateCalendar.getTimeInMillis() / 1000;
        toDateTimeStamp = String.valueOf(timestamp);
        Log.e("toTimestampUTC", toDateTimeStamp);


        binding.checkBox24HR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.checkBoxWorkingHR.setChecked(false);
                    binding.linearTime.setVisibility(GONE);
                } else {
                    if (binding.checkBoxWorkingHR.isChecked()) {
                        binding.linearTime.setVisibility(GONE);
                    } else {
                        binding.linearTime.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        binding.checkBoxWorkingHR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.checkBox24HR.setChecked(false);
                    binding.linearTime.setVisibility(GONE);
                } else {
                    if (binding.checkBox24HR.isChecked()) {
                        binding.linearTime.setVisibility(GONE);
                    } else {
                        binding.linearTime.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        //submit workPermit form
        apiViewModel.actionworkpermita_response().observe(this, model -> {
            ViewController.DismissProgressBar();
            if (model.getResult().equals(200)){
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "Failed Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });


        binding.imgBack.setOnClickListener(this);
        binding.linearContractor.setOnClickListener(this);
        binding.linearSubContractor.setOnClickListener(this);
        binding.linearSubContractor.setOnClickListener(this);
        binding.txtFromDate.setOnClickListener(this);
        binding.txtToDate.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                view.startAnimation(animation);
                finish();
                break;
            case R.id.linearContractor:
                view.startAnimation(animation);
                addContractorBottomSheetDialog();
                break;
            case R.id.linearSubContractor:
                view.startAnimation(animation);
                addSubContractorBottomSheetDialog();
                break;
            case R.id.txtFromDate:
                showFromDatePicker();
                break;
            case R.id.txtToDate:
                showToDatePicker();
                break;
            case R.id.btnNext:

                if (StatusEditPlaceOfWork.equalsIgnoreCase("true")) {
                    if (!binding.EditPlaceOfWork.getText().toString().equalsIgnoreCase("")){
                        placeOfWorkVisitType = binding.EditPlaceOfWork.getText().toString();
                    }
                }
                if (StatusEditScopeOfWork.equalsIgnoreCase("true")) {
                    if (!binding.EditScopeOfWork.getText().toString().equalsIgnoreCase("")){
                        scopeOfWorkTextArea = binding.EditScopeOfWork.getText().toString();
                    }
                }


                if (contractorList.isEmpty() && subContractorList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Contractor", Toast.LENGTH_SHORT).show();
                } else if (locationItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Location", Toast.LENGTH_SHORT).show();
                } else if (workVisitType.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Work / visit Type", Toast.LENGTH_SHORT).show();
                } else if (placeOfWorkVisitType.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Place of work/visit Type", Toast.LENGTH_SHORT).show();
                } else if (StatusEditPlaceOfWork.equalsIgnoreCase("true") && binding.EditPlaceOfWork.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Place of work/visit Type", Toast.LENGTH_SHORT).show();
                } else if (scopeOfWorkTextArea.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Scope of work text area", Toast.LENGTH_SHORT).show();
                } else if (StatusEditScopeOfWork.equalsIgnoreCase("true") && binding.EditScopeOfWork.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Scope of work text area", Toast.LENGTH_SHORT).show();
                } else if (binding.txtFromDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                } else if (binding.txtToDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                } else {

                    JsonObject json = createWorkPermitSubmit();
                    apiViewModel.actionworkpermita(getApplicationContext(), json);
                    ViewController.ShowProgressBar(WorkPermitSetUpActivity.this);

                }
                break;
        }
    }


    //Json
    private JsonObject createWorkPermitSubmit() {

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONObject contractorData_ = new JSONObject();
        JSONArray approveStatistics = new JSONArray();
        JSONArray assignDepartments = new JSONArray();
        JSONArray contractors_Data = new JSONArray();
        JSONArray sub_ContractorsData = new JSONArray();
        JSONArray startTimesArray = new JSONArray();
        JSONArray endTimesArray = new JSONArray();

        //empty contractorData
        try {
            contractorData_.put("name", "");
            contractorData_.put("company", "");
            contractorData_.put("id_type", "");
            contractorData_.put("id_name", "");
            contractorData_.put("nationality", "");
            contractorData_.put("id_number", "");
            contractorData_.put("email", "");
            contractorData_.put("mobile", "");
            contractorData_.put("mobileData","");
            contractorData_.put("performing_work", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //contractor List
        Gson gson = new Gson();
        try {
            for (Contractor contractor : contractorList) {
                String jsonString = gson.toJson(contractor);
                JSONObject jsonObject = new JSONObject(jsonString);
                contractors_Data.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //sub-Contractor List
        try {
            sub_ContractorsData = new JSONArray();
            for (int i = 0; i < subContractorList.size(); i++) {
                sub_ContractorsData.put(subContractorList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //company name
        String companyName = "";
        if (!contractorList.isEmpty()) {
            companyName = contractorCompName;
        } else if (!subContractorList.isEmpty()) {
            companyName = subContractorList.get(0).companyName;
        }

        //checkBox conditions
        boolean workingHours;
        boolean twentyFourHours;
        if (binding.checkBoxWorkingHR.isChecked()) {
            workingHours = true;
            fromSelectTime = "00:00";
            toSelectTime = "23:59";
        } else {
            workingHours = false;
            fromSelectTime = binding.spinnerStartTime.getSelectedItem().toString();
            toSelectTime = binding.spinnerEndTime.getSelectedItem().toString();
        }
        if (binding.checkBox24HR.isChecked()) {
            twentyFourHours = true;
            fromSelectTime = WorkingHRWorkingList.get(1).getStart();
            toSelectTime = WorkingHRWorkingList.get(1).getEnd();
        } else {
            twentyFourHours = false;
            fromSelectTime = binding.spinnerStartTime.getSelectedItem().toString();
            toSelectTime = binding.spinnerEndTime.getSelectedItem().toString();
        }

        //start and end datetime array list
        //start Date 8/08/2025 and Start Time 10:15
        String StartDateTimeArray = DateRangeTimestamps.getTimestampArrayString(fromSelectDate, fromSelectTime, toSelectDate, toSelectTime);
        try {
            startTimesArray = new JSONArray(StartDateTimeArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //End and Start datetime array list
        //start Date 8/08/2025 and End Time 10:15
        String EndDateTimeArray = DateRangeTimestamps.getTimestampArrayString(fromSelectDate, toSelectTime, toSelectDate, toSelectTime);
        try {
            endTimesArray = new JSONArray(EndDateTimeArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObj_.put("worktype", workVisitType);
            jsonObj_.put("c_empId", empData.getEmp_id());
            jsonObj_.put("sc_empId", "");
            jsonObj_.put("comp_id", "");
            jsonObj_.put("work_scope", scopeOfWorkTextArea);
            jsonObj_.put("sc_name", "");
            jsonObj_.put("sc_email", "");
            jsonObj_.put("sc_mobileData", "");
            jsonObj_.put("sc_mobile", "");
            jsonObj_.put("work_loc", placeOfWorkVisitType);
            jsonObj_.put("d_start", Long.parseLong(fromDateTimeStamp));
            jsonObj_.put("d_end", Long.parseLong(toDateTimeStamp));
            jsonObj_.put("t_start", fromSelectTime);
            jsonObj_.put("t_end", toSelectTime);
            jsonObj_.put("sc_perwork", "");
            jsonObj_.put("starts", startTimesArray);
            jsonObj_.put("ends", endTimesArray);
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("c_workpermit", "");
            jsonObj_.put("sc_status", true);
            jsonObj_.put("contractorData", contractorData_);
            jsonObj_.put("contractorsData", contractors_Data);
            jsonObj_.put("companyName", companyName);
            jsonObj_.put("subcontractorsData", sub_ContractorsData);
            jsonObj_.put("approver_statistics", approveStatistics);
            jsonObj_.put("assign_departments", assignDepartments);
            jsonObj_.put("l_id", locationIndexPosition);
            jsonObj_.put("workinghours", workingHours);
            jsonObj_.put("twentyfourhours", twentyFourHours);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("createjsongsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    //Spinners List Set
    private void SpinnersListApis() {

        //Working Hours Only
        apiViewModel.getworkingdays(getApplicationContext(), compId);
        apiViewModel.getworkingdays_response().observe(this, dModel -> {
            WorkingHRWorkingList = dModel.getItems().get(0).getWorkingdays();
        });

        //Location
        apiViewModel.getuserDetailsworkMeterial(getApplicationContext(), "address");
        apiViewModel.getuserDetailsworkMeterial_response().observe(this, dModel -> {
            ArrayList<LocationAddressList> documentsList = dModel.getItems().getAddress();
            ArrayList<String> visitTypeList = new ArrayList<>();

            // Prepare list for spinner
            for (LocationAddressList doc : documentsList) {
                visitTypeList.add(doc.getName());
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, visitTypeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerLocation.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    locationIndexPosition = position + "";
                    locationItem = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Work/visit Type
        apiViewModel.getworktypes(getApplicationContext(), compId);
        apiViewModel.getworktypes_response().observe(this, dModel -> {
            ArrayList<WorkVisitTypeList> documentsList = dModel.getItems();
            ArrayList<String> visitTypeList = new ArrayList<>();

            // Prepare list for spinner
            for (WorkVisitTypeList doc : documentsList) {
                if (doc.getActive()) {
                    visitTypeList.add(doc.getName());
                }
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, visitTypeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerVisitType.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerVisitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedName = (String) parent.getItemAtPosition(position);

                    for (WorkVisitTypeList item : documentsList) {
                        if (item.getActive() && item.getName().equals(selectedName)) {
                            workVisitType = item.get_id().get$oid();
                            break;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Place of work/visit Type
        apiViewModel.getworklocation(getApplicationContext(), compId);
        apiViewModel.getworklocation_response().observe(this, dModel -> {
            ArrayList<WorkVisitTypeList> documentsList = dModel.getItems();
            ArrayList<String> PlaceOfWorkVisitTypeList = new ArrayList<>();

            // Prepare list for spinner
            for (WorkVisitTypeList doc : documentsList) {
                if (doc.getActive()) {
                    PlaceOfWorkVisitTypeList.add(doc.getName());
                }
            }
            PlaceOfWorkVisitTypeList.add("Others");

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, PlaceOfWorkVisitTypeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerPlaceOfWork.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerPlaceOfWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    placeOfWorkVisitType = parent.getItemAtPosition(position).toString();
                    if (placeOfWorkVisitType.equalsIgnoreCase("Others")) {
                        StatusEditPlaceOfWork = "true";
                        binding.EditPlaceOfWork.setVisibility(View.VISIBLE);
                    } else {
                        StatusEditPlaceOfWork = "";
                        binding.EditPlaceOfWork.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Scope of work text area
        apiViewModel.getworkpurposes(getApplicationContext(), compId);
        apiViewModel.getworkpurposes_response().observe(this, dModel -> {
            ArrayList<WorkVisitTypeList> documentsList = dModel.getItems();
            ArrayList<String> ScopeOfWorkList = new ArrayList<>();

            // Prepare list for spinner
            for (WorkVisitTypeList doc : documentsList) {
                if (doc.getActive()) {
                    ScopeOfWorkList.add(doc.getName());
                }
            }
            ScopeOfWorkList.add("Others");

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, ScopeOfWorkList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerSelectScopeOfWork.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerSelectScopeOfWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    scopeOfWorkTextArea = parent.getItemAtPosition(position).toString();
                    if (scopeOfWorkTextArea.equalsIgnoreCase("Others")) {
                        StatusEditScopeOfWork = "true";
                        binding.EditScopeOfWork.setVisibility(View.VISIBLE);
                    } else {
                        StatusEditScopeOfWork = "";
                        binding.EditScopeOfWork.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

    }

    private void showFromDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {

                    //UTC Calendar to avoid timezone issues
                    fromDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    fromDateCalendar.set(year, month, dayOfMonth);

                    //(12:00 AM UTC)
                    fromDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    fromDateCalendar.set(Calendar.MINUTE, 0);
                    fromDateCalendar.set(Calendar.SECOND, 0);
                    fromDateCalendar.set(Calendar.MILLISECOND, 0);

                    //date
                    fromSelectDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    binding.txtFromDate.setText(fromSelectDate);

                    //timestamp (seconds)
                    long timestamp = fromDateCalendar.getTimeInMillis() / 1000;
                    fromDateTimeStamp = String.valueOf(timestamp);
                    Log.e("fromTimestampUTC", fromDateTimeStamp);

                    // Reset To Date
                    binding.txtToDate.setText("");
                    binding.txtToDate.setHint("To");
                    toDateCalendar = (Calendar) fromDateCalendar.clone();

                    // currentDate logic
                    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
                    String currentDate = sdf.format(Calendar.getInstance().getTime());
                    if (currentDate.equalsIgnoreCase(fromSelectDate)) {
                        StartAndEndTimeList(true);
                    } else {
                        StartAndEndTimeList(false);
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // ✅ Disable past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void showToDatePicker() {
        if (toSelectDate.equalsIgnoreCase("Select From Date")) {
            Toast.makeText(this, "Please select From Date first", Toast.LENGTH_SHORT).show();
            return;
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {

                    toDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

                    // Set selected date
                    toDateCalendar.set(year, month, dayOfMonth);

                    //Set END of day (11:59:59 PM)
                    toDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
                    toDateCalendar.set(Calendar.MINUTE, 59);
                    toDateCalendar.set(Calendar.SECOND, 59);
                    toDateCalendar.set(Calendar.MILLISECOND, 999);

                    // UI date
                    toSelectDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    binding.txtToDate.setText(toSelectDate);

                    // UNIX timestamp (seconds)
                    long timestamp = toDateCalendar.getTimeInMillis() / 1000;
                    toDateTimeStamp = String.valueOf(timestamp);

                    Log.e("toTimestamp", toDateTimeStamp);

                },
                toDateCalendar.get(Calendar.YEAR),
                toDateCalendar.get(Calendar.MONTH),
                toDateCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Minimum date = from date
        datePickerDialog.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    //Time List start and End
    private void StartAndEndTimeList(boolean isToday) {
        allTimeList.clear();
        filteredStartList.clear();

        // Fill time list from 00:00 to 23:45
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                allTimeList.add(time);
            }
        }

        // ✅ Filter past time slots if today
        if (isToday) {
            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);

            // Round up to next 15 min interval
            int roundedMinute = ((currentMinute + 14) / 15) * 15;
            if (roundedMinute == 60) {
                currentHour += 1;
                roundedMinute = 0;
            }

            if (currentHour >= 24) {
                // No time available
                filteredStartList.clear();
            } else {
                String roundedTime = String.format("%02d:%02d", currentHour, roundedMinute);

                // Remove time slots before current time
                boolean found = false;
                List<String> tempList = new ArrayList<>();
                for (String time : allTimeList) {
                    if (time.compareTo(roundedTime) >= 0) {
                        tempList.add(time);
                        found = true;
                    }
                }

                filteredStartList = tempList;

                if (!found) {
                    filteredStartList.clear(); // No valid times left
                }
            }
        } else {
            filteredStartList = allTimeList;
        }

        // Set Start Time Spinner
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(
                WorkPermitSetUpActivity.this, R.layout.custom_spinner_item, filteredStartList
        );
        startAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        binding.spinnerStartTime.setAdapter(startAdapter);

        // Auto-select first available time (if any)
        if (!filteredStartList.isEmpty()) {
            binding.spinnerStartTime.setSelection(0);
        }

        // Set listener for filtering End Time
        binding.spinnerStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromSelectTime = filteredStartList.get(position);

                // End time = any slot after selected Start Time
                List<String> filteredEndTimes = new ArrayList<>();
                for (int i = position + 1; i < filteredStartList.size(); i++) {
                    filteredEndTimes.add(filteredStartList.get(i));
                }

                if (filteredEndTimes.isEmpty()) {
                    filteredEndTimes.add("23:59");
                }

                ArrayAdapter<String> endAdapter = new ArrayAdapter<>(
                        WorkPermitSetUpActivity.this, R.layout.custom_spinner_item, filteredEndTimes
                );
                endAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
                binding.spinnerEndTime.setAdapter(endAdapter);
                binding.spinnerEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        toSelectTime = filteredStartList.get(position);

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    //Contractor
    private void addContractorBottomSheetDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_contractor, null);


        EditText WorkPermitEmail = view.findViewById(R.id.WorkPermitEmail);
        CountryCodePicker workPermitCCP = view.findViewById(R.id.workPermitCCP);
        EditText workPermitMobile = view.findViewById(R.id.workPermitMobile);
        EditText WorkPermitCompanyName = view.findViewById(R.id.WorkPermitCompanyName);
        EditText WorkPermitName = view.findViewById(R.id.WorkPermitName);
        Spinner spinnerSelectID = view.findViewById(R.id.spinnerSelectID);
        Spinner spinnerNationality = view.findViewById(R.id.spinnerNationality);
        EditText WorkPermitIDNumber = view.findViewById(R.id.WorkPermitIDNumber);
        CardView btnSubmit = view.findViewById(R.id.btnSubmit);
        CardView btnCancel = view.findViewById(R.id.btnCancel);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();


        workPermitCCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        workPermitCCP.setCountryForNameCode("+966");

        if (!subContractorList.isEmpty()){
            WorkPermitCompanyName.setText(subContractorList.get(0).companyName);
            WorkPermitCompanyName.setClickable(false);
            WorkPermitCompanyName.setFocusable(false);
            WorkPermitCompanyName.setFocusableInTouchMode(false);
        }

        if (!contractorList.isEmpty()){
            WorkPermitCompanyName.setText(contractorList.get(0).company);
            WorkPermitCompanyName.setClickable(false);
            WorkPermitCompanyName.setFocusable(false);
            WorkPermitCompanyName.setFocusableInTouchMode(false);
        }

        setDataForContractor(spinnerSelectID, spinnerNationality);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = WorkPermitName.getText().toString();
                String companyName = WorkPermitCompanyName.getText().toString();
                String idType = ContractorPosition;
                String id_name = ContractorSelectId;
                String nationality = ContractorNationality;
                String idNumber = WorkPermitIDNumber.getText().toString();
                String email = WorkPermitEmail.getText().toString();
                String mobile = workPermitCCP.getSelectedCountryCode() + workPermitMobile.getText().toString();


                if (email.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (workPermitMobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (companyName.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (id_name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select ID", Toast.LENGTH_SHORT).show();
                } else if (nationality.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Nationality", Toast.LENGTH_SHORT).show();
                } else if (idNumber.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter ID Number", Toast.LENGTH_SHORT).show();
                } else {

                    if (contractorCompName.equalsIgnoreCase("")){
                        contractorCompName = companyName;
                    }

                    String number = workPermitMobile.getText().toString();
                    String internationalNumber = workPermitCCP.getSelectedCountryCodeWithPlus() + workPermitMobile.getText().toString();
                    String nationalNumber = "0"+workPermitMobile.getText().toString();
                    String e164Number = workPermitCCP.getSelectedCountryCodeWithPlus() + workPermitMobile.getText().toString().trim();
                    String countryCode = workPermitCCP.getSelectedCountryNameCode();
                    String dialCode = workPermitCCP.getSelectedCountryCodeWithPlus();

                    // Build MobileData object
                    MobileData mobileData = new MobileData(
                            number,
                            internationalNumber,
                            nationalNumber,
                            e164Number,
                            countryCode,
                            dialCode
                    );

                    // Optional
                    String performing_work = "";
                    boolean common_nation = false;
                    boolean disabledStatus = false;

                    // Create SubContractor object
                    Contractor scontractor = new Contractor(
                            name,
                            "",
                            idType,
                            id_name,
                            nationality,
                            new ArrayList<>(),
                            idNumber,
                            email,
                            "",
                            mobileData,
                            performing_work,
                            common_nation,
                            disabledStatus
                    );

                    //list
                    contractorList.add(scontractor);
                    setContractorList();
                    dialog.dismiss();
                }

            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

    }
    private void setDataForContractor(Spinner spinnerSelectID, Spinner spinnerNationality) {
        //api
        apiViewModel.getdocuments(getApplicationContext());
        apiViewModel.getResponseforSelectedId_list().observe(this, d_model -> {

            ArrayList<Getdocuments> documentsList = d_model.getItems();
            ArrayList<Getdocuments> activeDocuments = new ArrayList<>();
            ArrayList<String> selectIDNames = new ArrayList<>();
            ArrayList<String> nationalityNames = new ArrayList<>();

            // Prepare list for spinner
            for (Getdocuments doc : documentsList) {
                if (doc.getActive()) {
                    if (!doc.getCommon() && !doc.getNationlities().isEmpty()) {
                        activeDocuments.add(doc); // keep full object
                        selectIDNames.add(doc.getName());
                    }else if (doc.getCommon()){
                        activeDocuments.add(doc); // keep full object
                        selectIDNames.add(doc.getName());
                    }
                }
            }

            // Adapter for Select ID
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, selectIDNames
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerSelectID.setAdapter(selectIDAdapter);

            // Adapter for Nationality
            ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, nationalityNames
            );
            nationalityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerNationality.setAdapter(nationalityAdapter);
            // Spinner Listener
            spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ContractorNationality = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Spinner Listener
            spinnerSelectID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ContractorNationality = "";
                    ContractorPosition = position + "";
                    Getdocuments selectedDoc = activeDocuments.get(position);
                    String selectedDocId = selectedDoc.get_id().get$oid();
                    ContractorSelectId = selectedDoc.getName();

                    //Update nationality spinner
                    nationalityNames.clear();
                    if (selectedDoc.getNationlities() != null) {
                        for (Getnationality nationality : selectedDoc.getNationlities()) {
                            if (nationality.getActive()) {
                                nationalityNames.add(nationality.getName());
                            }
                        }
                    }

                    if (activeDocuments.get(position).getNationlities().isEmpty()){
                        if (activeDocuments.get(position).getActive() && activeDocuments.get(position).getCommon()) {
                            Log.e("nationalityTest", "true");
                            try {
                                InputStream is = getResources().openRawResource(R.raw.nationalities);
                                int size = is.available();
                                byte[] buffer = new byte[size];
                                is.read(buffer);
                                is.close();
                                String json = new String(buffer, "UTF-8");
                                // Parse JSON to List<Nationality>
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<NationalityStaticFile>>() {}.getType();
                                List<NationalityStaticFile> nationalityList = gson.fromJson(json, listType);
                                // Add to your document
                                for (NationalityStaticFile national : nationalityList) {
                                    Log.e("nationality", national.getName());
                                    nationalityNames.add(national.getName());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    nationalityAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });
    }
    private void setContractorList() {

        if (contractorList.isEmpty()) {
            binding.linearInfoContracter.setVisibility(GONE);
        } else {
            binding.linearInfoContracter.setVisibility(View.VISIBLE);
            Log.e("contractorListsize",contractorList.size()+"");
            binding.txtContractorCount.setText("("+contractorList.size()+")");
        }

        binding.linearInfoContracter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractorBottomSheetDialogList();
            }
        });

    }
    private void contractorBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_contractor_list, null);


        RecyclerView recyclerviewContractor = view.findViewById(R.id.recyclerviewContractor);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddMore = view.findViewById(R.id.btnAddMore);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewContractor.setLayoutManager(new LinearLayoutManager(this));
        ContractorAdapter adapter = new ContractorAdapter(contractorList);
        recyclerviewContractor.setAdapter(adapter);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addContractorBottomSheetDialog();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }


    //Sub Contractor
    private void addSubContractorBottomSheetDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_subcontractor, null);


        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
        EditText WorkPermitSubEmail = view.findViewById(R.id.WorkPermitSubEmail);
        CountryCodePicker MaterialPermitCCP = view.findViewById(R.id.MaterialPermitCCP);
        EditText MaterialPermitMobile = view.findViewById(R.id.MaterialPermitMobile);
        EditText WorkPermitSubCompanyName = view.findViewById(R.id.WorkPermitSubCompanyName);
        EditText WorkPermitSubName = view.findViewById(R.id.WorkPermitSubName);
        Spinner spinnerWorkPermitSubSelectID = view.findViewById(R.id.spinnerWorkPermitSubSelectID);
        Spinner spinnerMaterialNationality = view.findViewById(R.id.spinnerMaterialNationality);
        EditText WorkPermitSubIDNumber = view.findViewById(R.id.WorkPermitSubIDNumber);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();


        MaterialPermitCCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        MaterialPermitCCP.setCountryForNameCode("+966");

        if (!contractorList.isEmpty()){
            WorkPermitSubCompanyName.setText(contractorList.get(0).company);
            WorkPermitSubCompanyName.setClickable(false);
            WorkPermitSubCompanyName.setFocusable(false);
            WorkPermitSubCompanyName.setFocusableInTouchMode(false);
        }

        if (!subContractorList.isEmpty()){
            WorkPermitSubCompanyName.setText(subContractorList.get(0).companyName);
            WorkPermitSubCompanyName.setClickable(false);
            WorkPermitSubCompanyName.setFocusable(false);
            WorkPermitSubCompanyName.setFocusableInTouchMode(false);
        }

        setDataForSubContractor(spinnerWorkPermitSubSelectID, spinnerMaterialNationality);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = WorkPermitSubName.getText().toString();
                String companyName = WorkPermitSubCompanyName.getText().toString();
                String idType = SubContractorPosition;
                String id_name = SubContractorSelectId;
                String nationality = SubContractorNationality;
                String idNumber = WorkPermitSubIDNumber.getText().toString();
                String email = WorkPermitSubEmail.getText().toString();
                String mobile = MaterialPermitCCP.getSelectedCountryCode() + MaterialPermitMobile.getText().toString();


                if (email.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (MaterialPermitMobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (companyName.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (id_name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select ID", Toast.LENGTH_SHORT).show();
                } else if (nationality.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Nationality", Toast.LENGTH_SHORT).show();
                } else if (idNumber.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter ID Number", Toast.LENGTH_SHORT).show();
                } else {
                    String number = MaterialPermitMobile.getText().toString();
                    String internationalNumber = MaterialPermitCCP.getSelectedCountryCodeWithPlus() + MaterialPermitMobile.getText().toString();
                    String nationalNumber = "0"+MaterialPermitMobile.getText().toString();
                    String e164Number = MaterialPermitCCP.getSelectedCountryCodeWithPlus() + MaterialPermitMobile.getText().toString().trim();
                    String countryCode = MaterialPermitCCP.getSelectedCountryNameCode();
                    String dialCode = MaterialPermitCCP.getSelectedCountryCodeWithPlus();

                    // Build MobileData object
                    MobileData mobileData = new MobileData(
                            number,
                            internationalNumber,
                            nationalNumber,
                            e164Number,
                            countryCode,
                            dialCode
                    );

                    // Optional
                    String performing_work = "";
                    boolean common_nation = false;
                    boolean disabledStatus = false;

                    // Create SubContractor object
                    SubContractor scontractor = new SubContractor(
                            name,
                            "",
                            companyName,
                            idType,
                            id_name,
                            nationality,
                            idNumber,
                            email,
                            "",
                            mobileData,
                            performing_work,
                            common_nation,
                            disabledStatus
                    );

                    //list
                    subContractorList.add(scontractor);
                    dialog.dismiss();
                    setSubList();
                }

            }
        });


        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }
    private void setDataForSubContractor(Spinner spinnerWorkPermitSubSelectID, Spinner spinnerMaterialNationality) {
        //api Sub Contractor
        apiViewModel.getdocuments(getApplicationContext());
        apiViewModel.getResponseforSelectedId_list().observe(this, d_model -> {

            ArrayList<Getdocuments> documentsList = d_model.getItems();
            ArrayList<Getdocuments> activeDocuments = new ArrayList<>();
            ArrayList<String> selectIDNames = new ArrayList<>();
            ArrayList<String> nationalityNamesList = new ArrayList<>();

            // Prepare list for spinner
            for (Getdocuments doc : documentsList) {
                if (doc.getActive()) {
                    activeDocuments.add(doc); // keep full object
                    selectIDNames.add(doc.getName());
                }
            }

            // Adapter for Select ID
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, selectIDNames
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerWorkPermitSubSelectID.setAdapter(selectIDAdapter);

            // Adapter for Nationality
            ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, nationalityNamesList
            );
            nationalityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerMaterialNationality.setAdapter(nationalityAdapter);
            // Spinner Listener
            spinnerMaterialNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubContractorNationality = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // SelectID Spinner Listener
            spinnerWorkPermitSubSelectID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubContractorNationality = "";
                    SubContractorPosition = position + "";
                    Getdocuments selectedDoc = activeDocuments.get(position);
                    String selectedDocId = selectedDoc.get_id().get$oid();
                    SubContractorSelectId = selectedDoc.getName();
                    // Update nationality spinner
                    nationalityNamesList.clear();
                    for (Getnationality nationality : selectedDoc.getNationlities()) {
                        if (nationality.getActive()) {
                            nationalityNamesList.add(nationality.getName());
                        }
                    }

                    if (activeDocuments.get(position).getNationlities().isEmpty()){
                        if (activeDocuments.get(position).getActive() && activeDocuments.get(position).getCommon()) {
                            Log.e("nationalityTest", "true");
                            try {
                                InputStream is = getResources().openRawResource(R.raw.nationalities);
                                int size = is.available();
                                byte[] buffer = new byte[size];
                                is.read(buffer);
                                is.close();
                                String json = new String(buffer, "UTF-8");
                                // Parse JSON to List<Nationality>
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<NationalityStaticFile>>() {}.getType();
                                List<NationalityStaticFile> nationalityList = gson.fromJson(json, listType);
                                // Add to your document
                                for (NationalityStaticFile national : nationalityList) {
                                    Log.e("nationality", national.getName());
                                    nationalityNamesList.add(national.getName());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    nationalityAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        });
    }
    private void setSubList() {

        if (subContractorList.isEmpty()) {
            binding.imgSubInfo.setVisibility(GONE);
            binding.txtSubCount.setVisibility(GONE);
        } else {
            binding.imgSubInfo.setVisibility(View.VISIBLE);
            binding.txtSubCount.setVisibility(View.VISIBLE);
            binding.txtSubCount.setText("("+subContractorList.size()+")");
        }

        binding.imgSubInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subContractorBottomSheetDialogList();
            }
        });

    }

    private void subContractorBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_subcontractor_list, null);


        RecyclerView recyclerviewSubContractor = view.findViewById(R.id.recyclerviewSubContractor);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddMore = view.findViewById(R.id.btnAddMore);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewSubContractor.setLayoutManager(new LinearLayoutManager(this));
        SubContractorAdapter adapter = new SubContractorAdapter(subContractorList);
        recyclerviewSubContractor.setAdapter(adapter);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addSubContractorBottomSheetDialog();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(intent);
    }

}