package ca.jrvs.apps.trading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
public class GlobalQuote {
    @JsonProperty("Global Quote")
    private Quote globalQuote;
    public Quote getGlobalQuote() {
        return globalQuote;
    }
    public void setGlobalQuote() {
        this.globalQuote = globalQuote;
    }
}
