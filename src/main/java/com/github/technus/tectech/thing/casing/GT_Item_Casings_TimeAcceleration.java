package com.github.technus.tectech.thing.casing;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static gregtech.api.enums.GT_Values.AuthorColen;

public class GT_Item_Casings_TimeAcceleration extends GT_Item_Casings_Abstract {
    public GT_Item_Casings_TimeAcceleration(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        switch (aStack.getItemDamage()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                aList.add(EnumChatFormatting.AQUA.toString()
                        + EnumChatFormatting.BOLD +
                        "Time dilation in a box.");
                break;
            default:
                aList.add(EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "Error, report to GTNH team");
        }
        aList.add(AuthorColen);
    }
}
