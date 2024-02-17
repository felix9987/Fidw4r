package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.modello.Card;

public class CardLayoutFragment extends Fragment {
    public final static String  CARD_KEY="card";
    public final static String CARD_HEIGHT_KEY ="height of the card";
    private float viewHeight;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.viewHeight=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        Card card;
        ImageView imageBackground=view.findViewById(R.id.cardImage);
        TextView textStroba=view.findViewById(R.id.cardStroba);
        TextView textAttack=view.findViewById(R.id.cardAttack);
        TextView textName=view.findViewById(R.id.cardName);
        TextView textEffect=view.findViewById(R.id.cardEffect);

        try {
            savedInstanceState = this.getArguments();
            if (savedInstanceState==null) throw new NullPointerException();

            this.viewHeight =savedInstanceState.getFloat(CARD_HEIGHT_KEY);
            if (this.viewHeight ==0) throw new NullPointerException();

            card=(Card) savedInstanceState.get(CARD_KEY);

            imageBackground.setBackgroundResource(card.getIdDrawable());
            textStroba.setText(String.valueOf(card.getStroba()));

            if(card.getAttack() == null){
                ViewGroup.LayoutParams params= textEffect.getLayoutParams();
                params.width= (int) (params.width*289/231);
                textEffect.setLayoutParams(params);

                ConstraintSet constraintSet = new ConstraintSet();
                ConstraintLayout layout=(ConstraintLayout) view.findViewById(R.id.backgroundCard);

                constraintSet.clone(layout);
                constraintSet.setHorizontalBias(textEffect.getId(), 0.55f);
                constraintSet.applyTo(layout);

            }
            else
                textAttack.setText(String.valueOf(card.getAttack()));

            textName.setText(card.getName());
            try {
                textEffect.setText(card.getEffect());
            } catch (Exception e) { }

            if(this.viewHeight ==0) throw new IllegalArgumentException();

            if(this.viewHeight != getResources().getDimension(R.dimen.card_height_hand)) {
                this.viewHeight /=this.getResources().getDimension(R.dimen.card_height_hand);
                this.setLayout(textStroba);
                this.setLayout(textAttack);
                this.setLayout(textName);
                this.setLayout(textEffect);
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setLayout(TextView textView) {
        ViewGroup.LayoutParams params= textView.getLayoutParams();
        params.width= (int) (params.width * this.viewHeight);
        params.height= (int) (params.height * this.viewHeight);
        textView.setLayoutParams(params);
    }

}
