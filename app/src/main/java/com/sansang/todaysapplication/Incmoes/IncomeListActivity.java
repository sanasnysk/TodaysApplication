package com.sansang.todaysapplication.Incmoes;

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

import com.sansang.todaysapplication.Adapter.IncomeAdapter;
import com.sansang.todaysapplication.Database.IncomeTableContents;
import com.sansang.todaysapplication.Database.JournalTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.R;

import java.io.IOException;

public class IncomeListActivity extends AppCompatActivity {
    private RecyclerView recyclerView_income;
    private IncomeAdapter incomeAdapter;
    TodayDatabase todayDatabase;
    SQLiteDatabase sqLiteDatabase;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);

        todayDatabase = new TodayDatabase(this);
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("수입 리스트");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        recyclerView_income = findViewById(R.id.recycler_income);
        recyclerView_income.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView_income.setLayoutManager(layoutManager);

        getAllIncomeRecyclerView();

    }

    public void goToMainActivity() {

        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllIncomeRecyclerView() {
        String rvListQuery = "SELECT j." + JournalTableContents.TEAM_LEADER +
                ", TOTAL(j." + JournalTableContents.JOURNAL_ONE + ") as oneDays, " +
                "SUM(j." + JournalTableContents.JOURNAL_AMOUNT + ") as amounts," +
                " si.deposit as deposit,si.taxs, " +
                " TOTAL(j.oneDay) - round((si.deposit + si.taxs)/round(avg(j.sitePay),1),1) as days," +
                " SUM(j." + JournalTableContents.JOURNAL_AMOUNT + ") - (si.deposit + si.taxs) as balances" +
                " FROM " + JournalTableContents.JOURNAL_TABLE +
                " as j LEFT OUTER JOIN (SELECT " + IncomeTableContents.TEAM_LEADER +
                ", SUM(" + IncomeTableContents.INCOME_DEPOSIT + ") as deposit," +
                " SUM(" + IncomeTableContents.INCOME_TAX + ") as taxs" +
                " FROM " + IncomeTableContents.INCOME_TABLE +
                " GROUP BY " + IncomeTableContents.TEAM_LEADER +
                ") as si ON j." + JournalTableContents.TEAM_LEADER +
                " = si." + IncomeTableContents.TEAM_LEADER +
                " GROUP BY j." + JournalTableContents.TEAM_LEADER +
                " ORDER BY " + JournalTableContents.JOURNAL_ID +
                " DESC";

        Cursor cursor = sqLiteDatabase.rawQuery(rvListQuery, null);
        incomeAdapter = new IncomeAdapter(this, cursor);
        recyclerView_income.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_view_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add_list:
                Toast.makeText(this, "수입 기록으로 이동", Toast.LENGTH_SHORT).show();
                Intent intent_incomeAdd = new Intent(getApplicationContext(), IncomeAddActivity.class);
                startActivity(intent_incomeAdd);
                finish();
                return true;

            case R.id.toolbar_table_list:
                Toast.makeText(this, "수입 테이블로 이동", Toast.LENGTH_SHORT).show();
                Intent intent_incometable = new Intent(getApplicationContext(), IncomeTableActivity.class);
                intent_incometable.putExtra("leader", incomeAdapter.getItemId(1));
                startActivity(intent_incometable);
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