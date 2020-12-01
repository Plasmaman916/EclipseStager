package net.eclipsecraft.stageitems.events;

import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Mod.EventBusSubscriber()
public class EventTest {

    private static boolean deactivate = false;
    private static boolean active = false;
    private static ArrayList<String> it = new ArrayList<>();
    private static EntityPlayer p;
    public static TextComponentString getComponent(String s){
        Random a = new Random();
        int b = a.nextInt();
        String loc = String.valueOf(b).substring(0,5);
        String finalLoc = Minecraft.getMinecraft().gameDir.toString()+"/EclipseStager/"+loc+".txt";
        File location = new File(finalLoc);
        try {
            File directory = new File(Minecraft.getMinecraft().gameDir.toString()+"/EclipseStager/");
            if (! directory.exists()){
                directory.mkdir();
            }
            location.createNewFile();
            FileWriter myWriter = new FileWriter(finalLoc);
            myWriter.write(TextFormatting.getTextWithoutFormattingCodes(s));
            myWriter.close();
        }catch (IOException e){e.printStackTrace();}
        TextComponentString t = new TextComponentString(s);
        ClickEvent c = new ClickEvent(ClickEvent.Action.OPEN_FILE,finalLoc);
        Style st = new Style().setClickEvent(c).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new TextComponentString("Click to copy!")));
        t.setStyle(st);
        return t;
    }
    @SubscribeEvent
    public static void talk(ClientChatEvent event){
        if(event.getMessage().equals("begin")){
            it.clear();
            active=true;
            p.sendMessage(new TextComponentString(TextFormatting.GREEN+"Began item checking!"));
        }
        if(event.getMessage().equals("end")){
            deactivate=true;
            for(ItemStack i : p.inventory.mainInventory){
                if(i != null && !i.isEmpty()){
                    Item item = i.copy().getItem();
                    NBTTagCompound tag = item.getNBTShareTag(i).copy();
                    String oof = "";
                    for(String key : tag.getKeySet()){
                        NBTBase nbtBase = tag.getTag(key);
                        oof = oof +nbtBase.toString() + " ";
                    }
                    String name = "<"+item.getRegistryName()+":"+i.getMetadata()+">"+oof;
                    if(!it.contains(name)){
                        it.add(name);
                    }
                    i.setCount(0);
                }
            }
            String n = "";
            for(String s : it){
                n=n+TextFormatting.BLUE+s+",\n";
            }
            if(n.length()>2) {
                n = n.substring(0, n.length() - 2);
            }
            n="var test() [\n"+n+"\n"+TextFormatting.BLUE+"] as string[];";
            p.sendMessage(getComponent(TextFormatting.BLUE+n));
        }
    }

    private static long time = 0;
    private static long total = 0;
    @SubscribeEvent
    public static void tickevent(TickEvent.PlayerTickEvent event) {
        long t = System.currentTimeMillis();
        long diff = t - time;
        time = t;
        total = total + diff;
        if (event.side == Side.CLIENT){
            p = event.player;
            if(total >= 300){
                if(active){
                    for(ItemStack i : event.player.inventory.mainInventory){
                        if(i != null && !i.isEmpty()){
                            Item item = i.copy().getItem();
                            String str = "";
                            if(i.hasTagCompound() && item.getRegistryName().toString().contains("minecraft:enchanted_book")) {
                                //Map<String,Pair<String,String>> map = getNBT(i);
                                NBTTagCompound nbt = i.getTagCompound();
                                str = (new NBTFormatter()).getStuff(nbt,p);
                            }
                            String name = "<"+item.getRegistryName()+":"+i.getMetadata()+">";
                            if(!str.equals("")){
                                name=name+".withTag("+str+")";
                            }
                            if(!it.contains(name)){
                                it.add(name);
                            }
                            i.setCount(0);
                        }
                    }
                    if(deactivate){
                        deactivate=false;
                        active=false;
                    }
                }
                total = 0;
            }
        }
    }
    public static Map<String, Pair<String, String>> getNBT(ItemStack i) {
        Map<String, Pair<String, String>> map = new HashMap<>();
        NBTTagCompound nbt = i.getTagCompound();
        for (String k : nbt.getKeySet()){

        }
        return map;
    }
}
