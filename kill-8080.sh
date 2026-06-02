#!/bin/bash

PORT=8080

echo "Checking for processes occupying port $PORT..."

PID=$(lsof -ti :$PORT)

if [ -z "$PID" ]; then
  echo "No process found on port $PORT."
  exit 0
fi

echo "Found process(es) on port $PORT:"
lsof -i :$PORT

read -p "Do you want to kill this/these process(es)? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
  kill -9 $PID
  echo "Killed process(es) $PID"
  exit 0
else
  echo "Aborted."
  exit 1
fi
