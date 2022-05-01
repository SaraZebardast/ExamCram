package ez10.com;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private Button button;

    private LottieAnimationView anim;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private TextView errorMessages;
    private LinearLayout errorMessagesLayout;
    private CheckBox termsAndConditions;
    private static String country, university;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        button= findViewById(R.id.createAccountButton);
        anim = findViewById(R.id.loadingSignUp);
        firstNameInput = findViewById(R.id.firstNameSignUp);
        lastNameInput = findViewById(R.id.lastNameSignUp);
        emailInput = findViewById(R.id.emailSignUp);
        passwordInput = findViewById(R.id.passwordSignUp);
        termsAndConditions = findViewById(R.id.termm);
        errorMessages = findViewById(R.id.errorMessages);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);

    }

    public static void setLocation(String Country, String University) {
        country = Country;
        university = University;
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
        if (!termsAndConditions.isChecked()) {
            errorMessages.setText("You must agree to terms and conditions.");
            errorMessagesLayout.setVisibility(View.VISIBLE);
            return;
        }



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

                            FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
                            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

                            DatabaseReference reference = rootNode.getReference("Registered Users");

                            UserInfo user = new UserInfo(emailStr, firstNameStr, lastNameStr, country, university);

                            reference.child(currentUser.getUid()).setValue(user);

                            reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userFriends");

                            for (int i=0; i<5; i++) {
                                reference.child("friend" + i).setValue("-");
                            }

                            reference = rootNode.getReference("Registered Universities/" + university + "/NoOfPeopleOnCampus");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String val = "" + snapshot.getValue();
                                    DatabaseReference reference = rootNode.getReference("Registered Universities/" + university + "/NoOfPeopleOnCampus");

                                    if (val.equals("null")) {
                                        reference.setValue(0);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Intent intent = new Intent(SignUp.this, SelectProfilePicture.class);


                            startActivity(intent);
                            finishAffinity();
                        }
                        else {
                            errorMessages.setText("An error occurred.");
                            errorMessagesLayout.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            anim.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }


    public void showTermsAndConditions(View view) {
        if (dialog!=null) dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.terms_conditions);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}