#!/bin/bash
PORT=8080
PID=$(lsof -t -i :$PORT)

if [ -n "$PID" ]; then
  echo '[+] Stopping application'
  kill $PID
fi

echo '[+] Shutdown local database'
docker-compose stop