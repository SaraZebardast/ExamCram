package ez10.com;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Results extends Fragment {

    private View viewMain;

    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;

    private ImageView[] resultsProfilePic;
    private TextView[] resultsName;
    private ImageView[] resultsMessage;
    private ImageView[] resultsAdd;
    private RelativeLayout[] resultsLayout;

    LottieAnimationView anim, noResultsANIM;
    TextView noResultsTXT;

    private int MAX_NO_OF_RESULTS = 5;
    String selectedCourse, selectedTopic;

    public Results(String selectedCourse, String selectedTopic) {
        this.selectedCourse = selectedCourse;
        this.selectedTopic = selectedTopic;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewMain = inflater.inflate(R.layout.homepage, container, false);
        return inflater.inflate(R.layout.results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = SplashScreen.mAuth.getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();

        anim = view.findViewById(R.id.loadingResults);
        noResultsANIM = view.findViewById(R.id.noResults);
        noResultsTXT = view.findViewById(R.id.noResultsTXT);


        resultsProfilePic = new ImageView[5];
        resultsName = new TextView[5];
        resultsMessage = new ImageView[5];
        resultsAdd = new ImageView[5];
        resultsLayout= new RelativeLayout[5];

        resultsLayout[0] = view.findViewById(R.id.result0Layout);
        resultsProfilePic[0] = view.findViewById(R.id.result0ProfilePicture);
        resultsName[0] = view.findViewById(R.id.result0name);
        resultsAdd[0] = view.findViewById(R.id.addFriend0);
        resultsMessage[0] = view.findViewById(R.id.message0);

        resultsLayout[1] = view.findViewById(R.id.result1Layout);
        resultsProfilePic[1] = view.findViewById(R.id.result1ProfilePicture);
        resultsName[1] = view.findViewById(R.id.result1name);
        resultsAdd[1] = view.findViewById(R.id.addFriend1);
        resultsMessage[1] = view.findViewById(R.id.message1);

        resultsLayout[2] = view.findViewById(R.id.result2Layout);
        resultsProfilePic[2] = view.findViewById(R.id.result2ProfilePicture);
        resultsName[2] = view.findViewById(R.id.result2name);
        resultsAdd[2] = view.findViewById(R.id.addFriend2);
        resultsMessage[2] = view.findViewById(R.id.message2);

        resultsLayout[3] = view.findViewById(R.id.result3Layout);
        resultsProfilePic[3] = view.findViewById(R.id.result3ProfilePicture);
        resultsName[3] = view.findViewById(R.id.result3name);
        resultsAdd[3] = view.findViewById(R.id.addFriend3);
        resultsMessage[3] = view.findViewById(R.id.message3);

        resultsLayout[4] = view.findViewById(R.id.result4Layout);
        resultsProfilePic[4] = view.findViewById(R.id.result4ProfilePicture);
        resultsName[4] = view.findViewById(R.id.result4name);
        resultsAdd[4] = view.findViewById(R.id.addFriend4);
        resultsMessage[4] = view.findViewById(R.id.message4);



        loadResults();

    }

    int x=0;

    public void loadResults() {

        DatabaseReference reference = rootNode.getReference("Registered Universities/" + Main.userUniversity + "/" + selectedCourse + "/" + selectedTopic);
//        Toast.makeText(getContext(), Main.userUniversity + "/" + selectedCourse + "/" + selectedTopic, Toast.LENGTH_SHORT).show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot resultIndex : snapshot.getChildren()) {
                        if (!resultIndex.getKey().equals(currentUser.getUid())) {
                            DatabaseReference reference = rootNode.getReference("Registered Users/" + resultIndex.getKey());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    resultsLayout[x].setVisibility(View.VISIBLE);
                                    resultsName[x].setText(snapshot.child("firstName").getValue() + "");
                                    String value = snapshot.child("profilePictureID").getValue() + "";
                                    if (value.equals("0")) {
                                        resultsProfilePic[x].setImageResource(R.drawable.steve);
                                    }
                                    else if (value.equals("1")) {
                                        resultsProfilePic[x].setImageResource(R.drawable.rosan);
                                    }
                                    else if (value.equals("2")){
                                        resultsProfilePic[x].setImageResource(R.drawable.isac);
                                    }
                                    else if (value.equals("3")){
                                        resultsProfilePic[x].setImageResource(R.drawable.davinci);
                                    }
                                    else if (value.equals("4")){
                                        resultsProfilePic[x].setImageResource(R.drawable.einstien);
                                    }
                                    resultsAdd[x].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            resultsAdd[x].setColorFilter(viewMain.getResources().getColor(R.color.faded_white));
                                            Toast.makeText(getContext(), "Friend request sent.", Toast.LENGTH_SHORT).show();
                                            DatabaseReference reference = rootNode.getReference("Registered Users/" + snapshot.getKey() + "/friendRequests");
                                            reference.child(currentUser.getUid()+"").setValue("");
                                        }
                                    });
                                    noResultsANIM.setVisibility(View.INVISIBLE);
                                    noResultsTXT.setVisibility(View.INVISIBLE);
                                    x++;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                    anim.setVisibility(View.INVISIBLE);
                }
                if (resultsLayout[0].getVisibility()==View.INVISIBLE){
                    noResultsANIM.setVisibility(View.VISIBLE);
                    noResultsTXT.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}