package nightgames.characters.body;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import nightgames.characters.body.mods.ArcaneMod;
import nightgames.characters.body.mods.CyberneticMod;
import nightgames.characters.body.mods.DivineMod;
import nightgames.characters.body.mods.ErrorMod;
import nightgames.characters.body.mods.FeralMod;
import nightgames.characters.body.mods.FieryMod;
import nightgames.characters.body.mods.GooeyMod;
import nightgames.characters.body.mods.PartMod;
import nightgames.characters.body.mods.DemonicMod;

public class BodyUtils {
    public static Map<CockMod, PartMod> EQUIVALENT_MODS = new HashMap<>();
    static {
        EQUIVALENT_MODS.put(CockMod.bionic, CyberneticMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.blessed, DivineMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.enlightened, FieryMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.error, ErrorMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.incubus, DemonicMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.primal, FeralMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.runic, ArcaneMod.INSTANCE);
        EQUIVALENT_MODS.put(CockMod.slimy, GooeyMod.INSTANCE);
    };

    public static <T, U> Optional<T> getKeyFromValue(Map<T, U> mapping, U value) {
        return mapping.entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey).findAny();         
    }
}
