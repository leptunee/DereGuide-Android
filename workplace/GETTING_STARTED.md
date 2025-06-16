# DereGuide Android - 快速开始指南

欢迎使用 DereGuide Android！这是一个完整的《偶像大师 灰姑娘女孩 星光舞台》辅助工具的安卓版本。

## 🚀 快速开始

### 环境要求
- **Android Studio**: 建议使用最新版本 (Hedgehog 2023.1.1 或更高)
- **JDK**: Java 17 或更高版本
- **Android SDK**: API 34 (Android 14)
- **最低支持版本**: Android 7.0 (API 24)

### 构建项目

1. **打开项目**
   ```bash
   # 使用Android Studio打开项目文件夹
   # 或者通过命令行：
   cd DereGuide-Android/workplace
   ```

2. **同步依赖**
   ```bash
   # Windows
   .\gradlew.bat build
   
   # Linux/Mac
   ./gradlew build
   ```

3. **运行项目**
   - 在Android Studio中点击运行按钮
   - 或使用命令行：
   ```bash
   .\gradlew.bat installDebug
   ```

## 📱 主要功能

### ✨ 已实现功能
- **现代化UI**: 使用Jetpack Compose和Material Design 3
- **卡片浏览器**: 完整的卡片数据库和搜索功能
- **角色资料**: 详细的角色信息和生日提醒
- **歌曲数据库**: 歌曲信息和难度数据
- **队伍编辑**: 创建和管理自定义队伍
- **离线优先**: 数据缓存和离线访问

### 🔄 开发中功能
- **谱面播放器**: 歌曲谱面可视化
- **Live分数计算器**: 精确的分数模拟
- **扭蛋模拟器**: 卡池模拟和统计
- **活动信息**: 当前和历史活动数据
- **好友系统**: 支援卡查找

## 🏗️ 项目架构

```
app/
├── src/main/java/com/dereguide/android/
│   ├── DereGuideApplication.kt        # 应用入口
│   ├── data/                          # 数据层
│   │   ├── api/                      # 网络API
│   │   ├── database/                 # 本地数据库 (Room)
│   │   ├── model/                    # 数据模型
│   │   └── repository/               # 数据仓库 (Repository模式)
│   ├── di/                           # 依赖注入 (Hilt)
│   ├── ui/                           # UI层
│   │   ├── components/               # 可复用组件
│   │   ├── screens/                  # 页面
│   │   ├── theme/                    # 主题样式
│   │   └── viewmodel/                # ViewModel (MVVM)
│   └── worker/                       # 后台任务 (WorkManager)
```

## 🛠️ 开发工具

### VS Code扩展 (推荐)
- **Android for VS Code**: Android开发支持
- **Kotlin Language**: Kotlin语法高亮
- **Gradle for Java**: Gradle构建支持

### 调试和测试
```bash
# 运行单元测试
.\gradlew.bat test

# 运行UI测试
.\gradlew.bat connectedAndroidTest

# 生成APK
.\gradlew.bat assembleRelease
```

## 📊 技术栈

- **🏗️ 架构**: MVVM + Repository Pattern
- **🎨 UI**: Jetpack Compose + Material Design 3
- **💾 数据库**: Room + SQLite
- **🌐 网络**: Retrofit + OkHttp
- **🔧 依赖注入**: Hilt (Dagger)
- **⚡ 异步**: Kotlin Coroutines + Flow
- **📷 图片**: Coil
- **🔔 通知**: WorkManager

## 🎯 开发指南

### 添加新功能
1. 在 `data/model/` 中定义数据模型
2. 在 `data/database/` 中添加DAO接口
3. 在 `data/repository/` 中实现Repository
4. 在 `ui/viewmodel/` 中创建ViewModel
5. 在 `ui/screens/` 中实现UI界面

### 代码规范
- 使用Kotlin官方代码规范
- ViewModel不应包含Android依赖
- 使用StateFlow管理UI状态
- 遵循Material Design指南

## 🎮 游戏数据

本应用使用的数据来源：
- **官方API**: 卡片、角色、歌曲数据
- **社区数据**: 活动信息、扭蛋数据
- **用户数据**: 队伍配置、设置偏好

## 📄 许可证

本项目仅供学习和研究使用。请勿用于商业用途。

《偶像大师 灰姑娘女孩 星光舞台》是 Bandai Namco Entertainment 的商标。

## 🤝 贡献

欢迎提交Issue和Pull Request！

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开Pull Request

## 📞 支持

如有问题，请通过以下方式联系：
- 提交GitHub Issue
- 查看项目Wiki
- 参与社区讨论

## 🔧 同步问题解决方案

### 如果Gradle同步失败：

1. **检查网络连接**
   ```bash
   # 确保网络可以访问：
   # - https://services.gradle.org
   # - https://repo1.maven.org
   # - https://dl.google.com
   ```

2. **清理项目缓存**
   ```bash
   # 在项目根目录执行：
   .\gradlew.bat clean
   
   # 或者在Android Studio中：
   # Build → Clean Project
   ```

3. **重新下载依赖**
   ```bash
   # 删除Gradle缓存（如果需要）：
   # Windows: %USERPROFILE%\.gradle\caches
   # 然后重新同步项目
   ```

4. **检查Gradle版本**
   - 确保 `gradle/wrapper/gradle-wrapper.properties` 中的版本正确
   - 当前配置：`gradle-8.2-bin.zip`

## ⚠️ 常见构建问题解决

### 内存不足错误 (OutOfMemoryError)
如果遇到 "Java heap space" 或 "The Daemon will expire immediately" 错误：

1. **检查gradle.properties配置**
   - 项目已包含 `gradle.properties` 文件
   - 配置了4GB堆内存：`-Xmx4096m`
   - 如果仍然不够，可以增加到 `-Xmx6144m` 或 `-Xmx8192m`

2. **清理项目缓存**
   ```bash
   # 停止所有Gradle守护进程
   .\gradlew.bat --stop
   
   # 清理项目
   .\gradlew.bat clean
   
   # 重新构建
   .\gradlew.bat build
   ```

3. **Android Studio内存设置**
   - Help → Edit Custom VM Options
   - 添加或修改：
   ```
   -Xmx4096m
   -XX:MaxMetaspaceSize=512m
   ```

### Gradle版本兼容性
当前项目使用：
- **Gradle**: 8.12
- **Android Gradle Plugin**: 8.7.0  
- **Kotlin**: 1.9.20
- **Java Target**: 17

## 🔧 在Android Studio中解决内存问题

### 停止Gradle守护进程

#### 方法一：通过Android Studio界面
1. **打开Gradle工具窗口**
   - 在Android Studio右侧边栏点击 **"Gradle"** 标签
   - 或者使用菜单：**View → Tool Windows → Gradle**

2. **停止守护进程**
   - 在Gradle窗口中，找到并点击 **"Stop Gradle Daemons"** 按钮（🛑图标）
   - 或者在Gradle窗口顶部工具栏中找到停止按钮

#### 方法二：通过Terminal
1. **打开Android Studio内置终端**
   - 在Android Studio底部点击 **"Terminal"** 标签
   - 或者使用快捷键：**Alt+F12**

2. **执行停止命令**
   ```bash
   # Windows
   .\gradlew.bat --stop
   
   # 或者直接杀死所有Java进程（谨慎使用）
   taskkill /f /im java.exe
   ```

#### 方法三：通过系统任务管理器
1. 按 **Ctrl+Shift+Esc** 打开任务管理器
2. 找到所有 **"OpenJDK Platform binary"** 或 **"java.exe"** 进程
3. 选择并 **"结束任务"**

### 清理和重建项目

#### 1. 清理项目缓存
```bash
# 在Android Studio Terminal中执行：

# 停止所有Gradle守护进程
.\gradlew.bat --stop

# 清理项目
.\gradlew.bat clean

# 清理Gradle缓存（可选，如果问题严重）
.\gradlew.bat cleanBuildCache
```

#### 2. 通过Android Studio菜单清理
1. **清理项目**
   - 菜单：**Build → Clean Project**
   - 等待清理完成

2. **重建项目**
   - 菜单：**Build → Rebuild Project**
   - 这会删除所有生成的文件并重新构建

#### 3. 清理IDE缓存（终极解决方案）
如果问题仍然存在：
1. **清理IDE缓存**
   - 菜单：**File → Invalidate Caches and Restart**
   - 选择 **"Invalidate and Restart"**
   - Android Studio会重启并清理所有缓存

### 验证内存配置生效

1. **检查gradle.properties文件是否创建**
   - 在项目根目录下应该有 `gradle.properties` 文件
   - 确保包含正确的内存设置

2. **重新同步项目**
   - 点击工具栏的 **"Sync Project with Gradle Files"** 按钮（🔄）
   - 或者使用快捷键：**Ctrl+Shift+O**

3. **查看Gradle同步日志**
   - 在底部的 **"Build"** 窗口查看同步进度
   - 确保没有内存相关的警告

### 完整的问题解决流程

按以下顺序执行：

```bash
# 1. 停止所有Gradle进程
.\gradlew.bat --stop

# 2. 清理项目
.\gradlew.bat clean

# 3. 重新同步（在Android Studio中点击同步按钮）

# 4. 尝试构建
.\gradlew.bat assembleDebug
```

### 如果问题仍然存在

#### 检查Java版本
```bash
# 检查Java版本
java -version

# 应该显示Java 17或更高版本
```

#### 增加更多内存（如果机器配置允许）
在 `gradle.properties` 中调整：
```properties
# 如果您的电脑内存充足（16GB+），可以设置更高
org.gradle.jvmargs=-Xmx6g -Dfile.encoding=UTF-8
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.configureondemand=true
```

#### 临时禁用并行构建
```properties
# 在gradle.properties中临时添加
org.gradle.parallel=false
```

### 监控构建过程

1. **查看内存使用情况**
   - 在构建过程中观察任务管理器
   - 确保Java进程内存使用正常

2. **查看构建输出**
   - 在Android Studio的 **"Build"** 窗口查看详细日志
   - 寻找任何错误或警告信息

现在按照这个流程操作，应该能够解决内存问题并成功构建项目。

---

**祝你使用愉快！プロデューサーさん、頑張って！** 🌟
