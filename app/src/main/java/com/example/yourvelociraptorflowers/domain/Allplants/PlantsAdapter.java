package com.example.yourvelociraptorflowers.domain.Allplants;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.yourvelociraptorflowers.databinding.ItemAllBinding;
import com.example.yourvelociraptorflowers.model.plant.Plants;

import java.util.ArrayList;
import java.util.List;

public class PlantsAdapter extends Adapter<PlantsViewHolder> {

    private List<Plants> plants = new ArrayList<>();



    @NonNull
    @Override
    public PlantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAllBinding binding = ItemAllBinding.inflate(inflater, parent, false);
        return new PlantsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsViewHolder holder, int position) {
        Plants plant = plants.get(position);
        holder.bind(plant);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setItems(List<Plants> plants) {
        int itemCount = getItemCount();
        this.plants = new ArrayList<>(plants);
        notifyItemRangeChanged(0, Math.max(itemCount, getItemCount()));
    }



}
