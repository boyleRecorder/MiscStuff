/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

import java.io.File;
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.apache.maven.artifact.Artifact;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;

/**
 *
 * @author paul.boyle
 */
public class ArtifactUtilsTest extends TestCase {
    
    public ArtifactUtilsTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private class MockArtifact implements Artifact {
        
            String mockGroupId;
            String mockArtifactId;
            String mockBaseVersion;
            
            public String getGroupId() {
                return mockGroupId;
            }

            public String getArtifactId() {
                return mockArtifactId;
            }

            public String getVersion() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setVersion(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getScope() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getClassifier() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean hasClassifier() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public File getFile() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setFile(File file) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getBaseVersion() {
                return mockBaseVersion;

            }

            public void setBaseVersion(String string) {
                mockBaseVersion = string;
            }

            public String getId() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getDependencyConflictId() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void addMetadata(ArtifactMetadata am) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Collection<ArtifactMetadata> getMetadataList() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setRepository(ArtifactRepository ar) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public ArtifactRepository getRepository() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void updateVersion(String string, ArtifactRepository ar) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getDownloadUrl() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setDownloadUrl(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public ArtifactFilter getDependencyFilter() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setDependencyFilter(ArtifactFilter af) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public ArtifactHandler getArtifactHandler() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public List<String> getDependencyTrail() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setDependencyTrail(List<String> list) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setScope(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public VersionRange getVersionRange() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setVersionRange(VersionRange vr) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void selectVersion(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setGroupId(String string) {
                mockGroupId = string;
            }

            public void setArtifactId(String string) {
                mockArtifactId = string;
            }

            public boolean isSnapshot() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setResolved(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isResolved() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setResolvedVersion(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setArtifactHandler(ArtifactHandler ah) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isRelease() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setRelease(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public List<ArtifactVersion> getAvailableVersions() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setAvailableVersions(List<ArtifactVersion> list) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isOptional() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setOptional(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int compareTo(Artifact o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        public ArtifactMetadata getMetadata(Class<?> type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        };

    
    /**
     * Test of getArtifactDir method, of class CDependArtifactUtils.
     */
    public void testGetArtifactDir() {
        System.out.println("getArtifactDir");
        MockArtifact obj = new MockArtifact();

        
        CDependArtifactUtils instance = new CDependArtifactUtils();
        

        obj.setArtifactId("sample");
        obj.setGroupId("infrastructure.core");
        obj.setBaseVersion("1.0.2-SNAPSHOT");
        

        String expResult = "INFRASTRUCTURECORESAMPLE_DIR";
        String result = instance.getArtifactDir(obj);

        assertEquals(expResult, result);

    }
    
    public void testGetArtifactPathString() {
                System.out.println("getArtifactPathString");
        MockArtifact obj = new MockArtifact();

        
        CDependArtifactUtils instance = new CDependArtifactUtils();
        

        obj.setArtifactId("sample");
        obj.setGroupId("infrastructure.core");
        obj.setBaseVersion("1.0.2-SNAPSHOT");
        

        String result = instance.getArtifactPathString(obj);
        String expResult = "infrastructure/core/sample/1.0.2-SNAPSHOT/sample-1.0.2-SNAPSHOT";
        System.out.print(result);

        assertEquals(expResult, result);
    }
    
}
