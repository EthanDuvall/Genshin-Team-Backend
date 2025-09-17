package genshinteambuillder.genshinTeamBuilderApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class JsonDatabaseService {
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Character> getCharacters() {
        try {
            InputStream inputStream = JsonDatabaseService.class
                    .getResourceAsStream("/characters.json");
            return mapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to read characters.json", e);
        }
    }

    public List<Object> generateTeams(int id) {
        List<Object> possibleTeams;

        try {
            InputStream inputStream = JsonDatabaseService.class
                    .getResourceAsStream("/reactions.json");
            possibleTeams = mapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to read reactions.json", e);
        }
        
        return possibleTeams;
    }

}
