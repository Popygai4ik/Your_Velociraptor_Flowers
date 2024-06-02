package com.example.yourvelociraptorflowers.ui.plants.illumination;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class IlluminationViewModel extends ViewModel {
    private List<Float> luxValues = new ArrayList<>();
    private int progress;
    private boolean isMeasuring;

    public List<Float> getLuxValues() {
        return luxValues;
    }

    public void setLuxValues(List<Float> luxValues) {
        this.luxValues = luxValues;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isMeasuring() {
        return isMeasuring;
    }

    public void setMeasuring(boolean measuring) {
        isMeasuring = measuring;
    }
}
