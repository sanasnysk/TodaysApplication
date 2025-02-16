package com.sansang.todaysapplication.DatabaseController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sansang.todaysapplication.Database.CostTableContents;
import com.sansang.todaysapplication.Database.JournalTableContents;
import com.sansang.todaysapplication.Database.SiteTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CostController {
    private TodayDatabase todayDatabase;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    public CostController(Context con) {
        this.context = con;
    }

    public CostController open() throws SQLException {
        try {
            todayDatabase = new TodayDatabase(context);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        sqLiteDatabase = todayDatabase.getWritableDatabase();
        return this;
    }

    public void close() {
        todayDatabase.close();
    }

    //--- Insert Data Cost
    public void insertCost(String csid, String date, String site, String detail, String price, String amount, String memo, String stid) {
        ContentValues costValues = new ContentValues();
        costValues.put(CostTableContents.COST_CSID, csid);
        costValues.put(CostTableContents.COST_DATE, date);
        costValues.put(CostTableContents.SITE_NAME, site);
        costValues.put(CostTableContents.COST_DETAIL, detail);
        costValues.put(CostTableContents.COST_PRICE, price);
        costValues.put(CostTableContents.COST_AMOUNT, amount);
        costValues.put(CostTableContents.COST_MEMO, memo);
        costValues.put(CostTableContents.SITE_STID, stid);

        sqLiteDatabase.insert(CostTableContents.COST_TABLE, null, costValues);

        sqLiteDatabase.close();
    }

    //--- Update Data Cost
    public void updateCost(String id, String csid, String date, String site, String detail, String price, String amount, String memo, String stid) {
        ContentValues costValues = new ContentValues();
        costValues.put(CostTableContents.COST_ID, id);
        costValues.put(CostTableContents.COST_CSID, csid);
        costValues.put(CostTableContents.COST_DATE, date);
        costValues.put(CostTableContents.SITE_NAME, site);
        costValues.put(CostTableContents.COST_DETAIL, detail);
        costValues.put(CostTableContents.COST_PRICE, price);
        costValues.put(CostTableContents.COST_AMOUNT, amount);
        costValues.put(CostTableContents.COST_MEMO, memo);
        costValues.put(CostTableContents.SITE_STID, stid);

        sqLiteDatabase.update(CostTableContents.COST_TABLE, costValues, "ID = ?", new String[]{id});

        sqLiteDatabase.close();
    }

    //--- Delete Data Cost
    public void deleteCost(String id) {
        sqLiteDatabase.delete(CostTableContents.COST_TABLE, CostTableContents.COST_ID + " = ?", new String[]{id});

        sqLiteDatabase.close();
    }

    //--- AutoId Cost
    public Cursor costAutoId() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String autoId = "select max(" +
                CostTableContents.COST_ID + ") from " +
                CostTableContents.COST_TABLE +
                " LIMIT 1";
        Cursor csListId = sqLiteDatabase.rawQuery(autoId, null);
        csListId.moveToFirst();

        return csListId;
    }

    //--- Select site and date Cost
    public Cursor searchSiteDate(String site, String stdate, String endate) {

        String selectQuery = " SELECT * FROM " + CostTableContents.COST_TABLE +
                " WHERE " + CostTableContents.SITE_NAME +
                " LIKE '%" + site + "%' AND " + CostTableContents.COST_DATE +
                " BETWEEN date('" + stdate + "') AND date('" + endate + "')" +
                " ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery(selectQuery, null);
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

    //--- Select site and date Cost sum
    public Cursor sumSearchSiteDate(String site, String stdate, String endate) {

        String selectQuery = " SELECT sum(" + CostTableContents.COST_AMOUNT +
                ")as costAmount FROM " + CostTableContents.COST_TABLE +
                " WHERE " + CostTableContents.SITE_NAME +
                " LIKE '%" + site + "%' AND " + CostTableContents.COST_DATE +
                " BETWEEN date('" + stdate + "') AND date('" + endate + "')";

        Cursor cusSearch = sqLiteDatabase.rawQuery(selectQuery, null);
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

    //--- Select date Data Cost
    public Cursor selectDateCost(String std, String endt) {

        String selectQuery = " SELECT * FROM " + CostTableContents.COST_TABLE +
                " WHERE " + CostTableContents.COST_DATE +
                " BETWEEN date('" + std + "') AND date('" + endt + "')" +
                " ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDatabase.rawQuery(selectQuery, null);
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

    //--- Select date Data Cost sum
    public Cursor sumSelectDateCost(String std, String endt) {

        String selectQuery = " SELECT sum(" + CostTableContents.COST_AMOUNT +
                ") as costAmount FROM " + CostTableContents.COST_TABLE +
                " WHERE " + CostTableContents.COST_DATE +
                " BETWEEN date('" + std + "') AND date('" + endt + "')";

        Cursor cusSearch = sqLiteDatabase.rawQuery(selectQuery, null);
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

    //--- Site Spinner Item Journal add & update Result oneDay & dailyPay
    public List<String> getAllSpinnerSite() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        List<String> listItem = new ArrayList<String>();
        //-- Select All Query
        String spinnerQuery = "SELECT * FROM site_table  ORDER BY id DESC";

        Cursor curSpinner = sqLiteDatabase.rawQuery(spinnerQuery, null);

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

    // --- Site Spinner Item Journal Table date DESC
    public List<String> siteSpinnerLimit(){
        List<String> listItem = new ArrayList<String>();
        String stQuery = "SELECT siteName, siteId, sitePay, teamId, teamLeader, max(id)" +
                " FROM journal_table " +
                "GROUP BY siteId " +
                "ORDER BY id DESC LIMIT 20";

        Cursor cursorSite = sqLiteDatabase.rawQuery(stQuery, null);
        //-- looping through all rows and adding to list
        if (cursorSite.moveToFirst()){
            do {
                listItem.add(cursorSite.getString(0));
            }while (cursorSite.moveToNext());
        }
        cursorSite.close();
        sqLiteDatabase.close();

        return listItem;
    }

    //----Journal add and update Site Result oneday and dailypay
    public Cursor siteSpinnerResult(String spinSite) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String siteItemResultQuery = "SELECT " + SiteTableContents.SITE_STID +
                ", " + SiteTableContents.SITE_NAME +
                ", " + SiteTableContents.SITE_PAY +
                ", " + SiteTableContents.TEAM_LEADER +
                ", " + SiteTableContents.TEAM_TMID +
                " FROM " + SiteTableContents.SITE_TABLE +
                " where " + SiteTableContents.SITE_NAME + " LIKE '%" + spinSite + "%'";

        Cursor cusAllSite = sqLiteDatabase.rawQuery(siteItemResultQuery, null);
        if (cusAllSite.moveToFirst()) {
            do {
                cusAllSite.getString(0);
                cusAllSite.getString(1);
                cusAllSite.getString(2);
                cusAllSite.getString(3);
                cusAllSite.getString(4);
            } while (cusAllSite.moveToNext());
        }
        cusAllSite.moveToFirst();
        sqLiteDatabase.close();
        return cusAllSite;
    }

    //--- Cost RecyclweView Data
    public Cursor recyclweViewData() {
        String listRecyclerQuery = "SELECT j." + JournalTableContents.SITE_NAME +
                " as sites, TOTAL(j." + JournalTableContents.JOURNAL_ONE +
                ") as Days, sum(j." + JournalTableContents.JOURNAL_AMOUNT +
                ") as amounts,j." + JournalTableContents.TEAM_LEADER +
                " as leaders,cs.camount as cost FROM " + JournalTableContents.JOURNAL_TABLE +
                " as j LEFT JOIN (SELECT c." + CostTableContents.SITE_STID +
                " as sid,sum(" + CostTableContents.COST_AMOUNT +
                ") as camount FROM " + CostTableContents.COST_TABLE +
                " as c LEFT JOIN " + SiteTableContents.SITE_TABLE +
                " as s ON c." + CostTableContents.SITE_STID +
                " = s." + SiteTableContents.SITE_STID +
                " GROUP BY c." + CostTableContents.SITE_STID +
                ") as cs ON j." + JournalTableContents.SITE_STID +
                " = cs.sid GROUP BY j." + JournalTableContents.SITE_STID +
                " ORDER BY j." + CostTableContents.COST_ID +
                " DESC limit 10";

        Cursor curRc = sqLiteDatabase.rawQuery(listRecyclerQuery, null);
        if (curRc != null) {
            int r = curRc.getCount();
            for (int i = 0; i < r; i++) {
                curRc.moveToNext();
            }
        } else {
            curRc.close();
            return null;
        }
        return curRc;
    }

    //-------------------------------------------------------------------------------------

    /*
    //--- site list startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumSearchDateCost(String site, String start, String end) {

        String searchSumQuery = "SELECT sum(j." + CostTableContents.COST_AMOUNT +
                " as days,Total(j." + JournalTableContents.JOURNAL_ONE +
                ") as amount FROM " + JournalTableContents.JOURNAL_TABLE +
                " WHERE " + CostTableContents.SITE_NAME +
                " LIKE '%" + site + "%' AND " + CostTableContents.COST_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')";

        Cursor cusSearch = sqLiteDatabase.rawQuery(searchSumQuery, null);
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
     */


}

