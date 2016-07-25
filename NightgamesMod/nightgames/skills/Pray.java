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
        return Global.getPlayer().getAddiction(AddictionType.ZEAL).map(addiction -> addiction.wasCausedBy(target))
                        .orElse(false);
    }

    @Override
    public String describe(Combat c) {
        return "Pray to your goddess for guidance";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(),
                        String.format("You bow your head and close your eyes,"
                                        + " whispering a quick prayer to Angel for guidance. %s looks at you strangely, but "
                                        + " the knowledge that Angel is there for you reinvigorates your spirit"
                                        + " and strengthens your faith.",
                        target.name()));
        int amt = Math.round((Global.getPlayer().getAddiction(AddictionType.ZEAL)
                        .orElseThrow(() -> new SkillUnusableException(this)).getMagnitude() * 5));
        getSelf().restoreWillpower(c, amt);
        Global.getPlayer().addict(AddictionType.ZEAL, Global.getCharacterByType("Angel"), Addiction.LOW_INCREASE);
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
