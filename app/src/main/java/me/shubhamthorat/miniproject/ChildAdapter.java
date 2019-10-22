package me.shubhamthorat.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChildAdapter extends FirestoreRecyclerAdapter<Child, ChildAdapter.ChildHolder> {

    public OnItemClickListener listener;

    public ChildAdapter(@NonNull FirestoreRecyclerOptions<Child> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChildHolder holder, int position, @NonNull Child model) {
    holder.ChildName.setText(model.getName());
    holder.DOB.setText(model.getDOB());
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectlist,parent,false);
        return (new ChildHolder(v));
    }

    class ChildHolder extends RecyclerView.ViewHolder{
        TextView ChildName, DOB;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            ChildName =itemView.findViewById(R.id.cname);
            DOB =itemView.findViewById(R.id.cdob);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                }
            });
         }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
