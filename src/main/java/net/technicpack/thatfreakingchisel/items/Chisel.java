package net.technicpack.thatfreakingchisel.items;

import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.microblock.ItemMicroPart;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.*;
import codechicken.multipart.handler.MultipartCompatiblity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.technicpack.thatfreakingchisel.blocks.SculpturePart;
import scala.Tuple2;

public class Chisel extends Item {

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(x, y, z, side, itemStack) || world.isAirBlock(x, y, z))
            return false;

        BlockCoord coord = new BlockCoord(x, y, z);
        TileMultipart tile = TileMultipart.getOrConvertTile(world, coord);

        if (tile != null && !world.isRemote) {
            ExtendedMOP mop = tile.collisionRayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
            if (mop != null) {
                Object object = mop.data;
                if (object instanceof Tuple2) {
                    TMultiPart part = tile.partList().apply(((Tuple2) object)._1());
                    if (part != null && part instanceof SculpturePart) {
                        if (((SculpturePart) part).chip(side))
                            MultipartHelper.sendDescPacket(world, tile);
                    }
                }
            }
        } else if (tile == null) {
            if (world.getTileEntity(x, y, z) != null)
                return false;

            Block block = world.getBlock(x, y, z);
            String materialName = block.getUnlocalizedName();

            MicroMaterialRegistry.IMicroMaterial microMaterial = MicroMaterialRegistry.getMaterial(materialName);
            if (microMaterial == null)
                return false;

            int metadata = world.getBlockMetadata(x, y, z);

            if (metadata != 0) {
                materialName = materialName+"_"+Integer.toString(metadata);
                microMaterial = MicroMaterialRegistry.getMaterial(materialName);

                if (microMaterial == null) {
                    //Things are gonna get real complicated- there's a very real possibility that we can only get the subblock's correct metadata
                    //and therefore micromaterial by picking the block.  So we'll try that, and if it doesn't work, just use the default
                    ItemStack item = block.getPickBlock(new MovingObjectPosition(x, y, z, side, Vec3.createVectorHelper(0,0,0)), world, x, y, z, player);
                    for(Tuple2<String, MicroMaterialRegistry.IMicroMaterial> entry : MicroMaterialRegistry.getIdMap()) {
                        if (entry._2().getItem().getItem() == item.getItem() && entry._2().getItem().getItemDamage() == item.getItemDamage()) {
                            materialName = entry._1();
                            break;
                        }
                    }
                    microMaterial = MicroMaterialRegistry.getMaterial(materialName);
                }

                if (microMaterial == null) {
                    materialName = block.getUnlocalizedName();
                    microMaterial = MicroMaterialRegistry.getMaterial(materialName);
                }
            }

            if (microMaterial == null)
                return false;

            if (!world.isRemote) {
                SculpturePart part = (SculpturePart)MultiPartRegistry.createPart("tfchisel:sculpture", false);
                part.setMicroMaterial(materialName);
                part.chip(side);

                world.setBlockToAir(x, y, z);
                if (part == null || !TileMultipart.canPlacePart(world, coord, part))
                    return false;

                TileMultipart.addPart(world, coord, part);
            }
        }

        return true;
    }
}
