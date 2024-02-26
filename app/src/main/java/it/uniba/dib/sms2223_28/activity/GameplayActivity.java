package it.uniba.dib.sms2223_28.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

import it.uniba.dib.sms2223_28.modello.User;
import it.uniba.dib.sms2223_28.modello.Card;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.Effect;
import it.uniba.dib.sms2223_28.modello.Egypt;
import it.uniba.dib.sms2223_28.modello.GenericDeck;
import it.uniba.dib.sms2223_28.modello.Persia;
import it.uniba.dib.sms2223_28.modello.Rome;
import it.uniba.dib.sms2223_28.modello.Sparta;
import it.uniba.dib.sms2223_28.BackgroundMusic;
import it.uniba.dib.sms2223_28.vista.BattleFragment;
import it.uniba.dib.sms2223_28.vista.GameplayFragment;
import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.vista.GifFragment;
import it.uniba.dib.sms2223_28.vista.ResultFragment;

public class GameplayActivity extends AppCompatActivity {
    public static final int MAX_CARD_ON_HAND = 5;
    private static final String FLAG1 = "flag1";
    private static final String CARD_FLAG = "cardFlag";
    private static final String PASS_TURN = "passTurn";
    private static final String INITIAL_TROPHIES_KEY = "initial trophies";
    public static final int TIME_AFTER_TURN_TO_WIN_FOR_SURRENDER = 20;
    public static final int MAX_MILLISECOND_TO_PLAY = 60000;
    public static final String BACK_ID_KEY = "back id key for player" ;
    public static final String BACK_ID_KEY2 = "back id key for opponent";
    public static final String BATTLE_FRAGMENT_TAG = "gameplayActivity tag for battleFragment";
    private int turn;
    private int opponentHero;
    private int playerHero;
    private GenericDeck deck;
    private int heroPlayerImage;
    private int heroOpponentImage;
    private GameplayFragment gameplayFragment;
    private BattleFragment battleFragment;
    private User opponentUser;
    boolean passTurn;
    boolean flag1, cardFlag;
    private final Handler handler= new Handler();
    private DatabaseReference databaseReference;
    private String connectionKey, key, key2;
    private ArrayList<Card> playedCards;
    private int playerChampionImageId;
    private int opponentChampionImageId;
    private ArrayList<Card> opponentCards;
    private LinkedList<Effect> effectList;
    private int countOpponentCards;
    private int playerHp,opponentHp;
    private CountDownTimer timer;
    private CountDownTimer timeForOpponentToPLay;
    private CountDownTimer timeForPlayerToPlay;
    private int initialTrophies;
    private boolean isActivityInOnStop;
    private boolean flag;
    private boolean isGameFinished;
    private int count;
    private int backId,backIdOpponent;
    private ImageView cardsBack;
    private ImageView cardsBackOpponent;


    private final ValueEventListener eventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(cardFlag){
                return;
            }

            if (!isGameFinished) {
                try {
                    passTurn = snapshot.child("endTurn").getValue(boolean.class);
                } catch (NullPointerException e) {
                    databaseReference.child("Connections").child(connectionKey).removeValue();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.avversario_ritirato), Toast.LENGTH_LONG).show();
                    endGame(1);
                    return;
                }
            }

            if(!passTurn){
                if (!flag) {
                    flag=true;
                    Toast.makeText(GameplayActivity.this, getResources().getString(R.string.attesa_avversario), Toast.LENGTH_LONG).show();

                    count=0;
                    int time = (gameplayFragment.getRemainingTime() + TIME_AFTER_TURN_TO_WIN_FOR_SURRENDER) * 1000;
                    timeForOpponentToPLay=  new CountDownTimer(time,(gameplayFragment.getRemainingTime()+2)*1000) {
                        @Override
                        public void onTick(long l) {
                            if(count==1){
                                Toast.makeText(GameplayActivity.this, getResources().getString(R.string.avversario_disconnesso), Toast.LENGTH_SHORT).show();
                                Toast.makeText(GameplayActivity.this, getResources().getString(R.string.attesa_riconnessione), Toast.LENGTH_SHORT).show();
                            }
                            count++;
                        }

                        @Override
                        public void onFinish() {
                            if (isMobileDataConnected() || isWiFiConnected()){
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.connesione_persa_avversario), Toast.LENGTH_LONG).show();
                                endGame(1);
                                databaseReference.child("Connections").child(connectionKey).removeValue();

                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connesione_persa_giocatore), Toast.LENGTH_LONG).show();
                                endGame(0);
                                databaseReference.child("Connections").child(connectionKey).child(key).removeValue();
                            }
                        }
                    };
                    timeForOpponentToPLay.start();
                }
            }else{
                cardFlag =true;
                if (timeForOpponentToPLay!=null)
                    timeForOpponentToPLay.cancel();

                for(DataSnapshot card: snapshot.child("Cards").getChildren()) {
                    try {
                        Integer attack = card.child("attack").getValue(Integer.class);
                        Integer damage = card.child("damage").getValue(Integer.class);
                        Integer description = card.child("description").getValue(Integer.class);
                        Integer draw = card.child("draw").getValue(Integer.class);
                        Integer effect = card.child("effect").getValue(Integer.class);

                        effectList = new LinkedList<>();
                        for (DataSnapshot effects : card.child("effects").getChildren()) {
                            Effect eff = effects.getValue(Effect.class);
                            effectList.add(eff);
                        }

                        Integer idDrawable = card.child("idDrawable").getValue(Integer.class);
                        Integer name = card.child("name").getValue(Integer.class);
                        Integer stroba = card.child("stroba").getValue(Integer.class);

                        Card card1 = new Card(stroba, attack, name, effect, idDrawable, description, damage, draw, effectList);
                        opponentCards.add(card1);
                        countOpponentCards++;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                Bundle bundle=new Bundle();
                bundle.putSerializable(BattleFragment.CARDS_PLAYER, gameplayFragment.getPlayedCardList());
                bundle.putSerializable(BattleFragment.CARDS_OPPONENT,opponentCards);
                bundle.putInt(BattleFragment.HP_PLAYER,playerHp);
                bundle.putInt(BattleFragment.HP_OPPONENT,opponentHp);
                battleFragment.setUpValues(bundle);

                if(!getSupportFragmentManager().isDestroyed() && !isActivityInOnStop){
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.frameActivityGameplay, battleFragment, BATTLE_FRAGMENT_TAG)
                            .commit();

                    handler.postDelayed(() -> {
                        battleFragment.effectResolver();

                        opponentHp=battleFragment.getHpOpponent();
                        playerHp=battleFragment.getHpPlayer();
                        gameplayFragment.setOpponentHp(opponentHp);
                        gameplayFragment.setPlayerHp(playerHp);
                    }, 1000);

                    handler.postDelayed(() -> {
                        battleFragment.setBattleButtonClickable(true);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        if(!fragmentManager.isDestroyed()){

                            if (battleFragment.getPlayerChampion()) {
                                findViewById(R.id.championImage).setVisibility(View.VISIBLE);
                                cardsBack.setImageResource(backId);
                            }


                            if (battleFragment.getOpponentChampion()) {
                                findViewById(R.id.opponentChampionImage).setVisibility(View.VISIBLE);
                                cardsBackOpponent.setImageResource(backIdOpponent);
                            }

                            if(!flag1)
                                cleanupAfterBattle();

                            snapshot.child("Cards").getRef().removeValue();

                            for(int i=0; i<battleFragment.getPlayerCardToDraw()+1; i++)
                                if (deck.getNCarte() != 0)
                                    gameplayFragment.drawCard(deck.topdeck());

                            for (int i = 0; i < countOpponentCards; i++)
                                gameplayFragment.opponentPlayCard();

                            for(int i=0; i<battleFragment.getOpponentCardToDraw()+1; i++)
                                gameplayFragment.opponentDrawCard();

                            opponentCards= new ArrayList<>();
                            playedCards= new ArrayList<>();
                            effectList=new LinkedList<>();

                            gameplayFragment.clearCardPlayed();
                            ((TextView)findViewById(R.id.turnTextView)).setText(getResources().getString(R.string.turn) + ++turn);
                            gameplayFragment.incrementStroba();
                            gameplayFragment.setTouchable(true);
                            timer.start();
                        }
                    }, 2000);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void endBattle(View view) {

        getSupportFragmentManager().beginTransaction()
                .remove(battleFragment)
                .commit();

        winHandler();

        battleFragment.setBattleButtonClickable(false);
        gameplayFragment.setBtnPassClickable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        this.isActivityInOnStop=false;
        playerHp = opponentHp = 20;
        turn = 1;
        isGameFinished=false;
        effectList = new LinkedList<>();
        if (savedInstanceState == null) {
            savedInstanceState = this.getIntent().getExtras();
            //DA QUI
            playerHero = savedInstanceState.getInt(Constants.HERO_KEY);
            opponentHero = savedInstanceState.getInt(Constants.OPPONENT_HERO_KEY);
            opponentUser = (User) savedInstanceState.getSerializable(Constants.USER2);
            connectionKey = savedInstanceState.getString(Constants.CONNECTION_KEY);
            key = savedInstanceState.getString(Constants.USER_KEY);
            key2 = savedInstanceState.getString(Constants.USER_KEY2);
            initialTrophies = MenuGameActivity.playerUser.getTrophies();
            backId = savedInstanceState.getInt(BACK_ID_KEY);
            backIdOpponent = savedInstanceState.getInt(BACK_ID_KEY2);

            gameplayFragment = new GameplayFragment();
            if (!setDeck() || !setOpponentHeroImage())
                throw new IllegalArgumentException();

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.USER2, opponentUser);
            bundle.putInt(GameplayFragment.HERO_KEY, playerHero);
            bundle.putSerializable(GameplayFragment.DECK_KEY, deck);

            bundle.putInt(GameplayFragment.HERO_PLAYER_KEY, heroPlayerImage);
            bundle.putInt(GameplayFragment.HERO_OPPONENT_KEY, heroOpponentImage);
            bundle.putInt(GameplayFragment.CHAMPION_PLAYER_KEY, playerChampionImageId);
            bundle.putInt(GameplayFragment.CHAMPION_OPPONENT_KEY, opponentChampionImageId);

            gameplayFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameActivityGameplay, gameplayFragment)
                    .commit();


        } else {
            turn = savedInstanceState.getInt(Constants.TURN);
            playerHp = savedInstanceState.getInt(Constants.PLAYER_HP);
            opponentHp = savedInstanceState.getInt(Constants.OPPONENT_HP);
            playedCards = (ArrayList<Card>) savedInstanceState.getSerializable(Constants.PLAYED_CARDS);
            deck = (GenericDeck) savedInstanceState.getSerializable(Constants.DECK);
            countOpponentCards = savedInstanceState.getInt(Constants.COUNT_OPPONENT_CARDS);
            flag1 = savedInstanceState.getBoolean(FLAG1);
            cardFlag = savedInstanceState.getBoolean(CARD_FLAG);
            passTurn = savedInstanceState.getBoolean(PASS_TURN);

            playerHero = savedInstanceState.getInt(Constants.HERO_KEY);
            opponentHero = savedInstanceState.getInt(Constants.OPPONENT_HERO_KEY);
            MenuGameActivity.playerUser = (User) savedInstanceState.getSerializable(Constants.USER);
            opponentUser = (User) savedInstanceState.getSerializable(Constants.USER2);
            connectionKey = savedInstanceState.getString(Constants.CONNECTION_KEY);
            key = savedInstanceState.getString(Constants.USER_KEY);
            key2 = savedInstanceState.getString(Constants.USER_KEY2);
            initialTrophies =savedInstanceState.getInt(INITIAL_TROPHIES_KEY);
            backId = savedInstanceState.getInt(BACK_ID_KEY);
            backIdOpponent = savedInstanceState.getInt(BACK_ID_KEY2);

        }

    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseReference= FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        databaseReference.child("Connections").child(connectionKey).child(key).child(MenuGameActivity.playerUser.getUsername()).child("endTurn").getRef().setValue(false);
        databaseReference.child("Connections").child(connectionKey).child(key2).child(opponentUser.getUsername()).child("endTurn").getRef().setValue(false);

        if(this.gameplayFragment==null) {
            this.gameplayFragment= (GameplayFragment) this.getSupportFragmentManager().findFragmentById(R.id.frameActivityGameplay);
            this.battleFragment= (BattleFragment) this.getSupportFragmentManager().findFragmentByTag(BATTLE_FRAGMENT_TAG);

        } else {
            battleFragment=new BattleFragment();
            Bundle bundle= new Bundle();
            bundle.putString(BattleFragment.OPPONENT_USERNAME_KEY,opponentUser.getUsername());
            bundle.putString(BattleFragment.PLAYER_USERNAME_KEY,MenuGameActivity.playerUser.getUsername());
            bundle.putLong(Constants.CONNECTION_KEY,Long.parseLong(connectionKey));
            bundle.putInt(BattleFragment.HERO_PLAYER,playerHero);
            bundle.putInt(BattleFragment.HERO_OPPONENT,opponentHero);
            battleFragment.setArguments(bundle);
        }


        if (gameplayFragment.getRemainingTime()==60) {
            timer = new CountDownTimer(MAX_MILLISECOND_TO_PLAY, 1000) {

                public void onTick(long millisUntilFinished) {
                    gameplayFragment.setTimer((int) millisUntilFinished / 1000);
                }

                public void onFinish() {
                    endTurn(findViewById(R.id.btnPass));
                }
            };
            timer.start();
        }


        cardsBack = findViewById(R.id.backDeckPlayer);
        cardsBack.setImageResource(backId);
        cardsBackOpponent = findViewById(R.id.backDeckOpponent);
        cardsBackOpponent.setImageResource(backIdOpponent);

        BackgroundMusic.stop();
        BackgroundMusic.setMusicFlag(false);
        BackgroundMusic.start(getApplicationContext(),R.raw.battle_music);

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("Users").child(key).child("status").setValue("online");
        isActivityInOnStop=false;
        try {
            if (battleFragment.getPlayerChampion())
                findViewById(R.id.championImage).setVisibility(View.VISIBLE);

            if (battleFragment.getOpponentChampion())
                findViewById(R.id.opponentChampionImage).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {

        }


        if (MenuGameActivity.playerUser.getTrophies()!= initialTrophies) {

            MenuGameActivity.playerUser.setTrophies(initialTrophies);
            databaseReference.child("Users").child(key).setValue(MenuGameActivity.playerUser);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Salva le informazioni di stato nel Bundle
        outState.putInt(Constants.HERO_KEY, playerHero);
        outState.putInt(Constants.OPPONENT_HERO_KEY, opponentHero);
        outState.putSerializable(Constants.USER, MenuGameActivity.playerUser);
        outState.putSerializable(Constants.USER2, opponentUser);
        outState.putString(Constants.CONNECTION_KEY, connectionKey);
        outState.putString(Constants.USER_KEY, key);
        outState.putString(Constants.USER_KEY2, key2);
        //nuovi inseriti
        outState.putInt(Constants.TURN, turn);
        outState.putSerializable(Constants.PLAYED_CARDS, playedCards);
        outState.putInt(Constants.PLAYER_HP, playerHp);
        outState.putInt(Constants.OPPONENT_HP, opponentHp);

        outState.putSerializable(Constants.DECK, deck);
        outState.putSerializable(Constants.COUNT_OPPONENT_CARDS, countOpponentCards);
        outState.putBoolean(FLAG1, flag1);
        outState.putBoolean(CARD_FLAG, cardFlag);
        outState.putBoolean(PASS_TURN, passTurn);
        outState.putInt(INITIAL_TROPHIES_KEY, initialTrophies);
    }


    private boolean setOpponentHeroImage() {
        switch (opponentHero){
            case Constants.EGYPT:
                heroOpponentImage = R.drawable.egypt_hero;
                opponentChampionImageId =R.drawable.egypt_champ;
                break;
            case Constants.SPARTA:
                heroOpponentImage = R.drawable.sparta_hero;
                opponentChampionImageId =R.drawable.sparta_champ;
                break;
            case Constants.PERSIA:
                heroOpponentImage = R.drawable.persia_hero;
                opponentChampionImageId =R.drawable.persia_champ;
                break;

            case Constants.ROME:
                heroOpponentImage = R.drawable.rome_hero;
                opponentChampionImageId =R.drawable.rome_champ;
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setDeck() {
        switch (playerHero){
            case Constants.EGYPT:
                deck = new Egypt();
                heroPlayerImage = R.drawable.egypt_hero;
                playerChampionImageId=R.drawable.egypt_champ;
                break;
            case Constants.SPARTA:
                deck = new Sparta();
                heroPlayerImage = R.drawable.sparta_hero;
                playerChampionImageId=R.drawable.sparta_champ;
                break;
            case Constants.PERSIA:
                deck = new Persia();
                heroPlayerImage = R.drawable.persia_hero;
                playerChampionImageId=R.drawable.persia_champ;
                break;

            case Constants.ROME:
                deck = new Rome();
                heroPlayerImage = R.drawable.rome_hero;
                playerChampionImageId=R.drawable.rome_champ;
                break;
            default:
                return false;
        }
        return true;
    }

    public void endTurn(View view) {
        view.setClickable(false);
        if(!isWiFiConnected() && !isMobileDataConnected()){
            if(timeForPlayerToPlay!=null) {
                timeForPlayerToPlay.cancel();
                view.setClickable(true);
            }
            int time= (gameplayFragment.getRemainingTime()+TIME_AFTER_TURN_TO_WIN_FOR_SURRENDER)*1000;
            if(time<TIME_AFTER_TURN_TO_WIN_FOR_SURRENDER)
                return;
            timeForPlayerToPlay= new CountDownTimer(time, time) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    endGame(0);
                    databaseReference.child("Connections").child(connectionKey).removeValue();
                }
            };
            timeForPlayerToPlay.start();
            Toast.makeText(this, getResources().getString(R.string.wifi_interrotto), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.ti_rimangono)+ (gameplayFragment.getRemainingTime()+ TIME_AFTER_TURN_TO_WIN_FOR_SURRENDER) + getResources().getString(R.string.secondi), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.partita_persa), Toast.LENGTH_SHORT).show();
            return;
        }

        if(timeForPlayerToPlay!=null)
            timeForPlayerToPlay.cancel();

        this.gameplayFragment.setTouchable(false);
        timer.cancel();
        playedCards= gameplayFragment.getPlayedCardList();

        writeCards(0);

        opponentCards = new ArrayList<>();
        countOpponentCards=0;
        flag1=false;
        cardFlag =false;
        flag=false;
        timeForOpponentToPLay=null;

        databaseReference.child("Connections").child(connectionKey).child(key2).child(this.opponentUser.getUsername()).addValueEventListener(this.eventListener);
    }

    private void writeCards(int i) {
        DatabaseReference snapshot=databaseReference.child("Connections").child(connectionKey).child(key).child(MenuGameActivity.playerUser.getUsername());

        if (i == playedCards.size()) {
            // Tutte le carte sono state scritte ,quindi imposta "endTurn" come vero sul db
            snapshot.child("endTurn").setValue(true);
            return;
        }

        //scrivi la carta di posizione i sul db
        snapshot.child("Cards").child(String.valueOf(i)).setValue(playedCards.get(i))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // L'operazione di scrittura è stata completata con successo
                        // Scrivi la prossima carta nel database
                        writeCards(i+1);
                    }
                });
    }

    private void winHandler() {

        if(turn==13 || opponentHp<=0 ||  playerHp<=0){
            databaseReference.child("Connections").child(connectionKey).removeValue();
            timer.cancel();
            if (playerHp <= 0 && opponentHp > 0 || (turn == 13 && opponentHp > playerHp))
                endGame(0);

            else if (opponentHp == playerHp || (playerHp<=0) )
                endGame(0.5f);

            else endGame(1);
        }
    }

    private void endGame(float gameResult){

        if(gameResult == 1){
            switch (playerHero){

                case Constants.EGYPT:

                    MenuGameActivity.playerUser.incrementEgyptWins();
                    databaseReference.child("Users").child(key).child("egyptWins").setValue(MenuGameActivity.playerUser.getEgyptWins());
                    break;

                case Constants.ROME:
                    MenuGameActivity.playerUser.incrementRomeWins();
                    databaseReference.child("Users").child(key).child("romeWins").setValue(MenuGameActivity.playerUser.getRomanWins());
                    break;


                case Constants.PERSIA:
                    MenuGameActivity.playerUser.incrementPersisWins();
                    databaseReference.child("Users").child(key).child("persisWins").setValue(MenuGameActivity.playerUser.getPersisWins());
                    break;


                case Constants.SPARTA:
                    MenuGameActivity.playerUser.incrementSpartansWins();
                    databaseReference.child("Users").child(key).child("spartansWins").setValue(MenuGameActivity.playerUser.getSpartanWins());
                    break;
            }

        }

        timer.cancel();
        isGameFinished=true;
        int trophiesEarned=this.trophiesCalculation(gameResult);
        ResultFragment resultFragment=new ResultFragment();
        Bundle bundle=new Bundle();

        bundle.putInt(ResultFragment.EARNED_TROPHIES_KEY,trophiesEarned);
        bundle.putInt(ResultFragment.INITIAL_TROPHIES_KEY,MenuGameActivity.playerUser.getTrophies());
        bundle.putFloat(ResultFragment.GAME_RESULT_KEY,gameResult);
        resultFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(!fragmentManager.isDestroyed()) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameActivityGameplay, new GifFragment())
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.frameActivityGameplay, resultFragment)
                    .commit();

            updateTrophiesOnDb(trophiesEarned);
        }
    }

    private void updateTrophiesOnDb(int trophiesEarned) {
        int newTrophies=MenuGameActivity.playerUser.getTrophies() + trophiesEarned;
        MenuGameActivity.playerUser.setTrophies(newTrophies);
        if(newTrophies>MenuGameActivity.playerUser.getMaxTrophies()) MenuGameActivity.playerUser.setMaxTrophies(newTrophies);
        databaseReference.child("Users").child(key).setValue(MenuGameActivity.playerUser);
    }

    private int trophiesCalculation(float playerHasWin){

        if(playerHasWin!=0 && playerHasWin!=0.5f && playerHasWin!=1) throw new IllegalArgumentException();
        return (int) (40*(playerHasWin-1f/(1+Math.pow(10,((opponentUser.getTrophies()-MenuGameActivity.playerUser.getTrophies())/400f)))));
    }

    private void cleanupAfterBattle() {
        databaseReference.child("Connections").child(connectionKey).child(key).child(MenuGameActivity.playerUser.getUsername()).child("endTurn").getRef().setValue(false);
        databaseReference.child("Connections").child(connectionKey).child(key2).child(opponentUser.getUsername()).child("endTurn").getRef().setValue(false);
    }

    public void goToMenu(View view) {
        BackgroundMusic.stop();
        Intent intent = new Intent(getApplicationContext(), MenuGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.USER_KEY, key);
        intent.putExtra(Constants.USER, MenuGameActivity.playerUser);
        startActivity(intent);
        this.finish();
    }

    public void surrend(View view){
        databaseReference.child("Connections").child(connectionKey).child(key).removeValue();
        endGame(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            timer.cancel();
            timeForOpponentToPLay.cancel();

        } catch (NullPointerException e) {}
        databaseReference.child("Users").child(key).child("status").setValue("offline");

        this.isActivityInOnStop=true;

        if(!this.isGameFinished)
            updateTrophiesOnDb(trophiesCalculation(0));


        if( turn!=13 && playerHp>0 &&  opponentHp>0) {
            updateTrophiesOnDb(this.trophiesCalculation(0));
        }
    }


    @Override
    public void onBackPressed() {
        if (this.getSupportFragmentManager().findFragmentById(R.id.frameActivityGameplay) instanceof ResultFragment)
            this.goToMenu(null);
        else
            super.onBackPressed();
    }

    private boolean isWiFiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
        }
        return false;
    }

    private boolean isMobileDataConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
        }
        return false;
    }

}