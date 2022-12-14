package com.progzesp22.scoutout;

import android.util.Log;

import com.android.volley.Response;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MockRequestHandler implements RequestInterface {
    private static final String TAG = "MockRequestHandler";


    @Override
    public void setSessionToken(String sessionToken) {
        Log.d(TAG, "Set session token " + sessionToken);
    }

    @Override
    public void postUserLogin(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /user/login/ %s %s", username, password));
        try {
            listener.onResponse(new JSONObject("{\n" +
                    "    \"sessionToken\": \"abcdefghjijfhrsnfnrnsfwnjfnjeffwfewfw\",\n" +
                    "    \"validUntil\": \"1997-07-16T19:20:30.45+01:00\"\n" +
                    "}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postRegister(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /user/register/ %s %s", username, password));
        try {
            listener.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET /teams");
        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\"id\": 1, \"gameId\": 2, \"name\": \"Mundo's Overlords\"},\n" +
                    "    {\"id\": 3, \"gameId\": 2, \"name\": \"Janna's Owls\"},\n" +
                    "    {\"id\": 5, \"gameId\": 6, \"name\": \"Teemo's Scouts\"}\n" +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTeams(long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("GET /teams?gameId=%d", gameId));

        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\"id\": 1, \"gameId\": 2, \"name\": \"Mundo's Overlords\"},\n" +
                    "    {\"id\": 3, \"gameId\": 2, \"name\": \"Janna's Owls\"}\n" +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getJoinedTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET /teams?filter=joined");

        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\"id\": 1, \"gameId\": 2, \"name\": \"Mundo's Overlords\"},\n" +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postTeams(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /teams payload: %s", team.toString()));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTeamInfo(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("GET /teams/%d", teamId));
        try {
            responseCallback.onResponse(new JSONObject("{\n" +
                    "    \"id\": 1,\n" +
                    "    \"gameId\": 2,\n" +
                    "    \"name\": \"Mundo's Overlords\",\n" +
                    "    \"creator\": \"misos\",\n" +
                    "    \"members\": [\"misos\", \"XGregory\", \"husiek\"]\n" +
                    "}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void patchTeam(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("PATCH /teams/<id> payload: %s", team));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postTeamJoin(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /teams/%d/join", teamId));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTasks(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET /tasks");
        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"name\": \"Example Task 1\",\n" +
                    "        \"description\": \"Example description 1\",\n" +
                    "        \"gameId\": 1,\n" +
                    "        \"type\": \"TEXT\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 2,\n" +
                    "        \"name\": \"Example Task 2\",\n" +
                    "        \"description\": \"Example description 2\",\n" +
                    "        \"gameId\": 1,\n" +
                    "        \"type\": \"PHOTO\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 3,\n" +
                    "        \"name\": \"Example Task 3\",\n" +
                    "        \"description\": \"Example description 3\",\n" +
                    "        \"gameId\": 1,\n" +
                    "        \"type\": \"QR_CODE\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 4,\n" +
                    "        \"name\": \"Example Task 4\",\n" +
                    "        \"description\": \"Example description 4\",\n" +
                    "        \"gameId\": 2,\n" +
                    "        \"type\": \"NAV_POS\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 5,\n" +
                    "        \"name\": \"Example Task 5\",\n" +
                    "        \"description\": \"Example description 5\",\n" +
                    "        \"gameId\": 2,\n" +
                    "        \"type\": \"AUDIO\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    }\n" +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTasks(Long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("GET /tasks?gameId=%d", gameId));
        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"name\": \"Example Task 1\",\n" +
                    "        \"description\": \"Example description 1\",\n" +
                    "        \"gameId\": 1,\n" +
                    "        \"type\": \"TEXT\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 2,\n" +
                    "        \"name\": \"Example Task 2\",\n" +
                    "        \"description\": \"Example description 2\",\n" +
                    "        \"gameId\": 1,\n" +
                    "        \"type\": \"PHOTO\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 3,\n" +
                    "        \"name\": \"Example Task 3\",\n" +
                    "        \"description\": \"Example description 3\",\n" +
                    "        \"gameId\": 1,\n" +
                    "        \"type\": \"QR_CODE\",\n" +
                    "        \"prerequisiteTasks\":[]\n" +
                    "    }\n" +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void postAnswer(long taskId, String response, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /answers  %d %s", taskId, response));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postTask(Task task, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /tasks  payload: %s", task.toString()));
        Log.d(TAG, task.getDescription());
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void patchTask(Task task, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("PATCH /tasks  payload: %s", task.toString()));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAnswers(Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET /answers");
        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\"id\": 2138420, \"taskId\": 1, \"userId\": 1, \"approved\": false, \"checked\": false},\n" +
                    "    {\"id\": 13231232, \"taskId\": 2, \"userId\": 2, \"approved\": false, \"checked\": false},\n" +
                    "    {\"id\": 13893212, \"taskId\": 3, \"userId\": 3, \"approved\": false, \"checked\": false},\n" +
                    "    {\"id\": 721387420, \"taskId\": 1, \"userId\": 5, \"approved\": false, \"checked\": true},\n" +
                    "    {\"id\": 521387420, \"taskId\": 3, \"userId\": 5, \"approved\": true, \"checked\": true}]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAnswers(long gameId, Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET /answers");
        try {
            responseCallback.onResponse(new JSONArray("[\n" +
                    "    {\"id\": 2138420, \"taskId\": 1, \"userId\": 1, \"approved\": false, \"checked\": false},\n" +
                    "    {\"id\": 13231232, \"taskId\": 2, \"userId\": 2, \"approved\": false, \"checked\": false},\n" +
                    "    {\"id\": 13893212, \"taskId\": 3, \"userId\": 3, \"approved\": false, \"checked\": false},\n" +
                    "    {\"id\": 721387420, \"taskId\": 1, \"userId\": 5, \"approved\": false, \"checked\": true},\n" +
                    "    {\"id\": 521387420, \"taskId\": 3, \"userId\": 5, \"approved\": true, \"checked\": true}]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAnswer(long answerId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("GET /answers/%d", answerId));
        try {
            responseCallback.onResponse(new JSONObject("{\"id\": 2138420, \"taskId\": 1, \"userId\": 1, \"response\":\"fhshjjsdkjfdsnjfnj fssnfknsndkfnskfsd sfnsnf\", \"approved\":false, \"checked\": false}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void patchAnswer(long answerId, Boolean approved, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("PATCH /answers/%d approved: %d", answerId, approved));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("POST /games payload: %s", game.toString()));
        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGames(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET /games");

        try {
            responseCallback.onResponse(new JSONArray("[" +
                    "{\"id\": 1234, \"name\": \"Bring back season 4\", \"gameMaster\": \"kris\", \"state\": \"CREATED\"}," +
                    "{\"id\": 1235, \"name\": \"Zbieram szyszki\", \"gameMaster\": \"misos\", \"state\": \"PENDING\"}," +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGame(long gameId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("GET /games/%d", gameId));

        try {
            responseCallback.onResponse(new JSONObject("{\n" +
                    "    \"id\": 1234,\n" +
                    "    \"name\": \"Bring back season 4\",\n" +
                    "    \"gameMaster\": \"misos\",\n" +
                    "    \"startTime\": \"1997-07-16T19:20:30.45+01:00\",\n" +
                    "    \"endCondition\": \"TIME\",\n" +
                    "    \"endTime\": \"1997-07-18T19:20:30.45+01:00\",\n" +
                    "    \"state\": \"CREATED\"\n" +
                    "}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void patchGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener) {
        Log.d(TAG, String.format("PATCH /games %s", game.toString()));

        try {
            responseCallback.onResponse(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
