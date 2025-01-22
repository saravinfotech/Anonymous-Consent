package com.codelab.anonymousanalytics;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.EnumMap;
import java.util.Map;

public class ConsentModeTestActivity extends AppCompatActivity {

    private static final String TAG = "ConsentModeTest";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);

        // Set default consent to denied
        configureAdvancedConsentMode(false);

        // Log example events to test consent settings
        logExampleEvents();
    }

    private void configureAdvancedConsentMode(boolean consentGranted) {
        Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> consentMap =
                new EnumMap<>(FirebaseAnalytics.ConsentType.class);

       /* FirebaseAnalytics.ConsentStatus status = consentGranted ?
                FirebaseAnalytics.ConsentStatus.GRANTED :
                FirebaseAnalytics.ConsentStatus.DENIED;
*/
        consentMap.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE, FirebaseAnalytics.ConsentStatus.DENIED);
        consentMap.put(FirebaseAnalytics.ConsentType.AD_STORAGE, FirebaseAnalytics.ConsentStatus.DENIED);
        consentMap.put(FirebaseAnalytics.ConsentType.AD_USER_DATA, FirebaseAnalytics.ConsentStatus.DENIED);
        consentMap.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION, FirebaseAnalytics.ConsentStatus.DENIED);

        mFirebaseAnalytics.setConsent(consentMap);

        for (FirebaseAnalytics.ConsentType consentType : consentMap.keySet()) {
            Log.d(TAG, "Consent Mode: " + consentType.name() + " -> " + consentMap.get(consentType).name());
        }
    }

    private void logExampleEvents() {
        // Custom Events - [GA4] Custom events - https://support.google.com/analytics/answer/9234069
        logCustomEvent("event_one", "param_key1", "param_value1");
        logCustomEvent("event_two", "param_key2", "param_value2");
        logCustomEvent("event_three", "param_key3", "param_value3");

        // eCommerce Events - [GA4] Recommended events - https://support.google.com/analytics/answer/9216061
        logAddToCartEvent();
        logPurchaseEvent();
        logBeginCheckoutEvent();

        // App Lifecycle Events - [GA4] Automatically collected events - https://support.google.com/analytics/answer/12229021
        logAppOpenEvent();
        logScreenViewEvent("HomeScreen");

        // Content Engagement Events - Recommended events - https://support.google.com/analytics/answer/9267735
        logSelectContentEvent();
        logViewItemEvent();
        logViewItemListEvent();

        // User Behavior Events - [GA4] Enhanced measurement events - https://support.google.com/analytics/table/13594742
        logSearchEvent();
        logShareEvent();
        logTutorialCompleteEvent();

        logUserID();
    }

    private void logUserID() {
        mFirebaseAnalytics.setUserId("test_user_id");
        mFirebaseAnalytics.setUserProperty("user_type", "premium");
    }

    /**
     * Logs a custom event.
     * [GA4] Custom events
     * Source: https://support.google.com/analytics/answer/9234069
     */
    private void logCustomEvent(String eventName, String paramKey, String paramValue) {
        Bundle params = new Bundle();
        params.putString(paramKey, paramValue);
        mFirebaseAnalytics.logEvent(eventName, params);

        Log.d(TAG, "Custom event '" + eventName + "' logged with parameters: " + params);
    }

    /**
     * Logs the 'add_to_cart' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/answer/9216061
     */
    private void logAddToCartEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "sku12345");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Example Product");
        params.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
        params.putDouble(FirebaseAnalytics.Param.VALUE, 29.99);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, params);

        Log.d(TAG, "Event 'add_to_cart' logged with parameters: " + params);
    }

    /**
     * Logs the 'purchase' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/answer/9216061
     */
    private void logPurchaseEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "T123456");
        params.putString(FirebaseAnalytics.Param.AFFILIATION, "Example Store");
        params.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
        params.putDouble(FirebaseAnalytics.Param.VALUE, 59.99);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, params);

        Log.d(TAG, "Event 'purchase' logged with parameters: " + params);
    }

    /**
     * Logs the 'begin_checkout' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/answer/9216061
     */
    private void logBeginCheckoutEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
        params.putDouble(FirebaseAnalytics.Param.VALUE, 99.99);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, params);

        Log.d(TAG, "Event 'begin_checkout' logged with parameters: " + params);
    }

    /**
     * Logs the 'app_open' event.
     * [GA4] Automatically collected events
     * Source: https://support.google.com/analytics/answer/12229021
     */
    private void logAppOpenEvent() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
        Log.d(TAG, "Event 'app_open' logged.");
    }

    /**
     * Logs the 'screen_view' event.
     * [GA4] Automatically collected events
     * Source: https://support.google.com/analytics/answer/12229021
     */
    private void logScreenViewEvent(String screenName) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params);

        Log.d(TAG, "Event 'screen_view' logged for screen: " + screenName);
    }

    /**
     * Logs the 'select_content' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/answer/9267735
     */
    private void logSelectContentEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "34343434334");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Example Content");
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);

        Log.d(TAG, "Event 'select_content' logged with parameters: " + params);
    }

    /**
     * Logs the 'view_item' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/answer/9267735
     */
    private void logViewItemEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "item123");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sample Item");
        params.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
        params.putDouble(FirebaseAnalytics.Param.VALUE, 10.99);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);

        Log.d(TAG, "Event 'view_item' logged with parameters: " + params);
    }

    /**
     * Logs the 'view_item_list' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/answer/9267735
     */
    private void logViewItemListEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Sample Item List");
        params.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "list123");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, params);

        Log.d(TAG, "Event 'view_item_list' logged with parameters: " + params);
    }

    /**
     * Logs the 'search' event.
     * [GA4] Enhanced measurement events
     * Source: https://support.google.com/analytics/table/13594742
     */
    private void logSearchEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, "Firebase Analytics");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, params);

        Log.d(TAG, "Event 'search' logged with parameters: " + params);
    }

    /**
     * Logs the 'share' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/table/13594742
     */
    private void logShareEvent() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "article");
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "123456");
        params.putString(FirebaseAnalytics.Param.METHOD, "social_media");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, params);

        Log.d(TAG, "Event 'share' logged with parameters: " + params);
    }

    /**
     * Logs the 'tutorial_complete' event.
     * [GA4] Recommended events
     * Source: https://support.google.com/analytics/table/13594742
     */
    private void logTutorialCompleteEvent() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
        Log.d(TAG, "Event 'tutorial_complete' logged.");
    }
}
