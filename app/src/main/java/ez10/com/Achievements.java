package ez10.com;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Achievements extends Fragment {

    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;

    private RelativeLayout[] friendsLayout;
    private ImageView[] friendsProfilePic;
    private TextView[] friendsNames;
    private TextView[] friendsStatus;
    private TextView noFriendsTxt;
    private LottieAnimationView anim;
    private final int MAX_NO_OF_FRIENDS = 4;

    public Achievements() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.achievements, container, false);
    }
}