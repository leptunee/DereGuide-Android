# Test if card image URLs are accessible

Write-Host "=== Testing Card Image URLs ===" -ForegroundColor Green

$cardId = 100001
$imageTypes = @(
    @{Type="Icon"; Suffix=1},
    @{Type="Card"; Suffix=2},
    @{Type="Spread"; Suffix=3}
)

foreach ($imageType in $imageTypes) {
    $url = "https://starlight.kirara.ca/card_image_t/${cardId}_0$($imageType.Suffix).jpg"
    Write-Host "Testing $($imageType.Type) image: $url" -ForegroundColor Yellow
    
    try {
        $response = Invoke-WebRequest -Uri $url -Method Head -TimeoutSec 10
        if ($response.StatusCode -eq 200) {
            Write-Host "✓ Image accessible (Status: $($response.StatusCode))" -ForegroundColor Green
        } else {
            Write-Host "✗ Image not accessible (Status: $($response.StatusCode))" -ForegroundColor Red
        }
    } catch {
        Write-Host "✗ Image failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}
