package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 20-02-2018.
 */

public class CheckSumModel {

    /**
     * status : success
     * checkSum : EKJwBNFmAFzxeSJ4+Ve1ZNU+R0bTVwGLsFelhHkt223HwdOVNjxBrkdCsdppuCRz8DJ1QzSl53q7FOQykGqtW4F0iWyEqf+1GXFTD2JsCA0=
     */

    private String status;
    private String checkSum;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
