/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

/*
 * Copyright Intune Networks.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

import org.apache.maven.artifact.Artifact;

import org.apache.maven.plugin.dependency.resolvers.ResolveDependenciesMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.dependency.utils.DependencyStatusSets;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Creates a flat directory structure in the dependencies directory.
 */
@Mojo(name = "createFlatDep", requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class FlattenDependencyDirectory extends ResolveDependenciesMojo {

  
    @SuppressWarnings("CallToThreadDumpStack")
    public void execute() throws MojoExecutionException {
        getLog().info("Flattening dependency directory structure");
        DependencyStatusSets results = this.getDependencySets(false);

        Iterator<Artifact> iter = results.getResolvedDependencies().iterator();

        while (iter.hasNext()) {
            Artifact obj = iter.next();

            String pathStr = obj.getGroupId().replace(".", File.separator);

            String origDir = "dependency" + File.separator
                    + pathStr + File.separator
                    + obj.getArtifactId() + File.separator
                    + obj.getBaseVersion() + File.separator
                    + obj.getArtifactId() + "-"
                    + obj.getBaseVersion();

            getLog().info(origDir);

            String source = origDir;
            File srcDir = new File(source);

            String destination = "dependency" + File.separator + obj.getArtifactId();
            File destDir = new File(destination);

            try {
                // If the destination directory already exists, remove it.
                if ( destDir.exists() ) {
                    FileUtils.deleteDirectory(destDir);
                }
                //
                // Move the source directory to the destination directory.
                // The destination directory must not exists prior to the
                // move process.
                //
                FileUtils.moveDirectory(srcDir, destDir);
                
                // Remove the marker file.
                String marker = "dependency" + File.separator + 
                        obj.getGroupId() + 
                        "-" + obj.getArtifactId() + 
                        "-tar.gz-bin-" + obj.getBaseVersion() + ".marker";
                File markerFile = new File(marker);
                FileUtils.deleteQuietly(markerFile);
                
                // Remove the now redundant directories.
                String dirToRemoveStr = "dependency" + File.separator
                    + pathStr + File.separator
                    + obj.getArtifactId();
                File dirToRemove = new File(dirToRemoveStr);
                
                FileUtils.deleteQuietly(dirToRemove);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
