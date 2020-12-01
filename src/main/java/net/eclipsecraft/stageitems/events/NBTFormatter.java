package net.eclipsecraft.stageitems.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;

public class NBTFormatter {

    private static String str = "{";

    public static String getStuff(NBTTagCompound comp, EntityPlayer p){
        str = "{";
        NBTChecker(comp, p);
        str = str.substring(0,str.length()-1)+"}";
        return str;
    }
    private static void NBTChecker(NBTTagCompound comp, EntityPlayer p){
        for(String a : comp.getKeySet()){
            if(comp.hasKey(a, Constants.NBT.TAG_SHORT)){
                str = a + ": " + String.valueOf(comp.getShort(a)) + " as short,";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_FLOAT)){
                str = a + ": " + String.valueOf(comp.getFloat(a)) + " as float,";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_BYTE)){
                str = a + ": " + String.valueOf(comp.getByte(a)) + " as byte,";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_BYTE_ARRAY)){
                str = a + ": {";
                byte[] b = comp.getByteArray(a);
                for(byte bt : b){
                    str=str+bt+",";
                }
                str=str.substring(0,str.length()-1)+"} as byte[],";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_COMPOUND)){
                NBTChecker(comp.getCompoundTag(a),p);
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_DOUBLE)){
                str = a + ": " + String.valueOf(comp.getDouble(a)) + " as double,";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_INT)){
                str = a + ": " + String.valueOf(comp.getInteger(a)) + " as int,";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_INT_ARRAY)){
                str = a + ": {";
                int[] i = comp.getIntArray(a);
                for(int in : i){
                    str=str+in+",";
                }
                str=str.substring(0,str.length()-1)+"} as byte[],";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_LONG)){
                str = a + ": " + String.valueOf(comp.getLong(a)) + " as long,";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_STRING)){
                str = a + ": " + String.valueOf(comp.getString(a)) + ",";
            }
            else if(comp.hasKey(a, Constants.NBT.TAG_LIST)){
                String list = comp.getTag(a).toString()
                        .replace("s,"," as short,")
                        .replace("s}"," as short}")
                        .replace("l,"," as long,")
                        .replace("l}"," as long}");
                str = str + a + ": "+list+",";
            }
            else{
                str = str+",";
            }
        }
    }
}
