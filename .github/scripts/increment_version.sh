#!/bin/bash

# Increment a version name using Semantic Versioning approach
# Usage: increment_version.sh [-Mmp] <version_name>

# Process options
while getopts ":Mmp" opt; do
  case $opt in
    M)
      component="major"
      ;;
    m)
      component="minor"
      ;;
    p)
      component="patch"
      ;;
    \?)
      echo "Usage: $(basename $0) [-M N] [-m N] [-p N] <version_name>"
      exit 1
      ;;
  esac
done

# Remove processed options from the argument list
shift $((OPTIND-1))

# Check for correct number of arguments
if [ "$#" -ne 1 ]; then
  echo "Usage: $(basename $0) [-M N] [-m N] [-p N] <version_name>"
  exit 1
fi

version="$1"

# Check if the version string is valid
if ! [[ "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
  echo "Invalid version string: $version. Must be in the format X.Y.Z"
  exit 1
fi

# Brake version name in parts
IFS='.' read -ra parts <<< "$version"

# Increment version numbers as requested
case "$component" in
  "major")
    ((parts[0]++))
    parts[1]=0
    parts[2]=0
    ;;
  "minor")
    ((parts[1]++))
    parts[2]=0
    ;;
  "patch")
    ((parts[2]++))
    ;;
esac

echo "${parts[0]}.${parts[1]}.${parts[2]}"