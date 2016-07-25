package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class BunshinService extends Skill {

    public BunshinService(Character self) {
        super("Bunshin Service", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 12;
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
                             .penetrated(getSelf())
                        && target.mostlyNude();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public String describe(Combat c) {
        return "Pleasure your opponent with shadow clones: 4 mojo per attack (min 2))";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int clones = Math.min(Math.min(getSelf().getMojo()
                                                .get()
                        / 4, getSelf().get(Attribute.Ninjutsu) / 3), 5);
        getSelf().buildMojo(c, (5 - clones) * 4);
        Result r;
        if (getSelf().human()) {
            c.write(String.format("You form %d shadow clones and rush forward.", clones));
        } else if (target.human()) {
            c.write(String.format("%s moves in a blur and suddenly you see %d of %s approaching you.", getSelf().name(),
                            clones, getSelf().pronoun()));
        }
        for (int i = 0; i < clones; i++) {
            if (target.roll(this, c, accuracy(c) + getSelf().get(Attribute.Speed) + getSelf().getLevel())) {
                switch (Global.random(4)) {
                    case 0:
                        r = Result.weak;
                        target.tempt(Global.random(3) + getSelf().get(Attribute.Seduction) / 4);
                        break;
                    case 1:
                        r = Result.normal;
                        target.body.pleasure(getSelf(),  getSelf().body.getRandom("hands"),target.body.getRandomBreasts(),
                                        Global.random(3 + getSelf().get(Attribute.Seduction) / 2)
                                                        + target.get(Attribute.Perception) / 2,
                                        c, this);
                        break;
                    case 2:
                        r = Result.strong;
                        BodyPart targetPart = target.body.has("cock") ? target.body.getRandomCock()
                                        : target.hasPussy() ? target.body.getRandomPussy()
                                                        : target.body.getRandomAss();
                        target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"),targetPart, 
                                        Global.random(4 + getSelf().get(Attribute.Seduction))
                                                        + target.get(Attribute.Perception) / 2,
                                        c, this);
                        break;
                    default:
                        r = Result.critical;
                        targetPart = target.body.has("cock") ? target.body.getRandomCock()
                                        : target.hasPussy() ? target.body.getRandomPussy()
                                                        : target.body.getRandomAss();
                        target.body.pleasure(getSelf(),getSelf().body.getRandom("hands"), targetPart, Global.random(6)
                                        + getSelf().get(Attribute.Seduction) / 2 + target.get(Attribute.Perception), c,
                                        this);
                        break;
                }
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, r, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, r, getSelf()));
                }
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.miss, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.miss, getSelf()));
                }
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new BunshinService(user);
    }

    @Override
    public int speed() {
        return 4;
    }
    
    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if(modifier==Result.miss){
            return String.format("%s dodges your clone's groping hands.",target.name());
        }else if(modifier==Result.weak){
            return String.format("Your clone darts close to %s and kisses %s on the lips.",target.name(),target.directObject());
        }else if(modifier==Result.strong){
            if(target.hasDick()){
                return String.format("Your shadow clone grabs %s's dick and strokes it.",target.name());
            }else{
                return String.format("Your shadow clone fingers and caresses %s's pussy lips.",target.name());
            }
        }else if(modifier==Result.critical){
            if(target.hasDick()){
                return String.format("Your clone attacks %s's sensitive penis, rubbing and stroking %s glans.",target.name(),target.possessivePronoun());
            }else{
                return String.format("Your clone slips between %s's legs to lick and suck %s swollen clit.",target.name(),target.possessivePronoun());
            }
        }else{
            return String.format("A clone pinches and teases %s's nipples",target.name());
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if(modifier==Result.miss){
            return String.format("You manage to avoid one of the shadow clones.");
        }else if(modifier==Result.weak){
            return String.format("One of the %ss grabs you and kisses you enthusiastically.",getSelf().name());
        }else if(modifier==Result.strong){
            if(target.hasBalls()){
                return String.format("A clone gently grasps and massages your sensitive balls.");
            }else{
                return String.format("A clone teases and tickles your inner thighs and labia.");
            }
        }else if(modifier==Result.critical){
            if(target.hasDick()){
                return String.format("One of the %s clones kneels between your legs to lick and suck your cock.",getSelf().name());
            }else{
                return String.format("One of the %s clones kneels between your legs to lick your nether lips.",getSelf().name());
            }
        }else{
            if(getSelf().hasBreasts()){
                return String.format("A %s clone presses her boobs against you and teases your nipples.",getSelf().name());
            }else{
                return String.format("A %s clone caresses your chest and teases your nipples.",getSelf().name());
            }
            
        }
    }

}
