package com.gmail.mooman219.richmessage;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ClickEvent extends JsonData {

    protected String action;
    protected String value;

    protected ClickEvent() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void write(JsonGenerator g) throws IOException {
        g.writeStringField("action", this.action);
        g.writeStringField("value", this.value);
        g.writeEndObject();
    }
}
