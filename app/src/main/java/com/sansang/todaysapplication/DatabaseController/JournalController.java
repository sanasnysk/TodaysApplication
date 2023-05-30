package com.sansang.todaysapplication.DatabaseController;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sansang.todaysapplication.Contents.IncomeContents;
import com.sansang.todaysapplication.Contents.JournalContents;
import com.sansang.todaysapplication.Database.CostTableContents;
import com.sansang.todaysapplication.Database.IncomeTableContents;
import com.sansang.todaysapplication.Database.JournalTableContents;
import com.sansang.todaysapplication.Database.SiteTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JournalController {
    private Context context;
    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;

    public JournalController( Context context ) {
        this.context = context;
    }


    public JournalController open() throws SQLException {
        todayDatabase = new TodayDatabase( context );
        sqLiteDatabase = todayDatabase.getWritableDatabase();
        return this;
    }

    public void close() {
        todayDatabase.close();
    }

    //--- Insert Data Journal
    public void insertJournal(String jnId, String date, String site, String day,
                              String pay, String amount, String memo, String stId, String tmLeader, String tmId) {
        ContentValues journalValues = new ContentValues();
        journalValues.put( JournalTableContents.JOURNAL_JNID, jnId );
        journalValues.put( JournalTableContents.JOURNAL_DATE, date );
        journalValues.put( JournalTableContents.SITE_NAME, site );
        journalValues.put( JournalTableContents.JOURNAL_ONE, day );
        journalValues.put( JournalTableContents.SITE_PAY, pay );
        journalValues.put( JournalTableContents.JOURNAL_AMOUNT, amount );
        journalValues.put( JournalTableContents.JOURNAL_MEMO, memo );
        journalValues.put( JournalTableContents.SITE_STID, stId );
        journalValues.put(JournalTableContents.TEAM_LEADER,tmLeader);
        journalValues.put(JournalTableContents.TEAM_TMID, tmId);

        sqLiteDatabase.insert( JournalTableContents.JOURNAL_TABLE, null, journalValues );

        sqLiteDatabase.close();
    }

    //--- Update Data Journal
    public void updateJournalData(String id, String jnId, String date, String site, String day,
                                  String pay, String amount, String memo, String stId, String tmLeader, String tmId) {
        ContentValues siteValues = new ContentValues();
        siteValues.put( JournalTableContents.JOURNAL_ID, id );
        siteValues.put( JournalTableContents.JOURNAL_JNID, jnId );
        siteValues.put( JournalTableContents.JOURNAL_DATE, date );
        siteValues.put( JournalTableContents.SITE_NAME, site );
        siteValues.put( JournalTableContents.JOURNAL_ONE, day );
        siteValues.put( JournalTableContents.SITE_PAY, pay );
        siteValues.put( JournalTableContents.JOURNAL_AMOUNT, amount );
        siteValues.put( JournalTableContents.JOURNAL_MEMO, memo );
        siteValues.put( JournalTableContents.SITE_STID, stId );
        siteValues.put( JournalTableContents.TEAM_LEADER, tmLeader );
        siteValues.put( JournalTableContents.TEAM_TMID, tmId );

        sqLiteDatabase.update( JournalTableContents.JOURNAL_TABLE, siteValues,
                "ID = ?", new String[]{id} );

        sqLiteDatabase.close();
    }

    //--- Delete Data Journal
    public void deleteJournalData(String id) {
        sqLiteDatabase.delete( JournalTableContents.JOURNAL_TABLE,
                JournalTableContents.JOURNAL_ID + " = ?", new String[]{id} );

        sqLiteDatabase.close();
    }

    //--- AutoId Journal
    public Cursor journalAutoId() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String autoId = "select max(" +
                JournalTableContents.JOURNAL_ID + ") from " +
                JournalTableContents.JOURNAL_TABLE +
                " LIMIT 1";
        Cursor csListId = sqLiteDatabase.rawQuery( autoId, null );
        csListId.moveToFirst();

        return csListId;
    }

    public Cursor recyclerViewData(){
        String recyclerViewQuery = "SELECT j." + JournalTableContents.SITE_NAME +
                " AS sites, TOTAL(j." + JournalTableContents.JOURNAL_ONE +
                ") AS ones, SUM(j." + JournalTableContents.JOURNAL_AMOUNT +
                ") AS amounts, j." + JournalTableContents.TEAM_LEADER +
                " AS leader, cs.cAmount AS costs FROM " + JournalTableContents.JOURNAL_TABLE +
                " AS j LEFT JOIN (SELECT c." + CostTableContents.SITE_STID +
                " AS sid, SUM(" + CostTableContents.COST_AMOUNT +
                ") AS cAmount FROM " + CostTableContents.COST_TABLE +
                " AS c LEFT JOIN " + SiteTableContents.SITE_TABLE +
                " AS s ON c." + CostTableContents.SITE_STID +
                " = s." + SiteTableContents.SITE_STID +
                " GROUP BY c." + CostTableContents.SITE_STID +
                ") AS cs ON j." + JournalTableContents.SITE_STID +
                " = cs.sid GROUP BY j." + JournalTableContents.SITE_STID +
                " ORDER BY j." + CostTableContents.COST_ID +
                " DESC LIMIT 20";

        Cursor cursor_recycler = sqLiteDatabase.rawQuery(recyclerViewQuery, null);
        if (cursor_recycler != null){
            int r = cursor_recycler.getCount();
            for (int i = 0; i < r; i++){
                cursor_recycler.moveToNext();
            }
        }else {
            cursor_recycler.close();
            return null;
        }

        return cursor_recycler;
    }

    //--- Search Date Journal
    public Cursor searchDateJournal(String start, String end) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT * FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- Search Site Journal
    public Cursor searchSiteJournal(String search, String start, String end) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String selectQuery = " SELECT " +
                JournalTableContents.JOURNAL_ID + "," +
                JournalTableContents.JOURNAL_JNID + "," +
                JournalTableContents.JOURNAL_DATE + ", " +
                JournalTableContents.SITE_NAME + "," +
                JournalTableContents.JOURNAL_ONE + "," +
                JournalTableContents.SITE_PAY + ", " +
                JournalTableContents.JOURNAL_AMOUNT + "," +
                JournalTableContents.JOURNAL_MEMO + "," +
                JournalTableContents.SITE_STID + "," +
                JournalTableContents.TEAM_LEADER + "," +
                JournalTableContents.TEAM_TMID +
                " FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.SITE_NAME +
                "  LIKE  '%" + search + "%' AND " + JournalTableContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- Site and startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumSearchSiteDateJournal(String start, String end, String search) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + JournalTableContents.JOURNAL_ONE + "), " +
                "sum(" + JournalTableContents.JOURNAL_AMOUNT + ") " +
                "FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.SITE_NAME + "  LIKE  '%" + search +
                "%' AND " + JournalTableContents.JOURNAL_DATE +
                "  BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumDateJournal(String start, String end) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String sumDateQuery = "SELECT sum(" + JournalTableContents.JOURNAL_ONE + "), " +
                "sum(" + JournalTableContents.JOURNAL_AMOUNT + ") FROM " + JournalTableContents.JOURNAL_TABLE + " " +
                "WHERE " + JournalTableContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND date('" + end + "')";

        Cursor cusSearch = sqLiteDatabase.rawQuery( sumDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- Select All Data Journal
    public Cursor selectAllJournal() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + JournalTableContents.JOURNAL_TABLE +
                " ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToFirst();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;

    }

    //----------------------------------- end -------------------------------------------------- **



    //--- Search Site Journal
    public Cursor searchJournal(String search) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String selectQuery = " SELECT " +
                JournalTableContents.JOURNAL_ID + "," +
                JournalTableContents.JOURNAL_JNID + "," +
                JournalTableContents.JOURNAL_DATE + ", " +
                JournalTableContents.SITE_NAME + "," +
                JournalTableContents.JOURNAL_ONE + "," +
                JournalTableContents.SITE_PAY + ", " +
                JournalTableContents.JOURNAL_AMOUNT + "," +
                JournalTableContents.JOURNAL_MEMO + "," +
                JournalTableContents.SITE_STID + "," +
                JournalTableContents.TEAM_LEADER + "," +
                JournalTableContents.TEAM_TMID +
                " FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.SITE_NAME +
                "  LIKE  '%" + search + "%'" +
                "ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- Team sum(oneDay) sum(amount)
    public Cursor sumSearchJournal(String search) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + JournalTableContents.JOURNAL_ONE + "), " +
                "sum(" + JournalTableContents.JOURNAL_AMOUNT + ") " +
                "FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.SITE_NAME + "  LIKE  '%" + search +
                "%' ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }



    //--- Search Team Journal
    public Cursor searchTeamJournal(String search, String start, String end) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String selectQuery = " SELECT * FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%' AND " + JournalTableContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }



    //--- Site Spinner Item Journal add & update Result oneDay & dailyPay
    public List<String> getAllSpinnerSite() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        List<String> listItem = new ArrayList<String>();
        //-- Select All Query
        String spinnerQuery = "SELECT * FROM site_table  ORDER BY ID DESC";

        Cursor curSpinner = sqLiteDatabase.rawQuery( spinnerQuery, null );

        //-- looping through all rows and adding to list
        if (curSpinner.moveToFirst()) {
            do {
                listItem.add(curSpinner.getString(2));
            } while (curSpinner.moveToNext());
        }

        //-- closing connection
        curSpinner.close();
        sqLiteDatabase.close();

        //-- returning Items
        return listItem;
    }

    //--- Team and startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumTeamSpinnerDateJournal(String start, String end, String search) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + JournalTableContents.JOURNAL_ONE + "), " +
                "sum(" + JournalTableContents.JOURNAL_AMOUNT + ") " +
                "FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' AND " + JournalTableContents.JOURNAL_DATE +
                "  BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- Team sum(oneDay) sum(amount)
    public Cursor sumTeamSpinnerJournal(String search) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + JournalTableContents.JOURNAL_ONE + "), " +
                "sum(" + JournalTableContents.JOURNAL_AMOUNT + ") " +
                "FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //--- Total sum Journal
    public Cursor sumTotalJournal(){
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + JournalTableContents.JOURNAL_ONE + "), " +
                "sum(" + JournalTableContents.JOURNAL_AMOUNT + ") " +
                "FROM " + JournalTableContents.JOURNAL_TABLE +
                " ORDER BY " + JournalTableContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( selectDateQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    //----Journal add and update Site Result oneday and dailypay
    public Cursor siteSpinnerResult( String spinSite) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String siteItemResultQuery = "SELECT " + SiteTableContents.TEAM_LEADER +
                ", " + SiteTableContents.SITE_PAY +
                ", " + SiteTableContents.SITE_STID +
                ", " + SiteTableContents.TEAM_TMID +
                " FROM " + SiteTableContents.SITE_TABLE +
                " where " + SiteTableContents.SITE_NAME + " LIKE '%" + spinSite + "%'";

        Cursor cusAllSite = sqLiteDatabase.rawQuery( siteItemResultQuery, null );
        if (cusAllSite.moveToFirst()) {
            do {
                cusAllSite.getString( 0 );
                cusAllSite.getString( 1 );
            } while (cusAllSite.moveToNext());
        }
        cusAllSite.moveToFirst();
        sqLiteDatabase.close();
        return cusAllSite;
    }

    @SuppressLint("Recycle")
    public ArrayList<JournalContents> getAllJournalList(){
        // 읽기가 가능하게 DB 열기
        String id = null;
        String jnId = null;
        String date = null;
        String stName = null;
        String one = null;
        String pay = null;
        String amount = null;
        String memo = null;
        String stId = null;
        String tmLeader = null;
        String tmId = null;

        String[] allColumns = new String[] {
                JournalTableContents.JOURNAL_ID,
                JournalTableContents.JOURNAL_JNID,
                JournalTableContents.SITE_NAME,
                JournalTableContents.JOURNAL_DATE,
                JournalTableContents.JOURNAL_ONE,
                JournalTableContents.SITE_PAY,
                JournalTableContents.JOURNAL_AMOUNT,
                JournalTableContents.JOURNAL_MEMO,
                JournalTableContents.SITE_STID,
                JournalTableContents.TEAM_LEADER,
                JournalTableContents.TEAM_TMID};

        String table = JournalTableContents.JOURNAL_TABLE; //or "journal_table"
        String[] columns = new String[] { JournalTableContents.SITE_NAME,
                "total(" + JournalTableContents.JOURNAL_ONE + ") as day",
                "sum(" + JournalTableContents.JOURNAL_AMOUNT +") as amount"};
        //new String[] { "name", "total(day) as day", "sum(amount) as amount", "memo" }
        String selection = null;//"name=?"
        String[] arguments = null;//new String[] { "0"}
        String groupBy = "siteName";
        String having = null;
        String orderBy = null;

        ArrayList<JournalContents> list = new ArrayList<>();

//        String listQuery = "select * from " + DatabaseHelper.TABLE_SITE;
//        Cursor cursor = sql.rawQuery(listQuery, null);
        Cursor cursor = sqLiteDatabase.query(table, columns,
                null,
                null,
                groupBy,
                null,
                null);
        if (cursor.moveToFirst()){
            do{
                //id = cursor.getString(0);
                stName = cursor.getString(0);
                one = cursor.getString(1);
                amount = cursor.getString(2);
                //memo = cursor.getString(4);

                JournalContents results = new JournalContents(id, jnId, date, stName, one, pay, amount, memo, stId, tmLeader,tmId);
                list.add(results);
            }while (cursor.moveToNext());

        }
        return list;
    }

    @SuppressLint("Recycle")
    public ArrayList<JournalContents> getJournalRecyclerItemList(){
        // 읽기가 가능하게 DB 열기
        String id = null;
        String jnId = null;
        String date = null;
        String stName = null;
        String one = null;
        String pay = null;
        String amount = null;
        String memo = null;
        String stId = null;
        String tmLeader = null;
        String tmId = null;

        String jQuery = "SELECT " + JournalTableContents.SITE_NAME +
                ",total(" + JournalTableContents.JOURNAL_ONE +
                ") as day, sum(" + JournalTableContents.JOURNAL_AMOUNT +
                ") as amount FROM " +
                JournalTableContents.JOURNAL_TABLE +
                " GROUP BY " + JournalTableContents.SITE_NAME;
        ArrayList<JournalContents> rList = new ArrayList<>();
        Cursor cursor_re = sqLiteDatabase.rawQuery(jQuery,null);
        if (cursor_re.moveToFirst()){
            do {
                stName = String.valueOf(cursor_re.getColumnIndex(cursor_re.getString(0)));
                one = String.valueOf(cursor_re.getColumnIndex(cursor_re.getString(1)));
                amount = String.valueOf(cursor_re.getColumnIndex(cursor_re.getString(2)));

                JournalContents result_journal = new JournalContents(id, jnId, date, stName, one, pay, amount, memo, stId, tmLeader,tmId);

                rList.add(result_journal);
            }while (cursor_re.moveToNext());
        }
        return rList;
    }

    @SuppressLint("Recycle")
    public ArrayList<IncomeContents> getIncomeRecyclerItemList(){
        //Contacts Incomes Columns Name
        String id = null;
        String icId = null;
        String icDate = null;
        String stName = null;
        String collect = null;
        String tax = null;
        String memo = null;
        String stId = null;
        String tmId = null;

        String iQuery = "SELECT " + IncomeTableContents.TEAM_LEADER +
                ", sum(" + IncomeTableContents.INCOME_DEPOSIT +
                ") as collect, sum(" + IncomeTableContents.INCOME_TAX +
                ") as tax FROM " + IncomeTableContents.INCOME_TABLE +
                " GROUP BY " + IncomeTableContents.TEAM_LEADER;
        ArrayList<IncomeContents> iList = new ArrayList<>();
        Cursor cursor_re = sqLiteDatabase.rawQuery(iQuery,null);
        if (cursor_re.moveToFirst()){
            do {
                stName = String.valueOf(cursor_re.getColumnIndex(cursor_re.getString(0)));
                collect = String.valueOf(cursor_re.getColumnIndex(cursor_re.getString(1)));
                tax = String.valueOf(cursor_re.getColumnIndex(cursor_re.getString(2)));

                IncomeContents result_income = new IncomeContents(id,icId,icDate,stName,collect,tax,memo,stId,tmId);

                iList.add(result_income);
            }while (cursor_re.moveToNext());
        }
        return iList;
    }
}
