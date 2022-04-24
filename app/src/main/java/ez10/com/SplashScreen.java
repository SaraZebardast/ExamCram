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
                    startActivity(new Intent(SplashScreen.this, SelectCoursesActivity.class)); //change back to loginactivity
                }
                else {
                    startActivity(new Intent(SplashScreen.this, SelectCoursesActivity.class));
                }
                finishAffinity();
            }
        }, 2000);





    }
}