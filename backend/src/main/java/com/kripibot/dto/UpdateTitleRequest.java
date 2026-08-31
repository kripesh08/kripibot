package com.kripibot.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateTitleRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    public UpdateTitleRequest() {}

    public UpdateTitleRequest(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
