package com.provizit.AdapterAndModel.PDFList;

import static com.provizit.Activities.SetupMeetingActivity.sharedPreferences1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.Config.CustomItemPDF;
import com.provizit.Config.OnListEmptyListener;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.Pdfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PDFListDetailPageAdapter extends RecyclerView.Adapter<PDFListDetailPageAdapter.MyviewHolder> {

    Context context;
    ArrayList<Pdfs> pdfs;
    private SharedPreferences sharedPreferences1;
    OnListEmptyListener listEmptyListener;
    public PDFListDetailPageAdapter(Context context, ArrayList<Pdfs> pdfs,  SharedPreferences sharedPreferences1, OnListEmptyListener listEmptyListener) {
        this.context = context;
        this.pdfs = pdfs;
        this.sharedPreferences1 = sharedPreferences1;
        this.listEmptyListener = listEmptyListener;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.pdf_details_items_list, parent, false);
        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtPdf.setText(pdfs.get(position).getName());

        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pdf_url = DataManger.IMAGE_URL + "/uploads/"
                        + sharedPreferences1.getString("company_id", null)
                        + "/pdfs/" + pdfs.get(position).getValue();
                Log.e("pdf_url_",pdf_url);
                downloadAndOpenPdf(context,pdf_url);
            }
        });
        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPDF(pdfs.get(position).getName(),pdfs.get(position).getValue());
            }
        });
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

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView txtPdf;
        ImageView imgView,imgDownload,imgDelete;

        public MyviewHolder(View view) {
            super(view);
            txtPdf = view.findViewById(R.id.txtPdf);
            imgView = view.findViewById(R.id.imgView);
            imgDownload = view.findViewById(R.id.imgDownload);
            imgDelete = view.findViewById(R.id.imgDelete);
        }
    }

    private void downloadAndOpenPdf(Context context, String pdfUrl) {
        new Thread(() -> {
            try {
                // Download the PDF file
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(pdfUrl).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    // Save the file to the cache directory
                    File pdfFile = new File(context.getCacheDir(), "downloaded_file.pdf");
                    try (InputStream inputStream = response.body().byteStream();
                         FileOutputStream outputStream = new FileOutputStream(pdfFile)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    // Open the PDF file
                    Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", pdfFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                } else {
                    showToast(context, "Failed to download the PDF!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Error downloading the PDF!");
            }
        }).start();
    }

    private void showToast(Context context, String message) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }



    private void downloadPDF(String name, String value) {
        String pdfUrl = DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/pdfs/" + value;
        startPdfDownload(pdfUrl, name);
    }

    private void startPdfDownload(String pdfUrl, String fileName) {
        Toast.makeText(context, fileName+" downloading", Toast.LENGTH_SHORT).show();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
        request.setTitle("Downloading PDF");
        request.setDescription("Downloading meeting PDF");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

}
