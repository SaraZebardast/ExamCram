package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectProfilePictureActivity extends AppCompatActivity {

    private Button nextButton;
    private int profilePictureChoice;
    private TextView mainText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_profile_picture);
        profilePictureChoice = 0;
        ViewPager viewPager = findViewById(R.id.pager);
        ImageAdapter adapter = new ImageAdapter(this);
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

        startActivity(new Intent(this, SelectCoursesActivity.class));
        finishAffinity();

    }
}