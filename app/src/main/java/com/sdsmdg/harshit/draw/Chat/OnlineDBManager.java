package com.sdsmdg.harshit.draw.Chat;

import android.content.Context;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.util.List;

public class OnlineDBManager {

    private MobileServiceClient mClient;
    private MobileServiceTable<Query> mQueryTable;
    List<Query> list;

    public OnlineDBManager(String url, Context context) {

        try {
            mClient = new MobileServiceClient(
                    url,
                    context
            );
            Log.i("harshit", "Got the client");
            mQueryTable = mClient.getTable(Query.class);
        } catch (Exception e) {
            Log.e("harshit", "failed to connect to database");
        }

        list = null;
        try{
            list = mQueryTable.execute().get();
        }
        catch (Exception e) {
            Log.e("harshit", e.getMessage());
        }

    }

    public List<Query> refreshItemsFromMobileServiceTable(){
        try{
            list = mQueryTable.execute().get();
        }
        catch (Exception e) {
            Log.e("harshit", e.getMessage());
        }
        return list;
    }

    public void insert(Query item)
    {
        try {
            mQueryTable.insert(item, new TableOperationCallback<Query>() {
                public void onCompleted(Query entity, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        Log.i("harshit", "Insert Succeeded");
                    } else {
                        Log.i("harshit", "Insert Failed");
                    }
                }
            });
        } catch (Exception e) {
            Log.e("harshit", "Error occured while insertion");
        }
    }

    public void update(Query query)
    {
        try
        {
            mQueryTable.update(query).get();
            Log.i("harshit", "Answer successfully submitted !");
        }
        catch(Exception e)
        {
            Log.e("harshit", e.getMessage());
        }
    }

}
