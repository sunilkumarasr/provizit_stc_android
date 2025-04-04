package com.provizit.FragmentDailouge.AllotParking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.AdapterAndModel.AllotParking.AllotParkingSlotsAdapter;
import com.provizit.Config.CustomItemCityListener;
import com.provizit.Conversions;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CompanyData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlotsAllotsListDialouges extends DialogFragment {
    private static final String TAG = "SlotsAllotsListDialouge";


    ImageView img_close;
    ProgressBar progressbar;
    TextView txt_no_data,txt_slot;
    RecyclerView recyclerview;
    private RecyclerView.LayoutManager layoutManagertrending;
    ArrayList<CompanyData> SlotspopupList;
    ArrayList<CompanyData> timepslotsList;
    AllotParkingSlotsAdapter allotParkingCategoryAdapter;

    String category_id = "";

    SlotsAllotsListDialouges.ItemSelected listner;
    public void onItemsSelectedListner(SlotsAllotsListDialouges.ItemSelected listner){
        this.listner = listner;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        category_id = bundle.getString("category_id");
        View view =  inflater.inflate(R.layout.fragment_slots_allots_list_dialouges, container, false);

        Log.e(TAG, "onCreateView:category_id"+category_id );

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        img_close = view.findViewById(R.id.img_close);
        progressbar = view.findViewById(R.id.progressbar);
        txt_no_data = view.findViewById(R.id.txt_no_data);
        txt_slot = view.findViewById(R.id.txt_slot);


        //slots list
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        layoutManagertrending = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManagertrending);
        SlotspopupList = new ArrayList<>();
        timepslotsList = new ArrayList<>();

        getcatlots();


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationimg_reccurence = Conversions.animation();
                v.startAnimation(animationimg_reccurence);

                dismiss();
            }
        });

        return view;
    }


    private void getcatlots() {
        progressbar.setVisibility(View.VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getcatlots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                progressbar.setVisibility(View.GONE);
                final Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        txt_no_data.setVisibility(View.VISIBLE);
                    } else if (statuscode.equals(not_verified)) {
                        txt_no_data.setVisibility(View.VISIBLE);
                    } else if (statuscode.equals(successcode)) {
                        Model1 model1 = response.body();
                        SlotspopupList.clear();
                        SlotspopupList.addAll(model1.getItems());

                        if (SlotspopupList != null && SlotspopupList.size()!=0){



                            for (int i = 0; i < SlotspopupList.size(); i++) {

                                if (AllotParkingFragment.allot_invites_list.size() != 0 && AllotParkingFragment.allot_invites_list != null){
                                    for (int j = 0; j < AllotParkingFragment.allot_invites_list.size(); j++) {
                                       if (SlotspopupList.get(i).get_id().get$oid().equalsIgnoreCase(AllotParkingFragment.allot_invites_list.get(j).getLot_id())){
                                            SlotspopupList.remove(i);
                                            Log.e(TAG, "onResponse:SlotspopupList"+"123" );
                                        }
                                    }
                                }
                            }


                            allotParkingCategoryAdapter=new AllotParkingSlotsAdapter(SlotspopupList, getActivity(),new CustomItemCityListener() {
                                @Override
                                public void onItemClick(View v, String id,String name,String empty) {
                                    gettimepslots(id,name);
                                }
                            });
                            recyclerview.setAdapter(allotParkingCategoryAdapter);

                        }else {
                            txt_no_data.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);
            }
        }, getActivity(), AllotParkingFragment.comp_id, SetupMeetingActivity.empData.getLocation(),category_id,SetupMeetingActivity.s_time,SetupMeetingActivity.e_time-1);
    }


    private void gettimepslots(String L_id, String L_name) {
        txt_slot.setVisibility(View.GONE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.gettimepslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        txt_no_data.setVisibility(View.VISIBLE);
                    } else if (statuscode.equals(not_verified)) {
                        txt_no_data.setVisibility(View.VISIBLE);
                    } else if (statuscode.equals(successcode)) {
                        Model1 model1 = response.body();
                        timepslotsList.clear();
                        timepslotsList.addAll(model1.getItems());


                        if (timepslotsList.size()!=0 && timepslotsList != null){
                            listner.onSelected(L_id,L_name);
                            dismiss();
                        }else {
                            txt_slot.setVisibility(View.VISIBLE);
                        }


                    }
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                txt_no_data.setVisibility(View.VISIBLE);
            }
        }, getActivity(), AllotParkingFragment.comp_id, SetupMeetingActivity.empData.getLocation(),L_id,category_id,"",SetupMeetingActivity.s_time,SetupMeetingActivity.e_time-1);
    }


    public interface ItemSelected {
        void onSelected(String id,String name);
    }


}