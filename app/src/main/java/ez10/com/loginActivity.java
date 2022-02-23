package ez10.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class loginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput, createUserName;
    private Button login;
    private TextView incorrectInfo ,createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameInput = findViewById(R.id.usernameIN);
        passwordInput = findViewById(R.id.passwordIN);
        login = findViewById(R.id.loginButton);
        incorrectInfo = findViewById(R.id.textView);
        createAccount = findViewById(R.id.createAccount);
    }

    public void onLogin(View view) { //login button onclick lister
        String usernameInputStr = usernameInput.getText().toString();
        String passwordInputStr = passwordInput.getText().toString();
        if (usernameInputStr.equals("admin") && passwordInputStr.equals("admin") ) {
            setContentView(R.layout.loading);
        }
        else {
            incorrectInfo.setText("Incorrect username or password");
        }

    }
    //Create account
    public void accountCreate (View view){
        Intent intent = new Intent(this, signupActivity.class);
        startActivity(intent);
    }
}