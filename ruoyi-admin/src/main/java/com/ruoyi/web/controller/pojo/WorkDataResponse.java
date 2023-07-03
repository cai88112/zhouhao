package com.ruoyi.web.controller.pojo;

import java.util.List;

public class WorkDataResponse {
    private double time;//1Bytes，扩大10倍
    private int speed;//4Bytes，扩大10000倍
    private int end;//4Bytes，扩大10000倍
    private int density;//2Bytes，不扩大

    private int floors;

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    private List<WorkFloor> list;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
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

    public List<WorkFloor> getList() {
        return list;
    }

    public void setList(List<WorkFloor> list) {
        this.list = list;
    }
}
