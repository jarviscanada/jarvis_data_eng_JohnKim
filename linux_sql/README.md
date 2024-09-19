# Linux Cluster Monitoring Agent (LCMA)

## Introduction

The Linux Cluster Monitoring Agent (LCMA) is a resource analytic tool used to monitor and collect hardware and usage data from a Linux machine or multiple Linux servers in a cluster. This tool aims to provide a solution for tracking the performance and resource utilization of each host in the cluster. The primary users of this tool are system admins or DevOps Engineers who need to ensure the health and efficiency of their server infrastructure.

The following are technologies used to create the LCMA:

- Bash (scripting)
- Docker (container management)
- PostgreSQL (datastorage, analysis, and reporting)
- Crontab (script automation)

# Quick Start

- Start a psql instance using psql_docker.sh

```bash
# Spinning up a fresh PostgreSQL continer
./scripts/psql_docker.sh create postgres password

# Starting the created PostgreSQL container
./scripts/psql_docker.sh start

# Halting the PostgreSQL container
./scripts/psql_docker.sh stop
```

- Create tables using ddl.sql

```bash
psql -h localhost -U postgres -d host_agent -f ./sql/ddl.sql
```

- Insert hardware specs data into the DB using host_info.sh

```bash
bash ./scripts/host_info.sh psql_hostname psql_port db_name psql_user psql_password
```

- Insert hardware usage data into the DB using host_usage.sh

```bash
bash ./scripts/host_usage.sh psql_hostname psql_port db_name psql_user psql_password
```

- Crontab setup

```bash
crontab -e

# Add the following line to schedule the host_usage.sh script to run every minute
# * * * * *: Specifies the schedule (every minute)
# > /tmp/host_usage.log: Redirects the output to a log file
* * * * * bash <full_path>/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

# Implemenation

## Architecture

Draw a cluster diagram with three Linux hosts, a DB, and agents (use draw.io website). Image must be saved to the `assets` directory.

## Scripts

- psql_docker.sh : Can initialize a PostgreSQL container, start the container or stop the container.

```bash
bash ./scripts/psql_docker.sh create|start|stop db_username db_password
```

- host_info.sh : Grabs hardware specifications then populates the PostgreSQL DB with those specifications.

```bash
bash ./scripts/host_info.sh psql_hostname psql_port db_name psql_user psql_password
```

- host_usage.sh : Grabs resource usage then populates the PostgreSQL DB with the resource logs.

```bash
bash ./scripts/host_usage.sh psql_hostname psql_port db_name psql_user psql_password
```

- crontab : Automates the execution of host_usage.sh in a 1 minute interval.

```bash
crontab -e
* * * * * bash <full_path>/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

## Database Modeling

- `host_info`
  | Column | Data Type | Constraints | Description |
  |--------------------|---------------|----------------------------------|--------------------------------------|
  | `id` | SERIAL | PRIMARY KEY, NOT NULL | Unique identifier for each host |
  | `hostname` | VARCHAR | UNIQUE, NOT NULL | Hostname of the server |
  | `cpu_number` | INT2 | NOT NULL | Number of CPUs |
  | `cpu_architecture` | VARCHAR | NOT NULL | CPU architecture |
  | `cpu_model` | VARCHAR | NOT NULL | CPU model |
  | `cpu_mhz` | FLOAT8 | NOT NULL | CPU frequency in MHz |
  | `l2_cache` | INT4 | NOT NULL | L2 cache size |
  | `timestamp` | TIMESTAMP | NULL | Timestamp of the data collection |
  | `total_mem` | INT4 | NULL | Total memory |

- `host_usage`
  | Column | Data Type | Constraints | Description |
  |--------------------|---------------|----------------------------------|--------------------------------------|
  | `timestamp` | TIMESTAMP | NOT NULL | Timestamp of the data collection |
  | `host_id` | SERIAL | NOT NULL, FOREIGN KEY | Reference to the host_info table |
  | `memory_free` | INT4 | NOT NULL | Free memory |
  | `cpu_idle` | INT2 | NOT NULL | Percentage of CPU idle time |
  | `cpu_kernel` | INT2 | NOT NULL | Percentage of CPU kernel time |
  | `disk_io` | INT4 | NOT NULL | Disk I/O |
  | `disk_available` | INT4 | NOT NULL | Available disk space |

# Test

- Testing for the bash scripts were accomplished through the CLI and comparing the output to the expected output.
- SQL commands were tested using sample data and comparing the tables to with the expected tables.

# Deployment

The LCMA is deployed on the Jarvis Repository through github. This includes all the scripts and instructions for running said scripts.

# Improvements

- Share the repository with more developers to test the clarity of the documentation and make adjustments based on their feedback.
- Create a more streamlined testing method to automate the validation of script outputs and SQL commands.
- Implement additional monitoring features such as network usage.
- Enhance the user interface for easier interaction and visualization of collected data. Perhaps a GUI.
- Implement automatic email alerts based on resource consumption thresholds to notify administrators of potential issues in real-time.
- Switch from using Crontab to GitHub Actions triggered by a cron schedule to simplify the setup process for users.
