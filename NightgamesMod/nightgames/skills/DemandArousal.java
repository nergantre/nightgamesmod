package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class DemandArousal extends Skill {

    public DemandArousal(Character self) {
        super("Demand Arousal", self, 4);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.ControlledRelease);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && c.getStance().facing(getSelf(), target) && target.human()
                        && ((Player) target).checkAddiction(AddictionType.MIND_CONTROL, getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Inspire arousal in your opponent. Weakens your control.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Player p = (Player) target;
        Addiction addict = p.getAddiction(AddictionType.MIND_CONTROL)
                            .get();
        int dmg = (int) ((20 + Global.randomdouble() * 20) * addict.getMagnitude());
        float alleviation;

        String msg;
        switch (addict.getSeverity()) {
            case HIGH:
                msg = Global.format("", getSelf(), target);
                alleviation = Addiction.MED_INCREASE;
                break;
            case LOW:
                msg = Global.format("\"<i><b>{other:name}. Listen to me.</b></i>\" {self:NAME-POSSESSIVE}"
                                + " looks deeply into {other:possessive} eyes, and {self:possessive}"
                                + " words seem to have more weight behind them than usual. \"<i>"
                                + "{other:name}, can you feel your %s?</i> Strangely, yes,"
                                + " {other:pronoun-action:do|does}. {other:PRONOUN-ACTION:feel|feels}"
                                + " a heat pour into {other:possessive} {other:main-genitals} as"
                                + " {self:subject-action:speak|speaks}.", getSelf(), target,
                                target.hasDick() ? "dick getting hard" : target.hasPussy()
                                                ? "pussy getting wet" : "nipples tingling");
                alleviation = Addiction.LOW_INCREASE;
                break;
            case MED:
                msg = Global.format("", getSelf(), target);
                alleviation = Addiction.MED_INCREASE * .67f;
                break;
            case NONE:
            default:
                alleviation = 0.f;
                msg = Global.format("<b>[[[DemandArousal executed even though the player isn't noticably addicted...]]]</b>",
                                getSelf(), target);
                break;
        }
        c.write(getSelf(), Global.format(msg, getSelf(), target));
        p.temptWithSkill(c, getSelf(), null, dmg, this);
        addict.alleviate(alleviation);

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new DemandArousal(user);
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
