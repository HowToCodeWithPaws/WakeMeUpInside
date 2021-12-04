#include <Adafruit_NeoPixel.h>

#define PIN 5
#define LED_NUMBER 16 //number of LEDs in the LED strip; can be 8!
#define FORWARD 1
#define REVERSE 2

struct data {
   String color;
   int wait;
   int dir;
   int func;
   int r;
   int g;
   int b;
};

 Adafruit_NeoPixel strip = Adafruit_NeoPixel(LED_NUMBER, PIN, NEO_RGB+NEO_KHZ800);
 
//check for more settings at github.com/adafruit/Adafruit_NeoPixel
void setup() {
   // put your setup code here, to run once:
   Serial.begin(115200);
   Serial.println("Arduino Strip RGB Led");
   strip.begin();
}

void loop() {
   // put your main code here, to run repeatedly:
   clearStrip(100, REVERSE);
   //rainbow(100, FORWARD);
   //clearStrip(100, REVERSE);
   //fillStrip(strip.Color(255,0,0), 100, FORWARD); // color = GRB!
   //fillStrip(strip.Color(0,255,0), 100, FORWARD); // color = GRB
   //fillStrip(strip.Color(0,0,255), 100, FORWARD); // color = GRB
   //blinkNTimes(strip.Color(50, 50, 50), 100, 5);
   //transfusion(20);
   //mildBlinkNTimes(strip.Color(0, 0, 255), 20, 5);
   mildFillStrip(strip.Color(0, 0, 255), 2, 10, FORWARD);
   //delay(1000);
}

void rainbow(int wait, int direction) {
   int first, last;
   setDirection(&first, &last, direction);
   byte color[3];
   byte count, a0, a1, a2;
   for (int i=0;i<10;i++) {
     color[count]=random(256);
     a0=count+random(1)+1;
     color[a0%3]=random(256-color[count]);
     color[(a0+1)%3]=255-color[a0%3]-color[count];
     count+=random(15); // to avoid repeating patterns
     count%=3;
     fillStrip(strip.Color(color[0], color[1], color[2]), wait, direction);
     if (direction == FORWARD) clearStrip(wait, REVERSE);
     else clearStrip(wait, FORWARD);
   }
}

void blinkNTimes(uint32_t color, int wait, int N) {
  for (int i = 0; i < N; ++i) {
    strip.fill(color);
    strip.show();
    delay(wait);
    strip.clear();
    strip.show();
    delay(wait);
  }
}

void transfusion(int wait) {
  int colors[] = {255, 0, 0};
  int descending = 0;
  for (int i = 0; i < 764; ++i) {
    strip.fill(strip.Color(colors[0], colors[1], colors[2]));
    strip.show();
    if (colors[descending] == 0)
      descending = (descending + 1) % 3;
    --colors[descending];
    ++colors[(descending + 1) % 3];
    delay(wait);
  }
}

void mildBlinkNTimes(uint32_t color, int wait, int N) {
  uint8_t r = (uint8_t)(color >> 16), g = (uint8_t)(color >> 8), b = (uint8_t)color;
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < 256; ++j) {
      strip.fill(strip.Color(((float)r) * j / 255, ((float)g) * j / 255, ((float)b) * j / 255));
      strip.show();
      delay(wait);
    }
    for (int j = 255; j >= 0; --j) {
      strip.fill(strip.Color(((float)r) * j / 255, ((float)g) * j / 255, ((float)b) * j / 255));
      strip.show();
      delay(wait);
    }
  }
}

void mildFillStrip(uint32_t color, int wait, int leds_num, int direction) {
  uint8_t r = (uint8_t)(color >> 16), g = (uint8_t)(color >> 8), b = (uint8_t)color;
  int first, last;
  setDirection(&first, &last, direction);
  int center = first;
  int part = 255 / leds_num;
  int brightnesses[LED_NUMBER];
  for (int i = 0; i < LED_NUMBER; ++i)
    brightnesses[i] = 0;
  while (center != last || brightnesses[last - 1] > 0) {
    if (brightnesses[center] == 255) {
      ++center;
    }
    if (center < last) {
      for (int j = center; j < min(center + leds_num, last); ++j) {
        if (j == center || brightnesses[j - 1] >= part) {
          ++brightnesses[j];
          strip.setPixelColor(j, ((float)r) * brightnesses[j] / 255, ((float)g) * brightnesses[j] / 255, ((float)b) * brightnesses[j] / 255);
        }
      }
    }
    for (int j = center - 1; j >= first && brightnesses[j] > 0; --j) {
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
   for (int p=first; p<=last; p++) {
     strip.setPixelColor(abs(p),color);
     strip.show();
     delay(wait);
   }
}

void clearStrip(int wait, int direction) {
   int first, last;
   setDirection(&first, &last, direction);
   for (int p=first; p<=last; p++) {
     strip.setPixelColor(abs(p),0);
     strip.show();
     delay(wait);
   }
}

void setDirection(int *first, int *last,int direction) {
   if (direction == FORWARD) {
     *first = 0;
     *last = LED_NUMBER;
   }
   else {
     *first = -LED_NUMBER;
     *last = 0;
   }
}
