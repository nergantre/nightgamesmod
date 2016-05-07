package nightgames.global;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nightgames.characters.Character;

/**
 * Match data that will be instantiated/cleared on every new match.
 * 
 * NOTE: DO NOT USE IN COMBAT. You may cause problems where it's being modified in an NPC skill evaluation.
 */
public class MatchData {
    public class PlayerData {
        private Map<String, String> flags;

        public PlayerData() {
            flags = new HashMap<>();
        }

        public void setFlag(String flag, String val) {
            flags.put(flag, val);
        }

        public String getFlag(String flag) {
            return flags.get(flag);
        }
    }

    private Map<Character, PlayerData> playerData;

    public MatchData(Collection<Character> combatants) {
        playerData = new HashMap<>();
        combatants.forEach(player -> playerData.put(player, new PlayerData()));
    }

    public PlayerData getDataFor(Character character) {
        return playerData.get(character);
    }
}
