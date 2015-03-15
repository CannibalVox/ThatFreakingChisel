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

package net.technicpack.thatfreakingchisel.multipart;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.technicpack.thatfreakingchisel.blocks.SculpturePart;

public class SculptureFactory implements MultiPartRegistry.IPartFactory {

    public void init() {
        MultiPartRegistry.registerParts(this, new String[] {"tfchisel:sculpture"});
    }

    @Override
    public TMultiPart createPart(String name, boolean client) {
        if (name.equals("tfchisel:sculpture"))
            return new SculpturePart();
        return null;
    }
}
