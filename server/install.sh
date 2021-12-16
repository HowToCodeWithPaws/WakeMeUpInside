#!/bin/sh

[ -f config.json ] || echo '{
  "phone_rm": "0",
  "phone_rt": "5",
  "lamp_ba": "1", "lamp_bat": "0",
  "curtains_o": "1", "curtains_ot": "0",
  "curtains_c": "1", "curtains_ct": "0",
  "lightlevel": "50",
  "mode": "0",
  "red": "183", "green": "225", "blue": "205"
}' > config.json

sudo usermod -a -G gpio "$LOGNAME"

sudo apt update
sudo apt install ffmpeg python3 mpg321 curl

if ! grep 'dtoverlay=audremap,pins_18_19' /boot/config.txt; then
  echo 'dtoverlay=audremap,pins_18_19' | sudo tee -a /boot/config.txt
  sudo reboot now
fi
