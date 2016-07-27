package nightgames.nskills.effects;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.nskills.struct.SkillResultStruct;

public class SeparatedMessageSkillEffect implements SkillEffect {
    private final String message;
    private Optional<String> npcMessage;

    protected SeparatedMessageSkillEffect(String message) {
        this.message = message;
        this.npcMessage = Optional.empty();
    }
    
    public SeparatedMessageSkillEffect andNPCMessage(String message) {
        npcMessage = Optional.ofNullable(message);
        return this;
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        Character self = results.getSelf().getCharacter();
        Character other = results.getOther().getCharacter();
        if (!self.human() && npcMessage.isPresent()) {
            results.getCombat().write(self, Global.format(npcMessage.get(), self, other));
        } else {
            results.getCombat().write(self, Global.format(message, self, other));
        }
        return true;
    }

    @Override
    public String getType() {
        return "message";
    }
}
