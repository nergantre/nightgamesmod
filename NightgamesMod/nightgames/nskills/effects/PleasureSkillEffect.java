package nightgames.nskills.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.nskills.struct.SkillResultStruct;

public class PleasureSkillEffect implements SkillEffect {
    private final String withBodyPart;
    private final String targetBodyPart;
    private final List<EffectNumberRoll> rolls;

    public PleasureSkillEffect(String withBodyPart, String targetBodyPart) {
        this.withBodyPart = withBodyPart;
        this.targetBodyPart = targetBodyPart;
        rolls = new ArrayList<>();
    }

    public PleasureSkillEffect addRoll(EffectNumberRoll roll) {
        rolls.add(roll);
        return this;
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        Character self = results.getSelf()
                                .getCharacter();
        Character other = results.getOther()
                                 .getCharacter();
        Optional<BodyPart> maybeSelfPart = results.getSelf()
                                                  .getParts()
                                                  .stream()
                                                  .filter(part -> part.isType(withBodyPart))
                                                  .findAny();
        Optional<BodyPart> maybeTargetPart = results.getOther()
                                                    .getParts()
                                                    .stream()
                                                    .filter(part -> part.isType(targetBodyPart))
                                                    .findAny();
        double total = rolls.stream()
                            .mapToDouble(roll -> roll.roll(results))
                            .sum();
        if (total > .01) {
            results.getSelf()
                   .getCharacter().body.pleasure(other, maybeSelfPart.orElse(other.body.getRandom(withBodyPart)),
                                   maybeTargetPart.orElse(self.body.getRandom(targetBodyPart)), total,
                                   results.getCombat());
        }
        return true;
    }

    @Override
    public String getType() {
        return "pleasure";
    }

}
