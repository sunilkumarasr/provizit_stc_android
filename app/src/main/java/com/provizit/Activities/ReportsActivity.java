package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.FragmentDailouge.CalenderDailougeFragment;
import com.provizit.FragmentDailouge.FilterDailougeFragment;
import com.provizit.FragmentDailouge.ReportsPDFAndXLSFragment;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.LocationData;
import com.provizit.Utilities.PeopleAdapter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ReportsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener  {
    private static final String TAG = "ReportsActivity";
    ImageView img_download;
    ImageView img_calender;
    ImageView img_menu_filter;
    EditText edit_mobile_e_search;
    CustomAdapter customAdapter1,customAdapter2;
    AutoCompleteTextView meeting_location;
    EditText edit_search;
    ImageView img_search;
    ImageView img_search_clear;
    RelativeLayout relative_from_date;
    TextView txt_from_date;
    RelativeLayout relative_from_to;
    TextView txt_to_date;
    Spinner spinner;
    AlertDialog.Builder builder;
    PeopleAdapter adapter1;
    DatabaseHelper myDb;
    EmpData empData;
    ArrayList<LocationData> meetinglocations;
    String location = "0";
    DatePickerDialog datePickerDialog;
    String dateType = "";
    String date_selection = "";
    long from_date_mills = 0;
    long to_date_mills = 0;
    ArrayList<CompanyData> reportlist1;
    ArrayList<CompanyData> reportlist;
    ArrayList<CompanyData> reportlist_final;
    ReportsAdapter reportsAdapter;
    RecyclerView recycler_view;

    String[] types = { "All","Appointments", "Check In", "Meetings"};

    //download pdf
    LinearLayout linear_download_pdf;
    List listpdf = new ArrayList();

    Table table0;
    Table table;

    private static Cell cell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ReportsActivity.this,R.color.colorPrimary));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Reports");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        img_download = findViewById(R.id.img_download);
        img_calender = findViewById(R.id.img_calender);
        img_menu_filter = findViewById(R.id.img_menu_filter);
        edit_mobile_e_search = findViewById(R.id.edit_mobile_e_search);
        meeting_location = findViewById(R.id.location_spinner);
        edit_search = findViewById(R.id.edit_search);
        img_search = findViewById(R.id.img_search);
        img_search_clear = findViewById(R.id.img_search_clear);
        relative_from_date = findViewById(R.id.relative_from_date);
        txt_from_date = findViewById(R.id.txt_from_date);
        relative_from_to = findViewById(R.id.relative_from_to);
        txt_to_date = findViewById(R.id.txt_to_date);
        spinner = findViewById(R.id.spinner);
        recycler_view = findViewById(R.id.recycler_view);
        linear_download_pdf = findViewById(R.id.linear_download_pdf);

        builder = new AlertDialog.Builder(ReportsActivity.this);
        myDb = new DatabaseHelper(ReportsActivity.this);
        empData = new EmpData();
        empData = myDb.getEmpdata();
        meetinglocations = new ArrayList<>();
        meetinglocations = myDb.getCompanyAddress();


        adapter1 = new PeopleAdapter(ReportsActivity.this, R.layout.location_n, R.id.lbl_name, meetinglocations);
        meeting_location.setThreshold(0);//will start working from first character
        meeting_location.setAdapter(adapter1);//setting the adapter data into the AutoCompleteTextView
        meeting_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String str = meeting_location.getText().toString();

                    for (int i = 0; i < adapter1.getCount(); i++) {
                        String temp = Objects.requireNonNull(adapter1.getItem(i)).getName();
                        if (str.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    customAdapter1.getFilter().filter("");
                    meeting_location.setText("");

                    builder.setMessage("Invalid Visitor type")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    ViewController.hideKeyboard(ReportsActivity.this);
                    meeting_location.showDropDown();
                }
            }
        });
        meeting_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View arg0) {
                System.out.println("subhasdh" + meetinglocations.size());
                meeting_location.showDropDown();
            }
        });
        meeting_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                LocationData contactModel = meetinglocations.get(index);
                String name1 = contactModel.getName();
                System.out.println("subashhh" + "0" + index);
                location = index + "";
            }
        });

        String location1 = meetinglocations.get(Integer.parseInt(empData.getLocation())).getName();
        meeting_location.setText(location1);
        reportsList("",location,"",empData.getEmp_id(),from_date_mills,to_date_mills);


        reportlist1 = new ArrayList<>();
        reportlist = new ArrayList<>();
        reportlist_final = new ArrayList<>();

        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        ad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);


        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                filter(text.toString());
                img_search.setVisibility(GONE);
                img_search_clear.setVisibility(View.VISIBLE);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        img_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_search.getText().clear();
                img_search.setVisibility(View.VISIBLE);
                img_search_clear.setVisibility(GONE);
                filter("");
            }
        });

        cell = null;

        relative_from_date.setOnClickListener(this);
        relative_from_to.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        linear_download_pdf.setOnClickListener(this);
        img_download.setOnClickListener(this);
        img_calender.setOnClickListener(this);
        img_menu_filter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_from_date:
                dateType = "from";
                txt_to_date.setText("");
                setDateTimeField();
                break;
            case R.id.relative_from_to:
                if (date_selection.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please Select From Date First",Toast.LENGTH_SHORT).show();
                }else {
                    dateType = "to";
                    setDateTimeField();
                }
                break;
            case R.id.linear_download_pdf:
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                xlxs();
                break;
            case R.id.img_download:
                FragmentManager fm = getSupportFragmentManager();
                ReportsPDFAndXLSFragment sFragment = new ReportsPDFAndXLSFragment();
                // Show DialogFragment
                sFragment.onItemsSelectedListner((comId, DepName, CompName) -> {

                });
                sFragment.show(fm, "Dialog Fragment");
                break;
            case R.id.img_calender:
                FragmentManager fm1 = getSupportFragmentManager();
                CalenderDailougeFragment sFragment1 = new CalenderDailougeFragment();
                // Show DialogFragment
                sFragment1.onItemsSelectedListner((comId, DepName, CompName) -> {

                });
                sFragment1.show(fm1, "Dialog Fragment");
                break;
            case R.id.img_menu_filter:
                FragmentManager fm2 = getSupportFragmentManager();
                FilterDailougeFragment sFragment2 = new FilterDailougeFragment();
                // Show DialogFragment
                sFragment2.onItemsSelectedListner((comId, DepName, CompName) -> {

                });
                sFragment2.show(fm2, "Dialog Fragment");
                break;
        }
    }

    private void setDateTimeField() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(ReportsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        if (dateType.equalsIgnoreCase("from")){
                            String date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            txt_from_date.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);
                            from_date_mills = Conversions.getdatestamp(date_time);
                            date_selection = from_date_mills+"";
                        }else {
                            txt_to_date.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);
                            String date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            to_date_mills = Conversions.getdatestamp(date_time);
                            Log.e(TAG, "onDateSet:url "+ meeting_location.getId()+""+empData.getEmp_id()+from_date_mills+to_date_mills);

                            reportsList("",location,"",empData.getEmp_id(),from_date_mills,to_date_mills);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void reportsList(String type, String location_id, String h_id, String emp_id, long from_mills, long to_mills) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.reportsList(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();

                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        reportlist1 = model.getItems();
                        reportlist.addAll(reportlist1);

                        for (int i = 0; i < reportlist.size(); i++) {
                            reportlist_final.add(reportlist.get(i));
                            listpdf.add(reportlist.get(i));
                        }

                        reportsAdapter = new ReportsAdapter(getApplicationContext(), reportlist_final);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        recycler_view.setLayoutManager(manager);
                        //set adapter
                        recycler_view.setAdapter(reportsAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                System.out.println(t + "subhash");
                Toast.makeText(ReportsActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }
        },ReportsActivity.this, type,"",h_id,emp_id,from_mills+"",to_mills+"");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//       Toast.makeText(getApplicationContext(),types[position],Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public class ReportsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        ArrayList<CompanyData> repot;
        private Context context;
        String click_s = "0";

        public ReportsAdapter(Context context, ArrayList<CompanyData> repot) {
            this.repot = repot;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_list_items, parent, false);
            return new ReportsAdapter.ViewHolderToday(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ReportsAdapter.ViewHolderToday Holder = (ReportsAdapter.ViewHolderToday) holder;

            Log.e(TAG, "onBindViewHolder:history:stype "+repot.get(position).getSupertype());



            for (int i = 0; i < repot.get(position).getHistory().size(); i++) {
                if (repot.get(position).getHistory().get(i).getCheckin()==0.0){
                    String date1 = Conversions.millitodateD((Long.parseLong(repot.get(position).getCreated_time().get$numberLong())+Conversions.timezone()) * 1000);
                    String s_time = Conversions.millitotime((Long.parseLong(repot.get(position).getCreated_time().get$numberLong()) + Conversions.timezone()) * 1000, false);
                    Holder.txt_date.setText(date1+" "+s_time);
                }else {
                    String date = Conversions.millitodateD((repot.get(position).getHistory().get(i).getCheckin()+Conversions.timezone()) * 1000);
                    String time = Conversions.millitotime((repot.get(position).getHistory().get(i).getCheckin() + Conversions.timezone()) * 1000, false);
                    Holder.txt_date.setText(date+" "+time);
                }
            }




            Holder.txt_visiter_name.setText(repot.get(position).getName());
            Holder.txt_purpose.setText(repot.get(position).getHistory().get(0).getPurpose());
            Holder.txt_designation.setText(repot.get(position).getDesignation());
            Holder.txt_origanization.setText(repot.get(position).getCompany());
            Holder.txt_origanization_address.setText(repot.get(position).getCaddress());
            Holder.txt_nationality.setText(repot.get(position).getNation());
            Holder.txt_host.setText(repot.get(position).getEmployee().getName());
            Holder.txt_department.setText(repot.get(position).getEmployee().getDepartment());
            Holder.txt_location_n_venue.setText(repot.get(position).getEmployee().getBranch());





//            if (repot.get(position).getName().equalsIgnoreCase("")||repot.get(position).getName()==null){
//                Holder.linear_visiter.setVisibility(GONE);
//            }else if (repot.get(position).getHistory().get(0).getPurpose().equalsIgnoreCase("")||repot.get(position).getHistory().get(0).getPurpose()==null){
//                Holder.linear_purpose.setVisibility(GONE);
//            }else if (repot.get(position).getDesignation().equalsIgnoreCase("")||repot.get(position).getDesignation()==null){
//                Holder.linear_designation.setVisibility(GONE);
//            }else if (repot.get(position).getCompany().equalsIgnoreCase("")||repot.get(position).getCompany()==null){
//                Holder.linear_organization.setVisibility(GONE);
//            }else if (repot.get(position).getCaddress().equalsIgnoreCase("")||repot.get(position).getCaddress()==null){
//                Holder.linear_organization_addrss.setVisibility(GONE);
//            }else if (repot.get(position).getNation().equalsIgnoreCase("")||repot.get(position).getNation()==null){
//                Holder.linear_nationality.setVisibility(GONE);
//            }else if (repot.get(position).getEmployee().getName().equalsIgnoreCase("")||repot.get(position).getEmployee().getName()==null){
//                Holder.linear_host.setVisibility(GONE);
//            }else if (repot.get(position).getEmployee().getDepartment().equalsIgnoreCase("")||repot.get(position).getEmployee().getDepartment()==null){
//                Holder.linear_department.setVisibility(GONE);
//            }else if (repot.get(position).getEmployee().getBranch().equalsIgnoreCase("")||repot.get(position).getEmployee().getBranch()==null){
//                Holder.linear_location.setVisibility(GONE);
//            }else {
//                Holder.linear_visiter.setVisibility(View.VISIBLE);
//                Holder.linear_purpose.setVisibility(View.VISIBLE);
//                Holder.linear_designation.setVisibility(View.VISIBLE);
//                Holder.linear_organization.setVisibility(View.VISIBLE);
//                Holder.linear_organization_addrss.setVisibility(View.VISIBLE);
//                Holder.linear_nationality.setVisibility(View.VISIBLE);
//                Holder.linear_host.setVisibility(View.VISIBLE);
//                Holder.linear_department.setVisibility(View.VISIBLE);
//                Holder.linear_location.setVisibility(View.VISIBLE);
//
//                Holder.txt_visiter_name.setText(repot.get(position).getName());
//                Holder.txt_purpose.setText(repot.get(position).getHistory().get(0).getPurpose());
//                Holder.txt_designation.setText(repot.get(position).getDesignation());
//                Holder.txt_origanization.setText(repot.get(position).getCompany());
//                Holder.txt_origanization_address.setText(repot.get(position).getCaddress());
//                Holder.txt_nationality.setText(repot.get(position).getNation());
//                Holder.txt_host.setText(repot.get(position).getEmployee().getName());
//                Holder.txt_department.setText(repot.get(position).getEmployee().getDepartment());
//                Holder.txt_location_n_venue.setText(repot.get(position).getEmployee().getBranch());
//            }



            Holder.linear_show_more_less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click_s.equalsIgnoreCase("0")){
                        click_s = "1";
                        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_up);
                        Holder.linear_show.startAnimation(bottomUp);
                        Holder.linear_show.setVisibility(View.VISIBLE);
                        Holder.txt_show.setText("Show Less");
                        Holder.img_arrow.animate().rotation(180).start();
                    }else {
                        click_s = "0";
                        Holder.linear_show.setVisibility(GONE);
                        Holder.txt_show.setText("Show More");
                        Holder.img_arrow.animate().rotation(0).start();
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return repot.size();
        }


        public class ViewHolderToday extends RecyclerView.ViewHolder {

            TextView txt_date;
            LinearLayout linear_visiter;
            TextView txt_visiter_name;
            LinearLayout linear_purpose;
            TextView txt_purpose;
            LinearLayout linear_show;
            LinearLayout linear_designation;
            TextView txt_designation;
            LinearLayout linear_organization;
            TextView txt_origanization;
            LinearLayout linear_organization_addrss;
            TextView txt_origanization_address;
            LinearLayout linear_nationality;
            TextView txt_nationality;
            LinearLayout linear_host;
            TextView txt_host;
            LinearLayout linear_department;
            TextView txt_department;
            LinearLayout linear_location;
            TextView txt_location_n_venue;
            LinearLayout linear_show_more_less;
            ImageView img_arrow;
            TextView txt_show;

            public ViewHolderToday(@NonNull View itemView) {
                super(itemView);
                txt_date = itemView.findViewById(R.id.txt_date);
                linear_visiter = itemView.findViewById(R.id.linear_visiter);
                txt_visiter_name = itemView.findViewById(R.id.txt_visiter_name);
                linear_purpose = itemView.findViewById(R.id.linear_purpose);
                txt_purpose = itemView.findViewById(R.id.txt_purpose);
                linear_show = itemView.findViewById(R.id.linear_show);
                linear_designation = itemView.findViewById(R.id.linear_designation);
                txt_designation = itemView.findViewById(R.id.txt_designation);
                linear_organization = itemView.findViewById(R.id.linear_organization);
                txt_origanization = itemView.findViewById(R.id.txt_origanization);
                linear_organization_addrss = itemView.findViewById(R.id.linear_organization_addrss);
                txt_origanization_address = itemView.findViewById(R.id.txt_origanization_address);
                linear_nationality = itemView.findViewById(R.id.linear_nationality);
                txt_nationality = itemView.findViewById(R.id.txt_nationality);
                linear_host = itemView.findViewById(R.id.linear_host);
                txt_host = itemView.findViewById(R.id.txt_host);
                linear_department = itemView.findViewById(R.id.linear_department);
                txt_department = itemView.findViewById(R.id.txt_department);
                linear_location = itemView.findViewById(R.id.linear_location);
                txt_location_n_venue = itemView.findViewById(R.id.txt_location_n_venue);
                linear_show_more_less = itemView.findViewById(R.id.linear_show_more_less);
                img_arrow = itemView.findViewById(R.id.img_arrow);
                txt_show = itemView.findViewById(R.id.txt_show);

            }
        }

        public void updateList(ArrayList<CompanyData> list){
            repot = list;
            notifyDataSetChanged();
        }

    }

    private void filter(String text) {
        ArrayList<CompanyData> temp = new ArrayList();
        for(CompanyData d: reportlist_final){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text) || d.getEmail().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        if (temp.size()==0){
            reportsAdapter.updateList(temp);
        }
        else {
            //update recyclerview
            reportsAdapter.updateList(temp);
        }
    }

    private void createPdf() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,"ReportsPdf.pdf");
        OutputStream outputStream = new FileOutputStream(file);


        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        //image set
        Drawable d = getDrawable(R.drawable.logo);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();
        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        image.setHeight(35);
        image.setWidth(100);
        document.add(image);


        Text text = new Text("Reports").setBold().setFontSize(18);
        Paragraph paragraph = new Paragraph();
        paragraph.setTextAlignment(TextAlignment.LEFT);
        paragraph.add(text);


        Paragraph paragraphname = new Paragraph("Name");
        paragraphname.setBold();
        Paragraph paragraphpur = new Paragraph("Purpose");
        paragraphpur.setBold();
        Paragraph paragraphaddr = new Paragraph("Address");
        paragraphaddr.setBold();

        float columnwidth[] = {100f, 100f, 100f};
        TableRow tbrow = new TableRow(this);

        table0 = new Table(columnwidth);
        table0.addCell(paragraphname);
        table0.addCell(paragraphpur);
        table0.addCell(paragraphaddr);

        table = new Table(columnwidth);



        for (int j = 0; j < reportlist_final.size(); j++) {
            table.addCell(reportlist_final.get(j).getName());
            table.addCell(reportlist_final.get(j).getHistory().get(0).getPurpose());
            table.addCell(reportlist_final.get(j).getCaddress());
        }


        document.add(paragraph);
        document.add(table0);
        document.add(table);
        document.close();
        Toast.makeText(getApplicationContext(),"downloading....",Toast.LENGTH_LONG).show();

    }

    private void xlxs() {
        
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File filePath = new File(pdfPath,"myReportsmls.xls");

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Reports");


        Row rowDatas = hssfSheet.createRow(0);
        org.apache.poi.ss.usermodel.Cell hssfCells = rowDatas.createCell(0);

        hssfCells = rowDatas.createCell(0);
        hssfCells.setCellValue("Name");


        hssfCells = rowDatas.createCell(1);
        hssfCells.setCellValue("Subject");


        hssfCells = rowDatas.createCell(2);
        hssfCells.setCellValue("Address");



 //     HSSFRow hssfRow = hssfSheet.createRow(0);


        for (int j = 0; j < reportlist_final.size(); j++) {
//            HSSFCell hssfCell = hssfRow.createCell(j);
//            hssfCell.setCellValue(reportlist.get(j).getName());

            Row rowData = hssfSheet.createRow(j+1);
            org.apache.poi.ss.usermodel.Cell hssfCell = rowData.createCell(j);

            hssfCell = rowData.createCell(0);
            hssfCell.setCellValue(reportlist_final.get(j).getName());

            hssfCell = rowData.createCell(1);
            hssfCell.setCellValue(reportlist_final.get(j).getHistory().get(0).getPurpose());


            hssfCell = rowData.createCell(2);
            hssfCell.setCellValue(reportlist_final.get(j).getCaddress());

            Log.e(TAG, "xlxs:count"+"1" );
        }

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
        return super.onOptionsItemSelected(item);
    }

}
