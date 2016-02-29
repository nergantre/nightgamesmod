package nightgames.nskills.tags;

import nightgames.nskills.struct.SkillResultStruct;
import nightgames.status.Stsflag;

public class KnockdownSkillTag extends SkillTag {
    private final SkillRequirement requirement = new SkillRequirement() {
        @Override
        public boolean meets(SkillResultStruct results, double value) {
            return !results.getSelf().getCharacter().is(Stsflag.braced)
                            && !results.getSelf().getCharacter().is(Stsflag.wary)
                            && results.getSelf().getCharacter().canKnockDown(results.getCombat(),
                                            results.getOther().getCharacter(), results.getResult().getAllAttributes(),
                                            (int) Math.round(value), results.getRoll());
        }
    };

    @Override
    public SkillRequirement getUsableRequirements() {
        return SkillRequirement.noRequirement();
    }

    @Override
    public SkillRequirement getRequirements() {
        return requirement;
    }

    @Override
    public String getName() {
        return "Knockdown";
    }
}
