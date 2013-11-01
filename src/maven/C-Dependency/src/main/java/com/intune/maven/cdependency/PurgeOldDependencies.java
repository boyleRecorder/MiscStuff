/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;
/*
 * Copyright Intune Networks.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.dependency.resolvers.ResolveDependenciesMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.dependency.utils.DependencyStatusSets;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.FileUtils;

/**
 * Purges any unused dependencies from the dependency directory.
 */
@Mojo(name = "PurgeDependencies", requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class PurgeOldDependencies extends ResolveDependenciesMojo {

    public class PurgeException extends Exception {
        public PurgeException(String s) {
            super(s);
        }
    }
    private final String depDirectory = "dependency";
    private String getDependencyDirectoryString() {
        String relativeDir = project.getProperties().getProperty("relativeDir");
        
        String ret;
        if(relativeDir != null)
        {
            ret = relativeDir + File.separator + depDirectory;
                    
        } else {
            ret = depDirectory;
        }
        return ret;
    }

    private File[] listMarkerFiles() {

        String sDir = getDependencyDirectoryString();

        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".marker");
            }
        };

        File[] faFiles = new File(sDir).listFiles(filter);


        return faFiles;
    }

    Map<String, Boolean> getMarkerMap() throws MojoExecutionException {
        getLog().debug("getMarkerMap");
        Map<String, Boolean> myMap = new HashMap<String, Boolean>();

        DependencyStatusSets results = this.getDependencySets(false);

        Iterator<Artifact> iter = results.getResolvedDependencies().iterator();


        while (iter.hasNext()) {
            Artifact obj = iter.next();


            String marker = this.generateMarkerFile(obj);

            getLog().debug("Entering: " + marker);
            myMap.put(marker, false);
            getLog().debug(marker);
        }

        return myMap;
    }

    private String generateMarkerFile(Artifact obj) {
        String marker;

        marker = obj.getGroupId();
        marker += "-";

        marker += obj.getArtifactId();
        marker += "-tar.gz-bin-";
        marker += obj.getBaseVersion();
        marker += ".marker";

        return marker;
    }


    private void removeFile(String marker) throws IOException, PurgeException {
        String[] split = marker.split("-");

        String groupId = split[0];
        String artifactId = split[1];
        String m_versionId = split[4];

        if (split.length == 6) {
            m_versionId = split[4] + "-" + split[5];
        } else if (split.length == 5) {
            m_versionId = split[4];

        } else {
            throw new PurgeException("Invalid marker file name:" + marker);
        }

        String version = m_versionId.replaceAll(".marker", "");

        getLog().debug("Goign to remove");
        getLog().debug(groupId);
        getLog().debug(artifactId);
        getLog().debug(version);

        String dirStr = getDependencyDirectoryString() + File.separator + groupId.replace(".", File.separator);
        dirStr = dirStr + File.separator;
        dirStr = dirStr + artifactId;
        dirStr = dirStr + File.separator;
        dirStr = dirStr + version;

        getLog().info("Going to remove directory: " + dirStr);
        FileUtils.deleteDirectory(dirStr);
        String markerPath = getDependencyDirectoryString() + File.separator + marker;
        getLog().debug("Going to remove file: " + markerPath);

        File delFile = new File(markerPath);
        delFile.delete();
    }

    public void execute() throws MojoExecutionException {

        getLog().info("Purging dependencies");

        if (new File(getDependencyDirectoryString()).exists() == false) {
            getLog().info("No dependency directory exists. Nothing to purge.");
            return;
        }

        Map<String, Boolean> markerMap = getMarkerMap();
        File[] listMarkerFiles = listMarkerFiles();

        for (File f : listMarkerFiles) {
            getLog().debug("Searching: " + f.getName());
            if (markerMap.containsKey(f.getName())) {

                getLog().debug("Found: " + f.getName());
                markerMap.put(f.getName(), true);
            } else {
                getLog().debug("I can remove: " + f.getName());
                try {
                    removeFile(f.getName());
                } catch (PurgeException ex) {
                    getLog().error(ex.getMessage());
                    throw new MojoExecutionException("Fatal error attempting to remove file: " + f.getName());
                } catch (IOException ex) {
                    throw new MojoExecutionException("Error deleting directory.");
                }
            }
        }
    }
}
