package com.groupsupportserver.pugbot.core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Embed {
    public static void helpStaff(TextChannel channel){
        EmbedBuilder helpStaff = new EmbedBuilder();
        helpStaff.setTitle("PugBut Commands");
        helpStaff.setColor(new Color(0x25c059));
        helpStaff.addField("&chill", "Puts a user in chill room: &chill [@Member]", false);
        helpStaff.addField("&clear", "Clears [amount] messages from current channel: &clear [amount]", false);
        channel.sendMessage(helpStaff.build()).queue();
    }

    public static void sendErrorMessagePermissionStaff(TextChannel channel, Member member){
        EmbedBuilder error = new EmbedBuilder();
        error.setTitle("Not Enough Permissions!");
        error.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        error.setColor(new Color(0x25c059));
        error.setDescription("Permission Require: Staff");
        channel.sendMessage(error.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    public static void help(TextChannel channel){
        EmbedBuilder help = new EmbedBuilder();
        help.setTitle("PugBut Commands");
        help.setColor(new Color(0x25c059));
        help.addField("&ping", "Tests bot's response time by replying with Pong", false);
        help.addField("&help", "Displays currently available commands", false);
        help.addField("&join", "Makes PugBot join a voice channel you are currently in", false);
        help.addField("&play [YOUTUBE LINK]", "Places the requested song in the queue", false);
        help.addField("&leave", "Makes PugBot leave a voice channel you are currently in", false);
        help.addField("&donate", "Displays current ways to donate", false);
        help.addField("&website", "Discplays a URL to our website", false);
        channel.sendMessage(help.build()).queue();
    }

    public static void  website(TextChannel channel){
        EmbedBuilder website = new EmbedBuilder();
        website.setColor(new Color(0x25c059));
        website.addField("Here you go:", "https://www.groupsupportserver.com", false);
        channel.sendMessage(website.build()).queue();
    }

    public static void ping(GuildMessageReceivedEvent event, TextChannel channel){
        EmbedBuilder ping = new EmbedBuilder();
        ping.setColor(new Color(0x25c059));
        ping.addField("Pong!", "Time took to respond: " + event.getJDA().getPing() + " ms", false);
        channel.sendMessage(ping.build()).queue();
    }

    public static void donate(TextChannel channel){
        EmbedBuilder donate = new EmbedBuilder();
        donate.setColor(new Color(0x25c059));
        donate.setTitle("We have two ways to donate right now:");
        donate.addField("~~Patreon~~", "~~Currently under development~~", false);
        donate.addField("Paypal", "Visit **&website** and at the bottom you will find the button", false);
        channel.sendMessage(donate.build()).queue();
    }
}
