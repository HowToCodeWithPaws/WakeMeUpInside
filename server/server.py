#!/usr/bin/env python3

from urllib.parse import urlparse, parse_qsl
from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import sys
import os
from subprocess import CalledProcessError, check_output
from pathlib import Path

pwd = str(Path(__file__).parent.resolve())
os.chdir(pwd)
if ' ' in pwd:
  print("cant have spaces in path")
  sys.exit(1)

weekdays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']

def update_crontab(obj):
  try:
    old_crontab = check_output(["crontab", "-l"]).decode('utf8')
    new_crontab_lines = [e for e in old_crontab.split('\n') if pwd not in e and e.strip() != '']
  except CalledProcessError:
    new_crontab_lines = []
  new_crontab_lines.append(f"59 * * * * {pwd}/start_server.sh")

  def add_extra_line(name, file):
    if obj[name] == '1':
      t1 = t - int(obj[name + "t"])
      if t1 < 0:
        t1 = 0
      m = t1 % 60
      h = t1 // 60
      new_crontab_lines.append(f"{m} {h} * * {i} {pwd}/{file} {time}")

  for i, day in enumerate(weekdays):
    for time in ['Morning', 'Evening']:
      hours = int(obj[day + time + 'HH'])
      minutes = int(obj[day + time + 'MM'])
      if hours == -1 or minutes == -1:
        continue
      t = hours*60 + minutes

      new_crontab_lines.append(f"{minutes} {hours} * * {i} {pwd}/ring.py {time}")

      if time == 'Morning':
        add_extra_line("lamp_ba", "lamp.py")
        add_extra_line("curtains_o", "curtains.py")
      if time == 'Evening':
        add_extra_line("curtains_c", "curtains.py")

  with os.popen("crontab", 'w') as f:
    f.write('\n'.join(new_crontab_lines) + '\n')


def json_dump(obj, filename):
  with open(filename, 'w') as f:
    json.dump(obj, f)

def json_load(filename):
  with open(filename, 'r') as f:
    return json.load(f)

def write_config(o):
  if o.get("LAMP_IP") == "":
    del o["LAMP_IP"]
  if o.get("MOTOR_IP") == "":
    del o["MOTOR_IP"]

  obj = {**json_load("config.json"), **o}
  json_dump(obj, "config.json")
  update_crontab(obj)

class S(BaseHTTPRequestHandler):
  def do_GET(self):
    self.send_response(200)
    self.send_header("Content-type", "text/html")
    self.end_headers()

    o = dict(parse_qsl(urlparse(self.path).query))
    write_config(o)

HTTPServer(('0.0.0.0', 8080), S).serve_forever()
