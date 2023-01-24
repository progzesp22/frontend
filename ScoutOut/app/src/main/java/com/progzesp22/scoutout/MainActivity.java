package com.progzesp22.scoutout;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.progzesp22.scoutout.databinding.ActivityMainBinding;
import com.progzesp22.scoutout.domain.UserModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSIONS = 200;
    public static RequestInterface requestHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean mock_requests = false;
        if(mock_requests){
            requestHandler = new MockRequestHandler();
        } else{
            requestHandler = new RequestHandler(getApplicationContext());
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        appBarConfiguration = new AppBarConfiguration.Builder().setOpenableLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);

        setupHamburgerMenu(navController);


    }

    private void setupHamburgerMenu(NavController navController) {
        // Powinno być ustawione na pierwszy ekran który ma być widoczny po otworzeniu aplikacji
        binding.navView.setCheckedItem(R.id.loginFragment);

        UserModel userModel = new ViewModelProvider(this).get(UserModel.class);

        userModel.getUserType().observe(this, userType -> {
            binding.navView.getMenu().clear();
            if (userType == null) {
                binding.navView.inflateMenu(R.menu.drawer_menu_default);
            } else if (userType == UserModel.UserType.PLAYER) {
                binding.navView.inflateMenu(R.menu.drawer_menu_player);
            } else if (userType == UserModel.UserType.GM) {
                binding.navView.inflateMenu(R.menu.drawer_menu_gm);
            }
        });

        binding.navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.loginFragment) {
                navController.navigate(R.id.loginFragment);
            } else if (item.getItemId() == R.id.listTasksFragment) {
                navController.navigate(R.id.listTasksFragment);
            } else if (item.getItemId() == R.id.GMlistTasksFragment) {
                navController.navigate(R.id.GMlistTasksFragment);
            } else if (item.getItemId() == R.id.GMqrGeneratorFragment) {
                navController.navigate(R.id.GMqrGeneratorFragment);
            } else if (item.getItemId() == R.id.GMListToAcceptFragment) {
                navController.navigate(R.id.GMListToAcceptFragment);
            } else if (item.getItemId() == R.id.playerTeamRankingsFragment){
                navController.navigate(R.id.playerTeamRankingsFragment);
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}