package ez10.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


public class LogInActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button login;
    private TextView incorrectInfo ,createAccount;
    private LottieAnimationView anim;
    private TextView errorMessages, banner;
    private LinearLayout errorMessagesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailInput = findViewById(R.id.emailLogIn);
        passwordInput = findViewById(R.id.passwordSignUp);
        login = findViewById(R.id.signUpButton);
        incorrectInfo = findViewById(R.id.textView);
        createAccount = findViewById(R.id.newHereCreateAccount);
        anim = findViewById(R.id.loadingLogIn);
        errorMessages = findViewById(R.id.errorMessages);
        banner = findViewById(R.id.textView);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);

    }

    public void onLogInButtonTap(View view) { //login button onclick listener

        String emailStr = emailInput.getText().toString(); //importing texts from 'edittext' items into their respective strings
        String passwordStr = passwordInput.getText().toString();

        if(emailStr.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            errorMessages.setText("Please enter a valid email.");
            banner.setVisibility(View.INVISIBLE);
            errorMessagesLayout.setVisibility(View.VISIBLE);
            emailInput.requestFocus();
            return;
        }
        if(passwordStr.isEmpty() || passwordStr.length()<8) {
            banner.setVisibility(View.INVISIBLE);
            errorMessages.setText("Please enter a valid password.");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            passwordInput.requestFocus();
            return;

        }

        signInUser(emailStr, passwordStr);



    }

    private void signInUser(String emailStr, String passwordStr) {
        login.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.VISIBLE);
        SplashScreen.mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
                            startActivity(new Intent(LogInActivity.this, HomePage.class));
                            login.setVisibility(View.VISIBLE);
                            anim.setVisibility(View.INVISIBLE);
                            finish();

                        } else {
                            errorMessages.setText("Your email or password is incorrect");
                            banner.setVisibility(View.INVISIBLE);
                            errorMessagesLayout.setVisibility(View.VISIBLE);
                            login.setVisibility(View.VISIBLE);
                            anim.setVisibility(View.INVISIBLE);

                        }
                    }
                });

    }


    public void goToSignUp(View view){
        Intent intent = new Intent(this, SelectCountryActivity.class);
        startActivity(intent);
    }
}