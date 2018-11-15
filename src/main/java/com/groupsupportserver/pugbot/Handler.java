package com.groupsupportserver.pugbot;

import com.groupsupportserver.pugbot.core.Bools;
import com.groupsupportserver.pugbot.core.Embed;
import com.groupsupportserver.pugbot.core.Info;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

public class Handler extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (event.getAuthor().isBot() || event.getAuthor().isFake() || event.getMessage().isWebhookMessage()) {
            return;
        }

        String message = event.getMessage().getContentRaw().toLowerCase();
        TextChannel channel = event.getChannel();

        if (args[0].equalsIgnoreCase(Info.PREFIX + "ping")) {
            Embed.ping(event, event.getChannel());
        }

        if (args[0].equalsIgnoreCase(Info.PREFIX + "help")) {
            if (args.length <= 1) {
                Embed.help(event.getChannel());
            }
            else if (args.length >= 2) {
                if (args[1].equalsIgnoreCase("staff")) {
                    if (!Bools.isStaff(event)) {
                        Embed.sendErrorMessagePermissionStaff(event.getChannel(), event.getMember());
                    } else {
                        Embed.helpStaff(event.getChannel());
                    }
                }

            }

        }

        if (args[0].equalsIgnoreCase(Info.PREFIX + "website")) {
            Embed.website(event.getChannel());
        }

        if (args[0].equalsIgnoreCase(Info.PREFIX + "donate")) {
            Embed.donate(event.getChannel());
        }

        // Checks if the command is &join.
        if (args[0].equalsIgnoreCase(Info.PREFIX + "join")) {
            // Checks if the bot has permissions.
            if (!event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) {
                // The bot does not have permission to join any voice channel. Don't forget the .queue()!
                channel.sendMessage("I do not have permissions to join a voice channel!").queue();
                return;
            }
            // Creates a variable equal to the channel that the user is in.
            VoiceChannel connectedChannel = event.getMember().getVoiceState().getChannel();
            // Checks if they are in a channel -- not being in a channel means that the variable = null.
            if (connectedChannel == null) {
                // Don't forget to .queue()!
                channel.sendMessage("You are not connected to a voice channel!").queue();
                return;
            }
            // Gets the audio manager.
            AudioManager audioManager = event.getGuild().getAudioManager();
            // When somebody really needs to chill.
            if (audioManager.isAttemptingToConnect()) {
                channel.sendMessage("The bot is already trying to connect! Chill mate!").queue();
                return;
            }
            // Connects to the channel.
            audioManager.openAudioConnection(connectedChannel);
            // Obviously people do not notice someone/something connecting.
            channel.sendMessage("Connected to the voice channel!").queue();
        } else if (args[0].equalsIgnoreCase(Info.PREFIX + "leave")) { // Checks if the command is !leave.
            // Gets the channel in which the bot is currently connected.
            VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            // Checks if the bot is connected to a voice channel.
            if (connectedChannel == null) {
                // Get slightly fed up at the user.
                channel.sendMessage("I am not connected to a voice channel!").queue();
                return;
            }

            // Disconnect from the channel.
            event.getGuild().getAudioManager().closeAudioConnection();
            // Notify the user.
            channel.sendMessage("Disconnected from the voice channel!").queue();
        }
    }
}