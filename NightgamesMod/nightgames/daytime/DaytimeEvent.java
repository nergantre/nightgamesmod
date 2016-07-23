package nightgames.daytime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nightgames.global.Global;
import nightgames.global.Scene;
import nightgames.characters.Character;

abstract class DaytimeEvent implements Scene {

    protected Character player;
    private List<EventVariation> scenes;
    
    protected DaytimeEvent(Character player) {
        this.player = player;
        scenes = new ArrayList<>();
    }
    
    protected void register(String name, int priority) {
        scenes.add(new EventVariation(name, priority));
    }

    // Mandatory and morning scenes do not trigger if an event listed higher in 
    // DaytimeEventManager.EVENT_TYPES already did.
    
    public boolean playMorning() {
        return playIfPresent(getMorningReason());        
    }
    
    public boolean playMandatory() {
        return playIfPresent(getMandatoryReason());
    }
    
    private boolean playIfPresent(Optional<String> reason) {
        if (reason.isPresent()) {
            runScene(reason.get());
            return true;
        }
        return false;
    }
    
    public boolean playAny() {
        for (EventVariation scene : scenes) {
            if (Global.random(100) < scene.priority) {
                runScene(scene.getName());
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final void respond(String response) {
        Global.getDay().advance(1);
        Global.getDay().plan();
    }
    
    private void runScene(String scene) {
        play(scene);
        Global.current = this;
        Global.gui().choose("Next");
    }
    
    abstract boolean available();
    
    protected abstract Optional<String> getMandatoryReason();
    protected abstract Optional<String> getMorningReason();
    protected abstract void play(String response);

    
    protected class EventVariation {
        private String name;
        private int priority;
        protected EventVariation(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public void setPriority(int priority) {
            this.priority = priority;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + priority;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EventVariation other = (EventVariation) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (priority != other.priority)
                return false;
            return true;
        }

        private DaytimeEvent getOuterType() {
            return DaytimeEvent.this;
        }

        @Override
        public String toString() {
            return "EventVariation [name=" + name + ", priority=" + priority + "]";
        }
        
        
    }
}
