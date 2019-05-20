package com.example.complaintclientuser.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.complaintclientuser.ComplaintActivity;
import com.example.complaintclientuser.EditComplaintActivity;
import com.example.complaintclientuser.R;
import com.example.complaintclientuser.models.Complaint;

import java.text.SimpleDateFormat;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {

    private List<Complaint> complaints;

    public ComplaintAdapter(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public ComplaintAdapter.ComplaintViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_complaint, viewGroup, false);

        final ComplaintViewHolder viewHolder = new ComplaintViewHolder(view);
        viewHolder.card_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String date = new SimpleDateFormat("EEE dd-MMM-yy").format(complaints.get(viewHolder.getAdapterPosition()).getCreated_at());

                Intent intent = new Intent(viewGroup.getContext(), ComplaintActivity.class);
                intent.putExtra("topic", complaints.get(viewHolder.getAdapterPosition()).getTopic());
                intent.putExtra("body", complaints.get(viewHolder.getAdapterPosition()).getBody());
                intent.putExtra("created_at", date);
                intent.putExtra("instance", complaints.get(viewHolder.getAdapterPosition()).getInstance().getName());
                intent.putExtra("category", complaints.get(viewHolder.getAdapterPosition()).getCategory());
                intent.putExtra("name", complaints.get(viewHolder.getAdapterPosition()).getUser().getName());
                intent.putExtra("id", complaints.get(viewHolder.getAdapterPosition()).getId());

                viewGroup.getContext().startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintAdapter.ComplaintViewHolder complaintViewHolder, int i) {
        String date = new SimpleDateFormat("EEE dd-MMM-yy").format(complaints.get(i).getCreated_at());

        complaintViewHolder.topic.setText(complaints.get(i).getTopic());
        complaintViewHolder.body.setText(complaints.get(i).getBody());
        complaintViewHolder.created_at.setText(date);
        complaintViewHolder.name.setText(complaints.get(i).getUser().getName());
    }

    @Override
    public int getItemCount() {
        return (complaints != null) ? complaints.size() : 0;
    }

    public class ComplaintViewHolder extends RecyclerView.ViewHolder {

        private TextView topic, body, created_at, name;
        private CardView card_complaint;

        public ComplaintViewHolder(View view) {
            super(view);
            topic = view.findViewById(R.id.text_complaintTopic);
            body = view.findViewById(R.id.text_complaintBody);
            created_at = view.findViewById(R.id.text_complaintCreatedAt);
            name = view.findViewById(R.id.text_userName);
            card_complaint = view.findViewById(R.id.card_complaint);
        }
    }
}
