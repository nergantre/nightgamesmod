package nightgames.pet;

import java.util.ArrayList;

import nightgames.characters.Character;
import nightgames.characters.body.PetPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.BD;
import nightgames.status.Masochistic;
import nightgames.status.Shamed;

public class FGoblin extends Pet {

    private final PetPart part = new PetPart("fetish goblin");
    
    public FGoblin(Character owner, int pow, int ac) {
        super("Fetish Goblin", owner, Ptype.fgoblin, pow, ac);
    }

    @Override
    public String describe() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void act(Combat c, Character target) {
        if(target.human()){
            switch(pickSkill(c,target)){
            case VIBRATOR:
                c.write(owner(),String.format("%s's goblin pulls the vibrator out of her wet hole and thrusts it between your legs.",owner().name()));
                target.body.pleasure(owner(), part, target.body.getRandomPussy(), 2+3*Global.random(power), c);
                break;
            case MASOCHISM:
                c.write(owner(),String.format("%s's fetish goblin draws a riding crop and hits her own balls with it. She shivers with delight at the pain and you can "
                        + "feel an aura of masochism radiate off her.",owner().name()));
                owner().add(new Masochistic(owner()));
                target.add(new Masochistic(target));
                break;
            case BONDAGE:
                c.write(owner(),String.format("%s's fetish goblin pulls the bondage straps tighter around herself. You can see the leather and latex digging into her skin as "
                        + "her bondage fascinatation begins to affect both you and %s.",owner().name(),owner().name()));
                owner().add(new BD(owner()));
                target.add(new BD(target));
                break;
            case DENIAL:
                c.write(owner(),String.format("%s's fetish goblin suddenly appears to turn against %s and slaps %s sensitive testicles. You're momentarily confused, but you "
                        + "realize the shock probably undid your efforts to make %s cum.",owner().name(),owner().directObject(),owner().possessivePronoun(),owner().directObject()));
                owner().pain(c, 3*Global.random(power));
                owner().calm(c,(3+Global.random(5))*power);
                break;
            case FACEFUCK:
                c.write(owner(),String.format("%s's fetish goblin straddles your head, giving you an eyefull of her assorted genitals. She pulls the vibrator out of her "
                        + "pussy, causing a rain of love juice to splash your face. She then wipes her leaking cock on your forehead, smearing you with precum. You feel "
                        + "your face flush with shame as she marks you with her fluids.",owner().name()));
                target.add(new Shamed(target));
                break;
            case ANALDILDO:
                c.write(owner(),String.format("You jump in surprise as you suddenly feel something solid penetrating your asshole. %s's fetish goblin got behind you during "
                        + "the fight and delivered a sneak attack with an anal dildo. Before you can retaliate she withdraws the toy and retreats to safety.",owner().name()));
                target.body.pleasure(owner(), part, target.body.getRandomAss(), 2+3*Global.random(power), c);
                break;
            case ANALBEADS:
                c.write(owner(),String.format("%s's fetish goblin takes advantage of your helplessness and positions herself behind you. She produces a string on anal beads "
                        + "and proceeds to insert them one bead at a time into your anus. She manages to get five beads in while you're unable to defend yourself. When she "
                        + "pulls them out, it feels like they're turning you inside out.",owner().name()));
                target.body.pleasure(owner(), part, target.body.getRandomAss(), 5*Global.random(power), c);
                break;
            default:
                c.write(owner(),String.format("%s's fetish goblin stays at the edge of battle and touches herself absentmindedly.",owner().name()));
                break;
            }
        }
        else{
            switch(pickSkill(c,target)){
            case VIBRATOR:
                c.write(owner(),String.format("Your fetish goblin removes the humming vibrator from her own wet pussy and shoves it into %s's.",target.name()));
                target.body.pleasure(owner(), part, target.body.getRandomPussy(), 2+3*Global.random(power), c);
                break;
            case MASOCHISM:
                c.write(owner(),String.format("Your fetish goblin draws a riding crop and hits her own balls with it. She shivers with delight at the pain and you can "
                        + "feel an aura of masochism radiate off her."));
                owner().add(new Masochistic(owner()));
                target.add(new Masochistic(target));
                break;
            case BONDAGE:
                c.write(owner(),String.format("Your fetish goblin pulls the bondage straps tighter around herself. You can see the leather and latex digging into her skin as "
                        + "her bondage fascinatation begins to affect both you and %s.",target.name()));
                owner().add(new BD(owner()));
                target.add(new BD(target));
                break;
            case DENIAL:
                c.write(owner(),String.format("As you feel your arousal build, your fetish goblin suddenly turns and slaps you sharply on the balls. You wince in pain and "
                        + "let out a yelp of protest. You can't see the goblin's expression through her mask, but her eyes seem to be scolding you for your lack of self control."));
                owner().pain(c, 3*Global.random(power));
                owner().calm(c,(3+Global.random(5))*power);
                break;
            case FACEFUCK:
                c.write(owner(),String.format("Your fetish goblin leaps onto %s's face as %s's lying on the floor. The goblin rubs her cock and balls on %s face, humiliating %s",
                        target.name(),target.pronoun(),target.possessivePronoun(),target.directObject()));
                target.add(new Shamed(target));
                break;
            case ANALDILDO:
                c.write(owner(),String.format("Your fetish goblin manages to sneak up on %s and stick a dildo in her ass. %s lets out a shriek of surprise at the sudden "
                        + "sensation and your goblin gets away before %s can catch her",target.name(),target.pronoun(),target.name()));
                target.body.pleasure(owner(), part, target.body.getRandomAss(), 2+3*Global.random(power), c);
                break;
            case ANALBEADS:
                c.write(owner(),String.format("Your fetish goblin takes advantage of %s's defenselessness to push a string of anal beads into her butt. She lets out a "
                        + "whimper of protest as each bead goes in and a moan of pleasure as they're all pulled out."));
                target.body.pleasure(owner(), part, target.body.getRandomAss(), 5*Global.random(power), c);
                break;
            default:
                c.write(owner(),String.format("Your fetish goblin stays at the edge of battle and touches herself absentmindedly."));
            }
        }
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
            c.write(captor,String.format("You manage to catch %s's fetish goblin by her bondage gear, keeping her from escaping. It's not immediately clear how you can "
                    + "finish off the overstimulated goblin. There's not much you can do to her genitals beyond what she's already doing with her 'accessories.' You need "
                    + "a strong enough stimulus to push her over the theshold. You grab the end of the anal beads sticking out of her ass and yank them out all at once. "
                    + "The goblin shudders and the flow of liquid leaking out of her holes signals her orgasm before she vanishes.",owner().name()));
        }
        remove();
    }

    @Override
    public boolean hasDick() {
        return true;
    }

    @Override
    public boolean hasPussy() {
        return true;
    }
    
    public PetSkill pickSkill(Combat c, Character target){
        ArrayList<PetSkill> available = new ArrayList<PetSkill>();
        available.add(PetSkill.IDLE);
        available.add(PetSkill.MASOCHISM);
        available.add(PetSkill.BONDAGE);
        if(owner().hasBalls()&&owner().getArousal().percent()>=80){
            available.add(PetSkill.DENIAL);
        }
        if(target.hasPussy()&&target.pantsless()){
            available.add(PetSkill.VIBRATOR);
        }
        if(target.pantsless()&&target.canAct()){
            available.add(PetSkill.ANALDILDO);
        }
        if(target.pantsless()&&!target.canAct()){
            available.add(PetSkill.ANALBEADS);
        }
        if(c.getStance().prone(target)){
            available.add(PetSkill.FACEFUCK);
        }
        return available.get(Global.random(available.size()));
    }
    
    private enum PetSkill{
        VIBRATOR,
        MASOCHISM,
        BONDAGE,
        DENIAL,
        FACEFUCK,
        ANALDILDO,
        ANALBEADS,
        IDLE,
    }
}
