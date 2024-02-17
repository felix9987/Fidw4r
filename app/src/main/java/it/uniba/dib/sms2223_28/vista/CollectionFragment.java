package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.CollectionActivity;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.GenericDeck;

public class CollectionFragment extends Fragment {
    private int hero;
    private GenericDeck deck;
    private boolean flagDeck;
    private String key;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        savedInstanceState=this.getArguments();
        if (savedInstanceState==null)
            throw new IllegalStateException();

        this.hero= savedInstanceState.getInt(Constants.HERO_KEY);
        this.deck= (GenericDeck) savedInstanceState.getSerializable(CollectionActivity.DECK_KEY);
        this.key= savedInstanceState.getString(Constants.USER_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!setImage()) throw new RuntimeException();

        this.deckFragmentCall();
        flagDeck=true;
        this.requireView().findViewById(R.id.collection_button).setOnClickListener(this::collectionPressed);
        this.requireView().findViewById(R.id.back_collection_button).setOnClickListener(this::backPressed);

    }

    private void deckFragmentCall() {
        DeckFragment deckFragment = new DeckFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(DeckFragment.DECK_TO_SHOW_KEY,this.deck);
        deckFragment.setArguments(bundle);

        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.animator.slide_in_animator, R.animator.slide_out_animator);
        transaction.replace(R.id.layout_collection, deckFragment);
        transaction.commit();

        Handler handler=new Handler();
        handler.postDelayed(deckFragment::uploadLayout, 100);
        handler.postDelayed(deckFragment::setOnClick, 1500);
    }

    private boolean setImage(){
        switch (this.hero){
            case Constants.EGYPT:
                this.requireView().findViewById(R.id.collection_choice_button_icon).setBackgroundResource(R.drawable.egypt_champ);
                break;

            case Constants.SPARTA:
                this.requireView().findViewById(R.id.collection_choice_button_icon).setBackgroundResource(R.drawable.sparta_champ);
                break;

            case Constants.PERSIA:
                this.requireView().findViewById(R.id.collection_choice_button_icon).setBackgroundResource(R.drawable.persia_champ);
                break;

            case Constants.ROME:
                this.requireView().findViewById(R.id.collection_choice_button_icon).setBackgroundResource(R.drawable.rome_champ);
                break;
            default:
                return false;
        }
        return true;
    }


    public void collectionPressed(View view){
        if (!flagDeck) {
            setBackAlpha(0.44f);
            setCollectionAlpha(1);
            this.deckFragmentCall();
            flagDeck=true;
        }
    }

    public void backPressed(View view){
        if(flagDeck) {
            BackFragment backFragment = new BackFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.HERO_KEY, this.hero);
            bundle.putString(Constants.USER_KEY, this.key);
            backFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_animator, 0)
                    .replace(R.id.layout_collection, backFragment)
                    .commit();

            flagDeck = false;
            setBackAlpha(1);
            setCollectionAlpha(0.44f);
        }
    }

    private void setCollectionAlpha(float alpha) {
        this.requireView().findViewById(R.id.collection_button).setAlpha(alpha);
        this.requireView().findViewById(R.id.collection_choice_button_icon).setAlpha(alpha);
        this.requireView().findViewById(R.id.back_choice_text).setAlpha(alpha);
    }

    private void setBackAlpha(float alpha) {
        this.requireView().findViewById(R.id.back_collection_button).setAlpha(alpha);
        this.requireView().findViewById(R.id.back_icon).setAlpha(alpha);
        this.requireView().findViewById(R.id.back_choice_text).setAlpha(alpha);
    }

}