package nightgames.items;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class PartModEffect extends ItemEffect {
    private String affectedType;
    private PartMod mod;
    private int selfDuration;

    public PartModEffect(String selfverb, String otherverb, String affectedType, PartMod mod) {
        this(selfverb, otherverb, affectedType, mod, -1);
    }

    public PartModEffect(String selfverb, String otherverb, String affectedType, PartMod mod, int duration) {
        super(selfverb, otherverb, true, true);
        this.affectedType = affectedType;
        this.mod = mod;
        selfDuration = duration;
    }

    @Override
    public boolean use(Combat c, Character user, Character opponent, Item item) {
        BodyPart oldPart = user.body.getRandom(affectedType);
        if (oldPart != null && oldPart instanceof GenericBodyPart && !oldPart.moddedPartCountsAs(user, mod)) {
            user.body.temporaryAddPartMod(affectedType, mod, selfDuration);
            BodyPart newPart = user.body.getRandom(affectedType);
            Global.writeFormattedIfCombat(c, "<b>{self:NAME-POSSESSIVE} %s turned into a %s!</b>", user, opponent, oldPart.describe(user), newPart.describe(user));
            return true;
        }
        return false;
    }
}