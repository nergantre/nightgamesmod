package nightgames.areas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import nightgames.actions.Movement;
import nightgames.characters.Character;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.trap.Trap;

public class Area implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1372128249588089014L;
    public String name;
    public HashSet<Area> adjacent;
    public HashSet<Area> shortcut;
    public ArrayList<Character> present;
    public String description;
    public IEncounter fight;
    public boolean alarm;
    public Trap trap;
    public ArrayList<Deployable> env;
    public MapDrawHint drawHint;
    private Movement enumerator;

    public Area(String name, String description, Movement enumerator) {
        this(name, description, enumerator, new MapDrawHint());
    }

    public Area(String name, String description, Movement enumerator, MapDrawHint drawHint) {
        this.name = name;
        this.description = description;
        this.enumerator = enumerator;
        adjacent = new HashSet<Area>();
        shortcut = new HashSet<Area>();
        present = new ArrayList<Character>();
        env = new ArrayList<Deployable>();
        alarm = false;
        trap = null;
        fight = null;
        this.drawHint = drawHint;
    }

    public void link(Area adj) {
        adjacent.add(adj);
    }

    public void shortcut(Area sc) {
        shortcut.add(sc);
    }

    public boolean open() {
        return enumerator == Movement.quad || enumerator == Movement.ftcCenter;
    }

    public boolean corridor() {
        return enumerator == Movement.bridge || enumerator == Movement.tunnel || enumerator == Movement.ftcTrail
                        || enumerator == Movement.ftcPass || enumerator == Movement.ftcPath;
    }

    public boolean materials() {
        return enumerator == Movement.workshop || enumerator == Movement.storage || enumerator == Movement.ftcCabin
                        || enumerator == Movement.ftcDump;
    }

    public boolean potions() {
        return enumerator == Movement.lab || enumerator == Movement.kitchen || enumerator == Movement.ftcLodge;
    }

    public boolean bath() {
        return enumerator == Movement.shower || enumerator == Movement.pool || enumerator == Movement.ftcPond
                        || enumerator == Movement.ftcWaterfall;
    }

    public boolean resupply() {
        return enumerator == Movement.dorm || enumerator == Movement.union;
    }

    public boolean recharge() {
        return enumerator == Movement.workshop || enumerator == Movement.ftcCabin;
    }

    public boolean mana() {
        return enumerator == Movement.la || enumerator == Movement.ftcOak;
    }

    public boolean ping(int perception) {
        if (fight != null) {
            return true;
        }
        for (Character c : present) {
            if (!c.stealthCheck(perception) || open()) {
                return true;
            }
        }
        return alarm;
    }

    public void enter(Character p) {
        present.add(p);
        Deployable found = getEnv();
        if (found != null) {
            found.resolve(p);
        }
    }

    public boolean encounter(Character p) {
        if (fight != null && fight.checkIntrude(p)) {
            p.intervene(fight, fight.getPlayer(1), fight.getPlayer(2));
        } else if (present.size() > 1) {
            for (Character opponent : Global.getMatch().combatants) {
                if (present.contains(opponent) && opponent != p) {
                    fight = Global.getMatch().getType().buildEncounter(p, opponent, this);
                    return fight.spotCheck();
                }
            }
        }
        return false;
    }

    public boolean opportunity(Character target, Trap trap) {
        if (present.size() > 1) {
            for (Character opponent : present) {
                if (opponent != target) {
                    if (target.eligible(opponent) && opponent.eligible(target) && fight == null) {
                        fight = Global.getMatch().getType().buildEncounter(opponent, target, this);
                        opponent.promptTrap(fight, target, trap);
                        return true;
                    }
                }
            }
        }
        remove(trap);
        return false;
    }

    public boolean humanPresent() {
        for (Character player : present) {
            if (player.human()) {
                return true;
            }
        }
        return false;
    }

    public void exit(Character p) {
        present.remove(p);
    }

    public void endEncounter() {
        fight = null;
    }

    public Movement id() {
        return enumerator;
    }

    public Deployable getEnv() {
        if (env.isEmpty()) {
            return null;
        }
        for (int i = 0; i < env.size(); i++) {
            if (env.get(i).getClass() == Trap.class) {
                return env.get(i);
            }
        }
        return env.get(0);
    }

    public void place(Deployable thing) {
        env.add(thing);
    }

    public void remove(Deployable triggered) {
        env.remove(triggered);
    }

    public Deployable get(Deployable type) {
        for (Deployable thing : env) {
            if (thing.getClass() == type.getClass()) {
                return thing;
            }
        }
        return null;
    }
}
