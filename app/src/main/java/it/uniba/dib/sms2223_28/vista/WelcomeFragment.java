package it.uniba.dib.sms2223_28.vista;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MenuGameActivity;

public class WelcomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activityMainView, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.btnRegister).setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activityMainView, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });


        view.findViewById(R.id.btnGuest).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MenuGameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        SettingsFragment settingsFragment=new SettingsFragment();
        view.findViewById(R.id.btnSettings).setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .add(R.id.activityMainView, settingsFragment)
                    .addToBackStack(null)
                    .commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    settingsFragment.setEscButton(view1 -> getActivity().finish());
                }
            },1000);
        });

    }

}