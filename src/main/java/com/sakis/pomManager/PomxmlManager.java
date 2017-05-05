package com.sakis.pomManager;

import com.sakis.pomManager.pomdepenencies.Dependencies;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sakis on 4/21/2017.
 */
public interface PomxmlManager extends  Serializable{

    void GetPomResults(File fXmlFile, ArrayList<Dependencies> dependencylist, File responsefile);

    void UpdatePomFile(File fXmlFile, ArrayList<Dependencies> dependencylist,String filename);
}
