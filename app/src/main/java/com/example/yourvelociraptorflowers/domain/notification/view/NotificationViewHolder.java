package com.example.yourvelociraptorflowers.domain.notification.view;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yourvelociraptorflowers.databinding.NotificationItemBinding;
import com.example.yourvelociraptorflowers.model.notification.Yvedomlenie;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    private final NotificationItemBinding binding;

    public NotificationViewHolder(NotificationItemBinding binding, NotificationsAdapter.OnDeleteClickListener onDeleteClickListener) {
        super(binding.getRoot());
        this.binding = binding;

        binding.delitButton.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onDeleteClickListener.onDeleteClick(position);

            }
        });
    }

    public void bind(Yvedomlenie item) {
        Log.wtf("NotificationViewHolder", "bind: " + item.getTitle() + " " + item.getMessage() + " " + item.getTimestamp());
        binding.title.setText(item.getTitle());
        binding.massage.setText(item.getMessage());
        binding.time.setText(item.getTimestamp());

    }
}
