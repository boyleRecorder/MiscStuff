/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;

import org.apache.maven.plugin.dependency.resolvers.ResolveDependenciesMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.dependency.utils.DependencyStatusSets;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 *
 * Creates template 'dep_includes.mk' file.
 *
 */
@Mojo(name = "createDepIncludes", requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class CreateDepIncludes extends ResolveDependenciesMojo {

    public void execute() throws MojoExecutionException {
        getLog().info("Creating dep_include.mk");

        DependencyStatusSets results = this.getDependencySets(false);

        File file = new File("dep_includes.mk");
        FileWriter fw;

        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException ex) {
            getLog().error("Error opening file");
            throw new MojoExecutionException("Error opening file");

        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {

            bw.write("################################################################################\n");
            bw.write("# Include module_include.mk from components that this depends on.              #\n");
            bw.write("################################################################################\n\n");

            bw.write("LIBFLAGS += -Wl,--start-group\n");
        } catch (IOException ex) {
            getLog().error("Error writing to file");
            throw new MojoExecutionException("Error writing to file");
        }

        Iterator<Artifact> iter = results.getResolvedDependencies().iterator();
        while (iter.hasNext()) {
            Artifact obj = iter.next();
            String dirStrUpper = new CDependArtifactUtils().getArtifactDir(obj);

            String scope = obj.getScope();
            if (scope.equals("test")) {
                getLog().debug("Test scope: skipping include of module_include.mk for this dependency.");
            } else {
                String writeString = "include $(" + dirStrUpper + ")/module_include.mk";

                try {
                    bw.write(writeString);
                    bw.write("\n");
                } catch (IOException ex) {
                    getLog().error("Error writing to file");
                    throw new MojoExecutionException("Error writing to file");
                }
            }
        }
        try {
            bw.write("LIBFLAGS += -Wl,--end-group\n");
            bw.close();
        } catch (IOException ex) {
            getLog().error("Error closing file");
            throw new MojoExecutionException("Error on file close");
        }
    }
}
