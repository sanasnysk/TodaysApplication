package com.sansang.todaysapplication.Database;

public class JournalTableContents {
    //---- Journal Table Constants ----
    //Table Name Journal
    public static final String JOURNAL_TABLE = "journal_table";
    //Contacts Journal Columns Name
    public static final String JOURNAL_ID = "id";
    public static final String JOURNAL_JNID = "journalId";
    public static final String JOURNAL_DATE = "journalDate";
    public static final String SITE_NAME = "siteName";
    public static final String JOURNAL_ONE = "oneDay";
    public static final String SITE_PAY = "sitePay";
    public static final String JOURNAL_AMOUNT = "journalAmount";
    public static final String JOURNAL_MEMO = "journalMemo";
    public static final String SITE_STID = "siteId";
    public static final String TEAM_LEADER = "teamLeader";
    public static final String TEAM_TMID = "teamId";


    //Create Table Journal
    public static final String CREATE_TABLE_JOURNAL = "CREATE TABLE "
            + JOURNAL_TABLE + "(" + JOURNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + JOURNAL_JNID + "  TEXT ," + JOURNAL_DATE + "  TEXT ,"
            + SITE_NAME + " TEXT ," + JOURNAL_ONE + "  INTEGER ,"
            + SITE_PAY + " INTEGER ," + JOURNAL_AMOUNT + " INTEGER ,"
            + JOURNAL_MEMO + " TEXT ," + SITE_STID + " TEXT ,"
            + TEAM_LEADER + " TEXT ," + TEAM_TMID + " TEXT );";
}
