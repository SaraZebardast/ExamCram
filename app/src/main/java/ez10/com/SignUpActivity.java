package ez10.com;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class SignUpActivity extends AppCompatActivity {

    Button button;
    TextView text;
    LottieAnimationView anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        button= findViewById(R.id.createButton);
        anim = findViewById(R.id.welcomeAnim);
        text = findViewById(R.id.textinsignuppage);
    }
    public void onSignup(View view) {
        button.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.VISIBLE);

    }
}