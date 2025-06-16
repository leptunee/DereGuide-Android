#!/usr/bin/env powershell

# Test API endpoints
Write-Host "Testing Starlight API endpoints..."

# Test card list endpoint
Write-Host "`nTesting card list endpoint:"
try {
    $response = Invoke-RestMethod -Uri "https://starlight.kirara.ca/api/v1/list/card_t" -Method Get -TimeoutSec 10
    Write-Host "✓ Card list endpoint accessible, returned $($response.result.Count) cards"
    
    # Show first card as example
    if ($response.result.Count -gt 0) {
        $firstCard = $response.result[0]
        Write-Host "  Sample card: ID=$($firstCard.id), Name=$($firstCard.name_eng), Rarity=$($firstCard.rarity)"
    }
} catch {
    Write-Host "✗ Card list endpoint failed: $($_.Exception.Message)"
}

# Test character list endpoint
Write-Host "`nTesting character list endpoint:"
try {
    $response = Invoke-RestMethod -Uri "https://starlight.kirara.ca/api/v1/list/char_t" -Method Get -TimeoutSec 10
    Write-Host "✓ Character list endpoint accessible, returned $($response.result.Count) characters"
} catch {
    Write-Host "✗ Character list endpoint failed: $($_.Exception.Message)"
}

# Test specific card endpoint
Write-Host "`nTesting specific card endpoint:"
try {
    $cardListResponse = Invoke-RestMethod -Uri "https://starlight.kirara.ca/api/v1/list/card_t" -Method Get -TimeoutSec 10
    if ($cardListResponse.result.Count -gt 0) {
        $testCardId = $cardListResponse.result[0].id
        $cardResponse = Invoke-RestMethod -Uri "https://starlight.kirara.ca/api/v1/card_t/$testCardId" -Method Get -TimeoutSec 10
        Write-Host "✓ Specific card endpoint accessible: ID=$testCardId"
        
        # Check for icon_image_ref
        if ($cardResponse.result[0].icon_image_ref) {
            Write-Host "  Icon image ref: $($cardResponse.result[0].icon_image_ref)"
        }
    }
} catch {
    Write-Host "✗ Specific card endpoint failed: $($_.Exception.Message)"
}

Write-Host "`nAPI testing completed."
