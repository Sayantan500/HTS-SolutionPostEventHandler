package helpdesk_ticketing_system.hts_status_update_svc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.net.HttpURLConnection;
import java.util.Map;

public class EventHandler implements RequestHandler<Map<String,Object>,Object> {
    private final MongoDb mongoDb;

    public EventHandler() {
        mongoDb = new MongoDb();
    }

    @Override
    public Object handleRequest(Map<String, Object> inputEvent, Context context) {
        String event = String.valueOf(inputEvent.get("event"));
        if(event.equalsIgnoreCase("SOLUTION_POSTED")){
            String issueId = String.valueOf(inputEvent.get("issue_id"));
            String ticketId = String.valueOf(inputEvent.get("ticket_id"));

            context.getLogger().log("issue id : " + issueId+"\n");
            context.getLogger().log("ticket id : " + ticketId+"\n");

            // updating the status of issue and ticket
            if(mongoDb.updateIssueStatus(issueId,context) && mongoDb.updateTicketStatus(ticketId,context))
                return HttpURLConnection.HTTP_OK;
            else
                return HttpURLConnection.HTTP_INTERNAL_ERROR;
        }
        return HttpURLConnection.HTTP_NOT_ACCEPTABLE;
    }
}
