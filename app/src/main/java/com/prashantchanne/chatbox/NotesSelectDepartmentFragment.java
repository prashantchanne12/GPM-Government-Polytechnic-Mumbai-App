package com.prashantchanne.chatbox;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NotesSelectDepartmentFragment extends Fragment {

    private View view;

    private CardView it;
    private CardView co;
    private CardView ce;
    private CardView ec;
    private CardView me;
    private CardView ee;
    private CardView is;
    private CardView rt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_notes_select_department, container, false);

         it = view.findViewById(R.id.it_card);
         co = view.findViewById(R.id.co_card);
         ce = view.findViewById(R.id.ce_card);
         ec = view.findViewById(R.id.ec_card);
         me = view.findViewById(R.id.me_card);
         ee = view.findViewById(R.id.ee_card);
         is = view.findViewById(R.id.is_card);
         rt = view.findViewById(R.id.rt_card);


         it.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("dept","IT");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"it")
                             .addToBackStack("it")
                             .commit();
                 }
             }
         });


         co.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("dept","CO");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"co")
                             .addToBackStack("co")
                             .commit();
                 }
             }
         });


         ce.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("dept","CE");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"ce")
                             .addToBackStack("ce")
                             .commit();
                 }
             }
         });

         ec.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Bundle bundle = new Bundle();
                 bundle.putString("dept","EC");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"ec")
                             .addToBackStack("ec")
                             .commit();
                 }

             }
         });

         me.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("dept","ME");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"me")
                             .addToBackStack("me")
                             .commit();
                 }
             }
         });

         ee.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("dept","EE");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"ee")
                             .addToBackStack("ee")
                             .commit();
                 }
             }
         });

         is.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("dept","IS");

                 NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                 notesFirstSecondThirdFragment.setArguments(bundle);

                 if (getFragmentManager() != null) {
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"is")
                             .addToBackStack("is")
                             .commit();
                 }
             }
         });

        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dept","RT");

                NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                notesFirstSecondThirdFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"rt")
                            .addToBackStack("rt")
                            .commit();
                }
            }
        });

         /*


        it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dept","IT");

                NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                notesFirstSecondThirdFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"it")
                            .addToBackStack("it")
                            .commit();
                }

            }
        });

        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dept","CO");

                NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                notesFirstSecondThirdFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"co")
                            .addToBackStack("co")
                            .commit();
                }
            }
        });

        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dept","ME");

                NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                notesFirstSecondThirdFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"me")
                            .addToBackStack("me")
                            .commit();
                }
            }
        });


        ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dept","CE");

                NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                notesFirstSecondThirdFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"ce")
                            .addToBackStack("ce")
                            .commit();
                }
            }
        });


        ec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dept","EC");

                NotesFirstSecondThirdFragment notesFirstSecondThirdFragment = new NotesFirstSecondThirdFragment();
                notesFirstSecondThirdFragment.setArguments(bundle);

                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame,notesFirstSecondThirdFragment,"ec")
                            .addToBackStack("ec")
                            .commit();
                }
            }
        });

*/
         return view;


    }




}
