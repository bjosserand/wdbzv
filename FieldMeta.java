package com.blj.misc;

/**
 * Created by bjosserand on 6/3/17.
 */
public class FieldMeta {
    private String name;
    private int position;
    private String type;
    private String dataType;
    private String lowRange;
    private String highRange;
    private String onError;

    public FieldMeta(String name, int position, String type, String dataType, String lowRange, String highRange, String onError) {
        this.name = name;
        this.position = position;
        this.type = type;
        this.dataType = dataType;
        this.lowRange = lowRange;
        this.highRange = highRange;
        this.onError = onError;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getLowRange() {
        return lowRange;
    }

    public void setLowRange(String lowRange) {
        this.lowRange = lowRange;
    }

    public String getHighRange() {
        return highRange;
    }

    public void setHighRange(String highRange) {
        this.highRange = highRange;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }
}
