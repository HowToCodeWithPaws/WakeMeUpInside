#!/usr/bin/env python3

import json
from subprocess import check_output, run

def json_load(filename):
  with open(filename, 'r') as f:
    return json.load(f)

config = json_load("config.json")
LAMP_IP = config.get("LAMP_IP") or "192.168.1.67"

threshold = int(config["lightlevel"]) * 700 + 100
mode = int(config["mode"][0])
direction = 'l' if mode == 2 else 'r'
delay = 5
number = 5
red = int(config["red"])
green = int(config["green"])
blue = int(config["blue"])
color = f"{red:02x}{green:02x}{blue:02x}"

res = json.loads(check_output(["curl", f"http://{LAMP_IP}/analog/0"]))
light = int(res["return_value"])
if light > threshold:
  run(["curl", f"http://{LAMP_IP}/fill?params={mode}{direction}{delay}{number}{color}"])
