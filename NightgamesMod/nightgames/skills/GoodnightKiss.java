package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class GoodnightKiss extends Skill {

    public GoodnightKiss(Character self) {
        super("Goodnight Kiss", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 18;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .kiss(getSelf(), target) && getSelf().canAct();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public String describe(Combat c) {
        return "Deliver a powerful knockout drug via a kiss: 30 Mojo";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(), String.format(
                        "%s surreptitiously %s %s lips with a powerful sedative, careful not "
                                        + "to accidently ingest any. As soon as %s %s an opening, "
                                        + "%s %s in and kiss %s softly. Only a small amount of the drug is actually "
                                        + "transfered by the kiss, but it's enough. %s immediately staggers "
                                        + "as the strength leaves %s body.",
                        getSelf().subject(), getSelf().action("coat"), getSelf().possessiveAdjective(),
                        getSelf().pronoun(), getSelf().action("see"), getSelf().pronoun(),
                        getSelf().action("dart"), target.subject(), target.subject(), target.possessiveAdjective()));
        target.tempt(Global.random(4));
        target.getStamina()
              .empty();
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new GoodnightKiss(user);
    }
    
    @Override
    public int speed() {
        return 7;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
