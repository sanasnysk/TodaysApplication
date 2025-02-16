package com.sansang.todaysapplication.Teams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.sansang.todaysapplication.Database.TodayDatabase;
import com.sansang.todaysapplication.DatabaseController.TeamController;
import com.sansang.todaysapplication.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TeamEditActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat;
    private EditText etxt_id,etxt_tmid,etxt_tmleader,etxt_tmmobile,etxt_tmdate,etxt_tmmemo;
    private TodayDatabase todayDatabase;
    private TeamController teamController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_edit);

        todayDatabase = new TodayDatabase( this );
        teamController = new TeamController( this );

        initView();
    }

    @SuppressLint("SimpleDateFormat")
    private void initView() {
        Toolbar editToolbar = findViewById(R.id.customToolbar);
        editToolbar.setTitle("팀 수정하기");
        setSupportActionBar(editToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTeamListActivity();
            }
        });

        mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        etxt_id = findViewById(R.id.tid_edit_etxt);
        etxt_tmid = findViewById(R.id.tmId_edit_etxt);
        etxt_tmleader = findViewById(R.id.tmLeader_edit_etxt);
        etxt_tmmobile = findViewById(R.id.tmMobile_edit_etxt);
        etxt_tmdate = findViewById(R.id.tmDate_edit_etxt);
        etxt_tmmemo = findViewById(R.id.tmMemo_edit_etxt);

        //Focus
        etxt_tmid.setFocusable( true );
        etxt_tmleader.requestFocus();
        etxt_tmdate.setFocusable( false );
        etxt_tmdate.setText(DateTime());

        dateChange();

        //Result Intent Data
        Intent positionIntent = getIntent();

        String id = positionIntent.getExtras().getString( "id" ); //String 형
        String tid = positionIntent.getExtras().getString( "tid" ); //String 형
        String tleader = positionIntent.getExtras().getString( "leader" ); //String 형
        String tmobile = positionIntent.getExtras().getString( "mobile" ); //String 형
        String tDate = positionIntent.getExtras().getString( "date" ); //String 형
        String tmemo = positionIntent.getExtras().getString( "memo" ); //String 형

        etxt_id.setText( String.valueOf( id ) );
        etxt_tmid.setText( tid );
        etxt_tmleader.setText( tleader );
        etxt_tmmobile.setText( tmobile );
        etxt_tmdate.setText( tDate );
        etxt_tmmemo.setText( tmemo );

    }

    public void goToTeamListActivity(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_edit_update:
                String id = etxt_id.getText().toString();
                String tid = etxt_tmid.getText().toString();
                String tleader = etxt_tmleader.getText().toString();
                String tmobile = etxt_tmmobile.getText().toString();
                String tdate = etxt_tmdate.getText().toString();
                String tmemo = etxt_tmmemo.getText().toString();
                
                if (etxt_id.length() > 0){
                    teamController.open();
                    teamController.updateTeam(id,tid,tleader,tmobile,tdate,tmemo);

                    teamController.close();

                    Toast.makeText(getApplicationContext(),
                            "팀 정보를 수정 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent_update = new Intent( getApplicationContext(), TeamListActivity.class );
                    startActivity( intent_update );
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "팀 정보를 수정하지 못 했습니다.", Toast.LENGTH_SHORT).show();

                    finish();
                }
                return true;

            case R.id.toolbar_delete_update:
                String deleteid = etxt_id.getText().toString();

                if (etxt_id.length() > 0){
                    teamController.open();
                    teamController.deleteTeam(deleteid);

                    teamController.close();

                    Toast.makeText(getApplicationContext(),
                            "팀 정보를 삭제 했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent_update = new Intent( getApplicationContext(), TeamListActivity.class );
                    startActivity( intent_update );
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "팀 정보를 삭제하지 못 했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;

            case R.id.toolbar_close_update:
                Toast.makeText(getApplicationContext(),
                        "팀 수정하기를 닫습니다.", Toast.LENGTH_SHORT).show();
                Intent intent_update = new Intent( getApplicationContext(), TeamListActivity.class );
                startActivity( intent_update );
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}