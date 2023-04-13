package com.sansang.todaysapplication.Database;

public class IncomeTableContents {
    //Create Table Name Income
    public static final String INCOME_TABLE = "income_table";
    //Contacts Journal Columns Name
    public static final String INCOME_ID = "id";
    public static final String INCOME_ICID = "incomeId";
    public static final String INCOME_DATE = "incpmeDate";
    public static final String SITE_NAME = "siteName";
    public static final String INCOME_DEPOSIT = "incomeDeposit";
    public static final String INCOME_TAX = "incomeTax";
    public static final String SITE_STID = "siteId";
    public static final String TEAM_TMID = "teamId";
    public static final String INCOME_MEMO = "Memo";

    //Create Table Income
    public static final String CREATE_TABLE_INCOME = "CREATE TABLE "
            + INCOME_TABLE + "(" + INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + INCOME_ICID + " TEXT," + INCOME_DATE + " TEXT,"
            + SITE_NAME + " TEXT," + INCOME_DEPOSIT + " INTEGER,"
            + INCOME_TAX + " INTEGER," + SITE_STID + " TEXT,"
            + TEAM_TMID + " TEXT ," + INCOME_MEMO + " TEXT )";
}
