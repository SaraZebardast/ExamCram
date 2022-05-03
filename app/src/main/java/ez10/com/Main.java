package ez10.com;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Main extends AppCompatActivity {

    private TextView username;
    private ImageView profilePicture;
    public static String userUniversity;

    private Dialog dialog;
    private FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
    private FirebaseDatabase rootNode;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        profilePicture = findViewById(R.id.profilepic);
        username = findViewById(R.id.name);
        rootNode = FirebaseDatabase.getInstance();
        bottomNav = findViewById(R.id.bottomNav);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Homepage()).commit();
        bottomNav.setSelectedItemId(R.id.homeNav);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId()==R.id.homeNav && bottomNav.getSelectedItemId()!=R.id.homeNav) {
                    fragment = new Homepage();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav && bottomNav.getSelectedItemId()!=R.id.homeNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations();
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }

                return true;
            }
        });

        loadUserData();

        SelectProfilePicture.whereToDirectTo=0; //this has to be here

    }



    public void displaySettings(View view) {
        if (dialog!=null) dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }


    public void onSignOutButtonTap(View view) {
        dialog.hide();
        SplashScreen.mAuth.signOut();
        SplashScreen.currentUser = null;
        startActivity(new Intent(this, Login.class));
        finishAffinity();
    }

    public void loadUserData() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/profilePictureID");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = "" + dataSnapshot.getValue();
                if (value.equals("0")) {
                    profilePicture.setImageResource(R.drawable.steve);
                }
                else if (value.equals("1")) {
                    profilePicture.setImageResource(R.drawable.rosan);
                }
                else if (value.equals("2")){
                    profilePicture.setImageResource(R.drawable.isac);
                }
                else if (value.equals("3")){
                    profilePicture.setImageResource(R.drawable.davinci);
                }
                else if (value.equals("4")){
                    profilePicture.setImageResource(R.drawable.einstien);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/firstName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                username.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/university");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = "" + dataSnapshot.getValue();
                userUniversity = value;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    public void changeProfilePicture(View view) {
        dialog.hide();
        SelectProfilePicture.whereToDirectTo=1;
        startActivity(new Intent(this, SelectProfilePicture.class));

    }


    public void changeCourses(View view) {
        dialog.hide();
        SelectCourses.comingFrom=1;
        startActivity(new Intent(this, SelectCourses.class));

    }



}