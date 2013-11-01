/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

import org.apache.maven.artifact.Artifact;

/**
 *
 * @author paul.boyle
 */
public class CDependArtifactUtils {
    public CDependArtifactUtils() {
        
    }
 
    public String getArtifactDir(Artifact obj) {
        String dir;
        
        
            dir = obj.getGroupId().replace(".", "");
            dir += obj.getArtifactId();
            dir += "_DIR";
            String dirUpper = dir.toUpperCase();
        
        return dirUpper;
    }
    
    public String getArtifactPathString(Artifact obj) {
                    String pathStr = obj.getGroupId().replace(".", "/");
                    pathStr = pathStr + "/" + obj.getArtifactId() + "/" + obj.getBaseVersion() + "/" + obj.getArtifactId() + "-" + obj.getBaseVersion();
                    
                    return pathStr;
    }
}
