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

public class profileselection extends AppCompatActivity implements View.OnClickListener {
    private ImageSwitcher switchImageView;
    private final Integer[] ListImage = {R.drawable.steve, R.drawable.marya, R.drawable.einstein};
    private final Integer imageLength = ListImage.length;
    private Integer counter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileselection);

        switchImageView = findViewById(R.id.imageSwitcher);
        MaterialButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        switchImageView.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());

                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT
                        , ActionBar.LayoutParams.WRAP_CONTENT));

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageResource(R.drawable.steve);

                return imageView;
            }
        });

    Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
    Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        switchImageView.setOutAnimation(out);
        switchImageView.setInAnimation(in);
    }
    @Override
    public void onClick(View view){
        counter++;

        if (counter.equals(imageLength)) {
            counter =0;
            switchImageView.setImageResource(ListImage[counter]);
        }
        switchImageView.setImageResource(ListImage[counter]);
    }
}