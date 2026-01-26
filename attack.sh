#!/bin/bash
echo "🚀 Launching MASSIVE Attack (500 Requests)..."
# Send 500 requests in the background
for i in {1..5000}; do
  curl -X POST "http://localhost:8080/api/bookings?eventId=2&userId=$i" >/dev/null 2>&1 &
done
echo "✅ Requests sent. Waiting for the dust to settle..."
wait
echo "🏁 Attack Finished. CHECK THE BROWSER NOW!"
