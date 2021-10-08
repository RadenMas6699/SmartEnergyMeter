package com.radenmas.smartenergymeter;

public class DataChart {
    float volt, arus1, arus2, arus3, watt1, watt2, watt3;
    long time;

    public DataChart() {
    }

    public DataChart(float volt, float arus1, float arus2, float arus3, float watt1, float watt2, float watt3, long time) {
        this.volt = volt;
        this.arus1 = arus1;
        this.arus2 = arus2;
        this.arus3 = arus3;
        this.watt1 = watt1;
        this.watt2 = watt2;
        this.watt3 = watt3;
        this.time = time;
    }

    public float getVolt() {
        return volt;
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

    public float getWatt1() {
        return watt1;
    }

    public float getWatt2() {
        return watt2;
    }

    public float getWatt3() {
        return watt3;
    }

    public long getTime() {
        return time;
    }
}
