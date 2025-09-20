package genshinteambuillder.genshinTeamBuilderApi;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    // POST /genshinBuilder/generate/teams?id=1
    @PostMapping("/generate")
    public List<Team> generateTeams(
            @RequestBody Integer coreId,
            @RequestBody List<Integer> ownedCharacters
    ) throws IOException {
        return db.generateTeams(coreId, ownedCharacters);
    }


}
