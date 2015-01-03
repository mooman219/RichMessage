# RichMessage

Send json messages to players through a simple API.

[You can download compiled jars here.](https://github.com/mooman219/RichMessage/releases)

## How to use

To build a message, simply create a new RichMessage object and start adding different attributes.

Welcome message example:
```java
RichMessage welcome_message = new RichMessage("Welcome!")
                      .color(ChatColor.GREEN)
                      .style(ChatColor.BOLD);
```
Multiple message segments example:
```java
RichMessage greeting = new RichMessage("Happy")
                      .color(ChatColor.GREEN)
                      .style(ChatColor.BOLD)
                      .then(" Holidays!")
                      .color(ChatColor.RED)
                      .style(ChatColor.BOLD);
```
---
Sending the message can be done by calling the ```message.send(Player player)``` method.

Example:
```java
RichMessage welcome_message = new RichMessage("Welcome!")
                      .color(ChatColor.GREEN)
                      .style(ChatColor.BOLD);
welcome_message.send(player);
```

Alternatively, getting the raw formatted message can be done by calling ```message.toString()```.
