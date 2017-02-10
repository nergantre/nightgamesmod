package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.DivineCharge;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class DivineMod extends PartMod {
    public static final DivineMod INSTANCE = new DivineMod();

    public DivineMod() {
        super("divine", 0, 1.0, 0.0, -10);
    }

    public String adjective(GenericBodyPart part) {
        if (part.getType().equals("pussy")) {
            return "divine";
        }
        if (part.getType().equals("ass")) {
            return "sacred";
        }
        return "holy";
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (self.getStatus(Stsflag.divinecharge) != null) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} concentrated divine energy in {self:possessive} %s seeps into {other:name-possessive} %s, sending unimaginable pleasure directly into {other:possessive} soul.",
                            self, opponent, part.getType(), target.getType()));
        }
        // no need for any effects, the bonus is in the pleasure mod
        return 0;
    }

    public double applyReceiveBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        DivineCharge charge = (DivineCharge) self.getStatus(Stsflag.divinecharge);
        if (charge == null) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} " + part.fullDescribe(self)
                                            + " radiates a golden glow when {self:pronoun-action:moan|moans}. "
                                            + "%s feeding on {self:possessive} own pleasure to charge up {self:possessive} divine energy.",
                            self, opponent, self == opponent ? "{self:SUBJECT-ACTION:are|is}" 
                                            : "{other:SUBJECT-ACTION:realize|realizes} {self:subject-action:are|is}"));
            self.add(c, new DivineCharge(self, .25));
        } else {
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:continue|continues} feeding on {self:possessive} own pleasure to charge up {self:possessive} divine energy.",
                            self, opponent));
            self.add(c, new DivineCharge(self, charge.magnitude));
        }
        return 0;
    }

    @Override
    public void receiveCum(Combat c, Character self, BodyPart part, Character opponent, BodyPart target) {
        if (self.has(Trait.zealinspiring) && Global.random(4) > -10) {
            if (c.getStance().partsForStanceOnly(c, self, opponent).contains(part) && c.getStance().partsForStanceOnly(c, opponent, self).stream().anyMatch(otherPart -> otherPart.isType("cock"))) {
                c.write(self, Global.format(
                                "As {other:possessive} cum floods {self:name-possessive} "
                                                + "%s, {other:subject-action:are} assaulted by a feeling of inner peace and belonging. The soothing"
                                                + " emotions washes over {other:pronoun}, before settling in as an unadulterated adoration for {other:possessive} divine partner.",
                                self, opponent, part.describe(self)));
                opponent.addict(c, AddictionType.ZEAL, self, Addiction.MED_INCREASE);
            }
        }
    }

    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart part, BodyPart target) {
        if (opponent.human()) {
            c.write(self, Global.format(
                            "<b>As soon as you penetrate {self:name-do}, you realize it was a bad idea. While it looks innocuous enough, {self:possessive} %s "
                                            + "feels like pure ecstasy. You're not sure why you thought fucking a bonafide sex goddess was a good idea. "
                                            + "{self:SUBJECT} isn't even moving yet, but warm walls of flesh knead your cock ceaselessly while her perfectly trained %s muscles constrict and "
                                            + "relax around your dick, bringing you waves of pleasure.</b>",
                            self, opponent, part.getType(), part.adjective()));
        }
    }

    @Override
    public double modPleasure(Character self) {
        DivineCharge charge = (DivineCharge) self.getStatus(Stsflag.divinecharge);
        double pleasureMod = super.modPleasure(self);
        if (charge != null) {
            pleasureMod += charge.magnitude;
        }
        return pleasureMod;
    }

    @Override
    public String describeAdjective(String partType) {
        return "divine aura";
    }
}