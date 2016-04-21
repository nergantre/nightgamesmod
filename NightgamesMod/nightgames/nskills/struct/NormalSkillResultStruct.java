package nightgames.nskills.struct;

import nightgames.combat.Combat;
import nightgames.nskills.SkillResult;

public class NormalSkillResultStruct implements SkillResultStruct {
    protected final Combat c;
    protected final CharacterResultStruct selfResults;
    protected final CharacterResultStruct otherResults;
    protected final SkillResult result;
    protected final int roll;

    public Combat getCombat() {
        return c;
    }

    public CharacterResultStruct getSelf() {
        return selfResults;
    }

    public CharacterResultStruct getOther() {
        return otherResults;
    }

    public SkillResult getResult() {
        return result;
    }

    public double getRoll() {
        return roll;
    }

    public NormalSkillResultStruct(Combat c, CharacterResultStruct self, CharacterResultStruct other,
                    SkillResult result, int roll) {
        this.c = c;
        this.selfResults = self;
        this.otherResults = other;
        this.result = result;
        this.roll = roll;
    }
    
    public SkillResultStruct reversed() {
        return new ReversedSkillResultStruct(this);
    }
}
