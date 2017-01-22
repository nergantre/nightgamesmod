package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.HeldPaizuri;
import nightgames.stance.Stance;

public class PinningPaizuri extends Skill {
    public PinningPaizuri(Character self) {
        super("Titfuck Pin", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.oral);
        addTag(SkillTag.foreplay);
        addTag(SkillTag.usesBreasts);
    }

    
    static int MIN_REQUIRED_BREAST_SIZE = 3;
    
    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 28;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf())
                && c.getStance().dom(getSelf())
                && c.getStance().facing(getSelf(), target)
                && (c.getStance().prone(target)  ||  c.getStance().en == Stance.paizuripin)
                && target.crotchAvailable() && getSelf().canAct()
                && !c.getStance().connected(c)
                && c.getStance().en != Stance.paizuripin
                && getSelf().hasBreasts() && getSelf().body.getLargestBreasts().getSize() >= MIN_REQUIRED_BREAST_SIZE
                && target.hasDick() && getSelf().breastsAvailable() && target.crotchAvailable();
    }

    @Override
    public float priorityMod(Combat c) {
        return 0;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        
        c.setStance(new HeldPaizuri(getSelf(), target), getSelf(), true);
     
        new Paizuri(getSelf()).resolve(c, target);
        
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new PinningPaizuri(user);
    }

    @Override
    public int speed() {
        return 5;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return receive(c, damage, modifier, target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        
        
        if( c.getStance().en == Stance.oralpin)
        {
            return Global.format(
                            "{self:SUBJECT-ACTION:free|frees} {other:possessive} cock from her mouth, and quickly {self:action:wrap|wraps} {self:possessive} breasts around {other:possessive} cock.",
                            getSelf(), target);
        }else
        {
            return Global.format(
                            "{self:SUBJECT-ACTION:bow|bows} {other:name-do} over, and {self:action:wrap|wraps} {self:possessive} breasts around {other:possessive} cock.",
                            getSelf(), target);
        }             
        
    }

    @Override
    public String describe(Combat c) {
        return "Hold your opponent down and use your tits";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
