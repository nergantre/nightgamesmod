package nightgames.characters;

import nightgames.nskills.tags.AttributeSkillTag;
import nightgames.nskills.tags.SkillTag;

public enum Attribute {
    Power("weaker", "stronger"),
    Seduction("less seductive", "more seductive"),
    Cunning("that it's harder to think", "more intelligent"),
    Perception("less perceptive", "more perceptive"),
    Speed("slower", "faster"),
    Arcane("more mundane", "more in tune with mystic energies"),
    Science("dumber", "more technologically inclined"),
    Dark("like {self:pronoun-action:are} lacking some of {self:possessive} usual darkness", "more sinful"),
    Fetish("like it's harder to fetishize things", "it's easier to dream about fetishes"),
    Animism("tamer", "wilder"),
    Ki("like {self:pronoun-action:have} less aura", "more in control of your body"),
    Bio("like {self:pronoun-action:have} less control over {self:possessive} biology", "more in control of {self:possessive} biology"),
    Divinity("less divine", "more divine"),
    Willpower("like {self:pronoun-action:have} less self-control", "more psyched up"),
    Medicine("like {self:pronoun-action:have} less medical knowledge", "{self:reflective} more in command of medical knowledge"),
    Technique("like {self:pronoun-action:have} less technique", "more sexually-experienced"),
    Submissive("less in tune with your partner's needs", "more responsive"),
    Hypnosis("less hypnotic", "like it's easier to bend other's wills"),
    Nymphomania("like {self:pronoun-action:have} less sex drive", "hornier"),
    Slime("like {self:pronoun-action:have} less control over {self:possessive} slime", "more in control over {self:possessive} slime"),
    Ninjutsu("less stealthy", "stealthier"),
    Temporal("like {self:pronoun-action:are} forgetting some finer details of the procrastinator", "better in tune with the finer details of the procrastinator");

    private final SkillTag skillTag;
    private final String lowerVerb;
    private final String raiseVerb;
    private Attribute(String lowerVerb, String raiseVerb) {
        skillTag = new AttributeSkillTag(this);
        this.lowerVerb = lowerVerb;
        this.raiseVerb = raiseVerb;
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

    public String getLowerPhrase() {
        return lowerVerb;
    }

    public String getRaisePhrase() {
        return raiseVerb;
    }
}
