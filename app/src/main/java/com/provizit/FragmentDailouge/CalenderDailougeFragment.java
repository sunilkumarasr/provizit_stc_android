package com.provizit.FragmentDailouge;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.R;


public class CalenderDailougeFragment extends BottomSheetDialogFragment {

    private static final String TAG = "CalenderFragment";


    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. BottomSheetSearchDialogTheme);
    }

    CalenderDailougeFragment.ItemSelected listner;
    public void onItemsSelectedListner(CalenderDailougeFragment.ItemSelected listner){
        this.listner = listner;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calender, container, false);
    }


    public interface ItemSelected {
        void onSelected(String comId,String DepName,String CompName);
    }


}