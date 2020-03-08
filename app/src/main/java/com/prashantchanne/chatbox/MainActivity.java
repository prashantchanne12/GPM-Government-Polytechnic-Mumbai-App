package com.prashantchanne.chatbox;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private FloatingActionButton addButton;

    private FirebaseAuth.AuthStateListener authStateListener;

    //Fragments
    private ProfileFragmnet profileFragmnet;
    private NotificationFragment notificationFragment;
    //private UsersFragment usersFragment;
    private NotesFragment notesFragment;

    private NoticeBoardFragment noticeBoardFragment;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private RelativeLayout relativeLayout;

    public boolean teacher = false;
    public String departmentOfTecher;

    private CircleImageView profile;
    private TextView userName,userEmail;



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
           sendToLoginActivity();
           return;
        }else{
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    teacher = documentSnapshot.getBoolean("teacher");
                    addButton.setVisibility(View.VISIBLE);
                   // Toast.makeText(MainActivity.this, "Teacher: "+teacher, Toast.LENGTH_SHORT).show();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to load! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //firebaseAuth.addAuthStateListener(authStateListener);

    }

    private void sendToLoginActivity() {

        Intent loginACtivityIntent = new Intent(MainActivity.this, LogInActivity.class);
        loginACtivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginACtivityIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up the toolbar
        toolbar = findViewById(R.id.toolbars);
        toolbar.setTitle("GPM");
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        relativeLayout = findViewById(R.id.rel);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the arrow button to slide


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            sendToLoginActivity();
            return;
        }

        bottomNavigationView = findViewById(R.id.mainNav);
        frameLayout = findViewById(R.id.mainFrame);

        addButton = findViewById(R.id.floatingButton);
        addButton.setVisibility(View.INVISIBLE);

        profileFragmnet = new ProfileFragmnet();
        notificationFragment = new NotificationFragment();
        //usersFragment = new UsersFragment();
        notesFragment = new NotesFragment();

        final NotesSelectDepartmentFragment notesSelectDepartmentFragment = new NotesSelectDepartmentFragment();

        noticeBoardFragment = new NoticeBoardFragment();

        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

         View navHeader = navigationView.inflateHeaderView(R.layout.navigation_header);

        userEmail = navHeader.findViewById(R.id.nav_email_id);
        userName = navHeader.findViewById(R.id.nav_name);
        profile = navHeader.findViewById(R.id.nav_profile_image);


        userEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        userName.setText(documentSnapshot.getString("name"));

                        String image = documentSnapshot.getString("image");

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.default_image);

                        if (image!=null) {

                            if (image.equals("captain")) {
                                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.captainamerica).into(profile);
                            }

                            if (image.equals("ww")) {
                                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.wonderwoman).into(profile);
                            }

                            if (image.equals("flash")) {
                                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.theflash).into(profile);
                            }

                            if (image.equals("spiderman")) {
                                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.spiderman).into(profile);
                            }

                            if (image.equals("incredibles")) {
                                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.incredibles).into(profile);
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



        setFragment(noticeBoardFragment);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(teacher) {

                    Intent addActivityIntent = new Intent(MainActivity.this, addActivity.class);
                    startActivity(addActivityIntent);

                }else{

                    Intent addActivityIntent = new Intent(MainActivity.this, AddActivityStudent.class);
                    startActivity(addActivityIntent);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navNotification : {

                        setFragment(notificationFragment);
                        return true;


                    }

                    case R.id.navNotes : {

                        setFragment(notesSelectDepartmentFragment);
                        return true;


                    }


                    case R.id.navNotice : {

                        setFragment(noticeBoardFragment);
                        return true;


                    }


                    /*case R.id.navUsers :{

                        setFragment(usersFragment);
                        return true;


                    }*/

                    default: return false;

                }


            }
        });

    }

    private void setFragment(android.support.v4.app.Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment,null);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch(item.getItemId()){

            case R.id.navResult : setFragment(notificationFragment);
                                    return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

       switch(item.getItemId()) {

           case R.id.navResult :
               Intent resultIntent = new Intent(MainActivity.this,ResultActivity.class);
               startActivity(resultIntent);
               return true;

           case R.id.navLogout :
               Map<String,Object> tokenRemove = new HashMap<>();
               tokenRemove.put("token_id", FieldValue.delete());

               firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(tokenRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {

                       firebaseAuth.signOut();

                       Intent loginIntent = new Intent(MainActivity.this, LogInActivity.class);
                       loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(loginIntent);
                       finish();

                       Toast.makeText(MainActivity.this, "Successfully Logegd Out", Toast.LENGTH_SHORT).show();

                   }
               });
               return true;

           case R.id.navAccount :
               Intent setProfileIntent = new Intent(MainActivity.this,ProfileSettingsActivity.class);
               startActivity(setProfileIntent);
               //Toast.makeText(MainActivity.this, "Account", Toast.LENGTH_SHORT).show();
               return true;

           case R.id.navSettings :
               Toast.makeText(MainActivity.this, "Yet to built", Toast.LENGTH_SHORT).show();


        default: return false;
       }
    }



   /* @Override
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit ? ");
        builder.setCancelable(true);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();

            }
        });



        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }*/

    @Override
    protected void onResume() {
        super.onResume();

        checkUser();

    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user==null){
            sendToLoginActivity();
            finish();
        }

        if(user!=null)
            if(!user.isEmailVerified()){
                firebaseAuth.signOut();
                sendToLoginActivity();
            }
    }

    /*private void setUpFirebaseAuth(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser==null){
                    sendToLoginActivity();
                }

                if(firebaseUser!=null){
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(MainActivity.this, "Logged in as "+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                    }else {
                        checkUser();
                    }
                }else {
                    sendToLoginActivity();
                }

            }
        };
    }*/

    @Override
    protected void onStop() {
        super.onStop();

        firebaseAuth.removeAuthStateListener(authStateListener);
    }

}
