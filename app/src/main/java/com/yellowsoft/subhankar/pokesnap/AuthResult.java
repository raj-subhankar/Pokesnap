package com.yellowsoft.subhankar.pokesnap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by subhankar on 7/18/2016.
 */
public class AuthResult {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;
    @SerializedName("name")
    private String userName;
    @SerializedName("team")
    private String team;

    public AuthResult(Boolean success, String message, String token, String userName, String team) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.userName = userName;
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
