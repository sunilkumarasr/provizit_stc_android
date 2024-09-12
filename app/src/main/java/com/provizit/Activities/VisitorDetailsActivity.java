package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.Logins.InitialActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.QrIndexModelRequest;
import com.provizit.Config.Preferences;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.Inviteeitem;
import com.provizit.Utilities.Inviteemodelclass;
import com.provizit.Utilities.Other;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitorDetailsActivity extends AppCompatActivity {
    private static final String TAG = "VisitorDetailsActivity";

    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;

    SharedPreferences sharedPreferences1;
    CircleImageView pic;
   DatabaseHelper myDb;
   TextView name,company,mobile,department,designation,nationality,email;
   EmpData empData;
    Inviteeitem item;
    RecyclerView emplyoeeform;
    Adapter form;
    ArrayList<Other> other1;
    ContentValues values;
    Bitmap thumbnail;
    Uri imageUri;
    Bitmap bitmap;
    private String TIME_STAMP = "null";
    private static final int PICTURE_RESULT = 1;
    private String encodedString;
    String filename;
    ArrayList<String> picArray,picsArray;
    ProgressDialog progress;
    Switch l_switch;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor1;
    TextView logout;

    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);


        //internet connection
        relative_internet = findViewById(R.id.relative_internet);
        relative_ui = findViewById(R.id.relative_ui);
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    relative_internet.setVisibility(GONE);
                    relative_ui.setVisibility(View.VISIBLE);
                }else {
                    relative_internet.setVisibility(View.VISIBLE);
                    relative_ui.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        apiViewModel = new ViewModelProvider(VisitorDetailsActivity.this).get(ApiViewModel.class);

        progress = new ProgressDialog(VisitorDetailsActivity.this);
        builder = new AlertDialog.Builder(VisitorDetailsActivity.this);
        progress.setCancelable(false);
        progress.show();
        Toolbar toolbar = findViewById(R.id.toolbar);
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(this);
        empData = myDb.getEmpdata();
        l_switch = findViewById(R.id.l_switch);
        logout = findViewById(R.id.logout);
        pic = findViewById(R.id.pic);
        company = findViewById(R.id.company);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        department = findViewById(R.id.department);
        designation = findViewById(R.id.designation);
        picArray = new ArrayList<>();
        picsArray = new ArrayList<>();
        emplyoeeform = findViewById(R.id.emplyoeeform);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(VisitorDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        emplyoeeform.setLayoutManager(linearLayoutManager1);
        editor1 = sharedPreferences1.edit();
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            TextView versionCode = findViewById(R.id.version_Code);
            versionCode.setText("Version " + info.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseMessaging.getInstance().deleteToken();

                                myDb.logout();
                                editor1.putString("language","en");
                                editor1.apply();
                                Intent intent = new Intent(VisitorDetailsActivity.this, InitialActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                Preferences.saveStringValue(getApplicationContext(), "status", "failed");
                                Preferences.deleteSharedPreferences(getApplicationContext());
                                startActivity(intent);
                                finish();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("");
                alert.show();
            }
        });
       if (sharedPreferences1.getString("language", "").equals("ar")){
            l_switch.setChecked(true);
       }
       else
        {
            l_switch.setChecked(false);
        }
        l_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DataManger.appLanguage = "ar";
                    setLocale("ar");
                    editor1.putString("language","ar");
                    editor1.apply();
                }
                else{
                    DataManger.appLanguage = "en";
                    setLocale("en");
                    editor1.putString("language","en");
                    editor1.apply();
                }
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                dispatchTakePictureIntent();
                //CropImage.activity().start(VisitorDetailsActivity.this);
            }
        });
        apiViewModel.getemployeeformdetails(getApplicationContext());

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //Employee Form Details
        apiViewModel.getemployeeformdetailsResponse().observe(this, new Observer<Inviteemodelclass>() {
            @Override
            public void onChanged(Inviteemodelclass response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    }else if (statuscode.equals(not_verified)) {

                    }else if (statuscode.equals(successcode)) {
                        ArrayList<Other> other= new ArrayList<>();
                        other1= new ArrayList<>();
                        other = response.getItem().getOther();
                        for(int position=0 ; position < other.size();position++){
                            if(other.get(position).getStatus() && !other.get(position).getUnique() && !other.get(position).getDepends() && !other.get(position).getHidden() && !other.get(position).getModel().equals("designation")){
                                other1.add(other.get(position));
                            }
                        }
                        form = new Adapter(VisitorDetailsActivity.this,other1);
                        emplyoeeform.setAdapter(form);
                        apiViewModel.getEmployeeDetails(getApplicationContext(),empData.getEmp_id());
                    }
                }else {
                    Conversions.errroScreen(VisitorDetailsActivity.this, "getemployeeformdetails");
                }
            }
        });

        //Employee Details
        apiViewModel.getEmployeeDetailsResponse().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject response) {
                progress.dismiss();
                if (response != null) {
                    Gson gson = new Gson();
                    Inviteemodelclass model = new Inviteemodelclass();

                    for(int position=0 ; position < other1.size();position++) {
                        try {
                            System.out.println("shjsdkh "+ response.getAsJsonObject("items").get(other1.get(position).getModel()).toString());
                            other1.get(position).setData(response.getAsJsonObject("items").get(other1.get(position).getModel()).toString().replace("\"",""));
                            form.notifyDataSetChanged();
                        }catch (Exception e){
                        }
                    }

                    model = gson.fromJson(String.valueOf(response), Inviteemodelclass.class);
                    if (model != null) {
                        Integer statuscode = model.getResult();
                        Integer successcode = 200, failurecode = 401, not_verified = 404;
                        if (statuscode.equals(failurecode)) {

                        } else if (statuscode.equals(not_verified)) {

                        } else if (statuscode.equals(successcode))  {
                            item = new Inviteeitem();
                            item = model.getItems();
                            name.setText(item.getName());
                            email.setText(item.getEmail());

                            company.setText(DatabaseHelper.companyname);
                            mobile.setText(item.getMobile());
//                        role.setText(item.getRoleDetails().getName());
                            try{
                                department.setText(item.getHierarchys().getName());
                            }catch (Exception e ){
                                department.setText("Security");
                            }
                            designation.setText(item.getDesignation());

                            picArray = item.getPics();

                            if (item.getPic() != null && item.getPic().size() != 0) {
                                //preferences
                                Preferences.saveStringValue(getApplicationContext(), Preferences.Profile_Url, DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + item.getPic().get(item.getPic().size() - 1)+"");
                                Glide.with(VisitorDetailsActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + item.getPic().get(item.getPic().size() - 1))
                                        .into(pic);
                            }
                        }
                    }
                }else {
                    Conversions.errroScreen(VisitorDetailsActivity.this, "getEmployeeDetails");
                }
            }
        });

        //qrindex
        apiViewModel.qrindexResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                String statuscode = response;
                String successcode = "200", failurecode = "401", not_verified = "404";
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    picArray = new ArrayList<>();
                    picArray.add(filename);
                    picsArray.add(filename);
                    JsonObject json = updatepicjson();
                    apiViewModel.actionEmployees(getApplicationContext(),json);
                }
            }
        });

        //action Employees
        apiViewModel.actionEmployeesResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                if (response != null) {
                    String statuscode = response.result+"";
                    String successcode = "200", failurecode = "401", not_verified = "404";
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        apiViewModel.getemployeeformdetails(getApplicationContext());
                    }
                }else {
                    Conversions.errroScreen(VisitorDetailsActivity.this, "actionEmployees");
                }
            }
        });


    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
        return super.onOptionsItemSelected(item);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.MyviewHolder> {
        Context context;
        ArrayList<Other> other;

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView label,value;
            public MyviewHolder(View view) {
                super(view);
                label = view.findViewById(R.id.label);
                value = view.findViewById(R.id.value);
            }
        }

        public Adapter(Context context, ArrayList<Other> other) {
            this.context = context;
            this.other = other;
        }

        @Override
        public  Adapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.emplyoee_form, parent, false);
            return new Adapter.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final  Adapter.MyviewHolder holder, final int position) {
            System.out.println("Size@"+other.size());
//            *ngIf="item.status && !item.unique && !item.depends && !item.hidden"
                holder.label.setText(other.get(position).getLabel());
                holder.value.setText(other.get(position).getData());
//            holder.agneda.setText((position+1) + "." + agenda.get(position).getDesc());
        }
        @Override
        public int getItemCount() {
            return other.size();
        }
    }

    private void dispatchTakePictureIntent() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(VisitorDetailsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){
                    if (permissionsGrantedCamera()) {
                        Log.e(TAG, "onClick:"+"cam" );
            values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                intent.putExtra("android.media.action.IMAGE_CAPTURE", 1);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra("aandroid.media.action.IMAGE_CAPTURE",1);
                intent.putExtra("android.media.action.IMAGE_CAPTURE", true);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 1);
        } else {
            ActivityCompat.requestPermissions(VisitorDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
                        Log.e(TAG, "onClick:"+"per" );
        }
        }
         else if (options[item].equals("Choose from Gallery")){
                    if (permissionsGrantedPickImage()) {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                    else {
                        ActivityCompat.requestPermissions(VisitorDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

                    }
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //                imageUri = result.getUri();
//                pic.setImageURI(imageUri);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        try (ParcelFileDescriptor pfd = VisitorDetailsActivity.this.getContentResolver().openFileDescriptor(imageUri, "r")) {
                            if (pfd != null) {
                                String imageurl = getRealPathFromURI(imageUri);
//                                        compressImage(imageurl);
                                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                                encodedString = encodeTobase64(BitmapFactory.decodeFile(imageurl),true);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                        String imageurl = getRealPathFromURI(imageUri);
//                                compressImage(imageurl);
                        bitmap = BitmapFactory.decodeFile(imageurl);
                        encodedString = encodeTobase64(BitmapFactory.decodeFile(imageurl),true);
                    }
                    Log.d("base64 ", encodedString);
                    filename = Conversions.datemillirandstring() + ".jpeg";
                    Matrix matrix = new Matrix();
                    matrix.postRotate(-90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//                            pic.setImageBitmap(rotatedBitmap);
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    //cid(company id),key(.jpeg or .png),img(base64)//
                    try {
                        jsonObj_.put("cid", sharedPreferences1.getString("company_id", null));
                        jsonObj_.put("key", filename);
                        jsonObj_.put("img", encodedString);
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        QrIndexModelRequest qrIndexModelRequest = new QrIndexModelRequest(sharedPreferences1.getString("company_id", null),filename,encodedString);
                        apiViewModel.qrindex(getApplicationContext(),qrIndexModelRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                        myDb.update("emp_table","EMPIMG",filename);
            } else {
                finish();
            }
        }
        else {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();

            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            filename = Conversions.datemillirandstring() + ".jpeg";
            //compressImage(picturePath,filename);

            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            //cid(company id),key(.jpeg or .png),img(base64)//
            try {
                jsonObj_.put("cid", sharedPreferences1.getString("company_id", null));
                jsonObj_.put("key", filename);
                jsonObj_.put("img", encodeTobase64(thumbnail,false));
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

                QrIndexModelRequest qrIndexModelRequest = new QrIndexModelRequest(sharedPreferences1.getString("company_id", null),filename,encodeTobase64(thumbnail,false));
                apiViewModel.qrindex(getApplicationContext(),qrIndexModelRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }


//                  Log.w("path of image from gallery......******************.........", picturePath + "");
            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(), thumbnail.getHeight(), true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            //  pic.setImageBitmap(rotatedBitmap);
            //  myDb.update("emp_table","EMPIMG",filename);

        }

    }

    private Boolean permissionsGrantedPickImage() {
        return ContextCompat.checkSelfPermission(VisitorDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private Boolean permissionsGrantedCamera() {
        if(ContextCompat.checkSelfPermission(VisitorDetailsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(VisitorDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else {
            return false;
        }
    }
    public static String encodeTobase64(Bitmap image ,Boolean Status) {
        Matrix matrix = new Matrix();
        if(Status){
            matrix.postRotate(-90);
        }
        else {
            matrix.postRotate(0);
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        Bitmap immagex = rotatedBitmap;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, NavigationActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refresh);
    }

    private JsonObject updatepicjson() {
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray pic = new JSONArray(picArray);
        JSONArray pics = new JSONArray(picsArray);
        try {
            jsonObj_.put("formtype", "pic");
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("id", empData.getEmp_id());
            jsonObj_.put("pic", pics);
            jsonObj_.put("pics",pic);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    public String compressImage(String imageUri,String filenameForServer) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            Log.d("File name ",  filenameForServer + "");
            Log.d("File Size ", scaledBitmap.getByteCount() + "");
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            //cid(company id),key(.jpeg or .png),img(base64)//
            try {
                jsonObj_.put("cid", sharedPreferences1.getString("company_id", null));
                jsonObj_.put("key", filenameForServer);
                jsonObj_.put("img", encodeTobase64(scaledBitmap,false));
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

                QrIndexModelRequest qrIndexModelRequest = new QrIndexModelRequest(sharedPreferences1.getString("company_id", null),filename,encodeTobase64(scaledBitmap,false));
                apiViewModel.qrindex(getApplicationContext(),qrIndexModelRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pic.setImageBitmap(scaledBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

}
