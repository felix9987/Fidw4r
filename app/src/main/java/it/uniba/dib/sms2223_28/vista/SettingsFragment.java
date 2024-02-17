package it.uniba.dib.sms2223_28.vista;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.AudioManager;
import android.widget.SeekBar;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MainActivity;


public class SettingsFragment extends Fragment {
    DatabaseReference databaseReference;
    private AudioManager audioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SeekBar volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);

        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        view.findViewById(R.id.btnLogOut).setOnClickListener(view1 -> {
            getActivity().finish();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        view.findViewById(R.id.btnChangeLanguage).setOnClickListener(view12 -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String currentLanguage = preferences.getString("current_language","it");

            if (currentLanguage.equals("it")) {
                currentLanguage = "en";
            } else {
                currentLanguage ="it";
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("current_language", currentLanguage);
            editor.apply();

            Locale locale = new Locale(currentLanguage);
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            config.setLocale(locale);

            Resources resources = getResources();
            resources.updateConfiguration(config, resources.getDisplayMetrics());

            getActivity().recreate();
        });
    }

    public void setEscButton(View.OnClickListener onClickListener) {
        try {
            requireView().findViewById(R.id.btnLogOut).setOnClickListener(onClickListener);
        } catch (Exception e) {}
    }
}