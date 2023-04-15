package com.sansang.todaysapplication.Teams;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansang.todaysapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TeamAddActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private EditText etxt_id,etxt_tmid,etxt_tmleader,etxt_tmphone,etxt_tmdate,etxt_tmmemo;
    //private TeamDatabase teamDB;
    //private TeamControl teamControl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_add);

        initView();

        //EditText Date Click
        etxt_tmdate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                @SuppressLint({"NewApi", "LocalSuppress"}) final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                DatePickerDialog();
            }
        } );

        teamAutoId();
        DateTime();
    }

    private void teamAutoId() {
        /*
        teamControl.open();
        final Cursor cus = teamControl.teamAutoId();
        final int rows = cus.getCount();
        String teamId = "t_";
        int idNo = 1;

        if (rows == 0){
            editText_tid.setText( "t_" + idNo );
        }else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            editText_tid.setText(teamId + rid);
        }
        */
    }


    private void initView() {
        Toolbar communityToolbar = findViewById(R.id.customToolbar);
        communityToolbar.setTitle("팀 추가하기");
        setSupportActionBar(communityToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        communityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        //teamDB = new TeamDatabase( this );
        //teamControl = new TeamControl( this );

        etxt_id = findViewById(R.id.tid_etxt);
        etxt_tmid = findViewById(R.id.tmId_etxt);
        etxt_tmleader = findViewById(R.id.tmLeader_etxt);
        etxt_tmphone = findViewById(R.id.tmPhone_etxt);
        etxt_tmdate = findViewById(R.id.tmDate_etxt);
        etxt_tmmemo = findViewById(R.id.tmMemo_etxt);

        //Focus
        etxt_tmid.setFocusable( false );
        etxt_tmleader.requestFocus();
        etxt_tmdate.setFocusable( false );
        etxt_tmdate.setText(DateTime());

        //Result Intent Data
        Intent positionIntent = getIntent();

        int rid = 1;
        String tmid = "t_1";
        String leader = "sanda";
        String mobile = "000-0000-0000";
        String date = "2023-01-01";
        String memo = "test";
//        String leader = positionIntent.getExtras().getString( "leader" ); //String 형
//        String phone = positionIntent.getExtras().getString( "mobile" ); //String 형
//        String tDate = positionIntent.getExtras().getString( "date" ); //String 형
//        String memo = positionIntent.getExtras().getString( "memo" ); //String 형

        etxt_tmid.setText( tmid );
        etxt_tmleader.setText( leader );
        etxt_tmphone.setText( mobile );
        etxt_tmdate.setText( date );
        etxt_tmmemo.setText( memo );
        etxt_id.setText( String.valueOf( rid ) );
    }

    public void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), TeamListActivity.class);
        startActivity(intent);
        finish();
    }

    private String DateTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
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
                        String teamDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );
                        etxt_tmdate.setText( teamDate );
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_save_team:
                Toast.makeText(getApplicationContext(), "저장 버튼 클릭", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), TeamListActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.toolbar_search_team:
                Toast.makeText(getApplicationContext(), "검색 버튼 클릭", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}