package ez10.com;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    Button button;
    TextView text, errorMessages;
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
        errorMessages = findViewById(R.id.errorMessages);
        text = findViewById(R.id.textinsignuppage);
    }
    public void onSignup(View view) {

        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();

        if(firstNameStr.isEmpty()) {
            errorMessages.setText("Your name cannot be empty");
            errorMessages.setVisibility(View.VISIBLE);
            firstName.requestFocus();
            return;
        }
        if(lastNameStr.isEmpty()) {
            errorMessages.setText("Your name cannot be empty");
            errorMessages.setVisibility(View.VISIBLE);
            lastName.requestFocus();
            return;
        }
        if(emailStr.isEmpty()) {
            errorMessages.setText("Please enter a valid email.");
            errorMessages.setVisibility(View.VISIBLE);
            email.requestFocus();
            return;
        }
        if(passwordStr.isEmpty() || passwordStr.length()<8) {
            errorMessages.setText("Your password must be at least 8 characters long.");
            errorMessages.setVisibility(View.VISIBLE);
            password.requestFocus();
            return;
        }
        //if (!termsAndConditions.isChecked()) {
        //    errorMessages.setText("You must agree to terms and conditions to continue.");
        //    errorMessages.setVisibility(View.VISIBLE);
        //    return;
        //  }

        text.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(firstNameStr, lastNameStr, emailStr, passwordStr);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

    }
}