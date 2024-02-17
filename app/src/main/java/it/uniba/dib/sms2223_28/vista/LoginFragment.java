package it.uniba.dib.sms2223_28.vista;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MenuGameActivity;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.User;

public class LoginFragment extends Fragment {
    private EditText mail,pass;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        mail= view.findViewById(R.id.userLogin);
        pass= view.findViewById(R.id.passLogin);
        ImageButton loginView = view.findViewById(R.id.btnConfirmLogin);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        loginView.setOnClickListener(view1 -> login());
    }

    private void login() {
        String email = mail.getText().toString();
        String psw = pass.getText().toString();


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern)){
            mail.setError(getResources().getString(R.string.formato_corretto));
        } else if (psw.isEmpty() || pass.length()<6) {
            pass.setError(getResources().getString(R.string.caratteri_psw));
        }else {
            Toast.makeText(getContext(),  getResources().getString(R.string.login_in_corso), Toast.LENGTH_SHORT).show();


            mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                        System.out.format("result: %s", Objects.requireNonNull(task.getResult().getUser()));

                        databaseReference.child("Users").child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 String username = snapshot.child("username").getValue(String.class);
                                 Integer actualTrophies = snapshot.child("trophies").getValue(Integer.class);
                                 Integer maxTrophies = snapshot.child("maxTrophies").getValue(Integer.class);
                                 Integer romanWins = snapshot.child("romanWins").getValue(Integer.class);
                                 Integer persisWins = snapshot.child("persisWins").getValue(Integer.class);
                                 Integer egyptWins = snapshot.child("egyptWins").getValue(Integer.class);
                                 Integer spartanWins = snapshot.child("spartanWins").getValue(Integer.class);
                                 Integer cardsBackRoman = snapshot.child("cardsBackRoman").getValue(Integer.class);
                                 Integer cardsBackPersis = snapshot.child("cardsBackPersis").getValue(Integer.class);
                                 Integer cardsBackSpartans = snapshot.child("cardsBackSpartans").getValue(Integer.class);
                                 Integer cardsBackEgypt = snapshot.child("cardsBackEgypt").getValue(Integer.class);
                                 String status = snapshot.child("status").getValue(String.class);

                                 user = new User(email,psw,actualTrophies,maxTrophies,username,romanWins,persisWins,egyptWins,spartanWins,cardsBackRoman,cardsBackPersis,cardsBackSpartans,cardsBackEgypt,status);

                            }
                            
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), getResources().getString(R.string.non_ottenuto), Toast.LENGTH_SHORT).show();
                            }
                        });
                        new Handler().postDelayed(()->{
                            if(user.getStatus().equals("offline")){
                                databaseReference.child("Users").child(mAuth.getUid()).child("status").setValue("online");
                                user.setStatusOn();
                                Toast.makeText(getContext(), getResources().getString(R.string.benvenuto), Toast.LENGTH_SHORT).show();

                                sendUserToNextActivity();
                            }else{
                                try {
                                    Toast.makeText(getContext(),  getResources().getString(R.string.loggato), Toast.LENGTH_SHORT).show();
                                } catch (NullPointerException e) {
                                    //se hai effettuato corretamente l'accesso, ma hai cliccato più volte su login
                                    //ignora il Toast, ma vai avanti
                                }
                            }
                        }, 2222);
                    }else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getContext(), getResources().getString(R.string.credenziali_non_valide), Toast.LENGTH_SHORT).show();

                        } else if (exception instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(getContext(), getResources().getString(R.string.utente_errato), Toast.LENGTH_SHORT).show();

                        } else if (exception instanceof FirebaseNetworkException) {
                            Toast.makeText(getContext(), getResources().getString(R.string.probelma_di_rete), Toast.LENGTH_SHORT).show();

                        } else if (exception instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(getContext(), getResources().getString(R.string.troppe_richeste), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.errore_login), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        }
    private void sendUserToNextActivity() {
        String key = mAuth.getUid();
        Intent intent = new Intent(getContext(), MenuGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.USER,user);
        intent.putExtra(Constants.USER_KEY, key);
        startActivity(intent);
    }

}
