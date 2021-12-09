#!/usr/bin/env python3

import subprocess
import sys
from shutil import which

MP3_PATH = "../mp3s_10dB/{}.mp3"
FFPLAY_ARGS = ['-hide_banner', '-autoexit', '-nodisp']
FFPLAY = which("ffplay.exe") or which("ffplay")

def play(number: str):
  for char in number:
    subprocess.run([FFPLAY, *FFPLAY_ARGS, MP3_PATH.format(char)], timeout=60, check=False)

if __name__ == '__main__':
  play(sys.argv[1])
