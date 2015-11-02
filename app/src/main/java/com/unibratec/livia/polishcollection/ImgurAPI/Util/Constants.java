package com.unibratec.livia.polishcollection.ImgurAPI.Util;

/**
 * Created by Livia on 02/11/2015.
 */
public class Constants {
    /*
  Logging flag
 */
    public static final boolean LOGGING = false;

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "2ce598920411fc3";
    public static final String MY_IMGUR_CLIENT_SECRET = "e22ff4e6ba950fefc2389961cc7d9d785dc067cc";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "http://android";

    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}
