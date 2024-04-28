#!/bin/bash
cd src || exit

# run
python3 configure.py &&
python3 main.py
