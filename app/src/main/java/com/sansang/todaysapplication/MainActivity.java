package com.sansang.todaysapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Sites.SiteListActivity;
import com.sansang.todaysapplication.Teams.TeamListActivity;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbarView();
    }

    @SuppressLint("SetTextI18n")
    private void initToolbarView() {
        Toolbar communityToolbar = findViewById(R.id.mainCustomToolbar);
        communityToolbar.setTitle("홈 오늘은");
        setSupportActionBar(communityToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        communityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.inflate(R.menu.main_toolbar_menu);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.journal_list_option:
                Toast.makeText(this, "journal list go to", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.income_list_option:
                Toast.makeText(this, "income list go to", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.site_list_option:
                Intent intent_siteList = new Intent(getApplicationContext(), SiteListActivity.class);
                startActivity(intent_siteList);
                //finish();

                Toast.makeText(this, "site add go to", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.team_list_option:
                Intent intent_teamadd = new Intent(getApplicationContext(), TeamListActivity.class);
                startActivity(intent_teamadd);
                //finish();

                Toast.makeText(this, "team add go to", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settling_table_option:
                Toast.makeText(this, "setting go to", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.main_close_option:
                Toast.makeText(this, "App Finished", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return false;
        }

    }
}