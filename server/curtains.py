#!/usr/bin/env python3

import json
from sys import argv
from subprocess import run

def json_load(filename):
  with open(filename, 'r') as f:
    return json.load(f)

config = json_load("config.json")
MOTOR_IP = config.get("MOTOR_IP") or "192.168.43.152"

if argv[1] == 'Morning':
  run(["curl", f"http://{MOTOR_IP}/forward"])
else:
  run(["curl", f"http://{MOTOR_IP}/reverse"])
