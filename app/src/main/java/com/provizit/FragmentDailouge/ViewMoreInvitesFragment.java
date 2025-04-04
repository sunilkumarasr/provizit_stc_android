package com.provizit.FragmentDailouge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.Activities.MeetingDescriptionNewActivity;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.AdapterAndModel.AddtoexistingcontactsAdapter;
import com.provizit.Conversions;
import com.provizit.CustomItemListener;
import com.provizit.R;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomItemthreeListener;
import com.provizit.Utilities.Invited;

import java.util.ArrayList;

public class ViewMoreInvitesFragment extends BottomSheetDialogFragment {
    private static final String TAG = "ViewMoreInvitesFragment";

    private FragmentActivity myContext;

    String meetingslist = "";

    RecyclerView recyclerViewinvites;
    InvitesAdapter1 invitesAdapter;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetSearchDialogTheme);
    }

    ItemSelected listner;

    public void onItemsSelectedListner(ItemSelected listner) {
        this.listner = listner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        meetings = getArguments().getString("meetingslist");
        View view = inflater.inflate(R.layout.fragment_view_more_invites, container, false);


        recyclerViewinvites = view.findViewById(R.id.recyclerViewinvites);
        recyclerViewinvites.setHasFixedSize(true);
        recyclerViewinvites.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        invitesAdapter = new InvitesAdapter1(getActivity(), MeetingDescriptionNewActivity.meetings.getInvites(), new CustomItemthreeListener() {
            @Override
            public void onItemClick(View v, String id, String name, String email) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                listner.onSelcetd(id, name, email);
            }
        });
        recyclerViewinvites.setAdapter(invitesAdapter);

        return view;
    }

    public class InvitesAdapter1 extends RecyclerView.Adapter<InvitesAdapter1.MyviewHolder> {
        CustomItemthreeListener customItemListener;
        Context context;
        ArrayList<Invited> invited;
        int SelectedIndex;

        public InvitesAdapter1(Context context, ArrayList<Invited> vehicles, CustomItemthreeListener customItemListener) {
            this.context = context;
            this.invited = vehicles;
            this.customItemListener = customItemListener;
        }

        @Override
        public InvitesAdapter1.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.invitess_items_list, parent, false);
            return new InvitesAdapter1.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final InvitesAdapter1.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {
            System.out.println("Size@" + invited.size());
            System.out.println("Size1@" + invited.get(position).getName());

            // Name
            String name = invited.get(position).getName();
            if (name == null || name.equals("")) {
                holder.txt_name.setText("   -   ");
            } else {
                holder.txt_name.setText(name);
            }

            // Email
            String email = invited.get(position).getEmail();
            if (email == null || email.equals("")) {
                holder.txt_email.setText("   -   ");
            } else {
                holder.txt_email.setText(email);
            }

            // Mobile
            String mobile = invited.get(position).getMobile();
            if (mobile == null || mobile.equals("")) {
                holder.txt_phone.setText("   -   ");
            } else {
                holder.txt_phone.setText(mobile);
            }


            // Slots
            if (invited.get(position).getParking_status()) {
                holder.txt_p_slot.setText(invited.get(position).getPcat_name() + " - " +
                        invited.get(position).getLot_name() + " - " +
                        invited.get(position).getSlot_name());
            } else if (!invited.get(position).getPcat_name().equalsIgnoreCase("") &&
                    !invited.get(position).getLot_name().equalsIgnoreCase("")) {
                holder.txt_p_slot.setText(invited.get(position).getPcat_name() + " - " +
                        invited.get(position).getLot_name() + " - " +
                        invited.get(position).getSlot_name());
            } else {
                holder.txt_p_slot.setText("   -   ");
            }


            holder.card_view.setOnClickListener(v -> {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                customItemListener.onItemClick(v, invited.get(position).getId(), invited.get(position).getName(), invited.get(position).getEmail());
            });


        }

        @Override
        public int getItemCount() {
            return invited.size();
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            CardView card_view;
            TextView txt_name, txt_email, txt_phone, txt_p_slot;

            public MyviewHolder(View view) {
                super(view);
                card_view = view.findViewById(R.id.card_view);
                txt_name = view.findViewById(R.id.txt_name);
                txt_email = view.findViewById(R.id.txt_email);
                txt_phone = view.findViewById(R.id.txt_phone);
                txt_p_slot = view.findViewById(R.id.txt_p_slot);
            }
        }

    }


    public interface ItemSelected {
        void onSelcetd(String id, String name, String email);
    }


}