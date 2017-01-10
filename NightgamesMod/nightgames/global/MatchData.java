package nightgames.global;

import java.util.HashMap;
import java.util.Map;

import nightgames.characters.Character;
import nightgames.pet.arms.ArmManager;

/**
 * Match data that will be instantiated/cleared on every new match.
 * 
 * NOTE: DO NOT USE IN COMBAT. You may cause problems where it's being modified in an NPC skill evaluation.
 */
public class MatchData {
    public class PlayerData {
        private Map<String, String> flags;
        private ArmManager manager;
        public PlayerData() {
            flags = new HashMap<>();
            manager = new ArmManager();
        }

        public void setFlag(String flag, String val) {
            flags.put(flag, val);
        }

        public String getFlag(String flag) {
            return flags.get(flag);
        }

        public ArmManager getArmManager() {
            return manager;
        }

        public void setArmManager(ArmManager manager) {
            this.manager = manager.instance();
        }
    }

    private Map<Character, PlayerData> playerData;

    public MatchData() {
        playerData = new HashMap<>();
    }

    public PlayerData getDataFor(Character character) {
        playerData.putIfAbsent(character, new PlayerData());
        return playerData.get(character);
    }
}
