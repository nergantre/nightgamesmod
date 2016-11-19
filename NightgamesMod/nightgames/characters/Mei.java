package nightgames.characters;

import java.util.HashSet;
import java.util.Optional;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.start.NpcConfiguration;

public class Mei extends BasePersonality {
    private static final long serialVersionUID = 8601852023164119671L;

    public Mei() {
        this(Optional.empty(), Optional.empty());
    }

    public Mei(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Mei", 1, charConfig, commonConfig, false);
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
        character.modAttributeDontSaveData(Attribute.Power, 1);
        character.modAttributeDontSaveData(Attribute.Seduction, 1);
        character.modAttributeDontSaveData(Attribute.Cunning, 1);
        character.modAttributeDontSaveData(Attribute.Perception, 1);
        character.modAttributeDontSaveData(Attribute.Speed, 1);
        character.getStamina().setMax(70);
        character.getArousal().setMax(70);
        character.rank = 1;
        Global.gainSkills(character);

        character.getMojo().setMax(110);

        character.setTrophy(Item.ExtremeAphrodisiac);
        character.body.add(BreastsPart.b);
        character.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 2;
        character.getGrowth().arousal = 2;
        character.getGrowth().willpower = .4f;
        character.getGrowth().bonusStamina = 2;
        character.getGrowth().bonusArousal = 2;

        character.getGrowth().addTrait(0, Trait.hairtrigger);
        character.getGrowth().addTrait(0, Trait.petite);
        character.getGrowth().addTrait(10, Trait.cute);
        character.getGrowth().addTrait(15, Trait.dexterous);
        character.getGrowth().addTrait(20, Trait.tight);
        character.getGrowth().addTrait(25, Trait.sexTraining1);
        character.getGrowth().addBodyPart(30, PussyPart.succubus);
        character.getGrowth().addBodyPart(30, TailPart.demonic);
        character.getGrowth().addBodyPart(30, WingsPart.fallenangel);
        character.getGrowth().addTrait(35, Trait.fallenAngel);
        character.getGrowth().addTrait(40, Trait.energydrain);
        character.getGrowth().addTrait(45, Trait.soulsucker);
        character.getGrowth().addTrait(50, Trait.lacedjuices);
        character.getGrowth().addTrait(55, Trait.vaginaltongue);
        character.getGrowth().addTrait(60, Trait.carnalvirtuoso);
        preferredAttributes.add(c -> c.getLevel() >= 30 ? Optional.of(Attribute.Dark) : Optional.empty());
        // mostly feminine face, cute but not quite at Angel's level
        character.body.add(new FacePart(.1, 2.9));
    }

    @Override
    public Action move(HashSet<Action> available, HashSet<Movement> radar) {
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

    @Override
    public String bbLiner(Combat c, Character other) {
        return "<i>They taught that one in self-defense class!</i>";
    }

    @Override
    public String nakedLiner(Combat c, Character opponent) {
        return "While covering herself with her arms, Mei fake-screams <i>What do you think you're doing!?</i>";
    }

    @Override
    public String stunLiner(Combat c, Character opponent) {
        return "<i>Angel... Sorry...</i>";
    }

    @Override
    public String taunt(Combat c, Character opponent) {
        return "<i>Mmm, that's right you're just a little " + opponent.boyOrGirl() + "toy for us. So why don't you just let us do our thing?</i>";
    }

    @Override
    public String temptLiner(Combat c, Character opponent) {
        return "Mei runs her hands all over her body while teasing you, <i>Mmmm you want some of this? Just ask and we'll do as you please.</i>";
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

    private static String FOUGHT_MEI_PET = "FOUGHT_SARAH_PET";
    @Override
    public String startBattle(Character self, Character other) {
        int meiFought = other.getFlag(FOUGHT_MEI_PET);
        if (other.human()) {
            if (meiFought == 0)  {
                other.setFlag(FOUGHT_MEI_PET, 1);
                return Global.format("{self:SUBJECT} waves happily at {other:name-do} <i>Hiya {other:name}, fancy meeting you here! "
                                + "This is a pretty absurd dream, Angel's a goddess and we're sex fighting on campus... I wonder if I'm just pent up?"
                                + "Oh well, no point minding it! Since we're doing this, I'm going all out!</i>", self, other);
            } else if (self.has(Trait.fallenAngel) && meiFought == 1) {
                other.setFlag(FOUGHT_MEI_PET, 2);
                return Global.format("After {self:SUBJECT} materializes as usual, she notices that her body has changed. "
                                + "Pitch black feathered wings grow out of her shoulder blades, and a thick demonic tail sprouts from her bum. "
                                + "With her eyes wide, Mei exclaims <i>Oh wow, what am I supposed to be now? Some kind of fallen angel? "
                                + "I knew I've been reading too much fantasy smut before bed...</i>", self, other);
            } else if (self.has(Trait.fallenAngel)) {
                return Global.format("{self:SUBJECT} opens her eyes and stretches her black wings. "
                                + "<i>This dream again? Well, it got pretty hot last time, so no complaints from me! So cum again for me will you? "
                                + "My little demon pussy seems pretty hungry!</i>", self, other);
            } else {
                return Global.format("{self:SUBJECT} opens her eyes and takes in the situation. "
                                + "<i>This dream again? Well, it got pretty hot last time, so no complaints from me!</i>", self, other);
            }
        }
        return Global.format("{self:SUBJECT} curiously scans the situation and with an approving look from Angel, she gets ready to attack!</i>", self, other);
    }

    @Override
    public boolean fit() {
        return true;
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
            default:
                return value >= 100;
        }
    }

    @Override
    public String orgasmLiner(Combat c) {
        return "Mei groans as she cums <i>\"Oh fuuuuckk!\"</i>";
    }

    @Override
    public String makeOrgasmLiner(Combat c, Character target) {
        return "<i>Try a little harder wont you? At this rate there's no way you'll be suitable for her!</i>";
    }
}