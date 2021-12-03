// порты для подключения модуля ULN2003 к Arduino
#define in1 8
#define in2 9
#define in3 10
#define in4 11

int dl = 2; // время задержки между импульсами
int iters = 6250;
char direction = 0;
char incomingByte = 0;

void setup() {
    Serial.begin(9600);
  
    pinMode(in1, OUTPUT);
    pinMode(in2, OUTPUT);
    pinMode(in3, OUTPUT);
    pinMode(in4, OUTPUT);
    
  
    
    }
   
   void loop(){
      if (Serial.available() > 0) {
        incomingByte = Serial.read();
        if (incomingByte == '0' || incomingByte == '1') {
          direction = incomingByte;
          digitalWrite(in1, LOW); 
          digitalWrite(in2, LOW); 
          digitalWrite(in3, LOW); 
          digitalWrite(in4, LOW);
          iters = 0;
        }
      }
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
