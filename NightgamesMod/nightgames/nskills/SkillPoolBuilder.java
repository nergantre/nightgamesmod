package nightgames.nskills;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.nskills.effects.BasicNumberRoll;
import nightgames.nskills.effects.PleasureSkillEffect;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;

public class SkillPoolBuilder {
    List<SkillInterface> skills;

    public SkillPoolBuilder() {
        skills = new ArrayList<>();
    }

    public void buildSkillPool() {
        GenericSkill anilingus = new GenericSkill("Anilingus");
        SkillResult defaultResult = new SkillResult().addUserTag(SkillTag.anal)
                                                     .addUserTag(Attribute.Seduction.getSkillTag(), 30)
                                                     .addUserTag(SkillTag.usesMouth)
                                                     .addUserTag(SkillTag.oral)
                                                     .addUserTag(SkillTag.reachLowerBody)
                                                     .addUserTag(SkillTag.contact)
                                                     .addTargetTag(SkillTag.usesAss)
                                                     .addTargetTag(SkillTag.pleasure)
                                                     .setLabel("Anilingus");
        defaultResult.createChildResult()
                     .addUserTag(SkillTag.miss)
                     .setSuccess(false)
                     .setPriority(0);
        defaultResult.createChildResult()
                     .addUserTag(SkillTag.accuracy, 70)
                     .addUserTag(SkillTag.mobile)
                     .setPriority(10)
                     .addTargetEffect(new PleasureSkillEffect("ass", "mouth").addRoll(new BasicNumberRoll(10, 16)));
        defaultResult.createChildResult()
                     .addUserTag(Stance.facesitting.getSkillTag())
                     .addUserTag(SkillTag.perfectAccuracy)
                     .addUserTag(SkillTag.subPosition)
                     .addTargetTag(SkillTag.mojoBuilding)
                     .setPriority(20);
        defaultResult.createChildResult()
                     .addUserTag(SkillTag.worship)
                     .addUserTag(SkillTag.perfectAccuracy)
                     .addTargetTag(SkillTag.mojoBuilding)
                     .setPriority(30)
                     .setLabel("Ass Worship");

        skills.add(anilingus);
    }
}
