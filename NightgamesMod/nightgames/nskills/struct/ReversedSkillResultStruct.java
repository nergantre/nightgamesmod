package nightgames.nskills.struct;

import nightgames.combat.Combat;
import nightgames.nskills.SkillResult;

class ReversedSkillResultStruct implements SkillResultStruct {
    private final SkillResultStruct innerStruct;
    public ReversedSkillResultStruct(SkillResultStruct innerStruct) {
        this.innerStruct = innerStruct;
    }

    @Override
    public Combat getCombat() {
        return innerStruct.getCombat();
    }

    @Override
    public CharacterResultStruct getSelf() {
        return innerStruct.getOther();
    }

    @Override
    public CharacterResultStruct getOther() {
        return innerStruct.getSelf();
    }

    @Override
    public SkillResult getResult() {
        return innerStruct.getResult();
    }

    @Override
    public double getRoll() {
        return innerStruct.getRoll();
    }

    @Override
    public SkillResultStruct reversed() {
        return innerStruct;
    }
}
