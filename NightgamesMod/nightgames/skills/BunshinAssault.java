package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class BunshinAssault extends Skill {

    public BunshinAssault(Character self) {
        super("Bunshin Assault", self);
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
                             .penetrated(target)
                        && !c.getStance()
                             .penetrated(getSelf());
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
            c.write(String.format("You form %d shadow clones and rush forward.",clones));
        }
        else if(target.human()){
            c.write(String.format("%s moves in a blur and suddenly you see %d of %s approaching you.",getSelf().name(),clones,getSelf().pronoun()));
        }
        for(int i=0;i<clones;i++){
            if(target.roll(this, c, accuracy(c)+getSelf().get(Attribute.Speed) + getSelf().getLevel())) {
                switch(Global.random(4)){
                case 0:
                    r=Result.weak;
                    target.pain(c,Global.random(4)+3);
                    break;
                case 1:
                    r=Result.normal;
                    target.pain(c,Global.random(4)+getSelf().get(Attribute.Power)/3);
                    break;
                case 2:
                    r=Result.strong;
                    target.pain(c,Global.random(8)+getSelf().get(Attribute.Power)/2);
                    break;
                default:
                    r=Result.critical;
                    target.pain(c,Global.random(12)+getSelf().get(Attribute.Power));
                    break;
                }
                if(getSelf().human()){
                    c.write(getSelf(),deal(c,0,r,target));
                }
                else if(target.human()){
                    c.write(getSelf(),receive(c,0,r,getSelf()));
                }
            }else{
                if(getSelf().human()){
                    c.write(getSelf(),deal(c,0,Result.miss,target));
                }
                else if(target.human()){
                    c.write(getSelf(),receive(c,0,Result.miss,getSelf()));
                }
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
            return String.format("You quickly dodge a shadow clone's attack.");
        }else if(modifier==Result.weak){
            return String.format("You lose sight of one of the clones until you feel a sharp spank on your ass cheek.");
        }else if(modifier==Result.strong){
            if(target.hasBalls()){
                return String.format("A %s clone gets a hold of your balls and squeezes them painfully.",getSelf().name());
            }else{
                return String.format("A %s clone unleashes a quick roundhouse kick that hits your sensitive boobs.",getSelf().name());
            }
        }else if(modifier==Result.critical){
            if(target.hasBalls()){
                return String.format("One lucky %s clone manages to land a snap-kick squarely on your unguarded jewels.",getSelf().name());
            }else{
                return String.format("One %s clone hits you between the legs with a fierce cunt-punt.",getSelf().name());
            }
        }else{
            return String.format("One of %s clones delivers a swift punch to your solar plexus.",getSelf().possessivePronoun());
        }
    }

}
