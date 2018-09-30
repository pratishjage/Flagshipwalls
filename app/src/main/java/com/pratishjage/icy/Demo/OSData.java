package com.pratishjage.icy.Demo;

import java.util.Date;

public class OSData {

    Date created_at;

    String local_name;
    String name;

    String platform_name;
    String platform_logo_url;

    Date release_date;

    Double version_number;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getLocal_name() {
        return local_name;
    }

    public void setLocal_name(String local_name) {
        this.local_name = local_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPlatform_logo_url() {
        return platform_logo_url;
    }

    public void setPlatform_logo_url(String platform_logo_url) {
        this.platform_logo_url = platform_logo_url;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public Double getVersion_number() {
        return version_number;
    }

    public void setVersion_number(Double version_number) {
        this.version_number = version_number;
    }
}
