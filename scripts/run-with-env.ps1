#!/usr/bin/env pwsh
# Start the app using the credentials in application.properties
$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Write-Host "Starting Spring Boot (profile=test) using application.properties..."
$mvnw = Join-Path $scriptRoot '..\mvnw.cmd'
Start-Process -FilePath $mvnw -ArgumentList 'spring-boot:run','-Dspring-boot.run.profiles=test' -NoNewWindow
