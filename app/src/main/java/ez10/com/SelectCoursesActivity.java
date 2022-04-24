package ez10.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectCoursesActivity extends AppCompatActivity {

    Spinner courseChoices;
    ArrayAdapter<CharSequence> coursesAdapter;
    TextView course1, course2, course3, course4, course5;
    ImageView remove1, remove2, remove3, remove4, remove5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_courses);

        course1 = findViewById(R.id.course1_name);
        course2 = findViewById(R.id.course2_name);
        course3 = findViewById(R.id.course3_name);
        course4 = findViewById(R.id.course4_name);
        course5 = findViewById(R.id.course5_name);

        remove1 = findViewById(R.id.remove1);
        remove2 = findViewById(R.id.remove2);
        remove3 = findViewById(R.id.remove3);
        remove4 = findViewById(R.id.remove4);
        remove5 = findViewById(R.id.remove5);

        courseChoices = findViewById(R.id.courses_select);
        coursesAdapter = ArrayAdapter.createFromResource(this, R.array.courses, R.layout.spinner_layout);
        courseChoices.setAdapter(coursesAdapter);

        courseChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedCourse = courseChoices.getSelectedItem().toString();

                if (selectedCourse.equals("Select Courses")) {
                    // do nothing
                }
                else if (course1.getText().toString().trim().equals("Empty")) {
                    course1.setText(selectedCourse);
                    course1.setTextColor(getResources().getColor(R.color.white));
                    remove1.setVisibility(View.VISIBLE);
                } else if (course2.getText().toString().trim().equals("Empty")) {
                    course2.setTextColor(getResources().getColor(R.color.white));
                    course2.setText(selectedCourse);
                    remove2.setVisibility(View.VISIBLE);
                } else if (course3.getText().toString().trim().equals("Empty")) {
                    course3.setTextColor(getResources().getColor(R.color.white));
                    course3.setText(selectedCourse);
                    remove3.setVisibility(View.VISIBLE);
                } else if (course4.getText().toString().trim().equals("Empty")) {
                    course4.setTextColor(getResources().getColor(R.color.white));
                    course4.setText(selectedCourse);
                    remove4.setVisibility(View.VISIBLE);
                } else if (course5.getText().toString().trim().equals("Empty")) {
                    course5.setTextColor(getResources().getColor(R.color.white));
                    course5.setText(selectedCourse);
                    remove5.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onNext(View view) {

    }
    public void removeFirstCourse(View view) {
        course1.setText("Empty");
        course1.setTextColor(getResources().getColor(R.color.faded_white));
        remove1.setVisibility(View.INVISIBLE);
    }
    public void removeSecondCourse(View view) {
        course2.setText("Empty");
        course2.setTextColor(getResources().getColor(R.color.faded_white));
        remove2.setVisibility(View.INVISIBLE);
    }
    public void removeThirdCourse(View view) {
        course3.setText("Empty");
        course3.setTextColor(getResources().getColor(R.color.faded_white));
        remove3.setVisibility(View.INVISIBLE);
    }
    public void removeFourthCourse(View view) {
        course4.setText("Empty");
        course4.setTextColor(getResources().getColor(R.color.faded_white));
        remove4.setVisibility(View.INVISIBLE);
    }
    public void removeFifthCourse(View view) {
        course5.setText("Empty");
        course5.setTextColor(getResources().getColor(R.color.faded_white));
        remove5.setVisibility(View.INVISIBLE);
    }
}