package nightgames.nskills.struct;

import nightgames.combat.Combat;
import nightgames.nskills.SkillResult;

public interface SkillResultStruct {
    Combat getCombat();

    CharacterResultStruct getSelf();

    CharacterResultStruct getOther();

    SkillResult getResult();

    double getRoll();
    
    SkillResultStruct reversed();
}
