package nightgames.characters;

import nightgames.nskills.tags.AttributeSkillTag;
import nightgames.nskills.tags.SkillTag;

public enum Attribute {
    Power("weaker", "stronger", "physical strength", "power"),
    Seduction("less seductive", "more seductive", "allure", "seductiveness"),
    Cunning("less intelligent", "more intelligent", "guile", "cunning"),
    Perception("less perceptive", "more perceptive", "keenness", "perception"),
    Speed("slower", "faster", "quickness", "speed"),
    Arcane("more mundane", "more in tune with mystic energies", "mystic energies", "arcane powers"),
    Science("dumber", "more technologically inclined", "gadget know-how", "scientific knowledge"),
    Dark("like {self:pronoun-action:are} lacking some of {self:possessive} usual darkness", "more sinful", "sin", "darkness"),
    Fetish("like it's harder to fetishize things", "it's easier to dream about fetishes", "fantasies", "fetishes"),
    Animism("tamer", "wilder", "instinct", "animism"),
    Ki("like {self:pronoun-action:have} less aura", "more in control of your body", "spirit", "ki"),
    Bio("like {self:pronoun-action:have} less control over {self:possessive} biology", "more in control of {self:possessive} biology", "essence", "biological control"),
    Divinity("less divine", "more divine", "divinity", ""),
    Willpower("like {self:pronoun-action:have} less self-control", "more psyched up", "self-control", "willpower"),
    Medicine("like {self:pronoun-action:have} less medical knowledge", "{self:reflective} more in command of medical knowledge", "medical knowledge", ""),
    Technique("like {self:pronoun-action:have} less technique", "more sexually-experienced", "sexual flair", "techniques"),
    Submissive("less in tune with your partner's needs", "more responsive", "submissiveness", "responsiveness"),
    Hypnosis("less hypnotic", "like it's easier to bend other's wills", "entrancing demeanour", "hypnotic gaze"),
    Nymphomania("like {self:pronoun-action:have} less sex drive", "hornier", "sex drive", "nymphomania"),
    Slime("like {self:pronoun-action:have} less control over {self:possessive} slime", "more in control over {self:possessive} slime", "control over {self:possessive} amorphous body", "slime"),
    Ninjutsu("less stealthy", "stealthier", "stealth and training", "ninjutsu"),
    Temporal("like {self:pronoun-action:are} forgetting some finer details of the procrastinator", "better in tune with the finer details of the procrastinator", "knowledge of the procrastinator", "");

    private final SkillTag skillTag;
    private final String lowerVerb;
    private final String raiseVerb;
    private final String drainedDirectObject;
    private final String drainerDirectObject;
    Attribute(String lowerVerb, String raiseVerb, String drainedDirectObject, String drainerDirectObject) {
        skillTag = new AttributeSkillTag(this);
        this.lowerVerb = lowerVerb;
        this.raiseVerb = raiseVerb;
        this.drainedDirectObject = drainedDirectObject;
        this.drainerDirectObject = drainerDirectObject;
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

    public String getDrainedDO() {
        return drainedDirectObject;
    }

    public String getDrainerOwnDO() {
        return drainerDirectObject.isEmpty() ? "own" : "own " + drainerDirectObject;
    }

    public String getDrainerDO() {
        return drainerDirectObject.isEmpty() ? drainedDirectObject : drainerDirectObject;
    }
}
