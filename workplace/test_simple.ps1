# Test Starlight API connection

Write-Host "=== Testing Starlight API ===" -ForegroundColor Green

$apiBase = "https://starlight.kirara.ca/api/v1"

Write-Host "Testing card list endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$apiBase/list/card_t" -Method Get -TimeoutSec 10
    Write-Host "API response success, returned $($response.result.Count) records" -ForegroundColor Green
    Write-Host "First 5 cards:" -ForegroundColor Cyan
    $response.result | Select-Object -First 5 | ForEach-Object {
        Write-Host "  - ID: $($_.id), Name: $($_.name_only)" -ForegroundColor White
    }
} catch {
    Write-Host "API call failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Check App Logs ===" -ForegroundColor Green
Write-Host "Run this command to check app logs:" -ForegroundColor Yellow
Write-Host "adb logcat -v time -s CardRepository CardListViewModel" -ForegroundColor Cyan
