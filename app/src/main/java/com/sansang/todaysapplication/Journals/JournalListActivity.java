package com.sansang.todaysapplication.Journals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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

import com.sansang.todaysapplication.Adapter.JournalAdapter;
import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.JournalController;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;

public class JournalListActivity extends AppCompatActivity {
   //private ArrayList<JournalContents> journalList;
    private RecyclerView recyclerView;
    private JournalAdapter journalAdapter;
    TodayDatabase todayDatabase;
    SQLiteDatabase sqLiteDatabase;
    JournalController journalController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        todayDatabase = new TodayDatabase(this);
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        journalController = new JournalController(this);

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("일지 리스트");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        recyclerView = findViewById(R.id.recycler_journal);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        //((LinearLayoutManager) layoutManager).setReverseLayout(true);
        //((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);

        getAllJournalRecyclerView();

    }

    public void goToMainActivity(){

        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllJournalRecyclerView(){
        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Cursor cursor = journalController.recyclerViewData();

        journalAdapter = new JournalAdapter(this, cursor);
        recyclerView.setAdapter(journalAdapter);
        journalAdapter.notifyDataSetChanged();

        journalController.close();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.list_view_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        switch (item.getItemId()){
            case R.id.toolbar_add_list:
                Toast.makeText(this, "오늘 일지를 기록하세요?", Toast.LENGTH_SHORT).show();
                Intent intent_add = new Intent(getApplicationContext(), JournalAddActivity.class);
                startActivity(intent_add);
                finish();
                return true;

            case R.id.toolbar_table_list:
                Toast.makeText(this, "일지 테이블로 이동하여 일지 수정하기", Toast.LENGTH_SHORT).show();
                Intent intent_table = new Intent(getApplicationContext(), JournalTableActivity.class);
                intent_table.putExtra("leader", journalAdapter.getItemId(1));
                startActivity(intent_table);
                finish();
                return true;

            case R.id.toolbar_close_list:
                Toast.makeText(this, "오늘도 수고하셨습니다...", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return false;
        }
    }
}