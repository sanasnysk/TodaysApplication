package com.sansang.todaysapplication.Sites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.SiteController;
import com.sansang.todaysapplication.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteAddActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;

    private final String TAG = this.getClass().getSimpleName();
    private EditText etxt_stId,etxt_stName,etxt_stPay,etxt_stManager,etxt_stDate,etxt_stMemo,etxt_tmId,etxt_tmLeader;

    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;
    private SiteController siteController;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result="";
    //ListView Dialog
    private Button btn_down;
    private TextView txt_dialog_spinner;
    private static final String TAG_TEXT = "text";
    private static final String TAG_IMAGE = "image";
    List<Map<String, Object>> dialogItemList;
    int[] image = {R.drawable.img_s0002};
    String[] text = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_add);

        todayDatabase = new TodayDatabase(this);
        siteController = new SiteController(this);

        initView();
        siteAutoId();

    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("싸이트 추가하기");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSiteListActivity();
            }
        });

        txt_dialog_spinner = findViewById(R.id.dialog_spinner_txt);

        etxt_stId = findViewById(R.id.stId_add_etxt);
        etxt_stName = findViewById(R.id.stName_add_etxt);
        etxt_stPay = findViewById(R.id.stPay_add_etxt);
        etxt_stManager = findViewById(R.id.stManager_add_etxt);
        etxt_stDate = findViewById(R.id.stDate_add_etxt);
        etxt_stMemo = findViewById(R.id.stMemo_add_etxt);
        etxt_tmId = findViewById(R.id.tmId_st_add_etxt);
        etxt_tmLeader = findViewById(R.id.tmLeader_st_add_etxt);

        etxt_stName.requestFocus();

        etxt_stDate.setText(dateTime());

        dateChange();
        payTextWatcher();

        btn_down = findViewById(R.id.btn_spinner_down);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    public void goToSiteListActivity(){
        Intent intent = new Intent(getApplicationContext(), SiteListActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void siteAutoId(){
        siteController.open();
        final Cursor cus = siteController.siteAutoId();
        final int rows = cus.getCount();
        String siteId = "s_";
        int idNo = 1;

        if (rows == 0) {
            etxt_stId.setText( "s_" + idNo );
        } else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            etxt_stId.setText(siteId + rid);
        }
    }

    @SuppressLint("ResourceType")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null);
        builder.setView(view);

        final TextView title = view.findViewById(R.id.textview_alterdialog_title);
        final ListView listview = view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();

        // database handler
        siteController.open();
        // ListView Dropdown elements
        List<String> data = siteController.getTeamSpinner();

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
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                txt_dialog_spinner.setText(text[postion]);
                String teams = txt_dialog_spinner.getText().toString();

                if (teams.equals("팀을 선택하세요?")){
                    // Do Nothing
                }else {
                    Toast.makeText(SiteAddActivity.this, "your selected :" + teams, Toast.LENGTH_SHORT).show();
                    etxt_tmLeader.setText(teams);

                    // outer for loop
                    //---Data teams leader and team id 출력
                    final Cursor cus = siteController.resultTeamSpinner(teams);
                    final int rows = cus.getCount();
                    final int clums = cus.getColumnCount();

                    cus.moveToFirst();
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < clums; j++) {

                            final int p = i;
                            final int r = i + 1;

                            if (rows == 0) {
                                return;
                            } else if (cus.moveToPosition(p)){

                                String tmId = cus.getString(0);
                                etxt_tmId.setText(tmId);

                                etxt_stName.requestFocus();

                            }
                        }
                    }
                }

                dialog.dismiss();
            }
        });

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
        etxt_stDate.setOnClickListener(new View.OnClickListener() {
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

    private void payTextWatcher(){
        //-- 숫자 입력 콤마
        TextWatcher watcher_pay = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    etxt_stPay.setText(result);
                    etxt_stPay.setSelection(result.length());
                }
            }
            @Override
            public void afterTextChanged( Editable s ) {

            }
        };

        etxt_stPay.addTextChangedListener(watcher_pay);
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
                        etxt_stDate.setText( siteDate );
                        //etxtDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_save_add:
                if (etxt_stName.length() == 0 || etxt_stPay.length() == 0){
                    Toast.makeText( SiteAddActivity.this,
                            "현장 또는 일당을 입력하세요?", Toast.LENGTH_LONG ).show();
                }else if (etxt_tmLeader.length() == 0){
                    Toast.makeText( SiteAddActivity.this,
                            "회사/팀장 이름을 입력하세요?", Toast.LENGTH_LONG ).show();
                }else {
                    String stid = etxt_stId.getText().toString().trim();
                    String stname = etxt_stName.getText().toString().trim();
                    String stpay = etxt_stPay.getText().toString().trim();
                    String stmanager = etxt_stManager.getText().toString().trim();
                    String stdate = etxt_stDate.getText().toString().trim();
                    String stmemo = etxt_stMemo.getText().toString().trim();
                    String tmleader = etxt_tmLeader.getText().toString().trim();
                    String tmid = etxt_tmId.getText().toString().trim();

                    siteController.open();
                    siteController.insertSite(stid,stname,stpay,stmanager,stdate,stmemo,tmleader,tmid);
                    Toast.makeText(getApplicationContext(), "새로운 현장을 저장 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent_site = new Intent(getApplicationContext(), SiteListActivity.class);
                    startActivity(intent_site);

                    finish();
                }
                return true;

            case R.id.toolbar_close_add:
                Toast.makeText(getApplicationContext(),
                        "현장 추가를 닫습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SiteListActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return false;
        }
    }
}