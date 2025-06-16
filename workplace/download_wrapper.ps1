# PowerShell script to download gradle-wrapper.jar
$url = "https://repo1.maven.org/maven2/org/gradle/gradle-wrapper/8.12/gradle-wrapper-8.12.jar"
$output = "gradle\wrapper\gradle-wrapper.jar"

Write-Host "正在下载 gradle-wrapper.jar..."
try {
    Invoke-WebRequest -Uri $url -OutFile $output -UseBasicParsing
    Write-Host "下载完成！"
    $fileInfo = Get-Item $output
    Write-Host "文件大小: $($fileInfo.Length) 字节"
} catch {
    Write-Host "下载失败: $_"
    # 备用下载链接
    $backupUrl = "https://github.com/gradle/gradle/raw/v8.12.0/gradle/wrapper/gradle-wrapper.jar"
    Write-Host "尝试备用链接..."
    try {
        Invoke-WebRequest -Uri $backupUrl -OutFile $output -UseBasicParsing
        Write-Host "备用下载完成！"
        $fileInfo = Get-Item $output
        Write-Host "文件大小: $($fileInfo.Length) 字节"
    } catch {
        Write-Host "备用下载也失败: $_"
    }
}
