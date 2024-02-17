package it.uniba.dib.sms2223_28.modello;

import java.util.LinkedList;
import java.util.List;

import it.uniba.dib.sms2223_28.R;

public class Egypt extends GenericDeck {

    public Egypt() {
        List<Effect> effects;
        this.deck = new LinkedList<>();
        Card card;

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for(int i=0; i<5; i++){
            card=new Card(1,2,R.string.schiavi, R.string.schiavi_effect,R.drawable.schiavi,R.string.schiavi_description,0,0,effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.HYKSOS);
        for(int i=0; i<4; i++) {
            card = new Card(2, 2, R.string.hyksos, R.string.hyksos_effect, R.drawable.hyksos, R.string.hyksos_description, 0, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.PYRAMIDS);
        for(int i=0; i<2; i++) {
            card = new Card(3, null, R.string.piramide, R.string.piramide_effect, R.drawable.piramide, R.string.piramide_description, 0, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for(int i=0; i<2; i++) {
            card = new Card(3, 3, R.string.visir, R.string.visir_effect, R.drawable.visir, R.string.visir_description, 0, 1, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for(int i=0; i<3; i++) {
            card = new Card(4, 4, R.string.sacerdoti, R.string.sacerdoti_effect, R.drawable.sacerdoti, R.string.sacerdoti_description, -4, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.SHIP);
        this.deck.add(new Card(5,null,R.string.nave, R.string.nave_effect,R.drawable.nave,R.string.nave_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        this.deck.add(new Card(5,5,R.string.micerino, R.string.micerino_effect,R.drawable.micerino,R.string.micerino_description,-5,1,effects));

        effects = new LinkedList<>();
        effects.add(Effect.CHEFREN);
        this.deck.add(new Card(5,4,R.string.chefren, R.string.chefren_effect,R.drawable.chefren,R.string.chefren_description,4,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.CHEOPE);
        this.deck.add(new Card(5,5,R.string.cheope, R.string.cheope_effect,R.drawable.cheope,R.string.cheope_description,0,0,effects));
        }
}
