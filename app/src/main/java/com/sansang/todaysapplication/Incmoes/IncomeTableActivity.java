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
    private IncomeController incomeController;
    private DecimalFormat formatDouble;
    private DecimalFormat formatPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_table);

        todayDatabase = new TodayDatabase(this);
        incomeController = new IncomeController(this);

        formatDouble = new DecimalFormat("#,###.##");
        formatPay = new DecimalFormat("#,###");

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("수입 테이블");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIncomeListActivity();
            }
        });

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

        //addTextChangedListener TextWatcher
        edtxt_StartDate.addTextChangedListener(textWatcher);
        edtxt_EndDate.addTextChangedListener(textWatcher);
        edtxt_search.addTextChangedListener(textWatcher);

        getIntentResult();

        tableDateTime();
        startDateChange();
        endDateChange();

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
        Intent intent = new Intent(getApplicationContext(), IncomeListActivity.class);
        startActivity(intent);
        finish();
    }

    private void tableDateTime() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        Cursor csDate, cusDate = null;

        String startQuery = "select date('now', 'start of month', '-1 month', 'localtime')";

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

    //--- addTextChangedListener TextWatcher
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            income_table.removeAllViews();
            searchTeamDateLoad();
            sumDateJournal();
            sumDateIncome();
        }
    };

    //------ Date Selected Search -----------------------------------------------------
    @SuppressLint("SetTextI18n")
    private void searchTeamDateLoad() {
        income_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 0, 1, 0);

        //TextView Income Table Row Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Income Table income ID
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" ic_id ");

        row0.addView(tv1);

        //TextView Income Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Income Table Leader Name
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText("리  더 ");

        row0.addView(tv3);

        //TextView Income Table deposit(입금)
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 입 금 ");

        row0.addView(tv4);

        //TextView Income Table Tax(세금)
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 세 금 ");

        row0.addView(tv5);

        //TextView Income Table memo
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 메 모 ");

        row0.addView(tv6);

        //TextView Income Table site id
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" stid ");

        row0.addView(tv7);

        //TextView Income Table taem id
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setLines(1);
        tv8.setPadding( 3, 1, 3, 1 );

        tv8.setText( "tmid" );

        row0.addView( tv8 );

        //TableLayout View
        income_table.addView(row0);


        //Data Load
        String search = edtxt_search.getText().toString();
        String sDate = edtxt_StartDate.getText().toString();
        String eDate = edtxt_EndDate.getText().toString();

        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = incomeController.selectIncomeDateLeader(search, sDate, eDate);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < clums; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(1, 1, 1, 1);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //--- TableRow TextView BackgroundColor
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma collect
                if (j == 4) {
                    int rcollect = cus.getInt(4);
                    String collect_Format = formatPay.format(rcollect);
                    tv.setText(collect_Format);
                }
                //--- comma tax
                if (j == 5) {
                    int rtax = cus.getInt(5);
                    String tax_Format = formatPay.format(rtax);
                    tv.setText(tax_Format);
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), IncomeEditActivity.class);
                            //Income Table rowid
                            positionIntent.putExtra("id", cus.getInt(0));
                            //Income Table Incomeid
                            positionIntent.putExtra("icid", cus.getString(1));
                            //Income Table Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Income Table Team Leader
                            positionIntent.putExtra("leader", cus.getString(3));
                            //Income Table Collect
                            positionIntent.putExtra("deposit", cus.getInt(4));
                            //Income Table Tax
                            positionIntent.putExtra("tax", cus.getInt(5));
                            //Income Table Memo
                            positionIntent.putExtra("memo", cus.getString(6));
                            //Income Table stid
                            positionIntent.putExtra("stid", cus.getString(7));
                            //Income Table tmid
                            positionIntent.putExtra("tmid", cus.getString(8));

                            String leader = positionIntent.getExtras().getString("leader");
                            Toast.makeText(IncomeTableActivity.this, "선택 팀 :" + leader,
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            income_table.addView(row);
        }
        incomeController.close();

    }

    @SuppressLint({"SetTextI18n", "Range"})
    private void sumDateJournal(){
        //-- search
        String stDate = edtxt_StartDate.getText().toString().trim();
        String enDate = edtxt_EndDate.getText().toString().trim();
        String search = edtxt_search.getText().toString().trim();

        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cursor curinc = incomeController.sumDateSearchJournal(stDate, enDate,search);

        if (curinc.getString(curinc.getColumnIndex("ione")) != null) {
            float day = curinc.getFloat(0);
            String one_format = formatDouble.format(day);
            txt_day.setText(one_format + " 일 ");//--> oneDay
        } else {
            txt_day.setText("0 일");
        }

        if (curinc.getString(curinc.getColumnIndex("iamount")) != null) {
            int amounts = curinc.getInt(1);
            String amt_format = formatPay.format(amounts);
            txt_amount.setText(amt_format + " 원 ");//--> amount
        } else {
            txt_amount.setText("0 원 ");//--> amount
        }

        incomeController.close();
        curinc.close();
    }

    @SuppressLint({"SetTextI18n", "Range"})
    private void sumDateIncome(){
        //-- search
        String stDate = edtxt_StartDate.getText().toString().trim();
        String enDate = edtxt_EndDate.getText().toString().trim();
        String search = edtxt_search.getText().toString().trim();

        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cursor curinc = incomeController.sumDateSearchIncome(stDate, enDate,search);

        //deposit income
        if (curinc.getString(curinc.getColumnIndex("icollect")) != null) {
            int deposit = curinc.getInt(2);
            String depo_format = formatPay.format(deposit);
            txt_deposit.setText(depo_format + " 원 ");
        } else {
            txt_deposit.setText("0 원");
        }

        //tax income
        if (curinc.getString(curinc.getColumnIndex("itax")) != null) {
            int taxs = curinc.getInt(3);
            String ta_format = formatPay.format(taxs);
            txt_tax.setText(ta_format + " 원 ");
        } else {
            txt_tax.setText("0 원");
        }

        //balance amount income
        if (curinc.getString(curinc.getColumnIndex("balance")) != null) {
            int balance = curinc.getInt(4);
            String bal_format = formatPay.format(balance);
            txt_balance.setText(bal_format + " 원 ");
            if (balance < 0) {
                txt_balance.setTextColor(Color.parseColor("#BA0513"));
                txt_balance.setText(bal_format.replace("-", "") + "원 ");

            } else {
                txt_balance.setTextColor(Color.parseColor("#075797"));
                txt_balance.setText(bal_format + " 원 ");
            }
        } else {
            txt_balance.setText("0 원");
        }

        //balance day one income
        if (curinc.getString(curinc.getColumnIndex("balance_day")) != null) {
            float b_day = curinc.getFloat(5);
            String bald_format = formatDouble.format(b_day);
            getTxt_balanceday.setText(bald_format + " 일 ");//--> day balance
        } else {
            getTxt_balanceday.setText("0 일 ");
        }

        incomeController.close();
        curinc.close();
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