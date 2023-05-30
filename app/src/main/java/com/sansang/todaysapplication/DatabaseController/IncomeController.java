package com.sansang.todaysapplication.DatabaseController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sansang.todaysapplication.Database.IncomeTableContents;
import com.sansang.todaysapplication.Database.JournalTableContents;
import com.sansang.todaysapplication.Database.SiteTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncomeController {
    private TodayDatabase todayDatabase;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    public IncomeController(Context con) {
        context = con;
    }

    public IncomeController open() throws SQLException {
        todayDatabase = new TodayDatabase( context );
        sqLiteDatabase = todayDatabase.getWritableDatabase();
        return this;
    }

    public void close() {
        todayDatabase.close();
    }

    //☆☆☆☆☆☆☆☆☆☆☆☆☆☆
    //---Income Table start---
    //--------------Table Income
    public void insertIncome(String icid, String date, String leader, String deposit, String tax, String memo, String stid, String tmid) {
        ContentValues incomValues = new ContentValues();
        incomValues.put( IncomeTableContents.INCOME_ICID, icid );
        incomValues.put( IncomeTableContents.INCOME_DATE, date );
        incomValues.put( IncomeTableContents.TEAM_LEADER, leader );
        incomValues.put( IncomeTableContents.INCOME_DEPOSIT, deposit );
        incomValues.put( IncomeTableContents.INCOME_TAX, tax );
        incomValues.put( IncomeTableContents.INCOME_MEMO, memo );
        incomValues.put( IncomeTableContents.SITE_STID, stid );
        incomValues.put(IncomeTableContents.TEAM_TMID, tmid);

        sqLiteDatabase.insert( IncomeTableContents.INCOME_TABLE, null, incomValues );

        sqLiteDatabase.close();
    }

    public void updateIncome(String id, String icid, String date, String leader, String deposit, String tax, String memo, String stid, String tmid) {
        ContentValues incomValues = new ContentValues();
        incomValues.put( IncomeTableContents.INCOME_ID, id );
        incomValues.put( IncomeTableContents.INCOME_ICID, icid );
        incomValues.put( IncomeTableContents.INCOME_DATE, date );
        incomValues.put( IncomeTableContents.TEAM_LEADER, leader );
        incomValues.put( IncomeTableContents.INCOME_DEPOSIT, deposit );
        incomValues.put( IncomeTableContents.INCOME_TAX, tax );
        incomValues.put( IncomeTableContents.INCOME_MEMO, memo );
        incomValues.put( IncomeTableContents.SITE_STID, stid );
        incomValues.put(IncomeTableContents.TEAM_TMID, tmid);

        sqLiteDatabase.update( IncomeTableContents.INCOME_TABLE, incomValues,
                "ID = ?", new String[]{id} );

        sqLiteDatabase.close();
    }

    public void deleteIncome(String id) {
        sqLiteDatabase.delete( IncomeTableContents.INCOME_TABLE,
                IncomeTableContents.INCOME_ID + " = ?", new String[]{id} );

        sqLiteDatabase.close();
    }

    public Cursor incomeAutoId() {
        //sqLiteDB = todayDatabase.getReadableDatabase();
        String autoid = "select " +
                IncomeTableContents.INCOME_ID + " from " +
                IncomeTableContents.INCOME_TABLE +
                " ORDER BY id DESC LIMIT 1";
        Cursor csListId = sqLiteDatabase.rawQuery( autoid, null );
        csListId.moveToFirst();

        return csListId;
    }

    public Cursor incomeAllSelect() {
        //sqLiteDB = todayDatabase.getReadableDatabase();
        String selectQuery = " SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
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

    public Cursor incomeSearchSelect(String search) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%'" +
                " ORDER BY id DESC ";

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

    //--Income Search sum(collect) sum(tax) sum(cost) ---
    public Cursor sumSearchIncome(String search) {
        //sqLiteDB = todayDatabase.getReadableDatabase();
        String tableSearchQuery = "SELECT total(" + JournalTableContents.JOURNAL_ONE +
                ") ones, sum(" + JournalTableContents.JOURNAL_AMOUNT +
                ") amounts, i.deposits, i.taxs, (sum(" + JournalTableContents.JOURNAL_AMOUNT +
                ") - i.collects) balance, round((sum(" + JournalTableContents.JOURNAL_AMOUNT +
                ") - i.collects),1)/" + JournalTableContents.SITE_PAY +
                " oneBalace FROM " + JournalTableContents.JOURNAL_TABLE +
                " j LEFT JOIN (SELECT " + IncomeTableContents.TEAM_TMID +
                " tmid, sum(" + IncomeTableContents.INCOME_DEPOSIT +
                ") deposits, sum(" + IncomeTableContents.INCOME_TAX +
                ") taxs, sum(" + IncomeTableContents.INCOME_DEPOSIT +
                ") + sum(" + IncomeTableContents.INCOME_TAX +
                ") collects FROM " + IncomeTableContents.INCOME_TABLE +
                " GROUP BY " + IncomeTableContents.TEAM_TMID +
                ") i ON i.tmid = j." + JournalTableContents.TEAM_TMID +
                " WHERE " + JournalTableContents.TEAM_LEADER +
                " LIKE '%"+ search + "%'GROUP BY j." + JournalTableContents.TEAM_TMID + ";";

        String selectDateQuery = "SELECT sum(" + IncomeTableContents.INCOME_DEPOSIT + ")," +
                " sum(" + IncomeTableContents.INCOME_TAX + ") " +
                "FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery( tableSearchQuery, null );
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

    public Cursor selectIncomeDateLeader(String leader, String start, String end) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER +
                "  LIKE  '%" + leader + "%' AND " +
                IncomeTableContents.INCOME_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

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

    public Cursor sumDateSearchJournal(String start, String end, String search){
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String journalDatesum = "SELECT total(" + JournalTableContents.JOURNAL_ONE +
                ") as ione,sum(" + JournalTableContents.JOURNAL_AMOUNT +
                ") as iamount FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + JournalTableContents.JOURNAL_DATE +
                " BETWEEN date('" + start +"') AND date('" + end + "') AND " + JournalTableContents.TEAM_LEADER +
                " LIKE '%" + search + "%' ";
        Cursor curjd = sqLiteDatabase.rawQuery(journalDatesum,null);
        if (curjd != null) {
            int r = curjd.getCount();
            for (int i = 0; i < r; i++) {
                curjd.moveToNext();
            }
        } else {
            curjd.close();
            return null;
        }

        return curjd;
    }

    public Cursor sumDateSearchIncome(String start, String end,String search){
        sqLiteDatabase = todayDatabase.getReadableDatabase();

        String sumSearchDateQuery = "SELECT total(j." + JournalTableContents.JOURNAL_ONE +
                ") as ione,sum(j." + JournalTableContents.JOURNAL_AMOUNT +
                ") as iamount,i.collects as icollect,i.taxs as itax,sum(j." + JournalTableContents.JOURNAL_AMOUNT +
                ")-i.collects-i.taxs as balance,round((sum(j." + JournalTableContents.JOURNAL_AMOUNT +
                ")-i.collects-i.taxs)/avg(j." + JournalTableContents.SITE_PAY +
                "),1) as balance_day FROM " + JournalTableContents.JOURNAL_TABLE +
                " as j LEFT JOIN (SELECT " + IncomeTableContents.TEAM_TMID +
                " as tid,sum(" + IncomeTableContents.INCOME_DEPOSIT +
                ") as collects,sum(" + IncomeTableContents.INCOME_TAX +
                ") as taxs FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND " + IncomeTableContents.INCOME_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')) as i ON i.tid = j." + JournalTableContents.TEAM_TMID +
                " WHERE j." + JournalTableContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND j." + JournalTableContents.JOURNAL_DATE +
                " >= date('" + start + "') AND j." + JournalTableContents.JOURNAL_DATE +
                " <= date('" + end + "')";

        Cursor curid = sqLiteDatabase.rawQuery(sumSearchDateQuery,null);
        if (curid != null) {
            int r = curid.getCount();
            for (int i = 0; i < r; i++) {
                curid.moveToNext();
            }
        } else {
            curid.close();
            return null;
        }

        return curid;
    }

    //--- Site Spinner Item Journal add & update Result oneDay & dailyPay
    public List<String> getAllSpinnerSite() {

        List<String> listItem = new ArrayList<String>();
        //-- Select All Query
        String spinnerQuery = "SELECT siteName || ' 리더 : ' || teamLeader leader FROM site_table  ORDER BY ID DESC";

        Cursor curSpinner = sqLiteDatabase.rawQuery( spinnerQuery, null );

        //-- looping through all rows and adding to list
        if (curSpinner.moveToFirst()) {
            do {
                listItem.add(curSpinner.getString(0));
            } while (curSpinner.moveToNext());
        }

        //-- closing connection
        curSpinner.close();
        sqLiteDatabase.close();

        //-- returning Items
        return listItem;
    }

    //----Journal add and update Site Result oneday and dailypay
    public Cursor siteSpinnerResult( String spinSite) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String siteItemResultQuery = "SELECT " + SiteTableContents.TEAM_LEADER +
                ", " + SiteTableContents.SITE_PAY +
                ", " + SiteTableContents.SITE_STID +
                ", " + SiteTableContents.TEAM_TMID +
                " FROM " + SiteTableContents.SITE_TABLE +
                " where " + SiteTableContents.SITE_NAME +
                " || ' 리더 : ' || " + SiteTableContents.TEAM_LEADER +
                " LIKE '%" + spinSite + "%'";

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

    //--------------------------------------------------------------------------------------

    public Cursor incomeDateSelect(String start, String end) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.INCOME_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

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

    //--startday and endday sum(collect) sum(tax) sum(cost) ------------------------------------
    public Cursor sumDateIncome(String start, String end) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomeTableContents.INCOME_DEPOSIT + ")," +
                " sum(" + IncomeTableContents.INCOME_TAX + ") " +
                "FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.INCOME_DATE +
                "  BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

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

    //--Income and startday and endday sum(collect) sum(tax) sum(cost) ----------------------
    public Cursor sumSiteDateIncome(String start, String end, String search) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomeTableContents.INCOME_DEPOSIT + ")," +
                " sum(" + IncomeTableContents.INCOME_TAX + ") " +
                "FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' AND " + IncomeTableContents.INCOME_DATE +
                "  BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

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

    //--Income and startday and endday sum(collect) sum(tax) sum(cost) ----------------------
    public Cursor sumSiteIncome(String search) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomeTableContents.INCOME_DEPOSIT + ")," +
                " sum(" + IncomeTableContents.INCOME_TAX + ") " +
                "FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' ORDER BY id DESC ";

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

    //--- Income Total sum
    public Cursor sumTotalIncome(){
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomeTableContents.INCOME_DEPOSIT + ")," +
                " sum(" + IncomeTableContents.INCOME_TAX + ") " +
                "FROM " + IncomeTableContents.INCOME_TABLE +
                " ORDER BY id DESC ";

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

}
