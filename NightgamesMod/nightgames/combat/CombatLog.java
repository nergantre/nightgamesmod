package nightgames.combat;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.items.clothing.Clothing;
import nightgames.skills.Skill;
import nightgames.stance.Position;
import nightgames.status.Status;

class CombatLog {

    private final Combat cbt;
    private final Character p1, p2;
    private Writer writer;
    private Character last1, last2;
    private Position lastP;


    CombatLog(Combat cbt) {
        this.cbt = cbt;
        this.p1 = cbt.p1;
        this.p2 = cbt.p2;
        try {
            last1 = p1.clone();
            last2 = p2.clone();
            last1.finishClone(p1);
            last2.finishClone(p2);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        lastP = cbt.getStance();
        try {
            File dir = new File("combatlogs");
            if (!dir.isDirectory())
                dir.mkdir();
            File file = new File(String.format("combatlogs/%s VS %s - %d.log", p1.getName(), p2.getName(),
                            System.currentTimeMillis()));
            file.createNewFile();
            writer = Files.newBufferedWriter(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void logHeader(String linebreak) {
        StringBuilder sb = new StringBuilder("Combat Log - ");
        sb.append(p1.getName())
          .append(" versus ")
          .append(p2.getName())
          .append(linebreak);
        describeForHeader(p1, p2, sb, linebreak);
        describeForHeader(p2, p1, sb, linebreak);
        sb.append("____________________________").append(linebreak).append(linebreak);
        try {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void logTurn(Skill p1act, Skill p2act) {
        try {
            writer.write(logTurnToString(p1act, p2act, "\n"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    String logTurnToString(Skill p1act, Skill p2act, String linebreak) {
        StringBuilder sb = new StringBuilder(String.format("Turn %d:%s", cbt.timer, linebreak));
        sb.append(String.format("%s: %s%s%s: %s%s", p1.getName(), p1act.getName(),
                        linebreak, p2.getName(), p2act.getName(), linebreak));
        useSkills(p1act, p2act, sb, linebreak);
        sb.append(p1.getName())
          .append(": ");
        describeChanges(p1, last1, sb);
        sb.append(linebreak)
          .append(p2.getName())
          .append(": ");
        describeChanges(p2, last2, sb);
        describePositionChange(cbt.getStance(), lastP, sb, linebreak);
        sb.append(linebreak).append("____________________________").append(linebreak).append(linebreak);
        
        try {
            last1 = p1.clone();
            last2 = p2.clone();
            last1.finishClone(p1);
            last2.finishClone(p2);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        lastP = cbt.getStance();
        return sb.toString();
    }
    
    void logEnd(Optional<Character> winner) {
        StringBuilder sb = new StringBuilder("\nMATCH OVER: ");
        if (winner.isPresent()) {
            sb.append(winner.get()
                            .getName())
              .append(" WINS");
        } else {
            sb.append("DRAW");
        }
        try {
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useSkills(Skill p1act, Skill p2act, StringBuilder sb, String linebreak) {
        Skill firstSkill, secondSkill;
        Character firstCharacter, secondCharacter;
        if (p1.init() + p1act.speed() >= p2.init() + p2act.speed()) {
            firstSkill = p1act;
            secondSkill = p2act;
            firstCharacter = p1;
            secondCharacter = p2;
        } else {
            firstSkill = p2act;
            secondSkill = p1act;
            firstCharacter = p2;
            secondCharacter = p1;
        }
        boolean first, second = false;
        if (!(first = cbt.resolveSkill(firstSkill, secondCharacter))) {
            // only use second skill if an orgasm didn't happen
            cbt.write("<br>");
            second = cbt.resolveSkill(secondSkill, firstCharacter);
        }
        sb.append(String.format("%s went first: %s%s", firstCharacter.getName(), first ? "orgasm" : "normal", linebreak));
        sb.append(String.format("%s went second: %s%s", secondCharacter.getName(),
                        cbt.lastFailed || first ? "failed" : second ? "orgasm" : "normal", linebreak))
          .append(linebreak);
    }

    private void describePositionChange(Position current, Position last, StringBuilder sb, String linebreak) {
        if (!current.equals(last)) {
            sb.append(String.format("%sPosition changed: %s -> %s ", linebreak, last.getClass()
                                                                         .getSimpleName(),
                            current.getClass()
                                   .getSimpleName()));
            if (current.dom(p1)) {
                sb.append(p1.getName())
                  .append(" dominant");
            } else if (current.dom(p2)) {
                sb.append(p2.getName())
                  .append(" dominant");
            }
            sb.append(linebreak);
        }
    }

    private static void describeForHeader(Character c, Character other, StringBuilder sb, String linebreak) {
        sb.append(c.getName())
          .append(" at start:").append(linebreak);
        sb.append(c.att.toString());
        sb.append(c.traits.toString());
        sb.append(c.status.toString());
        sb.append(", ");
        c.body.describe(sb, other, " ", false);
        sb.append(" -- ");
        sb.append(c.outfit.describe(c));
        sb.append(linebreak);
    }

    private static void describeChanges(Character c, Character clone, StringBuilder sb) {
        describeConditionChange(c, clone, sb);
        describeTraitChange(c, clone, sb);
        describeStatusChange(c, clone, sb);
        describeAttributeChange(c, clone, sb);
        describeClothesChange(c, clone, sb);
        describeBodyChange(c, clone, sb);
    }

    private static void describeTraitChange(Character c, Character clone, StringBuilder sb) {
        List<Trait> current = c.traits;
        List<Trait> last = clone.traits;
        if (!current.equals(last)) {
            sb.append('[');
            current.stream()
                   .filter(t -> !last.contains(t))
                   .forEach(t -> sb.append('+')
                                   .append(t.name())
                                   .append(' '));
            last.stream()
                .filter(t -> !current.contains(t))
                .forEach(t -> sb.append('-')
                                .append(t.name())
                                .append(' '));
            sb.append(']');
        }
    }

    private static void describeBodyChange(Character c, Character clone, StringBuilder sb) {
        boolean didChange = describeBodyPartChange(c, clone, false, "cock", sb);
        didChange |= describeBodyPartChange(c, clone, didChange, "pussy", sb);
        didChange |= describeBodyPartChange(c, clone, didChange, "breasts", sb);
        didChange |= describeBodyPartChange(c, clone, didChange, "wings", sb);
        didChange |= describeBodyPartChange(c, clone, didChange, "tail", sb);
        if (didChange) {
            sb.append(']');
        }
    }

    private static boolean describeBodyPartChange(Character c, Character clone, boolean otherChanges, String part,
                    StringBuilder sb) {
        if (!c.body.get(part)
                   .equals(clone.body.get(part))) {
            sb.append(otherChanges ? ',' : '[');
            sb.append(clone.body.has(part) ? clone.body.getRandom(part)
                                                       .describe(clone)
                            : "none");
            sb.append("->");
            sb.append(c.body.has(part) ? c.body.getRandom(part)
                                               .describe(c)
                            : part);
            return true;
        }
        return false;
    }

    private static void describeStatusChange(Character c, Character clone, StringBuilder sb) {
        Set<Status> current = new HashSet<>(c.status);
        Set<Status> last = new HashSet<>(clone.status);
        Set<Status> modified = current.stream()
                                      .filter(s -> last.stream()
                                                       .anyMatch(s2 -> s2.name.equals(s.name)))
                                      .collect(Collectors.toSet());
        
        current.removeIf(s -> modified.stream()
                                      .anyMatch(s2 -> s2.name.equals(s.name)));
        last.removeIf(s -> modified.stream()
                                   .anyMatch(s2 -> s2.name.equals(s.name)));
        
        if (!current.equals(last) || !modified.isEmpty()) {
            sb.append('[');
            modified.forEach(s -> sb.append('%')
                                    .append(s.name)
                                    .append(' '));
            current.stream()
                   .filter(t -> !last.contains(t))
                   .forEach(t -> sb.append('+')
                                   .append(t.name)
                                   .append(' '));
            last.stream()
                .filter(t -> !current.contains(t))
                .forEach(t -> sb.append('-')
                                .append(t.name)
                                .append(' '));
            sb.append(']');
        }
    }

    private static void describeConditionChange(Character c, Character clone, StringBuilder sb) {
        int staDiff = c.getStamina()
                       .get()
                        - clone.getStamina()
                               .get();
        int arDiff = c.getArousal()
                      .get()
                        - clone.getArousal()
                               .get();
        int mojoDiff = c.getMojo()
                        .get()
                        - clone.getMojo()
                               .get();
        int willDiff = c.getWillpower()
                        .get()
                        - clone.getWillpower()
                               .get();
        sb.append('[');
        if (staDiff != 0) {
            sb.append(String.format(" %s:%s%d ", "STA", staDiff > 0 ? "+" : "", staDiff));
        }
        if (arDiff != 0) {
            sb.append(String.format(" %s:%s%d ", "AR", arDiff > 0 ? "+" : "", arDiff));
        }
        if (mojoDiff != 0) {
            sb.append(String.format(" %s:%s%d ", "MOJO", mojoDiff > 0 ? "+" : "", mojoDiff));
        }
        if (willDiff != 0) {
            sb.append(String.format(" %s:%s%d ", "WILL", willDiff > 0 ? "+" : "", willDiff));
        }
        sb.append(']');
    }

    private static void describeAttributeChange(Character c, Character clone, StringBuilder sb) {
        if (!c.att.equals(clone.att)) {
            sb.append('[');
            sb.append(c.att.keySet()
                           .stream()
                           .filter(a -> clone.att.get(a)
                                                 .intValue() != c.att.get(a)
                                                                     .intValue())
                           .map(a -> String.format("%s:%d", a.name(), clone.att.get(a) - c.att.get(a)))
                           .collect(Collectors.joining(",")));
            sb.append(']');
        }
    }

    private static void describeClothesChange(Character c, Character clone, StringBuilder sb) {
        List<Clothing> current = c.outfit.getAllStrippable();
        List<Clothing> last = clone.outfit.getAllStrippable();
        if (!current.equals(last)) {
            sb.append('[');
            current.stream()
                   .filter(t -> !last.contains(t))
                   .forEach(t -> sb.append('+')
                                   .append(t.getName())
                                   .append(' '));
            last.stream()
                .filter(t -> !current.contains(t))
                .forEach(t -> sb.append('-')
                                .append(t.getName())
                                .append(' '));
            sb.append(']');
        }
    }
}
