# NewRandomTP
Minecraft随机传送插件

### 版本
理论全版本可用

### 指令&权限
| 指令         | 权限                           | 描述        | 控制台执行   | 玩家执行|
| ----------- | ------------------------------ | ----------- | ----------- | ------ |
| /tpr        | newrandomtp.cooldown (跳过冷却) | 主命令       | 否          | 是     |
| /tpr info   | 无                             | 查看传送参数 | 是          | 是      |
| /tpr reload | newrandomtp.admin              | 重载配置文件 | 是          | 是      |

### 配置文件
```yml
#插件前缀
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
