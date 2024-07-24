package mohammadali.fouladi.n01547173.mf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mo1e#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mo1e extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner imageSpinner;
    private Button downloadButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private String[] imageNames = {"Tiger", "Mountain", "Ocean", "Desert"};
    private String[] imageUrls = {
            "https://h5p.org/sites/default/files/h5p/content/1209180/images/file-6113d5f8845dc.jpeg",
            "https://media.cnn.com/api/v1/images/stellar/prod/230908155626-05-mount-fuji-overtourism.jpg",
            "https://thedaily.case.edu/wp-content/uploads/2023/06/underwater-view-768x329.jpg",
            "https://cdn.britannica.com/10/152310-050-5A09D74A/Sand-dunes-Sahara-Morocco-Merzouga.jpg"
    };

    private String selectedImageUrl;
    private ExecutorService executorService;


    public Mo1e() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mo1e.
     */
    // TODO: Rename and change types and number of parameters
    public static Mo1e newInstance(String param1, String param2) {
        Mo1e fragment = new Mo1e();
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
        View view= inflater.inflate(R.layout.fragment_mo1e, container, false);

        imageSpinner = view.findViewById(R.id.Mo1espinner);
        downloadButton = view.findViewById(R.id.Mo1ebutton);
        progressBar = view.findViewById(R.id.Mo1eprogressBar);
        imageView = view.findViewById(R.id.Mo1eimageView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, imageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(adapter);

        imageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedImageUrl = imageUrls[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedImageUrl = null;
            }
        });
        executorService = Executors.newSingleThreadExecutor();

        downloadButton.setOnClickListener(v -> {
            if (selectedImageUrl != null) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                executorService.execute(() -> {
                    try {
                        final Bitmap bitmap = downloadImage(selectedImageUrl);
                        getActivity().runOnUiThread(() -> new Handler().postDelayed(() -> {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                        },5000));
                    } catch (IOException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error downloading image", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private Bitmap downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP status code: " + responseCode);
        }

        InputStream input = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }
}