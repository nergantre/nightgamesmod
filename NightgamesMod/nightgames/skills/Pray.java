package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class Pray extends Skill {

    public Pray(Character self) {
        super("Pray", self, 2);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human();
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().getAddiction(AddictionType.ZEAL).map(addiction -> addiction.wasCausedBy(target))
                        .orElse(false) && getSelf().canRespond();
    }

    @Override
    public String describe(Combat c) {
        return "Pray to your goddess for guidance";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(),
                        Global.format("{self:SUBJECT-ACTION:bow} {self:possessive} head and close {self:possessive} eyes,"
                                        + " whispering a quick prayer to Angel for guidance. {other:SUBJECT-ACTION:look} at {self:direct-object} strangely, but "
                                        + " the knowledge that Angel is there for {self:direct-object} reinvigorates {self:possessive} spirit"
                                        + " and strengthens {self:possessive} faith.", getSelf(), target));
        int amt = Math.round((getSelf().getAddiction(AddictionType.ZEAL)
                        .orElseThrow(() -> new SkillUnusableException(this)).getMagnitude() * 8));
        getSelf().restoreWillpower(c, amt);
        getSelf().addict(c, AddictionType.ZEAL, Global.getCharacterByType("Angel"), Addiction.LOW_INCREASE);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Pray(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        throw new UnsupportedOperationException();
    }

}
