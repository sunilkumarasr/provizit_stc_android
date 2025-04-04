package com.provizit.FragmentDailouge.AllotParking;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.AdapterAndModel.AllotParking.AllotParkingAdapter;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Utilities.AllotInvited;
import com.provizit.Utilities.Invited;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class AllotParkingFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    private static final String TAG = "AllotParkingFragment";

    private FragmentActivity myContext;

    SharedPreferences sharedPreferences1;
    public static String comp_id="";

    CardView card_view_progress;
    EditText edit_search;
    public static RecyclerView recyclerview;
    public static List<AllotInvited> allot_invites_list ;
    public static AllotParkingAdapter allotParkingAdapter;

    CheckBox pSlotCheckBox;

    public static JSONArray allotinvites;

    Button bt_allot,bt_cancel;

    ApiViewModel apiViewModel;
    List<Invited> invitees_list1;

    boolean pSlot = false;

    @Override
    public void onAttach(@NonNull Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setStyle(STYLE_NORMAL, R.style. BottomSheetSearchDialogTheme);
    }

    AllotParkingFragment.ItemSelected listner;
    public void onItemsSelectedListner(AllotParkingFragment.ItemSelected listner){
        this.listner = listner;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
            // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
            FrameLayout bottomSheet =  dialogc.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_allot_parking, container, false);


        inits(view);


        bt_allot.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        return view;
    }

    private void inits(View view) {
        sharedPreferences1 = getActivity().getSharedPreferences("EGEMSS_DATA", 0);
        comp_id = sharedPreferences1.getString("company_id", null);

        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);

        invitees_list1 = new ArrayList<>();
        invitees_list1.clear();
        invitees_list1.addAll(SetupMeetingActivity.invitedArrayList);

        allot_invites_list = new ArrayList<>();
        allot_invites_list.clear();
        allotinvites = new JSONArray();


        card_view_progress = view.findViewById(R.id.card_view_progress);
        edit_search = view.findViewById(R.id.edit_search);
        pSlotCheckBox = view.findViewById(R.id.pSlotCheckBox);
        bt_allot = view.findViewById(R.id.bt_allot);
        bt_cancel = view.findViewById(R.id.bt_cancel);

        pSlotCheckBox.setOnClickListener(view1 -> {
            if (pSlotCheckBox.isChecked()){
                pSlotCheckBox.setChecked(true);
                pSlot = true;

                allotParkingAdapter.setEnableds(false);

                checkAllCheckboxes(true);
            }else {
                pSlotCheckBox.setChecked(false);
                pSlot = false;

                allotParkingAdapter.setEnableds(true);
                checkAllCheckboxes(false);
            }
        });

        recyclerview = view.findViewById(R.id.recycler);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        apiViewModel.getEmployees(getActivity(), comp_id);
        card_view_progress.setVisibility(VISIBLE);
        apiViewModel.getEmployees_response().observe(getActivity(), response -> {
            card_view_progress.setVisibility(GONE);
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)){

                } else if (statuscode.equals(successcode)){
                    if (response.getItems()!=null){

                        for (int i = 0; i < response.getItems().size(); i++) {

                            for (int z = 0; z < SetupMeetingActivity.invitedArrayList.size(); z++) {

                                //email
                                if ( !SetupMeetingActivity.invitedArrayList.get(z).getEmail().equalsIgnoreCase("") && !response.getItems().get(i).getEmail().equalsIgnoreCase("") && response.getItems().get(i).getEmail().equalsIgnoreCase(SetupMeetingActivity.invitedArrayList.get(z).getEmail())){
                                    invitees_list1.remove(z);
                                }

                                //phone
                                if ( !SetupMeetingActivity.invitedArrayList.get(z).getMobile().equalsIgnoreCase("") && !response.getItems().get(i).getMobile().equalsIgnoreCase("") && response.getItems().get(i).getMobile().equalsIgnoreCase(SetupMeetingActivity.invitedArrayList.get(z).getMobile())){
                                    invitees_list1.remove(z);
                                }

                            }

                        }

                        allotParkingAdapter = new AllotParkingAdapter(getActivity(), invitees_list1, (v, position, id) -> {

                        });
                        recyclerview.setAdapter(allotParkingAdapter);
                        allotParkingAdapter.notifyDataSetChanged();

                    }
                }
            }else {
                Conversions.errroScreen(getActivity(), "getEmployees");
            }

        });


        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                filter(text.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_allot:
                allotinvites = new JSONArray();
                try {
                    for (int i = 0; i < allot_invites_list.size(); i++) {
                        allotinvites.put(allot_invites_list.get(i).getAllotInvites());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < SetupMeetingActivity.invitedArrayList.size(); i++) {
                    if ((SetupMeetingActivity.invitedArrayList.get(i).getCat_id() != null &&
                            !SetupMeetingActivity.invitedArrayList.get(i).getCat_id().equalsIgnoreCase("")) &&
                            (SetupMeetingActivity.invitedArrayList.get(i).getLot_id() != null &&
                                    !SetupMeetingActivity.invitedArrayList.get(i).getLot_id().equalsIgnoreCase("")) ||
                            SetupMeetingActivity.invitedArrayList.get(i).getAuto_allot() == true ||
                            pSlot == true) {

                        listner.onSelected(allotinvites, "", pSlot);
                        dismiss();

                    }else {
                        Toast.makeText(getActivity(),"Please add a parking slot for a visitor.",Toast.LENGTH_SHORT).show();
                    }

                }

                break;
            case R.id.bt_cancel:
                //listner.onSelected(allotinvites,"",false);
                dismiss();
                break;
        }
    }


    private void filter(String text) {
        List<Invited> temp = new ArrayList<>();
        for(Invited d: SetupMeetingActivity.invitedArrayList){
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text) || d.getEmail().toLowerCase().contains(text) || d.getMobile().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        if (temp.isEmpty()){
            allotParkingAdapter.updateList(temp);
        }
        else {
            //update recyclerview
            allotParkingAdapter.updateList(temp);
        }
    }

    private void checkAllCheckboxes(boolean status) {
        for (int i = 0; i < allotParkingAdapter.getItemCount(); i++) {
            View view = recyclerview.getChildAt(i);
            if (view != null) {
                CheckBox checkBox = view.findViewById(R.id.checkbox);
                checkBox.setChecked(status);
            }
        }
    }

    public interface ItemSelected {
        void onSelected( JSONArray allot,String lastname,boolean allowCheck);
    }

}