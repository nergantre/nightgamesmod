package nightgames.characters.body.mods;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.global.Global;

public class SecondPussyMod extends PartMod {
    public static final SecondPussyMod INSTANCE = new SecondPussyMod();

    public SecondPussyMod() {
        super("secondpussy", .2, .2, .3, 999);
    }

    @Override
    public String adjective(BodyPart part) {
        return "";
    }

    public Optional<String> getFluids() {
        return Optional.of("juices");
    }

    public String getLongDescriptionOverride(Character self, BodyPart part, String previousDescription) {
        if (part.isType("ass")) {
            return Global.format("Instead of a normal sphincter, {self:possessive} round butt is crowned by a slobbering second pussy.", self, self);
        } else if (part.isType("mouth")) {
            return Global.format("When {self:pronoun} opens {self:possessive} mouth, you can see soft pulsating folds lining {self:possessive} inner mouth, tailor made to suck cocks.", self, self);
        }
        return previousDescription;
    }

    public Optional<String> getDescriptionOverride(Character self, BodyPart part) {
        return Optional.of("pussy");
    }

    public Optional<Boolean> getErogenousOverride() {
        return Optional.of(true);
    }

    @Override
    public String describeAdjective(String partType) {
        return "vaginal aspects";
    }
}