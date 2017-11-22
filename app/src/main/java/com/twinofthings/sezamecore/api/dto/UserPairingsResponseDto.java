package com.twinofthings.sezamecore.api.dto;

/**
 * @author Michael Präauer
 *         © NOUS 2017
 */

public class UserPairingsResponseDto {

    private String username;
    private String id;
    private String status;
    private String created;
    private String client;
    private String client_id;   //This id is equal to the client_id in a corresponding event

    //Length of the recent logins list for sorting the list by logins in services 'overview' tab
    private int loginSortIndex;


    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }

    public String getClient() {
        return client;
    }

    public String getClient_id() {
        return client_id;
    }

    public int getLoginSortIndex() {
        return loginSortIndex;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setLoginSortIndex(int loginSortIndex) {
        this.loginSortIndex = loginSortIndex;
    }
}
