package genshinteambuillder.genshinTeamBuilderApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class JsonDatabaseService {
    private static final ObjectMapper mapper = new ObjectMapper();

    public HashMap<String, Character> getCharacters() {
        try {
            InputStream inputStream = JsonDatabaseService.class
                    .getResourceAsStream("/characters.json");
            return mapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to read characters.json", e);
        }
    }

    public List<Team> generateTeams(int id, List<Integer> ownedCharacters) throws IOException {
        List<Integer> ownedChar = ownedCharacters;
        return TeamBuilder.buildTeam(id, ownedChar);


    }

}

