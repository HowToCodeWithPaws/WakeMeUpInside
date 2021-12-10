#!/bin/sh

cd -- "$(dirname -- "$0")"

if pgrep -f server.py; then
  exit
fi

./server.py
