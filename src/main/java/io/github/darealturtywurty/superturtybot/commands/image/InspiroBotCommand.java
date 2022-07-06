package io.github.darealturtywurty.superturtybot.commands.image;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import io.github.darealturtywurty.superturtybot.core.util.Constants;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InspiroBotCommand extends ImageCommand {
    public InspiroBotCommand() {
        super(new Types(true, false, false, false));
    }
    
    @Override
    public String getDescription() {
        return "Gets an artificially generated inspirational quote";
    }
    
    @Override
    public ImageCategory getImageCategory() {
        return ImageCategory.FUN;
    }
    
    @Override
    public String getName() {
        return "inspirobot";
    }
    
    @Override
    protected void runSlash(SlashCommandInteractionEvent event) {
        final CompletableFuture<String> quote = getInspiroQuote();
        event.deferReply().setContent("Loading InspiroBot quote...").mentionRepliedUser(false).queue();
        quote.thenAccept(result -> event.getHook().editOriginal(result).queue());
    }
    
    private static CompletableFuture<String> getInspiroQuote() {
        final var future = new CompletableFuture<String>();
        try {
            final URLConnection connection = new URL("https://inspirobot.me/api/?generate=true").openConnection();
            final String body = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
            future.complete(body);
        } catch (final IOException exception) {
            future.complete("There has been an issue processing this command. Please try again later!");
            Constants.LOGGER.error("Exception thrown whilst getting inspirobot quote!\n{}\n{}", exception.getMessage(),
                ExceptionUtils.getStackTrace(exception));
        }
        
        return future;
    }
}
