package com.yellowsoft.subhankar.pokesnap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by subhankar on 7/20/2016.
 */
public class RegisterResult {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
