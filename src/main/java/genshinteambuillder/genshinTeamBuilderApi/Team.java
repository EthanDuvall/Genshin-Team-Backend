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

    public void addMember(String element, Character character) {
        members.put(element, character);

    }

    public boolean hasMember(Character character) {
        return members.containsValue(character);
    }

}
