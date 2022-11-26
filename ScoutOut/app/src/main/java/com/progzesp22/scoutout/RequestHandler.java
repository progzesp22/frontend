package com.progzesp22.scoutout;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class RequestHandler {
    private static volatile RequestHandler instance;
    private RequestQueue requestQueue;

    public static final long GAME_ID = 2;
    private static String sessionToken;

    /**
     * Url to a server handling requests.
     */
    private final String url = "http://144.24.171.255:8080/rest/";

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

    public static void setSessionToken(String sessionToken) {
        RequestHandler.sessionToken = sessionToken;
    }

    /**
     * Sends a request to the server to log in.
     * @param username username of the user
     * @param password password of the user
     * @param listener listener that will be called when response is received
     * @param errorListener listener that will be called when error occurs
     */
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
     * Sends a request to the server to get a list of all teams.
     * Response is handled in listener. Teams are returned as a JSONArray.
     * @param responseCallback callback that will be called when response is received
     * @param errorListener   callback that will be called when error occurs
     */
    public void getTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "teams", null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sends a request to the server to get a list of teams for given gameId.
     * Response is handled in listener. Teams are returned as a JSONArray.
     * @param responseCallback callback that will be called when response is received
     * @param errorListener   callback that will be called when error occurs
     */
    public void getTeams(long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "teams?gameId=" + gameId, null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sends a request to the server to get a list of all teams that player joined.
     * @param responseCallback callback that will be called when response is received
     * @param errorListener  callback that will be called when error occurs
     */
    public void getJoinedTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "teams?filter=joined", null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sends a request to the server to add a new team.
     * @param team team to be added to the server
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    public void postTeams(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", team.getName());
            jsonBody.put("gameId", team.getGameId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "teams", jsonBody, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to get additional info on a team.
     * @param teamId id of the team
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    public void getTeamInfo(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + "teams/" + teamId, null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to update info on a team. We can only update name and members.
     * @param team team to be updated
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url + "teams/" + team.getId(), jsonBody, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    /**
     * Sends a request to the server to join current user to a team.
     * @param teamId id of the team
     * @param responseCallback callback that will be called when response is received
     * @param errorListener callback that will be called when error occurs
     */
    public void postTeamJoin(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "teams/" + teamId + "/join", null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };
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
                (Request.Method.GET, url + "tasks", null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }


    public void getTasks(Long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "tasks?gameId=" + gameId, null, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

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
                (Request.Method.POST, url + "answers", json, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

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
                (Request.Method.POST, url + "tasks", json, responseCallback, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

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
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

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
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void getAnswers(long gameId, Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url + "answers?gameId=" + gameId + "&filter=" + (filterUnchecked ? "unchecked" : "all"),
                        null, responseCallback,
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void getAnswer(long answerId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url + "answers/" + answerId, null, responseCallback,
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

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

    public void patchAnswer(long answerId, Boolean approved, Response.Listener<JSONObject> responseCallback,
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
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Send POST request to create a new Game. At least one game needs to exist on server side for
     * the app to work properly.
     *
     * @param responseCallback what should be done with response data when it arrives
     * @param errorListener    what should be done with any errors when they occur
     */
    public void postGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", game.getName());
            json.put("startTime", game.getStartTime());
            json.put("endCondition", game.getEndCondition());

            if (game.getEndCondition() == Game.EndCondition.TIME) {
                json.put("endTime", game.getEndTime());
            } else if(game.getEndCondition() == Game.EndCondition.SCORE) {
                json.put("endScore", game.getEndScore());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url + "games", json, responseCallback,
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

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
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void getGame(long gameId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url + "games/" + gameId, null, responseCallback,
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void patchGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener){
        JSONObject json = new JSONObject();
        try {
            json.put("name", game.getName());
            json.put("startTime", game.getStartTime());
            json.put("endCondition", game.getEndCondition());

            if (game.getEndCondition() == Game.EndCondition.TIME) {
                json.put("endTime", game.getEndTime());
            } else if(game.getEndCondition() == Game.EndCondition.SCORE) {
                json.put("endScore", game.getEndScore());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, url + "games/" + game.getId(), json, responseCallback,
                        errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionToken);
                return headers;
            }
        };

    }


}
