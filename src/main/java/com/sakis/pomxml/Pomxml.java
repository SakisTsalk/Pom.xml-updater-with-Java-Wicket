package com.sakis.pomxml;

import com.sakis.pomdepenencies.Dependencies;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sakis on 4/20/2017.
 */
public interface Pomxml {


    void GetPomResults(File fXmlFile, ArrayList<Dependencies> dependencylist,File responsefile);

    void UpdatePomFile(File fXmlFile, ArrayList<Dependencies> dependencylist,String filename);
}
