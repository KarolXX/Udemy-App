#!/bin/sh

# Abort on any error (including if wait-for-it fails).
set -e

# Wait for the backend to be up, if we know where it is.
if [ -n "udemy_db" ]; then
  /wait-for-it.sh "udemy_db", "3306"
fi

# Run the main container command.
exec "$@"