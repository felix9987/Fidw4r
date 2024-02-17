package it.uniba.dib.sms2223_28.vista;

import static java.lang.Math.abs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.exception.FilledHandException;
import it.uniba.dib.sms2223_28.modello.Card;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.Effect;


public class BattleFragment extends Fragment {
    public final static String CARDS_PLAYER = "cards of the player";
    public final static String CARDS_OPPONENT = "cards of the opponent";
    public final static String HERO_PLAYER = "hero of the player";
    public final static String HERO_OPPONENT = "hero of the opponent";
    public final static String HP_PLAYER = "hp of the player";
    public final static String HP_OPPONENT = "hp of the opponent";
    public final static  String PLAYER_USERNAME_KEY="username of the player";
    public final static  String OPPONENT_USERNAME_KEY="username of the opponent";
    private static final String PLAYER_COLOSSEUM_KEY = "player colosseum";
    private static final String OPPONENT_COLOSSEUM_KEY = "opponent colosseum";
    private static final String IS_OPPONENT_CHAMPION_ACTIVE_KEY = "opponent champion";
    private static final String IS_PLAYER_CHAMPION_ACTIVE_KEY = "player champion";
    private static final String OPPONENT_HP_LAST_TURN_KEY = "opponent hp last turn";
    private static final String PLAYER_HP_LAST_TURN_KEY = "player hp last turn";
    private static final String TROOPS_PLAYED_BY_OPPONENT_KEY = "troops played by opponent";
    private static final String TROOPS_PLAYED_BY_PLAYER = "troops played by player";
    private static final String HYKSOS_OPPONENT_KEY = "hyksos opponent";
    private static final String HYKSOS_PLAYER_KEY = "hyksos player";
    private static final String OPPONENT_CARD_TO_DRAW_KEY = "opponent cards to draw";
    private static final String PLAYER_CARD_TO_DRAW_KEY = "player cards to draw";
    private static final String TROOPS_PLAYED_LAST_TURN_BY_PLAYER_KEY = "troops played last turn by player";
    private static final String TROOPS_PLAYED_LAST_TURN_BY_OPPONENT_KEY = "troops played last turn by opponent" ;
    private String playerUsername;
    private String opponentUsername;
    private int heroPlayer;
    private int heroOpponent;
    private AtomicInteger hpPlayer;
    private AtomicInteger hpOpponent;
    public ArrayList<Card> listCardsOpponent;
    public ArrayList<Card> listCardsPlayer;
    private TextView viewOpponent;
    private TextView viewPlayer;
    private HandHandler cardsOpponent;
    private HandHandler cardsPlayer;
    private AtomicInteger nTroopsPlayedPreviousTurnByPlayer;
    private AtomicInteger nTroopsPlayedPreviousTurnByOpponent;
    private int hpPlayerPreviousTurn;
    private AtomicInteger playerCardToDraw;
    private AtomicInteger opponentCardToDraw;
    private AtomicBoolean isPlayerChampionActive;
    private AtomicBoolean isOpponentChampionActive;
    private AtomicInteger nTimesHyksos;
    private AtomicInteger nTimesOpponentHyksos;
    private AtomicInteger nTroopsPlayed;
    private AtomicInteger nTroopsPlayedByOpponent;
    private AtomicBoolean playerColosseum;
    private AtomicBoolean opponentColosseum;
    private int hpOpponentPreviousTurn;
    private long connectionKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nTimesHyksos = new AtomicInteger(0);
        nTimesOpponentHyksos = new AtomicInteger(0);
        nTroopsPlayed = new AtomicInteger(0);
        nTroopsPlayedByOpponent = new AtomicInteger(0);
        isPlayerChampionActive= new AtomicBoolean(false);
        isOpponentChampionActive= new AtomicBoolean(false);
        playerColosseum= new AtomicBoolean(false);
        opponentColosseum= new AtomicBoolean(false);
        nTroopsPlayedPreviousTurnByPlayer =new AtomicInteger(0);
        nTroopsPlayedPreviousTurnByOpponent =new AtomicInteger(0);
        opponentCardToDraw=new AtomicInteger(0);
        playerCardToDraw=new AtomicInteger(0);

        if (savedInstanceState==null){
            savedInstanceState=getArguments();
        } else {
            playerColosseum.set(savedInstanceState.getBoolean(PLAYER_COLOSSEUM_KEY));
            opponentColosseum.set(savedInstanceState.getBoolean(OPPONENT_COLOSSEUM_KEY));
            isOpponentChampionActive.set(savedInstanceState.getBoolean(IS_OPPONENT_CHAMPION_ACTIVE_KEY));
            isPlayerChampionActive.set(savedInstanceState.getBoolean(IS_PLAYER_CHAMPION_ACTIVE_KEY));

            hpOpponentPreviousTurn=savedInstanceState.getInt(OPPONENT_HP_LAST_TURN_KEY);
            hpPlayerPreviousTurn=savedInstanceState.getInt(PLAYER_HP_LAST_TURN_KEY);
            nTroopsPlayedByOpponent.set(savedInstanceState.getInt(TROOPS_PLAYED_BY_OPPONENT_KEY));
            nTroopsPlayed.set(savedInstanceState.getInt(TROOPS_PLAYED_BY_PLAYER));
            nTimesOpponentHyksos.set(savedInstanceState.getInt(HYKSOS_OPPONENT_KEY));
            nTimesHyksos.set(savedInstanceState.getInt(HYKSOS_PLAYER_KEY));
            opponentCardToDraw.set(savedInstanceState.getInt(OPPONENT_CARD_TO_DRAW_KEY));
            playerCardToDraw.set(savedInstanceState.getInt(PLAYER_CARD_TO_DRAW_KEY));
            nTroopsPlayedPreviousTurnByPlayer.set(savedInstanceState.getInt(TROOPS_PLAYED_LAST_TURN_BY_PLAYER_KEY));
            nTroopsPlayedPreviousTurnByOpponent.set(savedInstanceState.getInt(TROOPS_PLAYED_LAST_TURN_BY_OPPONENT_KEY));
        }
        this.playerUsername= savedInstanceState.getString(PLAYER_USERNAME_KEY);
        this.opponentUsername= savedInstanceState.getString(OPPONENT_USERNAME_KEY);
        this.connectionKey= savedInstanceState.getLong(Constants.CONNECTION_KEY);
        this.heroPlayer= savedInstanceState.getInt(HERO_PLAYER);
        this.heroOpponent= savedInstanceState.getInt(HERO_OPPONENT);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PLAYER_USERNAME_KEY, playerUsername);
        outState.putString(OPPONENT_USERNAME_KEY, opponentUsername);
        outState.putLong(Constants.CONNECTION_KEY, connectionKey);
        outState.putInt(HERO_PLAYER,heroPlayer);
        outState.putInt(HERO_OPPONENT,heroOpponent);


        outState.putBoolean(PLAYER_COLOSSEUM_KEY,playerColosseum.get());
        outState.putBoolean(OPPONENT_COLOSSEUM_KEY,opponentColosseum.get());
        outState.putBoolean(IS_OPPONENT_CHAMPION_ACTIVE_KEY,isOpponentChampionActive.get());
        outState.putBoolean(IS_PLAYER_CHAMPION_ACTIVE_KEY,isPlayerChampionActive.get());

        outState.putInt(OPPONENT_HP_LAST_TURN_KEY,hpOpponentPreviousTurn);
        outState.putInt(PLAYER_HP_LAST_TURN_KEY,hpPlayerPreviousTurn);
        outState.putInt(TROOPS_PLAYED_BY_OPPONENT_KEY, nTroopsPlayedByOpponent.get());
        outState.putInt(TROOPS_PLAYED_BY_PLAYER, nTroopsPlayed.get());
        outState.putInt(HYKSOS_OPPONENT_KEY,nTimesOpponentHyksos.get());
        outState.putInt(HYKSOS_PLAYER_KEY,nTimesHyksos.get());
        outState.putInt(OPPONENT_CARD_TO_DRAW_KEY,opponentCardToDraw.get());
        outState.putInt(PLAYER_CARD_TO_DRAW_KEY,playerCardToDraw.get());
        outState.putInt(TROOPS_PLAYED_LAST_TURN_BY_PLAYER_KEY, nTroopsPlayedPreviousTurnByPlayer.get());
        outState.putInt(TROOPS_PLAYED_LAST_TURN_BY_OPPONENT_KEY, nTroopsPlayedPreviousTurnByOpponent.get());
    }

    public void setUpValues(Bundle savedInstanceState) {
        playerCardToDraw = new AtomicInteger(0);
        opponentCardToDraw = new AtomicInteger(0);
        listCardsOpponent = (ArrayList <Card>) savedInstanceState.get(CARDS_OPPONENT);
        listCardsPlayer = (ArrayList <Card>) savedInstanceState.get(CARDS_PLAYER);
        hpPlayer = new AtomicInteger(savedInstanceState.getInt(HP_PLAYER));
        hpOpponent = new AtomicInteger(savedInstanceState.getInt(HP_OPPONENT));
    }

    public int getPlayerCardToDraw() {
        return playerCardToDraw.get();
    }

    public int getOpponentCardToDraw() {
        return this.opponentCardToDraw.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_battle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewOpponent = view.findViewById(R.id.opponentDamage);
        viewPlayer = view.findViewById(R.id.playerDamage);
        this.setBattleButtonClickable(false);


        ((TextView) view.findViewById(R.id.textPlayerHpBattle)).setText(String.valueOf(hpPlayer));
        ((TextView) view.findViewById(R.id.textOpponentHpBattle)).setText(String.valueOf(hpOpponent));
        ((TextView) view.findViewById(R.id.textPlayerNameBattle)).setText(playerUsername);
        ((TextView) view.findViewById(R.id.textOpponentNameBattle)).setText(opponentUsername);

        if (!setHeroImage(view.findViewById(R.id.heroOpponentImageBattle), heroOpponent) ||
                !setHeroImage(view.findViewById(R.id.heroPlayerImageBattle), heroPlayer)) {
            throw new IllegalArgumentException();
        }

        List<FrameLayout> listPlayer = new ArrayList<>();
        List<FrameLayout> listOpponent = new ArrayList<>();
        listOpponent.add(view.findViewById(R.id.cardOpponent1));
        listOpponent.add(view.findViewById(R.id.cardOpponent2));
        listOpponent.add(view.findViewById(R.id.cardOpponent3));
        listOpponent.add(view.findViewById(R.id.cardOpponent4));
        listOpponent.add(view.findViewById(R.id.cardOpponent5));

        listPlayer.add(view.findViewById(R.id.cardBattle1));
        listPlayer.add(view.findViewById(R.id.cardBattle2));
        listPlayer.add(view.findViewById(R.id.cardBattle3));
        listPlayer.add(view.findViewById(R.id.cardBattle4));
        listPlayer.add(view.findViewById(R.id.cardBattle5));

        cardsOpponent = new HandHandler(listOpponent, getParentFragmentManager());
        cardsPlayer = new HandHandler(listPlayer, getParentFragmentManager());

        setCards(listCardsOpponent, cardsOpponent);
        setCards(listCardsPlayer, cardsPlayer);
    }

    private void setCards(List<Card> listCards, HandHandler cards) {
        for (Card card : listCards) {
            try {
                cards.addCard(card);
            } catch (FilledHandException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean setHeroImage(View view, int hero) {
        switch (hero) {
            case Constants.EGYPT:
                view.setBackgroundResource(R.drawable.egypt_hero);
                break;
            case Constants.SPARTA:
                view.setBackgroundResource(R.drawable.sparta_hero);
                break;
            case Constants.PERSIA:
                view.setBackgroundResource(R.drawable.persia_hero);
                break;
            case Constants.ROME:
                view.setBackgroundResource(R.drawable.rome_hero);
                break;
            default:
                return false;
        }
        return true;
    }

    public Integer getHpPlayer() {
        return hpPlayer.get();
    }

    public Integer getHpOpponent() {
        return hpOpponent.get();
    }

    public boolean getPlayerChampion(){
        return isPlayerChampionActive.get();
    }

    public boolean getOpponentChampion(){
        return isOpponentChampionActive.get();
    }

    public void calculateDamage() {
        int damagePlayer = damage(listCardsPlayer);
        int damageOpponent = damage(listCardsOpponent);
        int difference = abs(damagePlayer - damageOpponent);

        if (damagePlayer > damageOpponent) {
            hpOpponent.set(hpOpponent.get() - difference);
            viewOpponent.setText(String.format("%s-%s",viewOpponent.getText(),  difference));
        } else {

            hpPlayer.set(hpPlayer.get() - difference);
            if (difference != 0) {
                viewPlayer.setText(String.format("%s-%s",viewPlayer.getText(),  difference));
            }
        }

    }


    private int damage(List<Card> listCard) {
        int sum = 0;

        for (Card card : listCard)
            try {
                sum = sum + card.getAttack();
            } catch (NullPointerException e) {
                //non contare le magie
            }

        return sum;
    }

    public void effectResolver() {
        this.colosseumCalculator(playerColosseum,cardsPlayer,listCardsPlayer);
        this.colosseumCalculator(opponentColosseum,cardsOpponent,listCardsOpponent);//SI
        this.battlePhase1(listCardsPlayer, viewPlayer, viewOpponent, hpPlayer, hpOpponent, cardsPlayer,
                playerCardToDraw,isPlayerChampionActive,nTimesHyksos, nTroopsPlayed, nTroopsPlayedPreviousTurnByPlayer, playerColosseum);
        this.battlePhase1(listCardsOpponent, viewOpponent, viewPlayer, hpOpponent, hpPlayer, cardsOpponent,
                opponentCardToDraw,isOpponentChampionActive,nTimesOpponentHyksos, nTroopsPlayedByOpponent, nTroopsPlayedPreviousTurnByOpponent,opponentColosseum);

        if (this.isPlayerChampionActive.get())
            this.championEffects(this.listCardsPlayer, this.viewPlayer, this.viewOpponent, this.hpPlayer, this.hpOpponent,heroPlayer);

        if (this.isOpponentChampionActive.get())
            this.championEffects(this.listCardsOpponent, this.viewOpponent,this.viewPlayer,this.hpOpponent, this.hpPlayer,heroOpponent);

        this.battlePhase2(this.listCardsPlayer, this.viewPlayer, this.hpPlayer,this.listCardsOpponent);
        this.battlePhase2(this.listCardsOpponent, this.viewOpponent, this.hpOpponent,this.listCardsPlayer);

        this.battlePhase3(this.listCardsPlayer,this.listCardsOpponent, this.viewPlayer, this.viewOpponent, this.hpOpponent);
        this.battlePhase3(this.listCardsOpponent, this.listCardsPlayer,this.viewOpponent, this.viewPlayer, this.hpPlayer);
        this.battlePhase4(this.listCardsPlayer, this.cardsPlayer, this.listCardsOpponent, this.cardsOpponent);
        this.battlePhase4(this.listCardsOpponent, this.cardsOpponent, this.listCardsPlayer,  this.cardsPlayer);
        this.calculateDamage();
        this.battlePhase5(this.listCardsPlayer, this.viewPlayer, this.viewOpponent, this.hpPlayer, this.hpOpponent, this.hpPlayerPreviousTurn);
        this.battlePhase5(this.listCardsOpponent, this.viewOpponent, this.viewPlayer, this.hpOpponent, this.hpPlayer, this.hpOpponentPreviousTurn);

    }

    public void colosseumCalculator(AtomicBoolean colosseum, HandHandler cards, ArrayList<Card> listCard){
        if(colosseum.get()){
            colosseum.set(false);
            try {
                for (int i=0; i<2; i++) {
                    Card card1 = new Card(3, 2, R.string.gladiatiori, 0, R.drawable.gladiatori, R.string.gladiatori_description, 0, 0, null);
                    cards.addCard(card1);
                    listCard.add(card1);
                }
            } catch (FilledHandException e) {
                Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void battlePhase1(ArrayList<Card> listCards, TextView viewPlayer, TextView viewOpponent, AtomicInteger hpPlayer, AtomicInteger hpOpponent,
                              HandHandler cardsHandler, AtomicInteger playerCardToDraw, AtomicBoolean isChampionActive, AtomicInteger nTimesHyksos,
                              AtomicInteger nTroopsPlayed, AtomicInteger nTroopsPlayedPreviousTurn, AtomicBoolean colosseum ) {

        for (int i = 0; i < listCards.size(); i++) {

            Card card = listCards.get(i);
            List<Effect> effects = card.getEffects();
            Integer damage = card.getDamage();
            Integer draw = card.getDraw();

            if (damage < 0) {
                hpPlayer.set(hpPlayer.get() - damage);
                viewPlayer.setText(String.format("%s+%s", viewPlayer.getText(), abs(damage)));
            } else if (damage > 0) {
                hpOpponent.set(hpOpponent.get() - damage);
                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), damage));
            }
            if (draw > 0) {
                playerCardToDraw.set(playerCardToDraw.get() + draw);
            }

            if (effects==null) return;
            for (Effect effect : effects) {
                if (effect==null) return;
                switch (effect) {

                        //ROMA
                        case COPY_THIS_CARD:
                            Card card1;
                            card1= Card.getAttackWithoutEffect(card);
                            try {
                                cardsHandler.addCard(card1);
                                listCards.add(card1);
                            } catch (FilledHandException e) {
                                Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
                            }
                            break;

                        case NO_EFFECT:
                            nTroopsPlayed.set(nTroopsPlayed.get()+1);
                            break;

                        case TWO_HEAL_FOR_BEFORE_PLAYED_TROOPS:
                            if(nTroopsPlayedPreviousTurn.get() > 0) {
                                viewPlayer.setText(String.format("%s+%s",viewPlayer.getText(), nTroopsPlayedPreviousTurn.get() * 2));
                                hpPlayer.set(hpPlayer.get()  + nTroopsPlayedPreviousTurn.get() * 2);
                            }

                            break;

                        case TWO_DAMAGE_FOR_BEFORE_PLAYED_TROOPS:
                            if (nTroopsPlayedPreviousTurn.get() > 0) {
                                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), nTroopsPlayedPreviousTurn.get() * 2));
                                hpOpponent.set(hpOpponent.get()  - nTroopsPlayedPreviousTurn.get() * 2);
                            }
                            break;

                        case NECROPOLIS:
                            playerCardToDraw.set(playerCardToDraw.get() + nTroopsPlayedPreviousTurn.get());
                            break;

                        case COLOSSEUM:
                            try {
                                for (int k=0; k<2; k++) {
                                    card1 = new Card(3, 2, R.string.gladiatiori, 0, R.drawable.gladiatori, R.string.gladiatori_description, 0, 0, null);
                                    cardsHandler.addCard(card1);
                                    listCards.add(card1);
                                }

                            } catch (FilledHandException e) {
                                Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
                            }

                            colosseum.set(true);
                            break;


                        case SPARTACUS:

                        case ALEXANDER_THE_GREAT:

                        case CHEOPE:
                            nTroopsPlayed.set(nTroopsPlayed.get()+1);

                        case MENELAUS:
                            isChampionActive.set(true);
                            break;

                        case HYKSOS:

                            if(nTimesHyksos.get() > 0) {
                                viewOpponent.setText(String.format("%s-%s",viewOpponent.getText(), nTimesHyksos));
                            }
                            hpOpponent.set(hpOpponent.get()  - nTimesHyksos.get());

                            nTimesHyksos.set(nTimesHyksos.get()+1);
                            nTroopsPlayed.set(nTroopsPlayed.get()+1);
                            break;

                        case SHIP:
                            if(nTroopsPlayed.get() > 0) {
                                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), nTroopsPlayed));
                            }
                            hpOpponent.set(hpOpponent.get()  - nTroopsPlayed.get());

                            break;

                        case PYRAMIDS:
                            for (int j = 0; j < listCards.size(); j++) {
                                if(i!=j) {

                                    Card card2=listCards.get(j);
                                    try {
                                        cardsHandler.addCard(card2);
                                        listCards.add(card2);
                                    }catch (FilledHandException e) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                }
                            }
                            break;

                        case CHEFREN:
                            viewPlayer.setText(String.format("%s+%s",viewPlayer.getText(), 4));
                            hpPlayer.set(hpPlayer.get() + 4);
                            nTroopsPlayed.set(nTroopsPlayed.get()+1);
                            break;

                        case X_DAMAGE:
                            viewPlayer.setText(String.format("%s-%s",viewPlayer.getText(), damage));
                            hpPlayer.set(hpPlayer.get() - damage);
                            break;


                        case THERMOPYLAE:
                            int nTroops = 5 - listCards.size();
                            for(int j = 0; j < nTroops; j++){
                                card1 = new Card(1, 2, R.string.iloti, null, R.drawable.iloti, 0, 0, 0, null);
                                try {
                                    cardsHandler.addCard(card1);
                                    listCards.add(card1);

                                } catch (FilledHandException e) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
                                }
                            }
                            break;


                        case GORGO:
                            if (hpPlayer.get() < hpOpponent.get()) {
                                card.setAttack(card.getAttack() * 2);
                            }
                            break;

                        case IPPOCRATES:
                            if (hpPlayer.get() <= 12) {
                                    viewOpponent.setText(String.format("%s-%s",viewOpponent.getText(), 5));
                                    hpOpponent.set(hpOpponent.get() - 5);
                                }

                            break;
                        case PHALANX:
                            if (hpPlayer.get() < 12) {
                                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), 4));
                                hpOpponent.set(hpOpponent.get() - 4);
                        }
                        default:
                            break;

                    }
                }

        }

    }


    private void battlePhase2(ArrayList<Card> listCardsPlayer, TextView viewPlayer, AtomicInteger hpPlayer, ArrayList<Card> listCardsOpponent) {
        for (int i = 0; i < listCardsPlayer.size(); i++) {
            Card card = listCardsPlayer.get(i);
            List<Effect> effects = card.getEffects();

            try {

                for (Effect effect : effects) {
                    switch (effect) {

                        case ADD_ATTACK_MY_TROOPS:
                            for (int j = 0; j < listCardsPlayer.size(); j++) {
                                if (i != j && listCardsPlayer.get(j).getAttack()!=null)
                                    listCardsPlayer.get(j).setAttack(listCardsPlayer.get(j).getAttack() + 1);
                            }
                            break;

                        //PERSIA
                        case HEAL_FOR_OPPONENT_TROOPS:
                            int heal = this.countTroops(listCardsOpponent);
                            if (heal > 0) {
                                viewPlayer.setText(String.format("%s+%s", viewPlayer.getText(), heal));
                            }
                            hpPlayer.set(hpPlayer.get() + heal);
                            break;


                        case BRUTUS:

                            if (hpPlayer.get() > 2) {
                                viewPlayer.setText(String.format("%s-%s",viewPlayer.getText(), 2));
                                hpPlayer.set(hpPlayer.get() - 2);
                                card.setAttack(card.getAttack() * 2);
                            }
                            break;
                    }

                }
            } catch (NullPointerException e){

            }


        }
    }


    private void championEffects(ArrayList<Card> listCardsPlayer, TextView viewPlayer, TextView viewOpponent, AtomicInteger hpPlayer, AtomicInteger hpOpponent, int hero) {

        switch (hero) {

            case Constants.ROME:

                for (Card card1 : listCardsPlayer) {
                    if (card1.getAttack()!=null && card1.getName()!=R.string.spartaco)
                        card1.setAttack(card1.getAttack() + 1);
                }
                break;


            case Constants.SPARTA:
                int damage = 0;
                for (Card card1 : listCardsPlayer) {
                    if (card1.getAttack() != null && card1.getName() != R.string.menelao) {
                        damage++;
                    }
                }
                if (damage != 0) {
                    try {
                        viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), damage));
                        hpOpponent.set(hpOpponent.get() - damage);

                    } catch (NullPointerException e) {

                    }
                }
                break;

            case Constants.PERSIA:
                int heal = 0;
                try {
                    if (listCardsPlayer.get(0).getName() == R.string.alessandro_magno) return;
                }catch ( IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                for (Card card1 : listCardsPlayer) {
                    if (card1.getAttack() != null) {
                        heal++;
                    }
                }
                if (heal != 0) {
                    try {
                        viewPlayer.setText(String.format("%s+%s", viewPlayer.getText(), heal));
                        hpPlayer.set(hpPlayer.get() + heal);
                    } catch (NullPointerException e) {

                    }
                }
                break;


            case Constants.EGYPT:
                try {
                    if (listCardsPlayer.get(0).getName() == R.string.cheope) return;
                }catch ( IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                viewPlayer.setText(String.format("%s+%s", viewPlayer.getText(), 1));
                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), 1));
                hpOpponent.set(hpOpponent.get() - 1);
                hpPlayer.set(hpPlayer.get() + 1);

                break;
        }

    }

    private void battlePhase3(ArrayList<Card> listCardsPlayer, ArrayList<Card> listCardsOpponent, TextView viewPlayer, TextView viewOpponent, AtomicInteger hpOpponent) {
        for (int i = 0; i < listCardsPlayer.size(); i++) {

            Card card = listCardsPlayer.get(i);
            List<Effect> effects = card.getEffects();

            if (effects == null) continue;

            for (Effect effect : effects) {
                if (effect == Effect.SELEUCUS) {

                    int damage=0;
                    Integer attack;
                    for (Card opponentCard: listCardsOpponent) {

                        attack=opponentCard.getAttack();
                        if (attack!=null && damage < attack)
                            damage = attack;
                    }

                    if (damage > 0) {
                        viewOpponent.setText(String.format("%s-%s", viewPlayer.getText(), damage));
                        hpOpponent.set(hpOpponent.get() - damage);
                    }
                }

            }


        }
    }

    private void battlePhase5(ArrayList<Card> listCardsPlayer, TextView viewPlayer, TextView viewOpponent, AtomicInteger hpPlayer, AtomicInteger hpOpponent, int hpPlayerPreviousTurn) {
        for (int i = 0; i < listCardsPlayer.size(); i++) {
            Card card = listCardsPlayer.get(i);
            List<Effect> effects = card.getEffects();

            try {
                for (Effect effect : effects) {
                    switch (effect) {

                        case IMMORTALS:
                            if (hpPlayerPreviousTurn > hpPlayer.get()) {

                                double differenceHp = (hpPlayerPreviousTurn - hpPlayer.get());
                                int heal;
                                int damage;

                                if ( differenceHp%2==0 )
                                    heal = damage = (int) differenceHp/2;

                                else {
                                    heal = (int) (differenceHp - 1) / 2;
                                    damage = heal++;
                                }

                                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), damage));
                                hpOpponent.set(hpOpponent.get() - damage);
                                viewPlayer.setText(String.format("%s+%s", viewPlayer.getText(), heal));
                                hpPlayer.set(hpPlayer.get() + heal);
                            }
                            break;

                        case PERSIAN_BOW:

                            if (hpPlayerPreviousTurn == 0) {
                                hpPlayerPreviousTurn = 20;
                            }
                            if (hpPlayerPreviousTurn > hpPlayer.get()) {
                                int hpDif = hpPlayerPreviousTurn - hpPlayer.get();
                                viewOpponent.setText(String.format("%s-%s", viewOpponent.getText(), hpDif));
                                hpOpponent.set(hpOpponent.get() - hpDif);
                            }

                            break;

                        case LEONIDA:
                            if (hpPlayerPreviousTurn == 0) {
                                hpPlayerPreviousTurn=20;
                            }
                            if (hpPlayerPreviousTurn > hpPlayer.get()) {
                                int hpDif = hpPlayerPreviousTurn - hpPlayer.get();
                                viewPlayer.setText(String.format("%s+%s", viewPlayer.getText(), hpDif));
                                hpPlayer.set(hpPlayer.get() + hpDif);

                            }
                            break;
                    }

                }
            } catch (NullPointerException e){}
        }
    }

    private void battlePhase4(ArrayList<Card> listCardsPlayer, HandHandler cardsPlayer, ArrayList<Card> listCardsOpponent, HandHandler cardsOpponent) {
        for (int i = 0; i < listCardsPlayer.size(); i++) {
            Card card = listCardsPlayer.get(i);
            List<Effect> effects = card.getEffects();

            try {

                for (Effect effect : effects) {
                    if (Objects.requireNonNull(effect) == Effect.CIRO) {
                        try {
                            if (listCardsOpponent.isEmpty() || listCardsOpponent.get(0).getName()==R.string.ciro) return;
                            boolean flag = false;
                            int random = new Random(connectionKey).nextInt(listCardsOpponent.size());
                            Card card1 = listCardsOpponent.get(random);

                            while (card1.getAttack() == null) {
                                random++;
                                if (random >= listCardsOpponent.size()) {
                                    if (flag) return;
                                    random = 0;
                                    flag = true;
                                }
                                card1 = listCardsOpponent.get(random);
                            }

                            if (card1.getName() == R.string.immortali || card1.getName() == R.string.trecento) {
                                cardsPlayer.addCard(card1);
                                card.setAttack(card.getAttack() + 3);
                                cardsOpponent.removeCard(random);
                                listCardsOpponent.get(random).setAttack(0);
                                return;
                            }

                            cardsPlayer.addCard(card1);
                            listCardsPlayer.add(card1);
                            cardsOpponent.removeCard(random);
                            listCardsOpponent.remove(card1);



                        } catch (FilledHandException e) {
                            Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            } catch (NullPointerException e){}
        }
    }




    @Override
    public void onStop() {
        nTroopsPlayedPreviousTurnByPlayer = new AtomicInteger(countTroops(this.listCardsPlayer));
        nTroopsPlayedPreviousTurnByOpponent = new AtomicInteger(countTroops(this.listCardsOpponent));
        hpPlayerPreviousTurn = hpPlayer.get();
        hpOpponentPreviousTurn = hpOpponent.get();
        super.onStop();
    }
    private int countTroops(List<Card> list) {
        int nTroops = 0;
        for(Card card: list) {
            if(card.getAttack() != null) {
                nTroops++;
            }
        }
        if (nTroops > 5) {
            nTroops = 5;
        }
        return nTroops;
    }

    public void setBattleButtonClickable(boolean flag) {
        requireView().findViewById(R.id.btnEndBattle).setClickable(flag);
    }
}