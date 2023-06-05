package com.sansang.todaysapplication.Settlement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.sansang.todaysapplication.Adapter.SettlementAdapter;
import com.sansang.todaysapplication.Database.CostTableContents;
import com.sansang.todaysapplication.Database.IncomeTableContents;
import com.sansang.todaysapplication.Database.JournalTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.R;

public class SettlementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SettlementAdapter settlementAdapter;
    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        todayDatabase = new TodayDatabase(this);

        initView();
    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("연도별 결산 리스트");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        recyclerView = findViewById(R.id.recycler_settlement);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getSettlementRecyclerView();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getSettlementRecyclerView(){
        String settlementQuery = "SELECT strftime('%Y', " + JournalTableContents.JOURNAL_DATE +
                ") as jyear, total(" + JournalTableContents.JOURNAL_ONE +
                ") as ones, sum(" + JournalTableContents.JOURNAL_AMOUNT +
                ") as amount, costs, deposit, tax FROM " + JournalTableContents.JOURNAL_TABLE +
                " j LEFT JOIN (SELECT strftime('%Y', " + CostTableContents.COST_DATE +
                ") as cyear, sum(" + CostTableContents.COST_AMOUNT +
                ") as costs FROM " + CostTableContents.COST_TABLE +
                " GROUP BY strftime('%Y', " + CostTableContents.COST_DATE +
                ")) c ON cyear = jyear LEFT JOIN (SELECT strftime('%Y'," + IncomeTableContents.INCOME_DATE +
                ") as iyear, sum(" + IncomeTableContents.INCOME_DEPOSIT +
                ") as deposit, sum(" + IncomeTableContents.INCOME_TAX +
                ") as tax FROM " + IncomeTableContents.INCOME_TABLE +
                " GROUP BY strftime('%Y', " + IncomeTableContents.INCOME_DATE +
                ")) i ON iyear = jyear GROUP BY jyear ORDER BY jyear DESC limit 5";

        String setQuery = "SELECT strftime('%Y',journalDate) AS jyear," +
                " total(oneDay) AS ones," +
                " sum(journalAmount) AS amount," +
                " deposit, tax,costs FROM journal_table j" +
                " LEFT JOIN (SELECT strftime('%Y',incomeDate) AS iyear, " +
                " sum(incomeDeposit) AS deposit, " +
                " sum(incomeTax) AS tax FROM income_table " +
                "  GROUP BY strftime('%Y',incomeDate)) i" +
                " ON iyear = jyear " +
                " LEFT JOIN (SELECT strftime('%Y',costDate) AS cyear," +
                " sum(costAmount) AS costs FROM cost_table " +
                "  GROUP BY strftime('%Y',costDate)) c " +
                " ON cyear = jyear " +
                " GROUP BY jyear " +
                " ORDER BY jyear DESC " +
                " LIMIT 5";

        sqLiteDatabase = todayDatabase.getReadableDatabase();
        Cursor cursor_settlement = sqLiteDatabase.rawQuery(settlementQuery, null);
        if (cursor_settlement != null) {
            int r = cursor_settlement.getCount();
            for (int i = 0; i < r; i++) {
                cursor_settlement.moveToFirst();
            }
        } else {
            cursor_settlement.close();
            return;
        }

        settlementAdapter = new SettlementAdapter(this, cursor_settlement);
        recyclerView.setAdapter(settlementAdapter);
        settlementAdapter.notifyDataSetChanged();

    }

    public void goToMainActivity(){
        finish();
    }

}