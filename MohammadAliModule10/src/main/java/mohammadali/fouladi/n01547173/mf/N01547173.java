package mohammadali.fouladi.n01547173.mf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
// MohammadAli Fouladi N01547173
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link N01547173#newInstance} factory method to
 * create an instance of this fragment.
 */
public class N01547173 extends Fragment {
    //MohammadAli Fouladi N01547173
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] videoUrls = {
            "https://youtu.be/17Rw1Lu_iWA?si=JIXnAMe9GjC2So4a",
            "https://youtu.be/Qpf3xPSv4Mw?si=uHqaR1bcFzyk3Tv6",
            "https://youtu.be/elTgqUW-NYE?si=qu1b69XPjQaLyXSw"
    };

    public N01547173() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment N01547173.
     */
    // TODO: Rename and change types and number of parameters
    public static N01547173 newInstance(String param1, String param2) {
        N01547173 fragment = new N01547173();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_n01547173, container, false);

        TextView studentInfoTextView = view.findViewById(R.id.MoeIdFragTV);
        studentInfoTextView.setText(R.string.Fullnameid);

        RecyclerView videoRecyclerView = view.findViewById(R.id.MoevideoRView);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        WebView videoWebView = view.findViewById(R.id.MoevideoWebView);
        videoWebView.setWebViewClient(new WebViewClient());
        videoWebView.getSettings().setJavaScriptEnabled(true);

        String[] videoDescriptions = getResources().getStringArray(R.array.Moevideo_descriptions);
        VideoAdapter adapter = new VideoAdapter(videoDescriptions, videoUrls, videoWebView);
        videoRecyclerView.setAdapter(adapter);

        return view;
    }
}
