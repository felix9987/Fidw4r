package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.activity.MenuGameActivity;
import it.uniba.dib.sms2223_28.modello.Constants;

public class BackFragment extends Fragment {
    private String key;
    private int maxTrophies;
    int backIdFavourite;
    String string;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState=getArguments();
        if (savedInstanceState != null ) {

            this.key=savedInstanceState.getString(Constants.USER_KEY);
        }

        if (MenuGameActivity.playerUser != null)
            maxTrophies = MenuGameActivity.playerUser.getMaxTrophies();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int hero=this.getArguments().getInt(Constants.HERO_KEY);
        int backId;
        databaseReference= FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        ImageButton imageButton=view.findViewById(R.id.back_unlock);

        switch (hero){
            case Constants.EGYPT:
                backId=R.drawable.back_egitto;

                if (MenuGameActivity.playerUser!=null)
                    backIdFavourite= MenuGameActivity.playerUser.getCardsBackEgypt();
                string = "cardsBackEgypt";

                if(maxTrophies < Constants.TROPHIES_TO_UNLOCK_EGYPT_BACK){
                    imageButton.setClickable(false);
                    view.findViewById(R.id.littleStarSpecialBack).setAlpha(0.44f);
                    view.findViewById(R.id.back_unlock).setAlpha(0.44f);

                } else
                    setOnClick(imageButton,backId,hero);
                break;

            case Constants.SPARTA:
                backId = R.drawable.back_sparta;

                if (MenuGameActivity.playerUser!=null)
                    backIdFavourite= MenuGameActivity.playerUser.getCardsBackSpartans();
                string = "cardsBackSpartans";

                if(maxTrophies < Constants.TROPHIES_TO_UNLOCK_SPARTA_BACK){
                    imageButton.setClickable(false);
                    view.findViewById(R.id.littleStarSpecialBack).setAlpha(0.44f);
                    view.findViewById(R.id.back_unlock).setAlpha(0.44f);

                } else
                    setOnClick(imageButton,backId,hero);

                break;

            case Constants.PERSIA:
                backId = R.drawable.back_persia;

                if (MenuGameActivity.playerUser!=null)
                    backIdFavourite= MenuGameActivity.playerUser.getCardsBackPersis();
                string = "cardsBackPersis";

                if(maxTrophies < Constants.TROPHIES_TO_UNLOCK_PERSIA_BACK){
                    imageButton.setClickable(false);
                    view.findViewById(R.id.littleStarSpecialBack).setAlpha(0.44f);
                    view.findViewById(R.id.back_unlock).setAlpha(0.44f);

                } else
                    setOnClick(imageButton,backId,hero);
                break;

            case Constants.ROME:
                backId = R.drawable.back_roma;

                if (MenuGameActivity.playerUser!=null)
                    backIdFavourite= MenuGameActivity.playerUser.getCardsBackRoman();
                string = "cardsBackRoman";

                if(maxTrophies < Constants.TROPHIES_TO_UNLOCK_ROMAN_BACK){
                    imageButton.setClickable(false);
                    view.findViewById(R.id.littleStarSpecialBack).setAlpha(0.44f);
                    view.findViewById(R.id.back_unlock).setAlpha(0.44f);

                } else
                    setOnClick(imageButton,backId,hero);

                break;
            default:
                throw new IllegalArgumentException();
        }

        imageButton.setBackgroundResource(backId);

        if(backIdFavourite != backId){

            view.findViewById(R.id.littleStarBackStandard).setAlpha(1);
            view.findViewById(R.id.littleStarSpecialBack).setAlpha(0.44f);
        } else {

            view.findViewById(R.id.littleStarSpecialBack).setAlpha(1);
            view.findViewById(R.id.littleStarBackStandard).setAlpha(0.44f);
        }


        view.findViewById(R.id.back_standard).setOnClickListener(view12 -> {

            setUserFavouriteBack(hero,R.drawable.back_carta);

            databaseReference
                    .child("Users")
                    .child(key)
                    .child(string)
                    .setValue(R.drawable.back_carta);
            view.findViewById(R.id.littleStarBackStandard).setAlpha(1);
            view.findViewById(R.id.littleStarSpecialBack).setAlpha(0.44f);
        });

    }

    private void setUserFavouriteBack(int hero, int back) {
        switch (hero){

            case Constants.EGYPT:
                MenuGameActivity.playerUser.setEgyptBack(back);
                break;

            case Constants.ROME:
                MenuGameActivity.playerUser.setRomeBack(back);
                break;

            case Constants.SPARTA:
                MenuGameActivity.playerUser.setSpartaBack(back);
                break;

            case Constants.PERSIA:
                MenuGameActivity.playerUser.setPersisBack(back);
                break;
        }
    }

    private void setOnClick(@NonNull View imageButton, int id, int hero) {
        imageButton.setOnClickListener(view1 -> {

            databaseReference.child("Users").child(key).child(string).setValue(id);
            requireView().findViewById(R.id.littleStarSpecialBack).setAlpha(1);
            requireView().findViewById(R.id.littleStarBackStandard).setAlpha(0.44f);
            setUserFavouriteBack(hero,id);


        });
    }

    public void setInvisible(){
        this.requireView().setVisibility(View.INVISIBLE);
    }

}