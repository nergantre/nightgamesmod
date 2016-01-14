package nightgames.items;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class RemoveTraitEffect extends ItemEffect {
    private Trait trait;
    private int selfDuration;

    public RemoveTraitEffect(String selfverb, String otherverb, Trait trait, int duration) {
        super(selfverb, otherverb, true, true);
        this.trait = trait;
        selfDuration = duration;
    }

    public RemoveTraitEffect(String self, String other, Trait trait) {
        this(self, other, trait, -1);
    }

    @Override
    public boolean use(Combat c, Character user, Character opponent, Item item) {
        int duration = selfDuration >= 0 ? selfDuration : item.duration;
        if (user.removeTemporaryTrait(trait, duration)) {
            if (c != null) {
                c.write(user, user.subjectAction("temporarily lost", "temporarily lost") + " the trait "
                                + trait.toString() + ".");
            } else if (user.human()) {
                Global.gui().message(user.subjectAction("temporarily lost", "temporarily lost") + " the trait "
                                + trait.toString() + ".");
            }
            return true;
        }
        return false;
    }
}
