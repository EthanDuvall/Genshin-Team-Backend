package genshinteambuillder.genshinTeamBuilderApi;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "https://genshin-team-builder-three.vercel.app"})

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
    public List<Team> generateTeams(@RequestBody Map<String, Object> body) throws IOException {
        Integer coreId = (Integer) body.get("coreId");
        List<Integer> ownedCharacters = (List<Integer>) body.get("ownedCharacters");
        return db.generateTeams(coreId, ownedCharacters);
    }

}
