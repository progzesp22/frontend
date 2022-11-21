package com.example.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Request Handler class implemented as singleton. Based on google's Volley library.
 * Allows user to send JSON requests. All networking is done in separate process.
 * Responses are handled in main process to allow for straightforward UI changes.
 * For now a server is running on my private cloud machine. It can be used for testing.
 * But it is not guaranteed to be up and running all the time.
 * Not all functions in this file were tested and some may require a little bit of tweaking to work
 * properly. Please let me know if you find any bugs.
 *
 * @author Kamil Włodarski
 */
public class RequestHandler {
    private static volatile RequestHandler instance;
    private RequestQueue requestQueue;

    /**
     * Url to a server handling requests.
     */
    private final String url = "http://130.61.232.251:8080/rest/";

    private RequestHandler() {
    }

    /**
     * no public constructor because this class is a singleton.
     *
     * @param context required for first time initialization
     * @return singleton instance of this class
     */
    public static RequestHandler getInstance(Context context) {
        if (instance == null) {
            synchronized (RequestHandler.class) {
                if (instance == null) {
                    instance = new RequestHandler();
                    instance.requestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return instance;
    }

    public void postUserLogin(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("login", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "user/login", jsonBody, listener, errorListener);
        requestQueue.add(request);
    }


    /**
     * Send GET request for all tasks. Tasks are returned as JSONArray.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void getTasks(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "tasks", null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    public void getTasks(Long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "tasks?gameId=" + gameId, null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Send POST request with new Answer. For response format see documentation.
     * This method will evolve to be more generic once we go to implement more than plaintext tasks.
     *
     * @param taskId           for which task is this answer
     * @param response         what is the actual answer to the task (for now it is just a String)
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void postAnswer(long taskId, String response, Response.Listener<JSONObject> responseCallback,
                           Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("taskId", taskId);
            json.put("response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "answers", json, responseCallback, errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send POST request with a new Task. Should be available only to Game Master.
     *
     * @param task             task to be added
     * @param responseCallback what should be done with response data when it arrive    s
     * @param errorListener    what should be done with any errors when they occur
     */
    public void postTask(Task task, Response.Listener<JSONObject> responseCallback,
                         Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", task.getName());
            json.put("description", task.getDescription());
            json.put("gameId", task.getGameId());
            json.put("type", task.getType());
            JSONArray jsonPrerequsiteTasks = new JSONArray(task.getPrerequisiteTasks());
            json.put("prerequisiteTasks", jsonPrerequsiteTasks);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "tasks", json, responseCallback, errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send PUT request to edit an existing Task. Should be available only to Game Master.
     * For all i know you have to supply all parameters because the old task is being replaced be the
     * new one not changed. TODO: validate that statement and maybe change the code accordingly
     *
     * @param task             task to be edited
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void patchTask(Task task, Response.Listener<JSONObject> responseCallback,
                          Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", task.getName());
            json.put("description", task.getDescription());
            json.put("type", task.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, url + "tasks/" + task.getId(), json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send GET request for all unchecked answers. Those answers are returned in JSONArray.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void getAnswers(Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "answers?filter=" + (filterUnchecked ? "unchecked" : "all"), null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    public void getAnswers(long gameId, Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "answers?gameId=" + gameId + "&filter=" + (filterUnchecked ? "unchecked" : "all"),
                        null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    public void getAnswer(long answerId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url + "answers/" + answerId, null, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send PUT request to edit the status of an existing Answer. It's a way for a Game Master to
     * approve or reject an answers.
     *
     * @param answerId         what answer we want to edit
     * @param approved         true if we want to approve the answer, false if we want to reject it
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */

    public void patchAnswer(int answerId, Boolean approved, Response.Listener<JSONObject> responseCallback,
                            Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("approved", approved);
            json.put("checked", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, url + "answers/" + answerId, json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send POST request to create a new Game. At least one game needs to exist on server side for
     * the app to work properly.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void postGame(Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "games", json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send GET request to get all existing Games. Games are returned in JSONArray.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void getGames(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "games", null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }
}
