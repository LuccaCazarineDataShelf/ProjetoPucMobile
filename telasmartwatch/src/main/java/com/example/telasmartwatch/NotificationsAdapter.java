package com.example.telasmartwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.Holder> {
    private final List<NotificationItem> data;

    public NotificationsAdapter(List<NotificationItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watch_notification, parent, false);
        return new Holder(v);
    }

    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NotificationItem n = data.get(position);
        TextView title = holder.itemView.findViewById(R.id.txtTitle);
        TextView body  = holder.itemView.findViewById(R.id.txtBody);
        title.setText(n.title);
        body.setText(n.body);
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtBody;
        Holder(@NonNull View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtBody  = v.findViewById(R.id.txtBody);
        }
    }
}
