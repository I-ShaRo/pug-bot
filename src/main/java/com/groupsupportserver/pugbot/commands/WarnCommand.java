package com.groupsupportserver.pugbot.commands;

import com.groupsupportserver.pugbot.core.Bools;
import com.groupsupportserver.pugbot.core.Embed;
import com.groupsupportserver.pugbot.Info;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WarnCommand extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");

        Role warning1 = event.getGuild().getRolesByName("1 Warning", true).get(0);
        Role warning2 = event.getGuild().getRolesByName("2 Warnings", true).get(0);
        Role warning3 = event.getGuild().getRolesByName("3 Warnings", true).get(0);
        Role warning4 = event.getGuild().getRolesByName("4 Warnings", true).get(0);
        Role warningMuted = event.getGuild().getRolesByName("Muted", true).get(0);

        if (args[0].equalsIgnoreCase(Info.PREFIX + "warn") && Bools.isStaff(event) &&!event.getAuthor().isBot()){
            String reason = "";
            if (args.length <= 2){
                sendErrorMessage(event.getChannel(), event.getMember());
            }
            if (args.length >= 3){
                Member target = event.getMessage().getMentionedMembers().get(0);
                ArrayList<Role> warnings = new ArrayList<Role>();
                warnings.add(warning1);
                warnings.add(warning2);
                warnings.add(warning3);
                warnings.add(warning4);
                warnings.add(warningMuted);

                Set<Role> inCommon = new HashSet<>(event.getMessage().getMentionedMembers().get(0).getRoles());
                inCommon.retainAll(warnings);
                int warned = inCommon.size();

                for (int i = 2; i < args.length; i++){
                    reason += args[i] + " ";
                }
                if (warned == 0){
                    event.getGuild().getController().addSingleRoleToMember(target, warning1).queue();
                    log(target, event.getMember(),  event.getGuild().getTextChannelById("495678876991619083"), reason);
                    success(target, reason, event.getChannel());
                }
                else if (warned == 1){
                    event.getGuild().getController().addSingleRoleToMember(target, warning2).queue();
                    log(target, event.getMember(),  event.getGuild().getTextChannelById("495678876991619083"), reason);
                    success(target, reason, event.getChannel());;
                }
                else if (warned == 2){
                    event.getGuild().getController().addSingleRoleToMember(target, warning3).queue();
                    log(target, event.getMember(),  event.getGuild().getTextChannelById("495678876991619083"), reason);
                    success(target, reason, event.getChannel());
                }
                else if (warned == 3){
                    event.getGuild().getController().addSingleRoleToMember(target, warning4).queue();
                    log(target, event.getMember(),  event.getGuild().getTextChannelById("495678876991619083"), reason);
                    success(target, reason, event.getChannel());
                }
                else if (warned == 4){
                    event.getGuild().getController().addSingleRoleToMember(target, warningMuted).queue();
                    log(target, event.getMember(),  event.getGuild().getTextChannelById("495678876991619083"), reason);
                    success(target, reason, event.getChannel());
                }

            }
        } else if (args[0].equalsIgnoreCase(Info.PREFIX + "warn") && !Bools.isStaff(event) &&!event.getAuthor().isBot()){
            Embed.sendErrorMessagePermissionStaff(event.getChannel(), event.getMember());
        }
    }

    public void sendErrorMessage (TextChannel channel, Member member){
        EmbedBuilder error = new EmbedBuilder();
        error.setTitle("Invalid Usage!");
        error.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        error.setColor(new Color(0x25c059));
        error.addField("Proper usage:", "&warn [@user] [reason]", false);
        channel.sendMessage(error.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    public void log (Member warned, Member warner, TextChannel channel, String reason){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        EmbedBuilder log = new EmbedBuilder();
        log.setTitle("Warning Report");
        log.setColor(new Color(0x25c059));
        log.addField("Warned User:", warned.getAsMention(), false);
        log.addField("Warned by:", warner.getAsMention(), false);
        log.addField("Date:", sdf.format(date), false);
        log.addField("Time:", stf.format(date), false);
        log.addField("Reason:", reason, false);
        channel.sendMessage(log.build()).queue();
    }

    public void success (Member warned, String reason, TextChannel channel){
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(new Color(0x25c059));
        success.setTitle("Success!");
        success.addField(warned.getNickname() + " warned for: " + reason, "", false);
        channel.sendMessage(success.build()).queue();
    }
}
