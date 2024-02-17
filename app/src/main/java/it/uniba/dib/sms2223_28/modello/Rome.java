package it.uniba.dib.sms2223_28.modello;


import java.util.LinkedList;
import java.util.List;

import it.uniba.dib.sms2223_28.R;

public class Rome extends GenericDeck {
    public Rome(){
        List<Effect> effects;
        this.deck =new LinkedList<>();
        Card card;

        effects = new LinkedList<>();
        effects.add(Effect.COPY_THIS_CARD);
        for(int i=0; i<5; i++) {
            card = new Card(1, 1, R.string.legionari, R.string.legionari_effect, R.drawable.legionari, R.string.legionari_description, 0, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.ADD_ATTACK_MY_TROOPS);
        this.deck.add(new Card(2,1,R.string.bighe,R.string.bighe_effect,R.drawable.bighe,R.string.bighe_description,0,0, effects));
        this.deck.add(new Card(2,1,R.string.bighe,R.string.bighe_effect,R.drawable.bighe,R.string.bighe_description,0,0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        this.deck.add(new Card(2,2,R.string.centurioni,R.string.centurioni_effect,R.drawable.centurioni,R.string.centurioni_description, 2, 0, effects));
        this.deck.add(new Card(2,2,R.string.centurioni,R.string.centurioni_effect,R.drawable.centurioni,R.string.centurioni_description, 2, 0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.COPY_THIS_CARD);
        effects.add(Effect.COPY_THIS_CARD);
        this.deck.add(new Card(3,2,R.string.gladiatiori,R.string.gladiatori_effect,R.drawable.gladiatori,R.string.gladiatori_description,0,0,effects));
        this.deck.add(new Card(3,2,R.string.gladiatiori,R.string.gladiatori_effect,R.drawable.gladiatori,R.string.gladiatori_description,0,0,effects));
        this.deck.add(new Card(3,2,R.string.gladiatiori,R.string.gladiatori_effect,R.drawable.gladiatori,R.string.gladiatori_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.TWO_HEAL_FOR_BEFORE_PLAYED_TROOPS);
        this.deck.add(new Card(3,null,R.string.terme,R.string.terme_effect,R.drawable.terme,R.string.terme_description, 0, 0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.TWO_DAMAGE_FOR_BEFORE_PLAYED_TROOPS);
        this.deck.add(new Card(4,null,R.string.testuggine,R.string.testuggine_effect,R.drawable.testuggine,R.string.testuggine_description, 0, 0, effects));
        this.deck.add(new Card(4,null,R.string.testuggine,R.string.testuggine_effect,R.drawable.testuggine,R.string.testuggine_description, 0, 0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.NECROPOLIS);
        this.deck.add(new Card(4,null,R.string.necropoli,R.string.necropoli_effect,R.drawable.necropoli,R.string.terme_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        this.deck.add(new Card(4,5,R.string.romoloremo,R.string.romoloremo_effect,R.drawable.romoloeremo,R.string.romoloremo_description,5, 0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.COLOSSEUM);
        this.deck.add(new Card(5,null,R.string.colosseo,R.string.colosseo_effect,R.drawable.colosseo,R.string.colosseo_description,0,0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.SPARTACUS);
        this.deck.add(new Card(5,5,R.string.spartaco,R.string.spartaco_effect,R.drawable.spartaco,R.string.spartaco_description, 0,0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.BRUTUS);
        this.deck.add(new Card(5,6,R.string.bruto,R.string.bruto_effect,R.drawable.bruto,R.string.bruto_description,0,0, effects));
    }

}
