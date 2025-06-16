# DereGuide API Test Script

Write-Host "Testing DereGuide API endpoints..." -ForegroundColor Green

function Test-ApiEndpoint {
    param([string]$Name, [string]$Url)
    
    Write-Host "Testing $Name..." -ForegroundColor Yellow
    Write-Host "URL: $Url" -ForegroundColor Gray
    
    try {
        $response = Invoke-RestMethod -Uri $Url -Method GET -TimeoutSec 10
        Write-Host "SUCCESS - Data type: $($response.GetType())" -ForegroundColor Green
        
        if ($response -is [array] -and $response.Length -gt 0) {
            Write-Host "Array length: $($response.Length)" -ForegroundColor Cyan
        }
        return $true
    }
    catch {
        Write-Host "FAILED: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
    Write-Host ""
}

# Test endpoints
$endpoints = @(
    @{ Name = "Starlight DB Cards"; Url = "https://starlight.tachibana.cool/api/v1/list/card_t" },
    @{ Name = "Starlight DB Characters"; Url = "https://starlight.tachibana.cool/api/v1/list/char_t" },
    @{ Name = "Deresute Info Cards"; Url = "https://deresute.info/api/cards" },
    @{ Name = "Force Update Config"; Url = "https://hoshimoriuta.kirara.ca/api/public/force_update.json" }
)

$successCount = 0
foreach ($endpoint in $endpoints) {
    if (Test-ApiEndpoint -Name $endpoint.Name -Url $endpoint.Url) {
        $successCount++
    }
}

Write-Host "Test completed: $successCount/$($endpoints.Length) endpoints available" -ForegroundColor Green
