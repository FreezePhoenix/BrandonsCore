package com.brandon3055.brandonscore.client;

import codechicken.lib.util.SneakyUtils;
import com.brandon3055.brandonscore.BCConfig;
import com.brandon3055.brandonscore.client.gui.GuiToolkit.GuiLayout;
import com.brandon3055.brandonscore.client.render.GuiSpriteUploader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.brandon3055.brandonscore.BrandonsCore.MODID;

/**
 * Created by brandon3055 on 3/09/2016.
 */
public class BCSprites {
    public static final ResourceLocation LOCATION_GUI_ATLAS = new ResourceLocation(MODID, "textures/atlas/gui.png");
    public static final String RESOURCE_PREFIX = MODID.toLowerCase() + ":";

    private static GuiSpriteUploader guiSpriteUploader;
    private static final Set<ResourceLocation> registeredSprites = new HashSet<>();
    private static final Map<String, Material> matCache = new HashMap<>();

    public static final RenderType guiType = RenderType.makeType("gui", DefaultVertexFormats.POSITION_TEX, GL11.GL_QUADS, 256, RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(LOCATION_GUI_ATLAS, false, false))
            .transparency(RenderState.TRANSLUCENT_TRANSPARENCY)
            .cull(RenderState.CULL_DISABLED)
            .texturing(new RenderState.TexturingState("lighting", RenderSystem::disableLighting, SneakyUtils.none()))
            .build(false)
    );

    public static final RenderType guiColorType = RenderType.makeType("gui_color", DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256, RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(LOCATION_GUI_ATLAS, false, false))
            .transparency(RenderState.TRANSLUCENT_TRANSPARENCY)
            .cull(RenderState.CULL_DISABLED)
            .texturing(new RenderState.TexturingState("lighting", RenderSystem::disableLighting, SneakyUtils.none()))
            .build(false)
    );

    public static void register() {
        guiSpriteUploader = new GuiSpriteUploader(registeredSprites);

        //Gui Backgrounds
        Stream.of(GuiLayout.values()).filter(e -> e.xSize != -1).forEach(layout -> registerThemed(MODID, layout.textureName()));
        registerThemed(MODID, "background_dynamic");

        //Gui Sprites
        registerThemed(MODID, "button");
        registerThemed(MODID, "button_highlight");
        registerThemed(MODID, "button_disabled");
        registerThemed(MODID, "slot");
        registerThemed(MODID, "resize");
        registerThemed(MODID, "theme");
        registerThemed(MODID, "gear");
        registerThemed(MODID, "advanced");
        registerThemed(MODID, "arrow_left");
        registerThemed(MODID, "arrow_right");

        register(MODID, "add");
        register(MODID, "delete");
        register(MODID, "delete_all");
        register(MODID, "info_panel");

        register(MODID, "redstone/always_active");
        register(MODID, "redstone/active_high");
        register(MODID, "redstone/active_low");
        register(MODID, "redstone/never_active");

        register(MODID, "slots/fuel");
        register(MODID, "slots/energy");
        register(MODID, "slots/armor_boots");
        register(MODID, "slots/armor_chestplate");
        register(MODID, "slots/armor_helmet");
        register(MODID, "slots/armor_leggings");
        register(MODID, "slots/armor_shield");

        register(MODID, "item_charge/btn_right_charge");
        register(MODID, "item_charge/btn_right_discharge");
        register(MODID, "item_charge/btn_right_disabled");
        register(MODID, "item_charge/btn_vertical_charge");
        register(MODID, "item_charge/btn_vertical_discharge");
        register(MODID, "item_charge/btn_vertical_disabled");
        register(MODID, "item_charge/horizontal_charge");
        register(MODID, "item_charge/horizontal_discharge");
        register(MODID, "item_charge/right_charge");
        register(MODID, "item_charge/right_discharge");
        register(MODID, "item_charge/vertical_charge");
        register(MODID, "item_charge/vertical_discharge");
    }

    //region register

    public static void registerThemed(String modid, String location) {
        register(modid, "light/" + location);
        register(modid, "dark/" + location);
    }
    public static void register(String modid, String location) {
        register(new ResourceLocation(modid, location));
    }

    public static void register(ResourceLocation location) {
        registeredSprites.add(location);
    }

    //endregion
    public static Material getThemed(String modid, String location) {
        return get(modid, (BCConfig.darkMode ? "dark/" : "light/") + location);
    }

    public static Material getThemed(String location) {
        return get(MODID, (BCConfig.darkMode ? "dark/" : "light/") + location);
    }

    public static Material get(String modid, String location) {
        return matCache.computeIfAbsent(modid + ":" + location, s -> new CustomMat(LOCATION_GUI_ATLAS, new ResourceLocation(modid, location)));
    }

    public static Material get(String location) {
        return get(MODID, location);
    }

    public static Material getButton(int state) {
        return getThemed(state == 1 ? "button" : state == 2 ? "button_highlight" : "button_disabled");
    }

    private static String[] ARMOR_ORDER = new String[] {"slots/armor_boots", "slots/armor_leggings", "slots/armor_chestplate", "slots/armor_helmet"};
    public static Material getArmorSlot(int slot) {
        return get(ARMOR_ORDER[slot]);
    }

    public static RenderType makeType(ResourceLocation location) {
        return RenderType.makeType("sprite_type", DefaultVertexFormats.POSITION_TEX, GL11.GL_QUADS, 256, RenderType.State.getBuilder()
                .texture(new RenderState.TextureState(location, false, false))
                .transparency(RenderState.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderState.CULL_DISABLED)
                .texturing(new RenderState.TexturingState("lighting", RenderSystem::disableLighting, SneakyUtils.none()))
                .build(false));
    }


    @Deprecated //TODO remove this
    public static final ResourceLocation MODULAR_GUI = new ResourceLocation(RESOURCE_PREFIX + "textures/gui/modular_gui.png");
//
//    private static final ResourceLocation WIDGETS_LIGHT = new ResourceLocation(RESOURCE_PREFIX + "textures/gui/light/widgets.png");
//    private static final ResourceLocation WIDGETS_DARK = new ResourceLocation(RESOURCE_PREFIX + "textures/gui/dark/widgets.png");
//
//    //TODO Switch to an atlas sprite based system
//    @Deprecated
//    public static final ResourceLocation WIDGETS_GENERIC = new ResourceLocation(RESOURCE_PREFIX + "textures/gui/generic/widgets_generic.png");
//
//    public static ResourceLocation widgets() {
//        return BCConfig.darkMode ? WIDGETS_DARK : WIDGETS_LIGHT;
//    }

    private static class CustomMat extends Material {

        public CustomMat(ResourceLocation atlasLocationIn, ResourceLocation textureLocationIn) {
            super(atlasLocationIn, textureLocationIn);
        }

        @Override
        public TextureAtlasSprite getSprite() {
            return guiSpriteUploader.getSprite(getTextureLocation());
        }
    }
}
