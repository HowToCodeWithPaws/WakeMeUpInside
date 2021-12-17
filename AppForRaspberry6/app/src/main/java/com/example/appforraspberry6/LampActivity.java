package com.example.appforraspberry6;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LampActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int red;
    private int green;
    private int blue;
    private int ba_lamp;
    private int ba_lamp_time;
    private int r_phone_time;
    private int r_phone_max;
    private int func;
    private int curtains_close;
    private int curtains_close_t;
    private int curtains_open;
    private int curtains_open_t;
    private int lightlevel;

    private OkHttpClient client;
    static String baseUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Настройки устройств");

        ArrayList<String> modes = new ArrayList<String>();
        modes.add("усиление цвета");
        modes.add("цвет по часовой стрелке");
        modes.add("разноцветное");
        modes.add("градиент");
        modes.add("мигание");

        Spinner spinnerMode = findViewById(R.id.mode_spinner);
        ArrayAdapter adapterMode=
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, modes);
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(adapterMode);
        spinnerMode.setOnItemSelectedListener(this);

        ArrayList<String> before_alarm_modes = new ArrayList<String>();
        before_alarm_modes.add("0");
        before_alarm_modes.add("5");
        before_alarm_modes.add("10");
        before_alarm_modes.add("15");
        before_alarm_modes.add("20");
        before_alarm_modes.add("25");
        before_alarm_modes.add("30");

        ArrayList<String> repeat_phone_modes = new ArrayList<String>();
        repeat_phone_modes.add("0");
        repeat_phone_modes.add("1");
        repeat_phone_modes.add("5");
        repeat_phone_modes.add("10");
        repeat_phone_modes.add("15");
        repeat_phone_modes.add("20");
        repeat_phone_modes.add("25");
        repeat_phone_modes.add("30");


        CheckBox BAlampCheckbox = findViewById(R.id.before_alarm_lamp_checkbox);
        CheckBox curtainsOpenCheckbox = findViewById(R.id.curtains_open_check);
        CheckBox curtainsCloseCheckbox = findViewById(R.id.curtains_close_check);

        Spinner BAlampSpinner = findViewById(R.id.before_alarm_lamp_spinner);
        ArrayAdapter adapterBA =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, before_alarm_modes);
        adapterBA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BAlampSpinner.setAdapter(adapterBA);
        BAlampSpinner.setOnItemSelectedListener(this);


        ArrayAdapter adapterRPhone =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, repeat_phone_modes);
        adapterBA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner RPhoneSpinner = findViewById(R.id.phone_repeat_spinner_time);
        RPhoneSpinner.setAdapter(adapterRPhone);
        RPhoneSpinner.setOnItemSelectedListener(this);

        ArrayList<String> max_alarm = new ArrayList<String>();
        max_alarm.add("0");
        max_alarm.add("1");
        max_alarm.add("2");
        max_alarm.add("3");
        max_alarm.add("4");
        max_alarm.add("5");

        Spinner RPhoneMaxSpinner = findViewById(R.id.phone_repeat_spinner_max);
        ArrayAdapter adapterRMax =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, max_alarm);
        adapterRMax.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RPhoneMaxSpinner.setAdapter(adapterRMax);
        RPhoneMaxSpinner.setOnItemSelectedListener(this);

        BAlampCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ba_lamp = ba_lamp== 0? 1 : 0;
            }
        });

        curtainsCloseCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curtains_close = curtains_close== 0? 1 : 0;
            }
        });

        curtainsOpenCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curtains_open = curtains_open== 0? 1 : 0;
            }
        });

        red = green = blue = func = -1;
        ba_lamp_time = ba_lamp = curtains_close_t = curtains_open_t = curtains_close = curtains_open = 0;
        lightlevel=100;

        Spinner curtainsClose = findViewById(R.id.curtains_close_spinner);
        curtainsClose.setAdapter(adapterBA);
        curtainsClose.setOnItemSelectedListener(this);

        Spinner curtainsOpen = findViewById(R.id.curtains_open_spinner);
        curtainsOpen.setAdapter(adapterBA);
        curtainsOpen.setOnItemSelectedListener(this);

        Button buttonSaveSettings = findViewById(R.id.saveSettings);
        Button buttonSelectColor = findViewById(R.id.selectColor);

        client = new OkHttpClient();

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCall();
            }
        });

        buttonSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChromaDialog.Builder()
                        .initialColor(Color.BLUE)
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new ColorSelectListener() {
                            @Override
                            public void onColorSelected(int color) {
                                Log.d("LampActivity", "Color selected");
                                red = Color.red(color);
                                green = Color.green(color);
                                blue = Color.blue(color);

                                buttonSelectColor.setBackgroundColor(color);
                            }
                        })
                        .create()
                        .show(getSupportFragmentManager(), "dialog");
            }
        });

        SeekBar lightlevel_seek = findViewById(R.id.lightlevel);

        lightlevel_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lightlevel = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void initCall() {

        if (red == -1) {
            Toast.makeText(this, "color is not set, press select a color", Toast.LENGTH_SHORT).show();
            return;
        }

        if (green == -1) {
            Toast.makeText(this, "color is not set, press select a color", Toast.LENGTH_SHORT).show();
            return;
        }

        if (blue == -1) {
            Toast.makeText(this, "color is not set, press select a color", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LampActivity", "Func [" + func + "] - Red [" + red + "] - Green [" + green + "] "
                + "- Blue [" + blue + "] - BAL ["+ba_lamp+"] - BATL ["+ba_lamp_time+"] " +
                "- RTP ["+r_phone_time+"] - RMP ["+r_phone_max+"] - CO ["+curtains_open+"] - COT ["+curtains_open_t+"]"+
                "- CC ["+curtains_close+"] - CCT ["+curtains_close_t+"] - LL ["+lightlevel+"]");



//        mode - режим лампы
//                - -1 - выбранный цвет с увеличением интенсивности
//        - 0 - выбранный цвет по часовой стрелке
//        - 1 - выбранный цвет против часовой стрелки
//        - 2 - радуга
//
//        red
//        green селф экспланатори
//                blue
//
//        lamp_ba
//                - 0 - лампа не включается утром
//                - 1 - лампа включается утром
//
//        lamp_bat - лампа включается за
//        - 0 - 0 минут до будильника утром
//        - 5 - 5 минут до будильника утром
//        - 10 - 10 минут до будильника утром
//        - 15 - 15 минут до будильника утром
//        - 20 - 20 минут до будильника утром
//        - 25 - 25 минут до будильника утром
//        - 30 - 30 минут до будильника утром
//
//        phone_rt - будильник повторяется каждые
//        - 0 - 0 минут
//                - 5 - 5 минут
//                - 10 - 10 минут
//                - 15 - 15 минут
//                - 20 - 20 минут
//                - 25 - 25 минут
//                - 30 - 30 минут
//
//        phone_rm - будильник повторяется максимум
//        - 0 - 0 раз
//                - 1 - 1 раз
//                - 2 - 2 раза
//                - 3 - 3 раза
//                - 4 - 4 раза
//                - 5 - 5 раз
//
//                curtains_o
//        - 0 - шторы не открываются утром
//                - 1 - шторы открываются утром
//
//        curtains_ot шторы открываются за
//        - 0 - 0 минут до будильника утром
//        - 5 - 5 минут до будильника утром
//        - 10 - 10 минут до будильника утром
//        - 15 - 15 минут до будильника утром
//        - 20 - 20 минут до будильника утром
//        - 25 - 25 минут до будильника утром
//        - 30 - 30 минут до будильника утром
//
//        curtains_c
//                - 0 - шторы не закрываются вечером
//                - 1 - шторы закрываются вечером
//
//        curtains_ct шторы закрываются за
//        - 0 - 0 минут до будильника вечером
//        - 5 - 5 минут до будильника вечером
//        - 10 - 10 минут до будильника вечером
//        - 15 - 15 минут до будильника вечером
//        - 20 - 20 минут до будильника вечером
//        - 25 - 25 минут до будильника вечером
//        - 30 - 30 минут до будильника вечером
//
//        lightlevel
//        число показывающее уровень света при <= котором мы утром включаем лампу

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter("mode", String.valueOf(func))
                .addQueryParameter("red", String.valueOf(red))
                .addQueryParameter("green", String.valueOf(green))
                .addQueryParameter("blue", String.valueOf(blue))
                .addQueryParameter("lamp_ba", String.valueOf(ba_lamp))
                .addQueryParameter("lamp_bat", String.valueOf(ba_lamp_time))
                .addQueryParameter("phone_rt", String.valueOf(r_phone_time))
                .addQueryParameter("phone_rm", String.valueOf(r_phone_max))
                .addQueryParameter("curtains_o", String.valueOf(curtains_open))
                .addQueryParameter("curtains_ot", String.valueOf(curtains_open_t))
                .addQueryParameter("curtains_c", String.valueOf(curtains_close))
                .addQueryParameter("curtains_ct", String.valueOf(curtains_close_t))
                .addQueryParameter("lightlevel", String.valueOf(lightlevel));

        Request req = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LampActivity", "Error");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", "Response.." + response.body().string());
            }
        });
    }

    public void selectLampMode(int position){
        if (position == 0) {
            func = 0;
            Log.d("Режим","Выбрали дефолт");
        } else if (position == 1) {
            func = 1;
            Log.d("Режим","Выбрали по часовой");
        } else if (position == 2) {
            func = 2;
            Log.d("Режим","Выбрали разноцветное");
        } else if (position == 3) {
            func = 3;
            Log.d("Режим","Выбрали градиент");
        }else if (position == 4) {
            func = 4;
            Log.d("Режим","Выбрали мигание");
        }
    }

    public void selectLampTime(int position){
        if (position == 0) {
            ba_lamp_time = 0;
            Log.d("Часы","Выбрали 0");
        } else if (position == 1) {
            ba_lamp_time = 5;
            Log.d("Часы","Выбрали 5");
        } else if (position == 2) {
            ba_lamp_time = 10;
            Log.d("Часы","Выбрали 10");
        } else if (position == 3) {
            ba_lamp_time = 15;
            Log.d("Часы","Выбрали 15");
        }else if (position == 4) {
            ba_lamp_time = 20;
            Log.d("Часы","Выбрали 20");
        }else if (position == 5) {
            ba_lamp_time = 25;
            Log.d("Часы","Выбрали 25");
        }else if (position == 6) {
            ba_lamp_time = 30;
            Log.d("Часы","Выбрали 30");
        }
    }

    public void selectCurtainsOpen(int position){
        if (position == 0) {
            curtains_open_t = 0;
            Log.d("шторы","Выбрали 0");
        } else if (position == 1) {
            curtains_open_t = 5;
            Log.d("шторы","Выбрали 5");
        } else if (position == 2) {
            curtains_open_t = 10;
            Log.d("шторы","Выбрали 10");
        } else if (position == 3) {
            curtains_open_t = 15;
            Log.d("шторы","Выбрали 15");
        }else if (position == 4) {
            curtains_open_t = 20;
            Log.d("шторы","Выбрали 20");
        }else if (position == 5) {
            curtains_open_t = 25;
            Log.d("шторы","Выбрали 25");
        }else if (position == 6) {
            curtains_open_t = 30;
            Log.d("шторы","Выбрали 30");
        }
    }
    public void selectCurtainsClose(int position){
        if (position == 0) {
            curtains_close_t = 0;
            Log.d("шторы","Выбрали 0");
        } else if (position == 1) {
            curtains_close_t = 5;
            Log.d("шторы","Выбрали 5");
        } else if (position == 2) {
            curtains_close_t = 10;
            Log.d("шторы","Выбрали 10");
        } else if (position == 3) {
            curtains_close_t = 15;
            Log.d("шторы","Выбрали 15");
        }else if (position == 4) {
            curtains_close_t = 20;
            Log.d("шторы","Выбрали 20");
        }else if (position == 5) {
            curtains_close_t = 25;
            Log.d("шторы","Выбрали 25");
        }else if (position == 6) {
            curtains_close_t = 30;
            Log.d("шторы","Выбрали 30");
        }
    }

    public void selectPhoneTime(int position){
        if (position == 0) {
        r_phone_time = 0;
        Log.d("Телефон","Выбрали 0");
    } else if (position == 1) {
        r_phone_time = 1;
        Log.d("Телефон","Выбрали 1");
    } else if (position == 2) {
        r_phone_time = 5;
        Log.d("Телефон","Выбрали 5");
    } else if (position == 3) {
        r_phone_time = 10;
        Log.d("Телефон","Выбрали 10");
    }else if (position == 4) {
        r_phone_time = 15;
        Log.d("Телефон","Выбрали 15");
    }else if (position == 5) {
        r_phone_time = 20;
        Log.d("Телефон","Выбрали 20");
    }else if (position == 6) {
        r_phone_time = 25;
        Log.d("Телефон","Выбрали 25");
    }else if (position == 7) {
            r_phone_time = 30;
            Log.d("Телефон","Выбрали 30");
        }}
    public void selectPhoneMax(int position){
        if (position == 0) {
            r_phone_max = 0;
            Log.d("Телефон max","Выбрали 0");
        } else if (position == 1) {
            r_phone_max = 1;
            Log.d("Телефон max","Выбрали 1");
        } else if (position == 2) {
            r_phone_max = 2;
            Log.d("Телефон max","Выбрали 2");
        } else if (position == 3) {
            r_phone_max = 3;
            Log.d("Телефон max","Выбрали 3");
        }else if (position == 4) {
            r_phone_max = 4;
            Log.d("Телефон max","Выбрали 4");
        }else if (position == 5) {
            r_phone_max = 5;
            Log.d("Телефон max","Выбрали 5");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if((Integer)parent.getId() ==  R.id.mode_spinner){
            selectLampMode(position);
        }else if((Integer)parent.getId() == R.id.before_alarm_lamp_spinner){
           selectLampTime(position);
        }else if((Integer)parent.getId() == R.id.phone_repeat_spinner_time){
            selectPhoneTime(position);
        }else if((Integer)parent.getId() == R.id.phone_repeat_spinner_max){
            selectPhoneMax(position);
        }else if((Integer)parent.getId() == R.id.curtains_open_spinner){
            selectCurtainsOpen(position);
        }else if((Integer)parent.getId() == R.id.curtains_close_spinner){
            selectCurtainsClose(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
