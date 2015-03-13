/*
 * This file is part of That Freaking Chisel
 * Copyright ©2015 Syndicate, LLC
 *
 * That Freaking Chisel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * That Freaking Chisel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * as well as a copy of the GNU Lesser General Public License,
 * along with Technic Launcher Core.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.technicpack.thatfreakingchisel;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid = ThatFreakingChisel.MODID, version = ThatFreakingChisel.VERSION)
public class ThatFreakingChisel
{
    public static final String MODID = "thatfreakingchisel";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        Item chisel = new Item().setUnlocalizedName("chisel").setTextureName("chisel").setMaxDamage(1024).setCreativeTab(CreativeTabs.tabTools).setMaxStackSize(1);
        GameRegistry.registerItem(chisel, "thatfreakingchisel");
    }
}
