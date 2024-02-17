package it.uniba.dib.sms2223_28.modello;



import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {
    private final Integer stroba;
    private Integer attack;
    private final Integer name;
    private final Integer effectDescription;
    private final Integer idDrawable;
    private final Integer description;
    private Integer damage;
    private Integer draw;
    private List<Effect> effects;
    public Card(Integer stroba, Integer attack, Integer name, Integer effect, Integer idDrawable, Integer description) {
        this.stroba = stroba;
        this.attack = attack;
        this.name = name;
        this.effectDescription = effect;
        this.idDrawable = idDrawable;
        this.description = description;
    }

    public Card(Integer stroba, Integer attack, Integer name, Integer effect, Integer idDrawable, Integer description, Integer damage, Integer draw, List<Effect> effects) {
        this.stroba = stroba;
        this.attack = attack;
        this.name = name;
        this.effectDescription = effect;
        this.idDrawable = idDrawable;
        this.description = description;
        this.damage=damage;
        this.draw = draw;
        this.effects = effects;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDamage() {
        return damage;
    }

    public Integer getDraw() {
        return draw;
    }

    public Integer getStroba() {
        return stroba;
    }

    public Integer getName() {
        return name;
    }

     public Integer getAttack() {
        return attack;
    }

    public Integer getIdDrawable() {
        return idDrawable;
    }

    public Integer getEffect() {
        return effectDescription;
    }
    public Integer getDescription() { return description; }


    @NonNull
    @Override
    public String toString() {
        return "Card{" +
                "stroba=" + stroba +
                ", attack=" + attack +
                ", name=" + name +
                ", effectDescription=" + effectDescription +
                ", idDrawable=" + idDrawable +
                ", description=" + description +
                ", damage=" + damage +
                ", draw=" + draw +
                ", effects=" + effects.get(0) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Card)) return false;


        Card card= (Card) obj;

        return this.name.equals(card.name) &&
                this.stroba.equals(card.stroba) &&
                this.effectDescription.equals(card.effectDescription)
                && this.idDrawable.equals(card.idDrawable) ;
    }

    public static Card getAttackWithoutEffect(Card card){
        return new Card(card.stroba, card.attack, card.name, 0,card.idDrawable, card.description,card.damage, card.draw, null);
    }


}
