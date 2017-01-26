package nightgames.characters;

import nightgames.global.Global;
import nightgames.nskills.tags.AttributeSkillTag;
import nightgames.nskills.tags.SkillTag;

public enum Attribute {
    Power(findYourself("weaker")),
    Seduction(findYourself("less seductive")),
    Cunning("{self:action:find|finds} that it's harder to think"),
    Perception(findYourself("less perceptive")),
    Speed(findYourself("slower")),
    Arcane(findYourself("more mundane")),
    Science(findYourself("dumber")),
    Dark(findYourself("lacking some of {self:possessive} usual darkness")),
    Fetish(youFind("it's harder to fetishize things")),
    Animism("tamer"),
    Ki(findYourself("with less aura")),
    Bio(findYourself("with less control over {self:possessive} biology")),
    Divinity(findYourself("less divine")),
    Willpower(findYourself("with less self-control")),
    Medicine(findYourself("with less medical knowledge")),
    Technique(findYourself("with less technique")),
    Submissive(findYourself("with understanding of being a bottom")),
    Hypnosis(findYourself("less hypnotic")),
    Nymphomania(findYourself("with less sex drive")),
    Slime(findYourself("with less control over {self:possessive} slime")),
    Ninjutsu(findYourself("less stealthy")),
    Temporal(findYourself("forgetting some finer details of the procrastinator"));

    private final SkillTag skillTag;
    private final String lowerVerb;
    private Attribute(String lowerVerb) {
        skillTag = new AttributeSkillTag(this);
        this.lowerVerb = lowerVerb;
    }
    
    public SkillTag getSkillTag() {
        return skillTag;
    }

    public static boolean isBasic(Character self, Attribute a) {
        return a == Power || a == Seduction || a == Perception || (self != null && self.has(Trait.nymphomania) && a == Attribute.Nymphomania);
    }

    public static boolean isTrainable(Character self, Attribute a) {
        if (a == Willpower) {
            return self.getWillpower().max() + 2 <= self.getMaxWillpowerPossible();
        }
        return a != Speed && a != Perception && (self.has(Trait.divinity) || a != Divinity);
    }

    private static String findYourself(String desc) {
        return "{self:action:find|finds} {self:reflective} " + desc;
    }

    private static String youFind(String desc) {
        return "{self:subject-action:find|finds} " + desc;
    }

    public String getLowerPhrase() {
        return lowerVerb;
    }
}
