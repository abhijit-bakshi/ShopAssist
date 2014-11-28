package com.sapient.shopassist;

import org.apache.http.client.methods.HttpUriRequest;


public abstract class HttpRequestHandler {

    public abstract HttpUriRequest getHttpRequestMethod();

    public abstract void onResponse(String result);

    public void execute(){
        new AsyncHttpTask(this).execute();
    } 
}




