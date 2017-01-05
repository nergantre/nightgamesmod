package nightgames.characters;

import java.util.Collection;
import java.util.Optional;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.custom.CharacterLine;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.start.NpcConfiguration;

public class Caroline extends BasePersonality {
    private static final long serialVersionUID = 8601852023164119671L;

    public Caroline() {
        this(Optional.empty(), Optional.empty());
    }

    public Caroline(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Caroline", 1, charConfig, commonConfig, false);
        constructLines();
    }

    @Override
    public void applyStrategy(NPC self) {}

    @Override
    public void applyBasicStats(Character self) {
        preferredCockMod = CockMod.error;
        character.outfitPlan.add(Clothing.getByID("lacybra"));
        character.outfitPlan.add(Clothing.getByID("lacepanties"));
        character.outfitPlan.add(Clothing.getByID("stockings"));

        character.change();
        character.modAttributeDontSaveData(Attribute.Seduction, 1);
        character.modAttributeDontSaveData(Attribute.Cunning, 2);
        character.modAttributeDontSaveData(Attribute.Perception, 1);
        character.modAttributeDontSaveData(Attribute.Speed, 1);
        character.getStamina().setMax(120);
        character.getArousal().setMax(120);
        character.rank = 1;
        Global.gainSkills(character);

        character.getMojo().setMax(110);

        character.setTrophy(Item.ExtremeAphrodisiac);
        character.body.add(BreastsPart.b);
        character.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 4;
        character.getGrowth().arousal = 4;
        character.getGrowth().willpower = .4f;
        character.getGrowth().bonusStamina = 2;
        character.getGrowth().bonusArousal = 2;

        character.getGrowth().addTrait(0, Trait.ticklish);
        character.getGrowth().addTrait(0, Trait.dexterous);
        character.getGrowth().addTrait(10, Trait.limbTraining1);
        character.getGrowth().addTrait(10, Trait.tongueTraining1);
        character.getGrowth().addTrait(15, Trait.healer);
        character.getGrowth().addTrait(20, Trait.romantic);
        character.getGrowth().addTrait(25, Trait.hawkeye);
        character.getGrowth().addBodyPart(30, PussyPart.arcane);
        character.getGrowth().addBodyPart(30, WingsPart.ethereal);
        character.getGrowth().addTrait(30, Trait.kabbalah);
        character.getGrowth().addTrait(35, Trait.protective);
        character.getGrowth().addTrait(40, Trait.magicEyeFrenzy);
        character.getGrowth().addTrait(45, Trait.supplicant);
        character.getGrowth().addTrait(50, Trait.magicEyeTrance);
        character.getGrowth().addTrait(55, Trait.beguilingbreasts);

        preferredAttributes.add(c -> Optional.of(Attribute.Cunning));
        preferredAttributes.add(c -> c.getLevel() >= 30 ? Optional.of(Attribute.Arcane) : Optional.empty());
        // mostly feminine face, cute but not quite at Angel's level
        character.body.add(new FacePart(.1, 2.9));
    }

    @Override
    public Action move(Collection<Action> available, Collection<Movement> radar) {
        Action proposed = Decider.parseMoves(available, radar, character);
        return proposed;
    }

    @Override
    public void rest(int time) {}

    @Override
    public String describe(Combat c, Character self) {
        return "";
    }

    @Override
    public String victory(Combat c, Result flag) {
        return "";
    }

    @Override
    public String defeat(Combat c, Result flag) {
        return "";
    }

    @Override
    public String draw(Combat c, Result flag) {
        return "";
    }

    private static String FOUGHT_CAROLINE_PET = "FOUGHT_CAROLINE_PET";
    private void constructLines() {
        character.addLine(CharacterLine.BB_LINER, (c, self, other) -> "Caroline seems all business even after brutalizing {other:name-possessive} genitals <i>\"Don't worry, I don't think the damage is permanent... I hope.\"</i>");
        character.addLine(CharacterLine.NAKED_LINER, (c, self, other) -> "Caroline doesn't even flinch after being stripped. <i>\"You know, you get used to these things after being friends with Angel this long.\"</i>");
        character.addLine(CharacterLine.STUNNED_LINER, (c, self, other) -> "Caroline staggers as she falls <i>\"You don't go easy do you...\"</i>");
        character.addLine(CharacterLine.TAUNT_LINER, (c, self, other) -> "<i>\"Come on, put in some more effort. You'll just be another notch on our bedpost at this rate.\"</i>");
        character.addLine(CharacterLine.TEMPT_LINER, (c, self, other) -> "Caroline turns around and spreads her lower lips with her fingers, <i>\"Mmm, I may not be as good as Angel, but I'm confident you wont last 10 seconds in me. Want to give it a go?\"</i>");
        character.addLine(CharacterLine.ORGASM_LINER, (c, self, other) -> "Caroline groans as she love juices drips endlessly between her legs <i>\"You're pretty good...\"</i>");
        character.addLine(CharacterLine.MAKE_ORGASM_LINER, (c, self, other) -> "<i>\"Come on, come on! Let's go for another round!\"</i>");
        character.addLine(CharacterLine.CHALLENGE, (c, self, other) -> {
            int carolineFought = other.getFlag(FOUGHT_CAROLINE_PET);
            if (other.human()) {
                if (carolineFought == 0)  {
                    other.setFlag(FOUGHT_CAROLINE_PET, 1);
                    return "You see runic circles twist and rotate in front of Angel, summoning a humanoid figure into the fight. "
                                    + "With a loud bang, you are thrown on your ass as the circles collapse inwards, wrapping themselves around the newly formed body. "
                                    + "Cautiously you pick yourself off the ground and check out the intruder. Oh shit. "
                                    + "You'd recognize that bob cut and competitive look anywhere. "
                                    + "It's Angel's friend Caroline!"
                                    + "<br/><br/>"
                                    + "Caroline looks around and spots you and Angel. <i>\"Hmmm I'm not entirely sure what's going on here, "
                                    + "but looks like some kind of sex fight? Sounds fun, I'm in!\"</i> You groan. Well she sure is adaptable...";
                } else if (self.has(Trait.kabbalah) && carolineFought == 1) {
                    other.setFlag(FOUGHT_CAROLINE_PET, 2);
                    return "Caroline emerges again from the runic circles you're used to seeing by now. However, she looks a bit different. "
                                    + "Angel must have shared some more of her divine power with her in thet summoning since Caroline now sports translucent "
                                    + "ethereal-looking wings between her shoulder blades and runic tattoos all over her body. "
                                    + "Moreover, she is holding a heavy tome in her hands that you've never seen before. "
                                    + "<br/><br/>"
                                    + "Caroline seems a bit surprised too. She inspects herself for a moment and tries tracing something in front of herself. "
                                    + "From her fingertips a glowing pattern emerges from thin air. Caroline smiles and says <i>\"This is way cool. "
                                    + "I wonder what else I can do?\"</i>";
                } else if (self.has(Trait.kabbalah)) {
                    return "{self:SUBJECT} emerges from the runic portal and unfurls her ethereal wings. "
                                    + "<i>\"Hmmm I can't seem to remember any of this during the day time, but I'm having so much fun I can't really complain. Ready {other:name}?\"</i>";
                } else {
                    return "{self:SUBJECT} opens her eyes and takes in the situation. Oooh a rematch? I'm game!</i>";
                }
            }
            return Global.format("{self:SUBJECT} quickly scans the situation and with an approving look from Angel, she gets ready to attack!</i>", self, other);
        });
    }

    @Override
    public boolean fightFlight(Character opponent) {
        return true;
    }

    @Override
    public boolean attack(Character opponent) {
        return true;
    }

    public double dickPreference() {
        return 0;
    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        return "";
    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        return "";
    }

    @Override
    public boolean fit() {
        return true;
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            default:
                return value >= 100;
        }
    }
}