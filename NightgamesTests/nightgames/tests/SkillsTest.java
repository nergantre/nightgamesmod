package nightgames.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonParseException;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.NPC;
import nightgames.characters.Personality;
import nightgames.characters.custom.CustomNPC;
import nightgames.characters.custom.JsonSourceNPCDataLoader;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.stance.Anal;
import nightgames.stance.AnalCowgirl;
import nightgames.stance.AnalProne;
import nightgames.stance.Behind;
import nightgames.stance.BehindFootjob;
import nightgames.stance.CoiledSex;
import nightgames.stance.Cowgirl;
import nightgames.stance.Doggy;
import nightgames.stance.Engulfed;
import nightgames.stance.FaceSitting;
import nightgames.stance.FlowerSex;
import nightgames.stance.FlyingCarry;
import nightgames.stance.FlyingCowgirl;
import nightgames.stance.HeldOral;
import nightgames.stance.Jumped;
import nightgames.stance.Missionary;
import nightgames.stance.Mount;
import nightgames.stance.Neutral;
import nightgames.stance.NursingHold;
import nightgames.stance.Pin;
import nightgames.stance.Position;
import nightgames.stance.ReverseCowgirl;
import nightgames.stance.ReverseMount;
import nightgames.stance.SixNine;
import nightgames.stance.Standing;
import nightgames.stance.StandingOver;
import nightgames.stance.TribadismStance;
import nightgames.stance.UpsideDownFemdom;
import nightgames.stance.UpsideDownMaledom;

public class SkillsTest {
	List<Personality> npcs1;
	List<Personality> npcs2;
	List<Position> stances;
	Area area;

	@Before
	public void prepare() throws JsonParseException, IOException {
		new Global(true);
		Global.newGame("Dummy", Optional.empty(), Collections.emptyList(),
	                    CharacterSex.male, Collections.emptyMap());
		npcs1 = new ArrayList<Personality>();
		npcs2 = new ArrayList<Personality>();
		try {
			npcs1.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("hermtestnpc.js"))));
			npcs1.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("femaletestnpc.js"))));
			npcs1.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("maletestnpc.js"))));
			npcs1.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("asextestnpc.js"))));
			npcs1.forEach(npc -> npc.getCharacter().setFakeHuman(true));

			npcs2.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("hermtestnpc.js"))));
			npcs2.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("femaletestnpc.js"))));
			npcs2.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("maletestnpc.js"))));
			npcs2.add(new CustomNPC(JsonSourceNPCDataLoader.load(SkillsTest.class.getResourceAsStream("asextestnpc.js"))));
		} catch (JsonParseException e) {
			e.printStackTrace();
			Assert.fail();
		}
		area = new Area("Test Area","Area for testing", Movement.quad);
		stances = new ArrayList<Position>();
		stances.add(new Anal(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new AnalCowgirl(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new AnalProne(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Behind(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new BehindFootjob(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new CoiledSex(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Cowgirl(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Doggy(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Engulfed(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new FaceSitting(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new FlowerSex(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new FlyingCarry(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new FlyingCowgirl(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new HeldOral(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Jumped(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Missionary(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Mount(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Neutral(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new NursingHold(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Pin(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new ReverseCowgirl(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new ReverseMount(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new SixNine(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new Standing(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new StandingOver(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new TribadismStance(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new UpsideDownFemdom(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
		stances.add(new UpsideDownMaledom(npcs1.get(0).getCharacter(), npcs1.get(1).getCharacter()));
	}

	public void testSkill(Character npc1, Character npc2, Position pos) throws CloneNotSupportedException {
		Combat c = new Combat(npc1, npc2, area, pos);
		pos.checkOngoing(c);
		if (c.getStance() == pos) {
			for (Skill skill : Global.getSkillPool()) {
				Combat cloned = c.clone();
				Skill used = skill.copy(cloned.p1);
				if (Skill.skillIsUsable(cloned, used)) {
					System.out.println("["+cloned.getStance().getClass().getSimpleName()+"] Skill usable: " + used.getLabel(cloned) + ".");
					used.resolve(cloned, cloned.p2);
				}
			}
		} else {
			System.out.println("STANCE NOT EFFECTIVE: " + pos.getClass().getSimpleName() + " with top: " + pos.top.name() + " and bottom: " + pos.bottom.getName());
		}
	}

	// TODO: May need to clone npc1 and npc2 here too, depending on how skills affect characters.
	public void testCombo(Character npc1, Character npc2, Position pos) throws CloneNotSupportedException {
		pos.top = npc1;
		pos.bottom = npc2;
		testSkill(npc1, npc2, pos);
		testSkill(npc2, npc1, pos);
	}

	@Test
	public void test() throws CloneNotSupportedException {
		for (int i = 0; i < npcs1.size(); i++) {
			for (int j = 0; j < npcs2.size(); j++) {
				System.out.println("i = " + i + ", j = " + j);
				for (Position pos : stances) {
					NPC npc1 = npcs1.get(i).getCharacter();
					NPC npc2 = npcs2.get(j).getCharacter();
					System.out.println("Testing [" + i + "]: " + npc1.getName() + " with [" + j + "]: " + npc2.getName() + " in Stance " + pos.getClass().getSimpleName());
					testCombo(npc1.clone(), npc2.clone(), pos);
					System.out.println("Testing [" + j + "]: " + npc2.getName() + " with [" + i + "]: " + npc1.getName() + " in Stance " + pos.getClass().getSimpleName());
					testCombo(npc2.clone(), npc1.clone(), pos);
				}
			}
		}
		System.out.println("test " + Global.random(100000) + " done");
	}
}
