package nightgames.characters.body;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.CockBound;
import nightgames.status.DivineCharge;
import nightgames.status.Enthralled;
import nightgames.status.FluidAddiction;
import nightgames.status.Horny;
import nightgames.status.Hypersensitive;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;
import nightgames.status.Winded;

public enum CockMod implements BodyPartMod {
    error(1.0, 1.0, 1.0),
    slimy(.5, 1.5, .7),
    runic(2.0, 1.0, 1.0),
    blessed(1.0, 1.0, .75),
    incubus(1.25, 1.3, .9),
    primal(1.0, 1.4, 1.2),
    bionic(.8, 1.3, .5),
    enlightened(1.0, 1.2, .8);

    private double sensitivity;
    private double pleasure;
    private double hotness;

    CockMod(double hotness, double pleasure, double sensitivity) {
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.sensitivity = sensitivity;
    }

    public double getHotness(Character self, Character opponent, BasicCockPart base) {
        return base.getHotness(self, opponent) * hotness;
    }

    public double getPleasure(Character self, BodyPart target, BasicCockPart base) {
        double pleasureMod = 0;
        DivineCharge charge = (DivineCharge) self.getStatus(Stsflag.divinecharge);
        if (charge != null) {
            pleasureMod += charge.magnitude;
        }
        return (pleasureMod + base.getPleasure(self, target)) * pleasure;
    }

    public double getSensitivity(BodyPart target, BasicCockPart base) {
        return base.getSensitivity(target) * sensitivity;
    }

    public boolean isReady(Character self, BasicCockPart base) {
        return base.isReady(self) || this.countsAs(self, bionic);
    }

    @SuppressWarnings("unchecked") public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("enum", name());
        return object;
    }

    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c,
                    ModdedCockPart part) {
        double bonus = part.getBase().applyBonuses(self, opponent, target, damage, c);
        if (this.countsAs(self, blessed) && target.isType("cock")) {
            if (self.getStatus(Stsflag.divinecharge) != null) {
                c.write(self, Global.format(
                                "{self:NAME-POSSESSIVE} concentrated divine energy in {self:possessive} cock rams into {other:name-possessive} pussy, sending unimaginable pleasure directly into {other:possessive} soul.",
                                self, opponent));
            }
            // no need for any effects, the bonus is in the pleasure mod
        }
        if (this.countsAs(self, runic)) {
            String message = "";
            if (target.moddedPartCountsAs(opponent, PussyPart.succubus)) {
                message += String.format(
                                "The fae energies inside %s %s radiate outward and into %s, causing %s %s to grow much more sensitve.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                opponent.possessivePronoun(), target.describe(opponent));
                bonus += damage * 0.5; // +50% damage
            }
            if (Global.random(8) == 0 && !opponent.wary()) {
                message += String.format("Power radiates out from %s %s, seeping into %s and subverting %s will. ",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                opponent.directObject());
                opponent.add(c, new Enthralled(opponent, self, 3));
            }
            if (self.hasStatus(Stsflag.cockbound)) {
                String binding = ((CockBound) self.getStatus(Stsflag.cockbound)).binding;
                message += String.format(
                                "With the merest of thoughts, %s %s out a pulse of energy from %s %s, freeing it from %s %s. ",
                                self.subject(), self.human() ? "send" : "sends", self.possessivePronoun(),
                                part.describe(self), opponent.nameOrPossessivePronoun(), binding);
                self.removeStatus(Stsflag.cockbound);
            }
            c.write(self, message);
        } else if (this.countsAs(self, incubus)) {
            String message = String.format("%s demonic appendage latches onto %s will, trying to draw it into %s.",
                            self.nameOrPossessivePronoun(), opponent.nameOrPossessivePronoun(),
                            self.reflectivePronoun());
            int amtDrained;
            if (target.moddedPartCountsAs(opponent, PussyPart.feral)) {
                message += String.format(" %s %s gladly gives it up, eager for more pleasure.",
                                opponent.possessivePronoun(), target.describe(opponent));
                amtDrained = 5;
                bonus += 2;
            } else if (target.moddedPartCountsAs(opponent, PussyPart.cybernetic)) {
                message += String.format(
                                " %s %s does not oblige, instead sending a pulse of electricity through %s %s and up %s spine",
                                opponent.nameOrPossessivePronoun(), target.describe(opponent),
                                self.nameOrPossessivePronoun(), part.describe(self), self.possessivePronoun());
                self.pain(c, Global.random(9) + 4);
                amtDrained = 0;
            } else {
                message += String.format(" Despite %s best efforts, some of the elusive energy passes into %s.",
                                opponent.nameOrPossessivePronoun(), self.nameDirectObject());
                amtDrained = 3;
            }
            if (amtDrained != 0) {
                opponent.loseWillpower(c, amtDrained);
                self.restoreWillpower(c, amtDrained);
            }
            c.write(self, message);
        } else if (this.countsAs(self, bionic)) {
            String message = "";
            if (Global.random(5) == 0 && target.getType().equals("pussy")) {
                message += String.format(
                                "%s %s out inside %s %s, pressing the metallic head of %s %s tightly against %s cervix. "
                                                + "Then, a thin tube extends from %s uthera and into %s womb, pumping in a powerful aphrodisiac that soon has %s sensitive and"
                                                + " gasping for more.",
                                self.subject(), self.human() ? "bottom" : "bottoms", opponent.nameOrPossessivePronoun(),
                                target.describe(opponent), self.possessivePronoun(), part.describe(self),
                                opponent.possessivePronoun(), self.possessivePronoun(), opponent.possessivePronoun(),
                                opponent.directObject());
                opponent.add(c, new Hypersensitive(opponent));
                // Instantly addict
                opponent.add(c, new FluidAddiction(opponent, self, 1, 2));
                opponent.add(c, new FluidAddiction(opponent, self, 1, 2));
                opponent.add(c, new FluidAddiction(opponent, self, 1, 2));
                bonus -= 3; // Didn't actually move around too much
            } else if (target != PussyPart.fiery) {
                message += String.format(
                                "Sensing the flesh around it, %s %s starts spinning rapidly, vastly increasing the friction against the walls of %s %s.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent));
                bonus += 5;
                if (Global.random(5) == 0) {
                    message += String.format(
                                    " The intense sensations cause %s to forget to breathe for a moment, leaving %s literally breathless.",
                                    opponent.subject(), opponent.directObject());
                    opponent.add(c, new Winded(opponent, 1));
                }
            }
            c.write(self, message);
        } else if (this.countsAs(self, enlightened)) {
            String message = "";
            if (target.moddedPartCountsAs(opponent, PussyPart.succubus)) {
                message = String.format(
                                "Almost instinctively, %s %s entire being into %s %s. While this would normally be a good thing,"
                                                + " whilst fucking a succubus it is very, very bad indeed.",
                                self.subjectAction("focus", "focuses"), self.possessivePronoun(),
                                self.possessivePronoun(), part.describe(self));
                c.write(self, message);
                // Actual bad effects are dealt with in PussyPart
            } else {
                message = String.format(
                                "Drawing upon %s extensive training, %s %s will into %s %s, enhancing %s own abilities",
                                self.possessivePronoun(), self.subjectAction("concentrate", "concentrates"),
                                self.possessivePronoun(), self.possessivePronoun(), part.describe(self),
                                self.possessivePronoun());
                c.write(self, message);
                for (int i = 0; i < Math.max(2, (self.get(Attribute.Ki) + 5) / 10); i++) { // +5
                                                                                           // for
                                                                                           // rounding:
                                                                                           // 24->29->20,
                                                                                           // 25->30->30
                    Attribute attr = new Attribute[] {Attribute.Power, Attribute.Cunning, Attribute.Seduction}[Global
                                    .random(3)];
                    self.add(c, new Abuff(self, attr, 1, 10));
                }
                self.buildMojo(c, 5);
                self.restoreWillpower(c, 1);
            }
        }
        return bonus;
    }

    public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
                    Combat c, ModdedCockPart moddedCockPart) {
        return moddedCockPart.getBase().applySubBonuses(self, opponent, with, target, damage, c);
    }

    public String getFluids(Character c, BasicCockPart base) {
        return this.countsAs(c, bionic) ? "artificial lubricant" : base.getFluids(c);
    }

    public boolean isVisible(Character c, BasicCockPart base) {
        return base.isVisible(c);
    }

    public boolean isNotable(BasicCockPart base) {
        return base.isNotable();
    }

    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c,
                    ModdedCockPart moddedCockPart) {
        if (this.countsAs(self, blessed) && c.getStance().inserted(self)) {
            DivineCharge charge = (DivineCharge) self.getStatus(Stsflag.divinecharge);
            if (charge == null) {
                c.write(self, Global.format(
                                "{self:NAME-POSSESSIVE} " + moddedCockPart.fullDescribe(self)
                                                + " radiates a golden glow as {self:subject-action:groan|groans}. "
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
        return moddedCockPart.getBase().applyReceiveBonuses(self, opponent, target, damage, c);
    }

    public String fullDescribe(Character c, BasicCockPart base) {
        String description;
        if (this.countsAs(c, bionic)) {
            description = "bionic robo-";
        } else if (this.countsAs(c, incubus) && c.hasPussy()) {
            description = "demonic girl-";
        } else {
            description = name() + (c.hasPussy() ? " girl-" : " ");
        }
        String syn = Global.pickRandom(BasicCockPart.synonyms);
        return base.desc + " " + description + syn;
    }

    public String describe(Character c, BasicCockPart base) {
        String description;
        if (this.countsAs(c, bionic)) {
            description = "bionic robo-";
        } else if (this.countsAs(c, incubus) && c.hasPussy()) {
            description = "demonic girl-";
        } else {
            description = name() + (c.hasPussy() ? " girl-" : " ");
        }
        String syn = Global.pickRandom(BasicCockPart.synonyms);
        return Global.maybeString(base.desc) + " " + description + syn;
    }

    public double priority(Character c, BasicCockPart base) {
        return getPleasure(c, null, base);
    }

    public void onOrgasm(Combat c, Character self, Character opponent, BodyPart target, boolean selfCame,
                    CockPart part) {
        if (this.countsAs(self, incubus) && c.getStance().inserted(self)) {
            if (selfCame) {
                if (target.moddedPartCountsAs(opponent, PussyPart.cybernetic)) {
                    c.write(self, String.format(
                                    "%s demonic seed splashes pointlessly against the walls of %s %s, failing even in %s moment of defeat.",
                                    self.nameOrPossessivePronoun(), opponent.nameOrPossessivePronoun(),
                                    target.describe(opponent), self.possessivePronoun()));
                } else {
                    int duration = Global.random(3) + 2;
                    String message = String.format(
                                    "The moment %s erupts inside %s, %s mind goes completely blank, leaving %s pliant and ready.",
                                    self.subject(), opponent.subject(), opponent.possessivePronoun(),
                                    opponent.directObject());
                    if (target.moddedPartCountsAs(opponent, PussyPart.feral)) {
                        message += String.format(" %s no resistance to the subversive seed.",
                                        Global.capitalizeFirstLetter(opponent.subjectAction("offer", "offers")));
                        duration += 2;
                    }
                    opponent.add(c, new Enthralled(opponent, self, duration));
                    c.write(self, message);
                }
            } else {
                if (target != PussyPart.cybernetic) {
                    c.write(self, String.format(
                                    "Sensing %s moment of passion, %s %s greedily draws upon the rampant flows of orgasmic energy within %s, transferring the power back into %s.",
                                    opponent.nameOrPossessivePronoun(), self.nameOrPossessivePronoun(),
                                    part.describe(self), opponent.directObject(), self.directObject()));
                    int attDamage = target.moddedPartCountsAs(opponent, PussyPart.feral) ? 10 : 5;
                    int willDamage = target.moddedPartCountsAs(opponent, PussyPart.feral) ? 40 : 20;
                    opponent.add(c, new Abuff(opponent, Attribute.Power, -attDamage, 20));
                    opponent.add(c, new Abuff(opponent, Attribute.Cunning, -attDamage, 20));
                    opponent.add(c, new Abuff(opponent, Attribute.Seduction, -attDamage, 20));
                    self.add(c, new Abuff(self, Attribute.Power, attDamage, 20));
                    self.add(c, new Abuff(self, Attribute.Cunning, attDamage, 20));
                    self.add(c, new Abuff(self, Attribute.Seduction, attDamage, 20));
                    opponent.loseWillpower(c, willDamage);
                    self.restoreWillpower(c, willDamage);

                }
            }
        }
    }

    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan, CockPart part) {
        if (this.countsAs(self, primal)) {
            c.write(self, String.format("Raw sexual energy flows from %s %s into %s %s, enflaming %s lust",
                            self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                            otherOrgan.describe(opponent), opponent.possessivePronoun()));
            opponent.add(c, new Horny(opponent, Global.random(3) + 1, 3,
                            self.nameOrPossessivePronoun() + " primal passion"));
        }
    }

    public CockMod load(JsonObject obj) {
        return CockMod.valueOf(obj.get("enum").getAsString());
    }

    public int mod(Attribute a, int total, BasicCockPart base) {
        return base.mod(a, total);
    }

    @Override
    public String getModType() {
        return name();
    }

    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart target,
                    ModdedCockPart moddedCockPart) {
        if (this.countsAs(self, blessed) && target.isErogenous()) {
            if (!self.human()) {
                c.write(self, Global.format(
                                "As soon as {self:subject} penetrates you, you realize you're screwed. Both literally and figuratively. While it looks innocuous enough, {self:name-possessive} {self:body-part:cock} "
                                                + "feels like pure ecstasy. {self:SUBJECT} hasn't even begun moving yet, but {self:possessive} cock simply sitting within you radiates a heat that has you squirming uncontrollably.",
                                self, opponent));
            }
        }
    }

    public void onEndPenetration(Combat c, Character self, Character opponent, BodyPart target,
                    ModdedCockPart moddedCockPart) {
        if (this.countsAs(self, slimy)) {
            c.write(self, Global.format(
                            "As {self:possessive} {self:body-part:cock} leaves {other:possessive} "
                                            + target.describe(opponent)
                                            + ", a small bit of slime stays behind, vibrating inside of {other:direct-object}.",
                            self, opponent));
            opponent.add(c, new Horny(opponent, 4f, 10, self.nameOrPossessivePronoun() + " slimy residue"));
        }
    }
    

    @Override
    public boolean countsAs(Character self, BodyPartMod part) {
        if (self == null) {
            return part == this;
        }
        SlimeMimicry mimicry = ((SlimeMimicry)self.getStatus(Stsflag.mimicry));
        if (this == CockMod.slimy && part != CockMod.slimy && mimicry != null) {
            return mimicry.getCockMimicked() == part;
        } else {
            return part == this;
        }
    }
}
