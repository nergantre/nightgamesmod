package nightgames.stance;

import nightgames.global.Global;
import nightgames.nskills.tags.BothRequirementSkillTag;
import nightgames.nskills.tags.SkillTag;

public enum Stance {
    behind,
    cowgirl,
    doggy,
    missionary,
    mount,
    neutral,
    pin,
    reversecowgirl,
    reversemount,
    sixnine,
    standing,
    standingover,
    anal,
    flying,
    trib,
    behindfootjob,
    nursing,
    facesitting,
    smothering,
    breastsmothering,
    engulfed,
    oralpin,
    paizuripin,
    titfucking,
    flowertrap,
    coiled,
    upsidedownmaledom,
    upsidedownfemdom, 
    kneeling, 
    succubusembrace,
    incubusembrace;

    public SkillTag getSkillTag() {
        return new BothRequirementSkillTag((result, value) -> result.getCombat().getStance().en == this, Global.capitalizeFirstLetter(name())+"Position");
    }
}
