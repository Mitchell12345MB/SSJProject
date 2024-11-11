package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class SSJSkillManager {
    private final SSJ ssj;
    
    public SSJSkillManager(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public boolean canUseSkill(Player player, String skillName) {
        FileConfiguration skillConfig = ssj.getSSJConfigs().getSCFile();
        ConfigurationSection skillSection = skillConfig.getConfigurationSection(skillName);
        
        if (skillSection == null) return false;
        
        // Check AP requirement
        int requiredAP = skillSection.getInt("APReq");
        int playerAP = ssj.getSSJPCM().getActionPoints(player);
        
        if (playerAP < requiredAP) return false;
        
        // Check stat requirements
        ConfigurationSection statReq = skillSection.getConfigurationSection("Stat_Req");
        if (statReq != null) {
            if (ssj.getSSJPCM().getPower(player) < statReq.getInt("Power")) return false;
            if (ssj.getSSJPCM().getStrength(player) < statReq.getInt("Strength")) return false;
        }
        
        return true;
    }
}
