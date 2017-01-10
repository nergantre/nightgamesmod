package nightgames.characters.body;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.pet.PetCharacter;
import nightgames.skills.damage.DamageType;
import nightgames.status.Abuff;
import nightgames.status.CockBound;
import nightgames.status.DivineCharge;
import nightgames.status.Enthralled;
import nightgames.status.Frenzied;
import nightgames.status.IgnoreOrgasm;
import nightgames.status.Pheromones;
import nightgames.status.Shamed;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public enum PussyPart implements BodyPart,BodyPartMod {
    normal("", 0, 1, 1, 6, 15, 0, CockMod.error),
    arcane("arcane patterned ", .05, 1.1, 1, 9, 5, 3, CockMod.runic),
    fiery("fiery ", 0, 1.3, 1.2, 8, 15, 3, CockMod.enlightened),
    succubus("succubus ", .1, 1.5, 1.2, 999, 0, 4, CockMod.incubus),
    feral("feral ", .2, 1.3, 1.2, 8, 7, 2, CockMod.primal),
    cybernetic("cybernetic ", -.1, 1.8, .5, 200, 0, 4, CockMod.bionic),
    gooey("gooey ", .2, 1.5, 1.2, 999, 0, 6, CockMod.slimy),
    tentacled("tentacled ", 0, 2, 1.2, 999, 0, 8, CockMod.error),
    plant("flower ", .3, 2, 1.2, 999, 0, 8, CockMod.error),
    divine("divine ", 0, 2.0, 1.0, 999, 0, 8, CockMod.blessed);

    public double priority;
    public String desc;
    public String type;
    public double hotness;
    public double pleasure;
    public double capacity;
    public double sensitivity;
    public int wetThreshold;
    private CockMod equivalent;
    public static String synonyms[] = {"pussy", "vagina", "slit",};

    PussyPart(String desc, double hotness, double pleasure, double sensitivity, double capacity, int wetThreshold,
                    int priority, CockMod equivalent) {
        this.type = "pussy";
        this.desc = desc;
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.capacity = capacity;
        this.sensitivity = sensitivity;
        this.wetThreshold = wetThreshold;
        this.priority = priority;
        this.equivalent = equivalent;
    }

    @Override
    public double getFemininity(Character self) {
        return 3;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        if (moddedPartCountsAs(c, plant)) {
            b.append("A larger fleshy red flower is situated between ");
            b.append(c.nameOrPossessivePronoun());
            b.append(" legs, spreading a intoxicatingly sweet scent in the vicinity.");
            return;
        }
        b.append("A ");
        if (c.getArousal()
             .percent() > 15
                        && c.getArousal()
                            .percent() < 60) {
            b.append("moist ");
        } else if (c.getArousal()
                    .percent() >= 60) {
            b.append("drenched ");
        }
        if (this == normal) {
            b.append(Global.pickRandom(Arrays.asList("perfectly ordinary ", "normal ", "womanly ")).get());
        }
        b.append(describe(c));
        b.append(' ');
        if (isType("pussy")) {
            b.append("is nested between " + c.nameOrPossessivePronoun() + " legs.");
        } else if (isType("ass")) {
            b.append("is nested between " + c.nameOrPossessivePronoun() + " asscrack.");
        }
    }

    @Override
    public String canonicalDescription() {
        return desc + "pussy";
    }

    @Override
    public double priority(Character c) {
        return priority + (c.has(Trait.tight) ? 1 : 0) + (c.has(Trait.holecontrol) ? 1 : 0)
                        + +(c.has(Trait.autonomousPussy) ? 4 : 0);
    }

    @Override
    public String describe(Character c) {
        String syn = Global.pickRandom(synonyms).get();
        return desc + syn;
    }

    @Override
    public String fullDescribe(Character c) {
        String syn = Global.pickRandom(synonyms).get();
        return desc + syn;
    }

    @Override
    public String toString() {
        return desc;
    }

    @Override
    public boolean isType(String type) {
        return type.equalsIgnoreCase(this.type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        double val = hotness;
        if (!opponent.hasDick()) {
            val /= 2;
        }
        Clothing bottom = self.getOutfit().getTopOfSlot(ClothingSlot.top);
        if (bottom == null) {
            if (self.hasDick()) {
                val += .05;
            } else {
                val += .1;
            }
        }
        return val;
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        double pleasureMod = pleasure;
        pleasureMod += self.has(Trait.sexTraining1) ? .5 : 0;
        pleasureMod += self.has(Trait.sexTraining2) ? .7 : 0;
        pleasureMod += self.has(Trait.sexTraining3) ? .7 : 0;
        DivineCharge charge = (DivineCharge) self.getStatus(Stsflag.divinecharge);
        if (charge != null) {
            pleasureMod += charge.magnitude;
        }
        return pleasureMod;
    }

    @Override
    public double getSensitivity(Character self, BodyPart target) {
        return sensitivity;
    }

    @Override
    public boolean isReady(Character self) {
        return self.has(Trait.alwaysready) || self.getArousal()
                                                  .percent() >= wetThreshold;
    }

     @Override public JsonObject save() {
        JsonObject obj = new JsonObject();
        obj.addProperty("enum", name());
        return obj;
    }

    @Override public BodyPart load(JsonObject obj) {
        return PussyPart.valueOf(obj.get("enum").getAsString());
    }

    @Override
    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart target) {
        if (moddedPartCountsAs(self, divine) && target.isErogenous()) {
            if (!self.human()) {
                c.write(self, Global.format(
                                "As soon as you penetrate {self:name-do}, you realize it was a bad idea. While it looks innocuous enough, {self:possessive} {self:body-part:pussy} "
                                                + "feels like pure ecstasy. You're not sure why you thought fucking a bonafide sex goddess was a good idea. "
                                                + "{self:SUBJECT} isn't even moving yet, but warm walls of flesh knead your cock ceaselessly while her perfectly trained vaginal muscles constrict and "
                                                + "relax around your dick, bringing you waves of pleasure.",
                                self, opponent));
            }
        } else if (moddedPartCountsAs(self, gooey) && target.isErogenous()) {
            c.write(self, Global.format("{self:NAME-POSSESSIVE} {self:body-part:pussy} envelops"
                            + " {other:possessive} {other:body-part:cock} in a sticky grip, making extraction more"
                            + " difficult.", self, opponent));
            opponent.add(c, new CockBound(opponent, 7, self.nameOrPossessivePronoun() + " gooey pussy"));
        }
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = 0;
        if (moddedPartCountsAs(self, divine) && c.getStance()
                               .vaginallyPenetrated(c, self)) {
            DivineCharge charge = (DivineCharge) self.getStatus(Stsflag.divinecharge);
            if (charge == null) {
                c.write(self, Global.format(
                                "{self:NAME-POSSESSIVE} " + fullDescribe(self)
                                                + " radiates a golden glow when {self:pronoun-action:moan|moans}. "
                                                + "{other:SUBJECT-ACTION:realize|realizes} {self:subject-action:are|is} feeding on {self:possessive} own pleasure to charge up {self:possessive} divine energy.",
                                self, opponent));
                self.add(c, new DivineCharge(self, .25));
            } else {
                c.write(self, Global.format(
                                "{self:SUBJECT-ACTION:continue|continues} feeding on {self:possessive} own pleasure to charge up {self:possessive} divine energy.",
                                self, opponent));
                self.add(c, new DivineCharge(self, charge.magnitude));
            }
        }
        if (moddedPartCountsAs(self, plant) && damage > self.getArousal()
                                                        .max()
                        / 5 && Global.random(4) == 0) {
            c.write(self, String.format("An intoxicating scent emanating from %s %s leaves %s in a trance!",
                            self.possessiveAdjective(), describe(self), opponent.directObject()));
        }
        if (moddedPartCountsAs(self, feral)) {
            c.write(self, String.format("Musk emanating from %s %s leaves %s reeling.", self.possessiveAdjective(),
                            describe(self), opponent.directObject()));
            double base = 3;
            if (target.moddedPartCountsAs(opponent, CockMod.runic)) {
                c.write(self, String.format(
                                "The wild scent overwhelms %s carefully layered enchantments, instantly sweeping %s away.",
                                opponent.nameOrPossessivePronoun(), opponent.directObject()));
                base *= 2.5;
            } else if (target.moddedPartCountsAs(opponent, CockMod.incubus)) {
                c.write(self, String.format("Whilst certainly invigorating, the scent leaves %s largely unaffected.",
                                opponent.subject()));
                base /= 2;
            }
            opponent.add(c, Pheromones.getWith(self, opponent, (float) base, 5, " feral musk"));
        }
        if (opponent.has(Trait.pussyhandler) || opponent.has(Trait.anatomyknowledge)) {
            c.write(opponent,
                            Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} pussy causes {self:subject} to shudder uncontrollably.",
                                            self, opponent));
            if (opponent.has(Trait.pussyhandler)) {
                bonus += 5;
            }
            if (opponent.has(Trait.anatomyknowledge)) {
                bonus += 5;
            }
        }
        return bonus;
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = 0;
        if (moddedPartCountsAs(self, divine) && target.isType("cock")) {
            if (self.getStatus(Stsflag.divinecharge) != null) {
                c.write(self, Global.format(
                                "{self:NAME-POSSESSIVE} concentrated divine energy in {self:possessive} pussy seeps into {other:name-possessive} cock, sending unimaginable pleasure directly into {other:possessive} soul.",
                                self, opponent));
            }
            // no need for any effects, the bonus is in the pleasure mod
        }
        if (moddedPartCountsAs(self, succubus) && target.isType("cock")) {
            if (target.moddedPartCountsAs(opponent, CockMod.runic)) {
                c.write(self, String.format(
                                "Putting in great effort, %s %s to draw upon %s power, but the fae enchantments in %s %s keep it locked away.",
                                self.nameOrPossessivePronoun(), self.human() ? "try" : " tries",
                                opponent.nameOrPossessivePronoun(), opponent.possessiveAdjective(),
                                target.describe(opponent)));
            } else {
                c.write(self, String.format(
                                "%s hot flesh kneads %s %s as %s %s"
                                                + ", drawing gouts of life energy out of %s %s, which is greedily absorbed by %s %s.",
                                self.possessiveAdjective(), opponent.possessiveAdjective(), target.describe(opponent),
                                self.subjectAction("ride", "rides"), opponent.directObject(),
                                opponent.possessiveAdjective(), target.describe(opponent), self.possessiveAdjective(),
                                describe(self)));
                int strength;
                if (target.moddedPartCountsAs(opponent, CockMod.enlightened)) {
                    c.write(self, String.format(
                                    "Since %s had focused so much of %s in %s %s, there is much more for %s to take.",
                                    opponent.subject(), opponent.reflectivePronoun(), opponent.possessiveAdjective(),
                                    target.describe(opponent), self.subject()));
                    strength = 10 + self.get(Attribute.Dark);
                } else {
                    strength = 10 + self.get(Attribute.Dark) / 2;
                }
                strength = (int) self.modifyDamage(DamageType.drain, opponent, strength);
                opponent.drain(c, self, strength);
                if (self.isPet()) {
                    Character master = ((PetCharacter) self).getSelf().owner();
                    c.write(self, Global.format("The stolen strength seems to be shared with {self:possessive} {other:master} through {self:possessive} infernal connection.", self, master));
                    master.heal(c, strength);
                }
                for (int i = 0; i < 10; i++) {
                    Attribute stolen = (Attribute) opponent.att.keySet()
                                                               .toArray()[Global.random(opponent.att.keySet()
                                                                                                    .size())];
                    if (stolen != Attribute.Perception && opponent.get(stolen) > 0) {
                        int stolenStrength = Math.min(strength / 10, opponent.get(stolen));
                        opponent.add(c, new Abuff(opponent, stolen, -stolenStrength, 20));
                        self.add(c, new Abuff(self, stolen, stolenStrength, 20));
                        if (self.isPet()) {
                            Character master = ((PetCharacter) self).getSelf().owner();
                            master.add(c, new Abuff(master, stolen, stolenStrength, 20));
                        }
                        break;
                    }
                }
            }
        }
        if (moddedPartCountsAs(self, tentacled) && target.isType("cock")) {
            if (!opponent.is(Stsflag.cockbound)) {
                if (!self.human()) {
                    c.write(self, Global.format(
                                    "Deep inside {self:name-possessive} pussy, soft walls pulse and strain against your cock. "
                                                    + "You suddenly feel hundreds of thin tentacles, probing like fingers, dancing over every inch of your pole. "
                                                    + "A thicker tentacle wraps around your cock, preventing any escape",
                                    self, opponent));
                } else {
                    c.write(self, Global.format(
                                    "As {other:name-possessive} cock pumps into you, you focus your mind on your lower entrance. "
                                                    + "You mentally command the tentacles inside your womb to constrict and massage {other:possessive} cock. "
                                                    + "{other:name} almost starts hyperventilating from the sensations.",
                                    self, opponent));
                }
                opponent.add(c, new CockBound(opponent, 10, self.nameOrPossessivePronoun() + " vaginal tentacles"));
            } else {
                if (!self.human()) {
                    c.write(self, Global.format(
                                    "As you thrust into {self:name-possessive} pussy, hundreds of tentacles squirm against the motions of your cock, "
                                                    + "making each motion feel like it will push you over the edge.",
                                    self, opponent));
                } else {
                    c.write(self, Global.format(
                                    "As {other:name-possessive} cock pumps into you, your pussy tentacles reflexively curl around the intruding object, rhythmically"
                                                    + "squeezing and milking it constantly.",
                                    self, opponent));
                }
                bonus += 5 + Global.random(4);
            }
        }
        if (moddedPartCountsAs(self, cybernetic) && target.moddedPartCountsAs(opponent, CockMod.enlightened)) {
            c.write(self, String.format(
                            "Despite %s %s's best efforts, %s focus does not waver, and %s barely %s a thing.",
                            self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
                            opponent.pronoun(), opponent.human() ? "feel" : "feels"));
            bonus -= 5;
        }
        if (moddedPartCountsAs(self, cybernetic) && target.isType("cock") && !target.moddedPartCountsAs(opponent, CockMod.enlightened)
                        && (Global.random(3) == 0 || target.moddedPartCountsAs(opponent, CockMod.incubus))) {
            String prefix = target.moddedPartCountsAs(opponent, CockMod.incubus) ? "Eager to gain a sample of "
                            + opponent.nameOrPossessivePronoun() + " exotic, demonic sperm, " : "";
            c.write(self, String.format(
                            prefix + "%s %s whirls to life and starts attempting to extract all the semen packed inside %s %s. "
                                            + "At the same time, %s feel a thin filament sliding into %s urethra, filling %s with both pleasure and shame.",
                            self.possessiveAdjective(), describe(self), opponent.possessiveAdjective(),
                            target.describe(opponent), opponent.pronoun(), opponent.possessiveAdjective(),
                            opponent.directObject()));
            bonus += 15;
            if (target.moddedPartCountsAs(opponent, CockMod.incubus) || Global.random(4) == 0) {
                opponent.add(c, new Shamed(opponent));
            }
        }
        if (moddedPartCountsAs(self, fiery) && target.isType("cock")) {
            if (target.moddedPartCountsAs(opponent, CockMod.primal)) {
                c.write(self, String.format(
                                "The intense heat emanating from %s %s only serves to enflame %s primal passion.",
                                self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun()));
                opponent.buildMojo(c, 7);
            } else if (target.moddedPartCountsAs(opponent, CockMod.bionic)) {
                c.write(self, String.format(
                                "The heat emanating from %s %s is extremely hazardous for %s %s, nearly burning through its circuitry and definitely causing intense pain.",
                                self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent)));
                opponent.pain(c, self, Math.max(30, 20 + self.get(Attribute.Ki)));
            } else {
                c.write(self, String.format("Plugging %s %s into %s %s leaves %s gasping from the heat.",
                                opponent.possessiveAdjective(), target.describe(opponent), self.possessiveAdjective(),
                                describe(self), opponent.directObject()));
                opponent.pain(c, self, 20 + self.get(Attribute.Ki) / 2);
            }
        }
        if (moddedPartCountsAs(self, arcane)) {
            String message;
            int strength;
            if (!target.moddedPartCountsAs(opponent, CockMod.bionic)) {
                if (target.moddedPartCountsAs(opponent, CockMod.primal)) {
                    message = String.format(
                                    "The tattoos around %s %s flare up with a new intensity, responding to the energy flow from %s %s."
                                                    + " The magic within them latches onto it and pulls fiercly, drawing %s strength into %s with great gulps.",
                                    self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
                                    target.describe(opponent), opponent.possessiveAdjective(), self.directObject());
                    strength = 10 + self.get(Attribute.Arcane) / 4;
                } else {
                    message = self.nameOrPossessivePronoun() + " tattoos surrounding " + self.possessiveAdjective()
                                    + " vagina light up with arcane energy as " + opponent.subjectAction("are", "is")
                                    + " inside " + self.directObject() + ", channeling some of "
                                    + opponent.possessiveAdjective() + " energies back to its master.";
                    strength = 5 + self.get(Attribute.Arcane) / 6;
                }
                opponent.loseMojo(c, strength);
                self.buildMojo(c, strength);
                if (self.isPet()) {
                    Character master = ((PetCharacter) self).getSelf().owner();
                    c.write(self, Global.format("The energy seems to flow through {self:direct-object} and into {self:possessive} {other:master}.", self, master));
                    master.buildMojo(c, strength);
                }
            } else {
                message = String.format(
                                "%s tattoos shine with an eldritch light, but they do not seem to be able to affect %s only partially-organic %s",
                                self.nameOrPossessivePronoun(), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent));
            }
            if (Global.random(8) == 0 && !opponent.wary() && !target.moddedPartCountsAs(opponent, CockMod.bionic)) {
                message += " The light seems to seep into " + opponent.possessiveAdjective() + " "
                                + target.describe(opponent) + ", leaving " + opponent.directObject() + " enthralled to "
                                + self.possessiveAdjective() + " will.";
                opponent.add(c, new Enthralled(opponent, self, 3));
            }
            c.write(self, message);
        }

        if (moddedPartCountsAs(self, feral)) {
            int chance = Math.max(3, 10 - self.getArousal()
                                              .getReal()
                            / 50);
            if (!self.is(Stsflag.frenzied) && !self.is(Stsflag.cynical) && target.isType("cock")
                            && Global.random(chance) == 0) {
                c.write(self, String.format(
                                "A cloud of lust descends over %s and %s, clearing both your thoughts of all matters except to fuck. Hard.",
                                opponent.subject(), self.subject()));
                self.add(c, new IgnoreOrgasm(opponent, 3));
                self.add(c, new Frenzied(self, 3));
                opponent.add(c, new Frenzied(opponent, 3));
            }
        }
        if (isType("pussy") && self.has(Trait.vaginaltongue) && target.isType("cock")
                        && !opponent.hasStatus(Stsflag.cockbound)) {
            opponent.add(c, new CockBound(opponent, 5, self.nameOrPossessivePronoun() + " pussy-tongue"));
            c.write(self, self.nameOrPossessivePronoun() + " long sinuous vaginal tongue wraps around "
                            + opponent.nameOrPossessivePronoun() + " " + target.describe(opponent)
                            + ", preventing any escape.\n");
        }
        if ((self.has(Trait.tight) || self.has(Trait.holecontrol)) && c.getStance()
                                                                       .vaginallyPenetrated(c, self)
                                                                       && opponent.body.has("cock")) {
            String desc = "";
            if (self.has(Trait.tight)) {
                desc += "powerful ";
            }
            if (self.has(Trait.holecontrol)) {
                desc += "well-trained ";
            }
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
                                            + "vaginal muscles to milk {other:name-possessive} cock, adding to the pleasure.",
                            self, opponent));
            bonus += self.has(Trait.tight) && self.has(Trait.holecontrol) ? 10 : 5;
        }
        return bonus;
    }

    @Override
    public String getFluids(Character c) {
        return "juices";
    }

    @Override
    public boolean isErogenous() {
        return true;
    }

    @Override
    public boolean isNotable() {
        return true;
    }

    @Override
    public BodyPart upgrade() {
        return this;
    }

    @Override
    public BodyPart downgrade() {
        return this;
    }

    @Override
    public String prefix() {
        return "a ";
    }

    @Override
    public int compare(BodyPart other) {
        return 0;
    }

    @Override
    public boolean isVisible(Character c) {
        return c.crotchAvailable();
    }

    @Override
    public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
                    Combat c) {
        return 0;
    }

    @Override
    public int mod(Attribute a, int total) {
        switch (a) {
            case Seduction:
                return (int) Math.round(hotness * 2);
            default:
                return 0;
        }
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        if (self.has(Trait.autonomousPussy)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} " + fullDescribe(self)
                                            + " churns against {other:name-possessive} cock, "
                                            + "seemingly with a mind of its own. Warm waves of flesh rub against {other:possessive} shaft, elliciting groans of pleasure from {other:direct-object}.",
                            self, opponent));
            opponent.body.pleasure(self, this, otherOrgan, 10, c);
        }
        if (moddedPartCountsAs(self, plant)) {
            c.write(self, Global.format(
                            "The small rough fibery filaments inside {self:name-possessive} flower pussy wrap around {other:name-possessive} cock. "
                                            + "A profound exhaustion settles on {other:direct-object}, as {other:subject-action:feel|feels} {self:name-possessive} insidious flower leeching {other:possessive} strength.",
                            self, opponent));
            opponent.drainStaminaAsMojo(c, self, 20, 1.25f);
            opponent.loseWillpower(c, 5);
        }
        if (moddedPartCountsAs(self, gooey)) {
            c.write(self, Global.format(
                            "The slimy filaments inside {self:possessive} {self:body-part:pussy} constantly massage"
                                            + " {other:possessive} {other:body-part:cock}, filling every inch of it with pleasure.",
                            self, opponent));
            opponent.body.pleasure(self, this, otherOrgan, 1 + Global.random(7), c);
        }
    }

    @Override
    public int counterValue(BodyPart otherPart, Character self, Character other) {
        if (moddedPartCountsAs(self, normal) && otherPart.getType()
                                   .equals("cock")
                        && !((CockPart) otherPart).isGeneric(other)) {
            // If opponent has a modded cock, that's dangerous
            return -1;
        }
        if (!this.isGeneric(self) && otherPart.getType()
                                   .equals("cock")
                        && ((CockPart) otherPart).isGeneric(other)) {
            // On the other hand, if we have a mod, but he doesn't, that's good
            // for us
            return 1;
        }
        if (otherPart instanceof ModdedCockPart) {
            if (moddedPartCountsAs(self, fiery)) {
                return moddedPartCountsAs(other, CockMod.bionic) ? 1 : moddedPartCountsAs(other, CockMod.primal) ? -1 : 0;
            }
            if (moddedPartCountsAs(self, arcane)) {
                return moddedPartCountsAs(other, CockMod.primal) ? 1 : moddedPartCountsAs(other, CockMod.bionic) ? -1 : 0;
            }
            if (moddedPartCountsAs(self, succubus)) {
                return moddedPartCountsAs(other, CockMod.runic) ? -1 : 0;
            }
            if (moddedPartCountsAs(self, feral)) {
                return moddedPartCountsAs(other, CockMod.runic) ? 1 : moddedPartCountsAs(other, CockMod.incubus) ? -1 : 0;
            }
            if (moddedPartCountsAs(self, cybernetic)) {
                return moddedPartCountsAs(other, CockMod.incubus) ? 1 : 0;
            }
        }

        return 0;
    }

    @Override
    public Collection<? extends BodyPartMod> getMods(Character self) {
        if (this == normal) {
            return Collections.emptySet();
        }
        return Collections.singleton(this);
    }

    @Override
    public String getModType() {
        return name();
    }

    @Override
    public void onOrgasm(Combat c, Character self, Character opponent) {
        if (moddedPartCountsAs(self, feral)) {
            c.write(self, Global.format(
                            "As {self:SUBJECT-ACTION:cum|cums} hard, a literal explosion of pheromones hits {other:name-do}. {other:POSSESSIVE} entire body flushes in arousal; {other:subject} better finish this fast!",
                            self, opponent));
            opponent.add(c, Pheromones.getWith(self, opponent, 10, 5, " orgasmic secretions"));
        }
    }

    @Override
    public void onOrgasmWith(Combat c, Character self, Character opponent, BodyPart target, boolean selfCame) {
         if (target.isType("cock") && !selfCame) {
            if (moddedPartCountsAs(self, gooey)) {
                c.write(self, Global.format(
                                "{self:NAME-POSSESSIVE} {self:body-part:pussy} clenches down hard"
                                                + " on {other:name-possessive} {other:body-part:cock}. The suction is so strong that the cum"
                                                + " leaves the shaft in a constant flow rather than spurts. When {other:possessive} orgasm is"
                                                + " over, {other:subject-action:are|is} much more drained of cum than usual.",
                                self, opponent));
                opponent.loseWillpower(c, 10 + Global.random(Math.min(20, self.get(Attribute.Bio))));
            } else if (moddedPartCountsAs(self, divine) && self.has(Trait.zealinspiring) && opponent.human() && opponent instanceof Player
                            && Global.random(4) > 0) {
                c.write(self, Global.format(
                                "As {other:possessive} cum floods {self:name-possessive} "
                                                + "{self:body-part:pussy}, a holy aura surrounds {self:direct-object}. The soothing"
                                                + " light washes over {other:pronoun}, filling {other:direct-object} with a zealous need to worship {self:possessive} divine body.",
                                self, opponent));
                ((Player)opponent).addict(AddictionType.ZEAL, self, Addiction.MED_INCREASE);
            }
        }
    }

    @Override
    public boolean countsAs(Character self, BodyPartMod part) {
        if (self == null) {
            return part == this;
        }
        SlimeMimicry mimicry = ((SlimeMimicry)self.getStatus(Stsflag.mimicry));
        if (this == gooey && part != gooey && mimicry != null) {
            return mimicry.getPussyMimicked() == part;
        } else {
            return part == this;
        }
    }

    public CockMod getEquivalentCockMod() {
        return equivalent;
    }

    public CockPart getEquivalentCock(BasicCockPart part) {
        if (equivalent != CockMod.error) {
            return new ModdedCockPart(part, equivalent);
        }
        return part;
    }

    @Override
    public String adjective() {
        return "vaginal";
    }
}
