package com.sansang.todaysapplication.Contents;

public class CostContents {
    //... Cost Contents Table Columns Name
    String id;
    String costId;
    String costDate;
    String siteName;
    String costDetail;
    String costPrice;
    String costAmount;
    String siteId;
    String costMemo;

    public CostContents() {
    }

    public CostContents(String id, String costId, String costDate, String siteName, String costDetail, String costPrice, String costAmount, String siteId, String costMemo) {
        this.id = id;
        this.costId = costId;
        this.costDate = costDate;
        this.siteName = siteName;
        this.costDetail = costDetail;
        this.costPrice = costPrice;
        this.costAmount = costAmount;
        this.siteId = siteId;
        this.costMemo = costMemo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCostDetail() {
        return costDetail;
    }

    public void setCostDetail(String costDetail) {
        this.costDetail = costDetail;
    }

    public String getcostPrice() {
        return costPrice;
    }

    public void setcostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getCostMemo() {
        return costMemo;
    }

    public void setCostMemo(String costMemo) {
        this.costMemo = costMemo;
    }
}
