package com.sansang.todaysapplication.Sites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.R;

import java.text.DecimalFormat;

public class SiteTableActivity extends AppCompatActivity {
    private EditText etxt_Search;
    public TableLayout site_table;
    private TodayDatabase todayDatabase;
    private SiteController siteController;
    private final DecimalFormat formatPay = new DecimalFormat("#,###"); //금액 콤마 소숫점 없음;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_table);

        todayDatabase = new TodayDatabase(this);
        siteController = new SiteController(this);

        initView();
        getIntentResultSite();
    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("현장 테이블");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSiteListActivity();
            }
        });

        site_table = findViewById(R.id.site_table);

        etxt_Search = findViewById(R.id.search_site);

        searchTextChanged();

    }

    public void goToSiteListActivity(){
        Intent intent = new Intent(getApplicationContext(), SiteListActivity.class);
        startActivity(intent);
        finish();
    }

    private void searchTextChanged(){
        // Search Edit Text Changed
        TextWatcher searchChanged = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                site_table.removeAllViews();
                searchSiteData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        etxt_Search.addTextChangedListener(searchChanged);

    }

    private void getIntentResultSite() {
        Intent positionIntent = getIntent();
        String leader = positionIntent.getExtras().getString("leader");
        String team_leader = positionIntent.getExtras().getString("leader");
        if (leader != null){
            etxt_Search.setText(leader);
        }else {
            etxt_Search.setText(team_leader);
        }
    }

    @SuppressLint("SetTextI18n")
    private void searchSiteData() {
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Site Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setLines(1);
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Site ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setLines(1);
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( "St_ID" );

        row0.addView( tv1 );

        //TextView Team Leader
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setLines(1);
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( "현장" );

        row0.addView( tv2 );

        //TextView Team Leader Phone No
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setLines(1);
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( "임금(일당)" );

        row0.addView( tv3 );

        //TextView Team Leader Phone No
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setLines(1);
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( "팀 반장)" );

        row0.addView( tv4 );

        //TextView Team Leader Phone No
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setLines(1);
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( "등록일자" );

        row0.addView( tv5 );

        //TextView Team Start Date
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setLines(1);
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( "현장 메모" );

        row0.addView( tv6 );

        //TextView Team Leader
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setLines(1);
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( "팀/회사" );

        row0.addView( tv7 );

        //TextView Team tmId
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setLines(1);
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( "tmId" );

        row0.addView( tv8 );

        //TableLayout View
        site_table.addView( row0 );

        //Data Load
        String searchSite = etxt_Search.getText().toString();
        siteController.open();
        final Cursor cus = siteController.siteSearch( searchSite );
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setLines(1);
                tv.setPadding( 0, 1, 0, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //********
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 3 ){
                    int rPay = cus.getInt( 3 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }

                final int p = i;
                final int r = i + 1;
                final int rId = i + 2;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {
                            Intent positionIntent = new Intent( getApplicationContext(), SiteEditActivity.class );

                            positionIntent.putExtra( "sId", cus.getString( 0 ) );
                            positionIntent.putExtra( "stId", cus.getString( 1 ) );
                            positionIntent.putExtra( "stName", cus.getString( 2 ) );
                            positionIntent.putExtra( "stPay", cus.getString( 3 ) );
                            positionIntent.putExtra( "stManager", cus.getString( 4 ) );
                            positionIntent.putExtra( "stDate", cus.getString( 5 ) );
                            positionIntent.putExtra( "stMemo", cus.getString( 6 ) );
                            positionIntent.putExtra( "tmLeader", cus.getString( 7 ) );
                            positionIntent.putExtra( "tmId", cus.getString( 8 ) );

                            String name = positionIntent.getExtras().getString( "name" );

                            Toast.makeText( SiteTableActivity.this, "선택 현장 :" + name,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }

                        cus.close();
                    }
                } );
            }
            cus.moveToNext();
            site_table.addView( row );
        }

        siteController.close();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.table_toolbar_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add_table:
                Toast.makeText(getApplicationContext(),
                        "현장 등록으로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_site_add = new Intent( getApplicationContext(), SiteAddActivity.class );
                startActivity( intent_site_add );
                finish();
                return true;

            case R.id.toolbar_new_table:
                etxt_Search.setText("");
                return true;

            case R.id.toolbar_close_table:
                Toast.makeText(getApplicationContext(),
                        "종료 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SiteListActivity.class);
                startActivity(intent);

                finish();
                return true;

            default:
                return false;
        }
    }
}