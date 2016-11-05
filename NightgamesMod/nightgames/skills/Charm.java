package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Charmed;
import nightgames.status.Stsflag;

public class Charm extends Skill {
    public Charm(Character self) {
        super("Charm", self, 4);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && c.getStance().facing() && !target.wary();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.human() && target.is(Stsflag.blinded)) {
            printBlinded(c);
            return false;
        }
        writeOutput(c, Result.normal, target);
        double mag = 2 + Global.random(4) + 5 * getSelf().body.getCharismaBonus(target);
        if (target.has(Trait.imagination)) {
            mag += 4;
        }
        int m = (int) Math.round(mag);
        target.tempt(c, getSelf(), m);
        double chance = (5 + Math.sqrt(Math.max(0, mag))) / 10.0;
        double roll = Global.randomdouble();
        if (chance > roll) {
            c.write(target.subjectAction("were", "was") + " charmed.");
            target.add(c, new Charmed(target));
            target.emote(Emotion.horny, 10);
            getSelf().emote(Emotion.confident, 20);
        }
        target.emote(Emotion.horny, 10);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 8 && user.get(Attribute.Seduction) > 16;
    }

    @Override
    public Skill copy(Character user) {
        return new Charm(user);
    }

    @Override
    public int speed() {
        return 9;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You flash a dazzling smile at " + target.getName() + ".";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().getName() + " flashes a dazzling smile at "+target.subject()+".";
    }

    @Override
    public String describe(Combat c) {
        return "Charms your opponent into not hurting you.";
    }
}
