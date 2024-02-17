package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.modello.Card;

public class CollectionSingleCardFragment extends Fragment {
    public final static String CARD_KEY="4";
    private Card card;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));

        savedInstanceState=getArguments();
        if (savedInstanceState != null) {
            this.card = (Card) savedInstanceState.get(CARD_KEY);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_single_card, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FragmentManager fragmentManager=this.getParentFragmentManager();

        Bundle bundle=new Bundle();
        bundle.putSerializable(CardLayoutFragment.CARD_KEY,card);
        bundle.putFloat(CardLayoutFragment.CARD_HEIGHT_KEY, this.getResources().getDimension(R.dimen.big_card_height_collection));

        CardLayoutFragment cardLayoutFragment=new CardLayoutFragment();
        cardLayoutFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.card_front_frame, cardLayoutFragment).commit();

        ((TextView)view.findViewById(R.id.text_description)).setText(this.getResources().getString(card.getDescription()));
    }

    public void setVisibility(int visibility) {
        requireView().findViewById(R.id.collectionSingleCardFragmentView).setVisibility(visibility);
    }
}