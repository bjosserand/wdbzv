package com.blj.misc;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by bjosserand on 6/3/17.
 */
public class FileMeta {
    private String separator;
    private int fileErrorTolerance;
    private String onError;
    private int rowErrorTolerance;
    private List<FieldMeta> fields;

    public List<FieldMeta> getFields() {
        return fields;
    }

    public void setFields(List<FieldMeta> fields) {
        this.fields = fields;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public int getFileErrorTolerance() {
        return fileErrorTolerance;
    }

    public void setFileErrorTolerance(int fileErrorTolerance) {
        this.fileErrorTolerance = fileErrorTolerance;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }

    public int getRowErrorTolerance() {
        return rowErrorTolerance;
    }

    public void setRowErrorTolerance(int rowErrorTolerance) {
        this.rowErrorTolerance = rowErrorTolerance;
    }
}
