package com.khumu.android;
//import com.khumu.android.Helper;
import java.text.MessageFormat;
import java.util.Scanner;

public class Helper {
    static String APIProtocol = "http";
    static String APIHost = "192.168.219.254";
    static String APIPort = "8000";
    static String APISubPathForRoot = "/api";
    public static String APIRootEndpoint;
    public static String DEFAULT_USERNAME = "admin";
    public static String DEFAULT_PASSWORD = "123123";


    static void Init(){
        APIRootEndpoint = APIProtocol + "://" + APIHost + ":" + APIPort + APISubPathForRoot + "/";
        System.out.println(APIRootEndpoint);
    }
}
