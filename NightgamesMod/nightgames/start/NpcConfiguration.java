package nightgames.start;

import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.NPC;
import nightgames.characters.Personality;
import nightgames.items.Item;

class NpcConfiguration extends CharacterConfiguration {

    private Optional<String> type;

    private NpcConfiguration() {
        type = Optional.empty();
    }

    NPC build(Optional<NpcConfiguration> common) {
        if (!type.isPresent() && common.isPresent()) {
            throw new IllegalStateException("No type for npc");
        } else if (!common.isPresent()) {
            throw new UnsupportedOperationException("Tried to build NPC from all_npcs configuration");
        }
        Personality pers = Personality.getByType(type.get());
        Item trophy = pers.getCharacter().getTrophy();
        NPC npc;
        pers.setCharacter(npc = new NPC(name.orElse(type.get()), level, pers));
        npc.setTrophy(trophy);
        if (common.isPresent()) {
           common.get().processCommon(npc);
        }
        processCommon(npc);
        return npc;
    }

    public static NpcConfiguration parse(JSONObject obj) {
        NpcConfiguration cfg = new NpcConfiguration();
        cfg.parseCommon(obj);
        cfg.type = getIfExists(obj, "type", Object::toString);
        /*if (!cfg.type.isPresent()) {
            throw new RuntimeException("Error: Tried to specify an NPC without a type! (" + cfg.name.orElse("no name") + ")");
        }*/
        return cfg;
    }

}
