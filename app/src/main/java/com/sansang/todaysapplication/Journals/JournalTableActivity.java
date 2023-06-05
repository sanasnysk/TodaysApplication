package com.sansang.todaysapplication.Journals;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.JournalController;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

public class JournalTableActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    private EditText etxt_StartDate, etxt_EndDate, etxt_Search;
    private TextView txt_Day, txt_inputDay, txt_Amount, txt_inputAmount;
    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;
    private JournalController journalController;
    private SiteController siteController;
    private TableLayout table_journal;
    private DecimalFormat formatDouble;
    private  DecimalFormat formatPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_table);

        todayDatabase = new TodayDatabase(this);
        journalController = new JournalController(this);
        siteController = new SiteController(this);

        initView();

        tableDateTime();
    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("일지 테이블");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJournalListActivity();
            }
        });

        formatDouble = new DecimalFormat("#,###.##");
        formatPay = new DecimalFormat("#,###");

        table_journal = findViewById(R.id.journal_table);
        etxt_StartDate = findViewById(R.id.journal_edit_startDate);
        etxt_EndDate = findViewById(R.id.journal_edit_endDate);
        etxt_Search = findViewById(R.id.journal_table_site_search);
        txt_Day = findViewById(R.id.journal_text_sum_dailyOne);
        txt_inputDay = findViewById(R.id.journal_text_sum_dailyOne_input);
        txt_Amount = findViewById(R.id.journal_text_sum_amount);
        txt_inputAmount = findViewById(R.id.journal_text_sum_amount_input);

        etxt_StartDate.setFocusable(false);
        etxt_EndDate.setFocusable(false);

        startDateChange();
        endDateChange();
        textSearchChangedListener();
        textStartChangedListener();
        textEndChangedListener();

        getIntentResult();
    }

    public void goToJournalListActivity(){
        Intent intent = new Intent(getApplicationContext(), JournalListActivity.class);
        startActivity(intent);
        finish();
    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String site = posionIntent.getExtras().getString("site");
        String site_name = posionIntent.getExtras().getString("site_name");
        if (site != null){
            etxt_Search.setText(site);
        }else {
            etxt_Search.setText(site_name);
        }

    }

    private void tableDateTime() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        Cursor csDate, cusDate = null;

        String startQuery = "select date('now', 'start of month', 'localtime')";

        String endQuery = "select date('now', 'start of month', '+1 month', '-1 day','localtime')";
        //String endQuery = "select datetime('now','localtime')";

        csDate = sqLiteDatabase.rawQuery( startQuery, null );
        cusDate = sqLiteDatabase.rawQuery( endQuery, null );



        if (csDate.getCount() > 0 || cusDate.getCount() > 0) {
            csDate.moveToFirst();
            cusDate.moveToFirst();

            etxt_StartDate.setText( csDate.getString( 0 ) );
            etxt_EndDate.setText( cusDate.getString( 0 ) );

            csDate.close();
            cusDate.close();
        }

        sqLiteDatabase.close();
    }

    @SuppressLint("DefaultLocale")
    private void startDateChange(){
        etxt_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                startDatePickerDialog();
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private void endDateChange(){
        etxt_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                endDatePickerDialog();
            }
        });
    }

    //----- DatePickerDialog -----
    @SuppressLint("DefaultLocale")
    private void startDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //for
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( year, month, dayOfMonth );
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get( Calendar.MONTH ) + 1;
                        dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );

                        String startDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );

                        etxt_StartDate.setText( startDate );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    //----- DatePickerDialog -----
    @SuppressLint("DefaultLocale")
    private void endDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //for
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( year, month, dayOfMonth );
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get( Calendar.MONTH ) + 1;
                        dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );

                        String endDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );

                        etxt_EndDate.setText( endDate );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    private void textSearchChangedListener(){
        //--- Search Journal Table
        etxt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {

            }

            @Override
            public void afterTextChanged( Editable s ) {

                searchJournalSiteDate();
                sumSiteDateJournal();

            }
        });

    }

    private void textStartChangedListener(){
        //--- Start Date add Text Changed Listener
        etxt_StartDate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    journalController.open();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                final Cursor cusLoad = journalController.selectAllJournal(  );
                final int rowCount = cusLoad.getCount();
                if (rowCount != 0){
                    table_journal.removeAllViews();
                    if (etxt_Search == null){
                        searchDateJournal();
                        sumDateJournal();
                    }else {
                        searchJournalSiteDate();
                        sumSiteDateJournal();
                    }

                }else {
                    //Do nothing
                }
            }

            @Override
            public void afterTextChanged( Editable s) {
            }
        } );

    }

    private void textEndChangedListener(){
        //--- End Date add Text Changed Listener
        etxt_EndDate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //journal Controller.open();
                try {
                    journalController.open();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                final Cursor cusLoad = journalController.selectAllJournal(  );
                final int rowCount = cusLoad.getCount();
                if (rowCount != 0){
                    table_journal.removeAllViews();
                    if (etxt_Search == null){
                        searchDateJournal();
                        sumDateJournal();
                    }else {
                        searchJournalSiteDate();
                        sumSiteDateJournal();
                    }

                }else {
                    //Do nothing
                }

            }
        } );
    }

    //--- Search Site Journal Data Table
    @SuppressLint("SetTextI18n")
    private void searchJournalSiteDate() {
        table_journal.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( "JN_ID" );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( "일  자" );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( "현  장" );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( "일일" );

        row0.addView( tv4 );

        //TextView Journal Table Daily Pay
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( "임   금" );

        row0.addView( tv5 );

        //TextView Journal Table Amount
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( "금  액" );

        row0.addView( tv6 );

        //TextView Journal Table Team Leader
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( "일일메모" );

        row0.addView( tv7 );

        //TextView Journal Table Memo
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( "stId" );

        row0.addView( tv8 );

        //TextView Journal Table stId
        TextView tv9 = new TextView( this );
        tv9.setLayoutParams( tlp );

        tv9.setBackgroundColor( Color.BLUE );
        tv9.setTextColor( Color.WHITE );
        tv9.setGravity( Gravity.CENTER );
        tv9.setTextSize( 18 );
        tv9.setPadding( 1, 1, 1, 1 );

        tv9.setText( "팀 리더" );

        row0.addView( tv9 );

        //TextView Journal Table tmId
        TextView tv10 = new TextView( this );
        tv10.setLayoutParams( tlp );

        tv10.setBackgroundColor( Color.BLUE );
        tv10.setTextColor( Color.WHITE );
        tv10.setGravity( Gravity.CENTER );
        tv10.setTextSize( 18 );
        tv10.setPadding( 1, 1, 1, 1 );

        tv10.setText( "tmId" );

        row0.addView( tv10 );

        //TableLayout View
        table_journal.addView( row0 );

        //Data Load
        //String spinSearchSite = spinner_Site.getSelectedItem().toString();
        String searchSite = etxt_Search.getText().toString();
        String startDay = etxt_StartDate.getText().toString();
        String endDay = etxt_EndDate.getText().toString();
        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = journalController.searchSiteJournal( searchSite, startDay, endDay );
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
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 6){
                    int rAmount = cus.getInt( 6 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEditActivity.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jnid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Daily pay
                            positionIntent.putExtra( "pay", cus.getInt( 5 ) );
                            //Journal Amount
                            positionIntent.putExtra( "amount", cus.getInt( 6 ) );
                            //Journal Team Memo
                            positionIntent.putExtra( "memo", cus.getString( 7 ) );
                            //Journal stid
                            positionIntent.putExtra( "stid", cus.getString( 8 ) );
                            //Journal leader
                            positionIntent.putExtra("leader", cus.getString(9));
                            //Journal tmId
                            positionIntent.putExtra("tmid", cus.getString(10));

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTableActivity.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            table_journal.addView( row );
        }
        journalController.close();

    }

    //--- Search Date Journal Data Table
    @SuppressLint("SetTextI18n")
    private void searchDateJournal() {
        table_journal.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( "JN_ID" );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( "일  자" );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( "현  장" );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( "일일" );

        row0.addView( tv4 );

        //TextView Journal Table Daily Pay
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( "임금(일당)" );

        row0.addView( tv5 );

        //TextView Journal Table Amount
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( "금  액" );

        row0.addView( tv6 );

        //TextView Journal Table Team Leader
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( "일일메모" );

        row0.addView( tv7 );

        //TextView Journal Table Memo
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( "stId" );

        row0.addView( tv8 );

        //TextView Journal Table stId
        TextView tv9 = new TextView( this );
        tv9.setLayoutParams( tlp );

        tv9.setBackgroundColor( Color.BLUE );
        tv9.setTextColor( Color.WHITE );
        tv9.setGravity( Gravity.CENTER );
        tv9.setTextSize( 18 );
        tv9.setPadding( 1, 1, 1, 1 );

        tv9.setText( "팀 리더" );

        row0.addView( tv9 );

        //TextView Journal Table tmId
        TextView tv10 = new TextView( this );
        tv10.setLayoutParams( tlp );

        tv10.setBackgroundColor( Color.BLUE );
        tv10.setTextColor( Color.WHITE );
        tv10.setGravity( Gravity.CENTER );
        tv10.setTextSize( 18 );
        tv10.setPadding( 1, 1, 1, 1 );

        tv10.setText( "tmId" );

        row0.addView( tv10 );

        //TableLayout View
        table_journal.addView( row0 );

        //Data Load
        String startDay = etxt_StartDate.getText().toString();
        String endDay = etxt_EndDate.getText().toString();

        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = journalController.searchDateJournal( startDay, endDay );
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
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** TableRow TextView Color
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 6){
                    int rAmount = cus.getInt( 6 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEditActivity.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jnid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Daily pay
                            positionIntent.putExtra( "pay", cus.getInt( 5 ) );
                            //Journal Amount
                            positionIntent.putExtra( "amount", cus.getInt( 6 ) );
                            //Journal Team Leader
                            positionIntent.putExtra( "memo", cus.getString( 7 ) );
                            //Journal stid
                            positionIntent.putExtra( "stid", cus.getString( 8 ) );
                            //Journal leader
                            positionIntent.putExtra("leader", cus.getString(9));
                            //Journal tmId
                            positionIntent.putExtra("tmid", cus.getString(10));

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTableActivity.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            table_journal.addView( row );
        }
        journalController.close();

    }

    //--- Site and start day and end day sum(one day) sum(amount)spinner
    @SuppressLint("SetTextI18n")
    private void sumSiteDateJournal(){
        //Data Load
        String startDay = etxt_StartDate.getText().toString();
        String endDay = etxt_EndDate.getText().toString();
        String search_site_date = etxt_Search.getText().toString();

        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (search_site_date.equals("현장을 선택 하세요?")){
            //to do nothing
        }else if (!search_site_date.equals("현장을 선택 하세요?")){
            final Cursor cus = journalController.sumSearchSiteDateJournal( startDay, endDay, search_site_date );
            final int rows = cus.getCount();
            final int columns = cus.getColumnCount();


            float oneDay = cus.getFloat( 0 );
            String formatted_oneday = formatDouble.format( oneDay );
            txt_inputDay.setText( formatted_oneday + "일" );

            int amount = cus.getInt( 1 );
            String formatted_inputAmount = formatPay.format( amount );
            txt_inputAmount.setText( formatted_inputAmount + "원" );
        }

    }

    //--- startDay and endDay sum(oneDay) sum(amount)
    @SuppressLint("SetTextI18n")
    private void sumDateJournal(){
        //Data Load
        String startDay = etxt_StartDate.getText().toString();
        String endDay = etxt_EndDate.getText().toString();

        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cusLoad = journalController.selectAllJournal(  );
        final int rowCount = cusLoad.getCount();
        if (rowCount != 0) {
            Cursor cus = journalController.sumDateJournal( startDay, endDay );
            float day = cus.getFloat( 0 );
            String formatted_day = formatDouble.format( day );
            txt_inputDay.setText( formatted_day + "일" );

            int money = cus.getInt( 1 );
            String formatted_inputAmount = formatPay.format( money );
            txt_inputAmount.setText( formatted_inputAmount + "원" );
        }else {
            txt_inputDay.setText( 0 );
            txt_inputAmount.setText( 0 );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_new_table:
                etxt_Search.setText("");
                tableDateTime();
                return true;
            case R.id.toolbar_add_table:
                Intent intent_journaladd = new Intent(getApplicationContext(), JournalAddActivity.class);
                startActivity(intent_journaladd);
                finish();
                return true;
            case R.id.toolbar_close_table:
                Intent intent_journallist = new Intent(getApplicationContext(), JournalListActivity.class);
                startActivity(intent_journallist);
                finish();
                return true;
            default:
                return false;
        }
    }
}