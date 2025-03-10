package com.brandon3055.brandonscore.utils;

import com.brandon3055.brandonscore.lib.Vec3D;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by Brandon on 25/07/2014.
 */
@SuppressWarnings("ALL")
public class Utils {

    public static final String SELECT = "\u00A7";
    private static DecimalFormat energyValue = new DecimalFormat("###,###,###,###,###");

    public static String formatNumber(double value) {
        if (value < 1000D) return String.valueOf(value);
        else if (value < 1000000D) return addCommas((int) value); //I mean whats the ploint of displaying 1.235K instead of 1,235?
        else if (value < 1000000000D) return String.valueOf(Math.round(value / 1000D) / 1000D) + "M";
        else if (value < 1000000000000D) return String.valueOf(Math.round(value / 1000000D) / 1000D) + "B";
        else return String.valueOf(Math.round(value / 1000000000D) / 1000D) + "T";
    }

    public static String formatNumber(long value) {
        if (value < 1000L) return String.valueOf(value);
        else if (value < 1000000L) return addCommas(value); //I mean whats the ploint of displaying 1.235K instead of 1,235?
        else if (value < 1000000000L) return String.valueOf(Math.round(value / 1000L) / 1000D) + "M";
        else if (value < 1000000000000L) return String.valueOf(Math.round(value / 1000000L) / 1000D) + "G";
        else if (value < 1000000000000000L) return String.valueOf(Math.round(value / 1000000000L) / 1000D) + "T";
        else if (value < 1000000000000000000L) return String.valueOf(Math.round(value / 1000000000000L) / 1000D) + "P";
        else if (value <= Long.MAX_VALUE) return String.valueOf(Math.round(value / 1000000000000000L) / 1000D) + "E";
        else return "Something is very broken!!!!";
    }

    /**
     * Add commas to a number e.g. 161253126 > 161,253,126
     */
    public static String addCommas(int value) {
        return energyValue.format(value);
    }

    /**
     * Add commas to a number e.g. 161253126 > 161,253,126
     */
    public static String addCommas(long value) {
        return energyValue.format(value);
    }

    /**
     * Calculates the exact distance between two points in 3D space
     *
     * @param x1 point A x
     * @param y1 point A y
     * @param z1 point A z
     * @param x2 point B x
     * @param y2 point B y
     * @param z2 point B z
     * @return The distance between point A and point B
     */
    public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }

    public static double getDistanceAtoB(Vec3D pos1, Vec3D pos2) {
        return getDistanceAtoB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public static int getCardinalDistance(BlockPos pos1, BlockPos pos2) {
        int x = Math.abs(pos2.getX() - pos1.getX());
        int y = Math.abs(pos2.getY() - pos1.getY());
        int z = Math.abs(pos2.getZ() - pos1.getZ());
        return Math.max(Math.max(x, y), z);
    }

    public static boolean inRangeSphere(BlockPos posA, BlockPos posB, int range) {
        if (Math.abs(posA.getX() - posB.getX()) > range || Math.abs(posA.getY() - posB.getY()) > range || Math.abs(posA.getZ() - posB.getZ()) > range) {
            return false;
        } else
            return getDistanceSq(posA.getX(), posA.getY(), posA.getZ(), posB.getX(), posB.getY(), posB.getZ()) <= range * range;
    }

    /**
     * Calculates the exact distance between two points in 2D space
     *
     * @param x1 point A x
     * @param z1 point A z
     * @param x2 point B x
     * @param z2 point B z
     * @return The distance between point A and point B
     */
    public static double getDistanceAtoB(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }

    public static double getDistanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double getDistanceSq(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz;
    }

//    /**
//     * Update the blocks an all 6 sides of a blocks.
//     */
//    public static void updateNeabourBlocks(World world, BlockPos pos) {
//        world.notifyNeighborsOfStateChange(pos, world.getBlockState(pos).getBlock(), true);
//    }//Just use the method in world directly

    /**
     * Determine the orientation of a blocks based on the position of the entity that placed it.
     */
    public static int determineOrientation(int x, int y, int z, LivingEntity entity) {
        if (MathHelper.abs((float) entity.getX() - (float) x) < 2.0F && MathHelper.abs((float) entity.getZ() - (float) z) < 2.0F) {
            double d0 = entity.getY() + 1.82D - (double) entity.getMyRidingOffset();

            if (d0 - (double) y > 2.0D) return 0;

            if ((double) y - d0 > 0.0D) return 1;
        }

        int l = MathHelper.floor((double) (entity.yRot * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 3 : (l == 1 ? 4 : (l == 2 ? 2 : (l == 3 ? 5 : 0)));
    }

    /**
     * Simple method to convert a Double object to a primitive int
     */
    public static int toInt(double d) {
        return (int) d;
    }

    public static int parseInt(String s) {
        return parseInt(s, true);
    }

    public static int parseInt(String s, boolean catchException) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        try {
            return Integer.parseInt(s);
        }
        catch (Exception e) {
            if (catchException) {
                return 0;
            }
            throw e;
        }
    }

    public static double parseDouble(String s, boolean catchException) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        try {
            return Double.parseDouble(s);
        }
        catch (Exception e) {
            if (catchException) {
                return 0;
            }
            throw e;
        }
    }

    public static double parseDouble(String s) {
        return parseDouble(s, true);
    }

    public static int parseHex(String s) {
        return parseHex(s, true);
    }

    public static int parseHex(String s, boolean catchException) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        if (catchException) {
            try {
                return (int) Long.parseLong(s, 16);
            }
            catch (Exception e) {
                return 0;
            }
        } else {
            return (int) Long.parseLong(s, 16);
        }
    }

    public static boolean validInteger(String value) {
        try {
            Long.parseLong(value);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean validDouble(String value) {
        try {
            Double.parseDouble(value);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Nullable
    public static PlayerEntity getClosestPlayer(World world, double posX, double posY, double posZ, double distance) {
        return getClosestPlayer(world, posX, posY, posZ, distance, true);
    }

    @Nullable
    public static PlayerEntity getClosestPlayer(World world, double posX, double posY, double posZ, double distance, boolean includeCreative) {
        return getClosestPlayer(world, posX, posY, posZ, distance, includeCreative, false);
    }


    @Nullable
    @Deprecated //Check the world method
    public static PlayerEntity getClosestPlayer(World world, double posX, double posY, double posZ, double distance, boolean includeCreative, boolean includeSpectators) {
        double d0 = -1.0D;
        PlayerEntity closestPlayer = null;

        for (int i = 0; i < world.players().size(); ++i) {
            PlayerEntity player = world.players().get(i);

            if ((!player.isCreative() || includeCreative) && (!player.isSpectator() || includeSpectators)) {
                double d1 = player.distanceToSqr(posX, posY, posZ);

                if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0)) {
                    d0 = d1;
                    closestPlayer = player;
                }
            }
        }

        return closestPlayer;
    }

    /**
     * This is mostly just here as a reminder to myself.<br>
     * This check is the reverse of the usual object instance of TargetClass check so i always get it backwards.<br>
     * This is more like TargetClass instanceof ObjectClass.
     */
    public static boolean checkClassInstanceOf(Class clazz, Class instanceOfThis) {
        if (clazz == null || instanceOfThis == null) {
            return clazz == null && instanceOfThis == null;
        }
        return instanceOfThis.isAssignableFrom(clazz);
    }

    public static String trimString(String input, int length, String trimExtension) {
        if (input.length() <= length) {
            return input;
        } else {
            return input.substring(0, length) + trimExtension;
        }
    }

    public static String getClipboardString() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception var1) {}

        return "";
    }

    public static void setClipboardString(String copyText) {
        if (!StringUtils.isEmpty(copyText)) {
            try {
                StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
            }
            catch (Exception var2) {
                var2.printStackTrace();
            }
        }
    }

    public static void openWebLink(URI url) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke((Object) null);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        }
        catch (Throwable throwable1) {
            Throwable throwable = throwable1.getCause();
            LogHelperBC.error("Couldn't open link: {}", (Object) (throwable == null ? "<UNKNOWN>" : throwable.getMessage()));
        }
    }

    public static int parseColourRGB(String value) {
        if (value.startsWith("0x") || value.startsWith("#")) {
            value = value.replace("0x", "").replace("#", "");
            return parseHex(value, false);
        } else if (value.contains(",")) {
            String[] vals = value.split(",");
            if (vals.length != 3)
                throw new NumberFormatException("Number must be a hex using the format 0xRRGGBB or #RRGGBB");
            int r = vals[0].contains(".") ? (int) (Double.parseDouble(vals[0]) * 255) : Integer.parseInt(vals[0]);
            int g = vals[1].contains(".") ? (int) (Double.parseDouble(vals[1]) * 255) : Integer.parseInt(vals[1]);
            int b = vals[2].contains(".") ? (int) (Double.parseDouble(vals[2]) * 255) : Integer.parseInt(vals[2]);
            return r << 16 | g << 8 | b;
        } else {
            throw new NumberFormatException("Number must be a hex using the format 0xRRGGBB or #RRGGBB");
        }
    }

    public static int parseColourARGB(String value) {
        if (value.startsWith("0x") || value.startsWith("#")) {
            value = value.replace("0x", "").replace("#", "");
            return parseHex(value, false);
        } else if (value.contains(",")) {
            String[] vals = value.split(",");
            if (vals.length < 3 || vals.length > 4) {
                throw new NumberFormatException("Number must be a hex using the format 0xAARRGGBB or #AARRGGBB");
            }
            int r = vals[0].contains(".") ? (int) (Double.parseDouble(vals[0]) * 255) : Integer.parseInt(vals[0]);
            int g = vals[1].contains(".") ? (int) (Double.parseDouble(vals[1]) * 255) : Integer.parseInt(vals[1]);
            int b = vals[2].contains(".") ? (int) (Double.parseDouble(vals[2]) * 255) : Integer.parseInt(vals[2]);
            int a = vals.length == 4 ? vals[3].contains(".") ? (int) (Double.parseDouble(vals[3]) * 255) : Integer.parseInt(vals[3]) : 0xFF;
            return a << 24 | r << 16 | g << 8 | b;
        } else {
            throw new NumberFormatException("Number must be a hex using the format 0xRRGGBB or #RRGGBB");
        }
    }

    private static BiMap<Block, Fluid> fluidBlocks = null;
    public static Fluid lookupFluidForBlock(Block block)
    {
        if (fluidBlocks == null)
        {
            BiMap<Block, Fluid> tmp = HashBiMap.create();
            for (Fluid fluid : ForgeRegistries.FLUIDS.getValues())
            {
                Block fluidBlock = fluid.defaultFluidState().createLegacyBlock().getBlock();
                if (fluidBlock != Blocks.AIR)
                {
                    tmp.put(fluidBlock, fluid);
                }
            }
            fluidBlocks = tmp;
        }
        return fluidBlocks.get(block);
    }

    public static String getTextFormatString(String stringIn) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = -1;
        int j = stringIn.length();

        while((i = stringIn.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                TextFormatting textformatting = TextFormatting.getByCode(stringIn.charAt(i + 1));
                if (textformatting != null) {
                    if (!textformatting.isFormat()) {
                        stringbuilder.setLength(0);
                    }

                    if (textformatting != TextFormatting.RESET) {
                        stringbuilder.append((Object)textformatting);
                    }
                }
            }
        }

        return stringbuilder.toString();
    }

    public static boolean isAreaLoaded(World world, BlockPos pos, ChunkHolder.LocationType minimum) {
        ChunkPos chunkPos = new ChunkPos(pos);
        IChunk ichunk = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false);
        if (!(ichunk instanceof Chunk)) {
            return false;
        }

        ChunkHolder.LocationType locationType = ((Chunk) ichunk).getFullStatus();
        return locationType.isOrAfter(minimum);
    }
}

