package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request checking currently scheduled billboard and the server get sessiontoken from client
 */
public class ViewerRequest implements Serializable {
    private String request;

    /**
     * Constructor setting string to server
     * @param request
     */
    public ViewerRequest(String request) {
        this.request = request;
    }

    /**
     * @return request
     */
    public String getRequest() {
        return request;
    }

}

