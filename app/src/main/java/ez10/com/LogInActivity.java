package ez10.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class LogInActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput, createUserName;
    private Button login;
    private TextView incorrectInfo ,createAccount, welcomeBack, weMissedYou;
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameInput = findViewById(R.id.usernameIN);
        passwordInput = findViewById(R.id.passwordIN);
        login = findViewById(R.id.createButton);
        incorrectInfo = findViewById(R.id.textView);
        createAccount = findViewById(R.id.createAccount);
        welcomeBack = findViewById(R.id.welcomeback);
        weMissedYou = findViewById(R.id.wemissedyou);
        animationView = findViewById(R.id.animationView);
    }

    public void onLogin(View view) { //login button onclick lister
        String usernameInputStr = usernameInput.getText().toString();
        String passwordInputStr = passwordInput.getText().toString();
        if (!usernameInputStr.equals("") && !passwordInputStr.equals("") ) {
            login.setVisibility(View.INVISIBLE);
            animationView.setVisibility(View.VISIBLE);

        }
        else {
            incorrectInfo.setText("Incorrect username or password");
        }

    }
    //Create account
    public void accountCreate (View view){
        Intent intent = new Intent(this, SelectCountryActivity.class);
        startActivity(intent);
    }
}