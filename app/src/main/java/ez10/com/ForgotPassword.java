package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button resetButton;
    LottieAnimationView anim;
    TextView errorMessages;
    LinearLayout errorMessagesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_password);
        anim = findViewById(R.id.loading);
        email = findViewById(R.id.emailReset);
        resetButton = findViewById(R.id.createAccountButton);
        errorMessages = findViewById(R.id.errorMessages);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);
    }

    public void sendPasswordReset(View view) {
        String emailStr = email.getText().toString().trim();

        if(emailStr.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            errorMessages.setText("Please enter a valid email.");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            email.requestFocus();
            return;
        }

        resetButton.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.VISIBLE);

        SplashScreen.mAuth.sendPasswordResetEmail(emailStr)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Password reset sent to your email.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPassword.this, Login.class));


                            finish();
                        }

                        else {
                            errorMessages.setText("Something went wrong.");
                            errorMessagesLayout.setVisibility(View.VISIBLE);
                            resetButton.setVisibility(View.VISIBLE);
                            anim.setVisibility(View.INVISIBLE);

                        }
                    }
                });


    }
}