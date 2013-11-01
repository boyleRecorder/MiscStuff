/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

/*
 * Copyright Intune Networks.
 */
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugin.dependency.resolvers.ResolveDependenciesMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.dependency.utils.DependencyStatusSets;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.FileUtils;

/**
 * 
 */
@Mojo(name = "createLoad", requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class PackageLoad extends ResolveDependenciesMojo {

    private void createDirectory(String directoryName) {
        File theDir = new File(directoryName);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            getLog().debug("Creating directory: " + directoryName);

            boolean result = theDir.mkdirs();

            if (result) {
                getLog().debug("DIR created");
            }
        }
    }
    
    /**
     * What architecture are we creating a load for
     *
     * @since 1.5
     */
    @Parameter(property = "createLoad.arch", defaultValue = "x86")
    protected String arch;
    
    private String dependencyDir = "dependency";
    
    private String loadDirectory = "load";
    private String libDirectory = "lib";
    private String binDirectory = "bin";
    private String appDirectory = "app";
    private String testDirectory = "test";
       
    private void copyLibrary(String directoryName) {
        String relativeDir = project.getProperties().getProperty("relativeDir");


        File dir = null;
        if (relativeDir == null) {
            dir = new File(dependencyDir + File.separator + directoryName);
        } else {
            dir = new File(relativeDir + File.separator + dependencyDir + File.separator + directoryName);
        }

        getLog().debug("Directory: " + dir.getAbsolutePath());

        try {
            List<File> files = FileUtils.getFiles(dir, "*.so*", "");
            Iterator<File> iterator = files.iterator();

            while (iterator.hasNext()) {
                File obj = iterator.next();
                getLog().debug("Copying: " + obj.getAbsolutePath());
                FileUtils.copyFileToDirectory(obj.getAbsolutePath(), loadDirectory + File.separator + libDirectory);
            }

        } catch (IOException ex) {
            getLog().error(ex.toString());
        } catch (IllegalStateException ex) {
        }
    }

    private void copyApplication(String directoryName) {
        File dir = null;
        String relativeDir = project.getProperties().getProperty("relativeDir");

        if (relativeDir == null) {
            dir = new File(dependencyDir + File.separator + directoryName);
        } else {
            dir = new File(relativeDir + File.separator + dependencyDir + File.separator + directoryName);
        }

        try {
            List<File> files = FileUtils.getFiles(dir, "*", "");
            Iterator<File> iterator = files.iterator();


            while (iterator.hasNext()) {
                File obj = iterator.next();
                getLog().debug("Found: " + obj.getName());

                String parsedFile = obj.getName().replace("-" + arch + "-qnx", "");

                File newFile = new File(loadDirectory + File.separator + binDirectory + File.separator + parsedFile);

                FileUtils.copyFile(obj, newFile);
                newFile.setExecutable(true);
            }

        } catch (IOException ex) {
            getLog().error(ex.toString());
        } catch (IllegalStateException ex) {
            // Not really an error. 
        }
    }
    
    private void copyTestApplication(String directoryName) {
        File dir = null;
        String relativeDir = project.getProperties().getProperty("relativeDir");

        if (relativeDir == null) {
            dir = new File(dependencyDir + File.separator + directoryName);
        } else {
            dir = new File(relativeDir + File.separator + dependencyDir + File.separator + directoryName);
        }

        try {
            List<File> files = FileUtils.getFiles(dir, "*", "");
            Iterator<File> iterator = files.iterator();


            while (iterator.hasNext()) {
                File obj = iterator.next();
                getLog().debug("Found: " + obj.getName());

                String parsedFile = obj.getName().replace("-" + arch + "-qnx", "");

                File newFile = new File(loadDirectory + File.separator + testDirectory + File.separator + parsedFile);

                FileUtils.copyFile(obj, newFile);
                newFile.setExecutable(true);
            }

        } catch (IOException ex) {
            getLog().error(ex.toString());
        } catch (IllegalStateException ex) {
            // Not really an error. 
        }
    }

    public void execute() throws MojoExecutionException {
        getLog().info("Creating package load. Load for arch: " + arch);

        createDirectory(loadDirectory + File.separator + libDirectory);
        createDirectory(loadDirectory + File.separator + binDirectory);
        createDirectory(loadDirectory + File.separator + testDirectory);

        DependencyStatusSets results = this.getDependencySets(false);

        Iterator<Artifact> iter = results.getResolvedDependencies().iterator();

        while (iter.hasNext()) {
            Artifact obj = iter.next();

            String dirStrUpper = new CDependArtifactUtils().getArtifactPathString(obj);

            String scope = obj.getScope();
            if (scope.equals("test")) {
                // Nothing to do here.  
            } else {
                copyLibrary(dirStrUpper + File.separator + libDirectory + File.separator + arch + "-qnx");
                copyApplication(dirStrUpper + File.separator + appDirectory + File.separator + arch + "-qnx");
                copyTestApplication(dirStrUpper + File.separator + testDirectory + File.separator + arch + "-qnx");                
            }
        }
    }
}
