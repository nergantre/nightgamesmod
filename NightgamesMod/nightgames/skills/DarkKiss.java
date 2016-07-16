package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class DarkKiss extends Skill {

    public DarkKiss(Character self) {
        super("Dark Kiss", self, 3);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human();
    }
    
    @Override
    public int getMojoBuilt(Combat c) {
        return 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return Global.getPlayer()
                     .checkAddiction(AddictionType.CORRUPTION)
                        && c.getStance()
                            .kiss(getSelf())
                        && getSelf().canAct() && !target.has(Trait.corrupting);
    }

    @Override
    public String describe(Combat c) {
        return "Feed some of your opponent's will to the demonic corruption taking hold of you.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        assert getSelf().human();
        c.write(getSelf(), String.format("You lean in and plant an intense kiss on %s lips. The corruption which Reyka"
                        + " has imbued you with stirs, and greedily draws %s willpower in through your connection, growing"
                        + " more powerful.", target.nameOrPossessivePronoun(), target.possessivePronoun()));

        Addiction add = Global.getPlayer().getAddiction(AddictionType.CORRUPTION)
                        .orElseThrow(() -> new SkillUnusableException(this));
        float mag = add.getMagnitude();
        int min = (int) (mag * 3);
        int mod = (int) (mag * 8);
        int amt = min + Global.random(mod);
        target.loseWillpower(c, amt, true);
        add.alleviateCombat(Addiction.HIGH_INCREASE);
        Global.getPlayer().addict(AddictionType.CORRUPTION, null, Addiction.LOW_INCREASE);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new DarkKiss(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
