# Check card list API response structure

Write-Host "=== Checking Card List API Response ===" -ForegroundColor Green

$apiBase = "https://starlight.kirara.ca/api/v1"

try {
    $response = Invoke-RestMethod -Uri "$apiBase/list/card_t" -Method Get -TimeoutSec 10
    Write-Host "API response success, returned $($response.result.Count) cards" -ForegroundColor Green
    
    $firstCard = $response.result[0]
    Write-Host "`nFirst card structure:" -ForegroundColor Cyan
    $firstCard.PSObject.Properties | ForEach-Object {
        Write-Host "  $($_.Name): $($_.Value)" -ForegroundColor White
    }
    
    Write-Host "`nImage-related fields:" -ForegroundColor Yellow
    $firstCard.PSObject.Properties | Where-Object { $_.Name -like "*image*" -or $_.Name -like "*icon*" } | ForEach-Object {
        Write-Host "  $($_.Name): $($_.Value)" -ForegroundColor White
    }
    
} catch {
    Write-Host "API call failed: $($_.Exception.Message)" -ForegroundColor Red
}
