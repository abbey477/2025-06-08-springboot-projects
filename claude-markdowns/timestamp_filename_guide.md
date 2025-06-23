# Shell Script Timestamp Filename Guide

## Basic Date Stamp Formats

### Simple date (YYYY-MM-DD)
```bash
filename="myfile_$(date +%Y-%m-%d).txt"
touch "$filename"
# Creates: myfile_2025-06-16.txt
```

### Date and time (YYYY-MM-DD_HH-MM-SS)
```bash
filename="logfile_$(date +%Y-%m-%d_%H-%M-%S).log"
touch "$filename"
# Creates: logfile_2025-06-16_14-30-25.log
```

### Compact format (YYYYMMDD_HHMMSS)
```bash
filename="backup_$(date +%Y%m%d_%H%M%S).tar.gz"
touch "$filename"
# Creates: backup_20250616_143025.tar.gz
```

## Shell Script Examples

### Basic script with timestamp
```bash
#!/bin/bash
# Define timestamp once and reuse
timestamp=$(date +%Y-%m-%d_%H-%M-%S)
filename="report_${timestamp}.txt"

# Create the file
echo "Report generated on $(date)" > "$filename"
echo "File created: $filename"
```

### Advanced script with multiple files
```bash
#!/bin/bash
DATE=$(date +%Y-%m-%d)
TIME=$(date +%H-%M-%S)
TIMESTAMP="${DATE}_${TIME}"

# Use in multiple places
touch "file1_${TIMESTAMP}.txt"
touch "file2_${TIMESTAMP}.log"
mkdir "folder_${TIMESTAMP}"
```

## Different Timestamp Formats

| Format | Command | Example Output |
|--------|---------|----------------|
| ISO format with timezone | `date +%Y-%m-%dT%H:%M:%S%z` | 2025-06-16T14:30:25-0500 |
| Human readable | `date +%Y-%m-%d_%I-%M-%S%p` | 2025-06-16_02-30-25PM |
| Unix timestamp | `date +%s` | 1718561425 |
| Custom format | `date +%b-%d-%Y_%H%M` | Jun-16-2025_1430 |
| Simple date only | `date +%Y%m%d` | 20250616 |
| Time only | `date +%H%M%S` | 143025 |

## Practical Use Cases

### Log file rotation
```bash
#!/bin/bash
logfile="/var/log/myapp_$(date +%Y%m%d).log"
echo "$(date): Application started" >> "$logfile"
```

### Database backup script
```bash
#!/bin/bash
backup_name="database_backup_$(date +%Y-%m-%d_%H-%M-%S).sql"
mysqldump mydb > "$backup_name"
echo "Backup created: $backup_name"
```

### Creating timestamped directories
```bash
#!/bin/bash
project_dir="project_$(date +%Y-%m-%d)"
mkdir "$project_dir"
cd "$project_dir"
echo "Working in: $project_dir"
```

### File processing with timestamps
```bash
#!/bin/bash
input_file="data.csv"
output_file="processed_data_$(date +%Y%m%d_%H%M%S).csv"

# Process the file
cat "$input_file" | sort | uniq > "$output_file"
echo "Processed file saved as: $output_file"
```

## Date Format Specifiers Reference

| Specifier | Description | Example |
|-----------|-------------|---------|
| `%Y` | 4-digit year | 2025 |
| `%y` | 2-digit year | 25 |
| `%m` | Month (01-12) | 06 |
| `%B` | Full month name | June |
| `%b` | Abbreviated month | Jun |
| `%d` | Day of month (01-31) | 16 |
| `%H` | Hour (00-23) | 14 |
| `%I` | Hour (01-12) | 02 |
| `%M` | Minute (00-59) | 30 |
| `%S` | Second (00-59) | 25 |
| `%p` | AM/PM | PM |
| `%s` | Unix timestamp | 1718561425 |
| `%z` | Timezone offset | -0500 |

## Best Practices

### 1. Use consistent formats
Choose one timestamp format and stick with it throughout your project.

### 2. Avoid spaces in filenames
Use underscores or hyphens instead of spaces:
```bash
# Good
filename="report_$(date +%Y-%m-%d).txt"

# Avoid
filename="report $(date +%Y-%m-%d).txt"
```

### 3. Store timestamp in variable for reuse
```bash
#!/bin/bash
TIMESTAMP=$(date +%Y-%m-%d_%H-%M-%S)
logfile="app_${TIMESTAMP}.log"
backupfile="backup_${TIMESTAMP}.tar.gz"
```

### 4. Use quotes around filenames
Always quote variables containing filenames:
```bash
filename="my_file_$(date +%Y-%m-%d).txt"
touch "$filename"  # Always quote the variable
```

### 5. Validate timestamp format for your needs
- Use `%Y-%m-%d` for daily files
- Use `%Y-%m-%d_%H-%M-%S` for files created multiple times per day
- Use `%Y%m%d_%H%M%S` for compact naming

## Common Functions

### Create a timestamped log entry
```bash
log_with_timestamp() {
    local message="$1"
    local logfile="app_$(date +%Y%m%d).log"
    echo "$(date +%Y-%m-%d\ %H:%M:%S): $message" >> "$logfile"
}

# Usage
log_with_timestamp "Application started"
log_with_timestamp "Processing complete"
```

### Archive files with timestamp
```bash
archive_with_timestamp() {
    local source_dir="$1"
    local archive_name="${source_dir}_$(date +%Y%m%d_%H%M%S).tar.gz"
    tar -czf "$archive_name" "$source_dir"
    echo "Created archive: $archive_name"
}

# Usage
archive_with_timestamp "my_project"
```

This guide covers the most common scenarios for adding timestamps to filenames in shell scripts. The most popular and readable format is `%Y-%m-%d_%H-%M-%S` which creates filenames like `myfile_2025-06-16_14-30-25.txt`.