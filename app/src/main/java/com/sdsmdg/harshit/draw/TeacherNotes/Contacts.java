package com.sdsmdg.harshit.draw.TeacherNotes;

/**
 * Created by Rahul Yadav on 3/17/2016.
 */
public class Contacts {

    private String name, topic, content;

    public Contacts(String name, String topic, String content) {
        this.setName(name);
        this.setTopic(topic);
        this.setContent(content);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
