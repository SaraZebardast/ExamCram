package ez10.com;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;


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

    private Dialog dialog;
    private Button searchButton;

    private final int MAX_NO_OF_COURSES = 5;



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

        searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSearch();
            }
        });

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


        loadFriends();
//        for (int i=0; i<MAX_NO_OF_FRIENDS; i++) {
//            DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/friend" + i);
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String friendUID = "" + dataSnapshot.getValue();
//                    if (!(friendUID.equals("-") || friendUID.equals(currentUser.getUid()) || friendUID.equals("null"))) {
//                        anim.setVisibility(View.INVISIBLE);
//                        noFriendsTxt.setVisibility(View.INVISIBLE);
//                        loadUserFriends(friendUID);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
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

    int z=0;
    int z1=0;
    private void loadFriends() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot friendIndex : snapshot.getChildren()) {
                    if (!friendIndex.getKey().equals(currentUser.getUid())) {
                        anim.setVisibility(View.INVISIBLE);
                        noFriendsTxt.setVisibility(View.INVISIBLE);
                        friendsLayout[z1].setVisibility(View.VISIBLE);
                        DatabaseReference reference = rootNode.getReference("Registered Users/" + friendIndex.getKey());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                friendsNames[z].setText(snapshot.child("firstName").getValue() + "");
                                String value = "" + snapshot.child("profilePictureID").getValue();
                                readProfilePic(value, z);
                                boolean friendStudying = Boolean.parseBoolean(snapshot.child("studying").getValue() + "");
                                if (!friendStudying) {
                                    friendsStatus[z].setText("Not Studying");
                                    friendsStatus[z].setTextColor(viewMain.getResources().getColor(R.color.faded_white));
                                }
                                else {
                                    String temp = "" + snapshot.child("startStudyStreakTime").getValue();
                                    Long time = Long.parseLong(temp);
                                    String studyStatus = "Studying since ";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        LocalDateTime date =
                                                LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
                                        studyStatus += date.toString().substring(11,16);
                                    }
                                    friendsStatus[z].setText(studyStatus);
                                    friendsStatus[z].setTextColor(viewMain.getResources().getColor(R.color.jungleGreen));
                                }
                                z++;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        z1++;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePic(String value, int z) {
        if (value.equals("0")) {
            friendsProfilePic[z].setImageResource(R.drawable.steve);
        } else if (value.equals("1")) {
            friendsProfilePic[z].setImageResource(R.drawable.rosan);
        } else if (value.equals("2")) {
            friendsProfilePic[z].setImageResource(R.drawable.isac);
        } else if (value.equals("3")) {
            friendsProfilePic[z].setImageResource(R.drawable.davinci);
        } else if (value.equals("4")) {
            friendsProfilePic[z].setImageResource(R.drawable.einstien);
        }
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
                readProfilePic(value, i);
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

    Spinner subCourseChoices, courseChoices;
    ArrayAdapter<String> courseAdapter;
    ArrayAdapter<CharSequence> subCourseAdapter;
    Button finalSearch;
    static String selectedCourse, selectedTopic;

    public void toggleSearch() {
        if (dialog!=null) dialog.hide();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);


        finalSearch = dialog.findViewById(R.id.searchButton);
        courseChoices = dialog.findViewById(R.id.course);

        finalSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCourse = courseChoices.getSelectedItem().toString().trim();
                selectedTopic = subCourseChoices.getSelectedItem().toString().trim();

                if (!(selectedCourse.equals("Select Course") || selectedTopic.equals("Topic"))) {
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/interestCounter");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int val = Integer.parseInt(""+snapshot.getValue());
                            DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/studyInterests/interest" + val);
                            reference.setValue(selectedTopic);
                            reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/interestCounter");
                            if (val==2) {
                                reference.setValue(0);
                            }
                            else {
                                reference.setValue(val+1);
                            }
                            reference = rootNode.getReference("Registered Universities/" + Main.userUniversity + "/" + selectedCourse + "/" + selectedTopic);
                            reference.child(currentUser.getUid()).setValue("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.hide();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, new Results(selectedCourse, selectedTopic))
                            .addToBackStack(null).commit();
                }

            }
        });

        loadCourses();

    }


    private void loadCourses() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userCourses");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> temp = new ArrayList<>();

                temp.add("Select Course");

                for (int i=0; i<MAX_NO_OF_COURSES; i++) {
                    if (!dataSnapshot.child("course" + i).getValue().equals("0")) temp.add("" + dataSnapshot.child("course" + i).getValue());
                }

                courseAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, temp);

                courseChoices.setAdapter(courseAdapter);

                subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.topic, R.layout.disabled_spinner_layout);
                subCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                courseChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        subCourseChoices = dialog.findViewById(R.id.subCourse);
                        String selectedCourse = courseChoices.getSelectedItem().toString().trim();


                        if (selectedCourse.equals("MATH 101")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_Math101, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        } else if (selectedCourse.equals("MATH 102")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_Math102, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        } else if (selectedCourse.equals("CS 101")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_CS101, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        } else if (selectedCourse.equals("CS 102")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_CS102, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else if (selectedCourse.equals("CS 102")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_CS102, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else if (selectedCourse.equals("PHYS 101")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_PHYS101, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else if (selectedCourse.equals("PHYS 102")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_PHYS102, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else if (selectedCourse.equals("MATH 132")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_Math132, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else if (selectedCourse.equals("CS 201")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_CS201, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else if (selectedCourse.equals("CS 202")) {

                            subCourseChoices.setClickable(true);
                            subCourseChoices.setEnabled(true);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Bilkent_CS202, R.layout.spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);

                        }
                        else {
                            subCourseChoices.setClickable(false);
                            subCourseChoices.setEnabled(false);
                            subCourseAdapter = ArrayAdapter.createFromResource(getContext(), R.array.topic, R.layout.disabled_spinner_layout);
                            subCourseChoices.setAdapter(subCourseAdapter);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        subCourseChoices.setClickable(false);
                        subCourseChoices.setEnabled(false);
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}