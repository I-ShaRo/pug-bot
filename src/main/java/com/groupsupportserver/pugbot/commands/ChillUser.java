package com.groupsupportserver.pugbot.commands;

import com.groupsupportserver.pugbot.core.Bools;
import com.groupsupportserver.pugbot.core.Info;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChillUser extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(args[0].equalsIgnoreCase(Info.PREFIX + "chill") && Bools.isStaff(event) && !event.getAuthor().isBot()){
            if (args.length <= 1){
                sendErrorMessage(event.getChannel(), event.getMember());
            }
            else {
                Member target = event.getMessage().getMentionedMembers().get(0);
                String roles = target.getRoles().toString();
                Role chilled = event.getGuild().getRolesByName("Taking time off", true).get(0);

                if (args.length >= 2){
                    log(target, event.getMember(), event.getGuild().getTextChannelById("495678876991619083"), roles);
                    event.getGuild().getController().removeRolesFromMember(target, target.getRoles()).queue();
                    event.getGuild().getController().addSingleRoleToMember(target, chilled).queueAfter(1, TimeUnit.SECONDS);

                    event.getChannel().sendMessage("Successfully Chilled" + target);
                }
            }
        }
    }

    public void sendErrorMessage (TextChannel channel, Member member){
        EmbedBuilder error = new EmbedBuilder();
        error.setTitle("Invalid Usage!");
        error.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        error.setColor(new Color(0x25c059));
        error.setDescription("[] = Required");
        error.addField("Proper usage:", "&chill [@user]", false);
        channel.sendMessage(error.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    public void log(Member muted, Member muter, TextChannel channel, String role){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        EmbedBuilder log = new EmbedBuilder();
        log.setTitle("Chill Report");
        log.setColor(new Color(0x25c059));
        log.addField("Chilled User:", muted.getAsMention(), false);
        log.addField("Roles before chill:", role.toString(), false);
        log.addField("Put in Chillroom by:", muter.getAsMention(), false);
        log.addField("Date:", sdf.format(date), false);
        log.addField("Time:", stf.format(date), false);
        channel.sendMessage(log.build()).queue();
    }
}
