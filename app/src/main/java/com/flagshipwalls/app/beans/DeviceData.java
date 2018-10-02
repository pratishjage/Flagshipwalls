package com.flagshipwalls.app.beans;

import java.util.Date;

public class DeviceData {

    String brandID;

    String brandName;

    String description;

    String deviceName;

    Date device_release_date;
    String modelNo;

    String osID;

    String osName;


    Double os_version;

    String platform_id;



    String platform_name;

    String  platform_logo_url;

    public String getPlatform_logo_url() {
        return platform_logo_url;
    }

    public void setPlatform_logo_url(String platform_logo_url) {
        this.platform_logo_url = platform_logo_url;
    }

    public Date getDevice_release_date() {
        return device_release_date;
    }

    public void setDevice_release_date(Date device_release_date) {
        this.device_release_date = device_release_date;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }



    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getOsID() {
        return osID;
    }

    public void setOsID(String osID) {
        this.osID = osID;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }


    public Double getOs_version() {
        return os_version;
    }

    public void setOs_version(Double os_version) {
        this.os_version = os_version;
    }

    public String getPlatform_id() {
        return platform_id;
    }

    public void setPlatform_id(String platform_id) {
        this.platform_id = platform_id;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }
}
