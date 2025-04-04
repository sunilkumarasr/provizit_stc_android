package com.provizit.FragmentDailouge.AllotParking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.provizit.AdapterAndModel.AllotParking.AllotParkingCategoryAdapter;
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


public class CategoryAllotsListDialouges extends DialogFragment {
    private static final String TAG = "CategoryAllotsListDialo";


    ImageView img_close;
    ProgressBar progressbar;
    TextView txt_no_data;
    RecyclerView recyclerview;
    private RecyclerView.LayoutManager layoutManagertrending;
    ArrayList<CompanyData> CategorypopupList;
    AllotParkingCategoryAdapter allotParkingCategoryAdapter;


    CategoryAllotsListDialouges.ItemSelected listner;
    public void onItemsSelectedListner(CategoryAllotsListDialouges.ItemSelected listner){
        this.listner = listner;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_allots_list_dialouges, container, false);


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        img_close = view.findViewById(R.id.img_close);
        progressbar = view.findViewById(R.id.progressbar);
        txt_no_data = view.findViewById(R.id.txt_no_data);


        //categorys list
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        layoutManagertrending = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManagertrending);
        CategorypopupList = new ArrayList<>();

        category_new_api();


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

    private void category_new_api() {
        progressbar.setVisibility(View.VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getpslotcategories(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                progressbar.setVisibility(View.GONE);
                final Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        Model1 model1 = response.body();
                        CategorypopupList.clear();
                        CategorypopupList.addAll(model1.getItems());

                        if (CategorypopupList != null && CategorypopupList.size()!=0){

                            allotParkingCategoryAdapter=new AllotParkingCategoryAdapter(CategorypopupList, getActivity(),new CustomItemCityListener() {
                                @Override
                                public void onItemClick(View v, String id,String name,String empty) {


                                    listner.onSelected(id,name);
                                    dismiss();
                                }
                            });
                            recyclerview.setAdapter(allotParkingCategoryAdapter);
                        }


                    }
                }
            }
            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        }, getActivity(), AllotParkingFragment.comp_id+"");
    }


    public interface ItemSelected {
        void onSelected(String id,String name);
    }

}