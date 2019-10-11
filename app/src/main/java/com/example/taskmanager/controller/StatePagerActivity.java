package com.example.taskmanager.controller;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Repository;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.UUID;

public class StatePagerActivity extends AppCompatActivity
        implements TaskCreateFragment.NoticeDialogListenerCreate, TaskEditFragment.NoticeDialogListenerEdit
        , NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPagerState;
    private TabLayout mTabLayout;
    private FloatingActionButton mFloatingActionButton;
    private StatePagerAdapter mStatePagerAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private TextView mTextViewUserName;

    // private int selectedTabPosition;
    private User mUser;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, StatePagerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid = Repository.getInstance(this).getSessionUserID();
        mUser = Repository.getInstance(this).getUser(uuid);

        setContentView(R.layout.navigation_drawer);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mNavigationView = findViewById(R.id.navigation);
        Menu navigationMenu = mNavigationView.getMenu();
        MenuItem allUser = navigationMenu.findItem(R.id.all_user);
        allUser.setVisible(false);
        if (Repository.getInstance(this).getAdminID() != null &&
                Repository.getInstance(this).getSessionUserID().toString().equals(Repository.getInstance(this).getAdminID().toString())) {
            allUser.setVisible(true);
        }
        mNavigationView.setNavigationItemSelectedListener(this);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        initUi();

        View headerView = mNavigationView.getHeaderView(0);
        mTextViewUserName = headerView.findViewById(R.id.text_view_username);
        mTextViewUserName.setText(mUser.getUsername());

        setPagerAdapter();
        //        mViewPagerState.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                selectedTabPosition = position;
//            }
//        });

        mFloatingActionButton.setOnClickListener(view -> {
            TaskCreateFragment taskCreateFragment = TaskCreateFragment.newInstance();
            taskCreateFragment.show(getSupportFragmentManager(), "m");
        });

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mTabLayout.setBackgroundColor(ContextCompat.getColor(StatePagerActivity.this, R.color.tab1));
                    mViewPagerState.setBackgroundResource(R.drawable.bg_blue);
                } else if (tab.getPosition() == 1) {
                    mTabLayout.setBackgroundColor(ContextCompat.getColor(StatePagerActivity.this, R.color.tab2));
                    mViewPagerState.setBackgroundResource(R.drawable.bg_purpel);
                } else {
                    mTabLayout.setBackgroundColor(ContextCompat.getColor(StatePagerActivity.this, R.color.tab3));
                    mViewPagerState.setBackgroundResource(R.drawable.bg_green);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void setPagerAdapter() {

        mStatePagerAdapter = new StatePagerAdapter(getSupportFragmentManager()
                , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPagerState.setAdapter(mStatePagerAdapter);
        // mViewPagerState.setCurrentItem(selectedTabPosition);

    }

    private void initUi() {
        mViewPagerState = findViewById(R.id.state_view_pager);
        mTabLayout = findViewById(R.id.state_tab_layout);
        mFloatingActionButton = findViewById(R.id.floating_action_button_create_task);
        mTabLayout.setupWithViewPager(mViewPagerState);
    }

    @Override
    public void onDialogPositiveClickCreateFragment(DialogFragment dialog) {
        mStatePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClickEditFragment(DialogFragment dialog) {
        mStatePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_log_out:
                Repository.getInstance(this).setSessionUserID(null);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.delete_all_task:
                AlertDialog dialog = askOptiaon();
                dialog.show();
                break;
            case R.id.all_user:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private AlertDialog askOptiaon() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete all tasks?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Repository.getInstance(this).deleteAllTask();
                    mStatePagerAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .create();
        return myQuittingDialogBox;
    }

    public class StatePagerAdapter extends FragmentStatePagerAdapter {


        public StatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                return TaskListFragment.newInstance(Task.State.Todo);
            } else if (position == 1) {

                return TaskListFragment.newInstance(Task.State.Doing);
            } else {
                return TaskListFragment.newInstance(Task.State.Done);
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Todo";
            else if (position == 1) return "Doing";
            else return "Done";
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            // POSITION_NONE means something like: this fragment is no longer valid
            // triggering the ViewPager to re-build the instance of this fragment.
            return POSITION_NONE;
        }
    }

}
