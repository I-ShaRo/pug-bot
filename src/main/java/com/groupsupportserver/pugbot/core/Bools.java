package com.groupsupportserver.pugbot.core;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Bools {
    public static boolean isStaff(GuildMessageReceivedEvent event){
        if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Staff", true).get(0))){
            return true;
        }
        else{
            return false;
        }
    }
}
