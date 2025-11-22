# Runs the built jar, checks logs and endpoint, then stops it.
$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir\..\

Write-Host "[1] Looking for existing jar processes..."
$matches = Get-WmiObject Win32_Process | Where-Object { $_.CommandLine -and ($_.CommandLine -match 'crud-0.0.1-SNAPSHOT.jar') }
if ($matches) {
  foreach ($m in $matches) {
    Write-Host "Found PID: $($m.ProcessId) CmdLine: $($m.CommandLine)"
    try { Stop-Process -Id $m.ProcessId -Force -ErrorAction Stop; Write-Host "Stopped PID $($m.ProcessId)" } catch { Write-Host "Failed to stop PID $($m.ProcessId): $_" }
  }
} else {
  Write-Host "No existing jar processes found."
}

# Prepare logs
if (-not (Test-Path .\logs)) { New-Item -ItemType Directory -Path .\logs | Out-Null }
$out = Join-Path (Get-Location) 'logs\\app.log'
$err = Join-Path (Get-Location) 'logs\\app.err'
$jarPath = Join-Path (Get-Location) 'target\\crud-0.0.1-SNAPSHOT.jar'
if (-not (Test-Path $jarPath)) { Write-Host "Jar not found at $jarPath"; exit 1 }
$jar = (Resolve-Path $jarPath).ProviderPath
if (-not (Test-Path $jar)) { Write-Host "Jar not found at $jar"; exit 1 }

Write-Host "[2] Starting jar: $jar"
$proc = Start-Process -FilePath 'java' -ArgumentList '-jar', $jar, '--spring.profiles.active=test' -RedirectStandardOutput $out -RedirectStandardError $err -PassThru
Write-Host "Started process PID: $($proc.Id)"

Write-Host "[3] Waiting for startup (6s)..."
Start-Sleep -Seconds 6

Write-Host "[4] Tail stdout log (last 200 lines):"
if (Test-Path $out) { Get-Content $out -Tail 200 | ForEach-Object { Write-Host $_ } } else { Write-Host "No stdout log yet" }

Write-Host "[5] Tail stderr log (last 200 lines):"
if (Test-Path $err) { Get-Content $err -Tail 200 | ForEach-Object { Write-Host $_ } } else { Write-Host "No stderr log" }

Write-Host "[6] Checking port 8080..."
netstat -ano | Select-String ':8080' | ForEach-Object { Write-Host $_ }

Write-Host "[7] HTTP GET /api/articulos"
try {
  $r = Invoke-WebRequest -Uri 'http://localhost:8080/api/articulos' -Method GET -UseBasicParsing -TimeoutSec 10
  Write-Host "STATUS: $($r.StatusCode)"
  Write-Host $r.Content
} catch {
  Write-Host "Request failed: $($_.Exception.Message)"
}

Write-Host "[8] Stopping started process PID $($proc.Id)"
try { Stop-Process -Id $proc.Id -Force -ErrorAction Stop; Write-Host "Stopped PID $($proc.Id)" } catch { Write-Host "Failed to stop PID $($proc.Id): $_" }

Write-Host "[9] Final port check 8080"
netstat -ano | Select-String ':8080' | ForEach-Object { Write-Host $_ }

Write-Host "Done. Logs available at .\logs\app.log and .\logs\app.err"
