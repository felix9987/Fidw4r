package it.uniba.dib.sms2223_28.vista;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MenuGameActivity;
import it.uniba.dib.sms2223_28.modello.Constants;
import it.uniba.dib.sms2223_28.modello.User;

public class RegisterFragment extends Fragment {
    public final static int INITIAL_TROPHIES=600;
    private EditText username,mail,password, confirmPass;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private User user;
    private String key;
    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ImageButton confirmRegister = view.findViewById(R.id.btnConfirmRegister);
        username = view.findViewById(R.id.userRegister);
        mail= view.findViewById(R.id.mail);
        password= view.findViewById(R.id.passwordRegister);
        confirmPass = view.findViewById(R.id.confirmPass);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        confirmRegister.setOnClickListener(view1 -> autentication());
    }

    private void autentication() {
        String username = this.username.getText().toString().trim();
        String email= mail.getText().toString().trim();
        String psw = password.getText().toString().trim();
        Integer actualTrophies = INITIAL_TROPHIES;
        Integer maxTrophies = INITIAL_TROPHIES;
        String pswConfirm= confirmPass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern)){
            mail.setError(getResources().getString(R.string.formato_corretto));

        } else if (psw.isEmpty() || password.length()<8) {
            password.setError(getResources().getString(R.string.caratteri_psw));

        } else if (!psw.matches(pswConfirm)) {
            confirmPass.setError(getResources().getString(R.string.password_diverse));

        } else if (username.isEmpty()){
            this.username.setError(getResources().getString(R.string.inserisci_username));

        } else {

            mAuth.createUserWithEmailAndPassword(email,psw).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    //scrittura dati utente su db
                    dbWrite(email, psw, actualTrophies,maxTrophies, username);

                    sendUserToNextActivity();
                    Toast.makeText(getContext(), getResources().getString(R.string.registrazione_avvenuta), Toast.LENGTH_SHORT).show();

                }else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(getContext(), getResources().getString(R.string.mail_esistente), Toast.LENGTH_SHORT).show();

                    else if (task.getException() instanceof FirebaseAuthWeakPasswordException)
                        Toast.makeText(getContext(), getResources().getString(R.string.password_debole), Toast.LENGTH_SHORT).show();

                    else if (task.getException() instanceof FirebaseNetworkException)
                        Toast.makeText(getContext(), getResources().getString(R.string.probelma_di_rete), Toast.LENGTH_SHORT).show();

                    else if (task.getException() instanceof FirebaseAuthException)
                        Toast.makeText(getContext(), getResources().getString(R.string.accesso_negato), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void dbWrite(String email , String psw , Integer actualTrophies ,Integer maxTrophies, String username){

        user = new User(email, psw, actualTrophies,maxTrophies, username,0,0,0,0,R.drawable.back_carta,R.drawable.back_carta,R.drawable.back_carta,R.drawable.back_carta,"online");
        databaseReference.child("Users").child(mAuth.getUid()).setValue(user);
        key=mAuth.getUid();


    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getContext(), MenuGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.USER,user);
        intent.putExtra(Constants.USER_KEY, key);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}