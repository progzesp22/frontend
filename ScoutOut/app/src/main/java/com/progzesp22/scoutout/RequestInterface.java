package com.progzesp22.scoutout;

import com.android.volley.Response;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.Team;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RequestInterface {
    long GAME_ID = 2;

    void setSessionToken(String sessionToken);

    void postUserLogin(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener);

    void postRegister(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener);

    void getTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void getTeams(long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void getJoinedTeams(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void postTeams(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void getTeamInfo(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void patchTeam(Team team, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void postTeamJoin(long teamId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void getTasks(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void getTasks(Long gameId, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void postAnswer(Answer answer, Response.Listener<JSONObject> responseCallback,
                    Response.ErrorListener errorListener);

    void postTask(Task task, Response.Listener<JSONObject> responseCallback,
                  Response.ErrorListener errorListener);

    void patchTask(Task task, Response.Listener<JSONObject> responseCallback,
                   Response.ErrorListener errorListener);

    void getAnswers(Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void getAnswers(long gameId, Boolean filterUnchecked, Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void getAnswer(long answerId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void patchAnswer(Answer answer, Response.Listener<JSONObject> responseCallback,
                     Response.ErrorListener errorListener);

    void postGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void getGames(Response.Listener<JSONArray> responseCallback, Response.ErrorListener errorListener);

    void getGame(long gameId, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);

    void patchGame(Game game, Response.Listener<JSONObject> responseCallback, Response.ErrorListener errorListener);
}
