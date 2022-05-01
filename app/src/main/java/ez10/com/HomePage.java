package ez10.com;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class HomePage extends AppCompatActivity {

    private TextView username;
    private TextView noOfPeopleOnCampus;
    private ImageView profilePicture;
    private String userUniversity;
    private Switch onCampusStatusSwitch, currentlyStudyingSwitch;
    Dialog dialog;

    private int noOfPeopleOnCampusCounter;
    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;

    private RelativeLayout[] friendsLayout;
    private ImageView[] friendsProfilePic;
    private TextView[] friendsNames;
    private TextView[] friendsStatus;
    private final int MAX_NO_OF_FRIENDS = 3;


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

        friendsLayout = new RelativeLayout[MAX_NO_OF_FRIENDS];
        friendsProfilePic = new ImageView[MAX_NO_OF_FRIENDS];
        friendsNames = new TextView[MAX_NO_OF_FRIENDS];
        friendsStatus = new TextView[MAX_NO_OF_FRIENDS];

        friendsLayout[0] = findViewById(R.id.friend0);
        friendsProfilePic[0] = findViewById(R.id.profilepicfriend0);
        friendsNames[0] = findViewById(R.id.friendname0);
        friendsStatus[0] = findViewById(R.id.friendstatus0);

        friendsLayout[1] = findViewById(R.id.friend1);
        friendsProfilePic[1] = findViewById(R.id.profilepicfriend1);
        friendsNames[1] = findViewById(R.id.friendname1);
        friendsStatus[1] = findViewById(R.id.friendstatus1);

        friendsLayout[2] = findViewById(R.id.friend2);
        friendsProfilePic[2] = findViewById(R.id.profilepicfriend2);
        friendsNames[2] = findViewById(R.id.friendname2);
        friendsStatus[2] = findViewById(R.id.friendstatus2);

        loadUserData();

        SelectProfilePicture.whereToDirectTo=0; //this has to be here

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

        for (int i=0; i<MAX_NO_OF_FRIENDS; i++) {
            loadUserFriends(0);
        }

    }

    public void startStudyCounter() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/studying");
        reference.setValue(true);
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/startStudyStreakTime");
        reference.setValue(System.currentTimeMillis());
    }

    public void endStudyCounter() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/studying");
        reference.setValue(false);
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/endStudyStreakTime");
        reference.setValue(System.currentTimeMillis());
        calculateHoursStudied();
    }

    public void calculateHoursStudied() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/startStudyStreakTime");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long startStudyStreakTime = (long) dataSnapshot.getValue();
                DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/endStudyStreakTime");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long endStudyStreakTime = (long) dataSnapshot.getValue();
                        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/timeStudied");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String temp = "" + dataSnapshot.getValue();
                                double currentTimeStudied = Double.parseDouble(temp);
                                double totalStudied = (endStudyStreakTime-startStudyStreakTime)/3600000.0; //converting millis to hours

                                currentTimeStudied += totalStudied;
                                DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/timeStudied");
                                reference.setValue(currentTimeStudied);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public void loadUserFriends(int i) {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/friend" + i);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String friendUID = "" + dataSnapshot.getValue();
                if (friendUID.equals("-")) return;
                friendsLayout[i].setVisibility(View.VISIBLE);
                DatabaseReference reference = rootNode.getReference("Registered Users/" + friendUID + "/studying");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String temp = "" + snapshot.getValue();
                        boolean friendStudying = Boolean.parseBoolean(temp);
                        if (!friendStudying) {
                            friendsStatus[i].setText("Not Studying");
                            friendsStatus[i].setTextColor(getColor(R.color.faded_white));
                        }
                        else {

                            DatabaseReference reference = rootNode.getReference("Registered Users/" + friendUID + "/startStudyStreakTime");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String temp = "" + snapshot.getValue();
                                    Long time = Long.parseLong(temp);
                                    String studyStatus = "Studying since ";

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        LocalDateTime date =
                                                LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
                                        studyStatus += date.toString().substring(11,16);
                                    }
                                    friendsStatus[i].setText(studyStatus);
                                    friendsStatus[i].setTextColor(getColor(R.color.jungleGreen));

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                reference = rootNode.getReference("Registered Users/" + friendUID + "/firstName");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String temp = "" + snapshot.getValue();
                        friendsNames[i].setText(temp);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                reference = rootNode.getReference("Registered Users/" + friendUID + "/profilePictureID");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = "" + snapshot.getValue();
                        if (value.equals("0")) {
                            friendsProfilePic[i].setImageResource(R.drawable.steve);
                        }
                        else if (value.equals("1")) {
                            friendsProfilePic[i].setImageResource(R.drawable.rosan);
                        }
                        else {
                            friendsProfilePic[i].setImageResource(R.drawable.isac);
                        }
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


    public void changeProfilePicture(View view) {
        dialog.hide();
        SelectProfilePicture.whereToDirectTo=1;
        startActivity(new Intent(this, SelectProfilePicture.class));

    }

    public void changeCourses(View view) {
        dialog.hide();
        startActivity(new Intent(this, SelectCourses.class));

    }

}