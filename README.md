[简体中文（Simplified Chinese）](https://github.com/LFMcxixif/NewRandomTP/blob/master/README_CN.md)

# NewRandomTP
Minecraft random teleport plugin

### Game Version
Theoretical full version available

### Command & Permissions
| Command     | Permission        | Describe                  |
| ----------- | ----------------- | ------------------------- |
| /tpr        | -                 | Main Command              |
| /tpr info   | -                 | View Delivery Parameters  |
| /tpr reload | newrandomtp.admin | Reload Configuration File |

| Permissions          | describe               |
| -------------------- | ---------------------- |
| newrandomtp.cooldown | Skip teleport cooldown |
| newrandomtp.admin    | Administrator          |

### Configuration File
```yml
#前缀
#Prefix
Prefix: "&5[NewRandomTP] &6> &r"

#传送冷却（秒）
#Teleport Cooldown (Second)
Cooldown: 30

#可用世界
#Available World
World:
  - "world"

#最小传送范围
#Minimum Teleport Range
MinTeleportX: 250
MinTeleportZ: 250

#最大传送范围
#Maximum Teleport Range
MaxTeleportX: 500
MaxTeleportZ: 500
```
