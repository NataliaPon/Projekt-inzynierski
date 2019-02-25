#include <TSL2581.h>//sensor swiatla
#include <Wire.h>
#include <Arduino.h>
#include <LPS.h> //cisnieniomierz
#include <HTS221.h> //wilgotnosciomierz
#include <Temperature_LM75_Derived.h>//termometr


LPS ps;
Generic_LM75 temperature;
WaveShare_TSL2581 tsl = WaveShare_TSL2581();
int TSL2581_INT = 13;

void setup() {

  Wire.begin();

  smeHumidity.begin();
  Serial.begin(115200);
  pinMode(TSL2581_INT, INPUT);
  if (!ps.init())
  {
    Serial.println("Failed to autodetect pressure sensor!");
    while (1);
  }

  ps.enableDefault();
  tsl.TSL2581_power_on();
  delay(2000);
  tsl.TSL2581_config();
}


void loop() {
  //wilgotnosciomierz
  double data = 0;
  data = smeHumidity.readHumidity();
  Serial.print("A");
  Serial.print(data);//data 1: wilgotnosciomierz wilgotnosc [%]
  delay(100);
  
  data = smeHumidity.readTemperature();
  Serial.print("B");
  Serial.print(data);// data 2: wilgotnosciomierz temp [st C]
  delay(100);
  
  //termometr
  Serial.print("C");
  Serial.print(temperature.readTemperatureC());//data 3: termometr temp [st C]
  delay(100);
  
  //cisnieniomierz
  float pressure = ps.readPressureMillibars();
  float altitude = ps.pressureToAltitudeMeters(pressure);
  float temperature = ps.readTemperatureC();

  Serial.print(pressure);//data 4: cisnieniomierz cisnienie [hPa]
  delay(100);
  
  Serial.print("D");
  Serial.print(altitude);//data 5: cisnieniomierz wysokosc [m]
  delay(100);
  
  Serial.print("E");
  Serial.print(temperature);// data 6: cisnieniomierz temp [st C]
  delay(100);

  //sensor swiatla
  unsigned long Lux;
  tsl.TSL2581_Read_Channel();
  Lux = tsl.calculateLux(2, NOM_INTEG_CYCLE);

  Serial.print("F" );
  Serial.print(Lux);//data 7: sensor swiatla natezenie swiatla [Luks/lx]
  delay(100);             

}
