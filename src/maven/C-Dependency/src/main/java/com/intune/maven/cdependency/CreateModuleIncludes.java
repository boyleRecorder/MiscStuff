package com.intune.maven.cdependency;
/*
 * Copyright Intune Networks.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.project.MavenProject;

/**
 * Creates template 'module_include.mk' file.
 *
 * @goal createModuleIncludesTemplate
 * @threadSafe
 * @requiresProject
 */
public class CreateModuleIncludes extends AbstractMojo {

    /**
     * The maven project.
     *
     * @parameter property="project"
     * @readonly
     */
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        getLog().info("Creating module_include.mk");

        File file = new File("module_include.mk");
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
            bw.write("# Add LIBS, DEPENDENCY_INCLUDES amd LIBFLAGS required for the included component\n");
            bw.write("################################################################################\n\n");
            String artifact = project.getArtifactId();
            String group = project.getGroupId();

            String dirStr = group.replace(".", "");
            dirStr += artifact;
            dirStr += "_DIR";
            String dirUpper = dirStr.toUpperCase();

            bw.write("LIBS                +=  -L$(" + dirUpper + ")/lib/$(PLATFORM_OS)\n");
            bw.write("\n");
            bw.write("DEPENDENCY_INCLUDES +=  -I$(" + dirUpper + ")/include\n");
            bw.write("\n");
            bw.write("################################################################################\n");
            bw.write("# For libs and applications, the .so versions of the included libraries are used\n");
            bw.write("# For unit test and test apps, the .a versions (with/without coverate) are used\n");
            bw.write("#\n");
            bw.write("# The library or application makefiel must define the MAKEFILE_OUTPUT_TYPE\n");
            bw.write("# to ensure that the correct (.a or .so) artifact is used\n");
            bw.write("################################################################################\n");
            bw.write("\n");
            bw.write("ifeq ($(MAKEFILE_OUTPUT_TYPE), TEST)\n");
            bw.write("\n");
            bw.write("LIBFLAGS            += -Wl,-Bstatic \n");
            bw.write("\n");
            bw.write("else ifeq ($(MAKEFILE_OUTPUT_TYPE), PRODUCTION_APP)\n");
            bw.write("\n");
            bw.write("LIBFLAGS            += -Wl,-Bdynamic \n");
            bw.write("\n");
            bw.write("endif\n");
            bw.write("\n");
            bw.write("LIBFLAGS            += -Wl,-Bdynamic\n");

            getLog().info("Finished writing.");

        } catch (IOException ex) {
            getLog().error("Error writing to file");
            throw new MojoExecutionException("Error writing to file");
        }
        try {
            bw.close();
        } catch (IOException ex) {
            getLog().error("Error closing file");
            throw new MojoExecutionException("Error on file close");
        }
    }
}
