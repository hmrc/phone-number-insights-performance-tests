#!/usr/bin/env bash

sm2 --start PHONE_NUMBER_INSIGHTS_PROXY PHONE_NUMBER_INSIGHTS PHONE_NUMBER_GATEWAY INTERNAL_AUTH --appendArgs '{
        "PHONE_NUMBER_INSIGHTS_PROXY": [
            "-J-Dauditing.consumer.baseUri.port=6001",
            "-J-Dauditing.consumer.baseUri.host=localhost",
            "-J-Dmicroservice.services.access-control.enabled=true",
            "-J-Dmicroservice.services.access-control.allow-list.0=phone-number-gateway",
            "-J-Dmicroservice.services.access-control.allow-list.1=pni-performance-tests"
        ],
        "PHONE_NUMBER_INSIGHTS": [
            "-J-Dmicroservice.phone-number-insights.database.dbName=postgres",
            "-J-Dmicroservice.phone-number-insights.database.use-canned-data=true",
            "-J-Dauditing.enabled=true"
        ]
    }'