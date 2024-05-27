package com.example.yourvelociraptorflowers.domain.Moiplants;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.yourvelociraptorflowers.databinding.ItemMoiBinding;
import com.example.yourvelociraptorflowers.model.Plants;

import java.util.ArrayList;
import java.util.List;

public class PlantsAdapterMoi extends Adapter<PlantsViewHolderMoi> {
    private List<Plants> plants = new ArrayList<>();
    @NonNull
    @Override
    public PlantsViewHolderMoi onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMoiBinding binding = ItemMoiBinding.inflate(inflater, parent, false);
        return new PlantsViewHolderMoi(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsViewHolderMoi holder, int position) {
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
