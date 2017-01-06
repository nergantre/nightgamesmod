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

public class Sarah extends BasePersonality {
    private static final long serialVersionUID = 8601852023164119671L;

    public Sarah() {
        this(Optional.empty(), Optional.empty());
    }

    public Sarah(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Sarah", 1, charConfig, commonConfig, false);
        constructLines();
    }

    @Override
    public void applyStrategy(NPC self) {}

    @Override
    public void applyBasicStats(Character self) {
        preferredCockMod = CockMod.error;
        character.outfitPlan.add(Clothing.getByID("frillybra"));
        character.outfitPlan.add(Clothing.getByID("frillypanties"));

        character.change();
        character.modAttributeDontSaveData(Attribute.Power, 2);
        character.modAttributeDontSaveData(Attribute.Cunning, 1);
        character.modAttributeDontSaveData(Attribute.Perception, 1);
        character.modAttributeDontSaveData(Attribute.Speed, 2);
        character.getStamina().setMax(150);
        character.getArousal().setMax(100);
        character.rank = 1;
        Global.gainSkills(character);

        character.getMojo().setMax(90);

        character.setTrophy(Item.HolyWater);
        character.body.add(BreastsPart.d);
        character.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 5;
        character.getGrowth().arousal = 3;
        character.getGrowth().willpower = .8f;
        character.getGrowth().bonusStamina = 2;
        character.getGrowth().bonusArousal = 2;

        character.getGrowth().addTrait(0, Trait.imagination);
        character.getGrowth().addTrait(0, Trait.pimphand);
        character.getGrowth().addTrait(10, Trait.QuickRecovery);
        character.getGrowth().addTrait(15, Trait.sadist);
        character.getGrowth().addTrait(20, Trait.disablingblows);
        character.getGrowth().addTrait(25, Trait.nimbletoes);
        character.getGrowth().addBodyPart(30, PussyPart.fiery);
        character.getGrowth().addBodyPart(30, WingsPart.angelic);
        character.getGrowth().addTrait(30, Trait.valkyrie);
        character.getGrowth().addTrait(35, Trait.overwhelmingPresence);
        character.getGrowth().addTrait(40, Trait.bitingwords);
        character.getGrowth().addTrait(45, Trait.commandingvoice);
        character.getGrowth().addTrait(50, Trait.oblivious);
        character.getGrowth().addTrait(55, Trait.resurrection);

        preferredAttributes.add(c -> Optional.of(Attribute.Power));
        preferredAttributes.add(c -> c.getLevel() >= 30 ? Optional.of(Attribute.Ki) : Optional.empty());
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
            return "<i>\"...\"</i> Sarah silently looks at you, with no hint of remorse in her eyes.";
        });

        character.addLine(CharacterLine.NAKED_LINER, (c, self, other) -> {
            return "Sarah looks unfazed at being undressed, but you can clearly see a flush creeping into her face.";
        });

        character.addLine(CharacterLine.STUNNED_LINER, (c, self, other) -> {
            return "<i>\"..!\"</i>";
        });

        character.addLine(CharacterLine.TAUNT_LINER, (c, self, other) -> {
            return "Sarah simply eyes you with a disdainful look. If looks could kill... well this still probably wouldn't kill you. But it definitely hurts your pride.";
        });

        character.addLine(CharacterLine.TEMPT_LINER, (c, self, other) -> {
            return "Sarah cups her large breasts and gives you a show. The gap between her placid face and her lewd actions is surprisingly arousing.";
        });

        character.addLine(CharacterLine.NIGHT_LINER, (c, self, other) -> {
            return "";
        });

        character.addLine(CharacterLine.ORGASM_LINER, (c, self, other) -> {
            return "Sarah's eyes slam shut in a blissful silent orgasm. "
                            + "You can clearly tell she's turned on like hell, but her face remains impassive as usual.";
        });

        character.addLine(CharacterLine.MAKE_ORGASM_LINER, (c, self, other) -> {
            return "Sarah looks a bit flushed as {other:subject-action:cum|cums} hard. However she does changes neither her blank demeanor nor her stance.";
        });
        
        character.addLine(CharacterLine.CHALLENGE, (c, self, other) -> {
            int sarahFought = other.getFlag(FOUGHT_SARAH_PET);
            if (other.human()) {
                if (sarahFought == 0)  {
                    other.setFlag(FOUGHT_SARAH_PET, 1);
                    return "The summoned figure stands up while wobbling on her feet. When she lifts her head, you see that it's Angel's friend Sarah! "
                                    + "<br/>On closer inspection though, you see that the easily-embarassed glasses girl seems to have a completely different air about her. "
                                    + "To tell the truth, She looks rather glassy eyed and unstable. "
                                    + "<br/><br/>You look questioningly at Angel and she sighs "
                                    + "<i>\"Sarah is actually a bit too shy to bring into the games, even unconsciously. "
                                    + "Instead of having her freak out, I thought I'd just have her mind come for the ride. Don't worry, I guarantee that she'll enjoy it.\"</i>";
                } else if (self.has(Trait.valkyrie) && sarahFought == 1) {
                    other.setFlag(FOUGHT_SARAH_PET, 2);
                    return "After {self:SUBJECT} materializes as usual from a brillant burst of light, you see that she looks different. "
                                    + "Sarah was always rather tall, but now she looks positively Amazonian. "
                                    + "Subtle but powerful muscles are barely visible under her fleshy body, making her presence larger than ever. "
                                    + "To top it off, a pair of large angelic wings crown her upper back, completing her look as a valkyrie in service to her Goddess. "
                                    + "<br/><br/>"
                                    + "Angel smiles mischievously at you <i>\"Isn't she beautiful? I love Sarah the way she is, but in a fight, a Goddess does need her guardians.\"</i>";
                } else if (self.has(Trait.valkyrie)) {
                    return "{self:SUBJECT} emerges from the pillar of light and stands at attention. "
                                    + "Angel walks over to {self:direct-object} and kisses her on the cheek. <i>\"Sarah dear, let's teach {other:direct-object} the proper way to worship a Goddess.\"<i>";
                } else {
                    return "{self:SUBJECT} opens her glassy eyes and stands silently while Angel coos, <i>\"Mmmm we're going to show you a good time.\"</i>";
                }
            }
            return "{self:SUBJECT} impassively scans the situation and with an approving look from Angel, she gets ready to attack.</i>";
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

    private static String FOUGHT_SARAH_PET = "FOUGHT_SARAH_PET";

    @Override
    public boolean fit() {
        return true;
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case angry:
                return value >= 80;
            default:
                return value >= 100;
        }
    }

}