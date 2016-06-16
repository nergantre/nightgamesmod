package nightgames.characters;

import nightgames.characters.body.Body;
import nightgames.global.Global;

public enum CharacterSex {
    male("male"),
    female("female"),
    herm("hermaphrodite"),
    asexual("asexual");

    private String desc;

    CharacterSex(String desc) {
        this.desc = desc;
    }

    public boolean hasPussy() {
        return this == female || this == herm;
    }

    public boolean hasCock() {
        return this == male || this == herm;
    }



    @Override
    public String toString() {
        return Global.capitalizeFirstLetter(desc);
    }
}
