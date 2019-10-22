package me.shubhamthorat.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChartAdapter extends FirestoreRecyclerAdapter<Chart, ChartAdapter.ChartHolder> {

    public ChartAdapter(@NonNull FirestoreRecyclerOptions<Chart> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChartHolder holder, int position, @NonNull Chart model) {
        holder.vName.setText(model.getvName());
        holder.vDate.setText(model.getvDate());

    }

    @NonNull
    @Override
    public ChartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaclist,parent,false);
        return new ChartHolder(v);
    }

    public class ChartHolder extends RecyclerView.ViewHolder{
        TextView vName,vDate;
        public ChartHolder(@NonNull View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.vaccname);
            vDate = itemView.findViewById(R.id.vaccdate);

        }
    }
}
