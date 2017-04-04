package com.sakis.pomdepenencies;

/**
 * Created by sakis on 4/4/2017.
 */
public class Dependencies {

    String groupid;
    String artifactid;
    String version;

    public Dependencies(String groupid, String artifactid, String version) {
        this.groupid = groupid;
        this.artifactid = artifactid;
        this.version = version;
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
}
