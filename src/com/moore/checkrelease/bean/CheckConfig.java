package com.moore.checkrelease.bean;

import java.io.Serializable;
import java.util.List;

public class CheckConfig implements Serializable {
    private List<CheckItem> checkItems;

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public static class CheckItem implements Serializable {
        private String className;
        private String fieldName;
        private String fieldValue;

        private boolean isCheckRight;
        private String errorMsg;

        public CheckItem() {
        }

        public CheckItem(String className, String fieldName, String fieldValue) {
            this.className = className;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public boolean isCheckRight() {
            return isCheckRight;
        }

        public void setCheckRight(boolean checkRight) {
            isCheckRight = checkRight;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
