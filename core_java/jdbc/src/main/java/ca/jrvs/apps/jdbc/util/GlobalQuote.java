package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.Quote;
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
