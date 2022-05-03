package ez10.com;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class Homepage extends Fragment {


    private TextView noOfPeopleOnCampus;
    private static String userUniversity;
    private Switch onCampusStatusSwitch, currentlyStudyingSwitch;


    private View viewMain;

    private int noOfPeopleOnCampusCounter;
    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;

    private RelativeLayout[] friendsLayout;
    private ImageView[] friendsProfilePic;
    private TextView[] friendsNames;
    private TextView[] friendsStatus;
    private TextView noFriendsTxt;
    private LottieAnimationView anim;
    private final int MAX_NO_OF_FRIENDS = 5;

    private int continueFrom=0;




    public Homepage() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewMain = inflater.inflate(R.layout.homepage, container, false);

        return inflater.inflate(R.layout.homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noOfPeopleOnCampus = view.findViewById(R.id.textpeopleoncampus);
        onCampusStatusSwitch = view.findViewById(R.id.switch1);
        currentlyStudyingSwitch = view.findViewById(R.id.switch2);
        currentUser = SplashScreen.mAuth.getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        anim = view.findViewById(R.id.noFriends);
        noFriendsTxt = view.findViewById(R.id.noFriendstxt);

        friendsLayout = new RelativeLayout[MAX_NO_OF_FRIENDS];
        friendsProfilePic = new ImageView[MAX_NO_OF_FRIENDS];
        friendsNames = new TextView[MAX_NO_OF_FRIENDS];
        friendsStatus = new TextView[MAX_NO_OF_FRIENDS];

        friendsLayout[0] = view.findViewById(R.id.friend0);
        friendsProfilePic[0] = view.findViewById(R.id.profilepicfriend0);
        friendsNames[0] = view.findViewById(R.id.friendname0);
        friendsStatus[0] = view.findViewById(R.id.friendstatus0);

        friendsLayout[1] = view.findViewById(R.id.friend1);
        friendsProfilePic[1] = view.findViewById(R.id.profilepicfriend1);
        friendsNames[1] = view.findViewById(R.id.friendname1);
        friendsStatus[1] = view.findViewById(R.id.friendstatus1);

        friendsLayout[2] = view.findViewById(R.id.friend2);
        friendsProfilePic[2] = view.findViewById(R.id.profilepicfriend2);
        friendsNames[2] = view.findViewById(R.id.friendname2);
        friendsStatus[2] = view.findViewById(R.id.friendstatus2);

        friendsLayout[3] = view.findViewById(R.id.friend3);
        friendsProfilePic[3] = view.findViewById(R.id.profilepicfriend3);
        friendsNames[3] = view.findViewById(R.id.friendname3);
        friendsStatus[3] = view.findViewById(R.id.friendstatus3);

        friendsLayout[4] = view.findViewById(R.id.friend4);
        friendsProfilePic[4] = view.findViewById(R.id.profilepicfriend4);
        friendsNames[4] = view.findViewById(R.id.friendname4);
        friendsStatus[4] = view.findViewById(R.id.friendstatus4);

        loadUserDataForHomePage();


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
            DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/friend" + i);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String friendUID = "" + dataSnapshot.getValue();
                    if (!(friendUID.equals("-") || friendUID.equals(currentUser.getUid()) || friendUID.equals("null"))) {
                        anim.setVisibility(View.INVISIBLE);
                        noFriendsTxt.setVisibility(View.INVISIBLE);
                        loadUserFriends(friendUID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
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

    private void loadUserFriends(String friendUID) {
        int i = continueFrom;
        continueFrom++;
        friendsLayout[i].setVisibility(View.VISIBLE);
        DatabaseReference reference = rootNode.getReference("Registered Users/" + friendUID + "/studying");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String temp = "" + snapshot.getValue();
                boolean friendStudying = Boolean.parseBoolean(temp);
                if (!friendStudying) {
                    friendsStatus[i].setText("Not Studying");
                    friendsStatus[i].setTextColor(viewMain.getResources().getColor(R.color.faded_white));
                }
                else {
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + friendUID + "/startStudyStreakTime");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String temp = "" + snapshot.getValue();
                            Long time = Long.parseLong(temp);
                            String studyStatus = "Studying since ";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalDateTime date =
                                        LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
                                studyStatus += date.toString().substring(11,16);
                            }
                            friendsStatus[i].setText(studyStatus);
                            friendsStatus[i].setTextColor(viewMain.getResources().getColor(R.color.jungleGreen));
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
                else if (value.equals("2")){
                    friendsProfilePic[i].setImageResource(R.drawable.isac);
                        }
                else if (value.equals("3")){
                    friendsProfilePic[i].setImageResource(R.drawable.davinci);
                }
                else if (value.equals("4")){
                    friendsProfilePic[i].setImageResource(R.drawable.einstien);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setOnCampus() {

        DatabaseReference reference = rootNode.getReference("Registered Universities/" + userUniversity + "/NoOfPeopleOnCampus");
        reference.setValue(noOfPeopleOnCampusCounter+1);
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/onCampus");
        reference.setValue(true);

    }

    public void setOffCampus() {
        DatabaseReference reference = rootNode.getReference("Registered Universities/" + userUniversity + "/NoOfPeopleOnCampus");
        reference.setValue(noOfPeopleOnCampusCounter-1);
        reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/onCampus");
        reference.setValue(false);
    }

    void loadUserDataForHomePage() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/university");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userUniversity = "" + dataSnapshot.getValue();
                DatabaseReference reference = rootNode.getReference("Registered Universities/" + userUniversity + "/NoOfPeopleOnCampus");
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