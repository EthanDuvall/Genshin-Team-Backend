package genshinteambuillder.genshinTeamBuilderApi;

import java.util.HashMap;
import java.util.Map;

public class Team {
    private final String reaction;
    private final Map<String, Character> members;

    public Team(String reaction) {
        this.reaction = reaction;
        this.members = new HashMap<>();
    }

    public void addMember(String slot, Character character) {
        members.put(slot, character);
    }

    public Map<String, Character> getMembers() {
        return members;
    }

    public String getReaction() {
        return reaction;
    }
}
