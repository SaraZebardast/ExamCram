package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SelectUniversity extends AppCompatActivity {

    private TextView errorMessages, spinnerFormat;
    private LinearLayout errorMessagesLayout;
    private LottieAnimationView anim;
    private Spinner universityChoices, countryChoices;
    private ArrayAdapter<CharSequence> universityAdapter, countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_country);
        errorMessages = findViewById(R.id.errorMessages);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);
        countryChoices = findViewById(R.id.country);
        spinnerFormat = findViewById(R.id.spinner_format);
        anim = findViewById(R.id.animationlocation);


        countryAdapter = ArrayAdapter.createFromResource(this, R.array.countries, R.layout.spinner_layout);
        countryChoices.setAdapter(countryAdapter);

        universityAdapter = ArrayAdapter.createFromResource(this, R.array.uni, R.layout.disabled_spinner_layout);
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        countryChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universityChoices = findViewById(R.id.university);

                String selectedCountry = countryChoices.getSelectedItem().toString().trim();


                if (selectedCountry.equals("Turkey")) {

                    universityChoices.setClickable(true);
                    universityChoices.setEnabled(true);
                    universityAdapter = ArrayAdapter.createFromResource(SelectUniversity.this, R.array.turk_unis, R.layout.spinner_layout);
                    universityChoices.setAdapter(universityAdapter);

                } else if (selectedCountry.equals("USA")) {

                    universityChoices.setClickable(true);
                    universityChoices.setEnabled(true);
                    universityAdapter = ArrayAdapter.createFromResource(SelectUniversity.this, R.array.usa_unis, R.layout.spinner_layout);
                    universityChoices.setAdapter(universityAdapter);

                } else if (selectedCountry.equals("United Kingdom")) {

                    universityChoices.setClickable(true);
                    universityChoices.setEnabled(true);
                    universityAdapter = ArrayAdapter.createFromResource(SelectUniversity.this, R.array.uk_unis, R.layout.spinner_layout);
                    universityChoices.setAdapter(universityAdapter);

                } else if (selectedCountry.equals("Germany")) {

                    universityChoices.setClickable(true);
                    universityChoices.setEnabled(true);
                    universityAdapter = ArrayAdapter.createFromResource(SelectUniversity.this, R.array.germany_unis, R.layout.spinner_layout);
                    universityChoices.setAdapter(universityAdapter);

                }
                else {
                    universityChoices.setClickable(false);
                    universityChoices.setEnabled(false);
                    universityAdapter = ArrayAdapter.createFromResource(SelectUniversity.this, R.array.uni, R.layout.disabled_spinner_layout);
                    universityChoices.setAdapter(universityAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                universityChoices.setClickable(false);
                universityChoices.setEnabled(false);
            }
        });



    }

    public void onNext (View view) {

        String selectedCountry = countryChoices.getSelectedItem().toString().trim();
        String selectedUniversity = universityChoices.getSelectedItem().toString().trim();
        if (!(selectedCountry.equals(null) || selectedCountry.equals("Country") || selectedCountry.equals(null) || selectedUniversity.equals("University"))) {
            SignUp.setLocation(selectedCountry, selectedUniversity);
            startActivity(new Intent(this, SignUp.class));
        }
        else {
            errorMessages.setText("Please enter valid info.");
            anim.setVisibility(View.INVISIBLE);
            errorMessagesLayout.setVisibility(View.VISIBLE);
            return;
        }
    }
}