# Introduction
The Linux Cluster Monitoring project is a 
minimum viable product that demonstrates the 
effectiveness of the Cluster Monitoring 
Solution to help the LCA team meet their 
business needs. It utilizes `Linux command 
lines, Git, Bash scripts, PostgreSQL, Docker,
and cron` to monitor a system's hardware 
specifications and resource usage. It works 
by first setting up a `psql` instance to 
persist all the data. Said data is then 
gathered using two bash scripts 
`host_info.sh` and `host_usage.sh` to
gather the hosts hardware and usage info 
respectively and then store it into the 
database. The later script is ran 
continuously at 5 minute intervals using 
`cron`.

# Quick Start
### - Start a `psql` instance using psql_docker.sh
```bash 
./scripts/psql_docker.sh create db_username db_password
./scripts/psql_docker.sh start
``` 
### - Create tables using `ddl.sql`
```bash
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```
### - Insert hardware specs data into the DB using `host_info.sh`
```bash
./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```
### - Insert hardware usage data into the DB using `host_usage.sh`
```bash
bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```
### - `Crontab` setup
```bash
bash> crontab -e
* * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password > /tmp/host_usage.log
```

# Implementation
the `psql` instance is created by first starting
the docker and pulling the latest postgres image.
We then create a docker volume called `pgdata`
and save the intended password as an environment
variable before creating and running the container
using the aforementioned password. After creating 
and running the container. We then use the script
to create the two tables to store the host hardware
and usage data using `sql` commands. The data is 
gathered using the two scripts uses text processing
software's such as `awk` and `tail` to extract the 
required values from the `vmstat` and `df` for the
host hardware and usage. Finally, the usage data
is recorded autonomously using `crontab`.

## Architecture
