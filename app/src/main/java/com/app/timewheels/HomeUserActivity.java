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
import android.widget.ImageView;
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

import com.app.timewheels.databinding.ActivityHomeUserBinding;
import com.squareup.picasso.Picasso;

public class HomeUserActivity extends AppCompatActivity {
    SharedPreferences sh;
    Boolean doubleBackToExitPressedOnce = false;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setSupportActionBar(binding.appBarHomeUser.toolbaruser);

        if (sh.getString("logincount", "").equalsIgnoreCase("no")){
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Welcome "+sh.getString("name", ""), Snackbar.LENGTH_LONG)
                    .setAction("CLOSE",null).show();
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("logincount", "yes");
            editor.commit();

        }


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        View headerview = navigationView.getHeaderView(0);
        LinearLayout ln = headerview.findViewById(R.id.header_user);
        TextView name = ln.findViewById(R.id.navUsername);
        TextView email = ln.findViewById(R.id.textView);
        ImageView imgv = ln.findViewById(R.id.imageView);
        name.setText(sh.getString("name", ""));
        email.setText(sh.getString("email", ""));
        String imgurl = sh.getString("url", "")+sh.getString("profilepic", "");
        Picasso.with(getApplicationContext()).load(imgurl).transform(new CircleTransform()).into(imgv);

        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Profile.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_user);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home){
                    finish();
                    Intent i = new Intent(getApplicationContext(), HomeUserActivity.class);
                    startActivity(i);
                }
                if (id == R.id.nav_searchbus){
                    Intent i = new Intent(getApplicationContext(), SearchBusActivity.class);
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (id == R.id.nav_feedbacks){
                    Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (id == R.id.nav_complaints){
                    Intent i = new Intent(getApplicationContext(), ViewComplaintsActivity.class);
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (id == R.id.nav_search_long_bus){

                    startActivity(new Intent(getApplicationContext(), SearchLongBusActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (id == R.id.nav_view_bookings){
                    startActivity(new Intent(getApplicationContext(), ViewBookingsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_user, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_user);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
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
                            Intent j = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(j);

                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
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