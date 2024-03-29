package com.example.emicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding the locations of button
        Button cal =(Button)findViewById(R.id.calculate);
        Button reset =(Button)findViewById(R.id.reset);

        //Setting constraints to the RESET Button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText loan =(EditText)findViewById(R.id.loan);
                EditText rate =(EditText)findViewById(R.id.rate);
                EditText time =(EditText)findViewById(R.id.time);
                loan.setText("");
                rate.setText("");
                time.setText("");
            }
        });

        //open another activity
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
    }

    //Calculation part and Creation of Intent for passing values and calling Second Activity
    public void openActivity2() {
        //getext of 3 EditText
        EditText loan = (EditText) findViewById(R.id.loan);
        EditText rate = (EditText) findViewById(R.id.rate);
        EditText time = (EditText) findViewById(R.id.time);

        String l = loan.getText().toString();
        String r = rate.getText().toString();
        String t = time.getText().toString();

        //Checking if the EditText Fields are Empty
        if (TextUtils.isEmpty(l)) {
            loan.setError("Enter Principal Amount");
            loan.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(r)) {
            rate.setError("Enter Interest Rate");
            rate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(t)) {
            time.setError("Enter Time Period");
            time.requestFocus();
            return;
        }

        float p = Float.parseFloat(l);
        float rt = Float.parseFloat(r);
        float tm = Float.parseFloat(t);

        //Checking for Zero values
        if (p == 0 || rt ==0 || tm == 0) {
            Toast.makeText(this, "Zero's are not allowed", Toast.LENGTH_LONG).show();
        } else {

            //Calculation of EMI
            float principal = p;
            float roi = calInt(rt);
            float month = calMonth(tm);
            float numerator = calnumerator(roi, month);
            float denominator = caldenominator(numerator);
            float emi = calres(numerator, denominator, roi, principal);
            float tpi = emi * month;

            //Creating Intent to navigate between 2 Activities--------------
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra("principal", principal + " ");//Passing data to Second Activity along with Key
            intent.putExtra("tpi", tpi + " ");
            intent.putExtra("emi", emi + " ");
            this.startActivity(intent);  //Calling Second Activity
        }
    }

    //Functions used for Calculation

    public float calInt(float r){
        return (float)(r/12/100);
    }

    //Calculating Month
    public float calMonth(float t){
        return (float)(t*12);
    }

    //Numerator Calculation
    public float calnumerator(float roi, float month){
        return (float)(Math.pow(1+roi,month));
    }

    //Denominator Calculation
    public float caldenominator(float numerator){
        return (float)(numerator-1);
    }

    //Final result of EMI
    public float calres(float numerator, float denominator, float roi, float principal){
        return (float)(principal*roi*(numerator/denominator));
    }
}
