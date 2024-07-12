package com.provizit.FragmentDailouge;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.R;

public class UpdatePopUpFragment extends BottomSheetDialogFragment {
    private static final String TAG = "UpdatePopUpFragment";


    private FragmentActivity myContext;

    CardView cardview_update;


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


    UpdatePopUpFragment.ItemSelected listner;
    public void onItemsSelectedListner(UpdatePopUpFragment.ItemSelected listner){
        this.listner = listner;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_pop_up, container, false);


        cardview_update = view.findViewById(R.id.cardview_update);


        cardview_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.provizit.ksa");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), " unable to find Provizit", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }


    public interface ItemSelected {
        void onSelected(String comId,String DepName,String CompName);
    }


}