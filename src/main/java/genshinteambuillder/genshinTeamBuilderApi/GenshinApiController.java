package genshinteambuillder.genshinTeamBuilderApi;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/genshinBuilder")
public class GenshinApiController {

    private final JsonDatabaseService db;

    // Inject service (Spring gives us an instance automatically)
    public GenshinApiController(JsonDatabaseService db) {
        this.db = db;
    }

    // GET /genshinBuilder/characters
    @GetMapping("/characters")
    public HashMap<String, Character> getAllCharacters() {
        return db.getCharacters();
    }

    // GET /genshinBuilder/teams?id=1
    @GetMapping("/teams")
    public List<Team> generateTeams(@RequestParam Integer id) {
        return db.generateTeams(id);
    }

    // POST /genshinBuilder/owned
    @PostMapping("/owned")
    public List<Integer> postOwnedCharacters(@RequestBody List<Integer> ownedCharacters) {
        try {
            db.saveOwnedCharacters(ownedCharacters);
            return ownedCharacters;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save owned characters");
        }
    }

}
