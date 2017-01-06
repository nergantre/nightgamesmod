package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Growth;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.ModdedCockPart;
import nightgames.combat.Combat;
import nightgames.skills.Grind;
import nightgames.skills.Piston;
import nightgames.skills.PussyGrind;
import nightgames.skills.Thrust;
import nightgames.skills.petskills.ImpAssault;
import nightgames.skills.petskills.ImpFacesit;
import nightgames.skills.petskills.ImpSemenSquirt;
import nightgames.skills.petskills.ImpStrip;
import nightgames.skills.petskills.ImpTease;

public class ImpMale extends Pet {

    public ImpMale(Character owner) {
        super("imp", owner, Ptype.impmale, 3, 2);
    }

    public ImpMale(Character owner, int power, int ac) {
        super("imp", owner, Ptype.impmale, power, ac);
    }

    @Override
    public String describe() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        switch (opponent.type()) {
            case fairyfem:
                c.write(getSelf(), own() + "imp manages to catch " + opponent.own()
                                + "faerie as the tiny fae flies around his head. He shoves his cock into the faerie's face, smearing his pre-cum over her. "
                                + "He presses her against his shaft, stroking himself with her entire body. The faerie, intoxicated by the potent fluid is unable to last long and cums with a high "
                                + "pitched moan.");
                break;
            case fairymale:
                c.write(getSelf(), own() + "imp swats " + opponent.own() + "faerie out of the air.");
                break;
            case impfem:
                c.write(getSelf(), own() + "imp grabs " + opponent.own()
                                + "female imp and bends her over. He rams his cock into her wet box without any foreplay. The female groans in protest and flails about "
                                + "in an attempt to take control, but she's held fast. The male shows impressive stamina, fucking the female until she orgasms and vanishes right off his dick. He then seeks a "
                                + "new target for his unsatisfied cock.");
                break;
            case impmale:
                c.write(getSelf(), "");
                break;
            case slime:
                c.write(getSelf(), own() + "imp pins " + opponent.own()
                                + "slime under foot and lets his leaking cock drip onto the amorphous mass. As the slime absorbs the first drops of pre-cum, it starts to "
                                + "frantically attempt to reach the imp's penis. It seems unable to change shape though and the demon keeps it pinned while letting more pre-cum drip down. After absorbing enough "
                                + "fluid, the slime's color darkens and it gradually solidifies, unable to move.");
                break;
            default:
                (new ImpTease(getSelf())).resolve(c, opponent.getSelf());
                return;
        }
        c.removePet(opponent.getSelf());
    }

    @Override
    public void caught(Combat c, Character captor) {
        if (owner().human()) {
            c.write(captor, captor.getName()
                            + " shoves your imp to the floor and pins its cock under her foot. She grinds her foot, lubricated with the pre-cum that's streaming from "
                            + "your minion's erection. The imp jabbers incoherently as it shoots its load and disappears, leaving only a puddle of cum.");
        } else if (captor.human()) {
            c.write(getSelf(), "");
        }
        c.removePet(getSelf());
    }

    @Override
    protected void buildSelf() {
        PetCharacter self = new PetCharacter(this, owner().nameOrPossessivePronoun() + " " + getName(), getName(), new Growth(), getPower());
        // imps are about as tall as goblins, maybe a bit shorter
        self.body.setHeight(115);
        if (getPower() > 30) {
            self.body.add(new ModdedCockPart(BasicCockPart.big, CockMod.incubus));
        } else {
            self.body.add(BasicCockPart.big);
        }
        self.body.finishBody(CharacterSex.male);
        self.learn(new ImpAssault(self));
        self.learn(new Thrust(self));
        self.learn(new Grind(self));
        self.learn(new Piston(self));
        self.learn(new PussyGrind(self));
        self.learn(new ImpTease(self));
        self.learn(new ImpStrip(self));
        self.learn(new ImpFacesit(self));
        self.learn(new ImpSemenSquirt(self));
        setSelf(self);
    }
}
