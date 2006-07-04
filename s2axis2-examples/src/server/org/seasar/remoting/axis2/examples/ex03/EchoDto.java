package org.seasar.remoting.axis2.examples.ex03;

public class EchoDto {

    private String  strParam    = "";

    private short   shortParam  = 1;

    private int     intParam    = 2;

    private long    longParam   = 3L;

    private float   floatParam  = 1.0f;

    private double  doubleParam = 1.1;

    private boolean boolParam   = true;

    public EchoDto() {}

    public String getStrParam() {
        return this.strParam;
    }

    public void setStrParam(String strParam) {
        this.strParam = strParam;
    }

    public short getShortParam() {
        return this.shortParam;
    }

    public void setShortParam(short shortParam) {
        this.shortParam = shortParam;
    }

    public int getIntParam() {
        return this.intParam;
    }

    public void setIntParam(int intParam) {
        this.intParam = intParam;
    }

    public long getLongParam() {
        return this.longParam;
    }

    public void setLongParam(long longParam) {
        this.longParam = longParam;
    }

    public float getFloatParam() {
        return this.floatParam;
    }

    public void setFloatParam(float floatParam) {
        this.floatParam = floatParam;
    }

    public double getDoubleParam() {
        return this.doubleParam;
    }

    public void setDoubleParam(double doubleParam) {
        this.doubleParam = doubleParam;
    }

    public boolean isBoolParam() {
        return this.boolParam;
    }

    public void setBoolParam(boolean boolParam) {
        this.boolParam = boolParam;
    }

}
