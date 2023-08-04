package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.Constants;
import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.Date;

public class Comment {

    private Author author;
    private Date createdAt;
    private Constants.LineType lineType;
    private String path;
    private Integer line;
    private String note;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Constants.LineType getLineType() {
        return lineType;
    }

    public void setLineType(Constants.LineType lineType) {
        this.lineType = lineType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
