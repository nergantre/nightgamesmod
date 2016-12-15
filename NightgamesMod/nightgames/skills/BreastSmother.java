package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.stance.BreastSmothering;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Charmed;

public class BreastSmother extends Skill {

    public BreastSmother(Character self) {
        super("Breast Smother", self);
        addTag(SkillTag.dominant);
        addTag(SkillTag.usesBreasts);
        addTag(SkillTag.weaken);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getLevel() >= 15 ||user.get(Attribute.Seduction) >= 30 && user.hasBreasts();
    }

    @Override
    public float priorityMod(Combat c) {
        return 6;
    }

    static int MIN_REQUIRED_BREAST_SIZE = 5;
    
    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().breastsAvailable() && c.getStance().reachTop(getSelf()) && c.getStance().front(getSelf())
                        && getSelf().body.getLargestBreasts().size >= MIN_REQUIRED_BREAST_SIZE
                        && c.getStance().mobile(getSelf())
                        && (!c.getStance().mobile(target) || c.getStance().prone(target)) && getSelf().canAct();
    }

    @Override
    public String describe(Combat c) {
        return "Shove your opponent's face between your tits to crush their resistance.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean special = c.getStance().en != Stance.breastsmothering && !c.getStance().havingSex(c);        
        writeOutput(c, special ? Result.special : Result.normal, target);

       
        double n = 10 + Global.random(5) + getSelf().body.getLargestBreasts().size;
        if (c.getStance().front(getSelf())) {
            // opponent can see self
            n += 3 * getSelf().body.getHotness(target);
        }
        
        if (target.has(Trait.temptingtits)) {
            n += Global.random(5, 10);
        }
        if (target.has(Trait.beguilingbreasts)) {
            n *= 1.5;
            target.add(c, new Charmed(target));
        }
        if (target.has(Trait.imagination)) {
            n *= 1.5;
        }
        
        target.tempt(c, getSelf(), getSelf().body.getRandom("breasts"), (int) Math.round(n / 2));
        target.weaken(c, (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(5, 15)));

        target.loseWillpower(c, Math.max(10, target.getWillpower().max() * 10 / 100 ));     
        
        if (special) {
            c.setStance(new BreastSmothering(getSelf(), target), getSelf(), true);      
            getSelf().emote(Emotion.dominant, 20);
        } else {
            getSelf().emote(Emotion.dominant, 10);
        }
        if (Global.random(100) < 15 + 2 * getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "breasts", .25));
        }
        return true;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 25;
    }

    @Override
    public Skill copy(Character user) {
        return new BreastSmother(user);
    }

    @Override
    public Tactics type(Combat c) {
        if (c.getStance().enumerate() != Stance.breastsmothering) {
            return Tactics.positioning;
        } else {
            return Tactics.pleasure;
        }
    }

    @Override
    public String getLabel(Combat c) {
        return "Breast Smother";
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        StringBuilder b = new StringBuilder();
        
        if (modifier == Result.special) {
            b.append( "You quickly wrap up " + target.name() + "'s head in your arms and press your "
                            + getSelf().body.getRandomBreasts().fullDescribe(getSelf()) + " into their face. ");
        }
        else {
            b.append( "You rock " + target.name() + "'s head between your "
                            + getSelf().body.getRandomBreasts().fullDescribe(getSelf()) + " trying to force them to gasp.");                           
        }
        
        if (getSelf().has(Trait.temptingtits)) {
            b.append("They can't help but groan in pleasure from having their face stuck between your perfect tits");           
                          
            if (getSelf().has(Trait.beguilingbreasts)) {
                b.append(", and you can't help but smile as they snuggle deeper into your cleavage");
            } 
            b.append(".");
            
        } else{
            b.append(" " + target.name() + " muffles something in confusion into your breasts before they begin to panic as they realize they cannot breathe!");            
        }   
        return b.toString();
}

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        StringBuilder b = new StringBuilder();
        if (modifier == Result.special) {
            b.append( getSelf().subject()+ " quickly wraps up your head between their "
                            + getSelf().body.getRandomBreasts().fullDescribe(getSelf()) + ", filling your vision instantly with them. ");
        } else {
            b.append( getSelf().subject()+ " rocks your head between their "
                            + getSelf().body.getRandomBreasts().fullDescribe(getSelf()) + " trying to force you to gasp for air. ");
        }
        
        if (getSelf().has(Trait.temptingtits)) {
            b.append("You can't help but groan in pleasure from having your face stuck between ");
            b.append(getSelf().possessivePronoun());
            b.append(" perfect tits as they take your breath away");           
                          
            if (getSelf().has(Trait.beguilingbreasts)) {
                b.append(", and due to their beguiling nature you can't help but want to stay there as long as possible");
            }
            b.append(".");
            
        } else{
            b.append(" You let out a few panicked sounds muffled by the breasts now covering your face as you realize you cannot breathe!");
        }        
        
        return b.toString();
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
