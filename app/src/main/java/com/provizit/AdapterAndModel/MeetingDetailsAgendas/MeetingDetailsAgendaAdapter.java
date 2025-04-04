package com.provizit.AdapterAndModel.MeetingDetailsAgendas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.R;
import com.provizit.Utilities.Agenda;
import java.util.ArrayList;

public class MeetingDetailsAgendaAdapter extends RecyclerView.Adapter<MeetingDetailsAgendaAdapter.MyviewHolder> {
    Context context;
    ArrayList<Agenda> agenda;

    public MeetingDetailsAgendaAdapter(Context context, ArrayList<Agenda> agenda) {
        this.context = context;
        this.agenda = agenda;
    }

    @NonNull
    @Override
    public MeetingDetailsAgendaAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.agenda_items_list, parent, false);
        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingDetailsAgendaAdapter.MyviewHolder holder, final int position) {
        holder.agenda.setText((position + 1) + "." + agenda.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return agenda.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView agenda;

        public MyviewHolder(View view) {
            super(view);
            agenda = view.findViewById(R.id.txt_agneda);
        }
    }

}
