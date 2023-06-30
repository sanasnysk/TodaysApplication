package com.sansang.todaysapplication.DatabaseController;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansang.todaysapplication.Contents.SiteContents;
import com.sansang.todaysapplication.Database.SiteTableContents;
import com.sansang.todaysapplication.Database.TeamTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;

import java.util.ArrayList;
import java.util.List;

public class SiteController {
    private final Context context;
    private TodayDatabase todayDatabase;
    private SQLiteDatabase sqLiteDatabase;

    public SiteController(Context con) {
        context = con;
    }

    public SiteController open() throws SQLException{
        todayDatabase = new TodayDatabase(context);
        sqLiteDatabase = todayDatabase.getWritableDatabase();
        return this;
    }

    public void close(){
        todayDatabase.close();
    }

    public Cursor siteAutoId() {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String autoId = " select " +
                SiteTableContents.SITE_ID + " from " +
                SiteTableContents.SITE_TABLE +
                " ORDER BY id DESC LIMIT 1";
        //Cursor csListId = sqLiteDB.rawQuery( autoId, null );
        Cursor csListId = sqLiteDatabase.rawQuery( autoId,null );
        csListId.moveToFirst();

        return csListId;
    }

    //--- Site Table start ---
    public void insertSite(String stid, String name, String pay, String manager, String date, String memo, String leader, String tmid) {
        ContentValues siteValues = new ContentValues();
        siteValues.put( SiteTableContents.SITE_STID, stid );
        siteValues.put( SiteTableContents.SITE_NAME, name );
        siteValues.put( SiteTableContents.SITE_PAY, pay );
        siteValues.put( SiteTableContents.SITE_MANAGER, manager );
        siteValues.put( SiteTableContents.SITE_DATE, date );
        siteValues.put( SiteTableContents.SITE_MEMO, memo );
        siteValues.put( SiteTableContents.TEAM_LEADER, leader );
        siteValues.put(SiteTableContents.TEAM_TMID, tmid);

        sqLiteDatabase.insert( SiteTableContents.SITE_TABLE, null, siteValues );

        sqLiteDatabase.close();
    }

    public void updateSite(String id, String stid, String name, String pay, String manager, String date, String memo, String leader, String tmid) {
        ContentValues siteValues = new ContentValues();
        siteValues.put( SiteTableContents.SITE_ID, id );
        siteValues.put( SiteTableContents.SITE_STID, stid );
        siteValues.put( SiteTableContents.SITE_NAME, name );
        siteValues.put( SiteTableContents.SITE_PAY, pay );
        siteValues.put( SiteTableContents.SITE_MANAGER, manager );
        siteValues.put( SiteTableContents.SITE_DATE, date );
        siteValues.put( SiteTableContents.SITE_MEMO, memo );
        siteValues.put( SiteTableContents.TEAM_LEADER, leader );
        siteValues.put(SiteTableContents.TEAM_TMID, tmid);

        sqLiteDatabase.update( SiteTableContents.SITE_TABLE, siteValues, "ID = ?", new String[]{id} );

        sqLiteDatabase.close();
    }

    public void deleteSite(String id) {
        sqLiteDatabase.delete( SiteTableContents.SITE_TABLE,
                SiteTableContents.SITE_ID + " = ?", new String[]{id} );

        sqLiteDatabase.close();
    }

    public Cursor siteSearch(String search) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        String selectQuery = "SELECT  " +
                SiteTableContents.SITE_ID + "," +
                SiteTableContents.SITE_STID + "," +
                SiteTableContents.SITE_NAME + "," +
                SiteTableContents.SITE_PAY + "," +
                SiteTableContents.SITE_MANAGER + "," +
                SiteTableContents.SITE_DATE + "," +
                SiteTableContents.SITE_MEMO + ", " +
                SiteTableContents.TEAM_LEADER + "," +
                SiteTableContents.TEAM_TMID +
                " FROM " + SiteTableContents.SITE_TABLE +
                " WHERE " + SiteTableContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%' order by id desc ";

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

    //Recycler View List
    @SuppressLint("Recycle")
    public ArrayList<SiteContents> getAllSiteList(){
        String id;
        String siteId;
        String siteName;
        String pay;
        String manager;
        String date;
        String memo;
        String leader;
        String tmid;

        String[] allColumns = new String[] {
                SiteTableContents.SITE_ID,
                SiteTableContents.SITE_STID,
                SiteTableContents.SITE_NAME,
                SiteTableContents.SITE_PAY,
                SiteTableContents.SITE_MANAGER,
                SiteTableContents.SITE_DATE,
                SiteTableContents.SITE_MEMO,
                SiteTableContents.TEAM_LEADER,
                SiteTableContents.TEAM_TMID};

        ArrayList<SiteContents> list = new ArrayList<>();

//        String listQuery = "select * from " + DatabaseHelper.TABLE_SITE;
//        Cursor cursor = sql.rawQuery(listQuery, null);
        Cursor cursor = sqLiteDatabase.query(SiteTableContents.SITE_TABLE, allColumns,
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()){
            do{
                id = cursor.getString(0);
                siteId = cursor.getString(1);
                siteName = cursor.getString(2);
                pay = cursor.getString(3);
                manager = cursor.getString(4);
                date = cursor.getString(5);
                memo = cursor.getString(6);
                leader = cursor.getString(7);
                tmid = cursor.getString(8);


                SiteContents results = new SiteContents(id, siteId, siteName, pay, manager, date, memo, leader, tmid);
                list.add(results);
            }while (cursor.moveToNext());

        }
        return list;
    }

    //Spinner Team List
    public List<String> getTeamSpinner() {
        List<String> listItem = new ArrayList<String>();
        // Select All Query
        String teamQuery = " SELECT " + TeamTableContents.TEAM_LEADER +
                ", " + TeamTableContents.TEAM_ID +
                " FROM " + TeamTableContents.TEAM_TABLE +
                " ORDER BY ID DESC";
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        Cursor curSpinner = sqLiteDatabase.rawQuery( teamQuery, null );
        // looping through all rows and adding to list
        if (curSpinner.moveToFirst()) {
            do {
                listItem.add(curSpinner.getString(0));

            } while (curSpinner.moveToNext());
        }
        // closing connection
        curSpinner.close();
        sqLiteDatabase.close();
        // returning ables
        return listItem;
    }

    public Cursor resultTeamSpinner(String team) {
        sqLiteDatabase = todayDatabase.getReadableDatabase();
        // Select All Query
        String teamQuery = " SELECT " + TeamTableContents.TEAM_TMID +
                ", " + TeamTableContents.TEAM_LEADER +
                " FROM " + TeamTableContents.TEAM_TABLE +
                " WHERE " + TeamTableContents.TEAM_LEADER +
                " LIKE '%" + team + "%'";

        Cursor curSpinner = sqLiteDatabase.rawQuery( teamQuery, null );
        // looping through all rows and adding to list
        if (curSpinner != null) {
            int r = curSpinner.getCount();
            for (int i = 0; i < r; i++) {
                curSpinner.moveToNext();
            }
        } else {
            curSpinner.close();
            return null;
        }
        return curSpinner;
    }
}
