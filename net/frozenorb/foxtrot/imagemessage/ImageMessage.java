package net.frozenorb.foxtrot.imagemessage;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import org.bukkit.*;
import java.awt.geom.*;
import java.awt.image.*;
import org.bukkit.command.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.entity.*;

public class ImageMessage
{
    private static final char TRANSPARENT_CHAR = ' ';
    private static final Color[] COLORS;
    private String[] lines;
    
    public ImageMessage(final String image) {
        super();
        try {
            final ChatColor[][] chatColors = this.toChatColorArray(ImageIO.read(new File(new File("ascii-art"), image + ".png")), 8);
            this.lines = this.toImgMessage(chatColors, '\u2588');
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ImageMessage appendText(final String... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length > y) {
                final StringBuilder sb = new StringBuilder();
                final String[] lines = this.lines;
                final int n = y;
                lines[n] = sb.append(lines[n]).append(" ").append(ChatColor.translateAlternateColorCodes('&', text[y])).toString();
            }
        }
        return this;
    }
    
    private ChatColor[][] toChatColorArray(final BufferedImage image, final int height) {
        final double ratio = image.getHeight() / image.getWidth();
        final BufferedImage resized = this.resizeImage(image, (int)(height / ratio), height);
        final ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); ++x) {
            for (int y = 0; y < resized.getHeight(); ++y) {
                final int rgb = resized.getRGB(x, y);
                final ChatColor closest = this.getClosestChatColor(new Color(rgb, true));
                chatImg[x][y] = closest;
            }
        }
        return chatImg;
    }
    
    private String[] toImgMessage(final ChatColor[][] colors, final char imgchar) {
        final String[] lines = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; ++y) {
            String line = "";
            for (int x = 0; x < colors.length; ++x) {
                final ChatColor color = colors[x][y];
                line += ((color != null) ? (colors[x][y].toString() + imgchar) : ' ');
            }
            lines[y] = line + ChatColor.RESET;
        }
        return lines;
    }
    
    private BufferedImage resizeImage(final BufferedImage originalImage, final int width, final int height) {
        final AffineTransform af = new AffineTransform();
        af.scale(width / originalImage.getWidth(), height / originalImage.getHeight());
        final AffineTransformOp operation = new AffineTransformOp(af, 1);
        return operation.filter(originalImage, null);
    }
    
    private double getDistance(final Color c1, final Color c2) {
        final double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        final double r = c1.getRed() - c2.getRed();
        final double g = c1.getGreen() - c2.getGreen();
        final int b = c1.getBlue() - c2.getBlue();
        final double weightR = 2.0 + rmean / 256.0;
        final double weightG = 4.0;
        final double weightB = 2.0 + (255.0 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }
    
    private boolean areIdentical(final Color c1, final Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
    }
    
    private ChatColor getClosestChatColor(final Color color) {
        if (color.getAlpha() < 128) {
            return null;
        }
        int index = 0;
        double best = -1.0;
        for (int i = 0; i < ImageMessage.COLORS.length; ++i) {
            if (this.areIdentical(ImageMessage.COLORS[i], color)) {
                return ChatColor.values()[i];
            }
        }
        for (int i = 0; i < ImageMessage.COLORS.length; ++i) {
            final double distance = this.getDistance(color, ImageMessage.COLORS[i]);
            if (distance < best || best == -1.0) {
                best = distance;
                index = i;
            }
        }
        return ChatColor.values()[index];
    }
    
    public void send(final CommandSender sender) {
        for (final String line : this.lines) {
            sender.sendMessage(line);
        }
    }
    
    public void broadcast() {
        for (final String line : this.lines) {
            buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage(line);
        }
    }
    
    public void sendOPs() {
        for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            if (player.isOp()) {
                this.send((CommandSender)player);
            }
        }
    }
    
    public String[] getLines() {
        return this.lines;
    }
    
    static {
        COLORS = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255) };
    }
}
