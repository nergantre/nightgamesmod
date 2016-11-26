package nightgames.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import javax.swing.JComponent;

import nightgames.areas.Area;
import nightgames.areas.Cache;
import nightgames.areas.MapDrawHint;
import nightgames.global.Global;

@SuppressWarnings("serial")
public class MapComponent extends JComponent {
    private static Font font = new Font("Courier", 1, 11);
    private int borderWidth = 3;

    public MapComponent() {}

    public void centerString(Graphics g, Rectangle r, MapDrawHint drawHint, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r2D = font.getStringBounds(drawHint.label, frc);

        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (r.width / 2) - (rWidth / 2) - rX;
        int b = (r.height / 2) - (rHeight / 2) - rY;
        AffineTransform orig = null;
        if (drawHint.vertical && g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            orig = g2d.getTransform();
            g2d.rotate(Math.PI / 2, (r.x + a) + rWidth * .5, r.y + b - rHeight / 4);
        }
        g.setFont(font);
        g.drawString(drawHint.label, r.x + a, r.y + b);

        if (orig != null && g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setTransform(orig);
        }
    }

    public void paint(Graphics g) {
        if (Global.getMatch() == null) {
            return;
        }
        if (g instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        int multiplier = 11;
        int width = Math.max(getWidth(), 25 * multiplier);
        int height = Math.max(getHeight(), 19 * multiplier);
        int mapBorder = 12;
        int yOffset = 0;
        g.setColor(new Color(0, 14, 60));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawRect(borderWidth, borderWidth, width - borderWidth * 2, height - borderWidth * 2);
        Collection<Area> rooms = Global.getMatch().getAreas();
        rooms.stream().forEach(area -> {
            if (area.drawHint.rect.width == 0 || area.drawHint.rect.height == 0) {
                return;
            }
            Rectangle rect = new Rectangle(area.drawHint.rect.x * multiplier + mapBorder,
                            area.drawHint.rect.y * multiplier + yOffset + mapBorder,
                            area.drawHint.rect.width * multiplier, area.drawHint.rect.height * multiplier);
            Color mapColor = new Color(0, 34, 100);
            if (area.humanPresent()) {
                mapColor = new Color(25, 74, 120);
            } else if (area.isDetected()) {
                mapColor = new Color(150, 45, 60);
            } else if (area.isPinged()) {
                mapColor = new Color(75, 25, 120);
            }

            if (area.env.stream().anyMatch(deployable -> deployable instanceof Cache)) {
                mapColor = mapColor.brighter().brighter();
            }

            g.setColor(mapColor);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        });
        rooms.stream().forEach(area -> {
            if (area.drawHint.rect.width == 0 || area.drawHint.rect.height == 0) {
                return;
            }
            Rectangle rect = new Rectangle(area.drawHint.rect.x * multiplier + mapBorder,
                            area.drawHint.rect.y * multiplier + yOffset + mapBorder,
                            area.drawHint.rect.width * multiplier, area.drawHint.rect.height * multiplier);
            g.setColor(new Color(50, 100, 200));
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        });
        rooms.stream().forEach(area -> {
            if (area.drawHint.rect.width == 0 || area.drawHint.rect.height == 0) {
                return;
            }
            Rectangle rect = new Rectangle(area.drawHint.rect.x * multiplier + mapBorder,
                            area.drawHint.rect.y * multiplier + yOffset + mapBorder,
                            area.drawHint.rect.width * multiplier, area.drawHint.rect.height * multiplier);
            g.setColor(Color.WHITE);
            centerString(g, rect, area.drawHint, font);
        });
    }
}
