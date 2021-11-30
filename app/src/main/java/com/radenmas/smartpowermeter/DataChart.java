package com.radenmas.smartpowermeter;

public class DataChart {
    int volt, watt1, watt2, watt3;
    float arus1, arus2, arus3;
    long time;

    public DataChart() {
    }

    public DataChart(int volt, int watt1, int watt2, int watt3, float arus1, float arus2, float arus3, long time) {
        this.volt = volt;
        this.watt1 = watt1;
        this.watt2 = watt2;
        this.watt3 = watt3;
        this.arus1 = arus1;
        this.arus2 = arus2;
        this.arus3 = arus3;
        this.time = time;
    }

    public int getVolt() {
        return volt;
    }

    public int getWatt1() {
        return watt1;
    }

    public int getWatt2() {
        return watt2;
    }

    public int getWatt3() {
        return watt3;
    }

    public float getArus1() {
        return arus1;
    }

    public float getArus2() {
        return arus2;
    }

    public float getArus3() {
        return arus3;
    }

    public long getTime() {
        return time;
    }
}
