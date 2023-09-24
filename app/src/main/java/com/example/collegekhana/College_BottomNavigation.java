package com.example.collegekhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.collegekhana.college.collegeHomeFragment;
import com.example.collegekhana.college.collegeOrdersFragment;
import com.example.collegekhana.college.collegePendingOrdersFragment;
import com.example.collegekhana.college.collegeProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class College_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.collegeHome) {
            fragment = new collegeHomeFragment();
        } else if (itemId == R.id.pendingOrders) {
            fragment = new collegePendingOrdersFragment();
        } else if (itemId == R.id.orders) {
            fragment = new collegeOrdersFragment();
        } else if (itemId == R.id.profile) {
            fragment = new collegeProfileFragment();
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}