package com.sansang.todaysapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class TodayDatabase extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "todays_db";
    public static final int DATABASE_VERSION = 1;
    private final Context context;

    public TodayDatabase(Context context) {

        super(context, context.getExternalFilesDir(DATABASE_NAME) +
                File.separator + "/database/" +
                File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TeamTableContents.CREATE_TABLE_TEAM);
        sqLiteDatabase.execSQL(SiteTableContents.CREATE_TABLE_SITE);
        sqLiteDatabase.execSQL(JournalTableContents.CREATE_TABLE_JOURNAL);
        sqLiteDatabase.execSQL(IncomeTableContents.CREATE_TABLE_INCOME);
        sqLiteDatabase.execSQL(CostTableContents.CREATE_TABLE_COST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TeamTableContents.TEAM_TABLE + "'");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + SiteTableContents.SITE_TABLE + "'");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + JournalTableContents.JOURNAL_TABLE + "'");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + IncomeTableContents.INCOME_TABLE + "'");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + CostTableContents.COST_TABLE + "'");

        } else {
            onCreate(sqLiteDatabase);
            //Database on current version
        }
    }

}
