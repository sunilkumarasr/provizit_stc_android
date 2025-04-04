package com.provizit.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutlookIntegration extends AsyncTask<String, Integer, String> {
    String company_name;
    DatabaseHelper myDb;
    Context activity;

    public OutlookIntegration(Context context) {
        activity = context;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(company_name + " PreExceute", "On pre Exceute......");
    }

    protected String doInBackground(String... strings) {
        myDb = new DatabaseHelper(activity);
        company_name = strings[0];
        Log.d(company_name + " DoINBackGround", "On doInBackground...");


        DataManger dataManager = DataManger.getDataManager();
        dataManager.getameetings(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {

            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {

            }
        }, "", company_name, myDb.getEmpdata().getEmail(), "", "");

        DataManger dataManager1 = DataManger.getDataManager();
        dataManager1.getumeetings(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {


            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {



            }
        }, "", company_name, myDb.getEmpdata().getEmail(), "", "");

        return "You are at PostExecute";
    }

    protected void onProgressUpdate(Integer... a) {
        super.onProgressUpdate(a);
        Log.d("tab" + " onProgressUpdate", "You are in progress update ... " + a[0]);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("asf" + " onPostExecute", "" + result);
    }

}
