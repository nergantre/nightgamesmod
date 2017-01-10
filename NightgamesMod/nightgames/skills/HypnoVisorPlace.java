package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.HypnoVisor;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;
import nightgames.status.addiction.Addiction.Severity;

public class HypnoVisorPlace extends Skill {

    public HypnoVisorPlace(Character self) {
        super("Place Hypno Visor", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.hypnovisor);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.human() && getSelf().canAct() && c.getStance().reachTop(getSelf()) 
                        && !target.canRespond() && !target.is(Stsflag.blinded);
    }

    @Override
    public String describe(Combat c) {
        return "Place a Hypno Visor on your opponent's head.";
    }
    
    @Override
    public float priorityMod(Combat c) {
        return 8.f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Player player = (Player) target;
        StringBuilder sb = new StringBuilder(Global.format("{self:SUBJECT-ACTION:walk|walks}"
                        + " around and kneels behind {other:name-do}, propping {other:direct-object}"
                        + " up against {self:direct-object}. ", getSelf(), target));
        sb.append(addictionDesc(c, player));
        sb.append(Global.format(" {self:PRONOUN-ACTION:rummage|rummages} around a bit"
                        + " behind {other:direct-object}, and then {other:possessive}"
                        + " vision goes dark. {self:PRONOUN-ACTION:have|has} placed some"
                        + " kind of device around {self:possessive} head, not unlike a VR headset."
                        + " It might be a really good idea to get it off quickly...", getSelf(), player));
        c.write(getSelf(), sb.toString());
        player.add(c, new HypnoVisor(player, getSelf()));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new HypnoVisorPlace(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }
    
    private boolean isInWithdrawal(Player player) {
        Addiction add = player.getAddiction(AddictionType.MIND_CONTROL).orElse(null);
        return add != null && add.atLeast(Severity.LOW) && add.isInWithdrawal();
    }
    
    private String addictionDesc(Combat c, Player player) {
        if (isInWithdrawal(player)) {
            return Global.format("<i>Ah, {other:name}. Why did you try to get away from my control?"
                            + " We both know there is no point, that you are secretly happier"
                            + " when you don't have to think for yourself. So, let's wash that pesky"
                            + " stubborness right out of you, alright?.</i>", getSelf(), player);
        }
        switch (player.getAddictionSeverity(AddictionType.MIND_CONTROL)) {
            case HIGH:
                return Global.format("<i>You're doing really well so far, {other:name}!"
                                + " Still, a little refresher surely can't hurt?</i>", getSelf(), player);
            case LOW:
                return Global.format("<i>You're still a little reluctant to do as you"
                                + " are told, aren't you {other:name}? Well, I know just"
                                + " the thing to help fix that!</i>", getSelf(), player);
            case MED:
                return Global.format("<i>You know {other:name}, we both know that, deep"
                                + " down, you want to be a good {other:boy}, right? You"
                                + " just need a little help. Well, who am I not to give"
                                + " it to you?</i>", getSelf(), player);
            case NONE:
                return Global.format("<i>Calm down now, {other:name}. Everything will be fine,"
                                + " you just need to hold still for a moment...</i>", getSelf(), player);
            default:
                return "<u>[[[Something went badly wrong in HypnoVisorPlace]]]</u>";
            
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
