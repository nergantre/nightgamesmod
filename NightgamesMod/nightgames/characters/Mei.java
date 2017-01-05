package nightgames.characters;

import java.util.Collection;
import java.util.Optional;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.custom.CharacterLine;
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
        constructLines();
    }

    @Override
    public void applyStrategy(NPC self) {}

    @Override
    public void applyBasicStats(Character self) {
        preferredCockMod = CockMod.error;
        character.outfitPlan.add(Clothing.getByID("negligee"));
        character.outfitPlan.add(Clothing.getByID("lacythong"));
        character.outfitPlan.add(Clothing.getByID("garters"));

        character.change();
        character.modAttributeDontSaveData(Attribute.Power, 1);
        character.modAttributeDontSaveData(Attribute.Seduction, 1);
        character.modAttributeDontSaveData(Attribute.Cunning, 1);
        character.modAttributeDontSaveData(Attribute.Perception, 1);
        character.modAttributeDontSaveData(Attribute.Speed, 1);
        character.getStamina().setMax(100);
        character.getArousal().setMax(150);
        character.rank = 1;
        Global.gainSkills(character);

        character.getMojo().setMax(110);

        character.setTrophy(Item.ExtremeAphrodisiac);
        character.body.add(BreastsPart.b);
        character.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 3;
        character.getGrowth().arousal = 5;
        character.getGrowth().willpower = .8f;
        character.getGrowth().bonusStamina = 2;
        character.getGrowth().bonusArousal = 2;

        character.getGrowth().addTrait(0, Trait.hairtrigger);
        character.getGrowth().addTrait(0, Trait.petite);
        character.getGrowth().addTrait(10, Trait.cute);
        character.getGrowth().addTrait(15, Trait.lacedjuices);
        character.getGrowth().addTrait(20, Trait.tight);
        character.getGrowth().addTrait(25, Trait.sexTraining1);
        character.getGrowth().addBodyPart(30, PussyPart.succubus);
        character.getGrowth().addBodyPart(30, TailPart.demonic);
        character.getGrowth().addBodyPart(30, WingsPart.fallenangel);
        character.getGrowth().addTrait(30, Trait.fallenAngel);
        character.getGrowth().addTrait(35, Trait.energydrain);
        character.getGrowth().addTrait(40, Trait.soulsucker);
        character.getGrowth().addTrait(45, Trait.gluttony);
        character.getGrowth().addTrait(50, Trait.vaginaltongue);
        character.getGrowth().addTrait(55, Trait.carnalvirtuoso);
        preferredAttributes.add(c -> Optional.of(Attribute.Seduction));

        preferredAttributes.add(c -> c.getLevel() >= 30 ? Optional.of(Attribute.Dark) : Optional.empty());
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
    
    private void constructLines() {
        character.addLine(CharacterLine.BB_LINER, (c, self, other) -> {
            return "<i>They taught that one in self-defense class!</i>";
       });

        character.addLine(CharacterLine.NAKED_LINER, (c, self, other) -> {
            return "While covering herself with her arms, Mei fake-screams <i>What do you think you're doing!?</i> Her lewd smile however speaks volumes about her true thoughts.";
       });

        character.addLine(CharacterLine.STUNNED_LINER, (c, self, other) -> {
            return "<i>Angel... Sorry...</i>";
       });

        character.addLine(CharacterLine.TAUNT_LINER, (c, self, other) -> {
            return "<i>\"That's right, you're just a little " + other.boyOrGirl() + "toy for us. So why don't you just stay still?\"</i>";
       });

        character.addLine(CharacterLine.TEMPT_LINER, (c, self, other) -> {
            return "Mei runs her hands all over her body while teasing you, <i>\"Mmmm you want some of this? Just ask and we'll do as you please.\"</i>";
       });

        character.addLine(CharacterLine.ORGASM_LINER, (c, self, other) -> {
            return "Mei yelps as she cums <i>\"Oh fuuuuckk!\"</i>";
       });

        character.addLine(CharacterLine.MAKE_ORGASM_LINER, (c, self, other) -> {
            return "<i>Try a little harder wont you? At this rate there's no way you'll be suitable for her!</i>";
       });
        
       character.addLine(CharacterLine.CHALLENGE, (c, self, other) -> {
           int meiFought = other.getFlag(FOUGHT_MEI_PET);
           if (other.human()) {
               if (meiFought == 0)  {
                   other.setFlag(FOUGHT_MEI_PET, 1);
                   return "Standing up in the fading light, {self:SUBJECT} looks around bewilderedly before catching sight of you and Angel. "
                                   + "{self:SUBJECT} waves happily at {other:name-do} <i>\"Hiya {other:name}, fancy meeting you here! Huh are we on campus? "
                                   + "I seem to be half naked and Angel has wings? Wait what's going on?\"</i> "
                                   + "<br/><br/>"
                                   + "When neither you nor Angel deigned to respond, she just shrugs <i>\"Ahhhh, I get it! This must be one of those sexy dreams right? "
                                   + "I wonder if I'm just pent up... ah well, no point in minding the details.\"<i/> Mei cracks her fingers. <i>\"Since we're doing this, I'm going all out!\"</i>"
                                   + "<br/><br/>"
                                   + "Errr... while you're glad she's so adaptable, it looks like the fight's become a two on one!";
               } else if (self.has(Trait.fallenAngel) && meiFought == 1) {
                   other.setFlag(FOUGHT_MEI_PET, 2);
                   return "After {self:SUBJECT} materializes as usual, she notices that her body has changed. "
                                   + "Pitch black feathered wings grow out of her shoulder blades, and a thick demonic tail sprouts from her bum. "
                                   + "With her eyes wide, Mei exclaims <i>\"Oh wow, what am I supposed to be now? Some kind of fallen angel? "
                                   + "I knew I've been reading too much fantasy smut before bed...\"</i>";
               } else if (self.has(Trait.fallenAngel)) {
                   return "{self:SUBJECT} opens her eyes and stretches her black wings. "
                                   + "<i>This dream again? Well, it got pretty hot last time, so no complaints from me! So cum again for me will you? "
                                   + "My little demon pussy seems pretty hungry!</i>";
               } else {
                   return "{self:SUBJECT} opens her eyes and takes in the situation. "
                                   + "<i>This dream again? Well, it got pretty hot last time, so no complaints from me!</i>";
               }
           }
           return "{self:SUBJECT} curiously scans the situation and with an approving look from Angel, she gets ready to attack!</i>";
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

    private static String FOUGHT_MEI_PET = "FOUGHT_MEI_PET";

    @Override
    public boolean fit() {
        return true;
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
}