package com.example.wordly_by_yuvalmiz;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FbModuleGame {

    ArrayList<String> userguesses;


    private GameActivity gameActivity;
    public FbModuleGame(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        userguesses = new ArrayList<>();

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference userDataRef = firebaseDatabase.getReference(userId + "/user_data");

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userguesses.clear();

                for (DataSnapshot guessSnapshot : snapshot.getChildren()) {
                    String guess = guessSnapshot.getValue(String.class);
                    if (guess != null) {
                        userguesses.add(guess);
                    }
                }

                gameActivity.onGuessesLoaded(userguesses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read user_data", error.toException());
            }
        });
    }


    public void setUserGuess(String guess) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // FIXED: now saving to the same path we read from
        ref.child(uid).child("user_data").push().setValue(guess);
    }

    public boolean CheckIfSaved() {
        if (userguesses!=null)
            return true;
        return false;
    }
    public void WinState(){
        userguesses.clear();
    }

    public ArrayList<String> ReturnSavedGuesses() {
        return userguesses;
    }
}

