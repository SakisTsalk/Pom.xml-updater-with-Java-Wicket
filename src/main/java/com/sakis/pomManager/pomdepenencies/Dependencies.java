package com.sakis.pomManager.pomdepenencies;

import org.apache.wicket.util.io.IClusterable;

import java.io.Serializable;

/**
 * Created by sakis on 4/4/2017.
 */
public class Dependencies implements IClusterable {

    String groupid;
    String artifactid;
    String version;
    String newversion;
    String newversiondate;



    public Dependencies(String groupid, String artifactid, String version, String newversion, String newversiondate) {
        this.groupid = groupid;
        this.artifactid = artifactid;
        this.version = version;
        this.newversion = newversion;
        this.newversiondate = newversiondate;

    }

    public String getNewversiondate() {
        return newversiondate;
    }

    public void setNewversiondate(String newversiondate) {
        this.newversiondate = newversiondate;
    }

    public String getNewversion() {
        return newversion;
    }

    public void setNewversion(String newversion) {
        this.newversion = newversion;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getArtifactid() {
        return artifactid;
    }

    public void setArtifactid(String artifactid) {
        this.artifactid = artifactid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "GroupID =" + groupid + ", ArtifactID =" + artifactid + ", VersionID =" + version +", Newest Version ="+newversion+
                "]";
    }
}
