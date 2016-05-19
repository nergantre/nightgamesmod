package nightgames.nskills.effects;

public class SkillEffectBuilder {
    public SeparatedMessageSkillEffect write(String message) {
        return new SeparatedMessageSkillEffect(message);
    }
    public PleasureSkillEffect pleasure(String withPart, String targetPart) {
        return new PleasureSkillEffect(withPart, targetPart);
    }
    public ConditionalSkillEffect doEffect(SkillEffect effect) {
        return new ConditionalSkillEffect(effect);
    }
    public BuildMojoSkillEffect buildMojo() {
        return new BuildMojoSkillEffect();
    }
    public TemptationSkillEffect tempt(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
