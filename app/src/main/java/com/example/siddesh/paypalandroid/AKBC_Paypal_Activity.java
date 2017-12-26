package com.example.siddesh.paypalandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.util.Collections;


/**
 * Created by Siddesh on 06-11-2017.
 */

public class AKBC_Paypal_Activity extends AppCompatActivity implements PaymentMethodNonceCreatedListener
        ,BraintreeErrorListener ,BraintreeCancelListener {

    private String mAuthorization = "";
    protected BraintreeFragment mBraintreeFragment;
    ProgressDialog pDialog_tokenization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.paypal_activity);
        InitPaypal();
    }

    private void InitPaypal(){
        try {

            mAuthorization = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIyZWJjZDYzMzliNGRjNTBlNDJhYjFmNTY0MmYyOTc4NzYwMGI0YzFkYThhYWVhOWE1ZmFhOTFiOWUxZGNmZDAxfGNsaWVudF9pZD1jbGllbnRfaWQkcHJvZHVjdGlvbiRkdHh0bWgzeGhxeHoyOTU3XHUwMDI2Y3JlYXRlZF9hdD0yMDE3LTExLTA4VDA4OjA4OjQ5LjI5ODU0NTYyNCswMDAwXHUwMDI2bWVyY2hhbnRfaWQ9cGM3Z3Z3cDRzZDk3aDRxcyIsImNvbmZpZ1VybCI6Imh0dHBzOi8vYXBpLmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvcGM3Z3Z3cDRzZDk3aDRxcy9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJwcm9kdWN0aW9uIiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuYnJhaW50cmVlZ2F0ZXdheS5jb206NDQzL21lcmNoYW50cy9wYzdndndwNHNkOTdoNHFzL2NsaWVudF9hcGkiLCJhc3NldHNVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImF1dGhVcmwiOiJodHRwczovL2F1dGgudmVubW8uY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5icmFpbnRyZWVnYXRld2F5LmNvbS9wYzdndndwNHNkOTdoNHFzIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOmZhbHNlLCJwYXlwYWxFbmFibGVkIjp0cnVlLCJwYXlwYWwiOnsiZGlzcGxheU5hbWUiOiJBS0JBUiBPTkxJTkUgQk9PS0lORyBDT01QQU5ZIFBWVCBMVEQiLCJjbGllbnRJZCI6IkFiM1dkQW0tNVgxMndRQkxPdkhWOGZ3Sk45dE9naWE5OTJLRkRxaVFBNHhpdUEtOUxaWkY1cllTalhrZ19DMjhUeVFGc1hUTkJzZ0htYXdxIiwicHJpdmFjeVVybCI6Imh0dHBzOi8vZXhhbXBsZS5jb20iLCJ1c2VyQWdyZWVtZW50VXJsIjoiaHR0cHM6Ly9leGFtcGxlLmNvbSIsImJhc2VVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFzc2V0c1VybCI6Imh0dHBzOi8vY2hlY2tvdXQucGF5cGFsLmNvbSIsImRpcmVjdEJhc2VVcmwiOm51bGwsImFsbG93SHR0cCI6ZmFsc2UsImVudmlyb25tZW50Tm9OZXR3b3JrIjpmYWxzZSwiZW52aXJvbm1lbnQiOiJsaXZlIiwidW52ZXR0ZWRNZXJjaGFudCI6ZmFsc2UsImJyYWludHJlZUNsaWVudElkIjoiQVJLcllSRGgzQUdYRHpXN3NPXzNiU2txLVUxQzdIR191V05DLXo1N0xqWVNETlVPU2FPdElhOXE2VnBXIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6IlVTRCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoicGM3Z3Z3cDRzZDk3aDRxcyIsInZlbm1vIjoib2ZmIn0=";
            mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);
            PayPal.requestOneTimePayment(mBraintreeFragment, getPayPalRequest("1.00"));
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            // There was an issue with your authorization string.
        }
    }


    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {

        // Send this nonce to your server
        String nonce = paymentMethodNonce.getNonce();
        Toast.makeText(this, "Nonce :-" + nonce, Toast.LENGTH_SHORT).show();

    }

    private PayPalRequest getPayPalRequest(@Nullable String amount) {
        PayPalRequest request = new PayPalRequest(amount);

        request.displayName(Settings.getPayPalDisplayName(this));

        String landingPageType = Settings.getPayPalLandingPageType(this);
        if (getString(R.string.paypal_landing_page_type_billing).equals(landingPageType)) {
            request.landingPageType(PayPalRequest.LANDING_PAGE_TYPE_BILLING);
        } else if (getString(R.string.paypal_landing_page_type_login).equals(landingPageType)) {
            request.landingPageType(PayPalRequest.LANDING_PAGE_TYPE_LOGIN);
        }

        String intentType = Settings.getPayPalIntentType(this);
        if (intentType.equals(getString(R.string.paypal_intent_authorize))) {
            request.intent(PayPalRequest.INTENT_AUTHORIZE);
        } else if (intentType.equals(getString(R.string.paypal_intent_order))) {
            request.intent(PayPalRequest.INTENT_ORDER);
        } else if (intentType.equals(getString(R.string.paypal_intent_sale))) {
            request.intent(PayPalRequest.INTENT_SALE);
        }

        if (Settings.isPayPalUseractionCommitEnabled(this)) {
            request.userAction(PayPalRequest.USER_ACTION_COMMIT);
        }

        if (Settings.isPayPalCreditOffered(this)) {
            request.offerCredit(true);
        }

        return request;
    }

  /*  @Override
    public void onConfigurationFetched(Configuration configuration) {
        if (Settings.shouldCollectDeviceData(this)) {
            DataCollector.collectDeviceData(mBraintreeFragment, new BraintreeResponseListener<String>() {
                @Override
                public void onResponse(String deviceData) {
                    mDeviceData = deviceData;
                }
            });
        }
    }*/

    @Override
    public void onError(Exception error) {

        Toast.makeText(this, "Error:-"+ error.getMessage(), Toast.LENGTH_LONG).show();

        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;

            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                  //  setErrorMessage(expirationMonthError.getMessage());
                    Toast.makeText(this, "Error:-"+ expirationMonthError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCancel(int requestCode) {
        // Use this to handle a canceled activity, if the given requestCode is important.
        // You may want to use this callback to hide loading indicators, and prepare your UI for input
        Toast.makeText(this, "Transaction Cancelled RequestCode :- "+ requestCode,Toast.LENGTH_LONG);
    }
}
