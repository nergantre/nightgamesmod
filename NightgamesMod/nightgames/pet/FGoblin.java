package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Growth;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.petskills.GoblinBondage;
import nightgames.skills.petskills.GoblinDenial;
import nightgames.skills.petskills.GoblinFaceFuck;
import nightgames.skills.petskills.GoblinMasochism;
import nightgames.skills.petskills.GoblinTease;

public class FGoblin extends Pet {
    public FGoblin(Character owner, int pow, int ac) {
        super("Fetish Goblin", owner, Ptype.fgoblin, pow, ac);
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        switch(opponent.type()){
            case fairyfem:
                c.write(owner(),String.format("%s faerie girl flies low, aiming for %s fetish goblin's weak spot, but the goblin knocks the little fae out of the air "
                        + "with a swing of her hefty girl-cock. The fetish goblin grabs the dazed faerie and finishes her off by pressing a vibrator against her tiny "
                        + "slit.",opponent.own(),own()));
                break;
            case fairymale:
                c.write(owner(),String.format("%s fetish goblin manages to catch %s faerie as he carelessly flies too close. She shoves the tiny male between her heavy "
                        + "boobs, completely engulfing him within her cleavage. You don't know if or when he desummoned, but he's clearly not coming out of there.", own(),opponent.own()));
                break;
            case impfem:
                c.write(owner(),String.format("%s fetish goblin overpowers %s imp girl and quickly binds her wrists. The goblin bends the helpless imp over her knee "
                        + "and slides a suspiciously wet vibrator into her exposes pussy. The imp moans and squirms, but is unable to get away. The goblin punishes the "
                        + "escape attempt with ten hard slaps on the imp's upturned ass. By the final slap, the imp is practically screaming in orgasm and quickly vanishes "
                        + "in a puff of brimstone.",own(),opponent.own()));
                break;
            case impmale:
                c.write(owner(),String.format("%s fetish goblin and %s imp grapple with each other, vying for dominance. The larger goblin gains the upper hand and "
                        + "staggers the imp with a quick knee to the groin. The imp doubles over in pain, giving the hermaphroditic goblin time to get behind him. She "
                        + "lines up her cock with the imp's unprotected ass and penetrates him with a firm thrust. %s imp squeals in alarm as being suddenly fucked "
                        + "from behind. The goblin pegs the imp steadily and reaches around to stroke his cock. Soon, the imp sprays cum into the air and disappears.",
                        own(),opponent.own(),opponent.own()));
                break;
            case slime:
                c.write(owner(),String.format("%s fetish goblin removes her boots and steps barefoot into %s slime. The amorphous creature squirms happily around the "
                        + "goblin's feet, but makes no attempt to attack. It seems to be completely fascinated with her feet. The goblin wiggles her toes and the slime "
                        + "trembles with delight, before melting into a content puddle.", own(),opponent.own()));
                break;
            case fgoblin:
                c.write(owner(),String.format("%s fetish goblin tackles %s goblin, pinning her arms behind her back. %s goblin ties up the other, using some spare ropes "
                        + "and bandage straps. The poor, bound herm is left completely immobile and vulnerable, but seems to be getting very aroused by her situation. The "
                        + "dominant fetish goblin takes her time getting her helpless opponent off, but eventually %s goblin is whimpering and twitching in orgams",
                        own(),opponent.own(),own(),opponent.own()));
                break;
            default:
                // TODO Write an actual scene for this.
                c.write(owner(), Global.format("{self:SUBJECT-ACTION:fuck|fucks} {other:name-do} senseless, releasing the summon.", getSelf(), opponent.getSelf()));
            }
    }

    @Override
    public void caught(Combat c, Character captor) {
        if(owner().human()){
            c.write(captor,String.format("%s gets a short running start and delivers a powerful punt to your fetish goblin's dangling balls. The impact lifts the short "
                    + "hermaphrodite completely off the floor as you cringe in sympathetic pain. Apparently it's too much even for the masochistic creature, because she "
                    + "collapses and disappears.",captor.name()));
        }
        else{
            c.write(captor, String.format("You manage to catch %s's ",owner().name()));
            c.write(captor, Global.format("{other:SUBJECT} manage to catch {self:name-do} fetish goblin by her bondage gear, keeping her from escaping. It's not immediately clear how {other:pronoun} can "
                    + "finish off the overstimulated goblin. There's not much {other:subject} can do to the goblin's genitals beyond what she's already doing with her 'accessories.' "
                    + "{other:SUBJECT-ACTION:need|needs} a strong enough stimulus to push her over the theshold. {other:PRONOUN} grab the end of the anal beads sticking out of the fetish goblin's ass and "
                    + "yank them out all at once. The goblin shudders and the flow of liquid leaking out of her holes signals her orgasm before she vanishes.", getSelf(), captor));
        }
        c.removePet(getSelf());
    }
    
    @Override
    protected void buildSelf() {
        PetCharacter self = new PetCharacter(this, owner().nameOrPossessivePronoun() +" " + getName(), getName(), new Growth(), power);
        // goblins are about 120 centimeters tall (around 4' for US people)
        self.body.setHeight(120);
        self.body.finishBody(CharacterSex.herm);
        self.learn(new GoblinTease(self));
        self.learn(new GoblinBondage(self));
        self.learn(new GoblinMasochism(self));
        self.learn(new GoblinFaceFuck(self));
        self.learn(new GoblinDenial(self));
        setSelf(self);
    }
}
