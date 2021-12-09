#!/bin/sh

sudo apt update
sudo apt install ffmpeg python3 mpg321

if ! grep 'dtoverlay=audremap,pins_18_19' /boot/config.txt; then
  echo 'dtoverlay=audremap,pins_18_19' | sudo tee -a /boot/config.txt
  sudo reboot now
fi
