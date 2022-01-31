package il.co.myapp.tickets.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.controller.NetworkController;
import il.co.myapp.tickets.data.AsyncLoginResponse;
import il.co.myapp.tickets.data.URLS;
import il.co.myapp.tickets.utils.ParseNetworkError;

/**
 * User Class:
 * Each user object has:
 * (1) email
 * (2) name
 * (3) accessToken
 * (4) loginType
 * FACEBOOK,GOOGLE - 2 final Types for login.
 */
public class User {

    private String email;
    private String name;
    private String accessToken;
    private String loginType;

    private static final String TAG = User.class.getSimpleName();

    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";


    public User() {

    }

    public User(String name, String email) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Input Params : (1) Context context , (2) AsyncLoginResponse loginCallBack */

    // Request a string response from the provided URL.
    /** JsonObjectResponse : HTTP Request where the response is a JSONObject
     * (1) Request Method POST : Means put this --> [new JSONObject(getLoginParams())] on Server Side. Object with login detail
     * (2) URLS.SOCIAL_LOGIN_URL : the exact location where to put the JSONObject containing LoginParams.
     * A) getLoginParams() : A Method that returns a Map of the specific User member values.
     *                         In this User.Java , the map is stored KEY-VALUE <String,String>
     * B) Response.Listener<JSONObject>() : CallBack invoked on Success .
     *
     *  @Override public void onResponse(JSONObject response) this method is invoked on a Success of LoginParams() JSONObject
    [this means the Login info/ User data --> JSON Object Data has been successfully received and "PUT" on Server Side.
    Returns a String Token - Indicator of Successful "PUT" Operation.

    C) Response.ErrorListener() : CallBack invoked on Failure
    Through the ParseNetworkError, GetErrorMessage will retrieve the error.
     *
     *
     */
    public void preformRegister(Context context, final AsyncLoginResponse loginCallBack) {
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                URLS.SOCIAL_LOGIN_URL, new JSONObject(getLoginParams()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // set the AppController's token to be the token given by the response from server.
                    AppController.getInstance().set_token(response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Return to the loginCallBack method (response : "Success" , status : 200). This method will be called
                // with these params after recieving the JSONObject response.
                if (null != loginCallBack) loginCallBack.LoginResponseReceived("Success", 200);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorResponse = ParseNetworkError.GetErrorMessage(error);
                if (null != loginCallBack)
                    loginCallBack.LoginResponseReceived(errorResponse, error.networkResponse.statusCode);
            }
        });

        NetworkController.getInstance(context).addToRequestQueue(loginRequest);

    }

    /**
     * This method returns a HashMap<String,String> - LoginParameters.
     * (1) Key : name          value : getName()
     * (2) Key : access_token  value : getAccessToken()
     * (3) Key : email         value : getEmail()
     * (4) Key : pushToken     value : Firebase.getToken()
     * (5) Key : loginType     value : getLoginType()
     */
    private HashMap<String, String> getLoginParams() {
        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("name", getName());
        loginParams.put("access_token", getAccessToken());
        loginParams.put("email", getEmail());
        loginParams.put("pushToken", FirebaseInstanceId.getInstance().getToken());
        loginParams.put("loginType", getLoginType());
        return loginParams;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
