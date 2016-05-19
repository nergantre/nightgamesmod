package nightgames.nskills.effects;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;

public class TemptationSkillEffect extends AbstractRollBasedSkillEffect {

    protected TemptationSkillEffect(Optional<String> withPart) {
        super((results, roll) -> {
            Character self = results.getSelf()
                                    .getCharacter();
            Character other = results.getOther()
                                     .getCharacter();
            Optional<BodyPart> maybeSelfPart = results.getSelf()
                                                      .getParts()
                                                      .stream()
                                                      .filter(part -> withPart.isPresent() && part.isType(withPart.get()))
                                                      .findAny();
            if (roll > .01) {
                self.tempt(results.getCombat(), other, maybeSelfPart.orElse(null), (int) roll);;
            }
            return true;
        });
    }

    @Override
    public String getType() {
        return "temptation";
    }
}
