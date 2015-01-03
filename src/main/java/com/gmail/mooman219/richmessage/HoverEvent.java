package com.gmail.mooman219.richmessage;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class HoverEvent extends JsonData {

    protected RichMessage value;

    protected HoverEvent() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(JsonGenerator g) throws IOException {
        g.writeStringField("action", "show_text");
        g.writeArrayFieldStart("value");
        this.value.write(g);
        g.writeEndObject();
    }
}
