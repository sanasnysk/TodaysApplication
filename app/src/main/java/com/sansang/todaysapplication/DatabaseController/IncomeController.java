package com.sansang.todaysapplication.DatabaseController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sansang.todaysapplication.Database.IncomeTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;

import java.sql.SQLException;

public class IncomeController {
    private TodayDatabase todayDatabase;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public IncomeController(Context con) {
        context = con;
    }

    public IncomeController open() throws SQLException {
        todayDatabase = new TodayDatabase( context );
        sqLiteDB = todayDatabase.getWritableDatabase();
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

        sqLiteDB.insert( IncomeTableContents.INCOME_TABLE, null, incomValues );

        sqLiteDB.close();
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

        sqLiteDB.update( IncomeTableContents.INCOME_TABLE, incomValues,
                "ID = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public void deleteIncome(String id) {
        sqLiteDB.delete( IncomeTableContents.INCOME_TABLE,
                IncomeTableContents.INCOME_ID + " = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public Cursor incomeAutoId() {
        //sqLiteDB = todayDatabase.getReadableDatabase();
        String autoid = "select " +
                IncomeTableContents.INCOME_ID + " from " +
                IncomeTableContents.INCOME_TABLE +
                " ORDER BY id DESC LIMIT 1";
        Cursor csListId = sqLiteDB.rawQuery( autoid, null );
        csListId.moveToFirst();

        return csListId;
    }

    public Cursor incomeAllSelect() {
        //sqLiteDB = todayDatabase.getReadableDatabase();
        String selectQuery = " SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
                " ORDER BY id DESC ";
        Cursor cusSearch = sqLiteDB.rawQuery( selectQuery, null );
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

        Cursor cusSearch = sqLiteDB.rawQuery( selectQuery, null );
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

        String selectDateQuery = "SELECT sum(" + IncomeTableContents.INCOME_DEPOSIT + ")," +
                " sum(" + IncomeTableContents.INCOME_TAX + ") " +
                "FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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

    public Cursor incomeSiteSelect(String site, String start, String end) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.TEAM_LEADER +
                "  LIKE  '%" + site + "%' AND " +
                IncomeTableContents.INCOME_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery( selectQuery, null );
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

    public Cursor incomeDateSelect(String start, String end) {
        //sqLiteDB = todayDatabase.getReadableDatabase();

        String selectDateQuery = "SELECT * FROM " + IncomeTableContents.INCOME_TABLE +
                " WHERE " + IncomeTableContents.INCOME_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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
