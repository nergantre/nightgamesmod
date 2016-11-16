package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.pet.ImpFem;
import nightgames.pet.ImpMale;
import nightgames.pet.Ptype;

public class SpawnImp extends Skill {
    private Ptype gender;

    public SpawnImp(Character self, Ptype gender) {
        super("Summon Imp", self);
        this.gender = gender;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 6;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && c.getPetsFor(getSelf()).size() < getSelf().getPetLimit();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Summon a demonic Imp: 10 mojo, 5 arousal";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().arouse(5, c);
        int power = 10 + getSelf().get(Attribute.Dark) / 2;
        int ac = 2 + getSelf().get(Attribute.Dark) / 10;
        if (getSelf().has(Trait.leadership)) {
            power += 5;
        }
        if (getSelf().has(Trait.tactician)) {
            ac += 3;
        }
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
            if (gender == Ptype.impfem) {
                c.addPet(new ImpFem(getSelf(), power, ac).getSelf());
            } else {
                c.addPet(new ImpMale(getSelf(), power, ac).getSelf());
            }
        } else {
            if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
            c.addPet(new ImpFem(getSelf(), power, ac).getSelf());
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SpawnImp(user, gender);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String getLabel(Combat c) {
        if (gender == Ptype.impfem) {
            return "Imp (female)";
        } else {
            return "Imp (male)";
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (gender == Ptype.impfem) {
            return "You focus your dark energy and summon a minion to fight for you. A naked, waist high, female imp steps out of a small burst of flame. She stirs up her honey "
                            + "pot and despite yourself, you're slightly affected by the pheromones she's releasing.";
        } else {
            return "You focus your dark energy and summon a minion to fight for you. A brief burst of flame reveals a naked imp. He looks at "
                            + target.name() + " with hungry eyes "
                            + "and a constant stream of pre-cum leaks from his large, obscene cock.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s spreads out %s dark aura and a demonic imp appears next to %s"
                        + " in a burst of flame. The imp stands about waist height, with bright red hair, "
                        + "silver skin and a long flexible tail. It's naked, clearly female, and "
                        + "surprisingly attractive given its inhuman features.",
                        getSelf().subject(), getSelf().possessivePronoun(), getSelf().directObject());
    }

}
