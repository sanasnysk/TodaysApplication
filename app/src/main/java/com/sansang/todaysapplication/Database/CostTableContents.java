package com.sansang.todaysapplication.Database;

public class CostTableContents {
    //Create Table Name Income
    public static final String COST_TABLE = "cost_table";

    //Contacts Journal Columns Name
    public static final String COST_ID = "id";
    public static final String COST_CSID = "costId";
    public static final String COST_DATE = "costDate";
    public static final String SITE_NAME = "siteName";
    public static final String COST_DETAIL = "costDetail";
    public static final String COST_PRICE = "costPrice";
    public static final String COST_AMOUNT = "costAmount";
    public static final String SITE_STID = "siteId";
    public static final String INCOME_MEMO = "Memo";

    //Create Table Income
    public static final String CREATE_TABLE_COST = "CREATE TABLE "
            + COST_TABLE + "(" + COST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COST_CSID + " TEXT," + COST_DATE + " TEXT,"
            + SITE_NAME + " TEXT," + COST_DETAIL + " INTEGER,"
            + COST_PRICE + " INTEGER," + COST_AMOUNT + " INTEGER,"
            + SITE_STID + " TEXT ," + INCOME_MEMO + " TEXT );";

}
