package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Horny;
import nightgames.status.InducedEuphoria;

public class InjectAphrodisiac extends Skill {
    public InjectAphrodisiac(Character self) {
        super("Inject Aphrodisiac", self);
    }

    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Medicine) >= 4;
    }

    public boolean usable(Combat c, Character target) {
        return (c.getStance().mobile(this.getSelf())) && (this.getSelf().has(Item.Aphrodisiac) && (!getSelf().human()))
                        && (this.getSelf().canAct()) && getSelf().has(Item.MedicalSupplies, 1)
                        && (!c.getStance().mobile(this.getSelf()) || !target.canAct());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    public boolean resolve(Combat c, Character target) {
        int magnitude = 2 + getSelf().get(Attribute.Medicine);
        if (this.getSelf().human()) {
            c.write(getSelf(), deal(c, magnitude, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, magnitude, Result.normal, this.getSelf()));
        }
        target.emote(Emotion.horny, 20);
        this.getSelf().consume(Item.Aphrodisiac, 1);
        target.add(c, new Horny(target, magnitude, 10, "Aphrodisac Injection"));
        target.add(c, new InducedEuphoria(target));
        getSelf().consume(Item.MedicalSupplies, 1);

        return true;
    }

    public Skill copy(Character user) {
        return new InjectAphrodisiac(user);
    }

    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "You quickly grab one of your syringes full of potent aphrodisiac before grabbing {other:name-do} and injecting {other:direct-object} with its contents. After you do so you see a bright flush spread across {other:possessive} face and {other:possessive} breathing picks up.",
                        getSelf(), target);
    }

    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "{self:SUBJECT} grins as {self:pronoun} flashes a hypodermic needle filled with light purple liquid. You yelp as {self:pronoun} grab your arm before jabbing you with the needle skillfully, pushing the plunger down to unload its cargo. A warmth floods through your body as the drug begins to take effect. It was an aphrodisiac!",
                        getSelf(), target);
    }

    public String describe(Combat c) {
        return "Injects your opponent with aphrodisiac";
    }
}
