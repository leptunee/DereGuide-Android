# 测试 Starlight API 连接

Write-Host "=== 测试 Starlight API 连接 ===" -ForegroundColor Green

$apiBase = "https://starlight.kirara.ca/api/v1"

Write-Host "测试卡片列表端点..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$apiBase/list/card_t" -Method Get -TimeoutSec 10
    Write-Host "API响应成功，返回 $($response.result.Count) 条记录" -ForegroundColor Green
    Write-Host "前5张卡片:" -ForegroundColor Cyan
    $response.result | Select-Object -First 5 | ForEach-Object {
        Write-Host "  - ID: $($_.id), Name: $($_.name_only)" -ForegroundColor White
    }
} catch {
    Write-Host "API调用失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== 检查应用日志 ===" -ForegroundColor Green
Write-Host "请手动运行以下命令查看应用日志:" -ForegroundColor Yellow
Write-Host "adb logcat -v time -s CardRepository CardListViewModel" -ForegroundColor Cyan
