package org.jboss.aerogear.plugin.dex2jar;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This will explode a aar, extract the dex file, and create a jar.
 */
@Mojo( name = "dex2jar")
public class Dex2JarMojo extends AbstractMojo {

    @Parameter(property = "dex2jar.aarGroupId", defaultValue = "${project.groupId}")
    private String aarGroupId;
    
    @Parameter(property = "dex2jar.aarArtifactId", defaultValue = "${project.artifactId}")
    private String aarArtifactId;
    
    @Parameter(property = "dex2jar.file")
    private String file;
    
    @Parameter(property = "dex2jar.version", defaultValue = "${project.version}")
    private String version;
    
    
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (file == null) {
            throw new MojoFailureException("There was no dex2jar file property set.");
        }
    }
    
}
