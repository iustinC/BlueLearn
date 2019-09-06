package com.example.iustin.bluelearn.domain;

public class LeaderboardEntry {

    public String score;
    public String category;
    public String username;

    public LeaderboardEntry(String score, String category, String username) {
        this.score = score;
        this.category = category;
        this.username = username;
    }

    public LeaderboardEntry() {
    }
}
