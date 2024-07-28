package mohammadali.fouladi.n01547173.mf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
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
    private static final String CHANNEL_ID = "location_notification_channel";
    private static final int NOTIFICATION_ID = 1;

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
        showSystemUI();

        MobileAds.initialize(getContext(), initializationStatus -> {});
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
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                Toast.makeText(getContext(), "Ad failed to load: " + adError.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded() {
                Toast.makeText(getContext(), "Ad loaded successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdImpression() {
                Toast.makeText(getContext(), "Ad impression recorded", Toast.LENGTH_SHORT).show();
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
                        showLocationNotification(location.getLatitude(), location.getLongitude());
                        showLocationSnackbar(location);
                    } else {
                        showSnackbar("Location not determined", Snackbar.LENGTH_INDEFINITE);
                    }
                })
                .addOnFailureListener(e -> showSnackbar("Error retrieving location", Snackbar.LENGTH_INDEFINITE));
    }

    private void showLocationSnackbar(Location location) {
        View bottomNavigationView = getActivity().findViewById(R.id.MoebottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        hideSystemUI();

        Snackbar snackbar = Snackbar.make(getView(), "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("DISMISS", v -> {
            showSystemUI();
            bottomNavigationView.setVisibility(View.VISIBLE);
            snackbar.dismiss();
        });

        snackbar.show();
    }

    private void showSnackbar(String message, int duration) {
        View bottomNavigationView = getActivity().findViewById(R.id.MoebottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        hideSystemUI();

        Snackbar snackbar = Snackbar.make(getView(), message, duration);
        snackbar.setAction("DISMISS", v -> {
            showSystemUI();
            bottomNavigationView.setVisibility(View.VISIBLE);
            snackbar.dismiss();
        });

        snackbar.show();
    }

    private void showSnackbarWithSettingsOption() {
        View bottomNavigationView = getActivity().findViewById(R.id.MoebottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        hideSystemUI();

        Snackbar snackbar = Snackbar.make(getView(), "Permission denied. Enable it from settings.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("SETTINGS", v -> {
            showSystemUI();
            bottomNavigationView.setVisibility(View.VISIBLE);
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });

        snackbar.show();
    }

    private void showLocationNotification(double latitude, double longitude) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.location_notifications);
            String description = getString(R.string.notifications_for_location_updates);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(getContext(), M4f.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_location)
                .setContentTitle("Location Determined")
                .setContentText("Latitude: " + latitude + ", Longitude: " + longitude)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground));

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
