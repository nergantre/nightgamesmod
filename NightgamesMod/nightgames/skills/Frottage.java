package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.BodyFetish;

public class Frottage extends Skill {

    public Frottage(Character self) {
        super("Frottage", self);
        addTag(SkillTag.pleasureSelf);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 26;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().sub(getSelf())
                        && !c.getStance().havingSex(c) && target.crotchAvailable()
                        && (getSelf().hasDick() && getSelf().crotchAvailable() || getSelf().has(Trait.strapped));
    }

    @Override
    public String describe(Combat c) {
        return "Rub yourself against your opponent";
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 10;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 6 + Global.random(8);
        BodyPart receiver = target.hasDick() ? target.body.getRandomCock() : target.body.getRandomPussy();
        BodyPart dealer = getSelf().hasDick() ? getSelf().body.getRandomCock() : getSelf().has(Trait.strapped) ? StraponPart.generic : getSelf().body.getRandomPussy();
        if (getSelf().human()) {
            if (target.hasDick()) {
                c.write(getSelf(), deal(c, m, Result.special, target));
            } else {
                c.write(getSelf(), deal(c, m, Result.normal, target));
            }
        } else if (getSelf().has(Trait.strapped)) {
            if (target.human()) {
                c.write(getSelf(), receive(c, m, Result.special, target));
            }
            target.loseMojo(c, 10);
            dealer = null;
        } else {
            if (target.human()) {
                c.write(getSelf(), receive(c, m, Result.normal, target));
            }
        }

        if (dealer != null) {
            getSelf().body.pleasure(target, receiver, dealer, m / 2, c, this);
        }
        target.body.pleasure(getSelf(), dealer, receiver, m, c, this);
        if (Global.random(100) < 15 + 2 * getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "cock", .25));
        }
        getSelf().emote(Emotion.horny, 15);
        target.emote(Emotion.horny, 15);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Frottage(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You tease " + target.name() + "'s penis with your own, dueling her like a pair of fencers.";
        } else {
            return "You press your hips against " + target.name()
                            + "'s legs, rubbing her nether lips and clit with your dick.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format("%s thrusts %s hips to prod %s delicate jewels with %s strapon dildo. "
                            + "As %s and %s %s hips back, %s presses the toy against %s cock, "
                            + "teasing %s sensitive parts.",
                            getSelf().subject(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(),
                            target.subjectAction("flinch", "flinches"), target.action("pull"), target.possessivePronoun(),
                            getSelf().subject(), target.possessivePronoun(), target.possessivePronoun());
        } else if (getSelf().hasDick()) {
            return String.format("%s pushes %s %s against the sensitive head of %s member, "
                            + "dominating %s manhood.", getSelf().subject(), getSelf().possessivePronoun(),
                            getSelf().body.getRandomCock().describe(getSelf()), target.nameOrPossessivePronoun(),
                            target.possessivePronoun());
        } else {
            return String.format("%s pushes %s cock against her soft thighs, rubbing %s shaft up"
                            + " against %s nether lips.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), target.possessivePronoun(),
                            getSelf().possessivePronoun());
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
