package genshinteambuillder.genshinTeamBuilderApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Map;

public class Character {
    private int id;
    private String name;
    private String element;
    private List<String> roles;
    private String rarity;
    private String icon;

    public static Character getCharacter(int id) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("characters.json");
            Map<String, Character> characters = mapper.readValue(
                    resource.getInputStream(),
                    mapper.getTypeFactory().constructMapType(Map.class, String.class, Character.class)
            );

            return characters.get(String.valueOf(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getElement() {
        return element;
    }

    public List<String> getRoles() {
        return roles;
    }

    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }
}
