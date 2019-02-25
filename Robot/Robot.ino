int E1 = 5;     //M1 Speed Control
int E2 = 6;     //M2 Speed Control
int M1 = 4;    //M1 Direction Control
int M2 = 7;    //M1 Direction Control
 
void stop(void)                    //Stop
{
  digitalWrite(E1,LOW);   
  digitalWrite(E2,LOW);      
}   
void advance(int a,int b)          //Move forward
{//PWM Speed Control
  analogWrite (E1,(60));      //lewe predkosc
  digitalWrite(M1,HIGH);    //prawe przod
  analogWrite (E2,(50));    // prawe predkosc
  digitalWrite(M2,LOW);     //lewe przod
}  
void back_off (int a,int b)          //Move backward
{
  analogWrite (E1,(60));
  digitalWrite(M1,LOW);   
  analogWrite (E2,(50));    
  digitalWrite(M2,HIGH);
}
void turn_L (char a,char b)             //Turn Left
{
  analogWrite (E1,(60));
  digitalWrite(M1,LOW);   //prawe przod
  analogWrite (E2,(50));    
  digitalWrite(M2,LOW);
}
void turn_BL (char a,char b)             //Turn Left
{
  analogWrite (E1,(50));
  digitalWrite(M1,LOW);   //prawe przod
  analogWrite (E2,(50));    
  digitalWrite(M2,HIGH);
}
void turn_FL (char a,char b)             //Turn Left
{
  analogWrite (E1,(50));
  digitalWrite(M1,HIGH);   //prawe przod
  analogWrite (E2,(50));    
  digitalWrite(M2,LOW);
}
void turn_R (char a,char b)             //Turn Right
{
  analogWrite (E1,(60));
  digitalWrite(M1,HIGH);   //prawe przod
  analogWrite (E2,(50));    
  digitalWrite(M2,HIGH);
}
void turn_FR (char a,char b)             //Turn Right
{
  analogWrite (E1,(60));
  digitalWrite(M1,HIGH);   //prawe przod
  analogWrite (E2,(40));    //prawe predkosc
  digitalWrite(M2,LOW);
}
void turn_BR (char a,char b)             //Turn Right
{
  analogWrite (E1,(60));
  digitalWrite(M1,LOW);   //prawe przod
  analogWrite (E2,(40));    //prawe predkosc
  digitalWrite(M2,HIGH);
}
void setup(void) 
{ 
  int i;
  for(i=4;i<=7;i++)
    pinMode(i, OUTPUT);  
  Serial.begin(115200);      //Set Baud Rate
  Serial.println("Run keyboard control");
} 
void loop(void) 
{
  if(Serial.available()){
    char val = Serial.read();
    if(val != -1)
    {
      switch(val)
      {
      case '1'://Move Forward
        advance (255,255);   //move forward in max speed
        break;
      case '2'://Move Backward
        back_off (255,255);   //move back in max speed
        break;
      case '3'://Turn Left
        turn_L (100,100);
        break;       
      case '4'://Turn Right
        turn_R (100,100);
        break;
      case '5'://Turn Front Right
        turn_FR (100,100);
        break;
      case '6'://Turn Front left
        turn_FL (100,100);
        break;
      case '7'://Turn back left
        turn_BL (100,100);
        break;
      case '8'://Turn back right
        turn_BR (100,100);
        break;
      case 'z':
        Serial.println("Hello");
        break;
      case '0':
        stop();
        break;
      
      }
      Serial.println(val);
    }
    else stop();  
  }
}

