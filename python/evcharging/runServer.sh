#!/bin/bash

export MKL_NUM_THREADS=1
export NUMEXPR_NUM_THREADS=1
export OMP_NUM_THREADS=1


file=`realpath $1`;
port=$2

cd "$(dirname "$0")"
cd src;

# python3 xmlrpc_interface.py ../config_advnn.json
# python3 xmlrpc_interface.py '../'."$1"

python3 xmlrpc_interface.py $file $port
