package nightgames.nskills.effects.rolls;

import nightgames.characters.Trait;
import nightgames.nskills.struct.SkillResultStruct;

public class TraitBonusRoll extends BasicNumberRoll {
    private final Trait trait;

    public TraitBonusRoll(Trait trait, int start, int end) {
        super(start, end);
        this.trait = trait;
    }

    @Override
    public double roll(SkillResultStruct result) {
        if (result.getSelf().getCharacter().has(trait)) {
            return super.roll(result);
        }
        return 0;
    }
}
