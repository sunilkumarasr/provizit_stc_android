package com.provizit.AdapterAndModel.PDFList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.Config.OnListEmptyListener;
import com.provizit.R;
import com.provizit.Utilities.Pdfs;
import java.util.ArrayList;

public class PDFListSetUpMeetingAdapter extends RecyclerView.Adapter<PDFListSetUpMeetingAdapter.MyviewHolder> {

    Context context;
    ArrayList<Pdfs> pdfs;
    OnListEmptyListener listEmptyListener;

    public PDFListSetUpMeetingAdapter(Context context, ArrayList<Pdfs> pdfs, OnListEmptyListener listEmptyListener) {
        this.context = context;
        this.pdfs = pdfs;
        this.listEmptyListener = listEmptyListener;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.pdf_setupmeeting_items_list, parent, false);
        return new MyviewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtPdf.setText(pdfs.get(position).getName());

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete PDF")
                        .setMessage("Are you sure you want to delete this PDF?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Remove the item
                            pdfs.remove(position);

                            // Notify the adapter
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, pdfs.size());



                            // Notify if the list is empty
                            if (listEmptyListener != null) {
                                listEmptyListener.onListEmpty();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfs.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView txtPdf;
        ImageView imgDelete;

        public MyviewHolder(View view) {
            super(view);
            txtPdf = view.findViewById(R.id.txtPdf);
            imgDelete = view.findViewById(R.id.imgDelete);
        }
    }
}
