/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intune.maven.cdependency;

import java.util.List;
import junit.framework.TestCase;
import org.apache.maven.plugin.logging.Log;

/**
 *
 * @author paul.boyle
 */
public class ParseDepMakeFileTest extends TestCase {
    
    private class testLog implements Log {

        public boolean isDebugEnabled() {
            return true;
        }

        public void debug(CharSequence cs) {
            System.out.println(cs);
        }

        public void debug(CharSequence cs, Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void debug(Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isInfoEnabled() {
            return true;
        }

        public void info(CharSequence cs) {
            System.out.println(cs);
        }

        public void info(CharSequence cs, Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void info(Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isWarnEnabled() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void warn(CharSequence cs) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void warn(CharSequence cs, Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void warn(Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isErrorEnabled() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void error(CharSequence cs) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void error(CharSequence cs, Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void error(Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    public ParseDepMakeFileTest(String testName) {
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

    /**
     * Test of getFileLines method, of class ParseDepMakeFile when there is no depMakeFile.mk.
     */
    public void testGetFileLinesNoDepMake() {

        
        System.out.println("getFileLines");
        ParseDepMakeFile instance = new ParseDepMakeFile("./",new testLog(), "depMakeFile.mk");
        List expResult = null;
        List result = instance.getFileLines();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
        instance.addDependency("TOOLSGOOGLEPROTOBUF_DIR := $(CR1_ROOT_DIR)/dependency/tools/google/protobuf/1.0.3/protobuf-1.0.3");
        
        result = instance.getFileLines();
        assert(result!=null);   
        instance.printContents();
    }
    
    /**
     * Test of getFileLines method, of class ParseDepMakeFile when there exists a depMakeFile.mk.
     */
    public void testGetFileLines() {
        
        System.out.println("getFileLines");
        ParseDepMakeFile instance = new ParseDepMakeFile("./",new testLog(), "src/test/resources/depMakeFile.mk");
        List result = instance.getFileLines();
        assert(result != null);
        
    }
    
        /**
     * Test of getFileLines method, of class ParseDepMakeFile when there exists a depMakeFile.mk.
     */
    public void testMultiAddDependencies() {

        System.out.println("MultiAddDependencies");
        ParseDepMakeFile instance = new ParseDepMakeFile("./",new testLog(), "src/test/resources/depMakeFile.mk");
        List result = instance.getFileLines();
        assert(result != null);
        
        // The instance should already have google
        // Replace the dependency with a new version number.
        String newDep = "TOOLSGOOGLEPROTOBUF_DIR := $(CR1_ROOT_DIR)/dependency/tools/google/protobuf/1.0.4/protobuf-1.0.4";
        
        assertEquals(instance.hasDependency(newDep), false);
                
        instance.addDependency(newDep);

        assertEquals(instance.hasDependency(newDep),true);
        
        
    }
            /**
     * Test of getFileLines method, of class ParseDepMakeFile when there exists a depMakeFile.mk.
     */
    public void testNullRelativeDirectory() {

        System.out.println("MultiAddDependencies");
        ParseDepMakeFile instance = new ParseDepMakeFile(null,new testLog(), "src/test/resources/depMakeFile.mk");
        List result = instance.getFileLines();
        assert(result != null);
        
        // The instance should already have google
        // Replace the dependency with a new version number.
        String newDep = "TOOLSGOOGLEPROTOBUF_DIR := $(CR1_ROOT_DIR)/dependency/tools/google/protobuf/1.0.4/protobuf-1.0.4";
        
        assertEquals(instance.hasDependency(newDep), false);
                
        instance.addDependency(newDep);

        assertEquals(instance.hasDependency(newDep),true);
        
        
    }
}
