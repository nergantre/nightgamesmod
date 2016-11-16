package nightgames.characters;

import java.util.Optional;

import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TentaclePart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.skills.Skill;
import nightgames.start.NpcConfiguration;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;

public class Airi extends BasePersonality {
    /**
     *
     */
    private static final long serialVersionUID = -8169646189131720872L;

    public Airi() {
        this(Optional.empty(), Optional.empty());
    }

    public Airi(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Airi", 10, charConfig, commonConfig, false);
    }

    @Override
    public void applyStrategy(NPC self) {
        self.plan = Plan.retreating;
        self.mood = Emotion.nervous;
    }

    @Override
    public void applyBasicStats(Character self) {
        self.change();
        self.setTrophy(Item.AiriTrophy);
        preferredCockMod = CockMod.slimy;

        self.outfitPlan.add(Clothing.getByID("shirt"));
        self.outfitPlan.add(Clothing.getByID("bra"));
        self.outfitPlan.add(Clothing.getByID("panties"));
        self.outfitPlan.add(Clothing.getByID("skirt"));
        self.outfitPlan.add(Clothing.getByID("pantyhose"));
        self.outfitPlan.add(Clothing.getByID("shoes"));
        self.change();
        self.rank = 1;
        self.modAttributeDontSaveData(Attribute.Power, -1);
        self.modAttributeDontSaveData(Attribute.Slime, 1);
        self.modAttributeDontSaveData(Attribute.Cunning, 2);
        self.modAttributeDontSaveData(Attribute.Speed, -1);
        self.modAttributeDontSaveData(Attribute.Seduction, 6);
        self.getStamina().setMax(50);
        self.getArousal().setMax(80);
        self.getMojo().setMax(100);
        self.getWillpower().setMax(80);
        self.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 1;
        character.getGrowth().arousal = 1;
        character.getGrowth().willpower = 1.5f;
        character.getGrowth().bonusStamina = 1;
        character.getGrowth().bonusArousal = 1;
        character.getGrowth().addTrait(0, Trait.dexterous);
        character.getGrowth().addTrait(0, Trait.imagination);
        character.getGrowth().addTrait(0, Trait.softheart);
        character.getGrowth().addTrait(0, Trait.repressed);
        character.getGrowth().addTrait(9, Trait.limbTraining1);
        character.getGrowth().addTrait(12, Trait.lacedjuices);
        character.getGrowth().addTrait(15, Trait.QuickRecovery);
        character.getGrowth().addTrait(18, Trait.BoundlessEnergy);
        character.getGrowth().addTrait(23, Trait.sexTraining1);
        character.getGrowth().addTrait(31, Trait.limbTraining2);
        character.getGrowth().addTrait(37, Trait.tongueTraining1);
        character.getGrowth().addTrait(44, Trait.limbTraining3);
        character.getGrowth().addTrait(51, Trait.sexTraining2);
        character.getGrowth().addTrait(58, Trait.tongueTraining2);

        preferredAttributes.add(c -> Optional.of(Attribute.Slime));
        preferredAttributes.add(c -> Optional.of(Attribute.Seduction));
    }
    
    @Override
    public void eot(Combat c, Character opponent, Skill last) {
        // always replace with gooey/slime versions of genitals.
        if (character.has(Trait.slime)) {
            if (character.hasPussy() && !character.body.getRandomPussy().moddedPartCountsAs(character, PussyPart.gooey)) {
                character.body.temporaryAddOrReplacePartWithType(PussyPart.gooey, 999);
                c.write(character, 
                                Global.format("{self:NAME-POSSESSIVE} %s turned back into a gooey pussy.",
                                                character, opponent, character.body.getRandomPussy()));
            }
            if (character.hasDick() && !character.body.getRandomCock().moddedPartCountsAs(character, CockMod.slimy)) {
                character.body.temporaryAddOrReplacePartWithType(character.body.getRandomCock().applyMod(CockMod.slimy), 999);
                c.write(character, 
                                Global.format("{self:NAME-POSSESSIVE} %s turned back into a gooey pussy.",
                                                character, opponent, character.body.getRandomPussy()));
            }
        }
    }

    @Override
    public void resolveOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times, int totalTimes) {
        if (times == totalTimes && character.getWillpower().percent() < 60 && !character.has(Trait.slime)) {
            c.write(character, Global.format("After {self:NAME-POSSESSIVE} orgasm, her whole body shimmers and melts into a puddle of goo. A human body rises from the slime and molds itself to a facsimile of {self:reflective}. "
                            + "Gone is the slim repressed girl you knew. The new Airi that appears before you is a sexually idealized version of herself, with bigger breasts, a dynamic body line and long legs that end in a ball of blue goo. "
                            + "You're now fighting {self:name} in slime form!", character, opponent));
            character.nudify();
            character.purge(c);
            character.addTemporaryTrait(Trait.slime, 999);
            character.removeTemporaryTrait(Trait.repressed, 999);
            character.removeTemporaryTrait(Trait.softheart, 999);
            if (character.hasPussy() && !character.body.getRandomPussy().moddedPartCountsAs(character, PussyPart.gooey)) {
                character.body.temporaryAddOrReplacePartWithType(PussyPart.gooey, 999);
            }
            if (character.hasDick() && !character.body.getRandomCock().moddedPartCountsAs(character, CockMod.slimy)) {
                character.body.temporaryAddOrReplacePartWithType(character.body.getRandomCock().applyMod(CockMod.slimy), 999);
            }
            BreastsPart part = character.body.getBreastsBelow(BreastsPart.h.size);
            if (part != null) {
                character.body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
            }
            character.body.temporaryAddOrReplacePartWithType(new GenericBodyPart("gooey skin", 2.0, 1.5, .8, "skin", ""), 999);
            character.body.temporaryAddOrReplacePartWithType(new TentaclePart("slime pseudopod", "back", "slime", 0.0, 1.0, 1.0), 999);
            character.body.temporaryAddOrReplacePartWithType(new TentaclePart("gooey feelers", "hands", "slime", 0.0, 1.0, 1.0), 999);
            character.body.temporaryAddOrReplacePartWithType(new TentaclePart("gooey feelers", "feet", "slime", 0.0, 1.0, 1.0), 999);
            character.body.temporaryAddOrReplacePartWithType(new TentaclePart("slime filaments", "pussy", "slime", 0.0, 1.0, 1.0), 999);
            if (character.level >= 21) {
                character.addTemporaryTrait(Trait.Sneaky, 999);
            }
            if (character.level >= 24) {
                character.addTemporaryTrait(Trait.shameless, 999);
            }
            if (character.level >= 27) {
                character.addTemporaryTrait(Trait.lactating, 999);
            }
            if (character.level >= 30) {
                character.addTemporaryTrait(Trait.addictivefluids, 999);
            }
            if (character.level >= 33) {
                character.addTemporaryTrait(Trait.autonomousPussy, 999);
            }
            if (character.level >= 36) {
                character.addTemporaryTrait(Trait.entrallingjuices, 999);
            }
            if (character.level >= 39) {
                character.addTemporaryTrait(Trait.energydrain, 999);
            }
            if (character.level >= 42) {
                character.addTemporaryTrait(Trait.desensitized, 999);
            }
            if (character.level >= 45) {
                character.addTemporaryTrait(Trait.steady, 999);
            }
            if (character.level >= 50) {
                character.addTemporaryTrait(Trait.strongwilled, 999);
            }
            character.moodSwing(c);
        }
    }
    
    @Override
    public void rest(int time) {
        super.rest(time);
        Decider.visit(character);
        int r;
        for (int i = 0; i < time; i++) {
            r = Global.random(8);
            if (r == 1) {
                Global.getDay().visit("Exercise", this.character, 0);
            } else if (r == 0) {
                Global.getDay().visit("Browse Porn Sites", this.character, 0);
            }
        }
    }

    @Override
    public String bbLiner(Combat c, Character other) {
        return character.has(Trait.slime) ? "Airi grimaces as you fall. <i>\"Apologies... but necessary.... Please understand...\"</i>" : "<i>\"Sorry... I hope it didn't hurt too badly...\"</i>";
    }

    @Override
    public String nakedLiner(Combat c, Character opponent) {
        // always naked in slime form
        return character.has(Trait.slime) ? "" : "<i>Nooo! Don't look at me!</i>";
    }

    @Override
    public String stunLiner(Combat c, Character opponent) {
        return character.has(Trait.slime) ? "Airi glares at you from the puddle she formed on the floor. <i>\"Unforgivable...\"</i>" : "<i>\"Unforgivable...\"</i>";
    }

    @Override
    public String taunt(Combat c, Character opponent) {
        return character.has(Trait.slime) ? "Airi coos at you <i>\"About to cum..? ...even trying..?\"</i>" : "<i><b>Giggle</b> \"Try a bit harder okay?\"</i>";
    }

    @Override
    public String temptLiner(Combat c, Character opponent) {
        return character.has(Trait.slime) ? "<i>\"Fill me with yourself... forget everything...\"</i>" : "<i>\"Uhm, it's okay, you can come inside...\"</i>";
    }

    @Override
    public String victory(Combat c, Result flag) {
        Character opponent;
        if (c.p1 == character) {
            opponent = c.p2;
        } else {
            opponent = c.p1;
        }
        character.arousal.empty();
        opponent.arousal.empty();
        character.purge(c);
        return "Airi crawls over to you at an agonizing pace. Her slime rapidly flows on top of your penis and covers it in a sticky bulb. <i>\"Time… for you to cum…\"</i><br><br>"
                        + "Her previously still slime suddenly starts to frantically squeeze and knead your cock, pulsating in waves of sticky goo on top of you. Startled by the sudden stimulation, you barely manage to hold on. Unfortunately--or perhaps fortunately--for you, Airi is not finished. She also covers your chest with her own sticky breasts and engulfs your nipples inside hers. Although it’s just slime, you feel as if her lips are on your nipples, sucking them and rolling the tips around inside her mouth.<br><br>"
                        + "As you’re being overloaded with sensations, Airi brings her face close to yours and whispers in your ear.<i>\"Cum… cum… cum…\"</i> With a groan of agonising pleasure, you come hard, firing ropes of your seed inside her translucent depths.<br><br>"
                        + "Panting with exertion from the aftershocks of your orgasm, you see your cum floating around in her body quickly getting absorbed and disappearing into nothing. Sensing danger, you glance at Airi's face <i>\"...Not enough... I need more food...\"</i><br><br>"
                        + "This time Airi engulfs your whole body, leaving only your face outside, facing the sky. Try as you might, you can't even move your neck to see what's happening below. Feeling frightened at what she might do, you tense up your body to attempt to resist. <i>\"Are you... ready..? I'll begin...\"</i> Whatever you expected, it was not this. Her whole body begins to churn around your own, both massaging and licking every square inch of you. You feel a tendril of slime enter your ass and press against your prostate. At the same time two tendrils of slime enter your ears and attach themselves to something deep inside your head. In seconds, you feel Airi literally inside your head.<br><br>"
                        + "<i>\"Cum...\"</i> An orgasm like nothing you felt before tears through your body, searing your head until your vision turns white.<br><br>"
                        + "<i>\"Cum...\"</i> Another climax wracks you, suspending all your thoughts.<br><br>"
                        + "<i>\"Cum...\"</i> Your cum turns thin, flowing out like water.<br><br>"
                        + "<i>\"Give yourself... to me...\"</i> One final orgasm leaves you out cold. When you come to, you see Airi has left, taking your boxers like that. Wow, you're not sure how many more of these you can endure.<br><br>";
    }

    @Override
    public String defeat(Combat c, Result flag) {
        character.purge(c);
        return "Fighting Airi is not easy. Her stickiness makes it"
                        + " quite difficult for you to accomplish much of anything. Still, "
                        + "considering her incoherent babbling she's probably not got much fight left in her. "
                        + "In a desperate attempt, she launces herself at you, knocking you both to the ground."
                        + " You react quickly, rolling over and getting on top before she can stick you to the "
                        + "floor. Her slime crawls up your sides, seeking to engulf and immobilize you, but you "
                        + "raise yourself up slightly, out of her reach. You reach down with one hand and deftly"
                        + " get to work on the area of slime shaped like a pussy. It's very different from a "
                        + "regular girl's, considering that your fingers actually <i>sink in</i> rather than graze"
                        + " over, and that she is sticky enough to prevent them from moving very fast. Still, "
                        + "it's clearly having the desired effect. You redouble your efforts, even leaning in to "
                        + "kiss her, not caring about the effects her slime has when ingested. Before long, Airi's "
                        + "mumbling reaches a crescendo, and she wraps her gooey limbs around you. Her body loses"
                        + " shapes as she cums, as if it's melting around you. Within a few seconds you are lying "
                        + "half-embedded in an amorphous heap of slime. Worried for Airi's safety, you jump up and call her name."
                        + " Fortunately, it is not long before she reassumes her human form with a look of ecstasy still"
                        + " on her face. <i>\"Wonderful... Reward...\"</i> she mutters, even breathier than usual. "
                        + "She gently pulls you back down onto her. Since you've already won, you do not resist as Airi "
                        + "lets you sink in to her body slightly. Nor when every bit of her body starts vibrating against "
                        + "your skin. The full-body massage is wonderful, especially when it starts focusing more and more "
                        + "on your dick. It's somehow ended up in her pussy, which is milking you greedily. Seeing no reason"
                        + " to stop her, you just relax in her embrace and let her slowly drive you closer. Right before you "
                        + "cum, she kisses you again, pushing you over the edge. There is no frantic milking this time, just "
                        + "a great orgasm. When you have both recovered, you get up and she hands you a small bit of slime "
                        + "that she separated from her body. <i>\"Trophy... No clothes... Will grow back...\"</i> You take "
                        + "the slime from her, examining it before stashing it away. It tingles in your hand, and you "
                        + "wonder just how much control she still has over it as she glides away.";
    }

    @Override
    public String describe(Combat c) {
        return character.has(Trait.slime) ? "A crystal blue figure stands in front of you. Well, \"stands\" might be an exaggeration. "
                        + "Airi sports a cute face and a tight body, but her thighs end in a giant ball of slime. "
                        + "Indeed, while her body might look human at a distance, she seems to be composed of a soft, translucent gel."
                        : "Airi looks at you cautiously. Airi sports a cute face and a tight body with shoulder length black hair "
                                        + "almost covering her bright intelligent black eyes. You're not too sure what she's thinking "
                                        + "so you approach her cautiously.";
    }

    @Override
    public String draw(Combat c, Result flag) {
        return "You make each other cum at the same time. Woohoo!";

    }

    @Override
    public boolean fightFlight(Character opponent) {
        return true;
    }

    @Override
    public boolean attack(Character opponent) {
        return true;
    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "Airi crawls over to you at an agonizing pace. Her slime rapidly flows on top of your penis and covers it in a sticky bulb. <i>\"Time… for you to cum…\"</i><br><br>"
                            + "Her previously still slime suddenly starts to frantically squeeze and knead your cock, pulsating in waves of sticky goo on top of you. Startled by the sudden stimulation, you cum in seconds, spilling the proof of your defeat inside her tendril.<br><br>";
        } else {
            return "Airi flows over to " + target.name()
                            + ". Her slime pools into a long and flexible appendage and worms itself inside "
                            + target.possessivePronoun()
                            + " depths. The appendage starts to twist and squirm inside her poor victim and almost instantly causes her victim to scream out in pleasure.<br><br>";
        }
    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return Global.format(
                            "Your fight with {other:name} seemed to have ran into a stalemate. Neither of you is willing to get close enough to each other for anything substantial to happen. You just continue to trade taunts whilst waiting for an opportunity.<br><br>"
                                            + "Suddenly, you feel something grasp your ankles and pull you off balance. You brace yourself for the fall, but after a second, you only feel softness on your back. It’s Airi. She somehow snuck up on you and tripped you into falling on top of her. She quickly engulfs your hands and legs in her slime and presents your helpless body to {other:name}’s ministrations.",
                            character, assist);
        } else {
            return Global.format(
                            "Your fight with {other:name} seemed to have ran into a stalemate. Neither of you is willing to get close enough to each other for anything substantial to happen. You just continue to trade taunts whilst waiting for an opportunity.<br><br>"
                                            + "Suddenly, a blue blob appears in your line of sight. It’s Airi! More swiftly than you would expect, Airi moves to {other:name}’s side and engulfs her body in her own. After dissolving her clothing with her slime, Airi surfaces only {other:name-possessive} torso and sex, presenting her to you. Well, presented with a gift on a silver platter, you’re not going to refuse!",
                            character, target);
        }
    }

    @Override
    public String startBattle(Character other) {
        if (other.human()) {
            return character.has(Trait.slime)
                            ? "Airi's main body rises up from her slime blob and forms the demure beauty you're used to seeing. <i>\"Delicious... Quickly... Give me your seed...\"</i>"
                            : "You're fighting Airi, a reticent asian girl. She looks pretty normal for now, but you know she's holding a secret.";
        } else {
            return "You... will do...";
        }
    }

    @Override
    public boolean fit() {
        return !character.mostlyNude() && character.getStamina().percent() >= 50
                        || character.getArousal().percent() > 50;
    }

    @Override
    public String night() {
        return "You walk back to your dorm after the match, and decide to take a shower after all that exertion. Who knew sex fighting a bunch of girls would be so exhausting? You strip off your shirt and boxers and head straight into the bathroom. As you flip on the lights, you notice that the tub seems already filled with water. Just as you wonder if you forgot to drain the tub from last night, the liquid in the tub quivers and… stands up. "
                        + "<br><br>It’s Airi. What’s she doing here? You ask her how did get in, since you were sure the door was locked. <i>Followed you… flowed under door… No problem…</i> Well, that explains it. Noticing the time, you let her know that you really need to take your shower now and head to bed or you won’t make it tomorrow for your morning classes. Airi looks at you for a second and nods. <i>Un… will help you clean…</i> Wait what? Oh n-! Airi pulls you into the tub with her gooey appendages and submerges you inside her body. <i>Relax… I’ll clean you up… Inside and out…</i>";
    }

    public void advance() {

    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case confident:
                return value >= 50;
            case desperate:
                return value >= 150;
            default:
                return value >= 100;
        }
    }

    @Override
    public String orgasmLiner(Combat c) {
        if (character.has(Trait.slime)) {
            return "<i>\"Ahhnn... forgot how good... feels... Will return favor...\"</i>.";
        } else if (character.getWillpower().percent() > 90) {
            return "<i>\"Aah that was a bit too much! Slow down a bit...\"</i>";
        } else if (character.getWillpower().percent() > 75) {
            return "<i>\"Aaahhh... my head's feeling fuzzy...\"</i>";
        } else {
            return "<i>\"I need more... Give me more...\"</i>";
        }
        
    }

    @Override
    public String makeOrgasmLiner(Combat c) {
        return character.has(Trait.slime) ? "<i>\"...Feels good..? I'll suck more out... I'll drain you dry...\"</i>" : "<i>\"Feels good? Let me have some more...\"</i>";
    }

    @Override
    public String image(Combat c) {
        if (character.has(Trait.slime)) {
            SlimeMimicry mimicry = (SlimeMimicry) character.getStatus(Stsflag.mimicry);
            if (mimicry != null) {
                return "airi_" + mimicry.getMimickedName() + "_slime.jpg";
            } else {
                return super.image(c);
            }
        } else if (character.getWillpower().percent() > 90) {
            return "airi_human.jpg";
        } else if (character.getWillpower().percent() > 75) {
            return "airi_mostly_human.jpg";
        } else {
            return "airi_mostly_slime.jpg";
        }
    }
}
