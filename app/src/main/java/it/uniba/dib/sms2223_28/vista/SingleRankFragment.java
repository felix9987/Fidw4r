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

public class SingleRankFragment extends Fragment {
    public final static String POSITION_KEY ="position";
    public final static String USERNAME_KEY ="username";
    public final static String TROPHIES_KEY ="trophy";
    private String position;
    private String username;
    private String trophies;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getArguments();
        if (savedInstanceState!=null) {
            position = savedInstanceState.getString(POSITION_KEY);
            username = savedInstanceState.getString(USERNAME_KEY);
            trophies = savedInstanceState.getString(TROPHIES_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_rank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView positionView = view.findViewById(R.id.position);
        TextView playerNameView = view.findViewById(R.id.player);
        TextView trophiesView = view.findViewById(R.id.trophiesResultFragment);

        if (position!=null) {

            positionView.setText(position);
            playerNameView.setText(username);
            trophiesView.setText(trophies);
        }
    }
}