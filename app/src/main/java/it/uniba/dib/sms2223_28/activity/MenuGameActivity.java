package it.uniba.dib.sms2223_28.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.*;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import it.uniba.dib.sms2223_28.BackgroundMusic;
import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.vista.GifFragment;
import it.uniba.dib.sms2223_28.vista.RankingFragment;
import it.uniba.dib.sms2223_28.modello.User;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.vista.AchievementsFragment;
import it.uniba.dib.sms2223_28.vista.SettingsFragment;
import it.uniba.dib.sms2223_28.vista.MenuFragment;

public class MenuGameActivity extends AppCompatActivity {
    public static User playerUser;
    private String key;
    DatabaseReference databaseReference;
    private boolean otherActStartedFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        Fade fade=new Fade(Fade.OUT);
        fade.setInterpolator(new DecelerateInterpolator());
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(fade);

        try{
            Bundle extra=this.getIntent().getExtras();
            playerUser = (User) Objects.requireNonNull(extra).getSerializable(Constants.USER);
            key =  extra.getString(Constants.USER_KEY);
        } catch (NullPointerException e) {
            playerUser=null;
            key=null;
        }

        setContentView(R.layout.activity_menu_game);

        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle=new Bundle();

        bundle.putBoolean(MenuFragment.FLAG_BLOCK_PLAY_KEY, playerUser == null);
        menuFragment.setArguments(bundle);

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.menuGameView, menuFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        BackgroundMusic.start(getApplicationContext(),R.raw.background_music);
        BackgroundMusic.setMusicFlag(false);

        if(playerUser!=null)
            databaseReference.child("Users").child(key).child("status").setValue("online");
    }

    @Override
    protected void onResume() {
        super.onResume();
        otherActStartedFlag =false;

        if (key!=null)
            databaseReference.child("Users").child(key).child("status").setValue("online");
    }

    public void collection(View view){
        otherActStartedFlag =true;
        BackgroundMusic.setMusicFlag(true);

        Intent intent = new Intent(this, CollectionActivity.class);
        intent.putExtra(Constants.USER_KEY,key);
        intent.putExtra(Constants.USER,playerUser);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        this.overridePendingTransition(R.animator.slide_in_animator,R.animator.slide_out_animator);
    }
    public void startGame(View view){
        if(playerUser!=null) {
            otherActStartedFlag =true;
            BackgroundMusic.setMusicFlag(true);

            Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
            intent.putExtra(Constants.USER_KEY, key);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        } else {
            Toast.makeText(this, getResources().getString(R.string.ospite_negato), Toast.LENGTH_LONG).show();
            Toast.makeText(this, getResources().getString(R.string.usare_account), Toast.LENGTH_LONG).show();
        }
    }

    public void settings (View view) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.menuGameView, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void achievements(View view) {

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.menuGameView, new GifFragment())
                .setCustomAnimations(R.animator.slide_in_animator,R.animator.slide_out_animator)
                .add(R.id.menuGameView, new AchievementsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void ranking(View view){

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.menuGameView,new GifFragment())
                .add(R.id.menuGameView, new RankingFragment())
                .addToBackStack(null).
                commit();
    }

    @Override
    protected void  onStop() {
        super.onStop();
        if(!BackgroundMusic.isMusicFlag())
            BackgroundMusic.stop();

        if (!otherActStartedFlag && key!=null)
            databaseReference.child("Users").child(key).child("status").setValue("offline");
    }

}