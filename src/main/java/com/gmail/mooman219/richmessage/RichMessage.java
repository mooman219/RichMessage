package com.gmail.mooman219.richmessage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RichMessage {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        RichMessage.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        RichMessage.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        RichMessage.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        RichMessage.mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private ArrayList<RichMessagePart> messages = new ArrayList<>();
    private boolean dirty;
    private String message;

    public RichMessage(String text) {
        this.messages.add(new RichMessagePart(text));
        this.dirty = true;
    }

    public RichMessage formatPattern(Pattern pattern, String replacement) {
        this.current().formatPattern(pattern, replacement);
        this.dirty = true;
        return this;
    }

    public RichMessage then(String text) {
        this.messages.add(new RichMessagePart(text));
        this.dirty = true;
        return this;
    }

    public RichMessage color(RichColor color) {
        this.current().color(color);
        this.dirty = true;
        return this;
    }

    public RichMessage command(String command) {
        this.current().command(command);
        this.dirty = true;
        return this;
    }

    public RichMessage link(String link) {
        this.current().link(link);
        this.dirty = true;
        return this;
    }

    public RichMessage style(RichStyle style) {
        this.current().style(style);
        this.dirty = true;
        return this;
    }

    public RichMessage style(RichStyle... styles) {
        this.current().style(styles);
        this.dirty = true;
        return this;
    }

    public RichMessage suggest(String text) {
        this.current().suggest(text);
        this.dirty = true;
        return this;
    }

    public RichMessage tooltip(String tooltip) {
        this.current().tooltip(tooltip);
        this.dirty = true;
        return this;
    }

    public RichMessage tooltip(Iterable<String> lines) {
        this.current().tooltip(lines);
        this.dirty = true;
        return this;
    }

    public RichMessage tooltip(String... lines) {
        this.current().tooltip(lines);
        this.dirty = true;
        return this;
    }

    private RichMessagePart current() {
        return this.messages.get(this.messages.size() - 1);
    }

    public void send(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + toString());
    }

    @Override
    public String toString() {
        if (this.dirty) {
            this.dirty = false;
            this.message = parse(this.messages);
        }
        return this.message;
    }

    private static String parse(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
