package ez10.com;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    Button button;
    TextView text;
    LottieAnimationView anim;
    private FirebaseAuth mAuth;
    EditText firstName, lastName, email, password;
    CheckBox termsAndConditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();
        button= findViewById(R.id.createButton);
        anim = findViewById(R.id.loading);
        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        email = findViewById(R.id.EmailIN);
        password = findViewById(R.id.passwordIN);
        text = findViewById(R.id.textinsignuppage);
    }
    public void onSignup(View view) {
        //button.setVisibility(View.INVISIBLE);
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        if(firstNameStr.isEmpty()) {
            firstName.setError("Your name cannot be empty");
            firstName.requestFocus();
            return;
        }
        if(lastNameStr.isEmpty()) {
            lastName.setError("Your name cannot be empty");
            lastName.requestFocus();
            return;
        }
        if(emailStr.isEmpty()) {
            email.setError("Please enter a valid email.");
            email.requestFocus();
            return;
        }
        if(passwordStr.isEmpty() || passwordStr.length()<8) {
            password.setError("Your password must be at least 8 characters long.");
            password.requestFocus();
            return;
        }
        if (!termsAndConditions.isEnabled()) {
            termsAndConditions.setError("You must agree to terms and conditions to continue.");
            return;
        }

        text.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.VISIBLE);

    }
}