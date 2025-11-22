#!/usr/bin/env pwsh
# The project no longer uses a .env file. Credentials and URLs are stored
# in `src/main/resources/application.properties`. This loader remains for
# compatibility but will not attempt to load a missing .env file.
Write-Host "No .env file used; credentials are read from application.properties."
exit 0
