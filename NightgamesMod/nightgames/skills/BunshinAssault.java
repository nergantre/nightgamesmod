package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class BunshinAssault extends Skill {

    public BunshinAssault(Character self) {
        super("Bunshin Assault", self);
        addTag(SkillTag.hurt);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 6;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && !c.getStance()
                             .prone(getSelf())
                        && getSelf().canAct() && !c.getStance()
                                                   .behind(target)
                        && !c.getStance()
                             .penetrated(c, target)
                        && !c.getStance()
                             .penetrated(c, getSelf());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 18;
    }
    
    @Override
    public String describe(Combat c) {
        return "Attack your opponent with shadow clones: 3 Mojo per attack (min 2)";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int clones = Math.min(Math.min(getSelf().getMojo().get()/3, getSelf().get(Attribute.Ninjutsu)/3),6);
        Result r;
        getSelf().buildMojo(c, (6-clones)*3);
        if(getSelf().human()){
            c.write(getSelf(), String.format("You form %d shadow clones and rush forward.",clones));
        }
        else if(c.shouldPrintReceive(target, c)){
            c.write(getSelf(), String.format("%s moves in a blur and suddenly %s %d of %s approaching %s.",getSelf().name(),
                            target.subjectAction("see"),clones,getSelf().pronoun(),target.reflectivePronoun()));
        }
        for(int i=0;i<clones;i++){
            if(target.roll(getSelf(), c, accuracy(c)+getSelf().get(Attribute.Speed) + getSelf().getLevel())) {
                switch(Global.random(4)){
                case 0:
                    r=Result.weak;
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(1, 4)));
                    break;
                case 1:
                    r=Result.normal;
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(2, 5)));
                    break;
                case 2:
                    r=Result.strong;
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(6, 9)));
                    break;
                default:
                    r=Result.critical;
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(10, 14)));
                    break;
                }
                writeOutput(c, r, target);
            }else{

                writeOutput(c, Result.miss, target);
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new BunshinAssault(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    
    @Override
    public int speed() {
        return 4;
    }
    
    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if(modifier==Result.miss){
            return String.format("%s dodges one of your shadow clones.",target.name());
        }else if(modifier==Result.weak){
            return String.format("Your shadow clone gets behind %s and slaps %s hard on the ass.",target.name(),target.directObject());
        }else if(modifier==Result.strong){
            if(target.hasBalls()){
                return String.format("One of your clones gets grabs and squeezes %s's balls.",target.name());
            }else{
                return String.format("One of your clones hits %s on %s sensitive tit.",target.name(),target.possessivePronoun());
            }
        }else if(modifier==Result.critical){
            if(target.hasBalls()){
                return String.format("One lucky clone manages to deliver a clean kick to %s's fragile balls.",target.name());
            }else{
                return String.format("One lucky clone manages to deliver a clean kick to %s's sensitive vulva.",target.name());
            }
        }else{
            return String.format("One of your shadow clones lunges forward and strikes %s in the stomach.",target.name());
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if(modifier==Result.miss){
            return String.format("%s quickly %s a shadow clone's attack.",
                            target.subject(), target.action("dodge"));
        }else if(modifier==Result.weak){
            return String.format("%s sight of one of the clones until %s %s a sharp spank on %s ass cheek.",
                            target.subjectAction("lose"), target.pronoun(), target.action("feel"),
                            target.possessivePronoun());
        }else if(modifier==Result.strong){
            if(target.hasBalls()){
                return String.format("A %s clone gets a hold of %s balls and squeezes them painfully.",getSelf().name(),
                                target.nameOrPossessivePronoun());
            }else{
                return String.format("A %s clone unleashes a quick roundhouse kick that hits %s sensitive boobs.",getSelf().name(),
                                target.nameOrPossessivePronoun());
            }
        }else if(modifier==Result.critical){
            if(target.hasBalls()){
                return String.format("One lucky %s clone manages to land a snap-kick squarely on %s unguarded jewels.",getSelf().name(),
                                target.nameOrPossessivePronoun());
            }else{
                return String.format("One %s clone hits %s between the legs with a fierce cunt-punt.",getSelf().name(),
                                target.nameOrPossessivePronoun());
            }
        }else{
            return String.format("One of %s clones delivers a swift punch to %s solar plexus.",getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun());
        }
    }

}
