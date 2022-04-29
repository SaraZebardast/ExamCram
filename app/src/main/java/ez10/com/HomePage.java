package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    private TextView username;
    private TextView noOfPeopleOnCampus;
    private ImageView profilePicture;
    String userUniversity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        profilePicture = findViewById(R.id.profilepic);
        username = findViewById(R.id.name);
        noOfPeopleOnCampus = findViewById(R.id.textpeopleoncampus);

        loadUserData();
    }

    public void onSignOutButtonTap(View view) {
        SplashScreen.mAuth.signOut();
        SplashScreen.currentUser = null;
        startActivity(new Intent(this, LogInActivity.class));
        finishAffinity();
    }

    public void loadUserData() {

        FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
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
                else {
                    profilePicture.setImageResource(R.drawable.isac);
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


        reference = rootNode.getReference("Bilkent University NoOfPeopleOnCampus");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = "" + dataSnapshot.getValue();
                noOfPeopleOnCampus.setText(value + " people on campus right now");


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}