package il.co.myapp.tickets.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;

import il.co.myapp.tickets.activities.ContactLawyerActivity;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.controller.NetworkController;
import il.co.myapp.tickets.data.AsyncLeadResponse;
import il.co.myapp.tickets.data.URLS;
import il.co.myapp.tickets.utils.ParseNetworkError;

/*      Lead Class:
        * Each lead object has:
        * (1) name
        * (2) phone
        * (3) email
        */
public class Lead {

    private String name;
    private String phone;
    private String email;

    private static final String TAG = Lead.class.getSimpleName();

    public Lead(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }


    public Lead() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void submitLead(final Context applicationContext, final AsyncLeadResponse asyncLeadResponse) {
        // Request a string response from the provided URL.
        /** JsonObjectResponse : HTTP Request where the response is a JSONObject
         *  (1) Request Method POST : Means Put this --> [  new JSONObject(getLeadParams()) ] on Server Side.
         *  This object contains the submitted Lead HashMap of parameters.
         *  (2) Providing the URL Address -   ADD_LEAD_URL
         *  (3) Convention Standard : Provide to the Server a JSONObject with 2 Params:
         *   A) getLoginParams() : A Method that returns a Map of the specific Lead member values.
         *                         In this Lead.Java , the map is stored KEY-VALUE <String,String>
         *   B) Response.Listener<JSONObject>() : CallBack invoked as a Successful
         *   Response of getting the Lead object in Server.
         *
         *   @Override
        public void onResponse(JSONObject response) this method is invoked on a Success of LoginParams() JSONObject
        [this means the Login info/ User data --> JSON Object Data has been successfully received and "PUT" on Server Side.
        Returns a String Token - Indicator of Successful "PUT" Operation.

        C) Response.ErrorListener() : CallBack invoked on Failure
        Through the ParseNetworkError, GetErrorMessage will retrieve the error.
        */
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                URLS.ADD_LEAD_URL, new JSONObject(getLeadParams()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v(TAG, "Success");
                // proceed with a "Success" Response from lead - input success to CallBack method NewLeadResponseRecieved.
                // this method is executed only if the response is positive - i.e. lead has been succesfully submitted in Server.

                if (null != asyncLeadResponse) asyncLeadResponse.NewLeadResponseReceived("Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorResponse = ParseNetworkError.GetErrorMessage(error);
                Toast.makeText(applicationContext, errorResponse, Toast.LENGTH_LONG).show();
                // proceed with a "Error" Response from lead - input error to CallBack method NewLeadResponseRecieved.
                // this method is executed only if the response is negative - i.e. lead has been unsuccesfully submitted in Server.

                if (null != asyncLeadResponse)
                    asyncLeadResponse.NewLeadResponseReceived(errorResponse);
            }
        });

        // Finally add this JsonObjectRequest to the NetworkController's RequestQueue.

        NetworkController.getInstance(applicationContext).addToRequestQueue(loginRequest);
    }

    /**
     * This method returns a HashMap<String,String> - LeadParameters. This hashMap will be sent as a JSONObject
     * to the firebase server when submitting a new lead.
     * (1) Key : name          value : getName()
     * (2) Key : phone         value : getPhone()
     * (3) Key : email         value : getEmail()
     */
    private HashMap<String, String> getLeadParams() {
        HashMap<String, String> leadParams = new HashMap<>();
        leadParams.put("name", getName());
        leadParams.put("phone", getPhone());
        leadParams.put("email", getEmail());
        return leadParams;
    }
}
