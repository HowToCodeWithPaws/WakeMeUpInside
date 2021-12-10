#!/usr/bin/env python3

from urllib.parse import urlparse, parse_qsl
from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import sys
import os
from subprocess import CalledProcessError, check_output
from pathlib import Path

pwd = str(Path(__file__).parent.resolve())
if ' ' in pwd:
  print("cant have spaces in path")
  sys.exit(1)

weekdays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']

def update_crontab(obj):
  try:
    old_crontab = check_output(["crontab", "-l"]).decode('utf8')
    new_crontab_lines = [e for e in old_crontab.split('\n') if 'start_server.sh' not in e and 'ring.py' not in e]
  except CalledProcessError:
    new_crontab_lines = []
  new_crontab_lines.append(f"59 * * * * {pwd}/start_server.sh")

  for i, day in enumerate(weekdays):
    for time in ['Morning', 'Evening']:
      hours = int(obj[day + time + 'HH'])
      minutes = int(obj[day + time + 'MM'])
      if hours == -1 or minutes == -1:
        continue
      new_crontab_lines.append(f"{minutes} {hours} * * {i} {pwd}/ring.py {time}")

  with os.popen("crontab", 'w') as f:
    f.writelines(new_crontab_lines)


def json_dump(obj, filename):
  with open(filename, 'w') as f:
    json.dump(obj, f)

def json_load(filename):
  with open(filename, 'r') as f:
    return json.load(f)

def write_config(o):
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
