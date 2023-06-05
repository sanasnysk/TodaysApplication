package com.sansang.todaysapplication.Costs;

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
import com.sansang.todaysapplication.DatabaseController.CostController;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class CostTableActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText edtxt_StartDate, edtxt_EndDate, edtxt_Search;
    private TextView txt_Amount, txt_inputAmount;
    private TableLayout cost_table;
    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;
    private CostController costController;
    private SiteController siteController;
    private DecimalFormat formatPay = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_table);

        todayDatabase = new TodayDatabase(this);
        costController = new CostController(this);
        siteController = new SiteController(this);

        initView();
    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("경비 테이블");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCostListActivity();
            }
        });

        cost_table = findViewById(R.id.cost_table);
        edtxt_StartDate = findViewById(R.id.cost_table_startDate);
        edtxt_StartDate.setInputType(InputType.TYPE_NULL);
        edtxt_StartDate.setFocusable(false);
        edtxt_EndDate = findViewById(R.id.cost_table_endDate);
        edtxt_EndDate.setInputType(InputType.TYPE_NULL);
        edtxt_EndDate.setFocusable(false);
        edtxt_Search = findViewById(R.id.cost_table_search);
        txt_Amount = findViewById(R.id.cost_table_sum_amount);
        txt_inputAmount = findViewById(R.id.cost_table_sum_amount_input);

        //addTextChangedListener TextWatcher
        edtxt_StartDate.addTextChangedListener(textWatcher);
        edtxt_EndDate.addTextChangedListener(textWatcher);
        edtxt_Search.addTextChangedListener(textWatcher);

        getIntentResult();

        tableDateTime();
        startDateChange();
        endDateChange();

    }

    public void goToCostListActivity(){
        Intent intent = new Intent(getApplicationContext(), CostListActivity.class);
        startActivity(intent);
        finish();
    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String site = posionIntent.getExtras().getString("site");
        String site_name = posionIntent.getExtras().getString("site_name");
        if (site != null) {
            edtxt_Search.setText(site);
        } else {
            edtxt_Search.setText(site_name);
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
            cost_table.removeAllViews();
            if (edtxt_Search == null) {
                searchDateCost();
                sumDateCost();
            } else {
                searchSiteDate();
                sumSearchSiteDate();
            }

        }
    };

    //---EditText Search Cost Data Table
    @SuppressLint("SetTextI18n")
    private void searchDateCost() {
        cost_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 1, 1, 1);

        //TextView Cost Table Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Cost Table csid
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" csid ");

        row0.addView(tv1);

        //TextView Cost Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Cost Table Site
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText(" 현 장 ");

        row0.addView(tv3);

        //TextView Cost Table DEtail
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 내 용 ");

        row0.addView(tv4);

        //TextView Cost Table Price
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 단  가 ");

        row0.addView(tv5);

        //TextView Cost Table Amount
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 금  액 ");

        row0.addView(tv6);

        //TextView Cost Table  Memo
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" 메  모   ");

        row0.addView(tv7);

        //TextView Cost Table stid
        TextView tv8 = new TextView(this);
        tv8.setLayoutParams(tlp);

        tv8.setBackgroundColor(Color.BLUE);
        tv8.setTextColor(Color.WHITE);
        tv8.setGravity(Gravity.CENTER);
        tv8.setTextSize(18);
        tv8.setPadding(1, 1, 1, 1);

        tv8.setText(" stid ");

        row0.addView(tv8);

        //TableLayout View
        cost_table.addView(row0);

        //Data Load
        String startDay = edtxt_StartDate.getText().toString();
        String endDay = edtxt_EndDate.getText().toString();
        try {
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = costController.selectDateCost(startDay,endDay);
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(3, 1, 1, 3);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma

                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }


                if (j == 6) {
                    int rAmount = cus.getInt(6);
                    String formattedAmount = formatPay.format(rAmount);
                    tv.setText(formattedAmount);
                }


                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), CostEditActivity.class);
                            //id
                            positionIntent.putExtra("id", cus.getInt(0));
                            //cost cid
                            positionIntent.putExtra("csid", cus.getString(1));
                            //Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Site Name
                            positionIntent.putExtra("site", cus.getString(3));
                            //detail
                            positionIntent.putExtra("detail", cus.getString(4));
                            //price
                            positionIntent.putExtra("price", cus.getInt(5));
                            //amount
                            positionIntent.putExtra("amount", cus.getInt(6));
                            //memo
                            positionIntent.putExtra("memo", cus.getString(7));
                            //sid
                            positionIntent.putExtra("stid", cus.getString(8));


                            String site = positionIntent.getExtras().getString("site");
                            Toast.makeText(CostTableActivity.this,
                                    site + "를 선택 했습니다. :",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            cost_table.addView(row);
        }
        costController.close();
    }

    //--- search date cost sum
    @SuppressLint({"SetTextI18n", "Range"})
    private void sumDateCost(){
        String stda = edtxt_StartDate.getText().toString();
        String enda = edtxt_EndDate.getText().toString();

        try {
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cursor cur = costController.sumSelectDateCost(stda,enda);

        if (cur.getString(cur.getColumnIndex("costAmount")) != null){
            int amounts = cur.getInt(0);
            String amt_format = formatPay.format(amounts);
            txt_inputAmount.setText(amt_format + " 원 ");//--> amount
        }else {
            txt_inputAmount.setText("0 원 ");//--> amount
        }
    }

    //---EditText Search Journal Data Table
    @SuppressLint("SetTextI18n")
    private void searchSiteDate() {
        cost_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 1, 1, 1);

        //TextView Cost Table ID
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Cost Table csid
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" csid ");

        row0.addView(tv1);

        //TextView Cost Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일  자 ");

        row0.addView(tv2);

        //TextView Cost Table Site
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText(" 현  장 ");

        row0.addView(tv3);

        //TextView Cost Table Detail
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 내 용 ");

        row0.addView(tv4);

        //TextView Cost Table Price
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 단  가 ");

        row0.addView(tv5);

        //TextView Cost Table Amount
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 금  액 ");

        row0.addView(tv6);

        //TextView Cost Table Memo
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" 메  모 ");

        row0.addView(tv7);

        //TextView Cost Table stid
        TextView tv8 = new TextView(this);
        tv8.setLayoutParams(tlp);

        tv8.setBackgroundColor(Color.BLUE);
        tv8.setTextColor(Color.WHITE);
        tv8.setGravity(Gravity.CENTER);
        tv8.setTextSize(18);
        tv8.setPadding(1, 1, 1, 1);

        tv8.setText(" stid ");

        row0.addView(tv8);

        //TableLayout View
        cost_table.addView(row0);

        //Data Load
        String searchSite = edtxt_Search.getText().toString();
        String startDay = edtxt_StartDate.getText().toString();
        String endDay = edtxt_EndDate.getText().toString();
        try {
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = costController.searchSiteDate(searchSite, startDay, endDay);
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(3, 1, 1, 3);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma

                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }


                if (j == 6) {
                    int rAmount = cus.getInt(6);
                    String formattedAmount = formatPay.format(rAmount);
                    tv.setText(formattedAmount);
                }


                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), CostEditActivity.class);
                            //id
                            positionIntent.putExtra("id", cus.getInt(0));
                            //cost cid
                            positionIntent.putExtra("csid", cus.getString(1));
                            //Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Site Name
                            positionIntent.putExtra("site", cus.getString(3));
                            //detail
                            positionIntent.putExtra("detail", cus.getString(4));
                            //price
                            positionIntent.putExtra("price", cus.getInt(5));
                            //amount
                            positionIntent.putExtra("amount", cus.getInt(6));
                            //memo
                            positionIntent.putExtra("memo", cus.getString(7));
                            //sid
                            positionIntent.putExtra("stid", cus.getString(8));


                            String site = positionIntent.getExtras().getString("site");
                            Toast.makeText(CostTableActivity.this,
                                    site + "를 선택 했습니다. :",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            cost_table.addView(row);
        }
        costController.close();

    }

    //--- search date cost sum
    @SuppressLint({"SetTextI18n", "Range"})
    private void sumSearchSiteDate(){
        String site = edtxt_Search.getText().toString();
        String stda = edtxt_StartDate.getText().toString();
        String enda = edtxt_EndDate.getText().toString();

        try {
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cursor cur = costController.sumSearchSiteDate(site,stda,enda);

        if (cur.getString(cur.getColumnIndex("costAmount")) != null){
            int amounts = cur.getInt(0);
            String amt_format = formatPay.format(amounts);
            txt_inputAmount.setText(amt_format + " 원 ");//--> amount
        }else {
            txt_inputAmount.setText("0 원 ");//--> amount
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_new_table:
                tableDateTime();
                edtxt_Search.setText("");

                return true;

            case R.id.toolbar_add_table:
                Toast.makeText(getApplicationContext(),
                        "경비 쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_costAdd = new Intent(getApplicationContext(), CostAddActivity.class);
                startActivity(intent_costAdd);
                finish();

                return true;

            case R.id.toolbar_close_table:
                Toast.makeText(getApplicationContext(),
                        "경비 쓰기를 닫습니다.", Toast.LENGTH_SHORT).show();
                Intent cost_table = new Intent(getApplicationContext(), CostListActivity.class);
                startActivity(cost_table);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}