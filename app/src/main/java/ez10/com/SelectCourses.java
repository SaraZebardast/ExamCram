package ez10.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectCourses extends AppCompatActivity {

    Spinner courseChoices;
    ArrayAdapter<CharSequence> coursesAdapter;
    TextView course1, course2, course3, course4, course5;
    ImageView remove1, remove2, remove3, remove4, remove5;
    private TextView errorMessages;
    private LinearLayout errorMessagesLayout;
    String[] courses = new String[5];


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

        courses[0]= "0"; //0 means null, null probably breaks database im scared to use it
        courses[1]= "0";
        courses[2]= "0";
        courses[3]= "0";
        courses[4]= "0";

        errorMessages = findViewById(R.id.errorMessages);
        errorMessagesLayout = findViewById(R.id.errorMessagesLayout);


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
                else if (course1.getText().toString().trim().equals("Empty") && !contains(selectedCourse)) {
                    courses[0] = selectedCourse;
                    course1.setText(selectedCourse);
                    course1.setTextColor(getResources().getColor(R.color.white));
                    remove1.setVisibility(View.VISIBLE);
                } else if (course2.getText().toString().trim().equals("Empty") && !contains(selectedCourse)) {
                    courses[1] = selectedCourse;
                    course2.setTextColor(getResources().getColor(R.color.white));
                    course2.setText(selectedCourse);
                    remove2.setVisibility(View.VISIBLE);
                } else if (course3.getText().toString().trim().equals("Empty") && !contains(selectedCourse)) {
                    courses[2] = selectedCourse;
                    course3.setTextColor(getResources().getColor(R.color.white));
                    course3.setText(selectedCourse);
                    remove3.setVisibility(View.VISIBLE);
                } else if (course4.getText().toString().trim().equals("Empty") && !contains(selectedCourse)) {
                    courses[3] = selectedCourse;
                    course4.setTextColor(getResources().getColor(R.color.white));
                    course4.setText(selectedCourse);
                    remove4.setVisibility(View.VISIBLE);
                } else if (course5.getText().toString().trim().equals("Empty") && !contains(selectedCourse)) {
                    courses[4] = selectedCourse;
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

        if (!isEmpty()) {
            FirebaseUser currentUser = SplashScreen.mAuth.getCurrentUser();
            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

            DatabaseReference reference = rootNode.getReference("Registered Users/" + currentUser.getUid() + "/userCourses");

            for (int i=0; i<courses.length; i++) {
                reference.child("course" + i).setValue(courses[i]);
            }

            startActivity(new Intent(SelectCourses.this, HomePage.class));

            finishAffinity();
        }
        else {
            errorMessages.setText("You should have atleast one course");
            errorMessagesLayout.setVisibility(View.VISIBLE);
        }
    }
    public void removeFirstCourse(View view) {
        course1.setText("Empty");
        courses[0]= "0";
        course1.setTextColor(getResources().getColor(R.color.faded_white));
        remove1.setVisibility(View.INVISIBLE);
    }
    public void removeSecondCourse(View view) {
        course2.setText("Empty");
        courses[1]= "0";
        course2.setTextColor(getResources().getColor(R.color.faded_white));
        remove2.setVisibility(View.INVISIBLE);
    }
    public void removeThirdCourse(View view) {
        course3.setText("Empty");
        courses[2]= "0";
        course3.setTextColor(getResources().getColor(R.color.faded_white));
        remove3.setVisibility(View.INVISIBLE);
    }
    public void removeFourthCourse(View view) {
        course4.setText("Empty");
        courses[3]= "0";
        course4.setTextColor(getResources().getColor(R.color.faded_white));
        remove4.setVisibility(View.INVISIBLE);
    }
    public void removeFifthCourse(View view) {
        course5.setText("Empty");
        courses[4]= "0";
        course5.setTextColor(getResources().getColor(R.color.faded_white));
        remove5.setVisibility(View.INVISIBLE);
    }

    public boolean contains(String a) {
        for (int i=0; i<courses.length; i++) {
            if (courses[i].equals(a)) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        for (int i=0; i<courses.length; i++) {
            if (!courses[i].equals("0")) return false;
        }
        return true;
    }
}