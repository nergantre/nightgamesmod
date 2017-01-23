package nightgames.requirements;

import java.util.Arrays;

import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.items.ItemAmount;
import nightgames.status.Abuff;
import nightgames.status.Stsflag;

/**
 * Shortcut functions for requirements creation
 */
public class RequirementShortcuts {
    public static AnalRequirement anal() {
        return new AnalRequirement();
    }

    public static AndRequirement and(Requirement... subReqs) {
        return new AndRequirement(Arrays.asList(subReqs));
    }

    public static AttributeRequirement attribute(Attribute att, int amount) {
        return new AttributeRequirement(att, amount);
    }

    public static BodyPartRequirement bodypart(String type) {
        return new BodyPartRequirement(type);
    }

    public static BodyPartModRequirement partmod(String type, PartMod mod) {
        return new BodyPartModRequirement(type, mod);
    }

    public static NotRequirement noPartmod(String type, PartMod mod) {
        return not(new BodyPartModRequirement(type, mod));
    }

    public static DomRequirement dom() {
        return new DomRequirement();
    }

    public static DurationRequirement duration(int duration) {
        return new DurationRequirement(duration);
    }

    public static OrRequirement eitherinserted() {
        return or(inserted(), rev(inserted()));
    }

    public static InsertedRequirement inserted() {
        return new InsertedRequirement();
    }

    public static ItemRequirement item(ItemAmount item) {
        return new ItemRequirement(item);
    }

    public static ItemRequirement item(Item item, int amount) {
        return item(new ItemAmount(item, amount));
    }

    public static LevelRequirement level(int level) {
        return new LevelRequirement(level);
    }

    public static MoodRequirement mood(Emotion mood) {
        return new MoodRequirement(mood);
    }

    public static NoneRequirement none() {
        return new NoneRequirement();
    }

    public static Requirement buffedAttLessThan(Attribute att, int amount) {
        return (c, self, other) -> {
            int buff = self.getStatusOfClass(Abuff.class).stream().filter(abuff -> abuff.getModdedAttribute().equals(att)).mapToInt(abuff -> abuff.value()).sum();
            return buff < amount;
        };
    }

    public static NotRequirement not(Requirement subReq) {
        return new NotRequirement(subReq);
    }

    public static OrgasmRequirement orgasms(int count) {
        return new OrgasmRequirement(count);
    }

    public static OrRequirement or(Requirement... subReqs) {
        return new OrRequirement(Arrays.asList(subReqs));
    }

    public static ProneRequirement prone() {
        return new ProneRequirement();
    }

    public static RandomRequirement random(float threshold) {
        return new RandomRequirement(threshold);
    }

    public static ResultRequirement result(Result result) {
        return new ResultRequirement(result);
    }

    public static ReverseRequirement rev(Requirement subReq) {
        return new ReverseRequirement(subReq);
    }

    public static SpecificBodyPartRequirement specificpart(BodyPart part) {
        return new SpecificBodyPartRequirement(part);
    }

    public static PositionRequirement position(String position) {
        return new PositionRequirement(position);
    }

    public static StatusRequirement status(String status) {
        return new StatusRequirement(status);
    }

    public static StatusRequirement status(Stsflag status) {
        return new StatusRequirement(status);
    }

    public static SubRequirement sub() {
        return new SubRequirement();
    }

    public static TraitRequirement trait(Trait trait) {
        return new TraitRequirement(trait);
    }

    public static NotRequirement noTrait(Trait trait) {
        return not(trait(trait));
    }

    public static WinningRequirement winning() {
        return new WinningRequirement();
    }
}
