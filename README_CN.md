# NewRandomTP
Minecraft随机传送插件

### 版本
理论全版本可用

### 指令&权限
| 指令        | 权限              | 描述        |
| ----------- | ----------------- | ---------- |
| /tpr        | -                 | 主命令      |
| /tpr info   | -                 | 查看传送参数 |
| /tpr reload | newrandomtp.admin | 重载配置文件 |

| 权限                 | 描述       |
| -------------------- | ---------- |
| newrandomtp.cooldown | 跳过传送冷却 |
| newrandomtp.admin    | 管理员      |

### 配置文件
```yml
#前缀
Prefix: "&5[随机传送] &6> &r"
#传送冷却（秒）
Cooldown: 30
#可用世界
World:
  - "world"
#最小传送范围
MinTeleportX: 250
MinTeleportZ: 250
#最大传送范围
MaxTeleportX: 500
MaxTeleportZ: 500
```
