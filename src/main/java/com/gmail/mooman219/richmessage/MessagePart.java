package com.gmail.mooman219.richmessage;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import org.bukkit.ChatColor;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class MessagePart extends JsonData {

    protected String text;
    protected ChatColor color;
    protected String insertion;
    protected boolean bold = false;
    protected boolean italic = false;
    protected boolean underlined = false;
    protected boolean strikethrough = false;
    protected boolean obfuscated = false;
    protected ClickEvent clickEvent;
    protected HoverEvent hoverEvent;

    protected MessagePart(String text) {
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(JsonGenerator g) throws IOException {
        if (text != null) {
            g.writeStringField("text", text);
        }
        if (color != null) {
            g.writeStringField("color", color.name().toLowerCase());
        }
        if (insertion != null) {
            g.writeStringField("insertion", insertion);
        }
        if (bold) {
            g.writeBooleanField("bold", true);
        }
        if (italic) {
            g.writeBooleanField("italic", true);
        }
        if (underlined) {
            g.writeBooleanField("underlined", true);
        }
        if (strikethrough) {
            g.writeBooleanField("strikethrough", true);
        }
        if (obfuscated) {
            g.writeBooleanField("obfuscated", true);
        }
        if (clickEvent != null) {
            g.writeObjectFieldStart("clickEvent");
            clickEvent.write(g);
        }
        if (hoverEvent != null) {
            g.writeObjectFieldStart("hoverEvent");
            hoverEvent.write(g);
        }
        g.writeEndObject();
    }
}
