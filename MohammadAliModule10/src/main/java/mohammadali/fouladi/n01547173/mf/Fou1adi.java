package mohammadali.fouladi.n01547173.mf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fou1adi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fou1adi extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView temperatureTextView, lonTextView, latTextView, countryTextView, humidityTextView, cityTextView, descTextView;
    private RadioGroup radioGroup;
    private RadioButton celsiusRadioButton, fahrenheitRadioButton;
    private Spinner citySpinner;

    private String selectedUnit = "metric";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String[] cityNames = {"Toronto", "London", "New York", "Tokyo", "Sydney"};
    private double[] lats = {43.70011, 51.5074, 40.7128, 35.6895, -33.8688};
    private double[] lons = {-79.4163, -0.1278, -74.0060, 139.6917, 151.2093};

    private ExecutorService executorService;

    public Fou1adi() {
        // Required empty public constructor
    }
// MohammadAli Fouladi N01547173
    public static Fou1adi newInstance(String param1, String param2) {
        Fou1adi fragment = new Fou1adi();
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

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        selectedUnit = sharedPreferences.getString("unit", "metric");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fou1adi, container, false);

        temperatureTextView = view.findViewById(R.id.MoetemperatureTextView);
        lonTextView = view.findViewById(R.id.MoelonTextView);
        latTextView = view.findViewById(R.id.MoelatTextview);
        countryTextView = view.findViewById(R.id.MoeCountryTextView);
        humidityTextView = view.findViewById(R.id.MoeHumidityTextView);
        cityTextView = view.findViewById(R.id.MoeCityTextView);
        descTextView = view.findViewById(R.id.MoeDescription);

        radioGroup = view.findViewById(R.id.MoeradioGroup);
        celsiusRadioButton = view.findViewById(R.id.MoecelsiusRadioButton);
        fahrenheitRadioButton = view.findViewById(R.id.fahrenheitRadioButton);

        citySpinner = view.findViewById(R.id.MoecitySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        if (selectedUnit.equals("imperial")) {
            fahrenheitRadioButton.setChecked(true);
        } else {
            celsiusRadioButton.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.MoecelsiusRadioButton) {
                selectedUnit = "metric";
            } else if (checkedId == R.id.fahrenheitRadioButton) {
                selectedUnit = "imperial";
            }
            editor.putString("unit", selectedUnit);
            editor.apply();
            fetchWeatherData(citySpinner.getSelectedItemPosition());
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchWeatherData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        executorService = Executors.newSingleThreadExecutor();

        return view;
    }

    private void fetchWeatherData(int position) {
        double lat = lats[position];
        double lon = lons[position];
        String apiKey = "e70ed408515f27caf8a23869a2a1b6de";
        //No calculation for unit needed here haha, the &units will do the job
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=" + selectedUnit + "&appid=" + apiKey;

        executorService.execute(() -> {
            try {
                String response = downloadWeatherData(urlString);
                getActivity().runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject main = jsonObject.getJSONObject("main");
                        JSONObject sys = jsonObject.getJSONObject("sys");
                        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

                        double temp = main.getDouble("temp");
                        int humidity = main.getInt("humidity");
                        double lonResponse = jsonObject.getJSONObject("coord").getDouble("lon");
                        double latResponse = jsonObject.getJSONObject("coord").getDouble("lat");
                        String country = sys.getString("country");
                        String cityName = jsonObject.getString("name");
                        String description = weather.getString("description");

                        temperatureTextView.setText(String.format("Temperature: %.1f %s", temp, selectedUnit.equals("metric") ? "°C" : "℉"));
                        lonTextView.setText(String.format("Lon: %.4f", lonResponse));
                        latTextView.setText(String.format("Lat: %.4f", latResponse));
                        countryTextView.setText(String.format("Country: %s", country));
                        humidityTextView.setText(String.format("Humidity: %d%%", humidity));
                        cityTextView.setText(String.format("City: %s", cityName));
                        descTextView.setText(String.format("Desc: %s", description));
                    } catch (JSONException e) {
                        getActivity().runOnUiThread(() -> {

                            Toast.makeText(getContext(), "Error parsing weather data", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } catch (IOException e) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), R.string.error_fetching_weather_data, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String downloadWeatherData(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP status code: " + responseCode);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }
}
