package ez10.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private Button button;

    private LottieAnimationView anim;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private TextView errorMessages;
    private LinearLayout errorMessagesLayout;
    private CheckBox termsAndConditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        button= findViewById(R.id.resetButton);
        anim = findViewById(R.id.loadingSignUp);
        firstNameInput = findViewById(R.id.firstNameSignUp);
        lastNameInput = findViewById(R.id.lastNameSignUp);
        emailInput = findViewById(R.id.emailSignUp);
        passwordInput = findViewById(R.id.passwordSignUp);
        errorMessages = findViewById(R.id.errorMessages);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);
    }



    public void onCreateButtonTap(View view) {

        String emailStr = emailInput.getText().toString().trim();
        String passwordStr = passwordInput.getText().toString().trim();
        String firstNameStr = firstNameInput.getText().toString().trim();
        String lastNameStr = lastNameInput.getText().toString().trim();

        if(firstNameStr.isEmpty()) {
            errorMessages.setText("Your name cannot be empty");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            firstNameInput.requestFocus();
            return;
        }
        if(lastNameStr.isEmpty()) {
            errorMessages.setText("Your name cannot be empty");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            lastNameInput.requestFocus();
            return;
        }
        if(emailStr.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            errorMessages.setText("Please enter a valid email.");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            emailInput.requestFocus();
            return;
        }
        if(passwordStr.isEmpty() || passwordStr.length()<8) {
            errorMessages.setText("Your password must be at least 8 characters long.");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            passwordInput.requestFocus();
            return;
        }
        //if (!termsAndConditions.isChecked()) {
        //    errorMessages.setText("You must agree to terms and conditions to continue.");
        //    errorMessages.setVisibility(View.VISIBLE);
        //    return;
        //  }



        signUpUser(firstNameStr, lastNameStr, emailStr, passwordStr);


    }



    private void signUpUser(String firstNameStr, String lastNameStr, String emailStr, String passwordStr) {
        button.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.VISIBLE);
        SplashScreen.mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(firstNameStr, lastNameStr, emailStr, passwordStr);
                            FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, HomePage.class));
                            finish();

                        } else {
                            errorMessages.setText("An error occurred.");
                            errorMessagesLayout.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            anim.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private void  goToWelcome() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}