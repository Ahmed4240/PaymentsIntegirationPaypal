package com.example.ahmed.paymentsintegirationpaypal;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>");

    EditText ammount;
    String Ammount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        ammount = (EditText)findViewById(R.id.input);

        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });


    }

    private void processPayment()
    {
        Ammount = ammount.getText().toString();

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(Ammount)),"USD","Donate For Pakistan",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);

        startActivityForResult(intent,PAYPAL_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    String paymentDetails = confirm.toJSONObject().toString(4);

                    startActivity(new Intent(this,PaymentDetails.class)
                    .putExtra("paymentsDetails",paymentDetails)
                    .putExtra("payment_ammount",ammount.toString()));

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}

