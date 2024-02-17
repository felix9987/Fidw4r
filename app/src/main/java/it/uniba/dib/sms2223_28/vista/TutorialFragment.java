package it.uniba.dib.sms2223_28.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.dib.sms2223_28.R;

public class TutorialFragment extends Fragment {

    private static final Integer[] tutorialList = {R.drawable.tutorial_background, R.drawable.tutorial_1, R.drawable.tutorial_2,R.drawable.tutorial_3, R.drawable.tutorial_4, R.drawable.tutorial_5, R.drawable.tutorial_6,R.drawable.tutorial_7, R.drawable.tutorial_8, R.drawable.tutorial_9, R.drawable.tutorial_10};
    private static final int[] stringList={0,R.string.tutorial_slide1, R.string.tutorial_slide2, R.string.tutorial_slide3, R.string.tutorial_slide4, R.string.tutorial_slide5, R.string.tutorial_slide6, R.string.tutorial_slide7, R.string.tutorial_slide8, R.string.tutorial_slide9, R.string.tutorial_slide10};
    private int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int drawable;
        int string;

        try {
            i=getArguments().getInt("4");
        } catch (NullPointerException e) {
            i=0;
        }


        if (i == 0) {
            view.findViewById(R.id.previous_tutorial_slide).setAlpha(0.44f);
            ((TextView)view.findViewById(R.id.tutorial_text_1)).setText(R.string.tutorial_text_1);
        } else {

            if (i>=tutorialList.length)
                i= tutorialList.length-1;

            drawable=tutorialList[i];
            string=stringList[i];
            view.findViewById(R.id.tutorial_view).setBackgroundResource(drawable);
            ((TextView) view.findViewById(R.id.tutorial_text_description)).setText(string);

            view.findViewById(R.id.previous_tutorial_slide).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TutorialFragment fragment = new TutorialFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("4", --i);
                    fragment.setArguments(bundle);

                    getParentFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.slide_out_animator,0)
                            .replace(R.id.tutorialTotalView, fragment)
                            .commit();
                }
            });
        }

        if (i == tutorialList.length - 1) {
            view.findViewById(R.id.next_tutorial_slide).setAlpha(0.44f);
        } else {

            view.findViewById(R.id.next_tutorial_slide).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TextView)requireView().findViewById(R.id.tutorial_text_1)).setText(null);
                    TutorialFragment fragment = new TutorialFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("4", ++i);
                    fragment.setArguments(bundle);



                    getParentFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.slide_in_animator,0)
                            .replace(R.id.tutorialTotalView, fragment)
                            .commit();
                }
            });
        }

    }

}

