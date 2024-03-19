package com.app.timewheels;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.app.timewheels.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    Intent serviceIntent;
    SharedPreferences sh;
    boolean doubleBackToExitPressedOnce = false;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (sh.getString("logincount", "").equalsIgnoreCase("no")){
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Welcome "+sh.getString("name", ""), Snackbar.LENGTH_LONG)
                    .setAction("CLOSE",null).show();
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("logincount", "yes");
            editor.commit();
        }
//        Start Service for GPS
        if (!sh.getString("busid", "").equalsIgnoreCase("")){
            serviceIntent = new Intent(getApplicationContext(), gpstracker.class);
            startService(serviceIntent);
        }

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        View headerview = navigationView.getHeaderView(0);
        LinearLayout ln = headerview.findViewById(R.id.header);
        TextView name = ln.findViewById(R.id.navUser);
        TextView email = ln.findViewById(R.id.textView);
        name.setText(sh.getString("name", ""));
        email.setText(sh.getString("email", ""));
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (R.id.nav_home == id){
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();

                }
                if (id == R.id.nav_allocatedBus){
                    Intent i = new Intent(getApplicationContext(), AllocateBusActivity.class);
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_logout){
            new AlertDialog.Builder(this)
                    .setTitle("Logout?")
                    .setMessage("Are you sure you want to Logout?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            SharedPreferences.Editor editor = sh.edit();
                            editor.putString("lid", "");
                            editor.putString("type", "");
                            editor.commit();
                            Intent serviceIntent = new Intent(getApplicationContext(), gpstracker.class);
                            stopService(serviceIntent);
                            Intent j = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(j);

                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent serviceIntent = new Intent(getApplicationContext(), gpstracker.class);
            stopService(serviceIntent);
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}