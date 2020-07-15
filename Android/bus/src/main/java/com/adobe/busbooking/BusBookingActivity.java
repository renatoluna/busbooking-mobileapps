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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.TargetParameters;
import com.adobe.marketing.mobile.TargetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity class is responsible to show booking engine page and offer card.
 */
public class BusBookingActivity extends AppCompatActivity {

    private TextView mTextGoingTo, mTextLeavingFrom, mTextSource, mTextDestination;
    private Button btn_find_buses;
    private ImageButton mBtnFlip;
    private CheckBox chk_premium_only;
    private AdobeUtils adobe = new AdobeUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adobe.init();
        adobe.syncUserIdentifiers("authenticated", "euhv2x83tq");
        adobe.setMboxName("mbox-home-page");

        setContentView(R.layout.activity_bus_booking);
        setUpToolBar();
        mTextGoingTo = findViewById(R.id.text_going_to);
        mTextLeavingFrom = findViewById(R.id.text_leaving_from);
        mTextDestination = findViewById(R.id.text_destination);
        mTextSource = findViewById(R.id.text_source);
        mBtnFlip = findViewById(R.id.btn_flip);

        mBtnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipSourceDesti();
            }
        });


        findViewById(R.id.btn_find_buses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });


        findViewById(R.id.rel_offer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusBookingActivity.this, OfferDetailsActivity.class));
            }
        });

        findViewById(R.id.fragOffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusBookingActivity.this, SampleFragmentActivity.class));
            }
        });

        setSource("San Francisco");
        setDest("Mexico");

        try {
            adobe.getExperience(getDefaultContent(), null, personalize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getDefaultContent() throws JSONException {
        JSONObject defaultContent = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("color", "#000000");
        defaultContent.put("header",  header);

        JSONObject button = new JSONObject();
        button.put("text", "Get bus");
        button.put("textColor", "#000000");
        button.put("backgroundColor", "#4CAF50");
        defaultContent.put("button",  header);

        defaultContent.put("premium",  "false");

        JSONObject location = new JSONObject();
        button.put("country", "USA");
        button.put("state", "California");
        button.put("city", "San Francisco");
        defaultContent.put("location",  location);

        defaultContent.put("source", "San Francisco");
        defaultContent.put("destination", "Las Vegas");
        return defaultContent.toString();
    }

    private AdobeCallback personalize = new AdobeCallback() {
        @Override
        public void call(Object response) {
            try {
                JSONObject experience = new JSONObject((String) response);
                chk_premium_only = findViewById(R.id.chk_premium_only);
                chk_premium_only.setChecked(Boolean.valueOf((String) experience.get("premium")));
                adobe.addData("page", "search.nonstop", (String) experience.get("premium"));

                setSourceText((String) experience.get("source"), R.color.black_opac);
                setDestinationText((String) experience.get("destination"), R.color.black_opac);

                JSONObject button = (JSONObject) experience.get("button");
                btn_find_buses = findViewById(R.id.btn_find_buses);
                btn_find_buses.setText((String) button.get("text"));
                btn_find_buses.setBackgroundColor(Color.parseColor((String) button.get("backgroundColor")));

                adobe.trackView("Home Screen");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void setUpToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        toolbar.setTitle("Bus Booking");
        adobe.addData("page", "toolbar", (String) toolbar.getTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void flipSourceDesti() {

        Animation animClockWise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anin_rotating_50_clockwise);

        final Animation aniAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_rotate_50_anti_clockwise);

        aniAntiClockWise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnFlip.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animClockWise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mBtnFlip.setVisibility(View.INVISIBLE);
                mBtnFlip.startAnimation(aniAntiClockWise);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnFlip.startAnimation(animClockWise);
        startAnimation();

    }

    /**
     * Switch animation
     */
    private void startAnimation() {

        Animation forLeavingFromIn = AnimationUtils.loadAnimation(this, R.anim.left_to_right_in);
        Animation forGoingToIn = AnimationUtils.loadAnimation(this, R.anim.right_to_left_in);

        final Animation forLeavingFromOut = AnimationUtils.loadAnimation(this, R.anim.left_to_right_out);
        final Animation forGoingToOut = AnimationUtils.loadAnimation(this, R.anim.right_to_left_out);

        forLeavingFromIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateCities();
                mTextSource.startAnimation(forLeavingFromOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        forGoingToIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextDestination.startAnimation(forGoingToOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        mTextSource.startAnimation(forLeavingFromIn);
        mTextDestination.startAnimation(forGoingToIn);
    }

    /**
     * Interchange
     */
    private void updateCities() {
        String oldSource = mTextSource.getText().toString().trim();
        setSourceText(mTextDestination.getText().toString().trim(), R.color.black_opac);
        setDestinationText(oldSource, R.color.black_opac);
    }


    private void setDest(String city) {
        mTextGoingTo.setVisibility(View.VISIBLE);
        setDestinationText(city, R.color.black_opac);
    }


    private void setSource(String city) {
        mTextLeavingFrom.setVisibility(View.VISIBLE);
        setSourceText(city, R.color.black_opac);
    }

    private void setSourceText(String city, Integer color) {
        setFieldText(city, color, (TextView) findViewById(R.id.text_source), "source");
    }

    private void setDestinationText(String city, Integer color) {
        setFieldText(city, color, (TextView) findViewById(R.id.text_destination), "destination");
    }

    private void setFieldText(String text, Integer color, TextView textView, String contextData) {
        textView.setText(capitalize(text));
        textView.setTextColor(ContextCompat.getColor(this, color));
        adobe.addData("page.search", contextData, capitalize(text));
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


    @Override
    protected void onResume() {
        MobileCore.setApplication(getApplication());
        MobileCore.lifecycleStart(null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobileCore.lifecyclePause();
        super.onPause();
    }

    private void showConfirmationDialog() {
        DialogFragment sampleDialogFragment = SampleDialogFragment.getInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag("dialogView");
        if (prevFragment != null) {
            fragmentTransaction.remove(prevFragment);
        }
        fragmentTransaction.addToBackStack(null);
        sampleDialogFragment.show(fragmentTransaction, "dialogView");
    }

}
