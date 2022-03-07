package ez10.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class forgotPassword extends AppCompatActivity {

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
        resetButton = findViewById(R.id.resetButton);
        errorMessages = findViewById(R.id.errorMessages);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);
    }

    public void sendPasswordReset(View view) {
        String emailStr = email.getText().toString().trim();

        SplashScreen.mAuth.sendPasswordResetEmail(emailStr)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(forgotPassword.this, LogInActivity.class));
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