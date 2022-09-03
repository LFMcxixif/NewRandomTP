[简体中文（Simplified Chinese）](https://github.com/LFMcxixif/NewRandomTP/blob/master/README_zh-CN.md)

# NewRandomTP
Minecraft random teleport plugin

### Game Version
Theoretical full version available

### Command & Permissions
| Command    | Permission        | Describe                   |
|------------| ----------------- |----------------------------|
| /nrtp      | -                 | Main Command               |
| /nrtp reload | newrandomtp.admin | Reload Configuration File  |

| Permissions          | Describe               |
| -------------------- | ---------------------- |
| newrandomtp.cooldown | Skip teleport cooldown |
| newrandomtp.admin    | Administrator          |

### Configuration File
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
