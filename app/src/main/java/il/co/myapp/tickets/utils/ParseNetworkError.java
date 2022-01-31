package il.co.myapp.tickets.utils;

import com.android.volley.VolleyError;

public class ParseNetworkError {

    /**
     *
     * This class converts the VolleyError recieved into a string. It takes the VolleyError JASONObject's
     * networkResponse.data - message body & converts it into a String.
     */
    public static final String GetErrorMessage(VolleyError error) {
        String body;
        //final String statusCode = String.valueOf(error.networkResponse.statusCode);
        try {
            body = new String(error.networkResponse.data, "UTF-8");
            return body;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
