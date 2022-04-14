package ez10.com;

import com.google.android.material.button.MaterialButton;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class profileselection extends AppCompatActivity implements View.onClickListener{
    private ImageSwitcher imageSwitcher;
    private MaterialButton nextButton;
    private Integer ListImage[] = {R.drawable.steve, R.drawable.marya, R.drawable.einstein};
    private Integer imageLength = ListImage.length;
    private Integer counter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileselection);

        imageSwitcher = findViewById(R.id.imageSwitcher);
        nextButton = findViewById(R.id.nextButton);

        public View makeView() {

        });
    }
    protected void onClick(View view){

    }
}