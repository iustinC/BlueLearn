package com.example.iustin.bluelearn.repository;

import androidx.annotation.NonNull;

import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.activities.ResultActivity;
import com.example.iustin.bluelearn.domain.LeaderboardEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderboardRepository {

    private static final String TAG = LeaderboardRepository.class.getName();

    private DatabaseReference database;

    private ResultActivity resultActivity;

    {
        database = FirebaseDatabase.getInstance().getReference().child("/leaderboard");
    }

    public LeaderboardRepository(ResultActivity resultActivity) {
        this.resultActivity = resultActivity;
    }

    public void addNewScore(String score, String user){
        LeaderboardEntry entry = new LeaderboardEntry(score, Utils.selectedCategory, user);

        String key = database.child(Utils.selectedCategory).push().getKey();

        database.child(Utils.selectedCategory).child(key).setValue(entry);
    }


    public void loadLeaderboard() {
        Utils.leaderboardEntries = new ArrayList<>();
        Query queryRef = database.child(Utils.selectedCategory).orderByChild("score").limitToLast(100);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    LeaderboardEntry entry = dataSnapshot1.getValue(LeaderboardEntry.class);
                    Utils.leaderboardEntries.add(entry);

                }
                resultActivity.goToLeaderboard();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
