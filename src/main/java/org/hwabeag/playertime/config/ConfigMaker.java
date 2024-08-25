package org.hwabeag.playertime.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigMaker {
    private final File file;
    private FileConfiguration config;

    public ConfigMaker(String path, String fileName){
        this.file = new File(path + "/" + fileName);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig(){
        return config;
    }

    public void saveConfig(){
        if(config == null)
            return;
        try{
            this.config.save(this.file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean exists(){
        return file != null && file.exists();
    }

    public void reloadConfig(){
        if(!exists())
            return;
        config = YamlConfiguration.loadConfiguration(file);
    }

}