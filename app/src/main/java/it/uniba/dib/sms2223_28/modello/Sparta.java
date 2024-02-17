package it.uniba.dib.sms2223_28.modello;

import java.util.LinkedList;
import java.util.List;

import it.uniba.dib.sms2223_28.R;

public class Sparta extends GenericDeck {

    public Sparta(){
        List<Effect> effects;
        this.deck=new LinkedList<>();
        Card card;


        effects = new LinkedList<>();
        effects.add(Effect.X_DAMAGE);
        for (int i =0; i<5; i++) {
            card = new Card(1, 2, R.string.iloti, R.string.iloti_effect, R.drawable.iloti, R.string.iloti_description, 1, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for (int i =0; i<3; i++) {
            card = new Card(2, 1, R.string.spartiati, R.string.spartiati_effect, R.drawable.spartiati, R.string.spartiati_description, 3, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.X_DAMAGE);
        for (int i =0; i<2; i++) {
            card = new Card(2, 3, R.string.cavalleria, R.string.cavalleria_effect, R.drawable.cavalleria, R.string.cavalleria_description, 3, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        for (int i =0; i<2; i++) {
            card = new Card(3, null, R.string.oplon, R.string.oplon_effect, R.drawable.oplon, R.string.oplon_description, 5, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.LEONIDA);
        this.deck.add(new Card(3,2,R.string.trecento,R.string.trecento_effect,R.drawable.trecento,R.string.trecento_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.NO_EFFECT);
        this.deck.add(new Card(3,null,R.string.taigeto, R.string.taigeto_effect,R.drawable.taigeto,R.string.taigeto_description,2,2,effects));


        effects = new LinkedList<>();
        effects.add(Effect.PHALANX);
        for (int i =0; i<2; i++) {
            card = new Card(4, 5, R.string.falange, R.string.falange_effect, R.drawable.falange, R.string.falange_description, 0, 0, effects);
            this.deck.add(card);
        }

        effects = new LinkedList<>();
        effects.add(Effect.THERMOPYLAE);
        this.deck.add(new Card(5,null,R.string.termopili,R.string.termopili_effect,R.drawable.termopili,R.string.termopili_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.GORGO);
        this.deck.add(new Card(5,6,R.string.gorgo,R.string.gorgo_effect,R.drawable.gorgo,R.string.gorgo_description,0,0, effects));

        effects = new LinkedList<>();
        effects.add(Effect.IPPOCRATES);
        this.deck.add(new Card(5,6,R.string.ippocatre,R.string.ippocatre_effect,R.drawable.ippocrate,R.string.ippocrate_description,0,0,effects));

        effects = new LinkedList<>();
        effects.add(Effect.MENELAUS);
        this.deck.add(new Card(5,5,R.string.menelao,R.string.menelao_effect,R.drawable.menelao,R.string.menelao_description,0,0, effects));
    }
}
