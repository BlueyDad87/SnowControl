# Using the Commands In-Game


 Adding a Zone
Creates a new protected zone using minimum and maximum coordinates.
**Syntax:** /snowcontrol add <name> <minX> <minZ> <maxX> <maxZ>

*Examples:*
 
 Protect a 400x400 area around spawn:
 
 `/snowcontrol add spawn -200 -200 200 200`

 Protect a specific town:
  
 `/snowcontrol add townhall 450 -300 650 -100`
 

# Listing And Removing Active Zones
Prints every zone name and its coordinates in the chat so you can verify what is currently active.


```
/snowcontrol list
```

Removing a Zone
Deletes a previously created zone by its name.


```
/snowcontrol remove <name>
```
*Example:*

`/snowcontrol remove spawn`

---

### Reloading Configuration
Reloads your zone settings directly from the configuration file. Use this command if you manually edit `config/snowcontrol/zones.json` on disk and want the server to apply the changes without needing a full restart.

**Command:**
```
/snowcontrol reload
```
