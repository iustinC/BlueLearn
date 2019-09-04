package com.example.iustin.bluelearn.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.adapters.CategoryAdapter;
import com.example.iustin.bluelearn.model.SpaceDecoration;

public class CategoryActivity
        extends AppCompatActivity {


    private RecyclerView recycler_category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setTitle("Categories");

        recycler_category = (RecyclerView) findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(this, 2));
        CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, Utils.categoriesNames);
        int spaceInPixel = 4;
        recycler_category.addItemDecoration(new SpaceDecoration(spaceInPixel));
        recycler_category.setAdapter(adapter);

    }
}
