package ez10.com;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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


public class Achievements extends Fragment {

    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;
    private View viewMain;


    private RelativeLayout[] friendsLayout;
    private ImageView[] friendsProfilePic;
    private TextView[] friendsNames;
    private TextView[] friendsStatus;
    private TextView noFriendsTxt;
    private TextView[] friendsRank;

    private LottieAnimationView anim;
    private final int MAX_NO_OF_FRIENDS = 5;

    private int userTimeStudied;
    private TextView hoursStudiedSoFarTXT;
    private TextView timeLeftUntilNextTXT;
    private ProgressBar progressBar;
    private ImageView rewards;

    private int continueFrom =0;


    public Achievements() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewMain = inflater.inflate(R.layout.achievements, container, false);

        return inflater.inflate(R.layout.achievements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        currentUser = SplashScreen.mAuth.getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();

        noFriendsTxt = view.findViewById(R.id.noFriendstxt);
        anim = view.findViewById(R.id.noFriends);

        friendsLayout = new RelativeLayout[MAX_NO_OF_FRIENDS];
        friendsProfilePic = new ImageView[MAX_NO_OF_FRIENDS];
        friendsNames = new TextView[MAX_NO_OF_FRIENDS];
        friendsStatus = new TextView[MAX_NO_OF_FRIENDS];
        friendsRank = new TextView[MAX_NO_OF_FRIENDS];


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

        friendsRank[0] = view.findViewById(R.id.firstTXT);
        friendsRank[1] = view.findViewById(R.id.secondTXT);
        friendsRank[2] = view.findViewById(R.id.thirdTXT);

        timeLeftUntilNextTXT = view.findViewById(R.id.studyxtounlocktxt);
        hoursStudiedSoFarTXT = view.findViewById(R.id.hoursStudiedSoFartxt);
        progressBar = view.findViewById(R.id.progressbar);
        rewards = view.findViewById(R.id.rewardImageview);


        super.onViewCreated(view, savedInstanceState);

        for (int i=0; i<MAX_NO_OF_FRIENDS; i++) {
            DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/friend" + i);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String friendUID = "" + dataSnapshot.getValue();
                    if (!(friendUID.equals("-") || friendUID.equals("null"))) {
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

        loadUserTimeStudied();

    }

    private void calculateNextUnlock() {
        if (userTimeStudied<10) {
            int differenceUntilNextUnlock = 10 - userTimeStudied;

            progressBar.setMax(10);
            progressBar.setProgress( userTimeStudied, true);
            rewards.setImageDrawable(viewMain.getResources().getDrawable(R.drawable.davinci));
            timeLeftUntilNextTXT.setText("Study " + differenceUntilNextUnlock + " hours more to unlock");
        }

        else if (userTimeStudied>=30) {

            progressBar.setMax(userTimeStudied);
            progressBar.setProgress( userTimeStudied, true);
            rewards.setImageDrawable(viewMain.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24));
            timeLeftUntilNextTXT.setText("You have unlocked all rewards.");
        }

        else if (userTimeStudied>=10) {
            int differenceUntilNextUnlock = 30 - userTimeStudied;

            progressBar.setMax(30);
            progressBar.setProgress( userTimeStudied, true);
            rewards.setImageDrawable(viewMain.getResources().getDrawable(R.drawable.einstien));
            timeLeftUntilNextTXT.setText("Study " + differenceUntilNextUnlock + " hours more to unlock");
        }
    }

    private void loadUserTimeStudied() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/timeStudied");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double temp = (double) dataSnapshot.getValue();
                userTimeStudied = temp.intValue();


                hoursStudiedSoFarTXT.setText("You studied " + userTimeStudied + " hours this week.");
                calculateNextUnlock();
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
        if (i<3) friendsRank[i].setVisibility(View.VISIBLE);
        DatabaseReference reference = rootNode.getReference("Registered Users/" + friendUID + "/timeStudied");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double temp = (double) snapshot.getValue();
                int temp2 = temp.intValue();
                friendsStatus[i].setText("" + temp2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference = rootNode.getReference("Registered Users/" + friendUID + "/firstName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (friendUID.equals(currentUser.getUid())) {
                    friendsNames[i].setText("You");
                }
                else {
                    String temp = "" + snapshot.getValue();
                    friendsNames[i].setText(temp);
                }
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
}