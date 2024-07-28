package mohammadali.fouladi.n01547173.mf;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FouladiActivity10 extends AppCompatActivity implements NavigationBarView.OnItemReselectedListener {
    private BottomNavigationView bottomNavigationView;
    private Mo1e moe;
    private Fou1adi fouladi;
    private N01547173 thirdFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.MoebottomNavigationView);

        moe = new Mo1e();
        fouladi = new Fou1adi();
        loadFragment(moe);
// MohammadAli Fouladi N01547173
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.Mo1e) {
                    selectedFragment = moe;
                } else if (item.getItemId() == R.id.Fou1adi) {
                    selectedFragment = fouladi;}
                   else if (item.getItemId() == R.id.N015437173) {
                    selectedFragment = thirdFragment;
            }
//                else if (item.getItemId() == R.id.Moe) {
//                    selectedFragment = moeFragment;


                if(selectedFragment!=null)
                {   loadFragment(selectedFragment);}

                return true;
            }
        });
}
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MoeflFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }
}


