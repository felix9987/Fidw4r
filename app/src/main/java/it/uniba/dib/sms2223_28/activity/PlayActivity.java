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
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Objects;

import it.uniba.dib.sms2223_28.BackgroundMusic;
import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.modello.User;
import it.uniba.dib.sms2223_28.vista.TutorialFragment;
import it.uniba.dib.sms2223_28.vista.WaitFragment;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.vista.ViewHeroChoice;
import it.uniba.dib.sms2223_28.vista.FragmentGameResearch;

public class PlayActivity extends AppCompatActivity {
    private static final int MILLIS_TO_START_MATCH = 120000;
    public static final int DELAY_MILLIS = 1000;
    private Integer egyptValue;
    private Integer romanValue;
    private Integer spartanValue;
    private Integer persisValue;
    private int backId,backIdOpponent;
    private FrameLayout frameGameResearch;
    private Integer hero, opponentHero;
    private DatabaseReference databaseReference;
    private LinkedList<String> dbKeys;
    private String key, key2;
    private User opponentUser;
    private String connectionKey;
    private boolean connectionFlag;
    private boolean flag,flag1;
    private FragmentGameResearch fragmentResearch;
    private ViewHeroChoice heroChoiceFragment;
    private boolean otherActStartedFlag;
    private boolean networkFlag;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        try{
            key = this.getIntent().getExtras().getString(Constants.USER_KEY);

        } catch (NullPointerException e){
            e.printStackTrace();
        }

        this.fragmentResearch=null;

        this.heroChoiceFragment =new ViewHeroChoice();
        Bundle bundle=new Bundle();
        bundle.putString(ViewHeroChoice.TEXT_KEY,this.getResources().getString(R.string.chooseYourHero));
        bundle.putBoolean(ViewHeroChoice.TUTORIAL_BUTTON_KEY, true);

        this.heroChoiceFragment.setArguments(bundle);

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.deckChoice, this.heroChoiceFragment)
                .commit();
    }

    @Override
    protected void onStart(){
        super.onStart();
        timer= new CountDownTimer(MILLIS_TO_START_MATCH, MILLIS_TO_START_MATCH) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.connessione_lunga), Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        };
        flag=false;
        flag1=false;
        connectionFlag=false;
        databaseReference= FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();


        this.frameGameResearch =findViewById(R.id.gameResearch);

        BackgroundMusic.setMusicFlag(true);
        BackgroundMusic.start(getApplicationContext(),R.raw.background_music);

        if (key != null) {
            databaseReference.child("Users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    egyptValue = snapshot.child("cardsBackEgypt").getValue(Integer.class);
                    romanValue= snapshot.child("cardsBackRoman").getValue(Integer.class);
                    spartanValue= snapshot.child("cardsBackSpartans").getValue(Integer.class);
                    persisValue= snapshot.child("cardsBackPersis").getValue(Integer.class);

                    int romanWins, persisWins,spartanWins;

                    romanWins = snapshot.child("romanWins").getValue(Integer.class);
                    persisWins = snapshot.child("spartanWins").getValue(Integer.class);
                    spartanWins = snapshot.child("spartanWins").getValue(Integer.class);

                    if(romanWins < Constants.VICTORIES_COUNT ){
                        heroChoiceFragment.setOnClick(false,Constants.PERSIA);
                    }

                    if(persisWins < Constants.VICTORIES_COUNT){
                        heroChoiceFragment.setOnClick(false,Constants.SPARTA);
                    }

                    if(spartanWins < Constants.VICTORIES_COUNT){
                        heroChoiceFragment.setOnClick(false,Constants.EGYPT);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        otherActStartedFlag =false;
        databaseReference.child("Users").child(key).child("status").setValue("online");
    }

    public void egypt(View view){
        this.frameGameResearch.setBackgroundResource(R.drawable.egypt_hero);
        this.hero = Constants.EGYPT;
        this.setResearchFragment();
    }

    public void sparta(View view){
        this.frameGameResearch.setBackgroundResource(R.drawable.sparta_hero);
        this.hero = Constants.SPARTA;
        this.setResearchFragment();
    }

    public void persia(View view){
        this.frameGameResearch.setBackgroundResource(R.drawable.persia_hero);
        this.hero = Constants.PERSIA;
        this.setResearchFragment();
    }

    public void rome(View view){
        this.frameGameResearch.setBackgroundResource(R.drawable.rome_hero);
        this.hero = Constants.ROME;
        this.setResearchFragment();
    }

    private void setResearchFragment(){

        this.fragmentResearch=new FragmentGameResearch();
        Bundle bundle=new Bundle();
        bundle.putInt(FragmentGameResearch.TROPHIES_KEY,MenuGameActivity.playerUser.getTrophies());
        this.fragmentResearch.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.gameResearch, this.fragmentResearch)
                .commit();

        this.heroChoiceFragment.setOnClick(false);
        this.frameGameResearch.setVisibility(View.VISIBLE);
    }


    public void searchMatch(View view) {
        timer.start();
       if(!isWiFiConnected() && !isMobileDataConnected()){
            Toast.makeText(this, getResources().getString(R.string.internet_necessario), Toast.LENGTH_SHORT).show();
           return;
        }
       this.frameGameResearch.setVisibility(View.INVISIBLE);
       FragmentManager fragmentManager=getSupportFragmentManager();

       fragmentManager.beginTransaction()
               .addToBackStack(null)
               .replace(R.id.deckChoice, new WaitFragment())
               .commit();

       switch(hero){

            case Constants.EGYPT:
                backId= egyptValue;
                break;

            case Constants.ROME:
                backId = romanValue;
                break;

            case Constants.SPARTA:
                backId = spartanValue;
                break;

            case Constants.PERSIA:
                backId = persisValue;
                break;

            default:
                return;

       }

        connectionFlag=false;
        dbKeys = new LinkedList<>();

        databaseReference.child("Connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(getSupportFragmentManager().findFragmentById(R.id.deckChoice) instanceof WaitFragment))
                    return;


                if(!flag){

                    for (DataSnapshot connection : snapshot.getChildren()) {
                        for (DataSnapshot player : connection.getChildren()) {

                            //PARTECIPA A UNA CONNESSIONE GIA' ESISTENTE
                            if (connection.getChildrenCount() == 1) {
                                if (!Objects.equals(player.getKey(), key)) {
                                    connectionKey=connection.getKey();
                                    snapshot.child(connectionKey).child(key).child(MenuGameActivity.playerUser.getUsername()).child("hero").getRef().setValue(hero);
                                    connectionFlag = true;
                                }
                            }

                            if (connection.getChildrenCount()  == 2) {
                                databaseReference.child("Connections").child(Objects.requireNonNull(connection.getKey())).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!(getSupportFragmentManager().findFragmentById(R.id.deckChoice) instanceof WaitFragment))
                                            return;


                                        if(!flag){
                                            for (DataSnapshot playerKey : snapshot.getChildren()) {
                                                dbKeys.add(playerKey.getKey());
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        checkNetworkConnection(error);
                                    }
                                });

                                new Handler().postDelayed(() -> {
                                    if (dbKeys.contains(key)) {
                                        for (String string : dbKeys) {
                                            if ( !string.equals(key)) {
                                                key2 = string;
                                                databaseReference.child("Users").child(key2).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                        if (!(getSupportFragmentManager().findFragmentById(R.id.deckChoice) instanceof WaitFragment))
                                                            return;


                                                        if(!flag){

                                                            String email = snapshot1.child("email").getValue(String.class);
                                                            String psw = snapshot1.child("password").getValue(String.class);
                                                            String status = snapshot1.child("status").getValue(String.class);
                                                            Integer actualTrophies = snapshot1.child("trophies").getValue(Integer.class);
                                                            Integer maxTrophies = snapshot1.child("maxTrophies").getValue(Integer.class);
                                                            Integer romanWins = snapshot1.child("romanWins").getValue(Integer.class);
                                                            Integer persisWins = snapshot1.child("persisWins").getValue(Integer.class);
                                                            Integer egyptWins = snapshot1.child("egyptWins").getValue(Integer.class);
                                                            Integer cardsBackRoman = snapshot1.child("cardsBackRoman").getValue(Integer.class);
                                                            Integer cardsBackPersis = snapshot1.child("cardsBackPersis").getValue(Integer.class);
                                                            Integer cardsBackSpartans = snapshot1.child("cardsBackSpartans").getValue(Integer.class);
                                                            Integer cardsBackEgypt = snapshot1.child("cardsBackEgypt").getValue(Integer.class);
                                                            Integer spartanWins = snapshot1.child("spartanWins").getValue(Integer.class);

                                                            String username = snapshot1.child("username").getValue(String.class);
                                                            opponentUser = new User(email,psw,actualTrophies,maxTrophies,username,romanWins,persisWins,egyptWins,spartanWins,cardsBackRoman,cardsBackPersis,cardsBackSpartans,cardsBackEgypt,status);

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        checkNetworkConnection(error);
                                                    }
                                                });


                                                databaseReference.child("Connections").child(Objects.requireNonNull(connection.getKey())).child(key2).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (!(getSupportFragmentManager().findFragmentById(R.id.deckChoice) instanceof WaitFragment))
                                                            return;

                                                        if(!flag){

                                                            try{
                                                                for(DataSnapshot username : snapshot.getChildren()) {
                                                                    for (DataSnapshot hero : username.getChildren()) {
                                                                        opponentHero = hero.getValue(Integer.class);
                                                                    }
                                                                }
                                                            }catch (DatabaseException e){}
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        checkNetworkConnection(error);
                                                    }
                                                });
                                            }
                                        }

                                        networkFlag=true;
                                        databaseReference.child("Connections").child(connectionKey).child(key).child(MenuGameActivity.playerUser.getUsername()).child("networkFlag").setValue(networkFlag);
                                        new Handler().postDelayed(() -> databaseReference.child("Connections").child(connectionKey).child(key2).child(opponentUser.getUsername()).child("networkFlag").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if(!flag1){
                                                    try{
                                                        networkFlag= (boolean) snapshot.getValue();
                                                    }catch(NullPointerException e){
                                                        databaseReference.child("Connections").child(connectionKey).removeValue();
                                                        Toast.makeText(PlayActivity.this, getResources().getString(R.string.problema_connessione), Toast.LENGTH_SHORT).show();
                                                        onBackPressed();
                                                        return;
                                                    }
                                                    if(!networkFlag){
                                                        databaseReference.child("Connections").child(connectionKey).removeValue();
                                                        Toast.makeText(PlayActivity.this, getResources().getString(R.string.problema_connessione), Toast.LENGTH_SHORT).show();
                                                        onBackPressed();
                                                        return;
                                                    }
                                                    if(!isWiFiConnected() && !isMobileDataConnected())
                                                        return;
                                                    otherActStartedFlag =true;


                                                    switch(opponentHero){
                                                        case Constants.EGYPT:

                                                            backIdOpponent= opponentUser.getCardsBackEgypt();
                                                            break;

                                                            case Constants.ROME:

                                                            backIdOpponent = opponentUser.getCardsBackRoman();
                                                            break;

                                                            case Constants.SPARTA:

                                                            backIdOpponent = opponentUser.getCardsBackSpartans();
                                                            break;

                                                        case Constants.PERSIA:

                                                            backIdOpponent = opponentUser.getCardsBackPersis();
                                                            break;

                                                        default:
                                                            return;

                                                    }

                                                    BackgroundMusic.setMusicFlag(false);

                                                    Intent intent = new Intent(getApplicationContext(), GameplayActivity.class);

                                                    intent.putExtra(Constants.HERO_KEY, hero);
                                                    intent.putExtra(Constants.USER2, opponentUser);
                                                    intent.putExtra(Constants.CONNECTION_KEY,connectionKey);
                                                    intent.putExtra(Constants.USER_KEY,key);
                                                    intent.putExtra(Constants.USER_KEY2,key2);
                                                    intent.putExtra(Constants.OPPONENT_HERO_KEY,opponentHero);
                                                    intent.putExtra(GameplayActivity.BACK_ID_KEY,backId);
                                                    intent.putExtra(GameplayActivity.BACK_ID_KEY2,backIdOpponent);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);

                                                    flag=true;
                                                    finish();
                                                    timer.cancel();
                                                    flag1=true;
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        }), DELAY_MILLIS);

                                    }
                                }, DELAY_MILLIS);
                            }

                        }
                    }
                    //CREA LA CONNESSIONE
                    if (!connectionFlag) {
                        connectionKey = String.valueOf(System.currentTimeMillis());
                        snapshot.child(connectionKey).child(key).child(MenuGameActivity.playerUser.getUsername()).child("hero").getRef().setValue(hero);

                        connectionFlag=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                checkNetworkConnection(error);
            }
        });
    }

    public void tutorial(View view){
        findViewById(R.id.gameResearch).setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.animator.slide_in_animator,R.animator.slide_out_animator)
                .replace(R.id.deckChoice, new TutorialFragment())
                .commit();
    }

    private void checkNetworkConnection(@NonNull DatabaseError error) {
        if(error.getCode()==DatabaseError.NETWORK_ERROR){
            Toast.makeText(PlayActivity.this, getResources().getString(R.string.rete_inaccessibile), Toast.LENGTH_SHORT).show();

        } else{
            Toast.makeText(PlayActivity.this, getResources().getString(R.string.errore_ricerca), Toast.LENGTH_SHORT).show();
            Toast.makeText(PlayActivity.this, getResources().getString(R.string.riprova), Toast.LENGTH_SHORT).show();
        }

        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        if(this.frameGameResearch.getVisibility() == View.VISIBLE ){
            this.frameGameResearch.setVisibility(View.INVISIBLE);
            this.heroChoiceFragment.setOnClick(true);
        }
        else if (this.getSupportFragmentManager().findFragmentById(R.id.deckChoice) instanceof WaitFragment) {
            try {
                databaseReference.child("Connections").child(connectionKey).child(key).removeValue();
            }catch (NullPointerException e) {

            }
            super.onBackPressed();

        } else {
            BackgroundMusic.setMusicFlag(false);
            otherActStartedFlag =true;
            this.finish();
        }

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

    @Override
    protected void  onStop() {
        super.onStop();
        if(BackgroundMusic.isMusicFlag())
            BackgroundMusic.stop();

        if (!otherActStartedFlag)
            databaseReference.child("Users").child(key).child("status").setValue("offline");
    }

}