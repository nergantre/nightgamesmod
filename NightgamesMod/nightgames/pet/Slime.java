package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.Growth;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TentaclePart;
import nightgames.combat.Combat;
import nightgames.skills.petskills.SlimeJob;
import nightgames.skills.petskills.SlimeMelt;
import nightgames.skills.petskills.SlimeOil;
import nightgames.skills.petskills.SlimeTrip;

public class Slime extends Pet {
    public Slime(Character owner) {
        super("Slime", owner, Ptype.slime, 3, 3);
    }

    public Slime(Character owner, int power, int ac) {
        super("Slime", owner, Ptype.slime, power, ac);
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        switch (opponent.type()) {
            case fairyfem:
                c.write(getSelf(), opponent.own() + "faerie flies over " + own()
                                + "slime and begins casting a spells. Without warning, several appendages shoot out from the blob and snag "
                                + "the faerie girl's limbs before she can escape. More appendages attach to her breasts and groin as the slime starts to vibrate. The faerie lets out a "
                                + "high pitched moan and squirms against her bonds until she shudders in orgasm and vanishes.");
                break;
            case fairymale:
                c.write(getSelf(), opponent.own() + "faerie flies too close to " + own()
                                + "slime and is suddenly engulfed up to his waist before he can react. He tries to free himself, but "
                                + "groans as it starts to suck and massage his penis. He tries to push the slime off his groin, but it just sucks in his hands, leaving him completely "
                                + "helpless until he ejaculates.");
                break;
            case impfem:
                c.write(getSelf(), own() + "slime gathers around " + opponent.own()
                                + "imp's ankles. With unexpected speed, it surges up her legs and simultaneously penetrates her pussy and "
                                + "ass. She screams in pleasure and falls to her knees as the amorphous blob fucks both her holes. By the time she climaxes and disappears, she's completely "
                                + "fucked senseless.");
                break;
            case impmale:
                c.write(getSelf(), opponent.own() + "imp grabs for " + own()
                                + "slime, but it leaps past his guard and covers his cock. The slime forms perfectly to the imp's dick and balls, milking "
                                + "as much pre-cum as it can get. The imp tries to pull off the slime, but it acts as lubricant and the imp's attempts to remove it devolve into masturbation. The "
                                + "imp demon ejaculates into the slime and disappears.");
                break;
            case slime:
                c.write(getSelf(), "The two slimes circle around each other, while gradually taking on human shape. One of the oozes looks vaguely like "
                                + own() + "small slimy twin, while the other takes " + opponent.own()
                                + "form. The two grapple and melt into each other so it's impossible to tell where one ends and the other begins. You can make out vaguely sexual shapes being formed "
                                + "in the mix. Somehow you can tell that they're each trying to pleasure the other. Eventually the battle ends and a single humanoid shape forms from the amorphous mass, "
                                + "revealing that " + own() + "slime was victorious.");
                break;
            default:
                break;
        }
        c.removePet(getSelf());
    }

    @Override
    public void caught(Combat c, Character captor) {
        if (owner().human()) {
            c.write(captor, captor.name()
                            + " seizes your slime and holds it near her groin. The ooze reacts to the closeness of her vagina and immediately forms a phallic appendage. She grabs the slimy "
                            + "cock before it can penetrate her and strokes it quickly. With each stroke, the shape becomes more defined, until the slime has a perfectly human penis and a set of testicles. "
                            + captor.name()
                            + " speeds up her stokes and grabs the artifical balls with her free hand. The slime ejaculates its own fluid and melts into a puddle.");
        } else if (captor.human()) {
            c.write(captor, "You manage to catch " + own()
                            + "slime, but you're not sure what to do with it. It occurs to you that this thing is actively seeking sexual pleasure, so you push two fingers "
                            + "into the mass and pump them back and forth. Soon a convincing replica of a vagina forms around your fingers and the entire slime gradually takes the shape of a woman. As "
                            + "soon at the clit forms you focus attention on it. The slime climaxes just as its girlish shape finishes forming. Its realistic face shapes in a silent moan and looks very "
                            + "content until it melts into a puddle of goo.");
        }
        c.removePet(getSelf());
    }

    @Override
    protected void buildSelf() {
        PetCharacter self = new PetCharacter(this, own() + getName(), getName(), new Growth(), power);
        // slimes are around 80 cm ish? comes up to about crotch level
        self.body.setHeight(80);
        self.body.add(new GenericBodyPart("skin", 0, 1, 1, "skin", ""));
        self.body.add(new GenericBodyPart("hands", 0, 1, 1, "hands", ""));
        self.body.add(new ModdedCockPart(BasicCockPart.average, CockMod.slimy));
        self.body.add(PussyPart.gooey);
        self.body.add(new TentaclePart("tentacles", "body", "slime", 0, 1, 1));
        // don't finish the body as a slime, it wont have normal body parts.
        self.learn(new SlimeJob(self));
        self.learn(new SlimeOil(self));
        self.learn(new SlimeMelt(self));
        self.learn(new SlimeTrip(self));

        setSelf(self);
    }
}
