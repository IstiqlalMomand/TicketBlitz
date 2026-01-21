#!/usr/bin/env bash
set -euo pipefail

# Default to 500 if not provided
N="${1:-500}"

# Must be an integer
if ! [[ "$N" =~ ^[0-9]+$ ]]; then
  echo "Error: '$N' is not a valid positive integer."
  exit 1
fi

# Must be > 0
if (( N <= 0 )); then
  echo "Error: number must be bigger than 0."
  exit 1
fi

echo "🚀 Launching MASSIVE Attack ($N Requests)..."

for i in $(seq 1 "$N"); do
  curl -sS -X POST "http://localhost:8080/api/bookings?eventId=1&userId=$i"
done

echo "✅ Requests sent. Waiting for the dust to settle..."
wait
echo "🏁 Attack Finished. CHECK THE BROWSER NOW!"
