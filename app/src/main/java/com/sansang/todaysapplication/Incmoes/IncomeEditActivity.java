package com.sansang.todaysapplication.Incmoes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.sansang.todaysapplication.NumberTextWatcher.NumberTextWatcher_ex;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IncomeEditActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    private EditText edtxt_id,edtxt_icid, edtxt_date, edtxt_leader, edtxt_deposit, edtxt_tax, edtxt_memo, edtxt_stid, edtxt_tmid;
    private TodayDatabase todayDatabase;
    private IncomeController incomeController;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result_deposit, result_tax;
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
        setContentView(R.layout.activity_income_edit);

        todayDatabase = new TodayDatabase(this);
        incomeController = new IncomeController(this);

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("수입 수정하기");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIncomeListActivity();
            }
        });

        edtxt_id = findViewById(R.id.edtxt_income_edit_id);
        edtxt_icid = findViewById(R.id.edtxt_income_edit_icid);
        edtxt_date = findViewById(R.id.edtxt_income_edit_date);
        edtxt_leader = findViewById(R.id.edtxt_income_edit_leader);
        edtxt_deposit = findViewById(R.id.edtxt_income_edit_deposit);
        edtxt_tax = findViewById(R.id.edtxt_income_edit_tax);
        edtxt_memo = findViewById(R.id.edtxt_income_edit_memo);
        edtxt_stid = findViewById(R.id.edtxt_income_edit_stid);
        edtxt_tmid = findViewById(R.id.edtxt_income_edit_tmid);

        dialog_spinner_txt = findViewById(R.id.dialog_spinner_txt);

        edtxt_date.setInputType(InputType.TYPE_NULL);
        edtxt_date.setFocusable(false);
        edtxt_deposit.requestFocus();
        edtxt_date.setText(dateTime());

        dateChange();
        //depositTextWatcher();
        //taxTextWatcher();
        textChangedListener();

        //ListView Dialog
        dialog_spinner_txt = findViewById(R.id.dialog_spinner_txt);
        btn_spinner_down = findViewById(R.id.btn_spinner_down);
        btn_spinner_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        getIntentResult();

    }

    public void goToIncomeListActivity(){
        Intent intent = new Intent(getApplicationContext(), IncomeListActivity.class);
        startActivity(intent);
        finish();
    }

    private void getIntentResult() {
        //--- Intent Result Data -----
        Intent positionIntent = getIntent();

        int id = positionIntent.getExtras().getInt( "id" ); // int 형
        String icid = positionIntent.getExtras().getString( "icid" ); //String 형
        String date = positionIntent.getExtras().getString( "date" );
        String leader = positionIntent.getExtras().getString( "leader" );
        int deposit = positionIntent.getExtras().getInt( "deposit" );
        int tax = positionIntent.getExtras().getInt( "tax" );
        String memo = positionIntent.getExtras().getString( "memo" );
        String stid = positionIntent.getExtras().getString( "stid" );
        String tmid = positionIntent.getExtras().getString("tmid");

        edtxt_id.setText( String.valueOf( id ) );
        edtxt_icid.setText( icid );
        edtxt_date.setText( date );
        edtxt_leader.setText( leader );
        edtxt_deposit.setText( String.valueOf( deposit ) );
        edtxt_tax.setText( String.valueOf( tax ) );
        edtxt_memo.setText( memo );
        edtxt_stid.setText( stid );
        edtxt_tmid.setText(tmid);

        dialog_spinner_txt.setText(leader);
    }

    private void depositTextWatcher(){
        TextWatcher watcher_deposit = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_deposit)){
                    result_deposit = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    edtxt_deposit.setText(result_deposit);
                    edtxt_deposit.setSelection(result_deposit.length());
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        edtxt_deposit.addTextChangedListener(watcher_deposit);
    }

    private void taxTextWatcher(){
        TextWatcher watcher_tax = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_tax)){
                    result_tax = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    edtxt_tax.setText(result_tax);
                    edtxt_tax.setSelection(result_tax.length());
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        edtxt_tax.addTextChangedListener(watcher_tax);
    }

    public void textChangedListener(){
        //edtxt_price.addTextChangedListener(new NumberTextWatcher(edtxt_price));
        //edtxt_amount.addTextChangedListener(new NumberTextWatcher(edtxt_amount));
        Locale locale = new Locale("ko", "KR");
        int numDecs = 2; // Let's use 2 decimals

        edtxt_deposit.addTextChangedListener(new NumberTextWatcher_ex(edtxt_deposit,locale,numDecs));
        edtxt_tax.addTextChangedListener(new NumberTextWatcher_ex(edtxt_tax,locale,numDecs));
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
        List<String> data = incomeController.siteSpinnerLimit();

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

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.edit_toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_edit_update:
                if (edtxt_leader.length() == 0){
                    Toast.makeText( IncomeEditActivity.this, "not Data Title",
                            Toast.LENGTH_LONG ).show();
                    edtxt_leader.requestFocus();
                }else if (edtxt_deposit.length() == 0){
                    Toast.makeText( IncomeEditActivity.this, "not Data Leader",
                            Toast.LENGTH_SHORT ).show();
                    edtxt_deposit.requestFocus();
                }else {
                    //Update Data
                    String id = edtxt_id.getText().toString();
                    String icid = edtxt_icid.getText().toString();
                    String date = edtxt_date.getText().toString();
                    String leader = edtxt_leader.getText().toString();
                    String deposit = edtxt_deposit.getText().toString().replace( ",", "" );
                    String tax = edtxt_tax.getText().toString().replace( ",", "" );
                    String memo = edtxt_memo.getText().toString();
                    String stid = edtxt_stid.getText().toString();
                    String tmid = edtxt_tmid.getText().toString();
                    //db open
                    try {
                        incomeController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    incomeController.updateIncome( id, icid, date, leader, deposit, tax, memo, stid, tmid );

                    incomeController.close();

                    Intent intentIncomeupdate = new Intent( getApplicationContext(), IncomeListActivity.class );
                    startActivity( intentIncomeupdate );
                    finish();
                }
                return true;

            case R.id.toolbar_delete_update:
                String id = edtxt_id.getText().toString();
                String iid = edtxt_icid.getText().toString();
                if (edtxt_id.length() > 0){
                    try {
                        incomeController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    incomeController.deleteIncome(id);

                    incomeController.close();

                    Toast.makeText( IncomeEditActivity.this,
                            "Deleted" + iid, Toast.LENGTH_LONG ).show();
                    Intent updateintent = new Intent( getApplicationContext(), IncomeListActivity.class );
                    startActivity( updateintent );

                    finish();
                }else {
                    Toast.makeText( IncomeEditActivity.this,
                            "Not Deleted" + iid, Toast.LENGTH_LONG ).show();
                }
                return true;

            case R.id.toolbar_close_update:
                Toast.makeText(getApplicationContext(),
                        "수입 추가 끝내기", Toast.LENGTH_SHORT).show();
                Intent intent_updateClose = new Intent( getApplicationContext(), IncomeListActivity.class );
                startActivity( intent_updateClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}