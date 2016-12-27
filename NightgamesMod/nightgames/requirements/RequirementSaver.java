package nightgames.requirements;

public interface RequirementSaver<T> {

    SavedRequirement<T> saveRequirement(Requirement req);
    
    static class SavedRequirement<T> {
        public T data;
        public String key;
        
        public SavedRequirement() {
            
        }
        
        public SavedRequirement(T data, String key) {
            this.data = data;
            this.key = key;
        }
    }
}
