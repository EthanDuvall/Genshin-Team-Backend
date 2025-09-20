package genshinteambuillder.genshinTeamBuilderApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class JsonDatabaseService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String FILE_PATH = "ownedCharacters.json";
    private static final String OWNED_CHAR_FILE = "ownedCharacters.json";

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

    public List<Team> generateTeams(int id) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            List<Integer> ownedChar = mapper.readValue(
                    new File("ownedCharacters.json"),
                    new TypeReference<List<Integer>>() {
                    }
            );

            if (ownedChar.size() > 0) {
                return TeamBuilder.buildTeam(id, ownedChar);
            } else {
                throw new RuntimeException("No owned characters");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void saveOwnedCharacters(List<Integer> ownedCharacters) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(OWNED_CHAR_FILE), ownedCharacters);
            System.out.println("Owned characters saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save owned characters");
        }
    }

}

