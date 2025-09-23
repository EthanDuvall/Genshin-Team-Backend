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

            for (String role : coreCharacter.getRoles()) {

                Team currentTeam = new Team(reaction);
                int teamSize = 0;
                boolean hasMainDps = role.equals("mainDps");
                boolean hasSustain = role.equals("sustain");

                if (elementsInReaction.size() == 2 && elementsInReaction.contains("Flex")) {
                    String mainElement = elementsInReaction.stream()
                            .filter(e -> !e.equals("Flex"))
                            .findFirst()
                            .orElse(coreCharacter.getElement());

                    int added = 0;
                    List<Integer> mainElementList = currentReaction.get(mainElement);
                    if (mainElementList != null) {
                        for (int id : mainElementList) {
                            if (!ownedCharacters.contains(id)) continue;
                            Character candidate = Character.getCharacter(id);
                            if (currentTeam.hasMember(candidate)) continue;
                            currentTeam.addMember(candidate);
                            added++;
                            teamSize++;
                            if (added == 2) break;
                        }
                    }

                    boolean flexAdded = false;
                    List<Integer> flexList = currentReaction.get("Flex");
                    if (flexList != null) {
                        for (int id : flexList) {
                            if (!ownedCharacters.contains(id)) continue;
                            Character candidate = Character.getCharacter(id);
                            if (currentTeam.hasMember(candidate)) continue;
                            currentTeam.addMember(candidate);
                            teamSize++;
                            flexAdded = true;
                            break;
                        }
                    }

                    if (!flexAdded && teamSize < 3 && mainElementList != null) {
                        for (int id : mainElementList) {
                            if (!ownedCharacters.contains(id)) continue;
                            Character candidate = Character.getCharacter(id);
                            if (currentTeam.hasMember(candidate)) continue;
                            currentTeam.addMember(candidate);
                            teamSize++;
                            break;
                        }
                    }

                } else {
                    while (teamSize < 3) {
                        boolean addedThisRound = false;
                        for (String element : elementsInReaction) {
                            if (teamSize >= 3) break;
                            List<Integer> rankedCandidates = currentReaction.get(element);
                            if (rankedCandidates == null) continue;

                            for (int id : rankedCandidates) {
                                if (!ownedCharacters.contains(id)) continue;
                                Character candidate = Character.getCharacter(id);
                                if (currentTeam.hasMember(candidate)) continue;
                                List<String> candidateRoles = candidate.getRoles();
                                if (candidateRoles.contains("mainDps") && hasMainDps) continue;
                                if (candidateRoles.contains("sustain") && hasSustain) continue;

                                if (candidateRoles.contains("mainDps")) hasMainDps = true;
                                if (candidateRoles.contains("sustain")) hasSustain = true;

                                currentTeam.addMember(candidate);
                                teamSize++;
                                addedThisRound = true;
                                break;
                            }
                        }
                        if (!addedThisRound) break; // avoid infinite loop if no candidates
                    }
                }

                generatedTeams.add(currentTeam);
            }
        }

        return generatedTeams;
    }
}
