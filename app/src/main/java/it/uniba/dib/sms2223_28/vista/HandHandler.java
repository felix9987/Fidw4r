package it.uniba.dib.sms2223_28.vista;


import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.exception.FilledHandException;
import it.uniba.dib.sms2223_28.modello.Card;

public class HandHandler implements Serializable {
    private final List<FrameLayout> cardPositions;
    private final FragmentManager fragmentManager;
    private final int size;

    public HandHandler(List<FrameLayout> cardPositions, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.cardPositions = cardPositions;
        this.size = cardPositions.size();
    }

    public void addCard(Card card) throws FilledHandException {
        int i;
        for ( i=0; i<this.size; i++) {
             if (!this.isVisible(i)) {
                this.setCard(card, i);
                return;
            }
        }
        if (i==this.size) throw new FilledHandException();
    }

    public void removeCard(int pos) throws FilledHandException {
        if (pos>this.size)
            throw new FilledHandException();

        this.cardPositions.get(pos).setVisibility(FrameLayout.INVISIBLE);
    }


    public boolean isVisible(int i){
        return this.cardPositions.get(i).getVisibility()==View.VISIBLE;
    }


    private void setCard(Card card, int i){
        CardLayoutFragment cardLayoutFragment=new CardLayoutFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable(CardLayoutFragment.CARD_KEY, card);
        bundle.putFloat(CardLayoutFragment.CARD_HEIGHT_KEY, cardPositions.get(i).getLayoutParams().height);
        cardLayoutFragment.setArguments(bundle);
        fragmentManager.beginTransaction().
                replace(cardPositions.get(i).getId(), cardLayoutFragment).
                commit();

        this.cardPositions.get(i).setTag(R.id.TagCardValue, card);
        this.cardPositions.get(i).setVisibility(View.VISIBLE);

    }

    public void slideHand(int i){
        for (; i<this.size-1; i++) {
            if (!this.isVisible(i)) {
                for (int k = i + 1; k < cardPositions.size(); k++) {
                    if (this.isVisible(k)) {
                        this.setCard((Card) this.cardPositions.get(k).getTag(R.id.TagCardValue),i);
                        this.cardPositions.get(k).setVisibility(FrameLayout.INVISIBLE);
                        break;
                    }
                    if (k==this.size-1) return;
                }
            }
        }
    }

    public void setOnTouchListener(View.OnTouchListener listener){
        for (View v : cardPositions) v.setOnTouchListener(listener);
    }

    public ArrayList<Card> getListCard () {
        ArrayList<Card> cardList = new ArrayList<>();

        for (int i = 0; i < this.size; i++) {
            if (!this.isVisible(i))
                return cardList;
            cardList.add((Card) this.cardPositions.get(i).getTag(R.id.TagCardValue));
        }
        return cardList;
    }

    public void clearBoard(){
        for (int i = 0; i < this.size; i++){
            this.cardPositions.get(i).setVisibility(FrameLayout.INVISIBLE);
        }
    }

}
