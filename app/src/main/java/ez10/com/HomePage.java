package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class HomePage extends AppCompatActivity {

    private TextView username;
    private TextView noOfPeopleOnCampus;
    private ImageView profilePicture;
    private String userUniversity;
    private Switch onCampusStatusSwitch, currentlyStudyingSwitch;
    private int noOfPeopleOnCampusCounter;
    private Date currentTime;
    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;
    private boolean onCampus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        profilePicture = findViewById(R.id.profilepic);
        username = findViewById(R.id.name);
        noOfPeopleOnCampus = findViewById(R.id.textpeopleoncampus);
        onCampusStatusSwitch = findViewById(R.id.switch1);
        currentlyStudyingSwitch = findViewById(R.id.switch2);
        currentUser = SplashScreen.mAuth.getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();

        currentTime = Calendar.getInstance().getTime();

        loadUserData();

        onCampusStatusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCampusStatusSwitch.isChecked()) {
                    setOnCampus();
                }
                else if (!onCampusStatusSwitch.isChecked()) {
                    setOffCampus();
                }
            }
        });

        currentlyStudyingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyStudyingSwitch.isChecked()) {
                    startStudyCounter();
                }
                else if (!currentlyStudyingSwitch.isChecked()) {
                     endStudyCounter();
                }
            }
        });



    }

    public void startStudyCounter() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/studying");
        reference.setValue(true);
    }

    public void endStudyCounter() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/studying");
        reference.setValue(false);
    }

    public void setOnCampus() {

        DatabaseReference reference = rootNode.getReference(userUniversity + " NoOfPeopleOnCampus");
        reference.setValue(noOfPeopleOnCampusCounter+1);
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/onCampus");
        reference.setValue(true);

    }

    public void setOffCampus() {

        DatabaseReference reference = rootNode.getReference(userUniversity + " NoOfPeopleOnCampus");
        reference.setValue(noOfPeopleOnCampusCounter-1);
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/onCampus");
        reference.setValue(false);
    }

    public void onSignOutButtonTap(View view) {
        SplashScreen.mAuth.signOut();
        SplashScreen.currentUser = null;
        setOffCampus();
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

        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/university");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userUniversity = "" + dataSnapshot.getValue();
                DatabaseReference reference = rootNode.getReference("" + userUniversity +" NoOfPeopleOnCampus");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = "" + dataSnapshot.getValue();
                        noOfPeopleOnCampus.setText(value + " people on campus right now");
                        noOfPeopleOnCampusCounter= Integer.parseInt(value);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/onCampus");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((boolean)dataSnapshot.getValue()) {
                    onCampusStatusSwitch.setChecked(true);
                }
                else onCampusStatusSwitch.setChecked(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/studying");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((boolean)dataSnapshot.getValue()) {
                    currentlyStudyingSwitch.setChecked(true);
                }
                else currentlyStudyingSwitch.setChecked(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}