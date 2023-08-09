package com.rockmining.dogecoin;

import static com.google.android.material.color.utilities.MaterialDynamicColors.onError;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.atikulsoftware.metaadslibrary.MetaAds.AdsUnit;
import com.atikulsoftware.metaadslibrary.MetaAds.FacebookAds;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.rockmining.dogecoin.viewmodel.InternetConnectedViewModel;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String ONESIGNAL_APP_ID = "407e5b7e-8d8e-4c0c-b239-a7c7586ed275";

    private final String TAG = MainActivity.class.getSimpleName();

    // creating NativeAdLayout object

    private NativeAdLayout nativeAdLayout;

    // creating  LinearLayout object

    private LinearLayout adView;

    // creating  NativeAd object

    private NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InternetConnectedViewModel internetConnectedViewModel = new ViewModelProvider(this).get(InternetConnectedViewModel.class);

        internetConnectedViewModel.getConnected().observe(this, aBoolean -> {
            if (!aBoolean) {
                Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.promptForPushNotifications();

        AdsUnit.BANNER = getString(R.string.fb_banner_id);

        FacebookAds.loadInterstitial(MainActivity.this);
        FacebookAds.setBanner(findViewById(R.id.fb_banner_container), MainActivity.this);

// initializing the Audience Network SDK

        AudienceNetworkAds.initialize(this);



        // initializing nativeAd object

        nativeAd = new NativeAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");

        // creating  NativeAdListener

        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override

            public void onMediaDownloaded(Ad ad) {

                // showing Toast message



            }

            @Override

            public void onError(Ad ad, AdError adError) {

                // showing Toast message



            }

            @Override

            public void onAdLoaded(Ad ad) {

                // showing Toast message



                if (nativeAd == null || nativeAd != ad) {

                    return;

                }

                // Inflate Native Ad into Container

                inflateAd(nativeAd);

            }

            @Override

            public void onAdClicked(Ad ad) {

                // showing Toast message



            }

            @Override

            public void onLoggingImpression(Ad ad) {

                // showing Toast message



            }

        };

        // Load an ad

        nativeAd.loadAd(

                nativeAd.buildLoadAdConfig()

                        .withAdListener(nativeAdListener)

                        .build());

        // initializing the Audience Network SDK

        AudienceNetworkAds.initialize(this);
    }


    // inflating the Ad

    void inflateAd(NativeAd nativeAd) {

        // unregister the native Ad View

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.

        nativeAdLayout = findViewById(R.id.native_ad_container);

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        // Inflate the Ad view.

        adView = (LinearLayout) inflater.inflate(R.layout.fan_native_ad_layout, nativeAdLayout, false);

        // adding view

        nativeAdLayout.addView(adView);

        // Add the AdOptionsView

        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);

        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);

        adChoicesContainer.removeAllViews();

        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.

        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);

        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);

        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);

        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);

        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);

        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);

        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Setting  the Text.

        nativeAdTitle.setText(nativeAd.getAdvertiserName());

        nativeAdBody.setText(nativeAd.getAdBodyText());

        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());

        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);

        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views

        List<View> clickableViews = new ArrayList<>();

        clickableViews.add(nativeAdTitle);

        clickableViews.add(nativeAdCallToAction);

        // Register the Title and  button to listen for clicks.

        nativeAd.registerViewForInteraction(

                adView, nativeAdMedia, nativeAdIcon, clickableViews);

    }

}
