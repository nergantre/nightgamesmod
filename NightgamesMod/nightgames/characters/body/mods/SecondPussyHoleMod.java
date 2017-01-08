package nightgames.characters.body.mods;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.global.Global;

public class SecondPussyHoleMod extends HoleMod {
    public SecondPussyHoleMod() {
        super("secondpussy", .2, .2, .3, 999);
    }

    public Optional<String> getFluids() {
        return Optional.of("juices");
    }

    public Optional<String> getLongDescriptionOverride(Character self, BodyPart part) {
        if (part.isType("ass")) {
            return Optional.of(Global.format("Instead of a normal sphincter, {self:possessive} round butt is crowned by a slobbering second pussy.", self, self));
        } else if (part.isType("mouth")) {
            return Optional.of(Global.format("When {self:pronoun} opens {self:possessive} mouth, you can see soft pulsating folds lining {self:possessive} inner mouth, tailor made to suck cocks.", self, self));
        }
        return Optional.empty();
    }

    public Optional<String> getDescriptionOverride(Character self, BodyPart part) {
        return Optional.of("pussy");
    }
}