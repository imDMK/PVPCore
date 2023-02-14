package me.dmk.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.DatabaseConfiguration;
import org.bukkit.Bukkit;

/**
 * Created by DMK on 28.12.2022
 */

@RequiredArgsConstructor
public class MongoClientService {

    private final CorePlugin corePlugin;
    private final DatabaseConfiguration databaseConfiguration;

    @Getter
    private MongoClient mongoClient;
    @Getter
    private MongoDatabase mongoDatabase;

    public void connect() {
        boolean auth = this.databaseConfiguration.isAuthentication();
        String userName = this.databaseConfiguration.getUserName();
        String password = this.databaseConfiguration.getPassword();
        String hostName = this.databaseConfiguration.getHostName();
        int port = this.databaseConfiguration.getPort();
        String databaseName = this.databaseConfiguration.getDatabaseName();

        String connectUrl = "mongodb://" + (auth ? userName + ":" + password + "@" : "") + hostName + ":" + port + "/" + databaseName;

        try {
            this.mongoClient = new MongoClient(new MongoClientURI(connectUrl));
            this.mongoDatabase = this.mongoClient.getDatabase(databaseName);
        } catch (MongoException mongoException) {
            Bukkit.getPluginManager().disablePlugin(this.corePlugin);

            mongoException.printStackTrace();
        }
    }

    public void close() {
        this.mongoClient.close();
    }
}
