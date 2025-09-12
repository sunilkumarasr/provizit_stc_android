package com.provizit.AdapterAndModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.R;
import java.util.List;

public class SubContractorDetailsAdapter extends RecyclerView.Adapter<SubContractorDetailsAdapter.ViewHolder> {
    private List<SubContractorData> subContractorList;

    public SubContractorDetailsAdapter(List<SubContractorData> list) {
        this.subContractorList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_contractor_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubContractorData subContractorData = subContractorList.get(position);
        holder.txtName.setText(subContractorData.getName());
        holder.txtCompanyName.setText(subContractorData.getCompanyName());
        holder.txtMobile.setText(subContractorData.getMobileData().internationalNumber);
        holder.txtIDType.setText(subContractorData.getId_type());
        holder.txtNationality.setText(subContractorData.getNationality());
        holder.txtIDNumber.setText(subContractorData.getId_number());

    }

    @Override
    public int getItemCount() {
        return subContractorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCompanyName, txtMobile, txtIDType, txtNationality, txtIDNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtCompanyName = itemView.findViewById(R.id.txtCompanyName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtIDType = itemView.findViewById(R.id.txtIDType);
            txtNationality = itemView.findViewById(R.id.txtNationality);
            txtIDNumber = itemView.findViewById(R.id.txtIDNumber);
        }
    }
}

