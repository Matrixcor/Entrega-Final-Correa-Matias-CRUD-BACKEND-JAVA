# Start app with default profile (MySQL) and check DB connection
$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir\..\

Write-Host "[1] Kill existing project jar processes (if any)..."
$matches = Get-WmiObject Win32_Process | Where-Object { $_.CommandLine -and ($_.CommandLine -match 'crud-0.0.1-SNAPSHOT.jar') }
if ($matches) {
  foreach ($m in $matches) {
    Write-Host "Found PID: $($m.ProcessId) CmdLine: $($m.CommandLine)"
    try { Stop-Process -Id $m.ProcessId -Force -ErrorAction Stop; Write-Host "Stopped PID $($m.ProcessId)" } catch { Write-Host "Failed to stop PID $($m.ProcessId): $_" }
  }
} else {
  Write-Host "No existing jar processes found."
}

if (-not (Test-Path .\logs)) { New-Item -ItemType Directory -Path .\logs | Out-Null }
$out = Join-Path (Get-Location) 'logs\\mysql-app.log'
$err = Join-Path (Get-Location) 'logs\\mysql-app.err'
$jarPath = Join-Path (Get-Location) 'target\\crud-0.0.1-SNAPSHOT.jar'
if (-not (Test-Path $jarPath)) { Write-Host "Jar not found at $jarPath"; exit 1 }
$jar = (Resolve-Path $jarPath).ProviderPath

Write-Host "[2] Starting jar with default profile (MySQL): $jar"
$proc = Start-Process -FilePath 'java' -ArgumentList '-jar', $jar -RedirectStandardOutput $out -RedirectStandardError $err -PassThru
Write-Host "Started process PID: $($proc.Id)"

Write-Host "[3] Waiting for startup (8s)..."
Start-Sleep -Seconds 8

Write-Host "[4] Tail stdout log (last 200 lines):"
if (Test-Path $out) { Get-Content $out -Tail 200 | ForEach-Object { Write-Host $_ } } else { Write-Host "No stdout log yet" }

Write-Host "[5] Search logs for Hikari / connection errors"
$logContent = ''
if (Test-Path $out) { $logContent += (Get-Content $out -Raw) }
if (Test-Path $err) { $logContent += (Get-Content $err -Raw) }

if ($logContent -match 'HikariPool-1 - Added connection' -or $logContent -match 'HikariPool-1 - Start completed' -or $logContent -match 'Connected to') {
  Write-Host "DB connection appears successful (Hikari reports a connection)."
} elseif ($logContent -match 'Cannot load driver' -or $logContent -match 'Communications link failure' -or $logContent -match 'Access denied' -or $logContent -match 'Connection refused') {
  Write-Host "DB connection failed. Relevant log lines:"; Select-String -InputObject $logContent -Pattern 'Cannot load driver|Communications link failure|Access denied|Connection refused' | ForEach-Object { Write-Host $_ }
} else {
  Write-Host "Could not determine DB connection status from logs. Showing recent Hikari and SQL lines:"; Select-String -InputObject $logContent -Pattern 'Hikari|jdbc|SQL|Communications|Access denied|Connection refused' | ForEach-Object { Write-Host $_ }
}

Write-Host "[6] Stopping started process PID $($proc.Id)"
try { Stop-Process -Id $proc.Id -Force -ErrorAction Stop; Write-Host "Stopped PID $($proc.Id)" } catch { Write-Host "Failed to stop PID $($proc.Id): $_" }

Write-Host "Done. Logs: $out and $err"
