package com.example.exwebservices;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends ActionBarActivity {

    private final String NAMESPACE = "http://www.w3schools.com/webservices/";
    private final String URL ="http://www.w3schools.com/webservices/tempconvert.asmx";
    private final String SOAP_ACTION="http://www.w3schools.com/webservices/CelsiusToFahrenheit";
    private final String METHOD_NAME = "CelsiusToFahrenheit";
    private String TAG = "PGGURU";
    private static String celcius;
    private static String fahren;
    Button b;
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Celcius Edit Control
        et = (EditText) findViewById(R.id.editText1);
        //Fahrenheit Text control
        tv = (TextView) findViewById(R.id.tv_result);
        //Button to trigger web service invocation
        b = (Button) findViewById(R.id.button1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().length() != 0 && et.getText().toString() != ""){//Check if Celcius text is not empty
                    celcius = et.getText().toString(); //Get the text control value
                    AsyncCallWS task = new AsyncCallWS(); //Create instance for AsyncCallWS
                    task.execute(); //Call execute
                } else {//If text control is empty
                    tv.setText("Please enter Celcius");
                }
            }
        });
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getFahrenheit(celcius);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            tv.setText(fahren + "° F");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            tv.setText("Calculating...");
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }

    public void getFahrenheit(String celsius) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //Create request
        PropertyInfo celsiusPI = new PropertyInfo(); //Property which holds input parameters
        celsiusPI.setName("Celsius"); //Set Name
        celsiusPI.setValue(celsius); //Set Value
        celsiusPI.setType(double.class); //Set dataType
        request.addProperty(celsiusPI); //Add the property to request object
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Envelope
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request); //Set output SOAP object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL); //Create HTTP call object
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope); //Invole web service
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse(); //Get the response
            fahren = response.toString(); //Assign it to fahren static variable
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
