package setup;


import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import main.CrusadeBot;
import main.Utils;

public class Config {

    public String token;
    public String ownerId;
    public Guild emoteServer;

    public Config() throws Exception {
        File file = new File(Utils.getJarContainingFolder(CrusadeBot.class) + "/config.json");

        if (file.exists()) {
            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(new FileReader(file));
            token = (String) config.get("token");
            ownerId = (String) config.get("ownerId");
        } else {
            FileWriter fileWriter = new FileWriter(file);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", null);
            jsonObject.put("ownerId", null);

            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();

            throw new Exception();
        }
    }
}
