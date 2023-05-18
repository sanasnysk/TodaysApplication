package com.sansang.todaysapplication.Contents;

public class JournalContents {
    //Journal Contents Table Columns Name
    String id;
    String journalId;
    String journalDate;
    String siteName;
    String journalOne;
    String sitePay;
    String journalAmount;
    String journalMemo;
    String siteId;
    String teamLeader;
    String teamId;



    public JournalContents() {
    }

    public JournalContents(String id, String journalId, String journalDate, String siteName, String journalOne, String sitePay, String journalAmount, String journalMemo, String siteId, String teamLeader, String teamId) {
        this.id = id;
        this.journalId = journalId;
        this.journalDate = journalDate;
        this.siteName = siteName;
        this.journalOne = journalOne;
        this.sitePay = sitePay;
        this.journalAmount = journalAmount;
        this.journalMemo = journalMemo;
        this.siteId = siteId;
        this.teamLeader = teamLeader;
        this.teamId = teamId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    public String getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getJournalOne() {
        return journalOne;
    }

    public void setJournalOne(String journalOne) {
        this.journalOne = journalOne;
    }

    public String getSitePay() {
        return sitePay;
    }

    public void setSitePay(String sitePay) {
        this.sitePay = sitePay;
    }

    public String getJournalAmount() {
        return journalAmount;
    }

    public void setJournalAmount(String journalAmount) {
        this.journalAmount = journalAmount;
    }

    public String getJournalMemo() {
        return journalMemo;
    }

    public void setJournalMemo(String journalMemo) {
        this.journalMemo = journalMemo;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
