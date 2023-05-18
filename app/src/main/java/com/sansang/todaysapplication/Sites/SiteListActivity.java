package com.sansang.todaysapplication.Sites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sansang.todaysapplication.Adapter.SitesAdapter;
import com.sansang.todaysapplication.Contents.SiteContents;
import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.R;

import java.util.ArrayList;

public class SiteListActivity extends AppCompatActivity {
    private ArrayList<SiteContents> list;
    private RecyclerView mRecyclerView;
    private SitesAdapter sitesAdapter;
    TodayDatabase todayDatabase;
    SQLiteDatabase sqLiteDatabase;
    SiteController siteController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);

        todayDatabase = new TodayDatabase(this);
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        siteController = new SiteController(this);

        initView();

    }

    @SuppressLint("CutPasteId")
    private void initView() {
        Toolbar listToolbar = findViewById(R.id.customToolbar);
        listToolbar.setTitle("현장 리스트 확인");
        setSupportActionBar(listToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview_site);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        mRecyclerView.setLayoutManager(layoutManager);

        getSiteRecyclerView();

    }

    public void goToMainActivity(){

        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getSiteRecyclerView(){
        list = new ArrayList<>();
        siteController.open();
        list = siteController.getAllSiteList();

        sitesAdapter = new SitesAdapter(this.list);
        mRecyclerView.setAdapter(sitesAdapter);
        sitesAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_add_list:
                Intent intent_add = new Intent(getApplicationContext(), SiteAddActivity.class);
                startActivity(intent_add);

                Toast.makeText(getApplicationContext(),
                        "팀 추가하기로 이동합니다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.toolbar_table_list:
                Intent intent_table = new Intent(getApplicationContext(), SiteTableActivity.class);
                intent_table.putExtra("tmLeader", sitesAdapter.getItemId(1));
                startActivity(intent_table);
                finish();

                return true;

            case R.id.toolbar_close_list:

                finish();
                return true;
            default:
                return false;
        }
    }
}