package com.sansang.todaysapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Teams.TeamAddActivity;

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
        Toolbar toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        
        AppCompatImageView img_popup = findViewById(R.id.popup_iv);
        img_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.main_toolbar_menu);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();
            }
        });

        AppCompatTextView txt_title = findViewById(R.id.custom_tv);
        txt_title.setText("Main Activity Toolbar");

        AppCompatImageView img_setting = findViewById(R.id.setting_iv);
        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "설정을 선택하였습나다.", Toast.LENGTH_SHORT).show();
            }
        });
        
        AppCompatImageView img_notification = findViewById(R.id.notification_iv);
        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "알림을 선택하였습나다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "site list go to", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.team_list_option:
                Intent intent_teamadd = new Intent(getApplicationContext(), TeamAddActivity.class);
                startActivity(intent_teamadd);
                finish();

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