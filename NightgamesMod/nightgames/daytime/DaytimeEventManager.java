package nightgames.daytime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import nightgames.characters.Character;

final class DaytimeEventManager {

    private static final List<Function<Character, DaytimeEvent>> EVENT_TYPES;

    static {
        EVENT_TYPES = new ArrayList<>();
        EVENT_TYPES.add(Threesomes::new);
        
        /*
         * The order of events is important: later events can only trigger if earlier ones fail to.
         * Use events sparingly: they block regular daytime actions if triggered.
         */
    }

    private List<DaytimeEvent> events;

    DaytimeEventManager(Character player) {
        events = EVENT_TYPES.stream()
                            .map(f -> f.apply(player))
                            .collect(Collectors.toList());
    }

    boolean playMorningScene() {
        return events.stream()
                     .anyMatch(DaytimeEvent::playMorning);
    }

    boolean playRegularScene() {
        if (events.stream()
                  .anyMatch(DaytimeEvent::playMandatory))
            return true;
        for (DaytimeEvent evt : events) {
            if (evt.available() && evt.playAny())
                return true;
        }
        return false;
    }
}
