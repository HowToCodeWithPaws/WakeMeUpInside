#!/usr/bin/env python3

from time import sleep, time
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(6, GPIO.IN, pull_up_down=GPIO.PUD_UP) # циферблат

i = 0
lastCallback = time()
def callback(pin):
  global i, lastCallback
  lastCallback = time()
  i += 1

# если использовать add_event_detect после какогто звука,
# то он падат насмерть без возможности востановления.
# я уже так 10 раз перезагружал малину. блять.
# я сдаюсь и пишу edge detection ручками.
#GPIO.add_event_detect(6, GPIO.FALLING, callback=callback, bouncetime=10)

def input(pin):
  sum = 0
  for _ in range(10):
    sum += GPIO.input(pin)
    sleep(0.003)
  return sum >= 5

prev = input(6)
try:
  while i == 0 or time() - lastCallback < 0.2:
    next = input(6)
    if prev == 1 and next == 0:
      callback(6)
    prev = next
    sleep(0.01)
except KeyboardInterrupt:
  pass

print(i)

GPIO.cleanup()
