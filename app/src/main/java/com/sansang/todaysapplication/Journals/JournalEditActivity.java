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

public class JournalEditActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    long mNow;
    Date mDate;
    private EditText edtxt_id, edtxt_jnid, edtxt_date, edtxt_Site, edtxt_one, edtxt_Pay, edtxt_Amount, edtxt_memo, edtxt_stid, edtxt_leader, edtxt_tmid;
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
        setContentView(R.layout.activity_journal_edit);

        todayDatabase = new TodayDatabase(this);
        journalController = new JournalController(this);

        initView();
    }

    private void initView() {
        Toolbar siteToolbar = findViewById(R.id.customToolbar);
        siteToolbar.setTitle("일지 수정하기");
        setSupportActionBar(siteToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSiteListActivity();
            }
        });

        //--- findView
        edtxt_id = findViewById(R.id.edxt_edit_journal_id);
        edtxt_jnid = findViewById(R.id.edxt_edit_journal_jnid);
        edtxt_date = findViewById(R.id.edxt_edit_journal_date);
        edtxt_Site = findViewById(R.id.edxt_edit_journal_site);
        edtxt_one = findViewById(R.id.edxt_edit_journal_one);
        edtxt_Pay = findViewById(R.id.edxt_edit_journal_pay);
        edtxt_Amount = findViewById(R.id.edxt_edit_journal_amount);
        edtxt_memo = findViewById(R.id.edxt_edit_journal_memo);
        edtxt_stid = findViewById(R.id.edxt_edit_journal_stid);
        edtxt_leader = findViewById(R.id.edxt_edit_journal_tmleader);
        edtxt_tmid = findViewById(R.id.edxt_edit_journal_tmid);

        dialog_spinner_txt = findViewById(R.id.dialog_spinner_txt);

        edtxt_Pay.setText("0");
        edtxt_Amount.setText("0");

        edtxt_stid.setFocusable(false);
        edtxt_leader.setFocusable(false);
        edtxt_tmid.setFocusable(false);
        edtxt_Site.setFocusable(false);
        edtxt_one.requestFocus();

        edtxt_date.setText(dateTime());

        //--> Comma in
        edtxt_Pay.addTextChangedListener(new NumberTextWatcher(edtxt_Pay));
        edtxt_Amount.addTextChangedListener(new NumberTextWatcher(edtxt_Amount));

        //intent Result
        intentResult();
        dateChange();
        oneAddTextChangedListener();

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

    public void goToSiteListActivity() {
        Intent intent = new Intent(getApplicationContext(), JournalListActivity.class);
        startActivity(intent);
        finish();
    }

    public void intentResult() {
        //--- Result Intent Data
        Intent positionIntent = getIntent();

        int id = positionIntent.getExtras().getInt("id"); // int 형
        String jid = positionIntent.getExtras().getString("jnid"); //String 형
        String date = positionIntent.getExtras().getString("date");
        String site = positionIntent.getExtras().getString("site");
        float oneday = positionIntent.getExtras().getFloat("oneDay");
        int pay = positionIntent.getExtras().getInt("pay");
        int amount = positionIntent.getExtras().getInt("amount");
        String memo = positionIntent.getExtras().getString("memo");
        String stid = positionIntent.getExtras().getString("stid");
        String leader = positionIntent.getExtras().getString("leader");
        String tmid = positionIntent.getExtras().getString("tmid");

        edtxt_id.setText(String.valueOf(id));
        edtxt_jnid.setText(jid);
        edtxt_date.setText(date);
        edtxt_Site.setText(site);
        edtxt_one.setText(String.valueOf(oneday));
        edtxt_Pay.setText(String.valueOf(pay));
        edtxt_Amount.setText(String.valueOf(amount));
        edtxt_memo.setText(memo);
        edtxt_stid.setText(stid);
        edtxt_leader.setText(leader);
        edtxt_tmid.setText(tmid);

        dialog_spinner_txt.setText(site);
    }

    private void oneAddTextChangedListener() {
        //----- One day add Text Changed Listener -----
        edtxt_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtxt_one == null) {
                    //Do nothing
                    float soneday = 0;
                    int pay = Integer.parseInt(edtxt_Pay.getText().toString());
                    int amount = (int) (soneday * pay);
                    edtxt_Amount.setText(String.valueOf(amount));

                } else if (edtxt_one.length() > 0) {

                    float oneday = Float.parseFloat(s.toString());

                    String sPay = edtxt_Pay.getText().toString().replace(",", "");
                    int pay = Integer.parseInt(sPay);

                    int amount = (int) (oneday * pay);
                    edtxt_Amount.setText(String.valueOf(amount));
                } else {
                    //Do nothing
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private String dateTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return mFormat.format(mDate);
    }

    private void dateChange() {
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

    //----- DatePickerDialog -----
    private void DatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //for
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        @SuppressLint("DefaultLocale")
                        String startDate = String.format("%d-%02d-%02d", year, month, dayOfMonth);

                        edtxt_date.setText(startDate);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    @SuppressLint("ResourceType")
    private void showAlertDialog() {
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
        List<String> data = journalController.siteSpinnerLimit();

        String[] item_data = data.toArray(new String[0]);
        int size = 0;
        for (String temp : data) {
            item_data[size++] = temp;
        }
        text = item_data;

        dialogItemList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put(TAG_IMAGE, image[0]);
            itemMap.put(TAG_TEXT, text[i]);

            dialogItemList.add(itemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), dialogItemList, R.layout.alert_dialog_row,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});

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
                    Toast.makeText(parent.getContext(), "You selected: " + site,
                            Toast.LENGTH_SHORT).show();

                    edtxt_Site.setText(site);

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    final Cursor cus = journalController.siteSpinnerResult(site);
                    final int rows = cus.getCount();
                    final int clums = cus.getColumnCount();

                    cus.moveToFirst();
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < clums; j++) {

                            final int p = i;
                            final int r = i + 1;

                            if (rows == 0) {
                                return;
                            } else if (cus.moveToPosition(p)) {

                                String tleader = cus.getString(0);
                                edtxt_leader.setText(tleader);
                                String spay = cus.getString(1);
                                edtxt_Pay.setText(spay);
                                String stId = cus.getString(2);
                                edtxt_stid.setText(stId);
                                String tmId = cus.getString(3);
                                edtxt_tmid.setText(tmId);

                                edtxt_one.requestFocus();

                            }
                        }

                    }

                    if (edtxt_one == null) {
                        float soneday = 0;
                        int pay = Integer.parseInt(edtxt_Pay.getText().toString().replace(",", ""));
                        float amount = soneday * pay;
                        edtxt_Amount.setText(String.valueOf(amount));

                    } else if (edtxt_one.length() > 0) {

                        float oneday = Float.parseFloat(edtxt_one.getText().toString());
                        int pay = Integer.parseInt(edtxt_Pay.getText().toString().replace(",", ""));
                        int amount = (int) (oneday * pay);
                        edtxt_Amount.setText(String.valueOf(amount));

                    } else {
                        //Do nothing
                    }
                    journalController.close();
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        //dialog.getWindow().setLayout(900,1200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_toolbar_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_edit_update:
                if (edtxt_date.length() == 0 || edtxt_Site.length() == 0) {
                    Toast.makeText(JournalEditActivity.this,
                            "날짜와 이름을 입력하세요?", Toast.LENGTH_LONG).show();
                } else if (edtxt_one.length() == 0) {
                    Toast.makeText(JournalEditActivity.this,
                            "일량을 입력하세요?", Toast.LENGTH_SHORT).show();
                } else {
                    String id = edtxt_id.getText().toString();
                    String jnid = edtxt_jnid.getText().toString();
                    String date = edtxt_date.getText().toString();
                    String site = edtxt_Site.getText().toString();
                    String day = edtxt_one.getText().toString();
                    String pay = edtxt_Pay.getText().toString().replace(",", "");
                    String amount = edtxt_Amount.getText().toString().replace(",", "");
                    String memo = edtxt_memo.getText().toString();
                    String sid = edtxt_stid.getText().toString();
                    String tleader = edtxt_leader.getText().toString();
                    String tid = edtxt_tmid.getText().toString();

                    try {
                        journalController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    journalController.updateJournalData(id, jnid, date, site, day, pay, amount, memo, sid, tleader, tid);

                    journalController.close();

                    Toast.makeText(getApplicationContext(),
                            "일지 내용을 수정 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent_update = new Intent(getApplicationContext(), JournalListActivity.class);
                    startActivity(intent_update);
                    finish();
                }
                return true;

            case R.id.toolbar_delete_update:
                String id = edtxt_id.getText().toString();
                String site = edtxt_Site.getText().toString();
                if (edtxt_Site.length() > 0) {
                    try {
                        journalController.open();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    journalController.deleteJournalData(id);

                    journalController.close();

                    Toast.makeText(JournalEditActivity.this,
                            site + "를 삭제 했습니다.", Toast.LENGTH_LONG).show();

                    Intent intent_update = new Intent(getApplicationContext(), JournalListActivity.class);
                    startActivity(intent_update);
                    finish();

                } else {
                    Toast.makeText(JournalEditActivity.this,
                            site + "가 삭제 되지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.toolbar_close_update:
                Toast.makeText(getApplicationContext(),
                        "일지 수정을 종료합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_update = new Intent(getApplicationContext(), JournalListActivity.class);
                startActivity(intent_update);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}