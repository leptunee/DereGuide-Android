# DereGuide Android

DereGuide Android 是《偶像大师 灰姑娘女孩 星光舞台》的非官方辅助工具的安卓版本。这个应用帮助玩家管理卡片、查看角色信息、模拟Live演出、组建队伍等等。

## 主要功能

### 🎴 卡片查看器
- 浏览所有卡片信息
- 按属性、稀有度筛选
- 搜索特定卡片
- 查看卡片详细信息和技能

### 🎵 歌曲和谱面
- 查看所有歌曲信息
- 谱面查看器和播放器
- 难度等级和音符统计
- 歌曲预览播放

### 👥 角色信息
- 完整的角色资料
- 生日提醒功能
- 角色相关卡片查看
- 声优信息

### ⚡ 队伍编辑器
- 创建和管理多个队伍
- Live分数模拟器
- 队伍总值计算
- 最佳队伍推荐

### 🎲 扭蛋模拟器
- 当前和历史卡池查看
- 扭蛋概率显示
- 模拟抽卡功能
- 抽卡统计

### 🎉 活动信息
- 当前和历史活动
- 活动奖励查看
- 排名信息
- 活动倒计时

### 🔍 好友查找
- 搜索游戏好友
- 支援卡片查看
- 好友列表管理

## 技术栈

### 核心技术
- **Kotlin** - 主要开发语言
- **Jetpack Compose** - 现代UI框架
- **Material Design 3** - UI设计系统

### 架构组件
- **MVVM架构模式** - 清晰的代码架构
- **Room数据库** - 本地数据存储
- **Retrofit** - 网络请求
- **Hilt** - 依赖注入
- **Navigation Component** - 页面导航

### 其他库
- **Coil** - 图片加载
- **Gson** - JSON解析
- **Work Manager** - 后台任务
- **Coroutines & Flow** - 异步编程

## 项目结构

```
app/
├── src/main/java/com/dereguide/android/
│   ├── DereGuideApplication.kt          # 应用主类
│   ├── data/                            # 数据层
│   │   ├── api/                        # 网络API
│   │   ├── database/                   # 本地数据库
│   │   ├── model/                      # 数据模型
│   │   └── repository/                 # 数据仓库
│   ├── di/                             # 依赖注入
│   ├── ui/                             # UI层
│   │   ├── components/                 # 可复用组件
│   │   ├── screens/                    # 页面
│   │   ├── theme/                      # 主题样式
│   │   └── viewmodel/                  # ViewModel
│   └── worker/                         # 后台任务
└── src/main/res/                       # 资源文件
    ├── drawable/                       # 图标和图片
    ├── values/                         # 字符串、颜色等
    └── xml/                           # XML配置
```

## 开始使用

### 环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- Android SDK 34
- Kotlin 1.9.10 或更高版本
- 最低支持 Android 7.0 (API 24)

### 编译和运行
1. 克隆项目到本地
2. 使用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器
5. 点击运行按钮或使用快捷键 `Ctrl+R`

### 构建Release版本
```bash
./gradlew assembleRelease
```

## 开发指南

### 代码规范
- 遵循 Kotlin 官方编码规范
- 使用 ktlint 进行代码格式检查
- 变量和函数使用驼峰命名法
- 类名使用帕斯卡命名法

### 提交规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 代码重构
- test: 测试相关
- chore: 构建过程或辅助工具变动

## 数据来源

本应用的数据来源于官方API和社区维护的数据库。我们尊重版权，仅用于学习和交流目的。

## 免责声明

这是一个非官方的粉丝项目，与 Bandai Namco Entertainment 没有关联。《偶像大师 灰姑娘女孩 星光舞台》是 Bandai Namco Entertainment 的商标。

## 许可证

本项目仅供学习和研究使用。请勿用于商业用途。

## 贡献

欢迎提交 Issue 和 Pull Request 来帮助改进这个项目！

## 联系我们

如果您有任何问题或建议，请通过 GitHub Issues 联系我们。

---

**注意**: 这是一个移植项目，从 iOS 版本移植到 Android 平台。我们努力保持功能的一致性和用户体验的连贯性。
