package it.uniba.dib.sms2223_28.vista;

import static it.uniba.dib.sms2223_28.modello.Constants.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MenuGameActivity;

public class AchievementsFragment extends Fragment {

    private View generalView;
    DatabaseReference databaseReference;
    private int userTrophies;
    private int romanWins,persisWins,egyptWins,spartanWins;

    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            if (MenuGameActivity.playerUser!=null) {
                userTrophies = MenuGameActivity.playerUser.getMaxTrophies();
                romanWins = MenuGameActivity.playerUser.getRomanWins();
                persisWins = MenuGameActivity.playerUser.getPersisWins();
                egyptWins = MenuGameActivity.playerUser.getEgyptWins();
                spartanWins = MenuGameActivity.playerUser.getSpartanWins();
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.fragment_achievements, container, false);
          generalView = view;
          return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (MenuGameActivity.playerUser==null) return;

        databaseReference = FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        manageVictories(R.id.textRedeem1, R.id.achievementImage1, ROME);
        manageVictories(R.id.textRedeem2, R.id.achievementImage2, PERSIA);
        manageVictories(R.id.textRedeem3, R.id.achievementImage3,SPARTA);
        manageVictories(R.id.textRedeem4, R.id.achievementImage4,EGYPT);
        manageAchievement(TROPHIES_TO_UNLOCK_ROMAN_BACK, R.id.textRedeem5,R.id.achievementImage5,R.id.backAchievement5);
        manageAchievement(TROPHIES_TO_UNLOCK_PERSIA_BACK, R.id.textRedeem6,R.id.achievementImage6,R.id.backAchievement6);
        manageAchievement(TROPHIES_TO_UNLOCK_SPARTA_BACK,R.id.textRedeem7, R.id.achievementImage7,R.id.backAchievement7);
        manageAchievement(TROPHIES_TO_UNLOCK_EGYPT_BACK,R.id.textRedeem8, R.id.achievementImage8,R.id.backAchievement8);

    }

    private void manageAchievement(int requiredTrophy, int textViewId, int imageViewId, int backViewId){

        TextView textRedeem = generalView.findViewById(textViewId);
        ImageView redeemImage = generalView.findViewById(imageViewId);

        if (userTrophies >= requiredTrophy) {

            generalView.setAlpha(1f);
            textRedeem.setAlpha(1f);
            redeemImage.setAlpha(1f);
            requireView().findViewById(backViewId).setAlpha(1f);
            redeemImage.setImageAlpha(255);

        }

    }


    private void manageVictories(int textViewId,int imageViewId, int hero){

        boolean flag=false;
        TextView textRedeem = generalView.findViewById(textViewId);
        ImageView redeemImage = generalView.findViewById(imageViewId);

        switch (hero){

            case ROME:
                if(romanWins >= VICTORIES_COUNT){
                    flag=true;
                }
                break;
            case PERSIA:
                if(persisWins >= VICTORIES_COUNT){
                    flag=true;
                }
                break;
            case SPARTA:
                if(spartanWins >= VICTORIES_COUNT){
                    flag=true;
                }
                break;
            case EGYPT:
                if(egyptWins >= VICTORIES_COUNT){
                    flag=true;
                }
                break;
        }

        if(flag){
            generalView.setAlpha(1f);
            textRedeem.setAlpha(1f);
            redeemImage.setAlpha(1f);
            redeemImage.setImageAlpha(255);
        }

    }

}