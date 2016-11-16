package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Stance;
import nightgames.status.Lovestruck;

public class Kiss extends Skill {
    private static final String divineString = "Kiss of Baptism";
    private static final int divineCost = 30;

    public Kiss(Character self) {
        super("Kiss", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().kiss(getSelf(), target) && getSelf().canAct();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        if (getLabel(c).equals(divineString)) {
            return 0;
        }
        return 10 + (getSelf().has(Trait.romantic) ? 5 : 0);
    }

    @Override
    public int getMojoCost(Combat c) {
        if (getLabel(c).equals(divineString)) {
            return divineCost;
        }
        return 0;
    }

    @Override
    public int accuracy(Combat c) {
        int accuracy = c.getStance().en == Stance.neutral ? 30 : 100;
        if (getSelf().has(Trait.romantic)) {
            accuracy += 40;
        }
        return accuracy;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = Global.random(6, 10);
        if (!target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, Result.miss, target);
            return false;
        }
        boolean deep = getLabel(c).equals("Deep Kiss");
        if (getSelf().has(Trait.romantic)) {
            m += 3;
            // if it's an advanced kiss.
            if (!getLabel(c).equals("Kiss")) {
                m += 3;
            }
        }
        Result res = Result.normal;
        if (getSelf().get(Attribute.Seduction) >= 9) {
            m += Global.random(4, 6);
            res = Result.normal;
        } else {
            res = Result.weak;
        }
        if (deep) {
            m += 5;
            res = Result.special;
        }
        if (getSelf().has(Trait.experttongue)) {
            m += 5;
            res = Result.special;
        }
        if (getSelf().has(Trait.soulsucker)) {
            res = Result.upgrade;
        }
        if (getLabel(c).equals(divineString)) {
            res = Result.divine;
            m += 20;
        }
        writeOutput(c, res, target);
        if (res == Result.upgrade) {
            target.drain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.drain, target, 10));
            target.loseWillpower(c, Global.random(3) + 2);
        }
        if (res == Result.divine) {
            target.buildMojo(c, 50);
            target.heal(c, 100);
            target.loseWillpower(c, Global.random(3) + 2, false);
            target.add(c, new Lovestruck(target, getSelf(), 2));
            getSelf().usedAttribute(Attribute.Divinity, c, .5);
        }
        BodyPart selfMouth = getSelf().body.getRandom("mouth");
        target.body.pleasure(getSelf(), selfMouth, target.body.getRandom("mouth"), m, c, this);
        int selfDamage = Math.max(1, m / 4);
        if (selfMouth.isErogenous()) {
            selfDamage = m / 2;
        }
        getSelf().body.pleasure(target, target.body.getRandom("mouth"), selfMouth, selfDamage, c, this);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 3;
    }

    @Override
    public Skill copy(Character user) {
        return new Kiss(user);
    }

    @Override
    public int speed() {
        return 6;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You pull " + target.name()
                            + " in for a kiss, but " + target.pronoun() + " pushes your face away. Rude.";
        }
        if (modifier == Result.divine) {
            return "You pull " + target.name()
                            + " to you and kiss her passionately, sending your divine aura into her body though her mouth. "
                            + "You tangle your tongue around hers and probe the sensitive insides her mouth while mirroring the action in the space of her soul, sending quakes of pleasure through her physical and astral body. "
                            + "As you finally break the kiss, she looks energized but desperate for more.";
        }
        if (modifier == Result.special) {
            return "You pull " + target.name()
                            + " to you and kiss her passionately. You run your tongue over her lips until she opens them and immediately invade her mouth. "
                            + "You tangle your tongue around hers and probe the sensitive insides her mouth. As you finally break the kiss, she leans against you, looking kiss-drunk and needy.";
        }
        if (modifier == Result.upgrade) {
            return "You pull " + target.name()
                            + " to you and kiss her passionately. You run your tongue over her lips until her opens them and immediately invade her mouth. "
                            + "You focus on her lifeforce inside her and draw it out through the kiss while overwhelming her defenses with heady pleasure. As you finally break the kiss, she leans against you, looking kiss-drunk and needy.";
        } else if (modifier == Result.weak) {
            return "You aggressively kiss " + target.name()
                            + " on the lips. It catches her off guard for a moment, but she soon responds approvingly.";
        } else {
            switch (Global.random(4)) {
                case 0:
                    return "You pull " + target.name()
                                    + " close and capture her lips. She returns the kiss enthusiastically and lets out a soft noise of approval when you "
                                    + "push your tongue into her mouth.";
                case 1:
                    return "You press your lips to " + target.name()
                                    + "'s in a romantic kiss. You tease out her tongue and meet it with your own.";
                case 2:
                    return "You kiss " + target.name()
                                    + " deeply, overwhelming her senses and swapping quite a bit of saliva.";
                default:
                    return "You steal a quick kiss from " + target.name()
                                    + ", pulling back before she can respond. As she hesitates in confusion, you kiss her twice more, "
                                    + "lingering on the last to run your tongue over her lips.";
            }

        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().subject()
                            + " pulls you in for a kiss, but you manage to push her face away.";
        }
        if (modifier == Result.divine) {
            return String.format("%s seductively pulls %s into a deep kiss. As first %s %s to match %s enthusiastic"
                            + " tongue with %s own, but %s starts using %s divine energy to directly attack %s soul. "
                            + "Golden waves of ecstacy flow through %s body, completely shattering every single thought %s and replacing them with %s.",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("try", "tries"), getSelf().possessivePronoun(),
                            target.possessivePronoun(), getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), target.possessivePronoun(), 
                            target.subjectAction("hold"), getSelf().name());
        }
        if (modifier == Result.upgrade) {
            return String.format("%s seductively pulls %s into a deep kiss. As first %s %s to match %s "
                            + "enthusiastic tongue with %s own, but %s %s quickly overwhelmed. "
                            + "%s to feel weak as the kiss continues, and %s %s %s is "
                            + "draining %s; %s kiss is sapping %s will to fight through %s connection! "
                            + "%s to resist, but %s splendid tonguework prevents "
                            + "%s from mounting much of a defense.",
                            getSelf().subject(), target.nameDirectObject(), target.subject(),
                            target.action("try", "tries"), getSelf().possessivePronoun(),
                            target.possessivePronoun(), target.pronoun(), target.action("are", "is"),
                            Global.capitalizeFirstLetter(target.subjectAction("start")),
                            target.pronoun(), target.action("realize"), getSelf().subject(),
                            target.directObject(), getSelf().possessivePronoun(), 
                            target.nameOrPossessivePronoun(), c.bothPossessive(target), 
                            Global.capitalizeFirstLetter(target.subjectAction("try", "tries")),
                            getSelf().nameOrPossessivePronoun(), target.directObject());
        }
        if (modifier == Result.special) {
            return String.format("%s seductively pulls %s into a deep kiss. As first %s %s to match %s "
                            + "enthusiastic tongue with %s own, but %s %s quickly overwhelmed. %s draws "
                            + "%s tongue into %s mouth and sucks on it in a way that seems to fill %s "
                            + "mind with a pleasant, but intoxicating fog.",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("try", "tries"), getSelf().possessivePronoun(),
                            target.possessivePronoun(), target.pronoun(), target.action("are", "is"),
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(), target.possessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format("%s presses %s lips against %s in a passionate, if not particularly skillful, kiss.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            target.human() ? "yours" : target.nameOrPossessivePronoun());
        } else {
            switch (Global.random(3)) {
                case 0:
                    return String.format("%s grabs %s and kisses %s passionately on the mouth. "
                                    + "As %s for air, %s gently nibbles on %s bottom lip.",
                                    getSelf().subject(), target.nameDirectObject(), target.directObject(),
                                    target.subjectAction("break"), getSelf().subject(), target.possessivePronoun());
                case 1:
                    return String.format("%s peppers quick little kisses around %s mouth before suddenly"
                                    + " taking %s lips forcefully and invading %s mouth with %s tongue.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(),
                                    target.possessivePronoun(), target.possessivePronoun(),
                                    getSelf().possessivePronoun());
                default:
                    return String.format("%s kisses %s softly and romantically, slowly drawing %s into %s "
                                    + "embrace. As %s part, %s teasingly brushes %s lips against %s.",
                                    getSelf().subject(), target.nameDirectObject(), target.directObject(),
                                    getSelf().possessivePronoun(), c.bothSubject(target),
                                    getSelf().subject(), target.possessivePronoun(),
                                    target.human() ? "yours" : target.possessivePronoun());
            }
        }
    }

    @Override
    public String describe(Combat c) {
        return "Kiss your opponent";
    }

    @Override
    public boolean makesContact() {
        return true;
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Divinity) >= 1 && getSelf().canSpend(divineCost)) {
            return divineString;
        } else if (getSelf().has(Trait.soulsucker)) {
            return "Drain Kiss";
        } else if (getSelf().get(Attribute.Seduction) >= 20) {
            return "Deep Kiss";
        } else {
            return "Kiss";
        }
    }
    
    @Override
    public Stage getStage() {
        return Stage.FOREPLAY;
    }
}
