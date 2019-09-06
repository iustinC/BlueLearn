package com.example.iustin.bluelearn.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.domain.LeaderboardEntry;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {


    private ListView leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        setTitle("Leaderboard for " + Utils.selectedCategory);

        leaderboard = findViewById(R.id.leaderboard);
        int index = 1;
        List<String> values = new ArrayList<>();
        for (LeaderboardEntry leaderboardEntry : Utils.leaderboardEntries) {
            if (!"0".equals(leaderboardEntry.score)) {
                values.add("" + index + "." + leaderboardEntry.username + " has score " + leaderboardEntry.score);
                index++;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        leaderboard.setAdapter(adapter);
    }
}
