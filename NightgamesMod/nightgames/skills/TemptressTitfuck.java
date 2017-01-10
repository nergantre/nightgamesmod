package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.BodyFetish;
import nightgames.status.FiredUp;
import nightgames.status.Stsflag;

public class TemptressTitfuck extends Paizuri {

    public TemptressTitfuck(Character user) {
        super("Skillful Titfuck", user);
        addTag(SkillTag.usesBreasts);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.oral);
    }

    @Override
    public float priorityMod(Combat c) {
        return super.priorityMod(c) + 1.5f;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.temptress)&& user.get(Attribute.Technique) >= 15;
    }

    @Override
    public String describe(Combat c) {
        return "Use your supreme titfucking skills on your opponent's dick.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        BreastsPart breasts = getSelf().body.getLargestBreasts();
        for (int i = 0; i < 3; i++) {
            BreastsPart otherbreasts = getSelf().body.getRandomBreasts();
            if (otherbreasts.size > MIN_REQUIRED_BREAST_SIZE) {
                breasts = otherbreasts;
                break;
            }
        }
        

        int fetishChance = 7 + breasts.size + getSelf().get(Attribute.Fetish) / 2;

        int m = 7 + Global.random(getSelf().get(Attribute.Technique) / 2) + breasts.size;
        
        if(getSelf().is(Stsflag.oiled)) {
            m += Global.random(2, 5);
        }
        
        if( getSelf().has(Trait.lactating)) {
            m += Global.random(3, 5);
            fetishChance += 5;
        }

        if (target.roll(getSelf(), c, accuracy(c, target))) {
            if (!target.body.getRandomCock().isReady(target)) {
                m -= 7;
                target.body.pleasure(getSelf(), getSelf().body.getRandom("breasts"), target.body.getRandomCock(), m, c, this);
                if (target.body.getRandomCock().isReady(target)) {
                    // Was flaccid, got hard
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                    getSelf().add(c, new FiredUp(getSelf(), target, "breasts"));
                    
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("breasts"), target.body.getRandom("cock"), m, c, this);
                    if (Global.random(100) < fetishChance) {
                        target.add(c, new BodyFetish(target, getSelf(), BreastsPart.a.getType(), .05 + (0.01 * breasts.size) + getSelf().get(Attribute.Fetish) * .01));
                    }
                } else {
                    // Was flaccid, still is
                    c.write(getSelf(), deal(c, 0, Result.weak, target));
                }
                
                
            } else {
                FiredUp status = (FiredUp) getSelf().status.stream().filter(s -> s instanceof FiredUp).findAny()
                                .orElse(null);
                int stack = status == null || !status.getPart().equals("breasts") ? 0 : status.getStack();
                c.write(getSelf(), deal(c, stack, Result.normal, target));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("breasts"), target.body.getRandomCock(),
                                m + m * stack / 2, c, this);
                getSelf().add(c, new FiredUp(getSelf(), target, "breasts"));
                
                if (Global.random(100) < fetishChance) {
                    target.add(c, new BodyFetish(target, getSelf(), BreastsPart.a.getType(), .05 + (0.01 * breasts.size) + getSelf().get(Attribute.Fetish) * .01));
                }
            }
        } else {
            c.write(getSelf(), deal(c, 0, Result.miss, target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TemptressTitfuck(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        switch (modifier) {
            case miss:
                return String.format("%s towards %s %s, but %s %s hips back.", getSelf().subjectAction("move"),
                                target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                                target.pronoun(), target.action("pull"));
            case weak:
                return String.format(
                                "%s %s up %s flaccid %s between %s %s, doing everything %s"
                                                + " can to get it hard, but %s %s back before %s can manage it.",
                                getSelf().getName(),  getSelf().subjectAction("wrap"), target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target), 
                                getSelf().pronoun(), getSelf().body.getLargestBreasts().describe(getSelf()),  
                                getSelf().pronoun(), target.pronoun(), target.action("pull"), getSelf().pronoun());
            case special:
                return String.format(
                                "%s %s %s %s between her %s and %s them with intense pressure. %s %s hardens"
                                                + " instantly, throbbing happily in it's new home.",
                                 getSelf().pronoun(), getSelf().subjectAction("trap"), target.possessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().body.getLargestBreasts().describe(getSelf()),
                                getSelf().action("squeeze"), target.possessivePronoun(), target.body.getRandomCock().describe(target));
            default: // should be Result.normal
                switch (damage) {
                    case 0:
                        return String.format(
                                        "%s strokes %s %s with her %s in slow circular motions while"
                                                        + " lightly licking the tip, causing %s to groan in pleasure.",
                                        getSelf().getName(), target.nameOrPossessivePronoun(),
                                        target.body.getRandomCock().describe(target), getSelf().body.getLargestBreasts().fullDescribe(getSelf()), target.directObject());
                    case 1:
                        return String.format("%s tongue loops around the head of %s hard %s "
                                        + "and %s the shaft with her %s, constantly increasing  in intensity.",
                                        getSelf().nameOrPossessivePronoun(),
                                        target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                                        getSelf().action("milk"), getSelf().body.getLargestBreasts().fullDescribe(getSelf()));
                    default:
                        return String.format("As %s %s rapidly fuck %s %s, a pleasurable pressure constantly builds at the base. "
                                        + "All while %s %s the head sending bolts of electric pleasure back down %s shaft. "
                                        + "Overwhelmed from the pleasure, you grit %s teeth through a pleasure filled smile trying not to cum.",
                                        getSelf().nameOrPossessivePronoun(), getSelf().body.getLargestBreasts().describe(getSelf()),
                                        target.possessivePronoun(), target.body.getRandomCock().describe(target), getSelf().getName(),
                                        getSelf().action("suck"), target.possessivePronoun(), target.possessivePronoun());
                }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

}

