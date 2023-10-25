package com.sansang.todaysapplication.Journals;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.JournalController;
import com.sansang.todaysapplication.NumberTextWatcher.NumberTextWatcher;
import com.sansang.todaysapplication.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalAddActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    private EditText etxt_jnId, etxt_date, etxt_Site, etxt_one, etxt_Pay, etxt_Amount, etxt_Memo, etxt_stId, etxt_tmLeader, etxt_tmId;
    private TodayDatabase todayDatabase;
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
        setContentView(R.layout.activity_journal_add);

        todayDatabase = new TodayDatabase(this);
        journalController = new JournalController(this);

        initView();
        jourAutoId();
    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("일지 추가하기");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJournalListActivity();
            }
        });

        //--- findView
        etxt_jnId = findViewById( R.id.edxt_add_journal_jnid );
        etxt_date = findViewById( R.id.edxt_add_journal_date );
        etxt_Site = findViewById( R.id.edxt_add_journal_site );
        etxt_one = findViewById( R.id.edxt_add_journal_one );
        etxt_Pay = findViewById( R.id.edxt_add_journal_pay );
        etxt_Amount = findViewById( R.id.edxt_add_journal_amount );
        etxt_Memo = findViewById( R.id.edxt_add_journal_memo );
        etxt_stId = findViewById( R.id.edxt_add_journal_stid );
        etxt_tmLeader = findViewById( R.id.edxt_add_journal_tmleader );
        etxt_tmId = findViewById( R.id.edxt_add_journal_tmid );

        etxt_Pay.setText("0");
        etxt_Amount.setText("0");

        etxt_one.requestFocus();

        etxt_date.setText(dateTime());

        dateChange();
        oneAddTextChangedListener();

        //--> Comma in
        etxt_Pay.addTextChangedListener( new NumberTextWatcher( etxt_Pay ) );
        etxt_Amount.addTextChangedListener( new NumberTextWatcher( etxt_Amount ) );

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

    public void goToJournalListActivity(){
        Intent intent = new Intent(getApplicationContext(), JournalListActivity.class);
        startActivity(intent);
        finish();
    }

    //----- Journal Table AutoId -----
    @SuppressLint("SetTextI18n")
    private void jourAutoId() {
        //Data Load
        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final Cursor cus = journalController.journalAutoId();
        final int rows = cus.getCount();
        String journalid = "jn_";
        int idNo = 1;
        int r = cus.getInt( 0 );

        if (cus == null) {
            etxt_jnId.setText( journalid + idNo );
        } else {
            int rid = r + idNo;
            etxt_jnId.setText(journalid + rid);
        }
        journalController.close();
    }

    @SuppressLint("ResourceType")
    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final TextView title = view.findViewById(R.id.textview_alterdialog_title);
        final ListView listview = view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();

        // database handler
        try {
            journalController.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // ListView Dropdown elements
        List<String> data = journalController.getAllSpinnerSite();

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
                //textview_result.setText(parent.getItemAtPosition(position).toString());
                dialog_spinner_txt.setText(text[position]);
                // Showing selected  item
                // On selecting a spinner item
                String site = dialog_spinner_txt.getText().toString();
                if (site.equals("현장을 선택 하세요?")) {
                    //Do nothing....
                } else {
                    // Showing selected spinner item
                    Toast.makeText( parent.getContext(), "You selected: " + site,
                            Toast.LENGTH_SHORT ).show();

                    etxt_Site.setText( site );

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    final Cursor cus = journalController.siteSpinnerResult( site );
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
                                etxt_stId.setText(stId);

                                String spay = cus.getString( 1 );
                                etxt_Pay.setText( spay);

                                String tleader = cus.getString( 2 );
                                etxt_tmLeader.setText( tleader );

                                String tmId = cus.getString(3);
                                etxt_tmId.setText(tmId);

                                etxt_one.requestFocus();

                            }
                        }

                    }

                    if (etxt_one == null) {
                        float soneday = 0;
                        int pay = Integer.parseInt( etxt_Pay.getText().toString().replace( ",", "" ) );
                        float amount = soneday * pay;
                        etxt_Amount.setText( String.valueOf( amount ) );

                    } else if (etxt_one.length() > 0) {

                        float oneday = Float.parseFloat( etxt_one.getText().toString() );
                        int pay = Integer.parseInt( etxt_Pay.getText().toString().replace( ",", "" ) );
                        int amount = (int) (oneday * pay);
                        etxt_Amount.setText( String.valueOf( amount ) );

                    } else {
                        //Do nothing
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        //dialog.getWindow().setLayout(900,1200);
    }

    //----- One day add Text Changed Listener -----
    private void oneAddTextChangedListener(){
        etxt_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxt_one == null) {
                    //Do nothing
                    float soneday = 0;
                    int pay = Integer.parseInt( etxt_Pay.getText().toString() );
                    int amount = (int) (soneday * pay);
                    etxt_Amount.setText( String.valueOf( amount ) );

                } else if (etxt_one.length() > 0) {

                    float oneday = Float.parseFloat( s.toString() );

                    String sPay = etxt_Pay.getText().toString().replace( ",", "" );
                    int pay = Integer.parseInt(sPay);

                    int amount = (int) (oneday * pay);
                    etxt_Amount.setText( String.valueOf( amount ) );
                } else {
                    //Do nothing
                }

            }

            @Override
            public void afterTextChanged( Editable s) {
            }
        } );
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
        etxt_date.setOnClickListener(new View.OnClickListener() {
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

    //----- DatePickerDialog -----
    private void DatePickerDialog() {
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

                        @SuppressLint("DefaultLocale")
                        String startDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );

                        etxt_date.setText( startDate );
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
        switch (item.getItemId())
        {
            case R.id.toolbar_save_add:
                if (etxt_date.length() == 0 || etxt_Site.length() == 0) {
                    Toast.makeText( JournalAddActivity.this,
                            "날짜와 이름을 입력하세요?", Toast.LENGTH_LONG ).show();
                } else if (etxt_one.length() == 0) {
                    Toast.makeText( JournalAddActivity.this,
                            "일량을 입력하세요?", Toast.LENGTH_SHORT ).show();
                } else {
                    String jid = etxt_jnId.getText().toString();
                    String date = etxt_date.getText().toString();
                    String site = etxt_Site.getText().toString();
                    String day = etxt_one.getText().toString();
                    String pay = etxt_Pay.getText().toString().replace( ",", "" );
                    String amount = etxt_Amount.getText().toString().replace( ",", "" );
                    String memo = etxt_Memo.getText().toString();
                    String sid = etxt_stId.getText().toString();
                    String tleader = etxt_tmLeader.getText().toString();
                    String tid = etxt_tmId.getText().toString();

                    try {
                        journalController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    journalController.insertJournal( jid,date,site,day,pay,amount,memo,sid,tleader,tid );
                    journalController.close();

                    Toast.makeText(getApplicationContext(),
                            "일지 내용을 저장 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent inSavejournal = new Intent( getApplicationContext(), JournalListActivity.class );
                    startActivity( inSavejournal );
                    finish();
                }
                return true;

            case R.id.toolbar_close_add:
                Toast.makeText(getApplicationContext(),
                        "일지 쓰기를 종료합니다.", Toast.LENGTH_SHORT).show();
                Intent intentAddClose = new Intent( getApplicationContext(), JournalListActivity.class );
                startActivity( intentAddClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}