package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser==null) {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                }
                else {
                    startActivity(new Intent(SplashScreen.this, Main.class));
                }
                finishAffinity();
            }
        }, 2500);

    }
}
/*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*/
/*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*//*
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
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

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

    final int MAX_NO_OF_REQUESTS = 3;
    private RelativeLayout[] requestsLayout;
    private ImageView[] requestsProfilePic;
    private TextView[] requestsNames;
    private Button[] requestsButtons;



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
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.chatsNav) {
                    Toast.makeText(Main.this, "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.achievementsNav && bottomNav.getSelectedItemId()!=R.id.achievementsNav) {
                    fragment = new Achievements();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
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

    public void displayFriendReqs(View view) {
        dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friend_requests);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        requestsLayout = new RelativeLayout[MAX_NO_OF_REQUESTS];
        requestsProfilePic = new ImageView[MAX_NO_OF_REQUESTS];
        requestsNames = new TextView[MAX_NO_OF_REQUESTS];
        requestsButtons = new Button[MAX_NO_OF_REQUESTS];

        requestsLayout[0] = dialog.findViewById(R.id.friendReq0);
        requestsProfilePic[0] = dialog.findViewById(R.id.friendReqPP0);
        requestsNames[0] = dialog.findViewById(R.id.friendReqName0);
        requestsButtons[0] = dialog.findViewById(R.id.accept0);

        requestsLayout[1] = dialog.findViewById(R.id.friendReq1);
        requestsProfilePic[1] = dialog.findViewById(R.id.friendReqPP1);
        requestsNames[1] = dialog.findViewById(R.id.friendReqName1);
        requestsButtons[1] = dialog.findViewById(R.id.accept1);

        requestsLayout[2] = dialog.findViewById(R.id.friendReq2);
        requestsProfilePic[2] = dialog.findViewById(R.id.friendReqPP2);
        requestsNames[2] = dialog.findViewById(R.id.friendReqName2);
        requestsButtons[2] = dialog.findViewById(R.id.accept2);

        j=0;
        j2=0;
        loadFriendRequest();


    }

    int j=0;
    int j2=0;
    private void loadFriendRequest() {

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestIndex : snapshot.getChildren()) {
                    requestsLayout[j2].setVisibility(View.VISIBLE);
                    DatabaseReference reference = rootNode.getReference("Registered Users/" + requestIndex.getKey());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestsNames[j].setText(snapshot.child("firstName").getValue() + "");
                            String value = "" + snapshot.child("profilePictureID").getValue();
                            readProfilePicture(value);
                            requestsButtons[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/friendRequests/" + snapshot.getKey());
                                    reference.removeValue();
                                    reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends/" + snapshot.getKey());
                                    reference.setValue("");
                                    reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/userFriends/" + currentUser.getUid());
                                    reference.setValue("");
                                    dialog.hide();
                                }
                            });
                            j++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    j2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readProfilePicture(String value) {
        if (value.equals("0")) {
            requestsProfilePic[j].setImageResource(R.drawable.steve);
        }
        else if (value.equals("1")) {
            requestsProfilePic[j].setImageResource(R.drawable.rosan);
        }
        else if (value.equals("2")){
            requestsProfilePic[j].setImageResource(R.drawable.isac);
        }
        else if (value.equals("3")){
            requestsProfilePic[j].setImageResource(R.drawable.davinci);
        }
        else if (value.equals("4")){
            requestsProfilePic[j].setImageResource(R.drawable.einstien);
        }
    }


}*/
