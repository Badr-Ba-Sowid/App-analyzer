package com.example.appanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = findViewById(R.id.main_tool_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.settings_menu_item){
                    setSearchViewGone();
                    // TODO add code
                }if(menuItem.getItemId()==R.id.search_menu_item){
                    setSearchViewVisible();
                    getFragment(new Search());
                }if(menuItem.getItemId()==R.id.app_analyzer_menu_item){
                    setSearchViewGone();
                    // TODO add code
                }if(menuItem.getItemId()==R.id.home_menu_item){
                    setSearchViewGone();
                    FrameLayout frameLayout = findViewById(R.id.main_frame_layout);
                    frameLayout.removeAllViews();
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