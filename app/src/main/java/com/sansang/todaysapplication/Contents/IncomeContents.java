package com.sansang.todaysapplication.Contents;

public class IncomeContents {
    //... Incomes Contents Table Columns Name
    String id;
    String incomeId;
    String incomeDate;
    String siteName;
    String incomeDeposit;
    String incomeTax;
    String siteId;
    String teamId;
    String incomeMemo;

    public IncomeContents() {
    }

    public IncomeContents(String id, String incomeId, String incomeDate, String siteName, String incomeDeposit, String incomeTax, String siteId, String teamId, String incomeMemo) {
        this.id = id;
        this.incomeId = incomeId;
        this.incomeDate = incomeDate;
        this.siteName = siteName;
        this.incomeDeposit = incomeDeposit;
        this.incomeTax = incomeTax;
        this.siteId = siteId;
        this.teamId = teamId;
        this.incomeMemo = incomeMemo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(String incomeId) {
        this.incomeId = incomeId;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getIncomeDeposit() {
        return incomeDeposit;
    }

    public void setIncomeDeposit(String incomeDeposit) {
        this.incomeDeposit = incomeDeposit;
    }

    public String getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(String incomeTax) {
        this.incomeTax = incomeTax;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getIncomeMemo() {
        return incomeMemo;
    }

    public void setIncomeMemo(String incomeMemo) {
        this.incomeMemo = incomeMemo;
    }
}
