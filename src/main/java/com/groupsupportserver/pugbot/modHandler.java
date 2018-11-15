package com.groupsupportserver.pugbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.List;

public class modHandler extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        //our prefix is &
        commandClientBuilder.setPrefix("&");
        //"Type &help"
        commandClientBuilder.useDefaultGame();
        commandClientBuilder.addCommand(new kick());

    }


    public static class kick extends Command {
        public kick() {
            this.name = "kick";
            this.help = "Kicks a mentioned member";
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            Guild guild = commandEvent.getGuild();
            //if we're not in a guild we can't kick anyone
            if(guild == null) {
                commandEvent.reply("You must run this command in a server");
                return;
            }
            Member author = commandEvent.getMessage().getMember();
            //the author can't kick people
            if(!author.hasPermission(Permission.KICK_MEMBERS)) {
                commandEvent.reply("You don't have permission to kick people!");
                return;
            }
            List<Member> mentionedMembers = commandEvent.getMessage().getMentionedMembers();
            if(mentionedMembers.isEmpty()) {
                commandEvent.reply("You must mention who you want to be kicked");
                return;
            }
            guild.getController().kick(mentionedMembers.get(0)).queue(success->{
                commandEvent.reply("Successfully kicked " + mentionedMembers.get(0).getUser().getName());
            }, error->{
                commandEvent.reply("Unable to kick " + mentionedMembers.get(0).getUser().getName() + ": " + error);
            });
        }
    }
}
