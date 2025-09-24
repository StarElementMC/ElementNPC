### ElementNPC
这是一个基于Nukkit的轻量级NPC前置插件。通过数据包实现，不会在存档中创建真正的实体。

此插件仅作为前置组件，提供API调用。

### 使用

在pom.xml中添加StarElement仓库
```xml
    <repository>
        <id>se-repo</id>
        <name>StarElement Repository</name>
        <url>https://repo.starelement.net/maven-public/</url>
    </repository>
```

添加依赖
```xml
<dependency>
    <groupId>net.starelement</groupId>
    <artifactId>ElementNPC</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```