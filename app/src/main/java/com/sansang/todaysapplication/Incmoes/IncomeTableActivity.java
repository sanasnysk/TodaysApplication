package com.sansang.todaysapplication.Incmoes;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.IncomeController;
import com.sansang.todaysapplication.DatabaseController.JournalController;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.Journals.JournalListActivity;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class IncomeTableActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText edtxt_StartDate, edtxt_EndDate, edtxt_search;
    private TextView txt_day, txt_amount, txt_balance, txt_deposit, txt_tax, getTxt_balanceday, txt_title;
    private TableLayout income_table;
    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;
    private JournalController journalController;
    private SiteController siteController;
    private IncomeController incomeController;
    private DecimalFormat formatDouble;
    private DecimalFormat formatPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_table);

        todayDatabase = new TodayDatabase(this);
        journalController = new JournalController(this);
        siteController = new SiteController(this);
        incomeController = new IncomeController(this);

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("수입 테이블 리스트");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIncomeListActivity();
            }
        });

        formatDouble = new DecimalFormat("#,###.##");
        formatPay = new DecimalFormat("#,###");

        //--- UI findViewById
        income_table = findViewById( R.id.income_table );

        edtxt_StartDate = findViewById( R.id.income_table_startDate );
        edtxt_EndDate = findViewById( R.id.income_table_endDate );
        edtxt_search = findViewById(R.id.income_table_leader_search);

        txt_day = findViewById( R.id.income_text_day_total );
        txt_amount = findViewById( R.id.income_text_amount_total );
        txt_balance = findViewById( R.id.income_text_balance_total );
        txt_deposit = findViewById( R.id.income_text_deposit_total );
        txt_tax = findViewById( R.id.income_text_tax_total );
        getTxt_balanceday = findViewById( R.id.income_text_balanceday_total );

        //--> EditText 항목 쓰기방지(read-only) or android:focusable="false"
        edtxt_StartDate.setInputType( InputType.TYPE_NULL );
        edtxt_StartDate.setFocusable( false );
        edtxt_EndDate.setInputType( InputType.TYPE_NULL );
        edtxt_EndDate.setFocusable( false );

        getIntentResult();

        tableDateTime();

        startDateChange();
        endDateChange();
        textSearchChangedListener();
        textStartChangedListener();
        textEndChangedListener();
    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String site = posionIntent.getExtras().getString("leader");
        String site_name = posionIntent.getExtras().getString("leader");
        if (site != null){
            edtxt_search.setText(site);
        }else {
            edtxt_search.setText(site_name);
        }

    }

    public void goToIncomeListActivity(){
        Intent intent = new Intent(getApplicationContext(), JournalListActivity.class);
        startActivity(intent);
        finish();
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

            edtxt_StartDate.setText( csDate.getString( 0 ) );
            edtxt_EndDate.setText( cusDate.getString( 0 ) );

            csDate.close();
            cusDate.close();
        }

        sqLiteDatabase.close();
    }

    @SuppressLint("DefaultLocale")
    private void startDateChange(){
        edtxt_StartDate.setOnClickListener(new View.OnClickListener() {
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
        edtxt_EndDate.setOnClickListener(new View.OnClickListener() {
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

                        edtxt_StartDate.setText( startDate );
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

                        edtxt_EndDate.setText( endDate );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    private void textSearchChangedListener(){
        //--- Search Journal Table
        edtxt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                income_table.removeAllViews();
                incomeSearchLoadData();
                sumIncomeSearch();

            }

            @Override
            public void afterTextChanged( Editable s ) {

            }
        });

    }

    private void textStartChangedListener(){
        //--- Start Date add Text Changed Listener
        edtxt_StartDate.addTextChangedListener( new TextWatcher() {
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
                Cursor cusIncomeAllSelect = journalController.selectAllJournal();
                int allCount = cusIncomeAllSelect.getCount();

                if (allCount != 0){
                    income_table.removeAllViews();
                    incomeDateLoad();
                    sumIncomeDate();
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
        edtxt_EndDate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //journal Controller.open();
                try {
                    journalController.open();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Cursor cusIncomeAllSelect = journalController.selectAllJournal();
                int allCount = cusIncomeAllSelect.getCount();

                if (allCount != 0){
                    income_table.removeAllViews();
                    incomeDateLoad();
                    sumIncomeDate();
                }else {
                    //Do nothing
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        } );
    }

    //--- Income Table Site Search ---
    @SuppressLint("SetTextI18n")
    private void incomeSearchLoadData() {
        income_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Income Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setLines(1);
        tv0.setPadding( 3, 1, 3, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Income Table income ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setLines(1);
        tv1.setPadding( 3, 1, 3, 1 );

        tv1.setText( "I_ID" );

        row0.addView( tv1 );

        //TextView Income Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setLines(1);
        tv2.setPadding( 3, 1, 3, 1 );

        tv2.setText( "일  자" );

        row0.addView( tv2 );

        //TextView Income Table Leader Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setLines(1);
        tv3.setPadding( 3, 1, 3, 1 );

        tv3.setText( "리  더" );

        row0.addView( tv3 );

        //TextView Income Table Collect
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setLines(1);
        tv4.setPadding( 3, 1, 3, 1 );

        tv4.setText( "입  금" );//수입금액 입금액

        row0.addView( tv4 );

        //TextView Income Table Tax
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setLines(1);
        tv5.setPadding( 3, 1, 3, 1 );

        tv5.setText( "세  금" );//세금공제

        row0.addView( tv5 );

        //TextView Income Table Memo
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setLines(1);
        tv6.setPadding( 3, 1, 3, 1 );

        tv6.setText( "메  모" );//경비

        row0.addView( tv6 );

        //TextView Income Table site id
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setLines(1);
        tv7.setPadding( 3, 1, 3, 1 );

        tv7.setText( "stid" );

        row0.addView( tv7 );

        //TextView Income Table site id
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setLines(1);
        tv8.setPadding( 3, 1, 3, 1 );

        tv8.setText( "stid" );

        row0.addView( tv8 );

        //TableLayout View
        income_table.addView( row0 );

        //Data Load
        String search = edtxt_search.getText().toString();

        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = incomeController.incomeSearchSelect(search);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < clums; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setLines(1);
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //--- BackgroundColor
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma collect
                if (j == 4){
                    int rcollect = cus.getInt( 4 );
                    String collect_Format = formatPay.format(rcollect);
                    tv.setText( collect_Format );
                }
                //--- comma tax
                if (j == 5){
                    int rtax = cus.getInt( 5 );
                    String tax_Format = formatPay.format(rtax);
                    tv.setText( tax_Format );
                }
                //--- comma cost
                if (j == 6){
                    int rcost = cus.getInt( 6 );
                    String cost_Format = formatPay.format( rcost );
                    tv.setText( cost_Format );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), IncomeEditActivity.class );
                            //Income Table rowid
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Income Table Incomeid
                            positionIntent.putExtra( "icid", cus.getString( 1 ) );
                            //Income Table Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Income Table Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 3 ) );
                            //Income Table Collect
                            positionIntent.putExtra( "collect", cus.getInt( 4 ) );
                            //Income Table Tax
                            positionIntent.putExtra( "tax", cus.getInt( 5 ) );
                            //Income Table Cost
                            positionIntent.putExtra( "memo", cus.getInt( 6 ) );
                            //Income Table Memo
                            positionIntent.putExtra( "stid", cus.getString( 7 ) );
                            positionIntent.putExtra("tmid", cus.getString(8));

                            String leader = positionIntent.getExtras().getString( "leader" );
                            Toast.makeText( IncomeTableActivity.this, "선택 팀 :" + leader,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            income_table.addView( row );
        }
        incomeController.close();

    }

    //------Income Team and startday and endday sum(collect) sum(tax) sum(cost)----------------
    @SuppressLint({"Recycle", "SetTextI18n"})
    private void sumIncomeSearch(){
        SQLiteDatabase db = todayDatabase.getReadableDatabase();

        String search = edtxt_search.getText().toString();

        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //----- Journal Table sum Data -----
        final Cursor cus = journalController.sumSearchJournal(search);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        float oneday = cus.getFloat( 0 );
        String oneDay_format = formatDouble.format( oneday );
        txt_day.setText( oneDay_format + "일" );//--> oneDay
        int pay = cus.getInt( 1 );
        String pay_format = formatPay.format( pay );
        txt_amount.setText( pay_format + "원" );//--> pay

        //incomeControl.open();
        if (edtxt_search.getText().toString().equals("")){
            //Do Nothing

        }else{
            try {
                incomeController.open();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            final Cursor cuso = incomeController.sumSearchIncome( search );
            final int incomerows = cuso.getCount();
            final int incomeclums = cuso.getColumnCount();

            int collect = cuso.getInt( 0 );
            String collect_format = formatPay.format( collect );
            txt_deposit.setText( collect_format + "원 " );//--> collect
            int tax = cuso.getInt( 1 );
            String tax_format = formatPay.format( tax );
            txt_tax.setText( tax_format + "원 " );//--> tax
//            float bday = cuso.getFloat( 2 );
//            String bday_format = formatPay.format( bday );
//            getTxt_balanceday.setText( bday_format + "원 " );//--> balance day

            //----- 잔액 계산 (balance) -----
            //int balance = pay - (collect + tax);
            //String balance_format = formatPay.format( balance );
            //textView_balance.setText( balance_format + "원" );//--> balance

            //----- 잔액 계산 (balance) -----

            final Cursor cursorTotalTeamJournal = journalController.sumTeamSpinnerJournal(search);
            int amountTotal = cursorTotalTeamJournal.getInt( 1 );

            final Cursor cursorTotalTeamIncome = incomeController.sumSiteIncome(search);
            int collectTotal = cursorTotalTeamIncome.getInt(0);
            int taxTotal = cursorTotalTeamIncome.getInt(1);
            //int costTotal = cursorTotalTeamIncome.getInt(2);


            int balance = amountTotal - (collectTotal + taxTotal);
            String formatted_Balance = formatPay.format( balance );

            if (balance < 0){
                txt_balance.setTextColor(Color.parseColor("#BA0513"));
                txt_balance.setText(formatted_Balance.replace("-", "")+ "원 ");

            }else {
                txt_balance.setTextColor(Color.parseColor("#075797"));
                txt_balance.setText(formatted_Balance + "원 ");
            }

        }

    }

    //------ Date Selected Search -----------------------------------------------------
    @SuppressLint("SetTextI18n")
    private void incomeDateLoad() {
        income_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Income Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Income Table income ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( "I_ID" );

        row0.addView( tv1 );

        //TextView Income Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( "일  자" );

        row0.addView( tv2 );

        //TextView Income Table Leader Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( "현  장" );

        row0.addView( tv3 );

        //TextView Income Table Collect
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( "입  금" );//수입금액 입금액

        row0.addView( tv4 );

        //TextView Income Table Tax
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( "세  금" );//세금공제

        row0.addView( tv5 );

        //TextView Income Table 메모
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( "메  모" );//메모

        row0.addView( tv6 );

        //TextView Income Table stid
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( "stid" );

        row0.addView( tv7 );

        //TextView Income Table stid
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( "tmid" );

        row0.addView( tv8 );

        //TableLayout View
        income_table.addView( row0 );


        //Data Load
        String sDate = edtxt_StartDate.getText().toString();
        String eDate = edtxt_EndDate.getText().toString();

        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = incomeController.incomeDateSelect( sDate, eDate );
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < clums; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //--- TableRow TextView BackgroundColor
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma collect
                if (j == 4){
                    int rcollect = cus.getInt( 4 );
                    String collect_Format = formatPay.format(rcollect);
                    tv.setText( collect_Format );
                }
                //--- comma tax
                if (j == 5){
                    int rtax = cus.getInt( 5 );
                    String tax_Format = formatPay.format(rtax);
                    tv.setText( tax_Format );
                }
                //--- comma cost
                if (j == 6){
                    int rcost = cus.getInt( 6 );
                    String cost_Format = formatPay.format( rcost );
                    tv.setText( cost_Format );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), IncomeEditActivity.class );
                            //Income Table rowid
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Income Table Incomeid
                            positionIntent.putExtra( "iid", cus.getString( 1 ) );
                            //Income Table Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Income Table Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 3 ) );
                            //Income Table Collect
                            positionIntent.putExtra( "collect", cus.getInt( 4 ) );
                            //Income Table Tax
                            positionIntent.putExtra( "tax", cus.getInt( 5 ) );
                            //Income Table Cost
                            positionIntent.putExtra( "cost", cus.getInt( 6 ) );
                            //Income Table Memo
                            positionIntent.putExtra( "memo", cus.getString( 7 ) );

                            String leader = positionIntent.getExtras().getString( "leader" );
                            Toast.makeText( IncomeTableActivity.this, "선택 팀 :" + leader,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            income_table.addView( row );
        }
        incomeController.close();

    }

    //------- Income startday and endday sum(oneday) sum(amount) --- 현제사용안함 ---
    @SuppressLint({"Recycle", "SetTextI18n"})
    private void sumIncomeDate(){
        SQLiteDatabase db = todayDatabase.getReadableDatabase();

        String stDate = edtxt_StartDate.getText().toString();
        String enDate = edtxt_EndDate.getText().toString();

        //----- 전월 -----
        Cursor csdate, cusdate, cumonth = null;

        String startQuery = "select date('"+ stDate +"', 'start of month', 'localtime')";
        //String startQuery = "select date('"+ stDate +"', 'start of month', '-1 month', 'localtime')";
        String endQuery = "select date('"+ enDate +"', 'start of month', '+1 month', '-1 day','localtime')";
        //String stmonthQuery = "select date( " + stDate + ", 'start of month', '-1 month', 'localtime')";

        csdate = db.rawQuery( startQuery, null );
        cusdate = db.rawQuery( endQuery, null );
        //cumonth = db.rawQuery( stmonthQuery, null );
        if (csdate.getCount() > 0 || cusdate.getCount() > 0) {
            csdate.moveToFirst();
            cusdate.moveToFirst();
        }
        //Data Load
        String startday = csdate.getString( 0 );
        String endday = cusdate.getString( 0 );
        //String stmonth = cumonth.getString(0);

        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //----- Journal Table sum Data -----
        final Cursor cus = journalController.sumDateJournal( stDate, enDate);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        float oneday = cus.getFloat( 0 );
        String formatted_day = formatDouble.format( oneday );
        txt_day.setText( formatted_day + "일" );

        int pay = cus.getInt( 1 );
        String formatted_pay = formatPay.format( pay );
        txt_amount.setText( formatted_pay + "원" );

        //----- Income Table sum Data -----
        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor balancecursor = incomeController.sumDateIncome( stDate, enDate );
        final int rowsBalance = balancecursor.getCount();
        final int columsBalance = balancecursor.getColumnCount();

        //----- 합계 가저오기 ------
        int collect = balancecursor.getInt( 0 );
        String formatted_deposit = formatPay.format( collect );
        txt_deposit.setText( formatted_deposit + "원 " );//--> collect
        int tax = balancecursor.getInt( 1 );
        String formatted_tax = formatPay.format( tax );
        txt_tax.setText( formatted_tax + "원 " );//--> tax
//        float bday = balancecursor.getFloat( 2 );
//        String bday_format = formatPay.format( bday );
//        getTxt_balanceday.setText( bday_format + "원 " );//--> balance day

        //----- 잔액 계산 (balance) -----
        //int balance = pay - (collect + tax);
        //String formatted_balance = formatPay.format( balance );
        //textView_balance.setText( formatted_balance + "원" );//--> balance

        //----- 잔액 계산 (balance) -----

        final Cursor cursorTotalTeamJournal = journalController.sumTotalJournal( );
        int amountTotal = cursorTotalTeamJournal.getInt( 1 );

        final Cursor cursorTotalTeamIncome = incomeController.sumTotalIncome( );
        int collectTotal = cursorTotalTeamIncome.getInt(0);
        int taxTotal = cursorTotalTeamIncome.getInt(1);
        //int costTotal = cursorTotalTeamIncome.getInt(2);

        int balance = amountTotal - (collectTotal + taxTotal);
        String formatted_Balance = formatPay.format( balance );

        if (balance < 0){
            txt_balance.setTextColor(Color.parseColor("#BA0513"));
            txt_balance.setText(formatted_Balance.replace("-", "")+ "원 ");

        }else {
            txt_balance.setTextColor(Color.parseColor("#075797"));
            txt_balance.setText(formatted_Balance + "원 ");
        }


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
            case R.id.toolbar_new_table:
                tableDateTime();
                edtxt_search.setText("");
                return true;

            case R.id.toolbar_add_table:
                Toast.makeText(getApplicationContext(),
                        "수입 기록으로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_incomeAddImage = new Intent( getApplicationContext(), IncomeAddActivity.class );
                startActivity( intent_incomeAddImage );
                finish();
                return true;

            case R.id.toolbar_close_table:
                Toast.makeText(getApplicationContext(),
                        "수입 리스트로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_incomeAddText = new Intent( getApplicationContext(), IncomeListActivity.class );
                startActivity( intent_incomeAddText );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}