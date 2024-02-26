package it.uniba.dib.sms2223_28.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.dib.sms2223_28.BackgroundMusic;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.Egypt;
import it.uniba.dib.sms2223_28.modello.GenericDeck;
import it.uniba.dib.sms2223_28.modello.Persia;
import it.uniba.dib.sms2223_28.modello.Rome;
import it.uniba.dib.sms2223_28.modello.Sparta;
import it.uniba.dib.sms2223_28.vista.CollectionFragment;
import it.uniba.dib.sms2223_28.vista.CollectionSingleCardFragment;
import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.modello.Card;
import it.uniba.dib.sms2223_28.vista.CardLayoutFragment;
import it.uniba.dib.sms2223_28.vista.GifFragment;
import it.uniba.dib.sms2223_28.vista.ViewHeroChoice;

public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DECK_KEY = "deck";
    private String key;
    private Integer hero;
    private GenericDeck deck;
    private Integer cardPosition;
    private ViewHeroChoice viewHeroChoice;
    public boolean flag;
    private DatabaseReference databaseReference;
    private boolean otherActStartedFlag;
    private CollectionSingleCardFragment collectionSingleCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        key = this.getIntent().getExtras().getString(Constants.USER_KEY);

        setContentView(R.layout.activity_collection);
        if (key!=null)
            databaseReference= FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        cardPosition=0;
        this.hero =null;
        if(savedInstanceState!=null)
            this.hero = savedInstanceState.getInt(Constants.HERO_KEY);
        flag=false;

        collectionSingleCardFragment=null;
    }

    @Override
    public void onStart(){
        super.onStart();

        BackgroundMusic.setMusicFlag(true);
        BackgroundMusic.start(getApplicationContext(),R.raw.background_music);

        if (this.hero ==null)
            this.heroChoiceFragmentCall();
    }

    @Override
    protected void onResume() {
        super.onResume();

        otherActStartedFlag=true;

        if (key!=null)
            databaseReference.child("Users").child(key).child("status").setValue("online");
    }

    private void heroChoiceFragmentCall() {
        this.viewHeroChoice =new ViewHeroChoice();
        Bundle bundle=new Bundle();

        bundle.putString(ViewHeroChoice.TEXT_KEY, this.getResources().getString(R.string.collection_text_herochoice));
        bundle.putBoolean(ViewHeroChoice.TUTORIAL_BUTTON_KEY, false);
        this.viewHeroChoice.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_animator,R.animator.slide_out_animator)
                .replace(R.id.collectionFrame, this.viewHeroChoice)
                .commit();
    }

    public void rome(View view){
        this.hero =Constants.ROME;
        this.deck=new Rome();
        this.viewHeroChoice.setOnClick(false);
        this.collectionFragmentCall();
    }

    public void persia(View view){
        this.hero =Constants.PERSIA;
        this.deck=new Persia();
        this.viewHeroChoice.setOnClick(false);
        this.collectionFragmentCall();
    }

    public void sparta(View view){
        this.hero =Constants.SPARTA;
        this.deck=new Sparta();
        this.viewHeroChoice.setOnClick(false);
        this.collectionFragmentCall();
    }

    public void egypt(View view){
        this.hero =Constants.EGYPT;
        this.deck=new Egypt();
        this.viewHeroChoice.setOnClick(false);
        this.collectionFragmentCall();
    }

    private void collectionFragmentCall() {
        CollectionFragment collectionFragment = new CollectionFragment();
        Bundle bundle=new Bundle();

        bundle.putInt(Constants.HERO_KEY,this.hero);
        bundle.putSerializable(DECK_KEY,this.deck);
        bundle.putString(Constants.USER_KEY,this.key);
        collectionFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.collectionFrame, new GifFragment());
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_animator,R.animator.slide_out_animator);
        fragmentTransaction.add(R.id.collectionFrame, collectionFragment);
        fragmentTransaction.commit();

        try {
            viewHeroChoice.setVisibility(View.INVISIBLE);
            if (collectionSingleCardFragment!=null)
                collectionSingleCardFragment.setVisibility(View.INVISIBLE);

        } catch (Exception e) {}

    }

    @Override
    public void onClick(View view){
        this.cardPosition=(int)view.getTag(R.id.TagCardPositionValue);

        collectionSingleCardFragment=new CollectionSingleCardFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable(CollectionSingleCardFragment.CARD_KEY,this.deck.getCardByPosition(this.cardPosition));
        bundle.putFloat(CardLayoutFragment.CARD_HEIGHT_KEY, this.getResources().getDimension(R.dimen.big_card_height_collection));
        collectionSingleCardFragment.setArguments(bundle);

        this.getSupportFragmentManager().beginTransaction().
                addSharedElement(view,"trans").
                replace(R.id.collectionFrame, collectionSingleCardFragment).
                addToBackStack(null).
                commit();
    }

    public void next(View view){
        CollectionSingleCardFragment fragment=new CollectionSingleCardFragment();
        Bundle bundle=new Bundle();
        this.cardPosition=this.findNextCardPosition();

        bundle.putSerializable(CollectionSingleCardFragment.CARD_KEY, this.deck.getCardByPosition(this.cardPosition));
        bundle.putFloat(CardLayoutFragment.CARD_HEIGHT_KEY, this.getResources().getDimension(R.dimen.big_card_height_collection));
        fragment.setArguments(bundle);

        replaceSingleCard(fragment);
    }

    private Integer findNextCardPosition() {
        Card currentCard=this.deck.getCardByPosition(this.cardPosition);
        for(int i = this.cardPosition+1; i<this.deck.getNCarte(); i++){
            if(!currentCard.equals(this.deck.getCardByPosition(i)))
                return i;
        }
        return 0;
    }

    public void previous(View view){
        CollectionSingleCardFragment fragment=new CollectionSingleCardFragment();
        Bundle bundle=new Bundle();
        this.cardPosition=findPreviousCardPosition();

        bundle.putSerializable(CollectionSingleCardFragment.CARD_KEY,this.deck.getCardByPosition((this.cardPosition)));
        bundle.putFloat(CardLayoutFragment.CARD_HEIGHT_KEY, this.getResources().getDimension(R.dimen.big_card_height_collection));
        fragment.setArguments(bundle);

        replaceSingleCard(fragment);
    }

    private Integer findPreviousCardPosition() {
        Card currentCard=this.deck.getCardByPosition(this.cardPosition);
        for(int i=this.cardPosition-1; i>0; i--){
            if(!currentCard.equals(this.deck.getCardByPosition(i)))
                return i;
        }
        return this.deck.getNCarte()-1;
    }

    private void replaceSingleCard(CollectionSingleCardFragment fragment) {
        this.getSupportFragmentManager().beginTransaction().
                replace(R.id.collectionFrame, fragment).
                commit();
    }

    @Override
    public void onBackPressed(){
        if (this.getSupportFragmentManager().findFragmentById(R.id.collectionFrame) instanceof ViewHeroChoice) {
            otherActStartedFlag = false;
            BackgroundMusic.setMusicFlag(false);
            this.finish();

        } //else if (this.getSupportFragmentManager().findFragmentById(R.id.collectionFrame) instanceof CollectionFragment)
          //  heroChoiceFragmentCall();

        else
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DECK_KEY,this.hero);
    }

    @Override
    protected void  onStop() {
        super.onStop();

        if(BackgroundMusic.isMusicFlag())
            BackgroundMusic.stop();

        if(otherActStartedFlag && key!=null)
            databaseReference.child("Users").child(key).child("status").setValue("offline");
    }
}
