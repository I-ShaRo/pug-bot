package com.groupsupportserver.pugbot.commands;

import com.groupsupportserver.pugbot.core.Bools;
import com.groupsupportserver.pugbot.core.Embed;
import com.groupsupportserver.pugbot.core.Info;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class KickCommand extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(Info.PREFIX + "kick") && Bools.isStaff(event)){
            if (args.length <= 1){
                sendErrorMessage(event.getChannel(), event.getMember());
            }
            else if (args.length == 2){
                sendErrorMessageReason(event.getChannel(), event.getMember());
            }
            else if (args.length >= 3){
                Member target = event.getMessage().getMentionedMembers().get(0);
                String roles = target.getRoles().toString();
                String reason = "";
                for (int i = 2; i < args.length; i++){
                    reason += args[i] + " ";
                }
                log(target, event.getMember(), reason, event.getGuild().getTextChannelById("495678876991619083"), roles);
                event.getGuild().getController().kick(target).queue();

                event.getChannel().sendMessage("Successfully kicked: " + target).queue();

            }
        }
        else if (args[0].equalsIgnoreCase(Info.PREFIX + "kick") && !Bools.isStaff(event)){
            Embed.sendErrorMessagePermissionStaff(event.getChannel(), event.getMember());
        }
    }

    public void sendErrorMessage(TextChannel channel, Member member){
        EmbedBuilder error = new EmbedBuilder();
        error.setTitle("Invalid Usage!");
        error.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        error.setColor(new Color(0x25c059));
        error.setDescription("[] = Required");
        error.addField("Proper usage:", "&kick [@user] [reason]", false);
        channel.sendMessage(error.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    public void sendErrorMessageReason(TextChannel channel, Member member){
        EmbedBuilder error = new EmbedBuilder();
        error.setTitle("Must State Reason!");
        error.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        error.setColor(new Color(0x25c059));
        error.setDescription("[] = Required");
        error.addField("Proper usage:", "&kick [@user] [reason]", false);
        channel.sendMessage(error.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    public void log(Member kicked, Member kicker, String reason, TextChannel channel, String role){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        EmbedBuilder log = new EmbedBuilder();
        log.setTitle("Kick Report");
        log.setColor(new Color(0x25c059));
        log.addField("Kicked User:", kicked.getAsMention(), false);
        log.addField("Roles before kick:", role.toString(), false);
        log.addField("Kicked by:", kicker.getAsMention(), false);
        log.addField("Reason:", reason, false);
        log.addField("Date:", sdf.format(date), false);
        log.addField("Time:", stf.format(date), false);
        channel.sendMessage(log.build()).queue();
    }
}
