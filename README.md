# TestFX Neon Playground (JavaFX)

这是一个使用 **JavaFX 17** 构建的桌面演示项目，展示“Neon Playground”交互式界面，包括动态统计卡片、实时折线图和可调节的主题/霓虹效果。主启动类位于 `com.example.testfx.HelloApplication`。

## 环境要求
- JDK 17+
- Maven 3.9+（仓库首次拉取依赖需要联网）
- 桌面环境可运行 JavaFX（Linux 上请确保安装了适配的 OpenGL/GTK 组件）

## 运行与打包
```bash
# 运行 JavaFX 应用（推荐在联网环境下首次执行）
./mvnw clean javafx:run

# 构建可分发的可执行 JAR（跳过测试）
./mvnw -DskipTests package
```

## 测试
项目当前只包含演示界面，没有单元测试；如需验证构建流程，可执行：
```bash
./mvnw test
```

## 常见问题
- **依赖下载失败 / Maven 无法联网**：首次构建需要下载 JavaFX 与第三方库，请确保网络可访问 Maven 中央仓库；离线环境下需预先将依赖缓存到本地仓库。
- **JavaFX 运行报错**：确认 JDK 版本为 17+，并在带有桌面图形环境的机器上运行。

