package com.sansang.todaysapplication.Costs;

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

import com.sansang.todaysapplication.Adapter.CostAdapter;
import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.CostController;
import com.sansang.todaysapplication.MainActivity;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;

public class CostListActivity extends AppCompatActivity {
    private RecyclerView rcv_cost;
    private CostAdapter costAdapter;
    TodayDatabase todayDatabase;
    CostController costController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_list);

        todayDatabase = new TodayDatabase(this);
        costController = new CostController(this);

        initView();
    }

    private void initView(){
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("경비 리스트");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        rcv_cost = findViewById(R.id.recycler_cost);
        rcv_cost.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rcv_cost.setLayoutManager(layoutManager);

        getAllCostRecyclerView();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllCostRecyclerView(){
        try {
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Cursor cursor = costController.recyclweViewData();
        costAdapter = new CostAdapter(this,cursor);
        rcv_cost.setAdapter(costAdapter);
        costAdapter.notifyDataSetChanged();
        costController.close();
    }

    public void goToMainActivity(){
        finish();
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
                Toast.makeText(getApplicationContext(),
                        "경비 기록으로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_cost_add = new Intent(getApplicationContext(), CostAddActivity.class);
                startActivity(intent_cost_add);
                finish();
                return true;

            case R.id.toolbar_table_list:
                Toast.makeText(getApplicationContext(),
                        "일지쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_cost_table = new Intent(getApplicationContext(), CostTableActivity.class);
                intent_cost_table.putExtra("site", costAdapter.getItemId(1));
                startActivity(intent_cost_table);
                finish();
                return true;

            case R.id.toolbar_close_list:
                Toast.makeText(getApplicationContext(),
                        "일지쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_cost_close = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_cost_close);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}