package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Alluring;

public class StripTease extends Skill {
    public StripTease(Character self) {
        super("Strip Tease", self);
    }

    public static boolean hasRequirements(Character user) {
        return user.get(Attribute.Seduction) >= 24 && !user.has(Trait.direct) && !user.has(Trait.shy)
                        && !user.has(Trait.temptress);
    }

    public static boolean isUsable(Combat c, Character self, Character target) {
        return self.stripDifficulty(target) == 0 && !self.has(Trait.strapped) && self.canAct()
                        && c.getStance().mobile(self) && !self.mostlyNude() && !c.getStance().prone(self)
                        && c.getStance().front(self) && (!self.breastsAvailable() || !self.crotchAvailable());
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return hasRequirements(user);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return isUsable(c, getSelf(), target);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 50;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        int m = 15 + Global.random(5);
        target.tempt(c, getSelf(), m);
        getSelf().undress(c);
        getSelf().add(c, new Alluring(getSelf(), 5));
        target.emote(Emotion.horny, 30);
        getSelf().emote(Emotion.confident, 15);
        getSelf().emote(Emotion.dominant, 15);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new StripTease(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "During a brief respite in the fight as " + target.name()
                        + " is catching her breath, you make a show of seductively removing your clothes. "
                        + "By the time you finish, she's staring with undisguised arousal, pressing a hand unconsciously against her groin.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().name()
                        + " asks for a quick time out and starts sexily slipping her her clothes off. Although there are no time outs in the rules, you can't help staring "
                        + "at the seductive display until she finishes with a cute wiggle of her naked ass.";
    }

    @Override
    public String describe(Combat c) {
        return "Tempt opponent by removing your clothes";
    }

}
