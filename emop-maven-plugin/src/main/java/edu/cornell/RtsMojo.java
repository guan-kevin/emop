package edu.cornell;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

@Mojo(name = "rts", requiresDirectInvocation = true, requiresDependencyResolution = ResolutionScope.TEST)
@Execute(phase = LifecyclePhase.PROCESS_TEST_CLASSES, lifecycle = "rts")
public class RpsMojo extends MonitorMojo {

    @Parameter(property = "tool", defaultValue = "starts")
    private String tool;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Component
    private BuildPluginManager manager;
    
    public void execute() throws MojoExecutionException {
        getLog().info("[eMOP] Invoking the RPS Mojo with RTS...");
        System.setProperty("exiting-rps", "false");

        long startRTS = System.currentTimeMillis();
        if (tool.equals("starts")) {
            getLog().info("Running starts:run");
            executeMojo(
                    plugin(groupId("edu.illinois"), artifactId("starts-maven-plugin"), version("1.4-SNAPSHOT")),
                    goal("run"),
                    configuration(),
                    executionEnvironment(
                            project,
                            session,
                            manager
                    )
            );
        } else if (tool.equals("ekstazi")){
            getLog().info("Running ekstazi:select");
            executeMojo(
                    plugin(groupId("org.ekstazi"), artifactId("ekstazi-maven-plugin"), version("5.3.0")),
                    goal("select"),
                    configuration(),
                    executionEnvironment(
                            project,
                            session,
                            manager
                    )
            );
        } else {
            throw new MojoExecutionException("Unknown RTS tool " + tool + ", currently only supports starts & ekstazi");
        }

        getLog().info("Finish selecting test with RTS in: " + (System.currentTimeMillis() - start) + " ms");
        long startTest = System.currentTimeMillis();
        getLog().info("Start running test");

        // TODO: Add agent?
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-surefire-plugin"),
                        version(System.getenv("SUREFIRE_VERSION") != null ? System.getenv("SUREFIRE_VERSION") : "3.1.2")
                ),
                goal("test"),
                configuration(),
                executionEnvironment(
                        project,
                        session,
                        manager
                )
        );

        getLog().info("Finish running test in: " + (System.currentTimeMillis() - startTest) + " ms");
        getLog().info("Finish running RTS in: " + (System.currentTimeMillis() - startRTS) + " ms");
    }
}
