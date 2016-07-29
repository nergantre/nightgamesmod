package nightgames.nskills;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Trait;
import nightgames.nskills.effects.SkillEffectBuilder;
import nightgames.nskills.effects.rolls.BasicNumberRoll;
import nightgames.nskills.effects.rolls.ConstantRoll;
import nightgames.nskills.effects.rolls.TraitBonusRoll;
import nightgames.nskills.tags.SkillTag;
import nightgames.requirements.TraitRequirement;
import nightgames.stance.Stance;

public class SkillPoolBuilder {
    List<SkillInterface> skills;

    public SkillPoolBuilder() {
        skills = new ArrayList<>();
    }

    public void buildSkillPool() {
        SkillEffectBuilder builder = new SkillEffectBuilder();
        SkillResult defaultResult = new SkillResult().addUserTag(SkillTag.anal)
                                                     .addUserTag(Attribute.Seduction.getSkillTag(), 30)
                                                     .addUserTag(SkillTag.usesMouth)
                                                     .addUserTag(SkillTag.oral)
                                                     .addUserTag(SkillTag.reachLowerBody)
                                                     .addUserTag(SkillTag.contact)
                                                     .addTargetTag(SkillTag.usesAss)
                                                     .addTargetTag(SkillTag.pleasure)
                                                     .setLabel("Anilingus")
                                                     .setSpeed(2)
                                                     .setDescription("Perform anilingus on opponent");
        defaultResult.createChildResult()
                     .addUserTag(SkillTag.miss)
                     .setSuccess(false)
                     .addEffect(builder.write(
                                     "{self:SUBJECT-ACTION:try|tries} to lick {other:name-possessive} rosebud, but {other:subject-action:push|pushes} pushes {self:possessive} head away."))
                     .setPriority(0);
        // normal result
        defaultResult.createChildResult()
                     .addUserTag(SkillTag.accuracy, 75)
                     .addUserTag(SkillTag.mobile)
                     .setPriority(10)
                     .addEffect(builder.write(
                                     "You thrust your tongue into {other:possessive} ass and lick it, making her yelp in surprise.")
                                       .andNPCMessage("{self:subject} licks your tight asshole, both surprising and arousing you."))
                     .addEffectForOther(builder.pleasure("mouth", "ass")
                                               .addRoll(new BasicNumberRoll(10, 16)))
        // subresult for silvertongue
                     .createChildResult()
                     .setPriority(15)
                     .addRequirement(new TraitRequirement(Trait.silvertongue).toSkillRequirement())
                     .addEffect(builder.write(
                                     "You gently rim {other:name-possessive}'s asshole with your tongue, sending shivers through her body.")
                                       .andNPCMessage("{self:SUBJECT} gently rims your asshole with her tongue, sending shivers through your body."))
                     .addEffectForOther(builder.pleasure("mouth", "ass")
                                               .addRoll(new BasicNumberRoll(15, 22)));
        // facesit result
        defaultResult.createChildResult()
                     .addUserTag(Stance.facesitting.getSkillTag())
                     .addUserTag(SkillTag.perfectAccuracy)
                     .addUserTag(SkillTag.subPosition)
                     .addTargetTag(SkillTag.mojoBuilding)
                     .addEffect(builder.write(
                                     "With {other:name-possessive} ass pressing into your face, you helplessly give in and take an experimental lick at her pucker.")
                                     .andNPCMessage("With your ass pressing into {self:name-possessive} face, she helplessly gives in and starts licking your ass."))
                     .addEffect(builder.pleasure("mouth", "ass")
                                     .addRoll(new BasicNumberRoll(10, 16))
                                     .addRoll(new TraitBonusRoll(Trait.silvertongue, 5, 5)))
                     .addEffect(builder.buildMojo().addRoll(new ConstantRoll(10)))
                     .setPriority(20);
        defaultResult.createChildResult()
                     .addUserTag(SkillTag.worship)
                     .addUserTag(SkillTag.perfectAccuracy)
                     .addTargetTag(SkillTag.mojoBuilding)
                     .setPriority(30)
                     .addEffect(builder.write(
                                     "With a terrible need coursing through you, you lower your face between {other:name-possessive} "
                                     + "rear cheeks and plunge your tongue repeatedly in and out of {other:possessive} tightness."
                                                     + "You dimly realize that this is probably arousing you as much as {other:direct-object}, "
                                                     + "but worshipping {other:possessive} sublime derriere seems much higher on your priorities than winning.")
                                     .andNPCMessage("As if entranced, {other:subject} buries {other:possessive} face inside your ass cheeks, licking your crack, and worshipping your anus."))
                     .addEffect(builder.pleasure("mouth", "ass")
                                     .addRoll(new BasicNumberRoll(10, 16))
                                     .addRoll(new TraitBonusRoll(Trait.silvertongue, 5, 5)))
                     .addEffect(builder.tempt("ass").addRoll(new BasicNumberRoll(10, 16)))
                     .addEffect(builder.buildMojo().addRoll(new ConstantRoll(20)))
                     .setLabel("Ass Worship")
                     .setDescription("Worship your opponent's ass, may not be the best idea.");
    }
}
