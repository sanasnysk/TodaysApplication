package com.sansang.todaysapplication.Costs;

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
import com.sansang.todaysapplication.DatabaseController.CostController;
import com.sansang.todaysapplication.NumberTextWatcher.NumberTextWatcher;
import com.sansang.todaysapplication.NumberTextWatcher.NumberTextWatcher_ex;
import com.sansang.todaysapplication.R;

import java.io.IOException;
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

public class CostAddActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    private EditText editx_csid, edtxt_date, edtxt_site, edtxt_detail, edtxt_price, edtxt_amount,edtxt_memo, edtxt_stid;
    private TodayDatabase todayDatabase;
    private CostController costController;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result="";
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
        setContentView(R.layout.activity_cost_add);

        try {
            todayDatabase = new TodayDatabase(this);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        costController = new CostController(this);

        initView();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("경비 추가하기");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCostListActivity();
            }
        });

        editx_csid = findViewById( R.id.edtxt_cost_add_csid );
        edtxt_date = findViewById( R.id.edtxt_cost_add_date );
        edtxt_site = findViewById( R.id.edtxt_cost_add_site );
        edtxt_detail = findViewById( R.id.edtxt_cost_add_detail );
        edtxt_price = findViewById( R.id.edtxt_cost_add_price );
        edtxt_amount = findViewById( R.id.edtxt_cost_add_amount );
        edtxt_memo = findViewById( R.id.edtxt_cost_add_memo );
        edtxt_stid = findViewById( R.id.edtxt_cost_add_stid );

        //--> EditText 항목 쓰기방지(read-only) or android:focusable="false"
        editx_csid.setInputType( InputType.TYPE_NULL );
        edtxt_site.setInputType( InputType.TYPE_NULL );
        edtxt_date.setInputType( InputType.TYPE_NULL );
        edtxt_date.setFocusable( false );
        edtxt_detail.requestFocus();

        edtxt_date.setText(dateTime());

        costAutoId();
        dateChange();
        textChangedListener();
        //priceTextWatcher();
        //amountTextWatcher();

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
    private void costAutoId() {
        //Data Load
        try {
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = costController.costAutoId();
        final int rows = cus.getCount();
        String costid = "cs_";
        int idNo = 1;

        if (rows == 0) {
            editx_csid.setText( costid + idNo );
        } else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            editx_csid.setText(costid + rid);
        }
        costController.close();
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
            costController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Spinner Drop down elements
        List<String> data = costController.getAllSpinnerSite();

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
                    final Cursor cus = costController.siteSpinnerResult( site );
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
                                String stId = cus.getString(0);
                                edtxt_stid.setText(stId);

                                String siteName = cus.getString( 1 );
                                edtxt_site.setText( siteName );

                                String spay = cus.getString( 2 );
                                //etxt_Pay.setText( spay);

                                edtxt_detail.requestFocus();

                            }
                        }

                    }
                }

                dialog.dismiss();
            }
        });

        costController.close();

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void goToCostListActivity(){
        Intent intent = new Intent(getApplicationContext(), CostListActivity.class);
        startActivity(intent);
        finish();
    }

    public void textChangedListener(){
        edtxt_price.addTextChangedListener(new NumberTextWatcher(edtxt_price));
        edtxt_amount.addTextChangedListener(new NumberTextWatcher(edtxt_amount));

        Locale locale = new Locale("ko", "KR");
        int numDecs = 2; // Let's use 2 decimals

        edtxt_price.addTextChangedListener(new NumberTextWatcher_ex(edtxt_price,locale,numDecs));
        edtxt_amount.addTextChangedListener(new NumberTextWatcher_ex(edtxt_amount,locale,numDecs));
    }

    private void priceTextWatcher(){
        //-- 숫자 입력 콤마
        TextWatcher watcher_price = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    edtxt_price.setText(result);
                    edtxt_price.setSelection(result.length());
                }
            }
            @Override
            public void afterTextChanged( Editable s ) {

            }
        };

        edtxt_price.addTextChangedListener(watcher_price);
    }

    public void amountTextWatcher(){
        TextWatcher watcher_amount = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    edtxt_amount.setText(result);
                    edtxt_amount.setSelection(result.length());
                }
            }
            @Override
            public void afterTextChanged( Editable s ) {

            }
        };

        edtxt_amount.addTextChangedListener(watcher_amount);
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
        getMenuInflater().inflate(R.menu.add_toolbar_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_save_add:
                if (edtxt_site.length() == 0 || edtxt_detail.length() == 0){
                    Toast.makeText( CostAddActivity.this,
                            "현장 또는 내용을 입력 하세요?", Toast.LENGTH_SHORT ).show();
                }else {
                    String csid = editx_csid.getText().toString();
                    String date = edtxt_date.getText().toString();
                    String site = edtxt_site.getText().toString();
                    String detail = edtxt_detail.getText().toString();
                    String price = edtxt_price.getText().toString().replace(",", "");
                    String amount = edtxt_amount.getText().toString().replace( ",", "" );
                    String memo = edtxt_memo.getText().toString();
                    String stid = edtxt_stid.getText().toString();

                    try {
                        costController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    costController.insertCost( csid, date, site, detail, price, amount, memo, stid );

                    costController.close();

                    Toast.makeText(getApplicationContext(),
                            "경비 내용을  추가", Toast.LENGTH_SHORT).show();

                    Intent savecostintent = new Intent( getApplicationContext(), CostListActivity.class );
                    startActivity( savecostintent );

                    finish();
                }

                return true;

            case R.id.toolbar_close_add:
                Toast.makeText(getApplicationContext(),
                        "경비 추가 끝내기", Toast.LENGTH_SHORT).show();
                Intent intent_addClose = new Intent( getApplicationContext(), CostListActivity.class );
                startActivity( intent_addClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}