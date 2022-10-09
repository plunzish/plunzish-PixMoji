package sh.plunzi.plunzichatplugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OtherUtils {

    private java.util.List<Color> gradientColor(Color start, Color end, int steps) {

        steps--;

        double differenceR = start.getRed() - end.getRed();
        double differenceG = start.getGreen() - end.getGreen();
        double differenceB = start.getBlue() - end.getBlue();

        double stepR = differenceR / steps;
        double stepG = differenceG / steps;
        double stepB = differenceB / steps;

        List<Color> output = new ArrayList<>();

        for (int i = 0; i < steps+1; i++) {
            Color color = Color.fromRGB(
                    (int) ((end.getRed() + stepR * i) + 0.5),
                    (int) ((end.getGreen() + stepG * i) + 0.5),
                    (int) ((end.getBlue() + stepB * i) + 0.5)
            );
            output.add(color);
        }

        return output;
    }

    public Component buildComponent(String text, Color color1, Color color2) {
        TextColor textColor1 = TextColor.color(HSVLike.fromRGB(color1.getRed(), color1.getGreen(), color1.getBlue()));
        TextColor textColor2 = TextColor.color(HSVLike.fromRGB(color1.getRed(), color1.getGreen(), color1.getBlue()));

        Component output = Component.empty();

        int length = text.length();
        char[] chars = text.toCharArray();

        List<Color> colors = gradientColor(color2, color1, length);

        for (int i = 0; i < length; i++) {
            Component component = Component.text(chars[i]);
            Color color = colors.get(i);
            component = component.color(TextColor.color(HSVLike.fromRGB(color.getRed(), color.getGreen(), color.getBlue())));
            output = output.append(component);
        }

        return output;
    }

    public List<Player> stringToPlayers(String input, CommandSender sender, boolean sendErrorMessages) {

        String[] splitInput = input.split(",");
        List<Player> players = new ArrayList<>();

        for(String inputElement : splitInput) {
            Player player = Bukkit.getPlayerExact(inputElement);
            if(player == null) {
                if(sendErrorMessages) PlunziChatPlugin.CHAT_HANDLER.sendCommandFeedback(inputElement + " > Player not found", true, sender);

                continue;
            }
            players.add(player);
        }
        return players;
    }
}


