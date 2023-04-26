package helpdesk_ticketing_system.hts_status_update_svc;

import com.amazonaws.services.lambda.runtime.Context;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDb {
    private final MongoCollection<Document> issuesCollection;
    private final MongoCollection<Document> ticketsCollection;
    private final String status;

    public MongoDb() {
        String connectionUri = System.getenv("mongodb_connection_uri");
        String username = System.getenv("mongodb_username");
        String password = System.getenv("mongodb_password");
        String database = System.getenv("mongodb_database");
        String issuesCollectionName = System.getenv("mongodb_collection_issues");
        String ticketsCollectionName = System.getenv("mongodb_collection_tickets");
        status = System.getenv("status_to_set");

        String connectionString = String.format(connectionUri, username, password);
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
                        .applyConnectionString(new ConnectionString(connectionString))
                        .build()
        );
        issuesCollection = mongoClient.getDatabase(database).getCollection(issuesCollectionName);
        ticketsCollection = mongoClient.getDatabase(database).getCollection(ticketsCollectionName);
    }

    boolean updateIssueStatus(Object id, Context context){
        return updateStatus(issuesCollection,id,context);
    }

    boolean updateTicketStatus(Object id, Context context){
        return updateStatus(ticketsCollection,id,context);
    }

    private boolean updateStatus(MongoCollection<Document> collection, Object id, Context context){
        Bson updateRequest = Updates.set("status",status);
        try
        {
            UpdateResult updateResult = collection.updateOne(Filters.eq("_id",id),updateRequest);
            if(updateResult.wasAcknowledged())
                return true;
        }catch (Exception e){
            context.getLogger().log("Exception class : " + e.getClass().getName() + "\n");
            context.getLogger().log("Exception Message : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
        return false;
    }
}
