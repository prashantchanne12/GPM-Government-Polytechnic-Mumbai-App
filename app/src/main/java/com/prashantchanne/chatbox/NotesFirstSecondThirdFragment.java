package com.prashantchanne.chatbox;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFirstSecondThirdFragment extends Fragment {

    private CardView first;
    private CardView second;
    private CardView third;

    public NotesFirstSecondThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes_first_second_third, container, false);


        first = view.findViewById(R.id.fy_card);
        second = view.findViewById(R.id.sy_card);
        third = view.findViewById(R.id.ty_card);

        final String dept = getArguments().getString("dept");
        Toast.makeText(getContext(), "Dept "+dept, Toast.LENGTH_SHORT).show();



        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(),NotesView.class);
                intent.putExtra("year","First");
                intent.putExtra("dept",dept);
                startActivity(intent);

                /*NotesFragment notesFragment = new NotesFragment();

                Bundle bundle = new Bundle();
                bundle.putString("year","First");
                bundle.putString("dept",dept);
                notesFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFragment,"firstYear")
                            .addToBackStack("firstYear")
                            .commit();
                }*/

            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(),NotesView.class);
                intent.putExtra("year","Second");
                intent.putExtra("dept",dept);
                startActivity(intent);


            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(),NotesView.class);
                intent.putExtra("year","Third");
                intent.putExtra("dept",dept);
                startActivity(intent);


            }
        });

        return view;
    }

}
/*
<TextView
            android:id="@+id/textView2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginEnd="26dp"
            android:text="Uploaded by"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/uploaded_by_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="8dp"
            android:text="prashant "
            android:textColor="@color/Black"
            android:textSize="13sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/textView2" />
 */