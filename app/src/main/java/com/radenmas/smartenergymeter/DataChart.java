package com.radenmas.smartenergymeter;

public class DataChart {
    float Tegangan, kkb, lampu1, lampu2;
    long time;

    public DataChart() {
    }

    public DataChart(float tegangan, float kkb, float lampu1, float lampu2, long time) {
        Tegangan = tegangan;
        this.kkb = kkb;
        this.lampu1 = lampu1;
        this.lampu2 = lampu2;
        this.time = time;
    }

    public float getTegangan() {
        return Tegangan;
    }

    public void setTegangan(float tegangan) {
        Tegangan = tegangan;
    }

    public float getKkb() {
        return kkb;
    }

    public void setKkb(float kkb) {
        this.kkb = kkb;
    }

    public float getLampu1() {
        return lampu1;
    }

    public void setLampu1(float lampu1) {
        this.lampu1 = lampu1;
    }

    public float getLampu2() {
        return lampu2;
    }

    public void setLampu2(float lampu2) {
        this.lampu2 = lampu2;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
