#!/usr/bin/env python3

import subprocess
import sys
import os
from shutil import which
from pathlib import Path

MP3_PATH = "../mp3s/{}.mp3"
FFPLAY_ARGS = ['-hide_banner', '-autoexit', '-nodisp']
FFPLAY = which("ffplay.exe") or which("ffplay")

def play(number: str):
  for char in number:
    subprocess.run([FFPLAY, *FFPLAY_ARGS, MP3_PATH.format(char)], timeout=60, check=False)

if __name__ == '__main__':
  pwd = str(Path(__file__).parent.resolve())
  os.chdir(pwd)
  play(sys.argv[1])
