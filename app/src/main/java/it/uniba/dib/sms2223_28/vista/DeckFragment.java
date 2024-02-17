package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.CollectionActivity;
import it.uniba.dib.sms2223_28.exception.FilledHandException;
import it.uniba.dib.sms2223_28.modello.GenericDeck;

public class DeckFragment extends Fragment{
    public final static String DECK_TO_SHOW_KEY="hero deck";
    public static final int DELAY_TIME_FOR_NEXT_CARD = 25;
    private static final int NUM_ROWS=5;
    private static final int NUM_CARDS_ON_LINE=4;
    private ArrayList<FrameLayout>[] cardViews;
    private GenericDeck deck;
    private int cardPosition;
    private boolean flagLayoutCompleted =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        this.setCardViews();
        flagLayoutCompleted=false;

        if(savedInstanceState==null)
            savedInstanceState=this.getArguments();
        else {
            flagLayoutCompleted =true;
        }

        this.deck=(GenericDeck) savedInstanceState.getSerializable(DECK_TO_SHOW_KEY);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(flagLayoutCompleted)
            if(!getParentFragmentManager().isDestroyed())
                uploadLayout();
    }

    public void uploadLayout() {
        ArrayList<HandHandler> listRows=new ArrayList<>(NUM_ROWS);
        try {
            cardPosition=0;
            for (int i=0; i<NUM_ROWS; i++ ) {
                
                listRows.add(new HandHandler(cardViews[i],this.getParentFragmentManager()));
                HandHandler row=listRows.get(i);
                for ( int k=0;k<NUM_CARDS_ON_LINE;k++) {
                    FrameLayout card=this.cardViews[i].get(k);
                    
                    new Handler().postDelayed(() -> {
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.animator.slide_in_animator, R.animator.slide_out_animator);

                            try {
                                row.addCard(deck.getCardByPosition(cardPosition));
                                card.setTag(R.id.TagCardPositionValue, cardPosition++);
                            } catch (FilledHandException e) {
                                throw new RuntimeException(e);
                            }
                            transaction.commit();
                            
                        }, DELAY_TIME_FOR_NEXT_CARD * ((i * NUM_CARDS_ON_LINE) + k));
                }
            }
        } catch ( NullPointerException e) {e.printStackTrace();}
    }

    private void setCardViews() {
        View view=this.getView();
        this.cardViews = new ArrayList[NUM_ROWS];

        this.cardViews[0]=new ArrayList<>(NUM_CARDS_ON_LINE);
        cardViews[0].add(view.findViewById(R.id.card1));
        cardViews[0].add(view.findViewById(R.id.card2));
        cardViews[0].add(view.findViewById(R.id.card3));
        cardViews[0].add(view.findViewById(R.id.card4));

        this.cardViews[1]=new ArrayList<>(NUM_CARDS_ON_LINE);
        cardViews[1].add(view.findViewById(R.id.card5));
        cardViews[1].add(view.findViewById(R.id.card6));
        cardViews[1].add(view.findViewById(R.id.card7));
        cardViews[1].add(view.findViewById(R.id.card8));

        this.cardViews[2]=new ArrayList<>(NUM_CARDS_ON_LINE);
        cardViews[2].add(view.findViewById(R.id.card9));
        cardViews[2].add(view.findViewById(R.id.card10));
        cardViews[2].add(view.findViewById(R.id.card11));
        cardViews[2].add(view.findViewById(R.id.card12));

        this.cardViews[3]=new ArrayList<>(NUM_CARDS_ON_LINE);
        cardViews[3].add(view.findViewById(R.id.card13));
        cardViews[3].add(view.findViewById(R.id.card14));
        cardViews[3].add(view.findViewById(R.id.card15));
        cardViews[3].add(view.findViewById(R.id.card16));

        this.cardViews[4]=new ArrayList<>(NUM_CARDS_ON_LINE);
        cardViews[4].add(view.findViewById(R.id.card17));
        cardViews[4].add(view.findViewById(R.id.card18));
        cardViews[4].add(view.findViewById(R.id.card19));
        cardViews[4].add(view.findViewById(R.id.card20));
    }


    public void setOnClick(){
        for ( ArrayList <FrameLayout> row: this.cardViews )
            for ( FrameLayout card: row)
                card.setOnClickListener((CollectionActivity)getActivity());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DECK_TO_SHOW_KEY,this.deck);
    }

}