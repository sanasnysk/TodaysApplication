package com.sansang.todaysapplication.DatabaseController;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansang.todaysapplication.Contents.TeamContents;
import com.sansang.todaysapplication.Database.TeamTableContents;
import com.sansang.todaysapplication.Database.TodayDatabase;

import java.io.IOException;
import java.util.ArrayList;

public class TeamController {
    private TodayDatabase todaysDB;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public TeamController(Context con) {
        context = con;
    }

    public TeamController open() throws SQLException {
        todaysDB = new TodayDatabase( context );
        sqLiteDB = todaysDB.getWritableDatabase();
        return this;
    }

    public void close() {
        todaysDB.close();
    }

    public Cursor teamAutoId() {
        sqLiteDB = todaysDB.getReadableDatabase();
        String autoId = "select " +
                TeamTableContents.TEAM_ID + " from " +
                TeamTableContents.TEAM_TABLE +
                " ORDER BY id DESC LIMIT 1";
        Cursor csListId = sqLiteDB.rawQuery( autoId, null );
        csListId.moveToFirst();
        //csListId.close();
        //sqLiteDB.close();
        return csListId;
    }

    //---Team Table start---
    public void insertTeam(String tmid, String leader, String mobile, String date, String memo) {
        ContentValues teamValues = new ContentValues();
        teamValues.put( TeamTableContents.TEAM_TMID, tmid );
        teamValues.put( TeamTableContents.TEAM_LEADER, leader );
        teamValues.put( TeamTableContents.TEAM_MOBILE, mobile );
        teamValues.put( TeamTableContents.TEAM_DATE, date );
        teamValues.put( TeamTableContents.TEAM_MEMO, memo );

        sqLiteDB.insert( TeamTableContents.TEAM_TABLE, null, teamValues );

        sqLiteDB.close();
    }

    public void updateTeam(String id, String tmid, String leader, String mobile, String date, String memo) {
        ContentValues teamValues = new ContentValues();
        teamValues.put( TeamTableContents.TEAM_ID, id );
        teamValues.put( TeamTableContents.TEAM_TMID, tmid );
        teamValues.put( TeamTableContents.TEAM_LEADER, leader );
        teamValues.put( TeamTableContents.TEAM_MOBILE, mobile );
        teamValues.put( TeamTableContents.TEAM_DATE, date );
        teamValues.put( TeamTableContents.TEAM_MEMO, memo );
        sqLiteDB.update( TeamTableContents.TEAM_TABLE, teamValues,
                "ID = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public void deleteTeam(String id) {
        sqLiteDB.delete( TeamTableContents.TEAM_TABLE,
                TeamTableContents.TEAM_ID + " = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public Cursor selectAllTeam() {
        sqLiteDB = todaysDB.getReadableDatabase();
        String[] allColumns = new String[]{TeamTableContents.TEAM_ID, TeamTableContents.TEAM_TMID,
                TeamTableContents.TEAM_LEADER, TeamTableContents.TEAM_MOBILE,
                TeamTableContents.TEAM_DATE, TeamTableContents.TEAM_MEMO};

        Cursor cusAllTeam = sqLiteDB.query( TeamTableContents.TEAM_TABLE, allColumns,
                null, null, null,
                null, TeamTableContents.TEAM_ID + " DESC " );
        if (cusAllTeam != null) {
            cusAllTeam.moveToFirst();
        }
        sqLiteDB.close();

        return cusAllTeam;
    }

    public Cursor searchTeam(String search) {
        sqLiteDB = todaysDB.getReadableDatabase();
        String selectQuery = "SELECT " +
                TeamTableContents.TEAM_ID + "," +
                TeamTableContents.TEAM_TMID + "," +
                TeamTableContents.TEAM_LEADER + "," +
                TeamTableContents.TEAM_MOBILE + "," +
                TeamTableContents.TEAM_DATE + "," +
                TeamTableContents.TEAM_MEMO +
                " FROM " + TeamTableContents.TEAM_TABLE +
                " WHERE " + TeamTableContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%' order by id desc";

        Cursor cusSearchTeam = sqLiteDB.rawQuery( selectQuery, null );
        if (cusSearchTeam != null) {
            int r = cusSearchTeam.getCount();
            for (int i = 0; i < r; i++) {
                cusSearchTeam.moveToNext();

            }
        } else {
            cusSearchTeam.close();
            return null;
        }
        return cusSearchTeam;
    }

    public ArrayList<TeamContents> getList(){
        //String List
        String id;
        String tid;
        String leader;
        String mobile;
        String date;
        String memo;
        //List 배열
        String[] allColumns = new String[] {
                TeamTableContents.TEAM_ID,
                TeamTableContents.TEAM_TMID,
                TeamTableContents.TEAM_LEADER,
                TeamTableContents.TEAM_MOBILE,
                TeamTableContents.TEAM_DATE,
                TeamTableContents.TEAM_MEMO };

        ArrayList<TeamContents> list = new ArrayList<>();

//        String listQuery = "select * from " + TeamTableContents.TEAM_TABLE;
//        Cursor cursor = sql.rawQuery(listQuery, null);
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDB.query(TeamTableContents.TEAM_TABLE, allColumns,
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()){
            do{
                id = cursor.getString(0);
                tid = cursor.getString(1);
                leader = cursor.getString(2);
                mobile = cursor.getString(3);
                date = cursor.getString(4);
                memo = cursor.getString(5);

                TeamContents results = new TeamContents(id, tid, leader, mobile, date, memo);
                list.add(results);
            }while (cursor.moveToNext());

        }
        return list;
    }
}
