#include <WiFiEsp.h>

#include "aREST.h"

// Emulate Serial1 on pins 6/7 if not present
#ifndef HAVE_HWSERIAL1
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7);  // RX, TX
#endif

// порты для подключения модуля ULN2003 к Arduino
#define in1 8
#define in2 9
#define in3 10
#define in4 11

char* ssid = "diplo";         // your network SSID (name)
char* pass = "otdimasika";    // your network password
int status = WL_IDLE_STATUS;  // the Wifi radio's status

WiFiEspServer server(80);
aREST rest = aREST();

int dl = 3; // время задержки между импульсами
int iters = 6250;
char direction = 0;
char incomingByte = 0;

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
    Serial.begin(9600);
  
    pinMode(in1, OUTPUT);
    pinMode(in2, OUTPUT);
    pinMode(in3, OUTPUT);
    pinMode(in4, OUTPUT);
    
    Serial.println("Arduino Strip RGB Led");
  
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
    
    rest.function("forward", forward);
    rest.function("reverse", reverse);
    
    }
   
   void loop(){
    WiFiEspClient client = server.available();
    handle(client);
      if (iters < 6250) {
        if (direction == '0') {
          digitalWrite(in1, HIGH); 
          digitalWrite(in2, LOW); 
          digitalWrite(in3, LOW); 
          digitalWrite(in4, HIGH);
          delay(dl);
          
          digitalWrite(in1, HIGH); 
          digitalWrite(in2, HIGH); 
          digitalWrite(in3, LOW); 
          digitalWrite(in4, LOW);
          delay(dl);
          
          digitalWrite(in1, LOW); 
          digitalWrite(in2, HIGH); 
          digitalWrite(in3, HIGH); 
          digitalWrite(in4, LOW);
          delay(dl);
          
          digitalWrite(in1, LOW); 
          digitalWrite(in2, LOW); 
          digitalWrite(in3, HIGH); 
          digitalWrite(in4, HIGH);
          delay(dl);
        } else {
          digitalWrite(in1, LOW); 
          digitalWrite(in2, LOW); 
          digitalWrite(in3, HIGH); 
          digitalWrite(in4, HIGH);
          delay(dl);
                  
          
          digitalWrite(in1, LOW); 
          digitalWrite(in2, HIGH); 
          digitalWrite(in3, HIGH); 
          digitalWrite(in4, LOW);
          delay(dl);
          
          digitalWrite(in1, HIGH); 
          digitalWrite(in2, HIGH); 
          digitalWrite(in3, LOW); 
          digitalWrite(in4, LOW);
          delay(dl);
      
          
          digitalWrite(in1, HIGH); 
          digitalWrite(in2, LOW); 
          digitalWrite(in3, LOW); 
          digitalWrite(in4, HIGH);
          delay(dl);
        }
        ++iters;
      }
   }

   void forward() {
      direction = '1';
      digitalWrite(in1, LOW); 
      digitalWrite(in2, LOW); 
      digitalWrite(in3, LOW); 
      digitalWrite(in4, LOW);
      iters = 0;
   }

   void reverse() {
      direction = '0';
      digitalWrite(in1, LOW); 
      digitalWrite(in2, LOW); 
      digitalWrite(in3, LOW); 
      digitalWrite(in4, LOW);
      iters = 0;
   }

//
//digitalWrite(in1, LOW); 
//    digitalWrite(in2, LOW); 
//    digitalWrite(in3, LOW); 
//    digitalWrite(in4, LOW);}

//void loop(){
//    digitalWrite(in1, HIGH); 
//    digitalWrite(in2, LOW); 
//    digitalWrite(in3, LOW); 
//    digitalWrite(in4, HIGH);
//    delay(dl);
//    
//    digitalWrite(in1, HIGH); 
//    digitalWrite(in2, HIGH); 
//    digitalWrite(in3, LOW); 
//    digitalWrite(in4, LOW);
//    delay(dl);
//    
//    digitalWrite(in1, LOW); 
//    digitalWrite(in2, HIGH); 
//    digitalWrite(in3, HIGH); 
//    digitalWrite(in4, LOW);
//    delay(dl);
//    
//    digitalWrite(in1, LOW); 
//    digitalWrite(in2, LOW); 
//    digitalWrite(in3, HIGH); 
//    digitalWrite(in4, HIGH);
//    delay(dl);
//
//   digitalWrite(in1, LOW); 
//    digitalWrite(in2, LOW); 
//    digitalWrite(in3, LOW); 
//    digitalWrite(in4, LOW);
//}
//  if(go == 1){
//  clockw();
//  clockw();
//  clockw();
//  clockw();
//   digitalWrite(in1, LOW); 
//    digitalWrite(in2, LOW); 
//    digitalWrite(in3, LOW); 
//    digitalWrite(in4, LOW);
//  cclockw();
//  cclockw();
//  cclockw();
//  cclockw();
//  cclockw();
//  go =0;
//   digitalWrite(in1, LOW); 
//    digitalWrite(in2, LOW); 
//    digitalWrite(in3, LOW); 
//    digitalWrite(in4, LOW);}
//  }

void clockw() {  
  
    digitalWrite(in1, HIGH); 
    digitalWrite(in2, LOW); 
    digitalWrite(in3, LOW); 
    digitalWrite(in4, HIGH);
    delay(dl);
    
    digitalWrite(in1, HIGH); 
    digitalWrite(in2, HIGH); 
    digitalWrite(in3, LOW); 
    digitalWrite(in4, LOW);
    delay(dl);
    
    digitalWrite(in1, LOW); 
    digitalWrite(in2, HIGH); 
    digitalWrite(in3, HIGH); 
    digitalWrite(in4, LOW);
    delay(dl);
    
    digitalWrite(in1, LOW); 
    digitalWrite(in2, LOW); 
    digitalWrite(in3, HIGH); 
    digitalWrite(in4, HIGH);
    delay(dl);
   }

void cclockw() {
    digitalWrite(in1, LOW); 
    digitalWrite(in2, LOW); 
    digitalWrite(in3, HIGH); 
    digitalWrite(in4, HIGH);
    delay(dl);
    
    digitalWrite(in1, LOW); 
    digitalWrite(in2, HIGH); 
    digitalWrite(in3, HIGH); 
    digitalWrite(in4, LOW);
    delay(dl);
    

    digitalWrite(in1, HIGH); 
    digitalWrite(in2, HIGH); 
    digitalWrite(in3, LOW); 
    digitalWrite(in4, LOW);
    delay(dl);

    digitalWrite(in1, HIGH); 
    digitalWrite(in2, LOW); 
    digitalWrite(in3, LOW); 
    digitalWrite(in4, HIGH);
    delay(dl);

}
