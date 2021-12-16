#include <Adafruit_NeoPixel.h>
#include <WiFiEsp.h>

#include "aREST.h"

// Emulate Serial1 on pins 6/7 if not present
#ifndef HAVE_HWSERIAL1
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7);  // RX, TX
#endif

#define PIN 5
#define LED_NUMBER 16  // number of LEDs in the LED strip; can be 8!
#define FORWARD 1
#define REVERSE 2
#define LIGHT_RES_PIN A0

char* ssid = "diplo";         // your network SSID (name)
char* pass = "otdimasika";    // your network password
int status = WL_IDLE_STATUS;  // the Wifi radio's status

WiFiEspServer server(80);
aREST rest = aREST();
Adafruit_NeoPixel strip = Adafruit_NeoPixel(LED_NUMBER, PIN, NEO_RGB + NEO_KHZ800);

int setStripColor(String command);

void handle(WiFiEspClient& client) {
  if(client.available()) {
    // Handle request
    rest.handle_proto(client, true, 0, true);

    // Answer
    rest.sendBuffer(client, 0, 0);
    client.stop();

    // Reset variables for the next command
    rest.reset_status();
  }
}

void setup() {
  pinMode(LIGHT_RES_PIN, INPUT);
  Serial.begin(9600);
  Serial.println("Arduino Strip RGB Led");
  strip.begin();

  Serial1.begin(9600);
  WiFi.init(&Serial1);
  WiFi.begin(ssid, pass);
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("You're connected to the network");
  Serial.println(WiFi.localIP());
  server.begin();
  rest.function("fill", setStripColor);
  // rest.variable("temperature", &temperature); что.,,,¿ как¿¿¿ для этого нету доков....
}

void loop() {
  WiFiEspClient client = server.available();
  handle(client);
}

// mode: 5
void rainbow(int wait, int direction) {
  int first, last;
  setDirection(&first, &last, direction);
  byte color[3];
  byte count, a0, a1, a2;
  for(int i = 0; i < 10; i++) {
    color[count] = random(256);
    a0 = count + random(1) + 1;
    color[a0 % 3] = random(256 - color[count]);
    color[(a0 + 1) % 3] = 255 - color[a0 % 3] - color[count];
    count += random(15);  // to avoid repeating patterns
    count %= 3;
    fillStrip(strip.Color(color[0], color[1], color[2]), wait, direction);
    if(direction == FORWARD)
      clearStrip(wait, REVERSE);
    else
      clearStrip(wait, FORWARD);
  }
}

// mode: unused
void blinkNTimes(uint32_t color, int wait, int N) {
  for(int i = 0; i < N; ++i) {
    strip.fill(color);
    strip.show();
    delay(wait);
    strip.clear();
    strip.show();
    delay(wait);
  }
}

// mode: 3
void transfusion(int wait) {
  int colors[] = {255, 0, 0};
  int descending = 0;
  for(int i = 0; i < 764; ++i) {
    strip.fill(strip.Color(colors[0], colors[1], colors[2]));
    strip.show();
    if(colors[descending] == 0)
      descending = (descending + 1) % 3;
    --colors[descending];
    ++colors[(descending + 1) % 3];
    delay(wait);
  }
}

// mode: 4
void mildBlinkNTimes(uint32_t color, int wait, int N) {
  uint8_t r = (uint8_t)(color >> 16), g = (uint8_t)(color >> 8), b = (uint8_t)color;
  for(int i = 0; i < N; ++i) {
    for(int j = 0; j < 256; ++j) {
      strip.fill(strip.Color(((float)r) * j / 255, ((float)g) * j / 255, ((float)b) * j / 255));
      strip.show();
      delay(wait);
    }
    for(int j = 255; j >= 0; --j) {
      strip.fill(strip.Color(((float)r) * j / 255, ((float)g) * j / 255, ((float)b) * j / 255));
      strip.show();
      delay(wait);
    }
  }
}

// mode: 0
void intensityUprise(uint32_t color, int wait) {
  uint8_t r = (uint8_t)(color >> 16), g = (uint8_t)(color >> 8), b = (uint8_t)color;
  for(int j = 0; j < 256; ++j) {
    strip.fill(strip.Color(((float)r) * j / 255, ((float)g) * j / 255, ((float)b) * j / 255));
    strip.show();
    delay(wait);
  }
}

// mode: 1, 2
void mildFillStrip(uint32_t color, int wait, int leds_num, int direction) {
  uint8_t r = (uint8_t)(color >> 16), g = (uint8_t)(color >> 8), b = (uint8_t)color;
  int first, last;
  setDirection(&first, &last, direction);
  int center = first;
  int part = 255 / leds_num;
  int brightnesses[LED_NUMBER];
  for(int i = 0; i < LED_NUMBER; ++i)
    brightnesses[i] = 0;
  while(center != last || brightnesses[last - 1] > 0) {
    if(brightnesses[center] == 255) {
      ++center;
    }
    if(center < last) {
      for(int j = center; j < min(center + leds_num, last); ++j) {
        if(j == center || brightnesses[j - 1] >= part) {
          ++brightnesses[j];
          strip.setPixelColor(j, ((float)r) * brightnesses[j] / 255, ((float)g) * brightnesses[j] / 255, ((float)b) * brightnesses[j] / 255);
        }
      }
    }
    for(int j = center - 1; j >= first && brightnesses[j] > 0; --j) {
      --brightnesses[j];
      strip.setPixelColor(j, ((float)r) * brightnesses[j] / 255, ((float)g) * brightnesses[j] / 255, ((float)b) * brightnesses[j] / 255);
    }
    strip.show();
    delay(wait);
  }
}

void fillStrip(uint32_t color, int wait, int direction) {
  int first, last;
  setDirection(&first, &last, direction);
  for(int p = first; p <= last; p++) {
    strip.setPixelColor(abs(p), color);
    strip.show();
    delay(wait);
  }
}

void clearStrip(int wait, int direction) {
  int first, last;
  setDirection(&first, &last, direction);
  for(int p = first; p <= last; p++) {
    strip.setPixelColor(abs(p), 0);
    strip.show();
    delay(wait);
  }
}

void setDirection(int* first, int* last, int direction) {
  if(direction == FORWARD) {
    *first = 0;
    *last = LED_NUMBER;
  } else {
    *first = -LED_NUMBER;
    *last = 0;
  }
}

// restapi: /fill?params=<String command>
// char 1: mode 0-5
// char 2: direction l/r
// char 3: delay 0-9 -> 0-900
// char 4: number 0-9
// char 5-...: color
int setStripColor(String command) {
  uint8_t mode = command[0] - '0';
  uint8_t direction = command[1] == 'r' ? REVERSE : FORWARD;
  uint8_t delayx100 = command[2] - '0';
  uint8_t num = command[3] - '0';
  uint32_t color = strtol(&command[4], NULL, 16);

  int delay = delayx100 * 100;

  switch(mode) {
    case 0:
      intensityUprise(color, delay);
      break;
    case 1:
    case 2:
      mildFillStrip(color, delay, num, direction);
      break;
    case 3:
      transfusion(delay);
      break;
    case 4:
      mildBlinkNTimes(color, delay, num);
      break;
    case 5:
      rainbow(delay, direction);
      break;
  }

  return 1;
}
