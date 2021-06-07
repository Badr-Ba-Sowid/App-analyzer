package com.example.appanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle barDrawerToggle;
    DrawerLayout drawerLayout;
    ImageButton logoImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = findViewById(R.id.include1);
        logoImageButton = toolbar.findViewById(R.id.logo_image_button);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        barDrawerToggle.syncState();
        drawerLayout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.setDrawerIndicatorEnabled(true);
        setSupportActionBar(toolbar);
        if(getIntent().getExtras()!=null) {
           if(getIntent().getExtras().getBoolean(Config.KIDS_MODE_ON)){
               Snackbar.make(drawerLayout , "Kids mode on" , Snackbar.LENGTH_SHORT).show();
            }
        }

        getSupportActionBar().setTitle(null);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext().getApplicationContext());

        if(sharedPref.getBoolean("PARENTAL_CONTROLS_SWITCH" , false)){
            logoImageButton.setImageDrawable(getDrawable(R.drawable.ic_logo_kids));
            Toast.makeText(getApplicationContext() , "on" , Toast.LENGTH_LONG).show();

        }else{
            logoImageButton.setImageDrawable(getDrawable(R.drawable.ic_appify_logo_30x42));
            Toast.makeText(getApplicationContext() , "off" , Toast.LENGTH_LONG).show();

        }


        getFragment(new ExploreRecommendations());


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.settings_menu_item){
                    setSearchViewGone();
                    getFragment(new SettingsFragment());
                    // TODO add code
                }if(menuItem.getItemId()==R.id.search_menu_item){
                    setSearchViewVisible();
                    getFragment(new Search());
                }if(menuItem.getItemId()==R.id.app_analyzer_menu_item){
                    setSearchViewGone();
                    // TODO add code
                }if(menuItem.getItemId()==R.id.home_menu_item){
                    setSearchViewGone();
                    getFragment(new ExploreRecommendations());
                }
                return true;
            }
        });
    }
    private void getFragment (Fragment fragment) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.main_frame_layout, fragment);
        ft.commit();
    }

    void setSearchViewVisible(){
        toolbar.findViewById(R.id.search_bar).animate().alpha(1.0f).setDuration(200);
        toolbar.findViewById(R.id.logo_image_button).animate().alpha(0.0f).setDuration(200);
        toolbar.findViewById(R.id.logo_image_button).setVisibility(View.GONE);
        toolbar.findViewById(R.id.search_bar).setVisibility(View.VISIBLE);


    }

    void setSearchViewGone(){
        toolbar.findViewById(R.id.search_bar).animate().alpha(0.0f).setDuration(200);
        toolbar.findViewById(R.id.logo_image_button).animate().alpha(1.0f).setDuration(200);
        toolbar.findViewById(R.id.search_bar).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo_image_button).setVisibility(View.VISIBLE);
    }

}