package com.gmail.mooman219.richmessage;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RichMessage extends JsonData {

    private final ArrayList<MessagePart> messages = new ArrayList<>();
    private MessagePart current;
    private String compiledMessage;
    private boolean dirty;

    public RichMessage(String text) {
        super(false);
        then(text);
    }

    public final RichMessage then(String text) {
        dirty = true;
        current = new MessagePart(text);
        messages.add(current);
        return this;
    }

    public RichMessage formatPattern(Pattern pattern, String replacement) {
        dirty = true;
        current.text = pattern.matcher(current.text).replaceAll(replacement);
        return this;
    }

    public RichMessage color(ChatColor color) {
        dirty = true;
        current.color = color;
        return this;
    }

    public RichMessage style(ChatColor style) {
        clearStyle();
        setStyle(style);
        return this;
    }

    public RichMessage style(ChatColor... styles) {
        clearStyle();
        for (ChatColor style : styles) {
            setStyle(style);
        }
        return this;
    }

    public RichMessage clearStyle() {
        dirty = true;
        current.bold = false;
        current.italic = false;
        current.underlined = false;
        current.strikethrough = false;
        current.obfuscated = false;
        return this;
    }

    private void setStyle(ChatColor style) {
        if (style == null) {
            return;
        }
        switch (style) {
            case BOLD:
                current.bold = true;
                break;
            case ITALIC:
                current.italic = true;
                break;
            case UNDERLINE:
                current.underlined = true;
                break;
            case STRIKETHROUGH:
                current.strikethrough = true;
                break;
            case MAGIC:
                current.obfuscated = true;
                break;
            default:
                break;
        }
    }

    public RichMessage insertion(String insertion) {
        dirty = true;
        current.insertion = insertion;
        return this;
    }

    public RichMessage link(String link) {
        dirty = true;
        if (current.clickEvent == null) {
            current.clickEvent = new ClickEvent();
        }
        current.clickEvent.action = "open_url";
        current.clickEvent.value = link;
        return this;
    }

    public RichMessage command(String command) {
        dirty = true;
        if (current.clickEvent == null) {
            current.clickEvent = new ClickEvent();
        }
        current.clickEvent.action = "run_command";
        current.clickEvent.value = command;
        return this;
    }

    public RichMessage suggest(String text) {
        dirty = true;
        if (current.clickEvent == null) {
            current.clickEvent = new ClickEvent();
        }
        current.clickEvent.action = "suggest_command";
        current.clickEvent.value = text;
        return this;
    }

    public RichMessage tooltip(RichMessage tooltip) {
        dirty = true;
        if (tooltip == this) {
            throw new IllegalArgumentException("Tooltip cannot be the same RichMessage as the current RichMessage.");
        }
        if (current.hoverEvent == null) {
            current.hoverEvent = new HoverEvent();
        }
        current.hoverEvent.value = tooltip;
        return this;
    }

    public void send(Player player, Object... arguments) {
        Map<String, Object> assignments = new HashMap<>();
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + toString(arguments));
    }

    public void send(Player player) {
        Map<String, Object> assignments = new HashMap<>();
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + toString());
    }

    public String toString(Object... arguments) {
        return MessageFormat.format(toString(), arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(JsonGenerator g) throws IOException {
        for (MessagePart part : messages) {
            g.writeStartObject();
            part.write(g);
        }
        g.writeEndArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (dirty) {
            dirty = false;
            compiledMessage = serialize();
        }
        return compiledMessage;
    }
}
