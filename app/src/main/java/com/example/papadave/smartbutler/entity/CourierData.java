package com.example.papadave.smartbutler.entity;

/**
 * Created by papadave on 2017/10/19.
 */
public class CourierData {
    //时间
    private String datatime;
    //状态
    private String remark;
    //城市
    private String zone;

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String  toString() {
        return "CourierData{" +
                "datatime='" + datatime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
