package com.groupsupportserver.pugbot;

import com.groupsupportserver.pugbot.commands.ChillUser;
import com.groupsupportserver.pugbot.commands.ClearCommand;
import com.groupsupportserver.pugbot.commands.KickCommand;
import com.groupsupportserver.pugbot.commands.WarnCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Bot {
    // Main function, program entry point.
    public static void main(String[] args) {
        // Checks if there were any parameters passed in.
        if(args.length == 0) {
            // We need at least one -- our token!
            System.out.println("Please provide a token!");
            return;
        }
        //String token = args[0]; // 0 based indexing.
        try {

            String token = Info.TOKEN;
            JDA jda = new JDABuilder(AccountType.BOT).setToken(token) // Set the token.
                    .addEventListener(new Handler()) // Add an event listener.
                    .addEventListener(new modHandler())
                    .addEventListener(new musicHandler())
                    .addEventListener(new ChillUser())
                    .addEventListener(new ClearCommand())
                    .addEventListener(new KickCommand())
                    .addEventListener(new WarnCommand())
                    .buildBlocking(); // Block the current thread until JDA is 100% ready.
                   jda.getPresence().setGame(Game.playing("with Beau - &help"));
            // Not required, but useful to demonstrate that everything worked.
            System.out.println("Logged in as " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator() + "!");
        } catch(LoginException | InterruptedException exception) {
            // Print the error.
            exception.printStackTrace();
        }
    }
}