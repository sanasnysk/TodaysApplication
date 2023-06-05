package com.sansang.todaysapplication.Teams;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.TeamController;
import com.sansang.todaysapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TeamAddActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat;
    //SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private EditText etxt_tmid,etxt_tmleader,etxt_tmmobile,etxt_tmdate,etxt_tmmemo;
    private TodayDatabase todayDatabase;
    private TeamController teamController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_add);

        initView();

    }

    @SuppressLint("SimpleDateFormat")
    private void initView() {
        Toolbar communityToolbar = findViewById(R.id.customToolbar);
        communityToolbar.setTitle("팀 추가하기");
        setSupportActionBar(communityToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        communityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTeamListActivity();
            }
        });

        mFormat = new SimpleDateFormat("yyyy-MM-dd");

        todayDatabase = new TodayDatabase( this );
        teamController = new TeamController( this );

        etxt_tmid = findViewById(R.id.tmId_etxt);
        etxt_tmleader = findViewById(R.id.tmLeader_etxt);
        etxt_tmmobile = findViewById(R.id.tmMobile_etxt);
        etxt_tmdate = findViewById(R.id.tmDate_etxt);
        etxt_tmmemo = findViewById(R.id.tmMemo_etxt);

        //Focus
        etxt_tmid.setFocusable( false );
        etxt_tmleader.requestFocus();
        etxt_tmdate.setFocusable( false );
        etxt_tmdate.setText(DateTime());

        teamAutoId();
        dateChange();
    }

    public void goToTeamListActivity(){
        Intent intent = new Intent(getApplicationContext(), TeamListActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void teamAutoId() {

        teamController.open();
        final Cursor cus = teamController.teamAutoId();
        final int rows = cus.getCount();
        String teamId = "tm_";
        int idNo = 1;

        if (rows == 0){
            etxt_tmid.setText( teamId + idNo );
        }else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            etxt_tmid.setText(teamId + rid);
        }

    }

    private String DateTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    private void dateChange(){
        etxt_tmdate.setOnClickListener(new View.OnClickListener() {
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
            case R.id.toolbar_save_add:
                Toast.makeText(getApplicationContext(), "저장 버튼 클릭", Toast.LENGTH_SHORT).show();

                if (etxt_tmleader.length() == 0 || etxt_tmmobile.length() == 0){
                    Toast.makeText(this,
                            "팀 또는 회사이름, 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    String tid = etxt_tmid.getText().toString();
                    String leader = etxt_tmleader.getText().toString();
                    String mobile = etxt_tmmobile.getText().toString();
                    String date = etxt_tmdate.getText().toString();
                    String memo = etxt_tmmemo.getText().toString();

                    teamController.open();
                    teamController.insertTeam(tid,leader,mobile,date,memo);

                    Toast.makeText( getApplicationContext(),
                            "새로운 데이터를 저장 했습니다.", Toast.LENGTH_SHORT ).show();

                    teamController.close();

                    Intent intent = new Intent(getApplicationContext(), TeamListActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;

            case R.id.toolbar_close_add:
                Toast.makeText(getApplicationContext(),
                        "팀 추가를 닫습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TeamListActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }

}