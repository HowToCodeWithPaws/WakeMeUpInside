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
    private static String baseUrl = ""; //change it to the Raspberry Pi3 IP


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toolbar topbar = this.findViewById(R.id.top_bar);
        topbar.setOnMenuItemClickListener((item)->{
            if(item.getItemId() ==  R.id.lamp){
                LampActivity.baseUrl = baseUrl;
                Intent intent = new Intent(this, LampActivity.class);
                startActivity(intent);
              //  finish();
            }
            return true;
        });

        TimePickerDialog.OnTimeSetListener tpmMo = (view, hourOfDay, minute) -> mMo = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpmTu = (view, hourOfDay, minute) -> mTu = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpmWe = (view, hourOfDay, minute) -> mWe = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpmFr = (view, hourOfDay, minute) -> mFr = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpmSa = (view, hourOfDay, minute) -> mSa = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpmSu = (view, hourOfDay, minute) -> mSu = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeMo = (view, hourOfDay, minute) -> eMo = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeTu = (view, hourOfDay, minute) -> eTu = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeWe = (view, hourOfDay, minute) -> eWe = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeTh = (view, hourOfDay, minute) -> eTh = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeFr = (view, hourOfDay, minute) -> eFr = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeSa = (view, hourOfDay, minute) -> eSa = new Pair(hourOfDay, minute);

        TimePickerDialog.OnTimeSetListener tpeSu = (view, hourOfDay, minute) -> eSu = new Pair(hourOfDay, minute);

        Button mMo_button = this.findViewById(R.id.mMo_button);
        mMo_button.setOnClickListener(  (View v) -> {
            new TimePickerDialog(MainActivity.this, tpmMo,
                    Calendar.HOUR_OF_DAY,
                    Calendar.MINUTE, true)
                    .show();
        });

        Button eMo_button = this.findViewById(R.id.eMo_button);
        eMo_button.setOnClickListener(  (View v) -> {
            new TimePickerDialog(MainActivity.this, tpeMo,
                    Calendar.HOUR_OF_DAY,
                    Calendar.MINUTE, true)
                    .show();
        });

        Button save = this.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCall();
            }
        });

        EditText url_input = this.findViewById(R.id.url_input);
        Button url_confirm = this.findViewById(R.id.url_confirm);
        url_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseUrl = url_input.getText().toString();
            }
        });

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

        Log.d("MainActivity", "url " +baseUrl+" mMo"+mMo.first+":"+mMo.second + "eMo"+eMo.first+":"+eMo.second);
//                + "- Blue [" + blue + "] - Dir [" + direction + "] - Delay [" + delVal + "]");

       HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter("MondayMorningHH", String.valueOf(mMo.first))
                .addQueryParameter("MondayMorningMM", String.valueOf(mMo.second))
               .addQueryParameter("MondayEveningHH", String.valueOf(eMo.first))
               .addQueryParameter("MondayEveningMM", String.valueOf(eMo.second));


        Request req = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "Error");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("MainActivity", "Response.." + response.body().string());
            }
        });
    }
}
