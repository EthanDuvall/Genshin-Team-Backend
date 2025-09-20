package genshinteambuillder.genshinTeamBuilderApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
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

        File elementsFile = new ClassPathResource("elements.json").getFile();
        HashMap<String, List<String>> elementReaction = mapper.readValue(
                elementsFile,
                new TypeReference<HashMap<String, List<String>>>() {
                }
        );

        File reactionsFile = new ClassPathResource("reactions.json").getFile();
        HashMap<String, HashMap<String, List<Integer>>> allReactions = mapper.readValue(
                reactionsFile,
                new TypeReference<HashMap<String, HashMap<String, List<Integer>>>>() {
                }
        );

        List<String> possibleReactions = elementReaction.get(coreCharacter.getElement());
        for (String reaction : possibleReactions) {
            HashMap<String, List<Integer>> currentReaction = allReactions.get(reaction);
            System.out.println(currentReaction + "current reaction");
            System.out.println(coreCharacter + "core");
            if (!currentReaction.get(coreCharacter.getElement()).contains(coreId)) continue;
            Set<String> elementsInReaction = currentReaction.keySet();

            for (String role : coreCharacter.getRoles()) {

                Team currentTeam = new Team(reaction);
                currentTeam.addMember(coreCharacter.getElement(), coreCharacter);

                int teamSize = 1;
                boolean hasMainDps = role.equals("mainDps");
                boolean hasSustain = role.equals("sustain");

                while (teamSize < 4) {
                    for (String element : elementsInReaction) {
                        if (teamSize >= 4) break;

                        int spotsLeft = 4 - teamSize;
                        int remainingElements = elementsInReaction.size();

                        int maxRoll;
                        if (remainingElements < spotsLeft) {
                            maxRoll = spotsLeft;
                        } else {
                            if (element.equals(coreCharacter.getElement())) {
                                maxRoll = 1;
                            } else {
                                maxRoll = 2;
                            }
                        }

                        int rollCharacters = (int) (Math.random() * (maxRoll + 1));

                        if (elementsInReaction.size() == 1) rollCharacters = spotsLeft;

                        while (rollCharacters > 0 && teamSize < 4) {
                            List<Integer> rankedCandidates = currentReaction.get(element);
                            for (int id : rankedCandidates) {
                                if (ownedCharacters.contains(id)) {

                                    Character candidate = Character.getCharacter(id);
                                    List<String> candidateRoles = candidate.getRoles();
                                    System.out.println(candidate);
                                    if (currentTeam.hasMember(candidate)) {
                                        continue;
                                    }
                                    if (candidateRoles.size() == 1 && candidateRoles.contains("mainDps") && hasMainDps) {
                                        continue;
                                    }
                                    if (candidateRoles.size() == 1 && candidateRoles.contains("sustain") && hasSustain) {
                                        continue;
                                    }

                                    if (candidateRoles.contains("mainDps")) hasMainDps = true;
                                    if (candidateRoles.contains("sustain")) hasSustain = true;

                                    currentTeam.addMember(element, candidate);
                                    teamSize++;
                                    rollCharacters--;
                                    break;
                                }
                            }
                        }
                    }
                }
                System.out.println(currentTeam);
                generatedTeams.add(currentTeam);
            }
        }

        return generatedTeams;
    }
}




