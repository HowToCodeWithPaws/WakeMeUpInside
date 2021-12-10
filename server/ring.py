#!/usr/bin/env python3

from time import sleep, time
import json
import random
import os
import RPi.GPIO as GPIO
from play import play
from pathlib import Path

pwd = str(Path(__file__).parent.resolve())
os.chdir(pwd)

GPIO.setmode(GPIO.BCM)
GPIO.setup(5, GPIO.OUT) # колокольчик (реле)
GPIO.setup(13, GPIO.IN, pull_up_down=GPIO.PUD_UP) # трубка
GPIO.setup(6, GPIO.IN, pull_up_down=GPIO.PUD_UP) # циферблат

def json_load(filename):
  with open(filename, 'r') as f:
    return json.load(f)

config = json_load("config.json")
REPEAT_TIMES = int(config["phone_rm"]) + 1
REPEAT_DELAY = float(config["phone_rt"]) * 60

DELAY = 0.1
TOTAL_TIME = 60

def input(pin):
  sum = 0
  for _ in range(10):
    sum += GPIO.input(pin)
    sleep(0.003)
  return sum >= 5

def count_pulses():
  i = 0
  lastCallback = time()
  prev = input(6)
  while i == 0 or time() - lastCallback < 0.2:
    next = input(6)
    if prev == 1 and next == 0:
      lastCallback = time()
      i += 1
    prev = next
    sleep(0.01)
  return i

def game():
  num = random.randint(1, 9)
  sleep(1.5)
  for _ in range(10):
    play(str(num))
    res = count_pulses()
    if res == num:
      break

def ring():
  for i in range(int(TOTAL_TIME/DELAY)):
    GPIO.output(5, i % 2)
    sleep(DELAY)
    if input(13) == 0:
      game()
      return True
  return False

try:
  for i in range(REPEAT_TIMES):
    if i != 0:
      sleep(REPEAT_DELAY)
    if ring():
      break
except KeyboardInterrupt:
  pass

GPIO.cleanup()
