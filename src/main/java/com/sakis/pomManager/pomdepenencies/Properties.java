package com.sakis.pomManager.pomdepenencies;

/**
 * Created by sakis on 4/17/2017.
 */
public class Properties {

    String name;
    String version;

    public Properties(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
