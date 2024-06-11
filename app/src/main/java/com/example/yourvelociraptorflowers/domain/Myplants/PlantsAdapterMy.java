package com.example.yourvelociraptorflowers.domain.Myplants;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.yourvelociraptorflowers.databinding.ItemMyBinding;
import com.example.yourvelociraptorflowers.model.plant.Plants;

import java.util.ArrayList;
import java.util.List;

public class PlantsAdapterMy extends Adapter<PlantsViewHolderMy> {
    private List<Plants> plants = new ArrayList<>();
    @NonNull
    @Override
    public PlantsViewHolderMy onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyBinding binding = ItemMyBinding.inflate(inflater, parent, false);
        return new PlantsViewHolderMy(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsViewHolderMy holder, int position) {
        holder.bind(plants.get(position));
    }
    @Override
    public int getItemCount() {
        return plants.size();
    }
    public void setItems(List<Plants> plants) {
        int itemCount = getItemCount();
        this.plants = new ArrayList<>(plants);
        notifyDataSetChanged();
        notifyItemRangeChanged(0, Math.max(itemCount, getItemCount()));
    }
}
