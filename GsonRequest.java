package com.example.bruno.logindesigner;

import android.util.Log;

import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by SENAI on 22/10/2016.
 */

public class GsonRequest<T> extends Request<T> {
    private Gson gson;
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Listener<T> listener;
    private Object objeto;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     //* @param url URL of the request to make
     //* @param clazz Relevant class object, for Gson's reflection

     */

    public GsonRequest( int method,
                        String url,
                        Listener<T> listener,
                        ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        gson = new Gson();
    }

    @Override
    public String getBodyContentType() {
        return "application/json;charset=utf-8";
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       Listener<T> listener,
                       ErrorListener errorListener,
                       Object objeto) {

        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.objeto = objeto;
        gson = new Gson();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return gson.toJson(objeto).getBytes("UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Log.d("**** TESTE ****", "**** TESTE ****");
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d("GSON: ",json);

            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.d("GSON: ","ERRRO !");
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.d("GSON: ","ERRRRO 2");
            return Response.error(new ParseError(e));
        }
    }
}