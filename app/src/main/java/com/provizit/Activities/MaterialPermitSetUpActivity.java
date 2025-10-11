package com.provizit.Activities;

import static android.view.View.GONE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;
import com.provizit.AdapterAndModel.ContractorAdapter;
import com.provizit.AdapterAndModel.GetSearchEmployees;
import com.provizit.AdapterAndModel.Getdocuments;
import com.provizit.AdapterAndModel.Getnationality;
import com.provizit.AdapterAndModel.Getsubhierarchys;
import com.provizit.AdapterAndModel.LocationAddressList;
import com.provizit.AdapterAndModel.MaterialDetailsSet;
import com.provizit.AdapterAndModel.MaterialItemsList;
import com.provizit.AdapterAndModel.MaterialPermitDetailsAdapter;
import com.provizit.AdapterAndModel.MaterialPermitDetailsSetUpAdapter;
import com.provizit.AdapterAndModel.SupplierDetails;
import com.provizit.AdapterAndModel.SupplierDetailsAdapter;
import com.provizit.AdapterAndModel.SupplierDetailsModel;
import com.provizit.AdapterAndModel.SupplierDetailsSetUpAdapter;
import com.provizit.AdapterAndModel.SupplierMobile;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RoleDetails;
import com.provizit.databinding.ActivityMaterialPermitSetUpBinding;
import com.provizit.databinding.ActivityWorkPermitSetUpBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MaterialPermitSetUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMaterialPermitSetUpBinding binding;

    BroadcastReceiver broadcastReceiver;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;
    SharedPreferences.Editor editor1;
    RoleDetails roleDetails;
    AnimationSet animation;
    ApiViewModel apiViewModel;

    String EntryType = "1";

    //Supplier Details
    List<SupplierDetails> supplierDetailsList = new ArrayList<>();

    //Date
    Calendar fromDateCalendar = Calendar.getInstance();
    Calendar toDateCalendar = Calendar.getInstance();
    String fromSelectDate = "";
    String toSelectDate = "";
    String fromDateTimeStamp = "";
    String toDateTimeStamp = "";



    //Ref Document
    String StatusEditspinnerRefDoc;
    String RefDocItem;


    //Purpose
    String PurposeItem;
    String PurposeId;
    boolean PurposeReturn;

    ArrayList<Getsubhierarchys> subhierarchysmaterial = new ArrayList<>();

    //Pertains
    String PertainsItem;
    String DepartmentId;


    //Employee
    ArrayList<GetSearchEmployees> searchemployeesmaterial = new ArrayList<>();
    String EmployeeItem;
    String EmployeeId = "";

    //location
    ArrayList<LocationAddressList> LocationList = new ArrayList<>();
    String locationItem;
    String locationIndexPosition;

    //add material Details
    private int counter = 1;
    List<MaterialDetailsSet> materialDetailsList = new ArrayList<>();


    String compId = "";
    String Nationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialPermitSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inits();

    }


    private void inits() {
        ViewController.barPrimaryColor(MaterialPermitSetUpActivity.this);
        ViewController.internetConnection(broadcastReceiver, MaterialPermitSetUpActivity.this);
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        editor1 = sharedPreferences1.edit();
        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        roleDetails = new RoleDetails();
        empData = myDb.getEmpdata();
        roleDetails = myDb.getRole();
        animation = Conversions.animation();
        apiViewModel = new ViewModelProvider(MaterialPermitSetUpActivity.this).get(ApiViewModel.class);
        compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");


        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioEntry) {
                    //Entry selected
                    EntryType = "1";
                    binding.viewRef.setVisibility(View.VISIBLE);
                    binding.txtRefDoc.setVisibility(View.VISIBLE);
                    binding.spinnerRefDoc.setVisibility(View.VISIBLE);
                    SpinnersListApis();
                } else if (checkedId == R.id.radioExit) {
                    //Exit selected
                    EntryType = "2";
                    binding.txtRefDoc.setVisibility(GONE);
                    binding.viewRef.setVisibility(GONE);
                    binding.spinnerRefDoc.setVisibility(GONE);
                    binding.EditSpinnerRefDoc.setVisibility(GONE);
                    SpinnersListApis();
                }
            }
        });


        //current Date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        fromSelectDate =currentDate;
        binding.txtFromDate.setText(fromSelectDate);
        //toDate
        toDateCalendar = (Calendar) calendar.clone();
        toSelectDate = currentDate;
        binding.txtToDate.setText(toSelectDate);
        //timeStamp
        try {
            SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
            sd.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC if you want exact UNIX time
            Date date = sd.parse(fromSelectDate);
            long timestamp = date.getTime() / 1000;
            fromDateTimeStamp = timestamp+"";
            toDateTimeStamp = timestamp+"";
            Log.e("fromtimestamp",timestamp+"");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Spinners List Set
        SpinnersListApis();

        //submit Material Permit form
        apiViewModel.actionentrypermitrequest_response().observe(this, model -> {
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
        binding.linearSupplierDetails.setOnClickListener(this);
        binding.linearMaterialPermitDetails.setOnClickListener(this);
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
            case R.id.linearSupplierDetails:
                view.startAnimation(animation);
                SupplierDetailsPopUp();
                break;
            case R.id.linearMaterialPermitDetails:
                view.startAnimation(animation);
                AddMaterialDetailsPopUp();
                break;
            case R.id.txtFromDate:
                view.startAnimation(animation);
                showFromDatePicker();
                break;
            case R.id.txtToDate:
                view.startAnimation(animation);
                showToDatePicker();
                break;
            case R.id.btnNext:
                view.startAnimation(animation);

                if (StatusEditspinnerRefDoc.equalsIgnoreCase("true")){
                    if (!binding.EditSpinnerRefDoc.getText().toString().equalsIgnoreCase("")){
                        RefDocItem = binding.EditSpinnerRefDoc.getText().toString();
                    }
                }


                if (supplierDetailsList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Supplier Details", Toast.LENGTH_SHORT).show();
                }else if (binding.txtFromDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                }else if (binding.txtToDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                }else if (EntryType.equalsIgnoreCase("1") && RefDocItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Ref Document", Toast.LENGTH_SHORT).show();
                }else if (EntryType.equalsIgnoreCase("1") && StatusEditspinnerRefDoc.equalsIgnoreCase("true")  && binding.EditSpinnerRefDoc.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Ref Document", Toast.LENGTH_SHORT).show();
                }else if (PurposeItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Purpose", Toast.LENGTH_SHORT).show();
                }else if (locationItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Location", Toast.LENGTH_SHORT).show();
                }else if (PertainsItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Pertains", Toast.LENGTH_SHORT).show();
                }else if (materialDetailsList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Material Details", Toast.LENGTH_SHORT).show();
                }else {

                    JsonObject json = createMaterialPermitSubmit();
                    apiViewModel.actionentrypermitrequest(getApplicationContext(), json);
                    ViewController.ShowProgressBar(MaterialPermitSetUpActivity.this);

                }

                break;
        }
    }


    private JsonObject createMaterialPermitSubmit() {

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray supplierDetailsData = new JSONArray();
        JSONArray materialDetailsData = new JSONArray();


        //supplier Details Data List
        Gson gson = new Gson();
        try {
            for (SupplierDetails supplierDetails : supplierDetailsList) {
                String jsonString = gson.toJson(supplierDetails);
                JSONObject jsonObject = new JSONObject(jsonString);
                supplierDetailsData.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //material Details List
        Gson gsonm = new Gson();
        try {
            for (MaterialDetailsSet materialDetailsSet : materialDetailsList) {
                String jsonString = gsonm.toJson(materialDetailsSet);
                JSONObject jsonObject = new JSONObject(jsonString);
                materialDetailsData.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("type", EntryType);
            jsonObj_.put("start", fromDateTimeStamp);
            jsonObj_.put("end", toDateTimeStamp);

            if (EntryType.equalsIgnoreCase("1")){
                jsonObj_.put("ref_document", RefDocItem);
            }else {
                jsonObj_.put("ref_document", "");
            }

            jsonObj_.put("purpose", PurposeItem);
            jsonObj_.put("purpose_id", PurposeId);
            jsonObj_.put("purpose_return", PurposeReturn);
            jsonObj_.put("l_id", locationIndexPosition);

            if (EmployeeId != null && !EmployeeId.isEmpty()) {
                jsonObj_.put("employee", EmployeeId);
            }else {
                jsonObj_.put("employee", "");
            }

            jsonObj_.put("department", DepartmentId);
            jsonObj_.put("material_details", materialDetailsData);
            jsonObj_.put("supplier_name", supplierDetailsList.get(0).contact_person);
            jsonObj_.put("supplier_details", supplierDetailsData);
            jsonObj_.put("contact_person", "");
            jsonObj_.put("driver_mobile", "");
            jsonObj_.put("driver_name", "");
            jsonObj_.put("id_number", "");
            jsonObj_.put("supplier_mobile", "");
            jsonObj_.put("nationality", "");
            jsonObj_.put("plate_no", "");
            jsonObj_.put("vehicle_type", "");
            jsonObj_.put("emp_id", empData.getEmp_id());
            jsonObj_.put("comp_id", compId);
            jsonObj_.put("location", empData.getLocation());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("createjsongsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;

    }


    //add Supplier Details
    private void SupplierDetailsPopUp() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_add_supplierdetails, null);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();

        // Apply transparent background to allow rounded corners to show
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText EditSupplierName = dialogView.findViewById(R.id.EditSupplierName);
        EditText EditEmail = dialogView.findViewById(R.id.EditEmail);
        EditText EditName = dialogView.findViewById(R.id.EditName);
        CountryCodePicker CCP = dialogView.findViewById(R.id.CCP);
        EditText EditMobile = dialogView.findViewById(R.id.EditMobile);
        EditText EditIDNumber = dialogView.findViewById(R.id.EditIDNumber);
        Spinner spinnerNationality = dialogView.findViewById(R.id.spinnerNationality);
        EditText EditVehicleNumber = dialogView.findViewById(R.id.EditVehicleNumber);
        EditText EditVehicleType = dialogView.findViewById(R.id.EditVehicleType);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        CCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        CCP.setCountryForNameCode("+966");

        setDataForSupplier(spinnerNationality);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = EditSupplierName.getText().toString();
                String email = EditEmail.getText().toString();
                String companyName = EditName.getText().toString();
                String idNumber = EditIDNumber.getText().toString();
                String nationality = Nationality;
                String vehicleNumber = EditVehicleNumber.getText().toString();
                String vehicleType = EditVehicleType.getText().toString();
                String mobileWithCountryCode = CCP.getSelectedCountryCode() + EditMobile.getText().toString();


                if (name.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter SupplierName", Toast.LENGTH_SHORT).show();
                } else if (email.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (companyName.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                }else if (nationality.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Select Nationality", Toast.LENGTH_SHORT).show();
                }else {

                    String number = EditMobile.getText().toString();
                    String internationalNumber = CCP.getSelectedCountryCodeWithPlus() + EditMobile.getText().toString();
                    String nationalNumber = "0"+EditMobile.getText().toString();
                    String e164Number = CCP.getSelectedCountryCodeWithPlus() + EditMobile.getText().toString().trim();
                    String countryCode = CCP.getSelectedCountryNameCode();
                    String dialCode = CCP.getSelectedCountryCodeWithPlus();


                    if (number.equalsIgnoreCase("") || number == null){
                        number = "";
                        internationalNumber = "";
                        nationalNumber = "";
                        e164Number = "";
                        countryCode = "";
                        dialCode = "";
                    }

                    // Build MobileData object
                    SupplierMobile mobileData = new SupplierMobile(
                            number,
                            internationalNumber,
                            nationalNumber,
                            e164Number,
                            countryCode,
                            dialCode
                    );

                    if (idNumber.equalsIgnoreCase("") || idNumber == null){
                        idNumber = "";
                    }
                    if (vehicleNumber.equalsIgnoreCase("") || vehicleNumber == null){
                        vehicleNumber = "";
                    }
                    if (vehicleType.equalsIgnoreCase("") || vehicleType == null){
                        vehicleType = "";
                    }


                    // Create SubContractor object
                    SupplierDetails supplierDetails = new SupplierDetails(
                            name,
                            email,
                            mobileData,
                            idNumber,
                            nationality,
                            vehicleNumber,
                            vehicleType,
                            false
                    );


                    //list
                    supplierDetailsList.add(supplierDetails);
                    setsupplierDetailsList();
                    bottomSheetDialog.dismiss();

                }
            }
        });

    }
    private void setsupplierDetailsList() {
        if (supplierDetailsList.isEmpty()) {
            binding.linearInfoSuppliers.setVisibility(GONE);
        } else {
            binding.linearInfoSuppliers.setVisibility(View.VISIBLE);
            Log.e("contractorListsize",supplierDetailsList.size()+"");
            binding.txtSupCount.setText("("+supplierDetailsList.size()+")");
        }

        binding.linearInfoSuppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliersBottomSheetDialogList();
            }
        });

    }
    private void suppliersBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_suppliers_list, null);

        RecyclerView recyclerviewSuppliers = view.findViewById(R.id.recyclerviewSuppliers);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddMore = view.findViewById(R.id.btnAddMore);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewSuppliers.setLayoutManager(new LinearLayoutManager(this));
        SupplierDetailsSetUpAdapter adapter = new SupplierDetailsSetUpAdapter(supplierDetailsList);
        recyclerviewSuppliers.setAdapter(adapter);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SupplierDetailsPopUp();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }


    private void setDataForSupplier(Spinner spinnerNationality) {
        //api Nationality List
        apiViewModel.getdocuments(getApplicationContext());
        apiViewModel.getResponseforSelectedId_list().observe(this, dModel -> {

            ArrayList<Getdocuments> DocumentsList = dModel.getItems();
            ArrayList<String> NationalityNamesList = new ArrayList<>();

            for (Getdocuments doc : DocumentsList) {
                // Ensure this document is active
                if (doc.getActive()) {

                    // Check if this document has a list of nationalities
                    if (doc.getNationlities() != null && !doc.getNationlities().isEmpty()) {
                        for (Getnationality nationality : doc.getNationlities()) {
                            if (nationality.getActive()) {
                                NationalityNamesList.add(nationality.getName());
                            }
                        }
                    }
                }
            }

            // Adapter for Nationality
            ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, NationalityNamesList
            );
            nationalityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerNationality.setAdapter(nationalityAdapter);
            // Spinner Listener
            spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Nationality = parent.getItemAtPosition(position).toString();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

    }


    //from Date
    private void showFromDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    fromDateCalendar.set(year, month, dayOfMonth);
                    fromSelectDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    binding.txtFromDate.setText(fromSelectDate);

                    //timeStamp
                    try {
                        SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
                        sd.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC if you want exact UNIX time
                        Date date = sd.parse(fromSelectDate);
                        long timestamp = date.getTime() / 1000;
                        fromDateTimeStamp = timestamp+"";
                        Log.e("fromtimestamp",timestamp+"");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    // Reset To Date
                    binding.txtToDate.setText("");
                    binding.txtToDate.setHint("To");
                    toDateCalendar = (Calendar) fromDateCalendar.clone();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // ✅ Disable past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    //To Date
    private void showToDatePicker() {
        if (toSelectDate.equalsIgnoreCase("Select From Date")) {
            Toast.makeText(this, "Please select From Date first", Toast.LENGTH_SHORT).show();
            return;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    toDateCalendar.set(year, month, dayOfMonth);
                    toSelectDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    binding.txtToDate.setText(toSelectDate);

                    //timeStamp
                    try {
                        SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
                        sd.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC if you want exact UNIX time
                        Date date = sd.parse(toSelectDate);
                        long timestamp = date.getTime() / 1000;
                        toDateTimeStamp = timestamp+"";
                        Log.e("fromtimestamp",timestamp+"");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                },
                toDateCalendar.get(Calendar.YEAR),
                toDateCalendar.get(Calendar.MONTH),
                toDateCalendar.get(Calendar.DAY_OF_MONTH)
        );
        // Set minimum date to fromDate
        datePickerDialog.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    //Spinners List Set
    private void SpinnersListApis() {

        //Ref Documents
        apiViewModel.getrefdocuments(getApplicationContext(), compId);
        apiViewModel.getrefdocuments_response().observe(this, dModel -> {
            ArrayList<MaterialItemsList> documentsList = dModel.getItems();
            ArrayList<String> RefDocumentsList = new ArrayList<>();

            // Prepare list for spinner
            for (MaterialItemsList doc : documentsList) {
                if (doc.getActive()) {
                    RefDocumentsList.add(doc.getName());
                }
            }
            RefDocumentsList.add("Others");

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, RefDocumentsList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerRefDoc.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerRefDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    RefDocItem = parent.getItemAtPosition(position).toString();
                    if (RefDocItem.equalsIgnoreCase("Others")) {
                        StatusEditspinnerRefDoc = "true";
                        binding.EditSpinnerRefDoc.setVisibility(View.VISIBLE);
                    } else {
                        StatusEditspinnerRefDoc = "";
                        binding.EditSpinnerRefDoc.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Purpose
        apiViewModel.getentrypurposes(getApplicationContext(), compId);
        apiViewModel.getentrypurposes_response().observe(this, dModel -> {
            ArrayList<MaterialItemsList> documentsList = dModel.getItems();
            ArrayList<String> PurposeList = new ArrayList<>();

            // Prepare list for spinner
            for (MaterialItemsList doc : documentsList) {
                if (doc.getActive()) {
                    PurposeList.add(doc.getName());
                }
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, PurposeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerPurpose.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PurposeItem = parent.getItemAtPosition(position).toString();

                    for (MaterialItemsList item : documentsList) {
                        if (item.getName().equals(PurposeItem)) {
                            PurposeId = item.get_id().get$oid();
                            PurposeReturn = item.getReturnType();
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Location
        apiViewModel.getuserDetails(getApplicationContext(), "address");
        apiViewModel.getuserDetails_response().observe(this, dModel -> {
            LocationList = new ArrayList<>();
            LocationList.clear();
            LocationList = dModel.getItems().getAddress();
            ArrayList<String> visitTypeList = new ArrayList<>();
            visitTypeList.clear();

            // Prepare list for spinner
            for (LocationAddressList doc : LocationList) {
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

                    //Pertains
                    PertainsApi("0"+locationIndexPosition);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });


    }
    private void PertainsApi(String position) {

        //Pertains clear
        ArrayAdapter<String> adapterPertains = (ArrayAdapter<String>) binding.spinnerPertains.getAdapter();
        if (adapterPertains != null) {
            adapterPertains.clear();                 // remove all items
            adapterPertains.notifyDataSetChanged();  // refresh spinner
        }
        //Employee clear
        ArrayAdapter<String> adapterEmployee = (ArrayAdapter<String>) binding.spinnerEmployee.getAdapter();
        if (adapterEmployee != null) {
            adapterEmployee.clear();                 // remove all items
            adapterEmployee.notifyDataSetChanged();  // refresh spinner
        }

        apiViewModel.getsubhierarchysmaterial(getApplicationContext(), compId, position);
        apiViewModel.getsubhierarchysmaterial_response().observe(this, dModel -> {
            subhierarchysmaterial = new ArrayList<>();
            subhierarchysmaterial.clear();
            subhierarchysmaterial = dModel.getItems();
            ArrayList<String> PertainsList = new ArrayList<>();
            PertainsList.clear();

            // Prepare list for spinner
            for (Getsubhierarchys doc : subhierarchysmaterial) {
                PertainsList.add(doc.getName()+"("+doc.getHierarchy()+")");
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, PertainsList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerPertains.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerPertains.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PertainsItem = parent.getItemAtPosition(position).toString();

                    for (Getsubhierarchys item : subhierarchysmaterial) {
                        String dd = item.getName()+"("+item.getHierarchy()+")";
                        if (dd.equalsIgnoreCase(PertainsItem)) {
                            DepartmentId = item.get_id().get$oid();
                            break;
                        }
                    }
                    //employeesList
                    employeesList(locationIndexPosition+"",DepartmentId);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });
    }
    private void employeesList(String position, String departmentId) {
        apiViewModel.getsearchemployeesmaterial(getApplicationContext(), position, departmentId, "");
        apiViewModel.getsearchemployeesmaterial_response().observe(this, model -> {
            searchemployeesmaterial = new ArrayList<>();
            searchemployeesmaterial.clear();
            searchemployeesmaterial = model.getItems();
            ArrayList<String> employeesList = new ArrayList<>();
            employeesList.clear();
            EmployeeId = "";
            // Prepare list for spinner
            for (GetSearchEmployees doc : searchemployeesmaterial) {
                employeesList.add(doc.getName());
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, employeesList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            binding.spinnerEmployee.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            binding.spinnerEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    EmployeeItem = parent.getItemAtPosition(position).toString();
                    for (GetSearchEmployees item : searchemployeesmaterial) {
                        if (item.getName().equals(EmployeeItem)) {
                            EmployeeId = item.get_id().get$oid();
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });
    }

    //add Material Details
    private void AddMaterialDetailsPopUp() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_material_details_popup_layout, null);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();

        // Optional: Make background transparent if you're using rounded corners
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText EditName = dialogView.findViewById(R.id.EditName);
        EditText EditSerialNo = dialogView.findViewById(R.id.EditSerialNo);
        LinearLayout linearDecrement = dialogView.findViewById(R.id.linearDecrement);
        TextView cartQty = dialogView.findViewById(R.id.cartQty);
        LinearLayout linearIncrement = dialogView.findViewById(R.id.linearIncrement);
        TextView txtQuantity = dialogView.findViewById(R.id.txtQuantity);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        counter = 1;

        // Decrement (but not less than 1)
        linearDecrement.setOnClickListener(v -> {
            if (counter > 1) {
                counter--;
                cartQty.setText(String.valueOf(counter));
                txtQuantity.setText(String.valueOf(counter));
            }
        });

        // Increment
        linearIncrement.setOnClickListener(v -> {
            counter++;
            cartQty.setText(String.valueOf(counter));
            txtQuantity.setText(String.valueOf(counter));
        });

        btnAdd.setOnClickListener(v -> {
            String name = EditName.getText().toString().trim();
            String serial = EditSerialNo.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Name and Description", Toast.LENGTH_SHORT).show();
            } else if (serial.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Serial No", Toast.LENGTH_SHORT).show();
            } else {
                MaterialDetailsSet materialDetailsSet = new MaterialDetailsSet(
                        name,
                        counter,
                        serial,
                        false
                );

                materialDetailsList.add(materialDetailsSet);
                bottomSheetDialog.dismiss();
                setMaterialDetailsList();
            }
        });
    }

    private void setMaterialDetailsList() {
        if (materialDetailsList.isEmpty()) {
            binding.linearInfoMaterialPermit.setVisibility(GONE);
        } else {
            binding.linearInfoMaterialPermit.setVisibility(View.VISIBLE);
            Log.e("contractorListsize",materialDetailsList.size()+"");
            binding.txtMaterialPermitCount.setText("("+materialDetailsList.size()+")");
        }

        binding.linearInfoMaterialPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMaterialDetailsListBottomSheetDialogList();
            }
        });
    }
    private void setMaterialDetailsListBottomSheetDialogList() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_material_permit_list, null);
        
        RecyclerView recyclerviewMaterialPermit = view.findViewById(R.id.recyclerviewMaterialPermit);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddMore = view.findViewById(R.id.btnAddMore);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        dialog.show();

        recyclerviewMaterialPermit.setLayoutManager(new LinearLayoutManager(this));
        MaterialPermitDetailsSetUpAdapter adapter = new MaterialPermitDetailsSetUpAdapter(materialDetailsList);
        recyclerviewMaterialPermit.setAdapter(adapter);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SupplierDetailsPopUp();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }


}