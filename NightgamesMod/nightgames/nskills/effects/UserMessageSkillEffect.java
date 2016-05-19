package nightgames.nskills.effects;

import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.nskills.struct.SkillResultStruct;

public class UserMessageSkillEffect implements SkillEffect {
    private final String message;

    protected UserMessageSkillEffect(String message) {
        this.message = message;
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        Character self = results.getSelf().getCharacter();
        Character other = results.getOther().getCharacter();
        results.getCombat().write(self, Global.format(message, self, other));
        return true;
    }

    @Override
    public String getType() {
        return "message";
    }
}
