package ez10.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;


public class CourseSelect extends AppCompatActivity {

    //variables
    Dialog dialog;
    Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3, adapter4, adapter5;
    SelectCourses c = new SelectCourses();
    ArrayList<String> subCourses = new ArrayList<String>(); // needs to be saved in the data base


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_select);

        // spinner 1
        spinner1 = findViewById(R.id.course1chosen);
        adapter1 = ArrayAdapter.createFromResource(this, findCorrectArrays(c.courses[0]),R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // saving th sub course chosen in an array list
                // need to save these in the data base somehow!!
                String sub = spinner1.getSelectedItem().toString().trim();
                subCourses.add(sub);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // think it is fine to leave empty
                // not sure
            }
        });

        // spinner 2
        spinner2 = findViewById(R.id.course2chosen);
        adapter2 = ArrayAdapter.createFromResource(this, findCorrectArrays(c.courses[1]),R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // saving th sub course chosen in an array list
                // need to save these in the data base somehow!!
                String sub = spinner2.getSelectedItem().toString().trim();
                subCourses.add(sub);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // think it is fine to leave empty
                // not sure
            }
        });

        //spinner3
        spinner3 = findViewById(R.id.course3chosen);
        adapter3 = ArrayAdapter.createFromResource(this, findCorrectArrays(c.courses[2]),R.layout.spinner_layout);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // saving th sub course chosen in an array list
                // need to save these in the data base somehow!!
                String sub = spinner3.getSelectedItem().toString().trim();
                subCourses.add(sub);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // think it is fine to leave empty
                // not sure
            }
        });

        // spinner 4
        spinner4 = findViewById(R.id.course4chosen);
        adapter4 = ArrayAdapter.createFromResource(this, findCorrectArrays(c.courses[3]),R.layout.spinner_layout);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // saving th sub course chosen in an array list
                // need to save these in the data base somehow!!
                String sub = spinner4.getSelectedItem().toString().trim();
                subCourses.add(sub);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // think it is fine to leave empty
                // not sure
            }
        });

        //spinner 5
        spinner5 = findViewById(R.id.course5chosen);
        adapter5 = ArrayAdapter.createFromResource(this, findCorrectArrays(c.courses[4]),R.layout.spinner_layout);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // saving th sub course chosen in an array list
                // need to save these in the data base somehow!!
                String sub = spinner5.getSelectedItem().toString().trim();
                subCourses.add(sub);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // think it is fine to leave empty
                // not sure
            }
        });

    }

    public void displaySettings(View view) {
        if (dialog!=null) dialog.hide();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    public int findCorrectArrays (String name) {
        if(name.equals("MATH101")) {
            return R.array.Math101;
        }
        else if (name.equals("MATH102")) {
            return R.array.Math102;
        }
        else if (name.equals("MATH132")) {
            return  R.array.Math132;
        }
        else if (name.equals("CS101")) {
            return R.array.CS101;
        }
        else if (name.equals("CS102")) {
            return R.array.CS102;
        }
        else if (name.equals("CS115")) {
            return R.array.CS115;
        }
        else if (name.equals("PHYS101")) {
            return R.array.PHYS101;
        }
        else if (name.equals("PHYS102")) {
           return R.array.PHYS102;
        }
        else if (name.equals("CS201")) {
            return R.array.CS201;
        }
        else if (name.equals("CS202")) {
            return R.array.CS202;
        }
        else if (name.equals("ENG101")) {
            return R.array.ENG101;
        }
        else{ //  (name.equals("ENG102"))
            return R.array.ENG102;
        }
    }
}