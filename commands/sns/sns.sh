#!/bin/bash
topic_arn=$1
notification_endpoint=$2
profile=$2
aws sns subscribe \
  --topic-arn $topic_arn \
  --protocol email \
  --notification-endpoint $notification_endpoint \
  --region eu-central-1 \
  --profile $profile