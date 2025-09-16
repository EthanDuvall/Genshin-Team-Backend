package genshinteambuillder.genshinTeamBuilderApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Character> getAllCharacters() {
        return db.getCharacters();
    }
/*
    // GET /genshinBuilder/teams?id=1
    @GetMapping("/teams")
    public List<Object> generateTeams(@RequestParam Integer id) {
        return db.generateTeams(id);
    }

    // POST /genshinBuilder/owned
    @PostMapping("/owned")
    public List<Integer> postOwnedCharacters(@RequestBody List<Integer> ownedCharacters) {
        db.saveOwnedCharacters(ownedCharacters);
        return ownedCharacters;
    }
    */
}
