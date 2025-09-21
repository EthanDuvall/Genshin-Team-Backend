package genshinteambuillder.genshinTeamBuilderApi;

import java.util.ArrayList;

public class Team {
    private final String reaction;
    private final ArrayList<Character> members;

    public Team(String reaction) {
        this.reaction = reaction;
        this.members = new ArrayList<Character>();
    }

    public void addMember(Character character) {
        members.add(character);
    }

    public boolean hasMember(Character character) {
        return members.stream()
                .anyMatch(c -> c.getId() == character.getId());
    }


    public ArrayList<Character> getMembers() {
        return members;
    }

    public String getReaction() {
        return reaction;
    }
}
