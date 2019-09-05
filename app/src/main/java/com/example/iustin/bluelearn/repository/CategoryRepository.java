package com.example.iustin.bluelearn.repository;

import androidx.annotation.NonNull;

import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.activities.MenuActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryRepository {

    private static final String TAG = CategoryRepository.class.getName();

    private DatabaseReference database;

    private MenuActivity menuAc;

    private List<String> categoriesNames = new ArrayList<>();

    {
        database = FirebaseDatabase.getInstance().getReference().child("/categories");
    }

    public CategoryRepository(MenuActivity menu) {
        this.menuAc = menu;
    }

    public void loadCategoriesName() {
        Utils.categoriesNames = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> value = (Map<String, Object>)dataSnapshot.getValue();
                categoriesNames = new ArrayList<>(value.keySet());
                Utils.categoriesNames = categoriesNames;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<String> getCategoriesNames() {
        return categoriesNames;
    }

    public void setCategoriesNames(List<String> categoriesNames) {
        this.categoriesNames = categoriesNames;
    }


}
