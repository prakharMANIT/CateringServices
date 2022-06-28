package com.example.cateringservice;

public class Member {
    public String beginDate,endDate;
    public String eventType;
    public String  contactNo;
    public String emailAddress;
    public int perDayTimes;

    public int getPerDayTimes() {
        return perDayTimes;
    }

    public void setPerDayTimes(int perDayTimes) {
        this.perDayTimes = perDayTimes;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Member(String beginDate,String endDate, String eventType,String  contactNo,String emailAddress,int perDayTimes) {

        this.perDayTimes = perDayTimes;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.eventType = eventType;
        this.contactNo = contactNo;
        this.emailAddress = emailAddress;
    }
}
