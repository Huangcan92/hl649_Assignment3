package com.mobileappclass.assignment3;

/**
 */
public class Info {
    private String time;
    private String y;
    private String x;
    private String netid;

    public Info(String time, String y, String x, String netid) {
        this.time = time;
        this.y = y;
        this.x = x;
        this.netid = netid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    @Override
    public String toString() {
        return "Info{" + "time='" + time + '\'' + ", y='" + y + '\'' + ", x='" + x + '\'' + ", netid='" + netid + '\'' + '}';
    }
}
