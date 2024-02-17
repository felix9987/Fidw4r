package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.dib.sms2223_28.R;

public class ResultFragment extends Fragment {
    public static final String EARNED_TROPHIES_KEY = "numbers of trophies earned";
    public static final String INITIAL_TROPHIES_KEY = "numbers of trophies before the game";
    public static final String GAME_RESULT_KEY = "player has ... the game";
    private int earnedTrophies;
    private int initialTrophies;
    private float gameResult;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            savedInstanceState=this.getArguments();
            earnedTrophies=savedInstanceState.getInt(EARNED_TROPHIES_KEY);
            initialTrophies=savedInstanceState.getInt(INITIAL_TROPHIES_KEY);
            gameResult=savedInstanceState.getFloat(GAME_RESULT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView resultTextView= view.findViewById(R.id.gameResult);
        TextView trophiesTextView= view.findViewById(R.id.trophiesResultFragment);

        String stringPositive=String.format("%d + %d", initialTrophies, earnedTrophies);
        String stringNegative=String.format("%d %d", initialTrophies, earnedTrophies);

        if (this.gameResult==1) {
            trophiesTextView.setText(stringPositive);
        }
        else if (this.gameResult==0) {
            resultTextView.setText(getResources().getString(R.string.hai_perso));
            trophiesTextView.setText(stringNegative);
            view.findViewById(R.id.graphicEndgame).setBackgroundResource(R.drawable.defeat);
        }
        else {
            resultTextView.setText(getResources().getString(R.string.pareggio));
            view.findViewById(R.id.graphicEndgame).setBackgroundResource(R.drawable.tie);

            if (earnedTrophies>=0)
                trophiesTextView.setText(stringPositive);
            else
                trophiesTextView.setText(stringNegative);
        }
    }

}