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

public class MenuFragment extends Fragment {

    public static final String FLAG_BLOCK_PLAY_KEY = "flag";
    boolean guestModeFlag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.guestModeFlag =false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState==null)
            savedInstanceState=this.getArguments();

        this.guestModeFlag = savedInstanceState.getBoolean(FLAG_BLOCK_PLAY_KEY);

        if (this.guestModeFlag)
            blockPlayMode();
    }



    public void blockPlayMode() {
        ((TextView)this.requireView().findViewById(R.id.playTextView)).setText("TUTORIAL");

        this.requireView().findViewById(R.id.btnPlay).setOnClickListener(view -> getParentFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.menuView, new TutorialFragment())
                .setCustomAnimations(R.animator.slide_in_animator,R.animator.slide_out_animator)
                .commit());


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLAG_BLOCK_PLAY_KEY,this.guestModeFlag);
    }
}