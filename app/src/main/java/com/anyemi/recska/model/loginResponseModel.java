package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 08-12-2017.
 */

public class loginResponseModel {


    /**
     * id : 666
     * name : P SATISH
     * email : satish@gmail.com
     * username : satish31
     * mpos_username : null
     * mpos_password : null
     * mpos_id : 45060085
     * group_id : 11
     * Service : anyemi
     * status : Success
     * version_id : 0.1
     */

    private String id;
    private String name;
    private String email;
    private String username;
    private String mpos_username;
    private String mpos_password;
    private String mpos_id;
    private String group_id;
    private String Service;
    private String status;
    private String version_id;
    private String Description;

    public String getMoile_number() {
        return moile_number;
    }

    public void setMoile_number(String moile_number) {
        this.moile_number = moile_number;
    }

    private String moile_number;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMpos_username() {
        return mpos_username;
    }

    public void setMpos_username(String mpos_username) {
        this.mpos_username = mpos_username;
    }

    public String getMpos_password() {
        return mpos_password;
    }

    public void setMpos_password(String mpos_password) {
        this.mpos_password = mpos_password;
    }

    public String getMpos_id() {
        return mpos_id;
    }

    public void setMpos_id(String mpos_id) {
        this.mpos_id = mpos_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getService() {
        return Service;
    }

    public void setService(String Service) {
        this.Service = Service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }
}
