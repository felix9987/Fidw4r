package it.uniba.dib.sms2223_28.modello;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class GenericDeck implements Serializable {
    protected LinkedList<Card> deck;
    private final static int max=20;

    public Card topdeck() {
        if (this.deck.size()==0) {

            return null;
        }
        else return this.deck.pop();
    }

    public int getNCarte() {
        return this.deck.size();
    }

    public void shuffle() {
        Collections.shuffle(this.deck);
        }
    public Card getCardByPosition(int i){
        return this.deck.get(i);
    }

}
