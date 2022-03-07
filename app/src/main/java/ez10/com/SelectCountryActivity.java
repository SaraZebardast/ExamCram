package ez10.com;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SelectCountryActivity extends AppCompatActivity {

    private Spinner countryChoice, uniChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_country);
        countryChoice.findViewById(R.id.country);
        uniChoice.findViewById(R.id.university);

//        countryChoice.setOnItemClickListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//
//                String items=spinner.getSelectedItem().toString();
//            }
//
//        });
    }

    public void onNext (View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }
}