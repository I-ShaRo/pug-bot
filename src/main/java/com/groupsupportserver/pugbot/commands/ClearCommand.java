package com.groupsupportserver.pugbot.commands;

import com.groupsupportserver.pugbot.core.Bools;
import com.groupsupportserver.pugbot.Info;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Info.PREFIX + "clear") && Bools.isStaff(event) && !event.getAuthor().isBot()) {
            if (args.length <= 1) {
                sendErrorMessage(event.getChannel(), event.getMember());
            } else {
                event.getMessage().delete().queue();
                TextChannel target = event.getChannel();
                int integer = Integer.parseInt(args[1]);
                purgeMessages(target, integer);
                log(event.getMember(), target, event.getGuild().getTextChannelById("473227114229792768"));
            }
        }
    }

    public void sendErrorMessage (TextChannel channel, Member member){
        EmbedBuilder error = new EmbedBuilder();
        error.setTitle("Invalid Usage!");
        error.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        error.setColor(new Color(0x25c059));
        error.setDescription("[] = Required");
        error.addField("Proper usage:", "&clear [number of messages]", false);
        channel.sendMessage(error.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    public void log(Member clearer, TextChannel target, TextChannel channel){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        EmbedBuilder log = new EmbedBuilder();
        log.setTitle("Clear Report");
        log.setColor(new Color(0x25c059));
        log.addField("Cleared Channel:", target.getAsMention(), false);
        log.addField("Cleared by:", clearer.getAsMention(), false);
        log.addField("Date:", sdf.format(date), false);
        log.addField("Time:", stf.format(date), false);
        channel.sendMessage(log.build()).queue();
    }

    private void purgeMessages(TextChannel channel, int num){
        MessageHistory history = new MessageHistory(channel);
        List<Message> messages;

        messages = history.retrievePast(num).complete();
        channel.deleteMessages(messages).queue();
    }
}
