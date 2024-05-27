package com.example.yourvelociraptorflowers.data;

import android.util.Log;

import com.example.yourvelociraptorflowers.model.Plants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class plants_generate {

    private ListenerRegistration listenerRegistration;

    public interface OnPlantsUpdatedListener {
        void onPlantsUpdated(List<Plants> plants);
        void onFailure(Exception e);
    }

    public void startListeningForPlantsUpdates(OnPlantsUpdatedListener listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        listenerRegistration = firestore.collection("plants")
                .addSnapshotListener((value, error) -> {
                    List<Plants> plants = new ArrayList<>();
                    if (value == null) {
                        if (error != null) {
                            Log.wtf("LOGG", error.getMessage());
                            listener.onFailure(error);
                            return;
                        } else {
                            Log.wtf("LOGG", "No document found");
                        }
                    } else {
                        try {
                            if (value != null) {
                                for (DocumentSnapshot plant : value.getDocuments()) {
                                    Plants user = plant.toObject(Plants.class);
                                    if (user != null) {
                                        plants.add(user);
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            Log.wtf("LOGG", e.getMessage());
                            listener.onFailure(e);
                        }
                    }

                    listener.onPlantsUpdated(plants);
                });
    }

    public void stopListeningForUsersUpdates() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
