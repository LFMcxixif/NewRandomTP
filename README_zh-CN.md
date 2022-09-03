# NewRandomTP
Minecraft随机传送插件

### 版本
理论全版本可用

### 指令&权限
| 指令        | 权限              | 描述        |
| ----------- | ----------------- | ---------- |
| /nrtp        | -                 | 主命令      |
| /nrtp reload | newrandomtp.admin | 重载配置文件 |

| 权限                 | 描述       |
| -------------------- | ---------- |
| newrandomtp.cooldown | 跳过传送冷却 |
| newrandomtp.admin    | 管理员      |

### 配置文件
```yml
#Language
#English: en-US, Simplified Chinese: zh-CN, Traditional Chinese: zh-TW
Language: "en-US"

# Teleport Delay (Second)
Delay: 3

# Teleport Cooldown (Second)
Cooldown: 30

# Available World
World:
  - "world"
  - "world_nether"
  - "world_the_end"

# Minimum Teleport Range
MinTeleportX: 250
MinTeleportZ: 250

# Maximum Teleport Range
MaxTeleportX: 500
MaxTeleportZ: 500
```
