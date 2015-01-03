package com.gmail.mooman219.richmessage;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class RichMessage extends JsonData {

    private final ArrayList<MessagePart> messages = new ArrayList<>();
    private MessagePart current;
    private String compiledMessage;
    private boolean dirty;

    /**
     * Creates a new RichMessage starting with the provided text.
     *
     * @param text the text to start the message with
     */
    public RichMessage(String text) {
        super(false);
        then(text);
    }

    /**
     * Starts a new message segment. A new message segment does not share any
     * attributes set in a previous segment.
     *
     * @param text the text to start the new message segment with
     * @return this RichMessage
     */
    public final RichMessage then(String text) {
        dirty = true;
        current = new MessagePart(text);
        messages.add(current);
        return this;
    }

    /**
     * Assigns a color to this message segment.
     *
     * @param color the color to assign
     * @return this RichMessage
     */
    public RichMessage color(ChatColor color) {
        dirty = true;
        current.color = color;
        return this;
    }

    /**
     * Assigns a style to this message segment. This overwrites any previously
     * set styles to this message segment. Valid styles include {ChatColor.BOLD,
     * ChatColor.ITALIC, ChatColor.UNDERLINE, ChatColor.STRIKETHROUGH,
     * ChatColor.MAGIC}.
     *
     * @param style the style to assign
     * @return this RichMessage
     */
    public RichMessage style(ChatColor style) {
        clearStyle();
        setStyle(style);
        return this;
    }

    /**
     * Assigns multiple styles to this message segment. This overwrites any
     * previously set styles to this message segment. Valid styles include
     * {ChatColor.BOLD, ChatColor.ITALIC, ChatColor.UNDERLINE,
     * ChatColor.STRIKETHROUGH, ChatColor.MAGIC}.
     *
     * @param styles the style to assign
     * @return this RichMessage
     */
    public RichMessage style(ChatColor... styles) {
        clearStyle();
        for (ChatColor style : styles) {
            setStyle(style);
        }
        return this;
    }

    /**
     * Clears any styles set on this message segment.
     *
     * @return this RichMessage
     */
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

    /**
     * Adds an 'insertion' event to this message segment. If this message
     * segment is clicked, the provided string is inserted into the player's
     * chat bar.
     *
     * @param insertion the insertion text
     * @return this RichMessage
     */
    public RichMessage insertion(String insertion) {
        dirty = true;
        current.insertion = insertion;
        return this;
    }

    /**
     * Adds an 'open_url' event to this message segment. If this message segment
     * is clicked, the provided link is opened on the player's end.
     *
     * @param link the link to open
     * @return this RichMessage
     */
    public RichMessage link(String link) {
        dirty = true;
        if (current.clickEvent == null) {
            current.clickEvent = new ClickEvent();
        }
        current.clickEvent.action = "open_url";
        current.clickEvent.value = link;
        return this;
    }

    /**
     * Adds a 'run_command' event to this message segment. If this message
     * segment is clicked, the player will run the provided command.
     *
     * @param command the command to run
     * @return this RichMessage
     */
    public RichMessage command(String command) {
        dirty = true;
        if (current.clickEvent == null) {
            current.clickEvent = new ClickEvent();
        }
        current.clickEvent.action = "run_command";
        current.clickEvent.value = command;
        return this;
    }

    /**
     * Adds an 'suggest_command' event to this message segment. If this message
     * segment is clicked, the provided string replaces the player's chat bar.
     *
     * @param text the command to suggest
     * @return this RichMessage
     */
    public RichMessage suggest(String text) {
        dirty = true;
        if (current.clickEvent == null) {
            current.clickEvent = new ClickEvent();
        }
        current.clickEvent.action = "suggest_command";
        current.clickEvent.value = text;
        return this;
    }

    /**
     * Adds a 'show_text' event to this message segment. If this message segment
     * is hovered over, the provided tooltip is displayed.
     *
     * @param tooltip the tooltip to display
     * @return this RichMessage
     */
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

    /**
     * Sends the message to the player.
     *
     * @param player the player to send the message to
     */
    public void send(Player player) {
        Map<String, Object> assignments = new HashMap<>();
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void write(JsonGenerator g) throws IOException {
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
