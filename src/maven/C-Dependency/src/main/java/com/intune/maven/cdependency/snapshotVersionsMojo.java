/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

import org.apache.maven.artifact.Artifact;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;



import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
/**
 *
 * Attempts to resolve unlocked snapshot dependency versions to the locked timestamp versions used in the build.
 * For example, an unlocked snapshot version like "1.0-SNAPSHOT" could be resolved to "1.0-20090128.202731-1".
 *
 * @author Paul Boyle
 * @goal list-snapshots
 * @requiresProject true
 * @requiresDirectInvocation true
 * @since 1.5
 */
public class snapshotVersionsMojo 
 extends AbstractMojo   {
    
    /**
     * The Maven Project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @since 1.5
     */
    private MavenProject project;
    
        /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     * @since 1.5
     */
    public MavenProject getProject()
    {
        return project;
    }

    /**
     * @component
     * @since 1.5
     */
    protected org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;
    // ------------------------------ FIELDS ------------------------------

    /**
     * Pattern to match a timestamped snapshot version. For example 1.0-20090128.202731-1
     */
    public final Pattern matchSnapshotRegex = Pattern.compile( "-SNAPSHOT" );
    
    /**
     * Setter for property 'project'.
     *
     * @param project Value to set for property 'project'.
     * @since 1.5
     */
    public void setProject( MavenProject project )
    {
        this.project = project;
    }
    
        /**
     * @component
     * @since 1.5
     */
    protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;
    
    
        /**
     * @parameter expression="${localRepository}"
     * @readonly
     * @since 1.5
     */
    protected ArtifactRepository localRepository;
    
    /**
     * Determine the timestamp version of the snapshot dependency used in the build.
     *
     * @param dep
     * @return The timestamp version if exists, otherwise the original snapshot dependency version is returned.
     */
    private String resolveSnapshotVersion( Dependency dep )
    {
        getLog().debug( "Resolving snapshot version for dependency: " + dep );

        String lockedVersion = dep.getVersion();

        
        String type = "pom";
        if(dep.getType().equals("tar.gz")) {
            type = "pom";
        } else {
            type = dep.getType();
        }

                                
        Artifact depArtifact =
            artifactFactory.createArtifact( dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getScope(),
                                            type );
        try
        {
            resolver.resolve( depArtifact, getProject().getRemoteArtifactRepositories(), localRepository );

            lockedVersion = depArtifact.getVersion();
        }
        catch ( Exception e )
        {
            getLog().error( e );
        }
        return lockedVersion;
    }
    
    
    private void printSnapshots(Collection dependencies) {
               Iterator iter = dependencies.iterator();

        while ( iter.hasNext() )
        {
            Dependency dep = (Dependency) iter.next();

            String version = dep.getVersion();

            Matcher versionMatcher = matchSnapshotRegex.matcher( version );
            if ( versionMatcher.find() && versionMatcher.end() == version.length() )
            {
                String lockedVersion = resolveSnapshotVersion( dep );
                getLog().info("Resolved SNAPSHOT version: " + dep.getArtifactId() + "-" + lockedVersion);
            }
        }
    }
    
    public void execute() throws MojoExecutionException {
            if ( getProject() != null  )
        {
           
            printSnapshots(  getProject().getDependencies() );
        }
    }
}
