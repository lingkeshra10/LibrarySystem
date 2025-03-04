package com.lingkesh.librarysystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchListBorrowerModel {
    private String id;
    private String name;
    private String email;
    private String limitContent;
    private String page;

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

    public String getLimitContent() {
        return limitContent;
    }

    public void setLimitContent(String limitContent) {
        this.limitContent = limitContent;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
