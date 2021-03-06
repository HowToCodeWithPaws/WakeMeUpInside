#!/usr/bin/env python3

import json
from subprocess import check_output, run
from pathlib import Path
import os

pwd = str(Path(__file__).parent.resolve())
os.chdir(pwd)

def json_load(filename):
  with open(filename, 'r') as f:
    return json.load(f)

config = json_load("config.json")
LAMP_IP = config.get("LAMP_IP") or "192.168.43.152"

threshold = (100 - int(config["lightlevel"])) * 7 + 100
mode = int(config["mode"][0])
direction = 'l' if mode == 2 else 'r'
delay = 5
number = 5
red = int(config["red"])
green = int(config["green"])
blue = int(config["blue"])
color = f"{green:02x}{red:02x}{blue:02x}"

res = json.loads(check_output(["curl", f"http://{LAMP_IP}/analog/0"]))
light = int(res["return_value"])
print(light, ">", threshold)
if light > threshold:
  run(["curl", f"http://{LAMP_IP}/fill?params={mode}{direction}{delay}{number}{color}"])
