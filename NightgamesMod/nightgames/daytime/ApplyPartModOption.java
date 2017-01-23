package nightgames.daytime;

import java.util.function.Function;

import nightgames.characters.Character;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.mods.PartMod;
import nightgames.characters.body.mods.SizeMod;
import nightgames.requirements.RequirementShortcuts;

public class ApplyPartModOption extends TransformationOption {
    public static Function<Character, Integer> createCostForNumberOfMods(String type) {
        return (c) -> 1600 * (int)Math.pow(5, c.body.getRandom(type).getMods().stream().filter(mod -> !(mod instanceof SizeMod)).count());
    }

    public ApplyPartModOption(String type, PartMod mod) {
        super();
        moneyCost = createCostForNumberOfMods(type);
        requirements.add(RequirementShortcuts.bodypart(type));
        requirements.add(RequirementShortcuts.noPartmod(type, mod));
        effect = (c, self, other) -> {
            GenericBodyPart target = (GenericBodyPart) self.body.getRandom(type);
            self.body.remove(target);
            self.body.add(target.applyMod(mod));
            return true;
        };
    }
}
