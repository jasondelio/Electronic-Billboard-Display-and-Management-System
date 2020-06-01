package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class ViewerRequest implements Serializable {
    private String request;

    public ViewerRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

}

