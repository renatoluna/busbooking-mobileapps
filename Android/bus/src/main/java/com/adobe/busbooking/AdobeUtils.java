package com.adobe.busbooking;

import android.support.annotation.NonNull;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.TargetParameters;
import com.adobe.marketing.mobile.TargetRequest;
import com.adobe.marketing.mobile.VisitorID;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdobeUtils {
    private static Map<String, String> digitalData = new HashMap<String, String>();
    private static String DATA_SOURCE_IDENTIFIER = "lunaID";
    private static String APP_PREFIX = "busBooking";
    private String mboxName;

    public static void syncUserIdentifiers(String authState, String userID) {
        addData("user", "authState", authState);
        addData("user", "profile", userID);
        Identity.syncIdentifier(DATA_SOURCE_IDENTIFIER, userID, (VisitorID.AuthenticationState) getAuthenticationState(authState));
    }

    private static Enum getAuthenticationState(String authState) {
        Enum authenticationState;
        switch (authState) {
            case "authenticated":
                authenticationState = VisitorID.AuthenticationState.AUTHENTICATED;
                break;
            case "logged_out":
                authenticationState = VisitorID.AuthenticationState.LOGGED_OUT;
                break;
            default:
                authenticationState = VisitorID.AuthenticationState.UNKNOWN;
                break;
        }
        return authenticationState;
    }

    public static void init () {
        cleanDigitalData();
        addData("app", "name", "Bus Booking");
        addData("app", "tech", getAppTech());
    }

    private static String getAppTech() {
        Map<String, String> extensions = new HashMap<>();
        extensions.put("Core", MobileCore.extensionVersion());
        extensions.put("AA", Analytics.extensionVersion());
        extensions.put("AT", Target.extensionVersion());
        extensions.put("ECID", Identity.extensionVersion());
        return getAnalyticsStringFromMap(extensions);
    }

    @NonNull
    private static String getAnalyticsStringFromMap(Map<String, String> map) {
        Joiner.MapJoiner appTech = Joiner.on(":").withKeyValueSeparator("|");
        return appTech.join(map);
    }

    private static void cleanDigitalData() {
        digitalData = new HashMap<String, String>();
    }

    public static void addData(String context, String attribute, String value) {
        String key = String.format("%s.%s.%s", APP_PREFIX, context, attribute);
        digitalData.put(key, value);
    }

    public String getMboxName() {
        return mboxName;
    }

    public void setMboxName(String mboxName) {
        this.mboxName = mboxName;
    }

    public void trackView(String stateName) {
        List<String> mboxList = new ArrayList<>();
        mboxList.add(this.mboxName);
        Target.locationsDisplayed(mboxList, null);
        MobileCore.trackState(stateName, digitalData);
    }

    public void getExperience(String defaultContent, TargetParameters targetParameters, AdobeCallback callback) {
        TargetRequest request = new TargetRequest(this.mboxName, targetParameters, defaultContent, callback);

        List<TargetRequest> locationRequests = new ArrayList<>();
        locationRequests.add(request);
        Target.retrieveLocationContent(locationRequests, null);
    }
}
