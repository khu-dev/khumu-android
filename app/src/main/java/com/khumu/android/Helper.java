package com.khumu.android;
//import com.khumu.android.Helper;
import java.util.Scanner;

public class Helper {
    static String APIProtocol = "http";
    static String APIHost = "192.168.219.254";
    static String APIPort = "8000";
    static String APISubPathForRoot = "/api";
    public static String APIRootEndpoint;

    static void Init(){
        APIRootEndpoint = APIProtocol + "://" + APIHost + ":" + APIPort + APISubPathForRoot + "/";
        System.out.println(APIRootEndpoint);
    }
}
