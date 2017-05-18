package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2017/5/15.
 */

public class Go {
    private int resultId;
    private String tips;
    private int lotteryCount;
    private String lotteryCountDesc;


    public String getLotteryCountDesc() {
        return lotteryCountDesc;
    }

    public void setLotteryCountDesc(String lotteryCountDesc) {
        this.lotteryCountDesc = lotteryCountDesc;
    }

    public int getLotteryCount() {
        return lotteryCount;
    }

    public void setLotteryCount(int lotteryCount) {
        this.lotteryCount = lotteryCount;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
