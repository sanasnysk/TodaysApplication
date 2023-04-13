package com.sansang.todaysapplication.Contents;

public class TeamContents {
    //-- Team Contents Table Columns Name

    String id;
    String teamId;
    String teamLeader;
    String teamPhone;
    String teamDate;
    String teamMemo;

    public TeamContents() {
    }

    public TeamContents(String id, String teamId, String teamLeader, String teamPhone, String teamDate, String teamMemo) {
        this.id = id;
        this.teamId = teamId;
        this.teamLeader = teamLeader;
        this.teamPhone = teamPhone;
        this.teamDate = teamDate;
        this.teamMemo = teamMemo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getTeamPhone() {
        return teamPhone;
    }

    public void setTeamPhone(String teamPhone) {
        this.teamPhone = teamPhone;
    }

    public String getTeamDate() {
        return teamDate;
    }

    public void setTeamDate(String teamDate) {
        this.teamDate = teamDate;
    }

    public String getTeamMemo() {
        return teamMemo;
    }

    public void setTeamMemo(String teamMemo) {
        this.teamMemo = teamMemo;
    }
}
