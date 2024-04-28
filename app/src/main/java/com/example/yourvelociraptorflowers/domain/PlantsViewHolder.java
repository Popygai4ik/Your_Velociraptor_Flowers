package com.example.yourvelociraptorflowers.domain;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ItemVseBinding;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.ui.OpisanieActivity;

public class PlantsViewHolder extends ViewHolder {

    private ItemVseBinding binding;


    public PlantsViewHolder(ItemVseBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Plants item) {
        binding.name.setText(item.getOpisanie());
        binding.opisanie.setText(item.getName());
        // Load image
        Glide.with(itemView)
                .load(item.getResinok())
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.resinok);
        // Set Listener
        binding.moreButton.setOnClickListener(v -> {
           Intent intent = new Intent(itemView.getContext(), OpisanieActivity.class);
           intent.putExtra("name", item.getName());
           intent.putExtra("opisanie", item.getOpisanie());
            intent.putExtra("temp", item.getOpisanie2());
            intent.putExtra("vlag", item.getOpisanie3());
            intent.putExtra("ysl", item.getOpisanie4());
            intent.putExtra("resinok", item.getResinok());
            intent.putExtra("resinok2", item.getResinok2());
            intent.putExtra("resinok3", item.getResinok3());
            intent.putExtra("resinok4", item.getResinok4());
            intent.putExtra("opisanie5", item.getOpisanie5());
           itemView.getContext().startActivity(intent);
        });

//        binding.addButton.setOnClickListener(v -> {});
    }
}
