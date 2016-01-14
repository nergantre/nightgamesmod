package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Lovestruck;

public class Kiss extends Skill {
    private static final String divineString = "Kiss of Baptism";
    private static final int divineCost = 30;

    public Kiss(Character self) {
        super("Kiss", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().kiss(getSelf()) && getSelf().canAct();
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
    public boolean resolve(Combat c, Character target) {
        int m = 2 + Global.random(2);
        boolean deep = getLabel(c).equals("Deep Kiss");
        if (getSelf().has(Trait.romantic)) {
            m += 3;
            if (deep) {
                m += 7;
            }
        }
        Result res = Result.normal;
        if (getSelf().get(Attribute.Seduction) >= 9) {
            m += 2 + Global.random(2);
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
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, m, res, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, m, res, target));
        }
        if (res == Result.upgrade) {
            target.drain(c, getSelf(), 10);
            target.loseWillpower(c, Global.random(3) + 2);
        }
        if (res == Result.divine) {
            target.buildMojo(c, 50);
            target.heal(c, 100);
            target.loseWillpower(c, Global.random(3) + 2, false);
            target.add(c, new Lovestruck(target, getSelf(), 2));
        }
        BodyPart selfMouth = getSelf().body.getRandom("mouth");
        target.body.pleasure(getSelf(), selfMouth, target.body.getRandom("mouth"), m, c);
        int selfDamage = Math.max(1, m / 4);
        if (selfMouth.isErogenous()) {
            selfDamage = m / 2;
        }
        getSelf().body.pleasure(target, target.body.getRandom("mouth"), selfMouth, selfDamage, c);
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
        if (modifier == Result.divine) {
            return getSelf().name()
                            + " seductively pulls you into a deep kiss. As first you try to match her enthusiastic tongue with your own, but she starts using her divine energy to directly attack your soul. "
                            + "Golden waves of ecstacy flow through your body, completely shattering every single thought you hold and replacing them with "
                            + getSelf().getName() + ".";
        }
        if (modifier == Result.upgrade) {
            return getSelf().name()
                            + " seductively pulls you into a deep kiss. As first you try to match her enthusiastic tongue with your own, but you're quickly overwhelmed. "
                            + "You start to feel weak as the kiss continues, and you realize she's draining you; her kiss is sapping your will to fight through your connection! "
                            + "You try to resist, but her splendid tonguework prevents you from mounting much of a defense.";
        }
        if (modifier == Result.special) {
            return getSelf().name()
                            + " seductively pulls you into a deep kiss. As first you try to match her enthusiastic tongue with your own, but you're quickly overwhelmed. She draws "
                            + "your tongue into her mouth and sucks on it in a way that seems to fill your mind with a pleasant, but intoxicating fog.";
        } else if (modifier == Result.weak) {
            return getSelf().name()
                            + " presses her lips against yours in a passionate, if not particularly skillful, kiss.";
        } else {
            switch (Global.random(3)) {
                case 0:
                    return getSelf().name()
                                    + " grabs you and kisses you passionately on the mouth. As you break for air, she gently nibbles on your bottom lip.";
                case 1:
                    return getSelf().name()
                                    + " peppers quick little kisses around your mouth before suddenly taking your lips forcefully and invading your mouth with her tongue.";
                default:
                    return getSelf().name()
                                    + " kisses you softly and romantically, slowly drawing you into her embrace. As you part, she teasingly brushes her lips against yours.";
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
}
