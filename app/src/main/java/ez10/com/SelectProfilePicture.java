package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectProfilePicture extends AppCompatActivity {

    private int profilePictureChoice;
    private TextView mainText;
    static int whereToDirectTo = 0; //0 normal, 1 is homepage

    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;
    ViewPager viewPager;

    ImageAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_profile_picture);
        profilePictureChoice = 0;

        currentUser = SplashScreen.mAuth.getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();

        viewPager = findViewById(R.id.pager);
        adapter = new ImageAdapter(this);
        loadUserTimeStudied();

        mainText = findViewById(R.id.main_text);

    }

    public void updateProfilePictureChoice(int i) {
        profilePictureChoice = i;
    }

    public void goToCourseSelect(View view) {
        FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid());


        reference.child("profilePictureID").setValue(profilePictureChoice);

        if (whereToDirectTo==1) {
            startActivity(new Intent(this, Main.class));

        }
        else {
            startActivity(new Intent(this, SelectCourses.class));

        }
        finishAffinity();

    }

    private void loadUserTimeStudied() {
        DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/timeStudied");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double temp = (double) dataSnapshot.getValue();


                if (temp>30) adapter.setAvailableProfilePics(2);
                else if (temp>10) adapter.setAvailableProfilePics(1);
                else adapter.setAvailableProfilePics(0);

                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        updateProfilePictureChoice(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}