package com.example.appforraspberry6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Pair<Integer, Integer> mMo = new Pair(-1, -1);
    private Pair<Integer, Integer> mTu= new Pair(-1, -1);
    private Pair<Integer, Integer> mWe= new Pair(-1, -1);
    private Pair<Integer, Integer> mTh= new Pair(-1, -1);
    private Pair<Integer, Integer> mFr= new Pair(-1, -1);
    private Pair<Integer, Integer> mSa= new Pair(-1, -1);
    private Pair<Integer, Integer> mSu= new Pair(-1, -1);
    private Pair<Integer, Integer> eMo= new Pair(-1, -1);
    private Pair<Integer, Integer> eTu= new Pair(-1, -1);
    private Pair<Integer, Integer> eWe= new Pair(-1, -1);
    private Pair<Integer, Integer> eTh= new Pair(-1, -1);
    private Pair<Integer, Integer> eFr= new Pair(-1, -1);
    private Pair<Integer, Integer> eSa= new Pair(-1, -1);
    private Pair<Integer, Integer> eSu= new Pair(-1, -1);

    private OkHttpClient client;
    private static String baseUrl = "";
    private static String lampIp = "";
    private static String motorIp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toolbar topbar = this.findViewById(R.id.top_bar);
        topbar.setOnMenuItemClickListener((item)->{
            if(item.getItemId() == R.id.lamp){
                if (baseUrl == "") {
                    Toast.makeText(this, "you have to set url", Toast.LENGTH_SHORT).show();
                }//else if(lampIp == ""){
                   // Toast.makeText(this, "you have to set lamp ip", Toast.LENGTH_SHORT).show();

             //   }else if(motorIp==""){
               //     Toast.makeText(this, "you have to set motor ip", Toast.LENGTH_SHORT).show();

            //    }
            else{
                    LampActivity.baseUrl = baseUrl;
                    Intent intent = new Intent(this, LampActivity.class);
                    startActivity(intent);
// finish();
                }
            }
            return true;
        });

        Button mMo_button = this.findViewById(R.id.mMo_button);
        Button eMo_button = this.findViewById(R.id.eMo_button);
        Button mTu_button = this.findViewById(R.id.mTu_button);
        Button eTu_button = this.findViewById(R.id.eTu_button);
        Button mWe_button = this.findViewById(R.id.mWe_button);
        Button eWe_button = this.findViewById(R.id.eWe_button);
        Button mTh_button = this.findViewById(R.id.mTh_button);
        Button eTh_button = this.findViewById(R.id.eTh_button);
        Button mFr_button = this.findViewById(R.id.mFr_button);
        Button eFr_button = this.findViewById(R.id.eFr_button);
        Button mSa_button = this.findViewById(R.id.mSa_button);
        Button eSa_button = this.findViewById(R.id.eSa_button);
        Button mSu_button = this.findViewById(R.id.mSu_button);
        Button eSu_button = this.findViewById(R.id.eSu_button);

        TimePickerDialog.OnTimeSetListener tpmMo =
                (view, hourOfDay, minute) -> {
                    mMo = new Pair(hourOfDay, minute);
                    String formatString = getString(R.string.time_string);
                    String output = String.format(formatString, mMo.first, mMo.second);
                    mMo_button.setText(output );
                };

        TimePickerDialog.OnTimeSetListener tpmTu = (view, hourOfDay, minute) -> {
            mTu = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, mTu.first, mTu.second);
            mTu_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpmWe = (view, hourOfDay, minute) -> {
            mWe = new
                    Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, mWe.first, mWe.second);
            mWe_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpmTh = (view, hourOfDay, minute) -> {
            mTh = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, mTh.first, mTh.second);
            mTh_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpmFr = (view, hourOfDay, minute) -> {
            mFr = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, mFr.first, mFr.second);
            mFr_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpmSa = (view, hourOfDay, minute) -> {
            mSa = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, mSa.first, mSa.second);
            mSa_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpmSu = (view, hourOfDay, minute) -> {
            mSu = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, mSu.first, mSu.second);
            mSu_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeMo = (view, hourOfDay, minute) -> {
            eMo = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eMo.first, eMo.second);
            eMo_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeTu = (view, hourOfDay, minute) -> {
            eTu = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eTu.first, eTu.second);
            eTu_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeWe = (view, hourOfDay, minute) -> {
            eWe = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eWe.first, eWe.second);
            eWe_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeTh = (view, hourOfDay, minute) -> {
            eTh = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eTh.first, eTh.second);
            eTh_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeFr = (view, hourOfDay, minute) -> {
            eFr = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eFr.first, eFr.second);
            eFr_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeSa = (view, hourOfDay, minute) -> {
            eSa = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eSa.first, eSa.second);
            eSa_button.setText(output );};

        TimePickerDialog.OnTimeSetListener tpeSu = (view, hourOfDay, minute) -> {
            eSu = new Pair(hourOfDay, minute);
            String formatString = getString(R.string.time_string);
            String output = String.format(formatString, eSu.first, eSu.second);
            eSu_button.setText(output );};


        mMo_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmMo,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eMo_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeMo,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        mTu_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmTu,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eTu_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeTu,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        mWe_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmWe,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eWe_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeWe,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        mTh_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmTh,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eTh_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeTh,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        mFr_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmFr,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eFr_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeFr,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        mSa_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmSa,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eSa_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeSa,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        mSu_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpmSu,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        eSu_button.setOnClickListener( (View v) -> new TimePickerDialog(MainActivity.this, tpeSu,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE, true)
                .show());

        Button save = this.findViewById(R.id.save);
        save.setOnClickListener(v -> initCall());

        EditText url_input = this.findViewById(R.id.url_input);
        Button url_confirm = this.findViewById(R.id.url_confirm);
        url_confirm.setOnClickListener(v -> baseUrl = url_input.getText().toString());

        EditText lamp_ip_input = this.findViewById(R.id.lamp_ip_input);
        Button lamp_ip_confirm = this.findViewById(R.id.lamp_ip_confirm);
        lamp_ip_confirm.setOnClickListener(v -> lampIp = lamp_ip_input.getText().toString());

        EditText motor_ip_input = this.findViewById(R.id.motor_ip_input);
        Button motor_ip_confirm = this.findViewById(R.id.motor_ip_confirm);
        motor_ip_confirm.setOnClickListener(v -> motorIp = motor_ip_input.getText().toString());

// Init OkHTTP
        client = new OkHttpClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//MenuInflater menuInflater = getMenuInflater();
/// menuInflater.inflate(R.menu.top_showcase_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() > 0) {

            if (item.getItemId() == android.R.id.home) {
                finish();
            }
        }
        return true;
    }

    public void initCall() {

        if (baseUrl == "") {
            Toast.makeText(this, "you have to set url", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("MainActivity", "url " +baseUrl+" mMo"+mMo.first+":"+mMo.second + "eMo"+eMo.first+":"+eMo.second);
// + "- Blue [" + blue + "] - Dir [" + direction + "] - Delay [" + delVal + "]");

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter("LAMP_IP", lampIp)
                .addQueryParameter("MOTOR_IP", motorIp)
                .addQueryParameter("MondayMorningHH", String.valueOf(mMo.first))
                .addQueryParameter("MondayMorningMM", String.valueOf(mMo.second))
                .addQueryParameter("MondayEveningHH", String.valueOf(eMo.first))
                .addQueryParameter("MondayEveningMM", String.valueOf(eMo.second))
                .addQueryParameter("TuesdayMorningHH", String.valueOf(mTu.first))
                .addQueryParameter("TuesdayMorningMM", String.valueOf(mTu.second))
                .addQueryParameter("TuesdayEveningHH", String.valueOf(eTu.first))
                .addQueryParameter("TuesdayEveningMM", String.valueOf(eTu.second))
                .addQueryParameter("WednesdayMorningHH", String.valueOf(mWe.first))
                .addQueryParameter("WednesdayMorningMM", String.valueOf(mWe.second))
                .addQueryParameter("WednesdayEveningHH", String.valueOf(eWe.first))
                .addQueryParameter("WednesdayEveningMM", String.valueOf(eWe.second))
                .addQueryParameter("ThursdayMorningHH", String.valueOf(mTh.first))
                .addQueryParameter("ThursdayMorningMM", String.valueOf(mTh.second))
                .addQueryParameter("ThursdayEveningHH", String.valueOf(eTh.first))
                .addQueryParameter("ThursdayEveningMM", String.valueOf(eTh.second))
                .addQueryParameter("FridayMorningHH", String.valueOf(mFr.first))
                .addQueryParameter("FridayMorningMM", String.valueOf(mFr.second))
                .addQueryParameter("FridayEveningHH", String.valueOf(eFr.first))
                .addQueryParameter("FridayEveningMM", String.valueOf(eFr.second))
                .addQueryParameter("SaturdayMorningHH", String.valueOf(mSa.first))
                .addQueryParameter("SaturdayMorningMM", String.valueOf(mSa.second))
                .addQueryParameter("SaturdayEveningHH", String.valueOf(eSa.first))
                .addQueryParameter("SaturdayEveningMM", String.valueOf(eSa.second))
                .addQueryParameter("SundayMorningHH", String.valueOf(mSu.first))
                .addQueryParameter("SundayMorningMM", String.valueOf(mSu.second))
                .addQueryParameter("SundayEveningHH", String.valueOf(eSu.first))
                .addQueryParameter("SundayEveningMM", String.valueOf(eSu.second));


        Request req = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "Error");
// Toast.makeText("something went wrong with url connection, try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("MainActivity", "Response.." + response.body().string());
            }
        });
    }
}