/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2018 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by all applicable intellectual property
 * laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package com.adobe.busbooking;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adobe.marketing.mobile.Audience;
import com.adobe.marketing.mobile.MobileCore;

import java.util.HashMap;
import java.util.Map;


//import com.adobe.marketing.mobile.MobileCore;

/**
 * This is the second screen for testing of multiple screens scenario
 */
public class SecondActivity extends AppCompatActivity {

    private Map<String, String> digitalData = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        setUpToolBar();

        MobileCore.trackState("Home screen", digitalData);
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitle("Second Screen");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        digitalData.put("busBooking.page.name", "Booking Details");
        trackProduct(digitalData);
    }

    private void trackProduct(Map<String, String> digitalData) {
        Map<String, String> product = new HashMap<>();
        product.put("id", "24D334");
        product.put("name", "Chartered Bus");
        product.put("category", "Volvo A/C Multi Axle (2+2)");
        product.put("quantity", "3");
        product.put("totalPrice", "1900");

        digitalData.put("&&products", getAnalyticsProductString(product.get("id"), product.get("name"), product.get("category"), product.get("quantity"), product.get("totalPrice")));
        digitalData.put("&&events", "prodView");
    }

    @NonNull
    private String getAnalyticsProductString(String id, String name, String category, String quantity, String totalValue) {
        return String.format("%s;%s;%s;%s;%s", id, name, category, quantity, totalValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

