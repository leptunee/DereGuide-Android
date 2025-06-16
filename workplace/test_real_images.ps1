# Test actual image URLs from API

Write-Host "=== Testing Real Image URLs ===" -ForegroundColor Green

$urls = @(
    "https://hidamarirhodonite.kirara.ca/icon_card/100001.png",
    "https://hidamarirhodonite.kirara.ca/card/100001.png"
)

foreach ($url in $urls) {
    Write-Host "Testing: $url" -ForegroundColor Yellow
    try {
        $response = Invoke-WebRequest -Uri $url -Method Head -TimeoutSec 10
        Write-Host "✓ Image accessible (Status: $($response.StatusCode))" -ForegroundColor Green
    } catch {
        Write-Host "✗ Image failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}
