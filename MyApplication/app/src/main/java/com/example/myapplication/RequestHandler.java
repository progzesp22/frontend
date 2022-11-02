package com.example.myapplication;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {
    private static RequestHandler instance;
    private RequestQueue requestQueue;
    private final String url = "http://130.61.232.251:8080/rest/"; // taki mój prywatny serwer na czas testowania

    private RequestHandler(Context ctx){
        requestQueue = getRequestQueue(ctx);
    }

    private RequestQueue getRequestQueue(Context ctx) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (instance == null) {
            instance = new RequestHandler(context);
        }
        return instance;
    }

    public void getTasks(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "tasks", null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    public void postAnswer(int taskId, int userId, String response, Response.Listener<JSONObject> responseCallback,
                           Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();
        try {
            json.put("taskId", taskId);
            json.put("userId", userId);
            json.put("response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "answers", json, responseCallback, errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    public void postTask(String name, String description, String type, int gameId, Response.Listener<JSONObject> responseCallback,
                         Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("description", description);
            json.put("gameId", gameId);
            json.put("taskType", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "tasks", json, responseCallback, errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    public void putTask(int taskId, String name, String description, int gameId, String type, Response.Listener<JSONObject> responseCallback,
                        Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("description", description);
            json.put("gameId", gameId);
            json.put("taskType", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url + "tasks/" + taskId, json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    public void getUncheckedAnswers(Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url + "answers/unchecked", null, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    public void patchAnswer(int answerId, Boolean approve, Response.Listener<JSONObject> responseCallback,
                            Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();
        try {
            json.put("approve", approve); // TODO: check!!!
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, url + "answers/" + answerId, json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    public void postGames(Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "games", json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    public void getGames(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "games", null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }
}
