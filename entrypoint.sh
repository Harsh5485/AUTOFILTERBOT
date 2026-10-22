#!/bin/bash

# Install any additional dependencies if needed
apt-get update && apt-get install -y ffmpeg git

# Set up environment
export PYTHONPATH="${PYTHONPATH}:/app"
export PYTHONDONTWRITEBYTECODE=1

# Print debug info
pwd
echo "Files in current directory:"
ls -la

# Validate Python syntax before running
python -m py_compile bot.py

# Start the bot
exec python bot.py