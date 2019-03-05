package com.example.iustin.bluelearn.activities;

import android.os.Bundle;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.adapters.MenuAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.menuOptions) RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //mAdapter = new MenuAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        this.getSupportActionBar().hide();
    }
}
