package nightgames.characters;

import java.util.Optional;

import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.start.NpcConfiguration;
import nightgames.status.Drowsy;
import nightgames.status.Energized;

public class Maya extends BasePersonality {

    private static final long serialVersionUID = 447375506153223682L;

    public Maya(int playerLevel) {
        this(playerLevel, Optional.empty(), Optional.empty());
    }

    public Maya(int playerLevel, Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Maya", 50, charConfig, commonConfig, false);

        while (character.getLevel() < playerLevel + 20) {
            character.ding();
        }
    }

    @Override
    public void applyStrategy(NPC self) {
        self.plan = Plan.hunting;
        self.mood = Emotion.confident;
    }

    @Override
    public void applyBasicStats(Character self) {
        self.outfitPlan.add(Clothing.getByID("camisole"));
        self.outfitPlan.add(Clothing.getByID("blouse"));
        self.outfitPlan.add(Clothing.getByID("lacepanties"));
        self.outfitPlan.add(Clothing.getByID("skirt"));
        self.outfitPlan.add(Clothing.getByID("sneakers"));
        self.outfitPlan.add(Clothing.getByID("socks"));
        self.change();
        self.modAttributeDontSaveData(Attribute.Dark, 10);
        self.modAttributeDontSaveData(Attribute.Seduction, 15);
        self.modAttributeDontSaveData(Attribute.Cunning, 15);
        self.modAttributeDontSaveData(Attribute.Speed, 2);
        self.modAttributeDontSaveData(Attribute.Power, 7);
        self.modAttributeDontSaveData(Attribute.Hypnosis, 5);
        self.getStamina().setMax(90);
        self.getArousal().setMax(150);
        self.getMojo().setMax(150);
        self.getWillpower().setMax(100);

        Global.gainSkills(self);
        self.setTrophy(Item.MayaTrophy);

        self.body.add(BreastsPart.d);
        self.initialGender = CharacterSex.female;
        preferredCockMod = CockMod.error;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 2;
        character.getGrowth().arousal = 5;
        character.getGrowth().willpower = 1;
        character.getGrowth().bonusStamina = 2;
        character.getGrowth().bonusArousal = 5;
        character.getGrowth().addTrait(0, Trait.darkpromises);
        character.getGrowth().addTrait(0, Trait.tongueTraining1);
        character.getGrowth().addTrait(0, Trait.tongueTraining2);
        character.getGrowth().addTrait(0, Trait.limbTraining1);
        character.getGrowth().addTrait(0, Trait.limbTraining2);
        character.getGrowth().addTrait(0, Trait.sexTraining1);
        character.getGrowth().addTrait(0, Trait.sexTraining2);
        character.getGrowth().addTrait(0, Trait.sexTraining3);
        character.getGrowth().addTrait(0, Trait.analTraining1);
        character.getGrowth().addTrait(0, Trait.analTraining2);
        character.getGrowth().addTrait(0, Trait.Confident);
        character.getGrowth().addTrait(0, Trait.dickhandler);
        character.getGrowth().addTrait(0, Trait.tight);
        character.getGrowth().addTrait(0, Trait.vaginaltongue);
        character.getGrowth().addTrait(0, Trait.insertion);
        character.getGrowth().addTrait(0, Trait.holecontrol);
        character.getGrowth().addTrait(0, Trait.autonomousPussy);
        character.getGrowth().addTrait(0, Trait.experienced);
        character.getGrowth().addTrait(0, Trait.responsive);
        character.getGrowth().addTrait(0, Trait.powerfulhips);

        character.getGrowth().addTrait(0, Trait.enchantingVoice);
        character.getGrowth().addTrait(0, Trait.unnaturalgrowth);
        character.getGrowth().addTrait(0, Trait.event);
        character.getGrowth().addTrait(0, Trait.cursed);
    }

    @Override
    public String bbLiner(Combat c, Character other) {
        return "Maya looks at you sympathetically. <i>\"Was that painful? Don't worry, you aren't seriously injured. Our Benefactor protects us.\"</i>";
    }

    @Override
    public String nakedLiner(Combat c, Character opponent) {
        return "Maya smiles, unashamed of her nudity. <i>\"Well done. Not many participants are able to get my clothes off anymore. You'll at least be able to look at a naked woman while you orgasm.\"</i>";
    }

    @Override
    public String stunLiner(Combat c, Character opponent) {
        return "You think you see something dangerous flicker in Maya's eyes. <i>\"Well done. I may need to get a little serious.\"</i>";
    }

    @Override
    public String taunt(Combat c, Character opponent) {
        return "Maya gives you a look of gentle disapproval. <i>\"You aren't putting up much of a fight, are you? Aren't you a little overeager to cum?\"</i>";
    }

    @Override
    public String victory(Combat c, Result flag) {
        Character target = c.getOpponent(character);
        target.add(c, new Drowsy(target));
        character.arousal.empty();
        character.add(c, new Energized(character, 10));
        return "Maya completely outmatches you. How were you suppose to deal with"
                        + " someone this skilled? Your cock spurts a sticky white flag in her hands "
                        + "as you cum. You slump down to the floor as you catch your breath. Maya "
                        + "smiles gently at you, but there's a predatory glint in her eyes. <i>\"Don't "
                        + "feel bad. You put up an impressive fight, given your inexperience.\"</i> "
                        + "She seems to be looking into your soul. You feel an almost animal instinct "
                        + "to look away, but can't bring yourself to break eye contact. <i>\"I hope "
                        + "you learned something, because the lesson is over now. It's time for me to "
                        + "take what I came for.\"</i><p>You wake up on the floor, naked and alone."
                        + " It doesn't seem like much time passed, but you feel lethargic and almost"
                        + " hung-over. What the hell happened to you?";
    }

    @Override
    public String defeat(Combat c, Result flag) {
        return "As you pleasure and caress Maya's body, she lets out a lovely gasp. You've "
                        + "finally got her on the ropes, and she knows it. Despite her experience, "
                        + "her body is still subject to her sexual needs. She grasps for your dick "
                        + "in desperation, but you manage to ward off her silk-gloved hand. Her body "
                        + "is rocked by a shudder of ecstasy and she lets out an orgasmic cry.<p>You"
                        + " did it! You actually managed to beat Maya! She sits on the floor, staring"
                        + " at you in shock as she recovers from her climax. <i>\"Oh my. A freshman "
                        + "actually made me orgasm. Either I was very careless, or you're no ordinary"
                        + " freshman.\"</i> She smiles approvingly. <i>\"Probably some of both. I'll "
                        + "see that Lilly gives you a nice bonus.\"</i> She gently pulls you to the "
                        + "floor next to her. <i>\"I think you also deserve a more immediate reward."
                        + "\"</i><p>She climbs on top of you and presses her soft boobs against your "
                        + "cock. The head of your penis pokes out of her cleavage and she covers it "
                        + "with her mouth. Oh God. This feels amazing. Her breasts thoroughly massage"
                        + " your shaft and her skilled tongue teases your sensitive glans. You writhe"
                        + " in exquisite pleasure. You won't last long against her technique, but you"
                        + " want to enjoy it as long as possible. She sucks hard on your dick and you"
                        + " can't resist cumming in her mouth.<p>She swallows your semen as gracefully"
                        + " as she can and smiles at you. <i>\"I'm pleased to see you do enjoy my "
                        + "efforts. I was worried I might be losing my touch.\"</i>";

    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "<i>\"Oh my. I don't think the boy had much of a chance to start with,"
                            + " but now the other girls are picking on him too.\"</i> She looks over "
                            + "your helpless body and sides of her mouth curl slightly with amusement."
                            + " She delicately extends her stocking-covered foot and teases the tip "
                            + "of your penis.<br><i>\"I think you'll enjoy this bullying, at least.\""
                            + "</i> She rubs and squeezes your sensitive glans with her toes. Your "
                            + "hips buck involuntarily as she keeps moving her foot to stimulate "
                            + "different spots on your shaft. How can she give you this kind of "
                            + "pleasure with just one foot? You knew she was experienced, but this"
                            + " is insane! She steps down on your dick, grinding it with the sole of "
                            + "her foot. You can't hold on any more. Your cock throbs and you shoot "
                            + "your seed onto her foot.";
        }
        if (assist.eligible(character)) {
            assist.defeated(character);
        }
        return "Maya looks a bit surprised as you restrain her opponent. <i>\"You're going to "
                        + "side with me? I appreciate the thought.\"</i> She kneels down in front of " + target.name()
                        + " and kisses her softly while staring deeply into her eyes. "
                        + "She turns her attention toward you and leans close to whisper in your"
                        + " ear. <i>\"But I don't need any help\"</i> "
                        + "You don't notice her hand snake between your legs until she suddenly "
                        + "gives your balls a painful flick. The shock and surprise makes you lose " + "your grip on "
                        + target.name() + ", who twists out of your arms and pins you to the floor. Suddenly you're"
                        + " the one being double-teamed. Her eyes have a certain glazed over "
                        + "appearance that tells you " + "she's under Maya's influence.<p>"
                        + "Maya casually moves between your legs, where she can easily reach your " + "dick and "
                        + target.name() + "'s slit. You groan despite yourself as she" + " starts to stroke your shaft "
                        + "with both hands. Her mouth goes to work on the other girl's pussy. "
                        + "The two of you writhe in pleasure, completely at the mercy of the " + "veteran sexfighter. "
                        + target.name() + " kisses you passionately as you both cum simultaneously.";

    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "In the middle of your fight with " + assist.name()
                            + ", you feel an overwhelming presence approach from behind. "
                            + "She apparently senses it too, because you both stop and turn "
                            + "to see Maya confidently walking towards you. She smiles and looks "
                            + "at the two of you. <i>\"Eenie... Meenie... Minie....\"</i> Her "
                            + "finger flicks in your direction. <i>\"Mo.\"</i><br>"
                            + "You take a step back and prepare to defend yourself, but she's "
                            + "too fast. She reaches you in an instant and sweeps your feet"
                            + " out from under you, depositing you on the floor. She "
                            + "pins your arms helplessly under her legs and sits casually on "
                            + "your chest. She's lighter than she looks, but you're still "
                            + "completely unable to move her. <i>\"Sorry, it's just your "
                            + "bad luck. I could have helped you instead, but I don't do this v"
                            + "ery often anymore and I wanted to watch a cute boy orgasm. You "
                            + "should try to enjoy your defeat, it's just another " + "part of the Game.\"</i><br>";
        }
        return "In the middle of your fight with " + target.name()
                        + ", you feel an overwhelming presence approach from behind. She apparently "
                        + "senses it too, because you both stop and turn "
                        + "to see Maya confidently walking towards you. She smiles and looks at"
                        + " the two of you. <i>\"Eenie... Meenie... Minie....\"</i> She points toward " + target.name()
                        + ". <i>\"Mo.\"</i><br>" + "Before the girl can react, Maya is on her and "
                        + "kisses her passionately. " + target.name()
                        + " struggles for just a moment before going limp. Maya moves to licking and " + "sucking "
                        + "her neck, while she eases the helpless girl to the floor. "
                        + "She glances back at you and you feel a chill run down your spine "
                        + "as her eyes seem to bore into you. <i>\"Are you "
                        + "going to make me do all the work? There's a lovely, naked girl in "
                        + "front of you who needs a good licking. Don't keep her waiting.</i><br>";
    }

    @Override
    public String describe(Combat c) {
        return "Maya is a beautiful young woman in her early twenties, though she carries"
                        + " herself with the grace of a more mature lady. She has soft, shapely "
                        + "breasts, larger than her slim frame would imply. Her waist length, raven "
                        + "black hair is tied in a single braid down her back. She wears elbow-length"
                        + " silk gloves, giving the appearance of sensual elegance. Her eyes are a "
                        + "beautiful dark blue, behind her red-framed glasses, but every so often the "
                        + "light catches them in a way that makes you think there might "
                        + "be something dangerous inside.";
    }

    @Override
    public String draw(Combat c, Result flag) {
        Character target = c.getOpponent(character);
        if (target.human()) {
            Global.flag(Flag.Clue1);
        }
        return "You've got her, you're sure of it. Maya let her guard down long enough "
                        + "for you to get her exactly where you want her. She falls to her hands"
                        + " and knees, her defenseless rear facing you. It's risky to attempt an "
                        + "insertion against someone of Maya's skill, but you can't afford to pass"
                        + " up any advantage you're given. Before she can regain her composure, you "
                        + "thrust your cock into her wet pussy. She lets out a yelp in surprise"
                        + " and pleasure, while you fuck her relentlessly. If she get free before"
                        + " you can make her cum, you're probably screwed, but you can feel that "
                        + "she's close to the end. Your cock warns you that you're approaching your"
                        + " limit and her dark aura is gradually eating away at your willpower. "
                        + "You need to end this right now! You reach down under her waist and quickly "
                        + "locate her love button. She moans in pleasure as you simultaneously "
                        + "stimulate her pussy and clit. It's working! She's already trembling on "
                        + "the verge of orgasm.<p>In a remarkable display of strength and desperation,"
                        + " she manages to wrap her legs around your hips, holding you completely "
                        + "inside her. You're caught completely off guard when her vaginal walls "
                        + "squeeze and milk your throbbing cock. She's cumming! If you can just hold"
                        + " out for a few more seconds, you'll win, but this sensation is just too "
                        + "much! You spurt hot jets of semen into Maya's spasming womb. It's a draw, "
                        + "snatched from the jaws of victory.<p>You both collapse on the floor, "
                        + "exhausted. You hear a lovely giggle, almost melodic, coming from Maya. Her"
                        + " giggles turn into unrestrained laughter as she rolls onto her back."
                        + " <i>\"A draw!\"</i> Her mature air of dignity is gone as she shakes with"
                        + " mirth. <i>\"Barely a draw! I had to struggle to achieve a mutual orgasm!"
                        + " Against a rookie, no less! God, I haven't felt this human in years!\""
                        + "</i> She sits up, more breathless from her laughter than the sex.<p><i>"
                        + "\"Sorry. It probably sounds like I'm disparaging you, but I thought there"
                        + " was only one man who could do that to me anymore.\"</i> She wears a "
                        + "delighted smile, neither dominant nor elegant. <i>\"I think I owe you "
                        + "quite a reward, but it appears your carnal desires are already sated. I "
                        + "know, I'll tell you a secret that Tyler would probably charge you a "
                        + "fortune for.\"</i><p>She looks at you intently, her eyes shining. "
                        + "<i>\"Let's start with a trivia question: in all the years these Games "
                        + "have been going on, how many competitors do you think have gotten "
                        + "seriously hurt?\"</i> This is a bit of an unfair question since you"
                        + " don't know how long the Games have been running. There can't have been "
                        + "too many, or it would have attracted attention. The matches are pretty "
                        + "rough and the physical combat seems to escalate over time. Probably 5-10?"
                        + "<p><i>\"Wrong. The answer is zero. No one has ever been injured in a match."
                        + " It's not just due to Lilly's hardwork, as diligent as she and her "
                        + "predecessors have been. It's because we're under our Benefactor's "
                        + "protection.\"</i> Her expression has shifted slightly as she talks. "
                        + "It now appears more like hero worship, along with something else "
                        + "you can't quite identify. <i>\"There's pain, of course. If we girls"
                        + " couldn't take advantage of male vulnerabilities, it would interfere "
                        + "with match balance. Yet, no matter how hard you get hit, you'll be back"
                        + " on your feet in no time, with no long term effects.\"</i><p>Her smile"
                        + " turns wistful as she stares off into space. Her voice drops almost to"
                        + " a whisper. <i>\"Our Benefactor is kind. Maybe too kind. He cannot bear"
                        + " to see any of his chosen hurt. That's why he couldn't let me die.\"</i>"
                        + " You suddenly realize what she reminds you of. She looks and sounds like"
                        + " a nun at prayer. She turns her attention back to you. <i>\"I think that"
                        + " was more than one secret. I could just erase that last bit from your memory,"
                        + " but I'll just call it a bonus.\"</i>";
    }

    @Override
    public boolean fightFlight(Character opponent) {
        return fit();
    }

    @Override
    public boolean attack(Character opponent) {
        return true;
    }

    @Override
    public String startBattle(Character other) {
        if (other.human()) {
            return "Maya smiles softly as she confidently steps toward you. <i>\"Are you simply unfortunate or were you actually hoping to challenge me? What a brave boy. I'll try not to disappoint you.\"</i>";
        } else {
            return Global.format("{self:SUBJECT} smiles softly as she confidently steps towards {other:name-do}.", character, other);
        }
    }

    @Override
    public boolean fit() {
        return !character.outfit.isNude();
    }

    @Override
    public String night() {
        return "";
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case horny:
                return value >= 50;
            case confident:
            case desperate:
                return value >= 200;
            default:
                break;
        }
        return value >= 100;
    }

    @Override
    public String temptLiner(Combat c, Character opponent) {
        return "Maya lowers her voice to a smokey tone as she speaks. <i>\"Shall"
                        + " I show you what experience can do for a sexfighter?\"</i>";
    }

    @Override
    public String orgasmLiner(Combat c) {
        return "<i>\"Oh.. SHIT! Did I just actually... Fuck! Come here and let me" + " return the favor, stud!\"</i>";
    }

    @Override
    public String makeOrgasmLiner(Combat c) {
        return "<i>\"Aaaand there we are. Think you can go again?\"</i>";
    }

    @Override
    public String resist3p(Combat c, Character assist, Character target) {
        if (assist.human()) {
            return "As you attempt to hold off Maya, you see " + target.name()
                            + " stealthily approaching behind her. Looks like you're about to get "
                            + "some help. Maya suddenly "
                            + "winks at you and her foot darts up almost playfully between your legs."
                            + " She barely seemed to use any force, but the impact is still staggering. "
                            + "You crumple to the floor, holding your balls in debilitating pain.<p>" + target.name()
                            + " hesitates, realizing she doesn't have the advantage of superior numbers. Maya is already moving, though, and pounces on the other girl "
                            + "before she can escape. From your position on the floor, you can't see exactly what is happening, but it's clear Maya is overwhelming her. As soon as the "
                            + "pain subsides, you force yourself back to your feet. You'd hoped to team up with "
                            + target.name() + ", but she is already trembling in orgasm under Maya's "
                            + "fingers. The older girl returns her attention to you and smiles. <i>\"Sorry about the interruption. Where were we?\"</i>";
        }
        return assist.name()
                        + " isn't likely to be able to hold off Maya on her own. Your best bet is to work together to take her down. You creep up behind her, with Maya "
                        + "showing no sign of noticing you. When you've gotten close enough, you lunge toward her, hoping to catch her from behind. To your surprise, she vanishes the "
                        + "moment you touch her and you stumble forward into " + assist.name()
                        + ". You turn around and see Maya standing a couple feet away. You've lost the element of "
                        + "surprise (you probably never had it), but it's still 2 on 1.<br>" + "You suddenly feel "
                        + assist.name()
                        + " grab you from behind. You turn your head and notice her eyes are dull and unfocused. Maya must have hypnotized her to "
                        + "help trap you. Maya speaks up in a melodic voice. <i>\"How rude of you to interrupt a perfectly enjoyable fight. Naughty boys should be punished.\"</i> She strips off your clothes and "
                        + "runs her fingers over your exposed dick. You immediately grow hard under her touch. She's too skilled with her hands for you to hold back and you're completely "
                        + "unable to defend yourself. She makes you cum embarrassingly quickly and both girls discard you unceremoniously to the floor. Maya snaps her fingers in front of "
                        + assist.name()
                        + "'s face to bring her out of the trance and the girl looks down at your defeated form in confusion. <i>\"Thank you for your cooperation. Now we can "
                        + "continue our fight without interruption.\"</i>";
    }

}
