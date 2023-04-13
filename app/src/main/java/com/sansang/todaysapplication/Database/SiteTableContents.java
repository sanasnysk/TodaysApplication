package com.sansang.todaysapplication.Database;

public class SiteTableContents {
    //--- Create Table Name Site ---
    public static final String SITE_TABLE = "site_table";

    //-- Contacts Site Table Columns Name
    public static final String SITE_ID = "ID";
    public static final String SITE_STID = "siteId" ;
    public static final String SITE_NAME = "siteName";
    public static final String SITE_PAY = "pay";
    public static final String SITE_MANAGER = "Manager";
    public static final String SITE_DATE = "Date";
    public static final String TEAM_LEADER = "teamLeader";
    public static final String TEAM_TMID = "teamId";
    public static final String SITE_MEMO = "Memo";

    //Create Table Site
    public static final String CREATE_TABLE_SITE = "CREATE TABLE "
            + SITE_TABLE + "(" + SITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SITE_STID + "  TEXT ," + SITE_NAME + "  TEXT ,"
            + SITE_PAY + "  INTEGER ," + SITE_MANAGER + " TEXT,"
            + SITE_DATE + " TEXT ," + TEAM_LEADER + " TEXT ,"
            + TEAM_TMID + " TEXT ," + SITE_MEMO + " TEXT);";
}
