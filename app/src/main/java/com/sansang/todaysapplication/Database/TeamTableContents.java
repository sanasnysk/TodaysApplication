package com.sansang.todaysapplication.Database;

import com.sansang.todaysapplication.Contents.TeamContents;

public class TeamTableContents {
    //--- Create Table Name Team ---
    public static final String TEAM_TABLE = "team_table";

    //--- Team Contents Table Columns Name
    public static final String TEAM_ID = "id";
    public static final String TEAM_TMID = "teamId" ;
    public static final String TEAM_LEADER = "teamLeader";
    public static final String TEAM_PHONE = "Phone";
    public static final String TEAM_DATE = "teamDate";
    public static final String TEAM_MEMO = "teamMemo";

    //-- Create Table Team
    public static final String CREATE_TABLE_TEAM = "CREATE TABLE " + TEAM_TABLE +
            "(" + TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TEAM_TMID + "  TEXT ," + TEAM_LEADER + "  TEXT ," + TEAM_PHONE + " TEXT ,"
            + TEAM_DATE + "  TEXT ," + TEAM_MEMO + " TEXT);";
}
