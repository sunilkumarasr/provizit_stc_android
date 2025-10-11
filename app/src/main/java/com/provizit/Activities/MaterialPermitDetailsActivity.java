package com.provizit.Activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.AdapterAndModel.MaterialDetailsModel;
import com.provizit.AdapterAndModel.MaterialPermitDetailsAdapter;
import com.provizit.AdapterAndModel.SubContractorAdapter;
import com.provizit.AdapterAndModel.SupplierDetailsModel;
import com.provizit.AdapterAndModel.SupplierDetailsAdapter;
import com.provizit.AdapterAndModel.WorkPermitModal;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.databinding.ActivityMaterialPermitDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialPermitDetailsActivity extends AppCompatActivity {

    ActivityMaterialPermitDetailsBinding binding;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    List<MaterialDetailsModel> materialDetailsList = new ArrayList<>();
    List<SupplierDetailsModel> suppliersDetailsList = new ArrayList<>();

    String id = "";
    String type = "";
    String compId = "";
    String permitID = "";
    String SupplierName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialPermitDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MaterialPermitDetailsActivity.this, R.color.colorPrimary));
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = b.getString("id");
            type = b.getString("type");
        }

        myDb = new DatabaseHelper(MaterialPermitDetailsActivity.this);
        sharedPreferences1 = MaterialPermitDetailsActivity.this.getSharedPreferences("EGEMSS_DATA", 0);
        empData = new EmpData();
        empData = myDb.getEmpdata();


        inits();

    }

    private void inits() {

        getentrypermitdetails();

        binding.linearMaterialDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDetailsBottomSheetDialogList();
            }
        });

        binding.linearSupplierDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplierDetailsBottomSheetDialogList();
            }
        });

        binding.linearApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sheetView = LayoutInflater.from(MaterialPermitDetailsActivity.this).inflate(R.layout.bottom_sheet_approve, null);

                TextView txtYes = sheetView.findViewById(R.id.txtYes);
                TextView txtNo = sheetView.findViewById(R.id.txtNo);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MaterialPermitDetailsActivity.this);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.setCancelable(false);

                txtYes.setOnClickListener(view -> {
                    bottomSheetDialog.dismiss();

                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "approve");
                        jsonObj_.put("comp_id", compId);
                        jsonObj_.put("emp_id", empData.getEmp_id());
                        jsonObj_.put("id", permitID);
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updatematerialpermit(gsonObject);

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
                View sheetView = LayoutInflater.from(MaterialPermitDetailsActivity.this).inflate(R.layout.bottom_sheet_reject, null);

                EditText editNote = sheetView.findViewById(R.id.editNote);
                TextView txtYes = sheetView.findViewById(R.id.txtYes);
                TextView txtNo = sheetView.findViewById(R.id.txtNo);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MaterialPermitDetailsActivity.this);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.setCancelable(false);

                txtYes.setOnClickListener(view -> {

                    if (editNote.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please enter reason", Toast.LENGTH_SHORT).show();
                    } else {
                        bottomSheetDialog.dismiss();
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "reject");
                            jsonObj_.put("comp_id", compId);
                            jsonObj_.put("emp_id", empData.getEmp_id());
                            jsonObj_.put("id", permitID);
                            jsonObj_.put("reason", editNote.getText().toString());
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updatematerialpermit(gsonObject);
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
                View sheetView = LayoutInflater.from(MaterialPermitDetailsActivity.this).inflate(R.layout.bottom_sheet_cancel, null);

                TextView txtYes = sheetView.findViewById(R.id.txtYes);
                TextView txtNo = sheetView.findViewById(R.id.txtNo);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MaterialPermitDetailsActivity.this);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.setCancelable(false);

                txtYes.setOnClickListener(view -> {
                    bottomSheetDialog.dismiss();

                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "cancel");
                        jsonObj_.put("comp_id", compId);
                        jsonObj_.put("emp_id", empData.getEmp_id());
                        jsonObj_.put("id", permitID);
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updatematerialpermit(gsonObject);

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

    private void getentrypermitdetails() {
        ViewController.ShowProgressBar(MaterialPermitDetailsActivity.this);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getentrypermitdetails(new Callback<WorkPermitModal>() {
            @Override
            public void onResponse(@NonNull Call<WorkPermitModal> call, @NonNull Response<WorkPermitModal> response) {
                ViewController.DismissProgressBar();
                WorkPermitModal model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(successcode)) {

                        //Material
                        if (!model.getItems().getMaterial_details().isEmpty()) {
                            binding.linearMaterialDetails.setVisibility(View.VISIBLE);
                            binding.txtMaterialDetailsCount.setText(model.getItems().getMaterial_details().size() + "");
                            materialDetailsList.addAll(model.getItems().getMaterial_details());
                        }

                        //Supplier
                        if (!model.getItems().getSupplier_details().isEmpty()) {
                            binding.linearSupplierDetails.setVisibility(View.VISIBLE);
                            binding.txtSupplierDetailsCount.setText(model.getItems().getSupplier_details().size() + "");
                            suppliersDetailsList.addAll(model.getItems().getSupplier_details());
                        }

                        compId = model.getItems().getComp_id();

                        permitID = model.getItems().get_id().get$oid();
                        SupplierName = model.getItems().getSupplier_name();

                        if (model.getItems().getPurpose_return()) {
                            binding.txtProcessType.setText(MaterialPermitDetailsActivity.this.getString(R.string.Entry));
                            binding.txtPurpose.setText(model.getItems().getPurpose() + " - ("+MaterialPermitDetailsActivity.this.getString(R.string.Return)+")");
                        } else {
                            binding.txtProcessType.setText(MaterialPermitDetailsActivity.this.getString(R.string.Exit));
                            binding.txtPurpose.setText(model.getItems().getPurpose());
                        }

                        long longtime = (Long.parseLong(String.valueOf(Long.parseLong(model.getItems().getCreated_time().get$numberLong()) * 1000)));
                        binding.txtCreateDate.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");

                        binding.txtMaterialName.setText(model.getItems().getEmployee_Data().getName());
                        binding.txtPermitID.setText(model.getItems().getMeterial_id());
                        binding.txtRefDocument.setText(model.getItems().getRef_document());
                        String fromDate = Conversions.millitodateD((model.getItems().getStart() + Conversions.timezone()) * 1000);
                        String toDate = Conversions.millitodateD((model.getItems().getEnd() + Conversions.timezone()) * 1000);
                        binding.txtFrom.setText(fromDate);
                        binding.txtTo.setText(toDate);

                        if (model.getItems().getStatus()==2){
                            binding.permitStatus.setVisibility(View.VISIBLE);
                            binding.permitStatus.setText("The Material Permit has been Cancelled");
                        }else {

                            //buttons Show
                            if (type.equalsIgnoreCase("self")){
                                binding.linearApprove.setVisibility(View.GONE);
                                binding.linearReject.setVisibility(View.GONE);
                                binding.linearCancel.setVisibility(View.VISIBLE);
                            }else {
                                String mPermitApproval = Preferences.loadStringValue(MaterialPermitDetailsActivity.this, Preferences.mPermitApproval, "");
                                for (int i = 0; i < model.getItems().getApprover_statistics().size(); i++) {
                                    if (model.getItems().getApprover_statistics().get(i).getId().equalsIgnoreCase(mPermitApproval)
                                            && model.getItems().getApprover_statistics().get(i).getId().equalsIgnoreCase(model.getItems().getApprover())) {

                                        if (model.getItems().getApprover_statistics().get(i).getStatus() == 0) {
                                            binding.linearApprove.setVisibility(View.VISIBLE);
                                            binding.linearReject.setVisibility(View.VISIBLE);
                                        } else if (model.getItems().getApprover_statistics().get(i).getStatus() == 1) {
                                            binding.linearReject.setVisibility(View.VISIBLE);
                                        } else if (model.getItems().getApprover_statistics().get(i).getStatus() == 2) {
                                            binding.linearApprove.setVisibility(View.VISIBLE);
                                        }

                                    }
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
        }, MaterialPermitDetailsActivity.this, id);
    }


    //update permit Approve or Reject
    private void updatematerialpermit(JsonObject jsonObject) {
        ViewController.ShowProgressBar(MaterialPermitDetailsActivity.this);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updatematerialpermit(new Callback<WorkPermitModal>() {
            @Override
            public void onResponse(Call<WorkPermitModal> call, Response<WorkPermitModal> response) {
                ViewController.DismissProgressBar();
                WorkPermitModal model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(successcode)) {

                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MaterialPermitDetailsActivity.this, NavigationActivity.class);
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
        }, MaterialPermitDetailsActivity.this, jsonObject);
    }


    //material Details list
    private void materialDetailsBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.material_details_listpopup, null);

        RecyclerView recyclerviewMaterialPermitDetails = view.findViewById(R.id.recyclerviewMaterialPermitDetails);
        RelativeLayout relativeNoData = view.findViewById(R.id.relativeNoData);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewMaterialPermitDetails.setLayoutManager(new LinearLayoutManager(this));
        MaterialPermitDetailsAdapter adapter = new MaterialPermitDetailsAdapter(materialDetailsList);
        recyclerviewMaterialPermitDetails.setAdapter(adapter);

        if (materialDetailsList.isEmpty()) {
            relativeNoData.setVisibility(View.VISIBLE);
        } else {
            relativeNoData.setVisibility(View.GONE);
        }

    }

    //supplier Details list
    private void supplierDetailsBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.supplier_details_listpopup, null);

        RecyclerView recyclerviewSupplierDetails = view.findViewById(R.id.recyclerviewSupplierDetails);
        TextView txtSupplierName = view.findViewById(R.id.txtSupplierName);
        RelativeLayout relativeNoData = view.findViewById(R.id.relativeNoData);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewSupplierDetails.setLayoutManager(new LinearLayoutManager(this));
        SupplierDetailsAdapter adapter = new SupplierDetailsAdapter(suppliersDetailsList);
        recyclerviewSupplierDetails.setAdapter(adapter);

        txtSupplierName.setText(SupplierName);

        if (suppliersDetailsList.isEmpty()) {
            relativeNoData.setVisibility(View.VISIBLE);
        } else {
            relativeNoData.setVisibility(View.GONE);
        }

    }

}