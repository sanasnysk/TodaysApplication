package com.sansang.todaysapplication.Teams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sansang.todaysapplication.Adapter.TeamsAdapter;
import com.sansang.todaysapplication.Contents.TeamContents;
import com.sansang.todaysapplication.MainActivity;
import com.sansang.todaysapplication.R;

import java.util.ArrayList;

public class TeamListActivity extends AppCompatActivity {
    private ArrayList<TeamContents> list;
    private RecyclerView mRecyclerView;
    private TeamsAdapter teamsAdapter;
    //TeamDatabase db;
    //SQLiteDatabase sql;
    //TeamControl teamControl;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    private RecyclerView rv_team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        initView();
    }

    @SuppressLint("CutPasteId")
    private void initView() {
        rv_team = findViewById(R.id.recyclerview_team);

        Toolbar communityToolbar = findViewById(R.id.customToolbar);
        communityToolbar.setTitle("팀 리스트 확인");
        setSupportActionBar(communityToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        communityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        //db = new TeamDatabase(this);
        //teamControl = new TeamControl(this);
        //sql = db.getReadableDatabase();

        mRecyclerView = findViewById(R.id.recyclerview_team);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        mRecyclerView.setLayoutManager(layoutManager);

        getTeamRecyclerView();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTeamRecyclerView(){
        list = new ArrayList<>();
        //teamControl.open();
        //list = teamControl.getList();

        TeamContents teams = new TeamContents();
        teams.setId("1");
        teams.setTeamId("t_1");
        teams.setTeamLeader("sanda");
        teams.setTeamPhone("000-0001-0001");
        teams.setTeamDate("2023-01-01");
        teams.setTeamMemo("test");

        list.add(teams);


        teamsAdapter = new TeamsAdapter(this.list);
        mRecyclerView.setAdapter(teamsAdapter);
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
                finish();
                return true;
            case R.id.recyclerview_edit_menu:
                Intent intent_edit = new Intent(getApplicationContext(), TeamAddActivity.class);
                startActivity(intent_edit);
                finish();
                return true;
            case R.id.recyclerview_home_menu:
                Intent intent_search = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_search);
                finish();
                return true;
            default:
                return false;
        }
    }
}