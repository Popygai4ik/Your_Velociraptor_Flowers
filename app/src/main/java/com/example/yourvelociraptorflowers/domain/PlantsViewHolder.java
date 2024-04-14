package com.example.yourvelociraptorflowers.domain;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ItemVseBinding;
import com.example.yourvelociraptorflowers.model.Plants;


public class PlantsViewHolder extends ViewHolder {

    private ItemVseBinding binding;

    public PlantsViewHolder(ItemVseBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Plants item) {
        // Set text data
        binding.name.setText(item.getOpisanie());
        binding.opisanie.setText(item.getName());
        // Load image
        Glide.with(itemView)
                .load(item.getResinok())
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.resinok);
//        // Set Listener
//        binding.getRoot().setOnClickListener(v -> userItemClickListener.onItemClick(item));
    }
}
