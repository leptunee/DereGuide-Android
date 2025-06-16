# Test Starlight API card detail to see image URLs

Write-Host "=== Testing Starlight API Card Detail ===" -ForegroundColor Green

$apiBase = "https://starlight.kirara.ca/api/v1"
$cardId = 100001  # First card ID

Write-Host "Testing card detail endpoint for card $cardId..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$apiBase/card_t/$cardId" -Method Get -TimeoutSec 10
    Write-Host "API response success" -ForegroundColor Green
    Write-Host "Card detail:" -ForegroundColor Cyan
    Write-Host "  - ID: $($response.result.id)" -ForegroundColor White
    Write-Host "  - Name: $($response.result.name)" -ForegroundColor White
    Write-Host "  - Chara ID: $($response.result.chara_id)" -ForegroundColor White
    Write-Host "  - Has Spread: $($response.result.has_spread)" -ForegroundColor White
    Write-Host "  - Evolution ID: $($response.result.evolution_id)" -ForegroundColor White
    
    Write-Host "`nImage URLs analysis:" -ForegroundColor Yellow
    Write-Host "Base Image URL pattern: https://starlight.kirara.ca/card_image_t/[id]_[suffix].jpg" -ForegroundColor Cyan
    Write-Host "Icon URL: https://starlight.kirara.ca/card_image_t/$($response.result.id)_01.jpg" -ForegroundColor White
    Write-Host "Card URL: https://starlight.kirara.ca/card_image_t/$($response.result.id)_02.jpg" -ForegroundColor White
    if ($response.result.has_spread) {
        Write-Host "Spread URL: https://starlight.kirara.ca/card_image_t/$($response.result.id)_03.jpg" -ForegroundColor White
    }
} catch {
    Write-Host "API call failed: $($_.Exception.Message)" -ForegroundColor Red
}
