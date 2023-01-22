package com.progzesp22.scoutout;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Handler class implemented as singleton. Based on google's Volley library.
 * Allows user to send JSON requests. All networking is done in separate process.
 * Responses are handled in main process to allow for straightforward UI changes
 * Not all functions in this file were tested and some may require a little bit of tweaking to work
 * properly. Please let me know if you find any bugs.
 *
 * @author Kamil Włodarski
 */
public class RequestHandler implements RequestInterface {
    private static volatile RequestHandler instance;
    private final RequestQueue requestQueue;

    private static String sessionToken;

    /**
     * Url to a server handling requests.
     */
    private final String url = "http://connect.knowak.xyz:2138/";
    private final String urlRest = url;

    public RequestHandler(Context context) {
        requestQueue = Volley.newRequestQueue(context);

    }

    /**
     * Converts empty responses to "{}"
     * Headers contain the authorization token
     */
    private static class MyJsonRequest extends JsonObjectRequest {
        Map<String, String> headers;
        public MyJsonRequest(int method, String url, JSONObject obj, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
            super(method, url, obj, listener, errorListener);
            headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + sessionToken);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            if (response.data.length == 0) {
                byte[] responseData = "{}".getBytes(StandardCharsets.UTF_8);
                response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
            }
            return super.parseNetworkResponse(response);
        }

        @Override
        public Map<String, String> getHeaders(){
            return headers;
        }
    }

    private static class MyJsonArrayRequest extends JsonArrayRequest {
        Map<String, String> headers;
        public MyJsonArrayRequest(int method, String url, JSONArray obj, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
            super(method, url, obj, listener, errorListener);
            headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + sessionToken);
        }

        @Override
        public Map<String, String> getHeaders(){
            return headers;
        }
    }

    @Override
    public void setSessionToken(String sessionToken) {
        RequestHandler.sessionToken = sessionToken;
    }

    /**
     * Sends a request to the server to log in.
     * @param username username of the user
     * @param password password of the user
     * @param listener listener that will be called when response is received
     * @param errorListener listener that will be called when error occurs
     */
    @Override
    public void postUserLogin(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "user/login", jsonBody, listener, errorListener);
        requestQueue.add(request);
    }




    @Override
    public void postRegister(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "user/register", jsonBody, listener, errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response.data.length == 0) {
                    byte[] responseData = "{}".getBytes(StandardCharsets.UTF_8);
                    response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
                }
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to get a list of all teams.
     * Response is handled in listener. Teams are returned as a JSONArray.
     * @param responseCallback callback that will be called when response is received
     * @param errorListener   callback that will be called when error occurs
     */
    @Override
    public void getTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "teams", null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sends a request to the server to get a list of teams for given gameId.
     * Response is handled in listener. Teams are returned as a JSONArray.
     * @param responseCallback callback that will be called when response is received
     * @param errorListener   callback that will be called when error occurs
     */
    @Override
    public void getTeams(long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "teams?gameId=" + gameId, null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sends a request to the server to get a list of all teams that player joined.
     * @param responseCallback callback that will be called when response is received
     * @param errorListener  callback that will be called when error occurs
     */
    @Override
    public void getJoinedTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "teams?filter=joined", null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sends a request to the server to add a new team.
     * @param team team to be added to the server
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    @Override
    public void postTeams(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", team.getName());
            jsonBody.put("gameId", team.getGameId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new MyJsonRequest(Request.Method.POST, urlRest + "teams", jsonBody, responseCallback, errorListener);
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to get additional info on a team.
     * @param teamId id of the team
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    @Override
    public void getTeamInfo(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new MyJsonRequest(Request.Method.GET, urlRest + "teams/" + teamId, null, responseCallback, errorListener);
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to update info on a team. We can only update name and members.
     * @param team team to be updated
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    @Override
    public void patchTeam(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", team.getName());
            JSONArray members = new JSONArray();
            for (String member : team.getMembers()) {
                members.put(member);
            }
            jsonBody.put("members", members);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new MyJsonRequest(Request.Method.PATCH, urlRest + "teams/" + team.getId(), jsonBody, responseCallback, errorListener);
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to join current user to a team.
     * @param teamId id of the team
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    @Override
    public void postTeamJoin(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new MyJsonRequest(Request.Method.POST, urlRest + "teams/" + teamId + "/join", null, responseCallback, errorListener);
        requestQueue.add(request);
    }

    /**
     * Send GET request for all tasks. Tasks are returned as JSONArray.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    @Override
    public void getTasks(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "tasks", null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void getTasks(Long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "tasks?gameId=" + gameId, null, responseCallback, errorListener);

        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void postAnswer(Answer answer, Response.Listener<JSONObject> responseCallback,
                           Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("taskId", answer.getTaskId());
            json.put("response", answer.getAnswer());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.POST, urlRest + "answers", json, responseCallback, errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send POST request with a new Task. Should be available only to Game Master.
     *
     * @param task             task to be added
     * @param responseCallback what should be done with response data when it arrive    s
     * @param errorListener    what should be done with any errors when they occur
     */
    @Override
    public void postTask(Task task, Response.Listener<JSONObject> responseCallback,
                         Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", task.getName());
            json.put("description", task.getDescription());
            json.put("gameId", task.getGameId());
            json.put("type", task.getType());
            json.put("maxScore", task.getMaxScore());
            JSONArray jsonPrerequsiteTasks = new JSONArray(task.getPrerequisiteTasks());
            json.put("prerequisiteTasks", jsonPrerequsiteTasks);
            json.put("correct_answer", task.getCorrectAnswer());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.POST, urlRest + "tasks", json, responseCallback, errorListener);

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
    @Override
    public void patchTask(Task task, Response.Listener<JSONObject> responseCallback,
                          Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", task.getName());
            json.put("description", task.getDescription());
            json.put("type", task.getType());
            json.put("maxScore", task.getMaxScore());
            json.put("correct_answer", task.getCorrectAnswer());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.PATCH, urlRest + "tasks/" + task.getId(), json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send GET request for all unchecked answers. Those answers are returned in JSONArray.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    @Override
    public void getAnswers(Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "answers?filter=" + (filterUnchecked ? "unchecked" : "all"), null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void getAnswers(long gameId, Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "answers?gameId=" + gameId + "&filter=" + (filterUnchecked ? "unchecked" : "all"),
                        null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void getAnswer(long answerId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.GET, urlRest + "answers/" + answerId, null, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void patchAnswer(Answer answer, Response.Listener<JSONObject> responseCallback,
                            Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();

        try {
            json.put("approved", answer.isApproved());
            json.put("score", answer.getScore());
            json.put("checked", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.PATCH, urlRest + "answers/" + answer.getId(), json, responseCallback,
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
    @Override
    public void postGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", game.getName());
            json.put("startTime", game.getStartTimeString());
            json.put("endCondition", game.getEndCondition());
            json.put("state", game.getState().toString());
            json.put("description", game.getDescription());


            if (game.getEndCondition() == Game.EndCondition.TIME) {
                json.put("endTime", game.getEndTimeString());
            } else if(game.getEndCondition() == Game.EndCondition.SCORE) {
                json.put("endScore", game.getEndScore());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.POST, urlRest + "games", json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send GET request to get all existing Games. Games are returned in JSONArray.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    @Override
    public void getGames(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, urlRest + "games", null, responseCallback,
                        errorListener);

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void getGame(long gameId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.GET, urlRest + "games/" + gameId, null, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void patchGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();
        try {
            json.put("name", game.getName());
            json.put("startTime", game.getStartTimeString());
            json.put("endCondition", game.getEndCondition());
            json.put("state", game.getState().toString());
            json.put("description", game.getDescription());

            if (game.getEndCondition() == Game.EndCondition.TIME) {
                json.put("endTime", game.getEndTimeString());
            } else if(game.getEndCondition() == Game.EndCondition.SCORE) {
                json.put("endScore", game.getEndScore());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new MyJsonRequest
                (Request.Method.PATCH, urlRest + "games/" + game.getId(), json, responseCallback,
                        errorListener);

        requestQueue.add(jsonObjectRequest);
    }


}
