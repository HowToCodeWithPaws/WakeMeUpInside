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
        modes.add("цвет против часовой стрелки");
        modes.add("радуга");

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


        CheckBox BAlampCheckbox = findViewById(R.id.before_alarm_lamp_checkbox);
        CheckBox curtainsOpenCheckbox = findViewById(R.id.curtains_open_check);
        CheckBox curtainsCloseCheckbox = findViewById(R.id.curtains_close_check);

        Spinner BAlampSpinner = findViewById(R.id.before_alarm_lamp_spinner);
        ArrayAdapter adapterBA =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, before_alarm_modes);
        adapterBA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BAlampSpinner.setAdapter(adapterBA);
        BAlampSpinner.setOnItemSelectedListener(this);

        Spinner RPhoneSpinner = findViewById(R.id.phone_repeat_spinner_time);
        RPhoneSpinner.setAdapter(adapterBA);
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
        ba_lamp_time = ba_lamp = curtains_close_t = curtains_open_t = curtains_close = curtains_open = lightlevel=0;

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
            func = -1;
            Log.d("Режим","Выбрали дефолт");
        } else if (position == 1) {
            func = 0;
            Log.d("Режим","Выбрали по часовой");
        } else if (position == 2) {
            func = 1;
            Log.d("Режим","Выбрали против часовой");
        } else if (position == 3) {
            func = 2;
            Log.d("Режим","Выбрали радугу");
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
        r_phone_time = 5;
        Log.d("Телефон","Выбрали 5");
    } else if (position == 2) {
        r_phone_time = 10;
        Log.d("Телефон","Выбрали 10");
    } else if (position == 3) {
        r_phone_time = 15;
        Log.d("Телефон","Выбрали 15");
    }else if (position == 4) {
        r_phone_time = 20;
        Log.d("Телефон","Выбрали 20");
    }else if (position == 5) {
        r_phone_time = 25;
        Log.d("Телефон","Выбрали 25");
    }else if (position == 6) {
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
        if((Integer)parent.getId() ==  2131230992){
            selectLampMode(position);
        }else if((Integer)parent.getId() == 2131230809){
           selectLampTime(position);
        }else if((Integer)parent.getId() == 2131296600){
            selectPhoneTime(position);
        }else if((Integer)parent.getId() == 2131296599){
            selectPhoneMax(position);
        }else if((Integer)parent.getId() == 2131296405){
            selectCurtainsOpen(position);
        }else if((Integer)parent.getId() == 2131296401){
            selectCurtainsClose(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
