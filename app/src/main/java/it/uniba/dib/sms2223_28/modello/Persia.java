package it.uniba.dib.sms2223_28.modello;

import java.util.LinkedList;
import java.util.List;

import it.uniba.dib.sms2223_28.R;

public class Persia extends GenericDeck {

    public Persia(){
        List<Effect> effects;
        this.deck =new LinkedList<>();
        Card card;

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for(int i=0;i<5;i++) {
            card = new Card(1, 1, R.string.sparabara, R.string.sparabara_effect, R.drawable.sparabara, R.string.legionari_description, -1, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for(int i=0;i<3;i++) {
            card = new Card(2,2, R.string.arcieri_persiani,R.string.arcieri_persiani_effect,R.drawable.arcieri_persiani,R.string.legionari_description, -2, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.HEAL_FOR_OPPONENT_TROOPS);
        this.deck.add(new Card(2,null, R.string.shamshir,R.string.shamshir_effect,R.drawable.shamshir,R.string.legionari_description,0,0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.IMMORTALS);
        this.deck.add(new Card(3,3, R.string.immortali,R.string.immortali_effect,R.drawable.immortali,R.string.legionari_description,0,0, effects));
        this.deck.add(new Card(3,3, R.string.immortali,R.string.immortali_effect,R.drawable.immortali,R.string.legionari_description,0,0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        this.deck.add(new Card(3,2, R.string.satrapi,R.string.satrapi_effect,R.drawable.satrapi,R.string.legionari_description,-2,1, effects));
        this.deck.add(new Card(3,2, R.string.satrapi,R.string.satrapi_effect,R.drawable.satrapi,R.string.legionari_description,-2, 1, effects));

        effects = new LinkedList<>();
        effects.add(Effect.HEAL_FOR_OPPONENT_TROOPS);
        this.deck.add(new Card(4,2, R.string.elefanti, R.string.elefanti_effect,R.drawable.elefanti,R.string.legionari_description,-4,0,effects));
        this.deck.add(new Card(4,2, R.string.elefanti, R.string.elefanti_effect,R.drawable.elefanti,R.string.legionari_description,-4,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.PERSIAN_BOW);
        this.deck.add(new Card(5,null, R.string.arco_persiano,R.string.arco_persiano_effect,R.drawable.arco_persiano,R.string.legionari_description,0,0,effects));
        this.deck.add(new Card(5,null, R.string.arco_persiano,R.string.arco_persiano_effect,R.drawable.arco_persiano,R.string.legionari_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.ALEXANDER_THE_GREAT);
        this.deck.add(new Card(5,5, R.string.alessandro_magno,R.string.alessandro_magno_effect,R.drawable.alessandro_magno,R.string.legionari_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.SELEUCUS);
        this.deck.add(new Card(5,4, R.string.seleuco,R.string.seleuco_effect,R.drawable.seleuco,R.string.legionari_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.CIRO);
        this.deck.add(new Card(5,5, R.string.ciro,R.string.ciro_effect,R.drawable.ciro,R.string.legionari_description,0,0,effects));

    }
}
