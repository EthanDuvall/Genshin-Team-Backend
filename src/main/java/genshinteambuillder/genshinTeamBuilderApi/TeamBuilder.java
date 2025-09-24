package genshinteambuillder.genshinTeamBuilderApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TeamBuilder {

    static List<Team> buildTeam(int coreId, List<Integer> ownedCharacters) throws IOException {
        List<Team> generatedTeams = new ArrayList<>();
        Character coreCharacter = Character.getCharacter(coreId);

        ObjectMapper mapper = new ObjectMapper();

        ClassPathResource elementsResource = new ClassPathResource("elements.json");
        HashMap<String, List<String>> elementReaction = mapper.readValue(
                elementsResource.getInputStream(),
                new TypeReference<HashMap<String, List<String>>>() {
                }
        );

        ClassPathResource reactionsResource = new ClassPathResource("reactions.json");
        HashMap<String, HashMap<String, List<Integer>>> allReactions = mapper.readValue(
                reactionsResource.getInputStream(),
                new TypeReference<HashMap<String, HashMap<String, List<Integer>>>>() {
                }
        );

        List<String> possibleReactions = elementReaction.get(coreCharacter.getElement());
        if (possibleReactions == null) return generatedTeams; // no reactions possible

        for (String reaction : possibleReactions) {
            HashMap<String, List<Integer>> currentReaction = allReactions.get(reaction);
            if (currentReaction == null) continue; // skip if reaction missing in JSON

            List<Integer> coreElementList = currentReaction.get(coreCharacter.getElement());
            if (coreElementList == null || !coreElementList.contains(coreId)) continue;

            Set<String> elementsInReaction = currentReaction.keySet();
            String role = coreCharacter.getRoles().isEmpty() ? "" : coreCharacter.getRoles().get(0);

            Team currentTeam = new Team(reaction);
            boolean hasMainDps = role.equals("mainDps");
            boolean hasSustain = role.equals("sustain");

            // Attempt to add one member per element first
            for (String element : elementsInReaction) {
                if (currentTeam.getMembers().size() >= 3) break;
                List<Integer> candidates = currentReaction.get(element);
                if (candidates == null) continue;

                for (int id : candidates) {
                    if (id == coreId) continue;
                    if (!ownedCharacters.contains(id)) continue;
                    Character candidate = Character.getCharacter(id);
                    if (currentTeam.hasMember(candidate)) continue;

                    List<String> candidateRoles = candidate.getRoles();
                    if (candidateRoles.contains("mainDps") && hasMainDps) continue;
                    if (candidateRoles.contains("sustain") && hasSustain) continue;

                    if (candidateRoles.contains("mainDps")) hasMainDps = true;
                    if (candidateRoles.contains("sustain")) hasSustain = true;

                    currentTeam.addMember(candidate);
                    break; // only one per element for now
                }
            }

            // If team has less than 3 members, fill with remaining candidates (lower-ranked or repeats)
            if (currentTeam.getMembers().size() < 3) {
                for (String element : elementsInReaction) {
                    if (currentTeam.getMembers().size() >= 3) break;
                    List<Integer> candidates = currentReaction.get(element);
                    if (candidates == null) continue;

                    for (int id : candidates) {
                        if (id == coreId) continue;
                        if (!ownedCharacters.contains(id)) continue;
                        Character candidate = Character.getCharacter(id);
                        if (currentTeam.hasMember(candidate)) continue;

                        currentTeam.addMember(candidate);
                        if (currentTeam.getMembers().size() >= 3) break;
                    }
                }
            }

            // Only add complete teams
            if (currentTeam.getMembers().size() == 3) {
                generatedTeams.add(currentTeam);
            }
        }

        return generatedTeams;
    }
}