package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.dib.sms2223_28.modello.Constants;

import it.uniba.dib.sms2223_28.R;

public class ViewHeroChoice extends Fragment {

    public static final String TEXT_KEY="text key";
    public static final String TUTORIAL_BUTTON_KEY = "activate the tutorial";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hero_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        TextView textView= view.findViewById(R.id.heroChoiceTextView);
        View tutorialButton=view.findViewById(R.id.btnTutorial);

        try {
            savedInstanceState = this.getArguments();
            textView.setText(savedInstanceState.getString(TEXT_KEY));

            if (!savedInstanceState.getBoolean(TUTORIAL_BUTTON_KEY)) {

                tutorialButton.setVisibility(View.INVISIBLE);
                view.findViewById(R.id.tutorialTextView).setVisibility(View.INVISIBLE);
            }

        } catch (NullPointerException e) {

            getParentFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        }

    }

    public void setOnClick(boolean flag) {

        this.getView().findViewById(R.id.romeHero).setClickable(flag);

        this.getView().findViewById(R.id.persiaHero).setClickable(flag);

        this.getView().findViewById(R.id.spartaHero).setClickable(flag);

        this.getView().findViewById(R.id.egyptHero).setClickable(flag);

    }

    public void setOnClick(boolean flag, int hero) {

        View view;

        switch (hero) {

            case Constants.EGYPT:
                view = requireView().findViewById(R.id.egyptHero);
                break;
            case Constants.SPARTA:
                view = requireView().findViewById(R.id.spartaHero);
                break;
            case Constants.PERSIA:
                view = requireView().findViewById(R.id.persiaHero);
                break;
            case Constants.ROME:
                view = requireView().findViewById(R.id.romeHero);
                break;

            default:
                throw new IllegalArgumentException();

        }

        view.setClickable(flag);
        view.setAlpha(0.4f);

    }


    public void setVisibility(int visibility) {
        requireView().findViewById(R.id.heroChoiceFrame).setVisibility(visibility);

    }
}