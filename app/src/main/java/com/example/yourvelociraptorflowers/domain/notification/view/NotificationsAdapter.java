package com.example.yourvelociraptorflowers.domain.notification.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvelociraptorflowers.databinding.NotificationItemBinding;
import com.example.yourvelociraptorflowers.model.Yvedomlenie;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<Yvedomlenie> yvedomlenieList = new ArrayList<>();
    private final OnDeleteClickListener onDeleteClickListener;

    public NotificationsAdapter(List<Yvedomlenie> yvedomlenieList, OnDeleteClickListener onDeleteClickListener) {
        this.yvedomlenieList = yvedomlenieList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NotificationItemBinding binding = NotificationItemBinding.inflate(inflater, parent, false);
        return new NotificationViewHolder(binding, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(yvedomlenieList.get(position));
    }

    @Override
    public int getItemCount() {
        return yvedomlenieList.size();
    }

    public void setItems(List<Yvedomlenie> yvedomlenieList) {
        this.yvedomlenieList = new ArrayList<>(yvedomlenieList);
        notifyDataSetChanged();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
