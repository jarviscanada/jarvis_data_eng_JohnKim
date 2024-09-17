#!/bin/bash

# assign parameters to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Checks for exactly 5 args
if [ "$#" -ne 5 ]; then
  echo "usage: ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password"
  exit 1
fi


lscpu_out=`lscpu`

# Saving hardware info
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "Model name:" | awk -F 'Model name: ' '{print $2}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | grep "Model name:" | awk -F '@' '{print $2}' | xargs | sed 's/GHz//g' | awk '{print $1}')
l2_cache=$(lscpu | grep "L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}' | xargs)
timestamp=$(date --utc '+%Y-%m-%d %H:%M:%S')

# PSQL command: Inserting relevant data into the host_info table
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp)
VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', $total_mem, '$timestamp');"

# Setting up env and inserting date using psql command
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
