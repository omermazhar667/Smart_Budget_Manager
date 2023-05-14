package com.testing.smartbudgetmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.testing.smartbudgetmanager.fragments.adapters.ViewPagerAdapter;
import com.testing.smartbudgetmanager.fragments.expenses.DailyFragment;
import com.testing.smartbudgetmanager.fragments.expenses.MonthlyFragment;
import com.testing.smartbudgetmanager.fragments.expenses.YeralyFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
    }

    private void initialization(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);



        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new DailyFragment());
        fragmentList.add(new MonthlyFragment());
        fragmentList.add(new YeralyFragment());

        List<String> titleList = new ArrayList<>();
        titleList.add("Daily");
        titleList.add("Monthly");
        titleList.add("Yearly");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}