package nightgames.nskills.effects;

import nightgames.nskills.struct.SkillResultStruct;

public class UserMessageSkillEffect implements SkillEffect {
    private final String message;

    public UserMessageSkillEffect(String message) {
        this.message = message;
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        results.getCombat()
               .write(results.getOther()
                             .getCharacter(), message);
        return true;
    }

    @Override
    public String getType() {
        return "message";
    }
}
