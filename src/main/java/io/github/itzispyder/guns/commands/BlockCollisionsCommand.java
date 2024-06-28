package io.github.itzispyder.guns.commands;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.pdk.utils.raytracers.BlockDisplayRaytracer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;

@CommandRegistry(value = "blockcollisions", usage = "/blockcollisions", playersOnly = true)
public class BlockCollisionsCommand implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Command command, String s, Args args) {
        Player p = (Player) sender;
        Block block = p.getTargetBlockExact(5);

        if (block == null || block.isEmpty() || !block.isCollidable()) {
            error(sender, "Block is not collidable");
            return;
        }

        VoxelShape shape = block.getCollisionShape();
        World world = p.getWorld();
        Vector offset = block.getLocation().toVector();

        for (BoundingBox box : shape.getBoundingBoxes()) {
            highlightBox(box, offset, world);
        }
    }

    @Override
    public void dispatchCompletions(CommandSender sender, Command command, String s, CompletionBuilder b) {

    }

    public static void highlightCollisions(Block block) {
        if (block == null || block.isEmpty() || !block.isCollidable())
            return;

        VoxelShape shape = block.getCollisionShape();
        World world = block.getWorld();
        Vector offset = block.getLocation().toVector();

        for (BoundingBox box : shape.getBoundingBoxes()) {
            highlightBox(box, offset, world);
        }
    }

    public static boolean collidesWidth(Block block, Location loc) {
        if (block == null || block.isEmpty() || !block.isCollidable())
            return false;

        Vector vec = loc.toVector().subtract(block.getLocation().toVector());
        VoxelShape shape = block.getCollisionShape();

        for (BoundingBox box : shape.getBoundingBoxes())
            if (box.contains(vec))
                return true;
        return false;
    }

    private static void highlightBox(BoundingBox box, Vector offset, World world) {
        double x1 = box.getMinX() + offset.getX();
        double y1 = box.getMinY() + offset.getY();
        double z1 = box.getMinZ() + offset.getZ();
        double x2 = box.getMaxX() + offset.getX();
        double y2 = box.getMaxY() + offset.getY();
        double z2 = box.getMaxZ() + offset.getZ();

        trace(world, x1, y1, z1, x2, y1, z1);
        trace(world, x2, y1, z1, x2, y1, z2);
        trace(world, x2, y1, z2, x1, y1, z2);
        trace(world, x1, y1, z2, x1, y1, z1);

        trace(world, x1, y2, z1, x2, y2, z1);
        trace(world, x2, y2, z1, x2, y2, z2);
        trace(world, x2, y2, z2, x1, y2, z2);
        trace(world, x1, y2, z2, x1, y2, z1);

        trace(world, x1, y1, z1, x1, y2, z1);
        trace(world, x2, y1, z1, x2, y2, z1);
        trace(world, x2, y1, z2, x2, y2, z2);
        trace(world, x1, y1, z2, x1, y2, z2);
    }

    private static void trace(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
        Location loc1 = new Location(world, x1, y1, z1);
        Location loc2 = new Location(world, x2, y2, z2);
        BlockDisplay ent = BlockDisplayRaytracer.trace(Material.WHITE_CONCRETE, loc1, loc2, 0.01, 20 * 3);
        ent.setGlowColorOverride(Color.WHITE);
        ent.setGlowing(true);
    }
}
