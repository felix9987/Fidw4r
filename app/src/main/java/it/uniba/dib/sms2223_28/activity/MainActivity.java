package it.uniba.dib.sms2223_28.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.vista.CreditsFragment;
import it.uniba.dib.sms2223_28.vista.WelcomeFragment;
import it.uniba.dib.sms2223_28.vista.SestrobaFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(+AppCompatDelegate.MODE_NIGHT_NO);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activityMainView, new SestrobaFragment())
                .commit();

        new Handler().postDelayed(() -> {
            if(!fragmentManager.isDestroyed()) {
                fragmentManager.beginTransaction()
                        .add(R.id.activityMainView, new WelcomeFragment())
                        .commit();
            }
        },4000);

    }

    public void credits(View view){

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.activityMainView, new CreditsFragment())
                .commit();

    }

}