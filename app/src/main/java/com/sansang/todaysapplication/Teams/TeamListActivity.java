package com.sansang.todaysapplication.Teams;

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

import com.sansang.todaysapplication.Adapter.TeamsAdapter;
import com.sansang.todaysapplication.Contents.TeamContents;
import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.TeamController;
import com.sansang.todaysapplication.MainActivity;
import com.sansang.todaysapplication.R;

import java.util.ArrayList;

public class TeamListActivity extends AppCompatActivity {
    private ArrayList<TeamContents> list;
    private RecyclerView rv_team;
    private TeamsAdapter teamsAdapter;
    TodayDatabase todayDatabase;
    SQLiteDatabase sqLiteDatabase;
    TeamController teamController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        initView();

        todayDatabase = new TodayDatabase(this);
        teamController = new TeamController(this);
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        rv_team = findViewById(R.id.recyclerview_team);
        rv_team.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        rv_team.setLayoutManager(layoutManager);

        getTeamRecyclerView();
    }

    @SuppressLint("CutPasteId")
    private void initView() {
        Toolbar listToolbar = findViewById(R.id.customToolbar);
        listToolbar.setTitle("팀 리스트 확인");
        setSupportActionBar(listToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTeamRecyclerView(){
        list = new ArrayList<>();
        teamController.open();
        list = teamController.getList();

        teamsAdapter = new TeamsAdapter(this.list);
        rv_team.setAdapter(teamsAdapter);
        teamsAdapter.notifyDataSetChanged();

    }

    public void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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
            case R.id.recyclerview_add_menu:
                Intent intent_add = new Intent(getApplicationContext(), TeamAddActivity.class);
                startActivity(intent_add);

                Toast.makeText(getApplicationContext(),
                        "팀 추가하기로 이동합니다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.recyclerview_home_menu:
                Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_home);
                finish();
                return true;
            default:
                return false;
        }
    }
}