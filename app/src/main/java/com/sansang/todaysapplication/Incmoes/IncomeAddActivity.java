package com.sansang.todaysapplication.Incmoes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.IncomeController;
import com.sansang.todaysapplication.DatabaseController.JournalController;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.NumberTextWatcher.NumberTextWatcher;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeAddActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    private EditText edtxt_icid, edtxt_date, edtxt_leader, edtxt_deposit, edtxt_tax, edtxt_memo,edtxt_stid,edtxt_tmid;
    private TodayDatabase todayDatabase;
    private IncomeController incomeController;
    private SiteController siteController;
    private JournalController journalController;
    //ListView Dialog
    private Button btn_spinner_down;
    private TextView dialog_spinner_txt;
    private static final String TAG_TEXT = "text";
    private static final String TAG_IMAGE = "image";
    List<Map<String, Object>> dialogItemList;
    int[] image = {R.drawable.img_s0002};
    String[] text = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);

        todayDatabase = new TodayDatabase(this);
        incomeController = new IncomeController(this);
        siteController = new SiteController(this);
        journalController = new JournalController(this);

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("수입 추가하기");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIncomeListActivity();
            }
        });

        //findViewById
        edtxt_icid = findViewById(R.id.edtxt_income_add_icid);
        edtxt_date = findViewById(R.id.edtxt_income_add_date);
        edtxt_leader = findViewById(R.id.edtxt_income_add_leader);
        edtxt_deposit = findViewById(R.id.edtxt_income_add_deposit);
        edtxt_tax = findViewById(R.id.edtxt_income_add_tax);
        edtxt_memo = findViewById(R.id.edtxt_income_add_memo);
        edtxt_stid = findViewById(R.id.edtxt_income_add_stid);
        edtxt_tmid = findViewById(R.id.edtxt_income_add_tmid);

        //--> EditText 항목 쓰기방지(read-only) or android:focusable="false"
        edtxt_icid.setInputType(InputType.TYPE_NULL);
        edtxt_leader.setInputType(InputType.TYPE_NULL);

        edtxt_deposit.requestFocus();

        edtxt_date.setText(dateTime());

        dateChange();
        textChangedListener();
        incomeAutoId();

        //ListView Dialog
        dialog_spinner_txt = findViewById(R.id.dialog_spinner_txt);
        btn_spinner_down = findViewById(R.id.btn_spinner_down);
        btn_spinner_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

    }

    //--- Income Auto Id
    @SuppressLint("SetTextI18n")
    private void incomeAutoId() {
        //Data Load
        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = incomeController.incomeAutoId();
        final int rows = cus.getCount();
        String incomeid = "ic_";
        int idNo = 1;

        if (rows == 0) {
            edtxt_icid.setText( incomeid + idNo );
        } else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            edtxt_icid.setText(incomeid + rid);
        }
    }

    public void textChangedListener(){
        edtxt_deposit.addTextChangedListener(new NumberTextWatcher(edtxt_deposit));
        edtxt_tax.addTextChangedListener(new NumberTextWatcher(edtxt_tax));

    }

    @SuppressLint("MissingInflatedId")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final TextView title = view.findViewById(R.id.dialog_spinner_txt);

        final ListView listview = view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();
        //title.setText("현장을 선택 하세요?");

        // database handler
        try {
            incomeController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Spinner Drop down elements
        List<String> data = incomeController.getAllSpinnerSite();

        String[] item_data = data.toArray(new String[0]);
        int size = 0;
        for (String temp:data){
            item_data[size++] = temp;
        }
        text = item_data;

        dialogItemList = new ArrayList<>();

        for(int i=0;i<data.size();i++)
        {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put(TAG_IMAGE, image[0]);
            itemMap.put(TAG_TEXT, text[i]);

            dialogItemList.add(itemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), dialogItemList, R.layout.alert_dialog_row,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView} );
        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_spinner_txt.setText(text[position]);

                // Showing selected  item
                // On selecting a  item
                String site = dialog_spinner_txt.getText().toString();
                if (site.equals("현장을 선택 하세요?")) {
                    //Do nothing.
                } else {
                    // Showing selected spinner item
                    Toast.makeText( parent.getContext(), "You selected: " + site,
                            Toast.LENGTH_SHORT ).show();

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    final Cursor cus = incomeController.siteSpinnerResult( site );
                    final int rows = cus.getCount();
                    final int clums = cus.getColumnCount();

                    cus.moveToFirst();
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < clums; j++) {

                            final int p = i;
                            final int r = i + 1;

                            if (rows == 0) {
                                return;
                            } else if (cus.moveToPosition( p )) {

                                String tleader = cus.getString( 0 );
                                edtxt_leader.setText( tleader );
                                String spay = cus.getString( 1 );
                                //etxt_Pay.setText( spay);
                                String stId = cus.getString(2);
                                edtxt_stid.setText(stId);
                                String tmId = cus.getString(3);
                                edtxt_tmid.setText(tmId);

                                edtxt_deposit.requestFocus();

                            }
                        }

                    }
                }

                dialog.dismiss();
            }
        });

        incomeController.close();

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    @SuppressLint("SimpleDateFormat")
    private String dateTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return mFormat.format(mDate);
    }

    private void dateChange(){
        edtxt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog();
            }
        });
    }

    private void DatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( year, month, dayOfMonth );
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get( Calendar.MONTH ) + 1;
                        dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );
                        @SuppressLint("DefaultLocale")
                        String siteDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );
                        edtxt_date.setText( siteDate );
                        //etxtDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    public void goToIncomeListActivity(){
        Intent intent = new Intent(getApplicationContext(), IncomeListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.add_toolbar_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_save_add:
                if (edtxt_leader.length() == 0 || edtxt_deposit.length() == 0){
                    Toast.makeText( IncomeAddActivity.this,
                            "팀장/회사 또는 수입금액을 입력 하세요?", Toast.LENGTH_SHORT ).show();
                }else {
                    String incId = edtxt_icid.getText().toString();
                    String incDate = edtxt_date.getText().toString();
                    String incLeader = edtxt_leader.getText().toString();
                    String incDeposit = edtxt_deposit.getText().toString().replace( ",", "" );
                    String incTax = edtxt_tax.getText().toString().replace( ",", "" );
                    String incMemo = edtxt_memo.getText().toString();
                    String incStid = edtxt_stid.getText().toString();
                    String incTmid = edtxt_tmid.getText().toString();

                    try {
                        incomeController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    incomeController.insertIncome( incId, incDate, incLeader, incDeposit, incTax, incMemo, incStid, incTmid );

                    Toast.makeText(getApplicationContext(),
                            "수입 내용을  추가", Toast.LENGTH_SHORT).show();

                    Intent saveincomeintent = new Intent( getApplicationContext(), IncomeListActivity.class );
                    startActivity( saveincomeintent );

                    finish();
                }

                return true;

            case R.id.toolbar_close_add:
                Toast.makeText(getApplicationContext(),
                        "수입/경비 추가 끝내기", Toast.LENGTH_SHORT).show();
                Intent intent_addClose = new Intent( getApplicationContext(), IncomeListActivity.class );
                startActivity( intent_addClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}