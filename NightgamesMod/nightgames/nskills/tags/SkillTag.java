package nightgames.nskills.tags;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Trait;
import nightgames.status.Stsflag;

public abstract class SkillTag {
    public abstract SkillRequirement getRequirements();

    public abstract SkillRequirement getUsableRequirements();

    public abstract String getName();

    public Optional<Attribute> getAttribute() {
        return Optional.empty();
    }
    public Optional<String> getBodyPartType() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        if (other != this && other instanceof SkillTag) {
            return ((SkillTag)other).getName().equals(getName());
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public final static SkillTag behind = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().behind(results.getSelf().getCharacter()), "Behind");
    public final static SkillTag mobile = new BothRequirementSkillTag(
                    (results, value) -> results.getSelf().getCharacter().canAct(), "Mobile");
    public final static SkillTag responsive = new BothRequirementSkillTag(
                    (results, value) -> results.getSelf().getCharacter().canRespond(), "Responsive");
    public final static SkillTag prone = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().prone(results.getSelf().getCharacter()), "Prone");
    public final static SkillTag domPosition = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().dom(results.getSelf().getCharacter()), "DomPosition");
    public final static SkillTag subPosition = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().sub(results.getSelf().getCharacter()), "SubPosition");
    public final static SkillTag oral = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().oral(results.getSelf().getCharacter(), results.getCombat().getOpponent(results.getSelf().getCharacter())), "Oral");
    public final static SkillTag insertion = new NameOnlySkillTag("Insertion");
    public final static SkillTag fucking = new NameOnlySkillTag("Fucking");
    public final static SkillTag anal = new NameOnlySkillTag("Anal");
    public final static SkillTag reachUpperBody = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().reachTop(results.getSelf().getCharacter()),
                    "ReachUpperBody");
    public final static SkillTag reachLowerBody = new BothRequirementSkillTag(
                    (results, value) -> results.getCombat().getStance().reachBottom(results.getSelf().getCharacter()),
                    "ReachLowerBody");
    public final static SkillTag requiresStripped = new BothRequirementSkillTag(
                    (results, value) -> results.getSelf().getCharacter().mostlyNude(), "RequiresStripped");
    public final static SkillTag requiresNude =
                    new BasicSkillTag((results, value) -> results.getSelf().getCharacter().reallyNude(),
                                    SkillRequirement.noRequirement(), "RequiresNude");
    public final static SkillTag nakedBreasts = new BothRequirementSkillTag(
                    (results, value) -> results.getSelf().getCharacter().breastsAvailable(), "NakedBreasts");
    public final static SkillTag nakedCrotch = new BothRequirementSkillTag(
                    (results, value) -> results.getSelf().getCharacter().crotchAvailable(), "NakedCrotch");
    public final static SkillTag foreplay = new NameOnlySkillTag("Foreplay");
    public final static SkillTag defensive = new NameOnlySkillTag("Defensive");
    public final static SkillTag helping = new NameOnlySkillTag("Helping");
    public final static SkillTag buff = new NameOnlySkillTag("Buff");
    public final static SkillTag debuff = new NameOnlySkillTag("Debuff");
    public final static SkillTag physical = new NameOnlySkillTag("Physical");
    public final static SkillTag mental = new NameOnlySkillTag("Mental");
    public final static SkillTag contact = new NameOnlySkillTag("Contact");
    public final static SkillTag cooldown = new CooldownSkillTag();
    public final static SkillTag restricting = new NameOnlySkillTag("Restricting");
    public final static SkillTag suicidal = new NameOnlySkillTag("Suicidal");
    public final static SkillTag failed = new NameOnlySkillTag("Failed");
    public final static SkillTag counter = new NameOnlySkillTag("Counter");
    public final static SkillTag knockdown = new KnockdownSkillTag();
    public final static SkillTag chain = new NameOnlySkillTag("Chain");
    public final static SkillTag bodyAltering = new NameOnlySkillTag("BodyAltering");
    public final static SkillTag permantlyBodyAltering =
                    new BasicSkillTag((results, value) -> !results.getSelf().getCharacter().has(Trait.stableform),
                                    SkillRequirement.noRequirement(), "PermanentlyBodyAltering");
    public final static SkillTag mindAltering =
                    new BasicSkillTag((results, value) -> !results.getSelf().getCharacter().is(Stsflag.cynical),
                                    SkillRequirement.noRequirement(), "MindAltering");
    public final static SkillTag dominant = new NameOnlySkillTag("Dominant");
    public final static SkillTag submissive = new NameOnlySkillTag("Submissive");
    public final static SkillTag permanentEffects = new NameOnlySkillTag("permanentEffects");
    public final static SkillTag usesCock = new BodyPartSkillTag("cock");
    public final static SkillTag usesPussy = new BodyPartSkillTag("pussy");
    public final static SkillTag usesAss = new BodyPartSkillTag("ass");
    public final static SkillTag usesTail = new BodyPartSkillTag("tail");
    public final static SkillTag usesMouth = new BodyPartSkillTag("mouth");
    public final static SkillTag usesHands = new BodyPartSkillTag("hands");
    public final static SkillTag usesFeet = new BodyPartSkillTag("feet");
    public final static SkillTag usesToy = new BodyPartSkillTag("toy");
    public final static SkillTag pleasureSelf = new NameOnlySkillTag("PleasureSelf");
    public final static SkillTag pleasure = new NameOnlySkillTag("Pleasure");
    public final static SkillTag temptation = new NameOnlySkillTag("Temptation");
    public final static SkillTag arouse = new NameOnlySkillTag("Arouse");
    public final static SkillTag hurt = new NameOnlySkillTag("Hurt");
    public final static SkillTag mean = new NameOnlySkillTag("Mean");
    public final static SkillTag weaken = new NameOnlySkillTag("Weaken");
    public final static SkillTag heal = new NameOnlySkillTag("Heal");
    public final static SkillTag calm = new NameOnlySkillTag("Calm");
    public final static SkillTag drain = new NameOnlySkillTag("Drain");
    public final static SkillTag mojoBuilding = new NameOnlySkillTag("MojoBuilding");
    public final static SkillTag mojoCosting = new NameOnlySkillTag("MojoCosting");
    public final static SkillTag stripping = new NameOnlySkillTag("Stripping");
    public final static SkillTag undressing = new NameOnlySkillTag("Undressing");
    public final static SkillTag reclothing = new NameOnlySkillTag("Reclothing");
    public final static SkillTag perfectAccuracy = new NameOnlySkillTag("PerfectAccuracy");
    public final static SkillTag breastfeed = new NameOnlySkillTag("Breastfeed");
    public final static SkillTag staminaDamage = new NameOnlySkillTag("StaminaDamage");
    public final static SkillTag positioning = new NameOnlySkillTag("Positioning");
    public final static SkillTag dark = new NameOnlySkillTag("Dark");
    public final static SkillTag escaping = new NameOnlySkillTag("Escaping");
    public final static SkillTag petMasterSkill = new NameOnlySkillTag("PetOk");

    public final static SkillTag accuracy =
                    new BasicSkillTag((results, value) -> results.getRoll() * 100 >= (100 - value) || results.getResult().hasUserTag(perfectAccuracy),
                                    SkillRequirement.noRequirement(), "Accuracy");
    public final static SkillTag miss = new NameOnlySkillTag("Miss");
    public final static SkillTag worship = new WorshipSkillTag();
    public static final SkillTag facesit = new NameOnlySkillTag("Facesitting");

}
