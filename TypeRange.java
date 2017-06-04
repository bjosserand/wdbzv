package com.blj.misc;

/**
 * Created by blj on 6/4/17.
 */
public class TypeRange {
    private String type;
    private String highRange;
    private String lowRange;
    private String dataType;

    public TypeRange(String type, String highRange, String lowRange, String dataType) {
        this.type = type;
        this.highRange = highRange;
        this.lowRange = lowRange;
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHighRange() {
        return highRange;
    }

    public void setHighRange(String highRange) {
        this.highRange = highRange;
    }

    public String getLowRange() {
        return lowRange;
    }

    public void setLowRange(String lowRange) {
        this.lowRange = lowRange;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
