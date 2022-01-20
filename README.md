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
#Prefix
Prefix: "&5[随机传送] &6> &r"
#Teleport Cooldown (Second)
Cooldown: 30
#Available World
World:
  - "world"
#Minimum Teleport Range
MinTeleportX: 250
MinTeleportZ: 250
#Maximum Teleport Range
MaxTeleportX: 500
MaxTeleportZ: 500
```
