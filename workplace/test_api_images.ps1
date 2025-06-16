# Test actual API response to see image URLs

Write-Host "=== Testing Actual API Response for Images ===" -ForegroundColor Green

$apiBase = "https://starlight.kirara.ca/api/v1"
$cardId = 100001

Write-Host "Testing card detail API response..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$apiBase/card_t/$cardId" -Method Get -TimeoutSec 10
    Write-Host "API response success" -ForegroundColor Green
    
    $card = $response.result
    Write-Host "`nImage URLs in API response:" -ForegroundColor Cyan
    
    if ($card.icon_image_ref) {
        Write-Host "Icon Image: $($card.icon_image_ref)" -ForegroundColor White
    } else {
        Write-Host "Icon Image: Not found" -ForegroundColor Red
    }
    
    if ($card.card_image_ref) {
        Write-Host "Card Image: $($card.card_image_ref)" -ForegroundColor White
    } else {
        Write-Host "Card Image: Not found" -ForegroundColor Red
    }
    
    if ($card.spread_image_ref) {
        Write-Host "Spread Image: $($card.spread_image_ref)" -ForegroundColor White
    } else {
        Write-Host "Spread Image: Not found" -ForegroundColor Red
    }
    
    Write-Host "`nAll keys in response:" -ForegroundColor Yellow
    $card.PSObject.Properties | Where-Object { $_.Name -like "*image*" } | ForEach-Object {
        Write-Host "  $($_.Name): $($_.Value)" -ForegroundColor White
    }
    
} catch {
    Write-Host "API call failed: $($_.Exception.Message)" -ForegroundColor Red
}
