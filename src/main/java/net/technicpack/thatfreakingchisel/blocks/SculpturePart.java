package net.technicpack.thatfreakingchisel.blocks;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroblockRender;
import codechicken.multipart.MultipartHelper;
import codechicken.multipart.TCuboidPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SculpturePart extends TMultiPart implements TCuboidPart, TNormalOcclusion {
    private String materialName = "tile.stone";
    private int[] chips = {0, 0, 0, 0, 0, 0};

    public void setMicroMaterial(String material) {
        this.materialName = material;
    }

    @Override
    public void readDesc(MCDataInput data) {
        super.readDesc(data);
        this.materialName = data.readString();
        for (int i = 0; i < 6; i++) {
            chips[i] = data.readInt();
        }
    }

    @Override
    public void writeDesc(MCDataOutput data) {
        super.writeDesc(data);
        data.writeString(this.materialName);

        for (int i = 0; i < 6; i++) {
            data.writeInt(chips[i]);
        }
    }

    @Override
    public void save(NBTTagCompound tag) {
        tag.setString("Material", this.materialName);
        tag.setIntArray("Chips", chips);
    }

    @Override
    public void load(NBTTagCompound tag) {
        if (tag.hasKey("Material", 8))
            this.materialName = tag.getString("Material");
        if (tag.hasKey("Chips", 11))
            this.chips = tag.getIntArray("Chips");
    }

    public boolean chip (int side) {
        int totalChips = chips[side] + chips[ForgeDirection.VALID_DIRECTIONS[side].getOpposite().ordinal()];

        if (totalChips < 15) {
            chips[side]++;
            return true;
        }

        return false;
    }

    public Iterable<Cuboid6> generateBoxes() {
        List<Cuboid6> cuboid6List = new ArrayList<Cuboid6>(1);
        double component = 1.0/16.0;
        cuboid6List.add(new Cuboid6(component*chips[4], component*chips[0], component*chips[2], 1.0 - (component*chips[5]), 1.0 - (component*chips[1]), 1.0 - (component*chips[3])));
        return cuboid6List;
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
        return generateBoxes();
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {
        return generateBoxes();
    }

    @Override
    public String getType() {
        return "tfchisel:sculpture";
    }

    @Override
    public Cuboid6 getBounds() {
        return generateBoxes().iterator().next();
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {
        Iterable<Cuboid6> boxes = generateBoxes();
        List<IndexedCuboid6> parts = new LinkedList<IndexedCuboid6>();
        for(Cuboid6 part : boxes)
            parts.add(new IndexedCuboid6(0, part));

        return parts;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vector3 pos, int pass) {
        MicroMaterialRegistry.IMicroMaterial material = MicroMaterialRegistry.getMaterial(this.materialName);
        if (material.canRenderInPass(pass)) {
            MicroblockRender.renderCuboid(pos, material, pass, getBounds(), 0);
            return true;
        }
        return false;
    }
}
