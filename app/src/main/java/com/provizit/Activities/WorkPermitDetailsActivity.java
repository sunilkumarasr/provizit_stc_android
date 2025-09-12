package com.provizit.Activities;

import static com.provizit.Conversions.milliToDateTime;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.AdapterAndModel.ContractorData;
import com.provizit.AdapterAndModel.ContractorDetailsAdapter;
import com.provizit.AdapterAndModel.SubContractorData;
import com.provizit.AdapterAndModel.SubContractorDetailsAdapter;
import com.provizit.AdapterAndModel.WorkPermitModal;
import com.provizit.AdapterAndModel.WorkVisitTypeModel;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.databinding.ActivityWorkPermitDetailsBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkPermitDetailsActivity extends AppCompatActivity {

    ActivityWorkPermitDetailsBinding binding;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;


    List<ContractorData> contractorList = new ArrayList<>();
    List<SubContractorData> subContractorList = new ArrayList<>();

    String id = "";
    String type = "";
    String compId = "";
    String companyName = "";
    String permitID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkPermitDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(WorkPermitDetailsActivity.this, R.color.colorPrimary));
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            id = b.getString("id");
            type = b.getString("type");
        }
        myDb = new DatabaseHelper( WorkPermitDetailsActivity.this);
        sharedPreferences1 = WorkPermitDetailsActivity.this.getSharedPreferences("EGEMSS_DATA", 0);
        empData = new EmpData();
        empData = myDb.getEmpdata();


        inits();
        
    }

    private void inits() {

        getworkpermitDetails();

        binding.linearContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractorBottomSheetDialogList();
            }
        });

        binding.linearSubContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subContractorBottomSheetDialogList();
            }
        });

        binding.linearApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sheetView = LayoutInflater.from(WorkPermitDetailsActivity.this).inflate(R.layout.bottom_sheet_approve, null);

                TextView txtYes = sheetView.findViewById(R.id.txtYes);
                TextView txtNo = sheetView.findViewById(R.id.txtNo);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(WorkPermitDetailsActivity.this);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.setCancelable(false);

                txtYes.setOnClickListener(view -> {
                    bottomSheetDialog.dismiss();

                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "accept");
                        jsonObj_.put("comp_id", compId);
                        jsonObj_.put("emp_id", empData.getEmp_id());
                        jsonObj_.put("id", permitID);
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateworkpermita(gsonObject);

                });

                txtNo.setOnClickListener(view -> bottomSheetDialog.dismiss());

                View bottomSheet = bottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackgroundColor(Color.TRANSPARENT);
                }

                bottomSheetDialog.show();
            }
        });

        binding.linearReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sheetView = LayoutInflater.from(WorkPermitDetailsActivity.this).inflate(R.layout.bottom_sheet_reject, null);

                EditText editNote = sheetView.findViewById(R.id.editNote);
                TextView txtYes = sheetView.findViewById(R.id.txtYes);
                TextView txtNo = sheetView.findViewById(R.id.txtNo);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(WorkPermitDetailsActivity.this);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.setCancelable(false);

                txtYes.setOnClickListener(view -> {

                    if (editNote.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please enter reason",Toast.LENGTH_SHORT).show();
                    }else {
                        bottomSheetDialog.dismiss();
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "decline");
                            jsonObj_.put("comp_id", compId);
                            jsonObj_.put("emp_id", empData.getEmp_id());
                            jsonObj_.put("id", permitID);
                            jsonObj_.put("dept_id", empData.getHierarchy_id());
                            jsonObj_.put("reason", editNote.getText().toString());
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateworkpermita(gsonObject);
                    }

                });

                txtNo.setOnClickListener(view -> bottomSheetDialog.dismiss());

                View bottomSheet = bottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackgroundColor(Color.TRANSPARENT);
                }

                bottomSheetDialog.show();
            }
        });


        binding.linearCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sheetView = LayoutInflater.from(WorkPermitDetailsActivity.this).inflate(R.layout.bottom_sheet_cancel, null);

                TextView txtYes = sheetView.findViewById(R.id.txtYes);
                TextView txtNo = sheetView.findViewById(R.id.txtNo);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(WorkPermitDetailsActivity.this);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.setCancelable(false);

                txtYes.setOnClickListener(view -> {
                    bottomSheetDialog.dismiss();

                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "delete");
                        jsonObj_.put("comp_id", compId);
                        jsonObj_.put("emp_id", empData.getEmp_id());
                        jsonObj_.put("id", permitID);
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    actionworkpermita(gsonObject);

                });

                txtNo.setOnClickListener(view -> bottomSheetDialog.dismiss());

                View bottomSheet = bottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackgroundColor(Color.TRANSPARENT);
                }

                bottomSheetDialog.show();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void getworkpermitDetails() {
        ViewController.ShowProgressBar(WorkPermitDetailsActivity.this);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getworkpermitDetails(new Callback<WorkPermitModal>() {
            @Override
            public void onResponse(Call<WorkPermitModal> call, Response<WorkPermitModal> response) {
                ViewController.DismissProgressBar();
                WorkPermitModal model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(successcode)) {

                        //Contractors list
                        if (!model.getItems().getContractorsData().isEmpty()) {
                            binding.txtContractorCount.setText(model.getItems().getContractorsData().size()+"");
                            contractorList.addAll(model.getItems().getContractorsData());
                        }

                        //subcontractors list
                        if (!model.getItems().getSubcontractorsData().isEmpty()) {
                            binding.txtSubContractorCount.setText(model.getItems().getSubcontractorsData().size()+"");
                            subContractorList.addAll(model.getItems().getSubcontractorsData());
                        }

                        permitID = model.getItems().get_id().get$oid();
                        compId = model.getItems().getComp_id();
                        companyName = model.getItems().getCompanyName();

                        binding.permitID.setText(model.getItems().getWorkpermit_id());
                        binding.companyName.setText(model.getItems().getCompanyName());
                        binding.txtWorkType.setText(model.getItems().getWorktypeData().getName());
                        binding.txtLocation.setText(model.getItems().getLocations_Data().getName());
                        binding.txtPlaceOfWork.setText(model.getItems().getWorklocationData().getName());
                        binding.txtScopeOfWork.setText(model.getItems().getWork_scope());

                        //from date time
                        String fromDate = Conversions.formatToFullDateTime(model.getItems().getStarts().get(0));
                        // 1 min add
                        long plusOneMinInSecondsFrom = model.getItems().getEnds().get(0) + 60;
                        String fromEndTIme = Conversions.millitotime((plusOneMinInSecondsFrom + Conversions.timezone()) * 1000, false);
                        binding.txtFrom.setText(fromDate+" - "+fromEndTIme);

                        //to date time
                        //array past position on to date
                        ArrayList<Long> ends = model.getItems().getEnds();
                        if (ends != null && !ends.isEmpty()) {
                            Long lastValue = ends.get(ends.size() - 1);
                            Log.d("LastEnd", "Last end time: " + lastValue);
                            String toDate = Conversions.formatToFullDateonly(lastValue);
                            String toStartTIme = Conversions.millitotime((model.getItems().getStarts().get(0) + Conversions.timezone()) * 1000, false);
                            // 1 min add
                            long plusOneMinInSecondsTo = lastValue + 60;
                            String toEndTIme = Conversions.millitotime((plusOneMinInSecondsTo + Conversions.timezone()) * 1000, false);
                            binding.txtTo.setText(toDate + toStartTIme+" - "+toEndTIme);
                        }

                        if (model.getItems().getStatus()==2){
                            binding.permitStatus.setVisibility(View.VISIBLE);
                            binding.permitStatus.setText("The Work / Visit Permit has been Cancelled");
                        }else {

                            //buttons Showing
                            if (type.equalsIgnoreCase("self")){
                                binding.linearCancel.setVisibility(View.VISIBLE);
                            } else {
                                String workPermitApprover = Preferences.loadStringValue(WorkPermitDetailsActivity.this, Preferences.workpermitPpprover, "");
                                Boolean isApprover = Boolean.valueOf(workPermitApprover);
                                if (Boolean.TRUE.equals(isApprover)
                                        && model.getItems().getWorkpermit_approval() != null
                                        && !model.getItems().getWorkpermit_approval().isEmpty()
                                        && model.getItems().getWorkpermit_approval().get(0).getEmp_id().equalsIgnoreCase("")
                                        && !empData.getEmp_id().equalsIgnoreCase(model.getItems().getC_empId())) {
                                    binding.linearApprove.setVisibility(View.VISIBLE);
                                    binding.linearReject.setVisibility(View.VISIBLE);
                                }
                            }
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<WorkPermitModal> call, Throwable t) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                System.out.println("asdf2" + t);
            }
        }, WorkPermitDetailsActivity.this, id );
    }


    //update permit Approve or Reject
    private void updateworkpermita(JsonObject jsonObject) {
        ViewController.ShowProgressBar(WorkPermitDetailsActivity.this);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updateworkpermita(new Callback<WorkPermitModal>() {
            @Override
            public void onResponse(Call<WorkPermitModal> call, Response<WorkPermitModal> response) {
                ViewController.DismissProgressBar();
                WorkPermitModal model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(successcode)) {

                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(WorkPermitDetailsActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call<WorkPermitModal> call, Throwable t) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                System.out.println("asdf2" + t);
            }
        }, WorkPermitDetailsActivity.this, jsonObject);
    }

    //cancel
    private void actionworkpermita(JsonObject gsonObject) {
        ViewController.ShowProgressBar(WorkPermitDetailsActivity.this);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actionworkpermita(new Callback<WorkVisitTypeModel>() {
            @Override
            public void onResponse(Call<WorkVisitTypeModel> call, Response<WorkVisitTypeModel> response) {
                ViewController.DismissProgressBar();
                WorkVisitTypeModel model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(successcode)) {

                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(WorkPermitDetailsActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call<WorkVisitTypeModel> call, Throwable t) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                System.out.println("asdf2" + t);
            }
        }, WorkPermitDetailsActivity.this, gsonObject);
    }

    //contractor list
    private void contractorBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.contractor_listpopup, null);


        RecyclerView recyclerviewContractor = view.findViewById(R.id.recyclerviewContractor);
        RelativeLayout relativeNoData = view.findViewById(R.id.relativeNoData);
        TextView txtCompanyName = view.findViewById(R.id.txtCompanyName);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewContractor.setLayoutManager(new LinearLayoutManager(this));
        ContractorDetailsAdapter adapter = new ContractorDetailsAdapter(contractorList);
        recyclerviewContractor.setAdapter(adapter);

        txtCompanyName.setText(WorkPermitDetailsActivity.this.getString(R.string.CompanyName)+ " "+companyName);

        if (contractorList.isEmpty()) {
            relativeNoData.setVisibility(View.VISIBLE);
        } else {
            relativeNoData.setVisibility(View.GONE);
        }

    }


    //subContractor list
    private void subContractorBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.subcontractor_listpopup, null);

        RecyclerView recyclerviewSubContractor = view.findViewById(R.id.recyclerviewSubContractor);
        RelativeLayout relativeNoData = view.findViewById(R.id.relativeNoData);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewSubContractor.setLayoutManager(new LinearLayoutManager(this));
        SubContractorDetailsAdapter adapter = new SubContractorDetailsAdapter(subContractorList);
        recyclerviewSubContractor.setAdapter(adapter);

        if (subContractorList.isEmpty()) {
            relativeNoData.setVisibility(View.VISIBLE);
        } else {
            relativeNoData.setVisibility(View.GONE);
        }

    }


}