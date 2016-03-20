package com.sdsmdg.harshit.draw.Chat;


public class Query {

    private String id;
    private String mQuery;
    private String mAnswer;

    public Query(String query, String answer)
    {
        mQuery = query;
        mAnswer = answer;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getmAnswer() {
        return mAnswer;
    }

    public String getmQuery() {
        return mQuery;
    }
}
