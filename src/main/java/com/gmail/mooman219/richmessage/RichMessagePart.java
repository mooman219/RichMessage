package com.gmail.mooman219.richmessage;

import java.util.regex.Pattern;

public final class RichMessagePart {

    private String text;
    private String color;
    private String bold = "false";
    private String italic = "false";
    private String underlined = "false";
    private String strikethrough = "false";
    private String obfuscated = "false";
    private RichEvent clickEvent;
    private RichEvent hoverEvent;

    protected RichMessagePart() {
    }

    public RichMessagePart(String text) {
        this.text = text;
    }

    public void formatPattern(Pattern pattern, String replacement) {
        this.text = pattern.matcher(this.text).replaceAll(replacement);
    }

    public void color(RichColor color) {
        if (color == RichColor.NONE) {
            this.color = null;
        } else {
            this.color = color.toString();
        }
    }

    public void style(RichStyle style) {
        this.clearStyle();
        this.setStyle(style);
    }

    public void style(RichStyle... styles) {
        this.clearStyle();
        for (RichStyle style : styles) {
            this.setStyle(style);
        }
    }

    private void setStyle(RichStyle style) {
        switch (style) {
            case BOLD:
                this.bold = "true";
                break;
            case UNDERLINED:
                this.underlined = "true";
                break;
            case STRIKETHROUGH:
                this.strikethrough = "true";
                break;
            case OBFUSCATED:
                this.obfuscated = "true";
                break;
            default:
                break;
        }
    }

    private RichMessagePart clearStyle() {
        this.bold = "false";
        this.italic = "false";
        this.underlined = "false";
        this.strikethrough = "false";
        this.obfuscated = "false";
        return this;
    }

    public void link(String link) {
        this.clickEvent = new RichEvent();
        this.clickEvent.action = "open_url";
        this.clickEvent.value = link;
    }

    public void suggest(String suggest) {
        this.clickEvent = new RichEvent();
        this.clickEvent.action = "suggest_command";
        this.clickEvent.value = suggest;
    }

    public void command(String command) {
        this.clickEvent = new RichEvent();
        this.clickEvent.action = "run_command";
        this.clickEvent.value = command;
    }

    public void tooltip(String tooltip) {
        this.hoverEvent = new RichEvent();
        this.hoverEvent.action = "show_text";
        this.hoverEvent.value = tooltip;
    }

    public void tooltip(Iterable<String> lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        this.hoverEvent = new RichEvent();
        this.hoverEvent.action = "show_text";
        this.hoverEvent.value = builder.toString();
    }

    public void tooltip(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        this.hoverEvent = new RichEvent();
        this.hoverEvent.action = "show_text";
        this.hoverEvent.value = builder.toString();
    }

    public static class RichEvent {

        private String action;
        private String value;
    }
}
