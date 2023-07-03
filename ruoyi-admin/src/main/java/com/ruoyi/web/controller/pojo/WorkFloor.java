package com.ruoyi.web.controller.pojo;

public class WorkFloor {
    private int floor;
    private int start;//4Bytes，扩大10000倍
    private int speed;//4Bytes，扩大10000
    private int end;//4Bytes，扩大10000倍
    private int density;//2Bytes，不扩大

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }
}
