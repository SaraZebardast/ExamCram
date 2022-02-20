package ez10.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameInput = (EditText) findViewById(R.id.usernameIN);
        passwordInput = (EditText) findViewById(R.id.passwordIN);

    }

    public void onLogin(View view) { //login button onclick lister
        setContentView(R.layout.loading);
        if (usernameInput.getText().toString()=="a") {
            setContentView(R.layout.login);
        }

    }
}