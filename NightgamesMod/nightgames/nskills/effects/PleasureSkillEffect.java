package nightgames.nskills.effects;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;

public class PleasureSkillEffect extends AbstractRollBasedSkillEffect {
    private final String withBodyPart;
    private final String targetBodyPart;

    protected PleasureSkillEffect(String withBodyPart, String targetBodyPart) {
        super((results, roll) -> {
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
            if (roll > .01) {
                results.getSelf()
                       .getCharacter().body.pleasure(other, maybeSelfPart.orElse(other.body.getRandom(withBodyPart)),
                                       maybeTargetPart.orElse(self.body.getRandom(targetBodyPart)), roll,
                                       results.getCombat());
            }
            return true;
        });
        this.withBodyPart = withBodyPart;
        this.targetBodyPart = targetBodyPart;
    }

    @Override
    public String getType() {
        return "pleasure";
    }

    public String getWithBodyPart() {
        return withBodyPart;
    }

    public String getTargetBodyPart() {
        return targetBodyPart;
    }
}
