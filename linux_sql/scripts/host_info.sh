#! /bin/sh

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

vmstat_mb=$(vmstat --unit M)
specs=$(lscpu)
hostname=$(hostname -f)

cpu_number=$(echo "$specs"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$specs"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$specs"  | egrep "^Model name:" | awk '{print substr($0, index($0, $3))}' | xargs)
cpu_mhz=$(echo "$specs"  | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$specs"  | egrep "^L2 cache:" | awk '{print $3}' | tr -d 'K' | xargs)
total_mem=$(echo "$vmstat_mb" | tail -1 | awk '{print $4}')

timestamp=$(vmstat -t | awk '{print $(NF-1), $NF}' | tail -1 | xargs)

insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem)
            VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$timestamp', '$total_mem')"

export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?