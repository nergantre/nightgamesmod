package characters;

import skills.Skill;

public class WeightedSkill implements Comparable<WeightedSkill> {
	public float weight;
	public float rating;
	public float raw_rating;
	public Skill skill;
	
	public WeightedSkill(float weight, float raw_rating, float rating, Skill skill) {
		this.weight = weight;
		this.skill = skill;
		this.rating = rating;
		this.raw_rating = raw_rating;
	}

	public WeightedSkill(float weight, Skill skill) {
		this(weight, 0.f, 0.f, skill);
	}

	@Override
	public int compareTo(WeightedSkill obj) {
		if (weight < ((WeightedSkill) obj).weight) {
			return -1;
		} else if (weight > ((WeightedSkill) obj).weight) {
			return 1;
		} else {
			return 0;
		}
	}
}
