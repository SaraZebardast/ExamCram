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


public class Results extends Fragment {

    private View viewMain;

    private ImageView[] resultsProfilePic;
    private TextView[] resultsName;
    private ImageView[] resultsMessage;
    private ImageView[] resultsAdd;
    private RelativeLayout[] resultsLayout;

    private int MAX_NO_OF_RESULTS = 5;

    public Results() {
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

        resultsProfilePic = new ImageView[5];
        resultsName = new TextView[5];
        resultsMessage = new ImageView[5];
        resultsAdd = new ImageView[5];
        resultsLayout= new RelativeLayout[5];

        for (int i = 0; i<MAX_NO_OF_RESULTS; i++) {

            String tempID = "result" + i + "name";
            int resID = getResources().getIdentifier(tempID, "id", getContext().getPackageName());
            resultsName[i] = (TextView) view.findViewById(resID);

            tempID = "result" + i + "Layout";
            resID = getResources().getIdentifier(tempID, "id", getContext().getPackageName());
            resultsLayout[i] = (RelativeLayout) view.findViewById(resID);

            tempID = "result" + i + "ProfilePicture";
            resID = getResources().getIdentifier(tempID, "id", getContext().getPackageName());
            resultsProfilePic[i] = (ImageView) view.findViewById(resID);

            tempID = "message" + i;
            resID = getResources().getIdentifier(tempID, "id", getContext().getPackageName());
            resultsMessage[i] = (ImageView) view.findViewById(resID);
            resultsMessage[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "This feature is yet to come :(", Toast.LENGTH_SHORT).show();
                }
            });

            resultsProfilePic[i] = view.findViewById(R.id.result0ProfilePicture);
            tempID = "addFriend" + i;
            resID = getResources().getIdentifier(tempID, "id", getContext().getPackageName());
            resultsAdd[i] = (ImageView) view.findViewById(resID);
            resultsAdd[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Friend Request Sent", Toast.LENGTH_SHORT).show();
                }
            });

        }



    }
}