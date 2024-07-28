package mohammadali.fouladi.n01547173.mf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class M4f extends Fragment {

    private static final int LOCATION_SETTINGS_REQUEST_CODE = 2;
    private AdView adView;
    private int adClickCounter = 0;
    private FusedLocationProviderClient fusedLocationClient;

    // Permissions launcher
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted != null && fineLocationGranted) {
                    getLocation();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    getLocation();
                } else {
                    showSnackbarWithSettingsOption();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m4f, container, false);

        Button locationButton = view.findViewById(R.id.buttonLocation);
        adView = view.findViewById(R.id.adView);

        MobileAds.initialize(getContext(), initializationStatus -> {
        });
        List<String> testDeviceIds = Arrays.asList(AdRequest.DEVICE_ID_EMULATOR, "B3EEABB8EE11C2BE770B684D95219ECB");
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build();
        MobileAds.setRequestConfiguration(configuration);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClicked() {
                adClickCounter++;
                Toast.makeText(getContext(), getString(R.string.mohammadali_fouladi) + adClickCounter, Toast.LENGTH_LONG).show();
            }
        });

        locationButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
            } else {
                getLocation();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        return view;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        showLocationSnackbar(location);
                    } else {
                        showSnackbar("Location not determined", Snackbar.LENGTH_INDEFINITE);
                    }
                })
                .addOnFailureListener(e -> showSnackbar("Error retrieving location", Snackbar.LENGTH_INDEFINITE));
    }

    private void showLocationSnackbar(Location location) {
        Snackbar snackbar = Snackbar.make(getView(), "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("DISMISS", v -> snackbar.dismiss());
        snackbar.show();
    }

    private void showSnackbar(String message, int duration) {
        Snackbar snackbar = Snackbar.make(getView(), message, duration);
        snackbar.setAction("DISMISS", v -> snackbar.dismiss());
        snackbar.show();
    }

    private void showSnackbarWithSettingsOption() {
        Snackbar snackbar = Snackbar.make(getView(), "Permission denied. Enable it from settings.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("SETTINGS", v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        snackbar.show();
    }
}
