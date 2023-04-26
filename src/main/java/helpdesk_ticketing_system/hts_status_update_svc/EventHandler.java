package helpdesk_ticketing_system.hts_status_update_svc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.net.HttpURLConnection;
import java.util.Map;

public class EventHandler implements RequestHandler<Map<String,Object>,Object> {
    @Override
    public Object handleRequest(Map<String, Object> inputEvent, Context context) {
        String event = String.valueOf(inputEvent.get("event"));
        if(event.equalsIgnoreCase("SOLUTION_POSTED")){
            String issueId = String.valueOf(inputEvent.get("issue_id"));
            String ticketId = String.valueOf(inputEvent.get("ticket_id"));

            context.getLogger().log("issue id : " + issueId+"\n");
            context.getLogger().log("ticket id : " + ticketId+"\n");

            return HttpURLConnection.HTTP_OK;
        }
        return HttpURLConnection.HTTP_NOT_ACCEPTABLE;
    }
}
