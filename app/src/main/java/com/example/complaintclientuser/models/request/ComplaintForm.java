package com.example.complaintclientuser.models.request;

public class ComplaintForm {

    private String topic;
    private String body;
    private String category;

    public ComplaintForm(String topic, String body, String category) {
        this.topic = topic;
        this.body = body;
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
