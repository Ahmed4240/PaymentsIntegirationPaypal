package com.example.ahmed.paymentsintegirationpaypal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView id , ammount , status ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        id = (TextView)findViewById(R.id.txtId);
        ammount = (TextView)findViewById(R.id.txtAmmount);
        status = (TextView)findViewById(R.id.txtStatus);


        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("paymentsDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("payment_ammount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String payment_ammount)
    {
        try {
            id.setText(response.getString("id"));
            ammount.setText(response.getString(String.format("$&s",payment_ammount)));
            status.setText(response.getString("state"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
