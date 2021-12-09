#!/usr/bin/env python3

from time import sleep, time
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(5, GPIO.OUT) # колокольчик (реле)
GPIO.setup(13, GPIO.IN, pull_up_down=GPIO.PUD_UP) # трубка

DELAY = 0.1
TOTAL_TIME = 60

def input(pin):
  sum = 0
  for _ in range(10):
    sum += GPIO.input(pin)
    sleep(0.003)
  return sum >= 5

for i in range(int(TOTAL_TIME/DELAY)):
  GPIO.output(5, i % 2)
  sleep(DELAY)
  if input(13) == 0:
    # todo: мини игра с мостачём
    break

GPIO.cleanup()
