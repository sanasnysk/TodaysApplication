package com.sansang.todaysapplication.Contents;

public class SiteContents {
    //-- Site Contents Table Columns Name
    String id;
    String siteId;
    String siteName;
    String sitePay;
    String siteManager;
    String siteDate;
    String siteMemo;
    String teamLeader;
    String teamId; //teamLeader 로 표시

    public SiteContents() {
    }

    public SiteContents(String id, String siteId, String siteName, String sitePay, String siteManager, String siteDate, String siteMemo, String teamLeader, String teamId) {
        this.id = id;
        this.siteId = siteId;
        this.siteName = siteName;
        this.sitePay = sitePay;
        this.siteManager = siteManager;
        this.siteDate = siteDate;
        this.siteMemo = siteMemo;
        this.teamLeader = teamLeader;
        this.teamId = teamId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSitePay() {
        return sitePay;
    }

    public void setSitePay(String sitePay) {
        this.sitePay = sitePay;
    }

    public String getSiteManager() {
        return siteManager;
    }

    public void setSiteManager(String siteManager) {
        this.siteManager = siteManager;
    }

    public String getSiteDate() {
        return siteDate;
    }

    public void setSiteDate(String siteDate) {
        this.siteDate = siteDate;
    }

    public String getSiteMemo() {
        return siteMemo;
    }

    public void setSiteMemo(String siteMemo) {
        this.siteMemo = siteMemo;
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
