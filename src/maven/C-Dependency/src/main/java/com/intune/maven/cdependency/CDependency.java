package com.intune.maven.cdependency;

/*
 * Copyright Intune Networks.
 */
import java.io.File;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugin.dependency.resolvers.ResolveDependenciesMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.dependency.utils.DependencyStatusSets;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Creates a 'C' Style makefile with macros pointing to all the dependencies.
 * 
 */
@Mojo(name = "createMakeDep", requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class CDependency extends ResolveDependenciesMojo {

    /**
     * Do we flatten the directory structure in the depMakeFile.mk
     *
     * @since 1.1
     */
    @Parameter( property = "createMakeDep.flattenDirectories", defaultValue = "false" )
    protected Boolean flattenDirectories;
    
    final String depMakeFile = "depMakeFile.mk";
    
    public void execute() throws MojoExecutionException {
        getLog().info("Creating: " + depMakeFile);
        getLog().info("flattenDirectories is: " + this.flattenDirectories);
        DependencyStatusSets results = this.getDependencySets(false);

        Iterator<Artifact> iter = results.getResolvedDependencies().iterator();

        String relativeDir = project.getProperties().getProperty("relativeDir");
        ParseDepMakeFile parseDepMakeFile = new ParseDepMakeFile(relativeDir, getLog(), depMakeFile);

        while (iter.hasNext()) {
            Artifact obj = iter.next();

            String dirStrUpper = new CDependArtifactUtils().getArtifactDir(obj);

            if (flattenDirectories) {
                dirStrUpper += " := $(CR1_ROOT_DIR)/dependency" + File.separator + obj.getArtifactId();
            } else {
                dirStrUpper += " := $(CR1_ROOT_DIR)/dependency/" + new CDependArtifactUtils().getArtifactPathString(obj);
            }

            parseDepMakeFile.addDependency(dirStrUpper);
        }

        parseDepMakeFile.writeDepMakeFile();
    }
}
