package it.uniba.dib.sms2223_28.vista;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MenuGameActivity;
import it.uniba.dib.sms2223_28.modello.User;
import it.uniba.dib.sms2223_28.activity.GameplayActivity;
import it.uniba.dib.sms2223_28.exception.FilledHandException;
import it.uniba.dib.sms2223_28.modello.Card;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.GenericDeck;


public class GameplayFragment extends Fragment {
    public static final String CHAMPION_PLAYER_KEY = "champion player";
    public static final String CHAMPION_OPPONENT_KEY = "champion opponent";
    public static final String HERO_OPPONENT_KEY = "hero opponent image";
    public static final String HERO_KEY="hero";
    public static final String DECK_KEY = "deck";
    public static final String HERO_PLAYER_KEY = "hero Player image";
    private static final String TOTAL_STROBA = "totalStroba";
    private static final String TURN_STROBA = "turnStroba";
    private static final String CARD_PLAYED = "card played";
    private static final String PLAYER_HAND = "playerHand";
    private static final String N_CARD_DECK_PLAYER = "nCardDeckPlayer";
    private static final String N_CARD_DECK_OPPONENT = "nCardDeckOpponent";
    private static final String N_CARD_HAND_OPPONENT = "nCardHandOpponent";
    private static final String HP_PLAYER="hp player";
    private static final String HP_OPPONENT="hp opponent";
    private static final int NUM_INITIAL_CARD_HAND = 4;
    private HandHandler playerHand;
    private GenericDeck deck;
    private int turnStroba;
    private int totalStroba;
    private int nCardDeckPlayer;
    private int nCardDeckOpponent;
    private int nCardHandOpponent;
    private HandHandler cardPlayed;
    private int heroPlayerImage;
    private int heroOpponentImage;
    private User opponentUser;
    private int playerChampion;
    private int opponentChampion;
    private boolean cardPlayedOnTouchFlag;
    private int nCardInTouchingMode;
    private TextView playerHpView;
    private TextView opponentHpView;
    TextView timerTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        totalStroba = 1;
        turnStroba = 1;
        nCardDeckPlayer=16;
        nCardDeckOpponent=16;
        nCardHandOpponent=4;
        cardPlayedOnTouchFlag =true;
        nCardInTouchingMode =0;

        if(savedInstanceState == null) {
            savedInstanceState = this.getArguments();
        }
        deck = (GenericDeck) savedInstanceState.getSerializable(DECK_KEY);
        heroPlayerImage = savedInstanceState.getInt(HERO_PLAYER_KEY);
        heroOpponentImage = savedInstanceState.getInt(HERO_OPPONENT_KEY);
        opponentUser =(User)savedInstanceState.getSerializable(Constants.USER2);
        playerChampion=savedInstanceState.getInt(CHAMPION_PLAYER_KEY);
        opponentChampion=savedInstanceState.getInt(CHAMPION_OPPONENT_KEY);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TOTAL_STROBA, totalStroba);
        outState.putInt(TURN_STROBA, turnStroba);
        outState.putSerializable(CARD_PLAYED, cardPlayed.getListCard());
        outState.putSerializable(PLAYER_HAND, playerHand.getListCard());
        outState.putInt(N_CARD_HAND_OPPONENT, nCardDeckOpponent);
        outState.putInt(N_CARD_DECK_OPPONENT, nCardDeckOpponent);
        outState.putInt(N_CARD_DECK_PLAYER, nCardDeckPlayer);

        outState.putSerializable(DECK_KEY, deck);
        outState.putInt(HERO_PLAYER_KEY, heroPlayerImage);
        outState.putInt(HERO_OPPONENT_KEY, heroOpponentImage);
        outState.putSerializable(Constants.USER, MenuGameActivity.playerUser);
        outState.putSerializable(Constants.USER2, opponentUser);
        outState.putInt(CHAMPION_PLAYER_KEY, playerChampion);
        outState.putInt(CHAMPION_OPPONENT_KEY, opponentChampion);
        outState.putString(HP_PLAYER, (String) playerHpView.getText());
        outState.putString(HP_OPPONENT, (String) opponentHpView.getText());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gameplay, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.heroPlayerImage).setBackgroundResource(heroPlayerImage);
        view.findViewById(R.id.heroOpponentImage).setBackgroundResource(heroOpponentImage);
        view.findViewById(R.id.championImage).setBackgroundResource(playerChampion);
        view.findViewById(R.id.opponentChampionImage).setBackgroundResource(opponentChampion);
        ((TextView)view.findViewById(R.id.textopponentName)).setText(opponentUser.getUsername());
        ((TextView)view.findViewById(R.id.textPlayerName)).setText(MenuGameActivity.playerUser.getUsername());

        view.findViewById(R.id.btnSurrender).setOnClickListener(view1 -> view.findViewById(R.id.surrender_gui).setVisibility(View.VISIBLE));
        view.findViewById(R.id.btnNotSurrender2).setOnClickListener(view1 -> view.findViewById(R.id.surrender_gui).setVisibility(View.INVISIBLE));

        FragmentManager fragmentManager = this.getParentFragmentManager();
        ArrayList<FrameLayout> cardPositions = getFrameLayoutsCardsPlayer();
        playerHand=new HandHandler(cardPositions,fragmentManager);
        cardPositions = getFrameLayoutsCardsPlayed();
        cardPlayed = new HandHandler(cardPositions,fragmentManager);

        playerHpView=view.findViewById(R.id.textPlayerHp);
        opponentHpView=view.findViewById(R.id.textOpponentHp);
        timerTextView = view.findViewById(R.id.remainingTime);




        if (savedInstanceState != null) {
            totalStroba = savedInstanceState.getInt(TOTAL_STROBA);
            turnStroba = savedInstanceState.getInt(TURN_STROBA);
            nCardDeckPlayer = savedInstanceState.getInt(N_CARD_DECK_PLAYER);
            nCardDeckOpponent = savedInstanceState.getInt(N_CARD_DECK_OPPONENT);
            nCardHandOpponent = savedInstanceState.getInt(N_CARD_HAND_OPPONENT);

            deck = (GenericDeck) savedInstanceState.getSerializable(DECK_KEY);
            heroPlayerImage = savedInstanceState.getInt(HERO_PLAYER_KEY);
            heroOpponentImage = savedInstanceState.getInt(HERO_OPPONENT_KEY);
            MenuGameActivity.playerUser=(User)savedInstanceState.getSerializable(Constants.USER);
            opponentUser =(User)savedInstanceState.getSerializable(Constants.USER2);
            playerChampion=savedInstanceState.getInt(CHAMPION_PLAYER_KEY);
            opponentChampion=savedInstanceState.getInt(CHAMPION_OPPONENT_KEY);

            ((TextView) view.findViewById(R.id.stroba)).setText(String.valueOf(totalStroba));
            ((TextView) view.findViewById(R.id.textNCardDeck)).setText(String.valueOf(nCardDeckPlayer));
            ((TextView) view.findViewById(R.id.textNCardOpponentDeck)).setText(String.valueOf(nCardDeckOpponent));
            ((TextView) view.findViewById(R.id.NCardOpponentHand)).setText(String.valueOf(nCardHandOpponent));
            this.playerHpView.setText(savedInstanceState.getString(HP_PLAYER));
            this.opponentHpView.setText(savedInstanceState.getString(HP_OPPONENT));

            for ( Card card: (List<Card>) savedInstanceState.getSerializable(PLAYER_HAND) ) {
                try {
                    playerHand.addCard(card);
                } catch (FilledHandException e) {
                    //non si attiva mai
                }
            }
            for ( Card card: (List<Card>) savedInstanceState.getSerializable(CARD_PLAYED) ) {
                try {
                    cardPlayed.addCard(card);
                } catch (FilledHandException e) {
                    //non si attiva mai
                }
            }

        }else{
            deck.shuffle();
            for(int i = 0; i < NUM_INITIAL_CARD_HAND; i++){
                Card card=deck.topdeck();
                try {
                    playerHand.addCard(card);
                } catch (FilledHandException e) {
                    //non si attiva mai
                }
            }
        }

        this.setOnTouchListeners();
    }

    private void setOnTouchListeners() {
        View view=this.getView();
        this.playerHand.setOnTouchListener((view1, motionEvent) -> {
            if (!cardPlayedOnTouchFlag) return false;
            onTouchHandler(view1, motionEvent, (view11, cardFrame, card) -> {
                if (cardCollision(cardFrame.getX(), cardFrame.getY()) &&  card.getStroba() <= turnStroba){
                    try {
                        turnStroba = turnStroba - card.getStroba();
                        cardPlayed.addCard(card);
                        ((TextView) requireView().findViewById(R.id.stroba)).setText(String.valueOf(turnStroba));
                    } catch (FilledHandException e) {
                        Toast.makeText(getContext(), getResources().getString(R.string.cinque_carte), Toast.LENGTH_LONG).show();
                    }
                    playerHand.slideHand(0);
                }
                else view11.setVisibility(View.VISIBLE);
            });
            return true;
        }

        );

        this.cardPlayed.setOnTouchListener((view12, motionEvent) -> {
            if (!cardPlayedOnTouchFlag) return false;
            onTouchHandler(view12, motionEvent, (view121, cardFrame, card) -> {
                if (cardFrame.getY() + cardFrame.getHeight() > requireView().findViewById(R.id.backDeckPlayer).getY()+requireView().findViewById(R.id.backDeckPlayer).getHeight()){
                    try {
                        turnStroba = turnStroba + card.getStroba();
                        playerHand.addCard(card);
                        ((TextView) requireView().findViewById(R.id.stroba)).setText(String.valueOf(turnStroba));
                    } catch (FilledHandException e) {
                        //mano piena
                    }
                    cardPlayed.slideHand(0);
                }
                else view121.setVisibility(View.VISIBLE);
            });
            return true;
        });
        Objects.requireNonNull(view).findViewById(R.id.championImage).setOnTouchListener((v, motionEvent) -> champDescriptionOnTouch(playerChampion,motionEvent));

        view.findViewById(R.id.opponentChampionImage).setOnTouchListener((v, motionEvent) -> champDescriptionOnTouch(opponentChampion,motionEvent));

        view.findViewById(R.id.playerInfographic).setOnTouchListener((v, motionEvent) -> {
            infographicDescriptionOnTouch(requireView().findViewById(R.id.player_info_description) ,motionEvent);
            return true;
        });

        view.findViewById(R.id.opponentInfographic).setOnTouchListener((v, motionEvent) -> {
            infographicDescriptionOnTouch(requireView().findViewById(R.id.opponent_info_description) ,motionEvent);
            return true;
        });

    }

    private static void infographicDescriptionOnTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            view.setVisibility(View.VISIBLE);

        else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
            view.setVisibility(View.INVISIBLE);
    }

    private boolean champDescriptionOnTouch(int championDrawable, MotionEvent motionEvent) {
        View view=requireView().findViewById(R.id.champion_description_back);
        TextView text=requireView().findViewById(R.id.champion_description);

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int championEffect;
            if (championDrawable==R.drawable.egypt_champ)
                    championEffect=R.string.cheope_effect;

            else if (championDrawable==R.drawable.persia_champ)
                    championEffect=R.string.alessandro_magno_effect;

            else if (championDrawable==R.drawable.sparta_champ)
                    championEffect=R.string.menelao_effect;

            else if (championDrawable==R.drawable.rome_champ)
                    championEffect=R.string.spartaco_effect;
            else
                return true;

            text.setText(championEffect);
            view.setBackgroundResource(championDrawable);
            view.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
        }

        return true;
    }

    public void setTimer(int time){
        timerTextView.setText(String.valueOf(time));
    }

    public int getRemainingTime(){
        return Integer.parseInt((String) timerTextView.getText());
    }

    @NonNull
    private ArrayList<FrameLayout> getFrameLayoutsCardsPlayed() {
        ArrayList<FrameLayout> cardPositions;
        cardPositions=new ArrayList<>(GameplayActivity.MAX_CARD_ON_HAND);
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayed1));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayed2));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayed3));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayed4));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayed5));
        return cardPositions;
    }

    @NonNull
    private ArrayList<FrameLayout> getFrameLayoutsCardsPlayer() {
        ArrayList <FrameLayout> cardPositions=new ArrayList<>(GameplayActivity.MAX_CARD_ON_HAND);
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayer1));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayer2));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayer3));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayer4));
        cardPositions.add(this.requireView().findViewById(R.id.cardPlayer5));
        return cardPositions;
    }

    public ArrayList<Card> getPlayedCardList(){
        return this.cardPlayed.getListCard();
    }


    public void onTouchHandler(View view, MotionEvent motionEvent, CollisionHandler collisionHandler) {
        FrameLayout cardFrame = this.requireView().findViewById(R.id.CardDraggedFrame);

        float offsetX = (float) cardFrame.getWidth() / 2;
        float offsetY = (float) cardFrame.getHeight()/ 2;

        Resources res=this.getResources();


        if((motionEvent.getAction() == MotionEvent.ACTION_DOWN)) {
            if (nCardInTouchingMode!=0) return;
            nCardInTouchingMode++;
            Bundle bundle=new Bundle();
            bundle.putFloat(CardLayoutFragment.CARD_HEIGHT_KEY,res.getDimension(R.dimen.cardDragged_height));
            bundle.putSerializable(CardLayoutFragment.CARD_KEY,(Card) view.getTag(R.id.TagCardValue));


            CardLayoutFragment cardDragged=new CardLayoutFragment();
            cardDragged.setArguments(bundle);

            this.getParentFragmentManager().beginTransaction()
                    .replace(cardFrame.getId(), cardDragged, null)
                    .commit();

            cardFrame.setX(view.getX() -offsetX/2);
            cardFrame.setY(view.getY() -offsetY/2 );

            view.setVisibility(View.INVISIBLE);
            cardFrame.setVisibility(View.VISIBLE);

        } else if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE )) {

            cardFrame.setX(view.getX() + motionEvent.getX()-offsetX);
            cardFrame.setY(view.getY() + motionEvent.getY()-offsetY);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {//se alzi il ditino
            Card card = (Card) view.getTag(R.id.TagCardValue);
            collisionHandler.collision(view, cardFrame, card);
            cardFrame.setVisibility(View.INVISIBLE);
            nCardInTouchingMode=0;
        }
    }

    private boolean cardCollision(float x, float y) {
        View v = this.requireView().findViewById(R.id.backDeckOpponent);
        return x < v.getX() && y < 0;
    }

    public void drawCard(Card card) {
        try {
            playerHand.addCard(card);
            ((TextView) requireView().findViewById(R.id.textNCardDeck)).setText(String.valueOf(--nCardDeckPlayer));
        } catch (FilledHandException e) {
            Toast.makeText(this.getActivity(), getResources().getString(R.string.troppe_carte), Toast.LENGTH_LONG).show();
        }

    }

    public void opponentPlayCard() {
        ((TextView)this.requireView().findViewById(R.id.textNCardOpponentHand)).setText(String.valueOf(--this.nCardHandOpponent));
    }

    public void opponentDrawCard(){
        if(this.nCardHandOpponent<GameplayActivity.MAX_CARD_ON_HAND && this.nCardDeckOpponent>0){

            ((TextView)this.requireView().findViewById(R.id.textNCardOpponentDeck)).setText(String.valueOf(--this.nCardDeckOpponent));
            ((TextView)this.requireView().findViewById(R.id.textNCardOpponentHand)).setText(String.valueOf(++this.nCardHandOpponent));
        }
    }

    public void clearCardPlayed(){
        this.cardPlayed.clearBoard();
    }

    public void incrementStroba(){
        if(totalStroba < 5){
            totalStroba++;
        }
        turnStroba = totalStroba;
        ((TextView) this.requireView().findViewById(R.id.stroba)).setText(String.valueOf(totalStroba));
    }

    public void setOpponentHp(int hp) {
        this.opponentHpView.setText(String.valueOf(hp));
    }

    public void setPlayerHp(int hp) {
        this.playerHpView.setText(String.valueOf(hp));
    }

    public void setBtnPassClickable() {
        requireView().findViewById(R.id.btnPass).setClickable(true);
    }

    private interface CollisionHandler {
        void collision(View view, FrameLayout cardFrame, Card card) ;
    }

    public void setTouchable(boolean onTouchFlag) {
        this.cardPlayedOnTouchFlag = onTouchFlag;
    }


}