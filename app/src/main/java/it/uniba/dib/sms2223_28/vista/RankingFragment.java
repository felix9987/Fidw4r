package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.LinkedList;

import it.uniba.dib.sms2223_28.R;
import it.uniba.dib.sms2223_28.modello.User;

public class RankingFragment extends Fragment {

    DatabaseReference databaseReference;
    LinkedList<User> userList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList= new LinkedList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance("https://dbgiococarte-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot key : snapshot.getChildren()){
                    String username = key.child("username").getValue(String.class);
                    int trophies = key.child("trophies").getValue(Integer.class);
                    userList.add(new User(username,trophies));
                }
                if (userList.isEmpty()) {
                    return;
                }
                int counter=0;
                Collections.sort(userList);
                for(User user1 : userList){

                    LinearLayout linearLayout = requireView().findViewById(R.id.fragmentRankingLinearLayout);
                    FrameLayout frameLayout= new FrameLayout(getContext());
                    frameLayout.setBackgroundColor( getResources().getColor(android.R.color.transparent));
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.setId(++counter);
                    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.card_width_hand)));
                    linearLayout.addView(frameLayout);
                    SingleRankFragment singleRankFragment = new SingleRankFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(SingleRankFragment.USERNAME_KEY, user1.getUsername());
                    bundle.putString(SingleRankFragment.TROPHIES_KEY, user1.getTrophies().toString());
                    bundle.putString(SingleRankFragment.POSITION_KEY, String.valueOf(counter));

                    singleRankFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().add(frameLayout.getId(), singleRankFragment).commit();
                }
                userList.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}