# eMOP
eMOP is a Maven plugin that realizes evolution-aware runtime verification through the following techniques:

1. Regression Property Selection (RPS)
2. Violation Message Suppression (VMS)
3. Regression Property Prioritization (RPP)

## Table of Contents

1. [Prerequisites](#Prerequisites)
2. [Installation](#Installation)
3. [Integration](#Integration)
4. [Usages](#Usages)
5. [Options](#Options)
6. [Citation](#Citation)

## Prerequisites

### Java

Use Java 8 (Oracle JDK, Open JDK)

### STARTS Maven Plugin

Make sure you install the `impacted-both-ways` branch of [STARTS](https://github.com/TestingResearchIllinois/starts) by following its installation guide, or run the following:

```bash
bash scripts/install-starts.sh
```

### JavaMOP

To install `JavaMOP`, clone [JavaMOP](https://github.com/owolabileg/javamop) and follow the installation guide there.

### JavaMOP Agent

To install `JavaMOP` agent, clone [JavaMOP Agent](https://github.com/SoftEngResearch/javamop-agent-bundle), checkout the `record-spec-list` branch, and follow the installation guide there.

### Maven

Use Maven version `3.3.9` or above.

### Maven Surefire Plugin

`maven-surefire-plugin` has to have a version number of `2.20` or higher.

## Installation

To install eMOP as a plugin in your Maven local repository, run

```bash
mvn install
```

## Integration

To integrate eMOP as a plugin into your Maven project, add the following segment to the plugins section under the build section in your `pom.xml`:

```xml
<project>
  ...
  <build>
    ...
    <plugins>
      ...
      <plugin>
      	<groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.20</version> <!-- 2.20 or greater -->
        <configuration>
          <!-- Use $HOME/.m2/repository/javamop-agent/javamop-agent/1.0/javamop-agent-1.0.jar -->
          <!-- if you do not have a specific javamop agent jar that you want to use. -->
        	<argLine>-javaagent:${JavaMOP jar}</argLine>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>emop-maven-plugin</artifactId>
        <groupId>edu.cornell</groupId>
        <version>1.0-SNAPSHOT</version>
      </plugin>
    </plugins>
  </build>
</project>
```

## Usages

Invoke various eMOP goals with the following commands:

1. `mvn emop:help # list all goals`
2. `mvn emop:impacted # list impacted classes`
3. `mvn emop:affected-specs # list affected speciﬁcations`
4. `mvn emop:rps # run RPS`
5. `mvn emop:vms # run VMS`
6. `mvn emop:rpp # run RPP`
7. `mvn emop:rps−vms # run RPS+VMS`
8. `mvn emop:rpp−vms # run RPS+VMS`
9. `mvn emop:rps−rpp # run RPS+RPP`
10. `mvn emop:rps−rpp-vms # run RPS+RPP+VMS`
11. `mvn emop:clean # delete all metadata`

## Options

### RPS Options

- `closureOption` (default: `PS3`) determines which option to use for computing impacted classes.
   - `PS3` will only instrument properties related to the set of changed and new classes (Δ), and the dependents of Δ (classes that depend on those in Δ).
   - `PS2` will instrument all properties that `PS3` instruments, along with properties impacted by the dependees of Δ (classes that those in Δ depend on).
   - `PS1` will instrument all properties that `PS2` instruments, along with properties impacted by the dependees of dependents of Δ. This is the safest option out of the three.
- `includeLibraries` (default: `true`) indicates whether third-party library classes should be monitored. Setting this option to `false` excludes all third-party library classes from monitoring, resulting in a weak RPS variant with an *l* in its superscript.
- `includeNonAffected` (default: `true`) indicates whether unaffected classes in the program should be monitored. Setting this option to `false` excludes all unaffected classes from monitoring, resulting in a RPS variant with an *c* in its superscript.
- `javamopAgent` indicates the location of the JavaMOP jar. The default location is `${M2_HOME}/javamop-agent/javamop-agent/1.0/javamop-agent-1.0.jar`.

### VMS Options

- `lastSha` indicates the sha against which "new" code will be determined. By default, the last sha VMS was run on will be used.
- `newSha` indicates the sha for which "new" code will be determined. By default, the working tree will be used.
- `showAllInConsole` (default: `false`) indicates whether all violations should be shown in the console.
- `showAllInFile` (default: `false`) indicates whether all violations should be shown in the `violation-counts` file.
- `javamopAgent` indicates the location of the JavaMOP jar. The default location is `${M2_HOME}/javamop-agent/javamop-agent/1.0/javamop-agent-1.0.jar`.

### RPP Options

- `backgroundSpecsFile` indicates the location of a file containing the set of background properties. The file should contain a newline-delimited list of property names. In the default case, either (1) it is the first invocation of RPP, in which RPP will not run anything in the background phase, or (2) RPP will run previously non-violated properties in the background phase.
- `criticalSpecsFile` indicates the location of a file containing the set of critical properties. The file should contain a newline-delimited list of property names. In the default case, either (1) it is the first invocation of RPP, in which RPP will run all properties in the critical phase, or (2) RPP will run previously violated properties in the critical phase.
- `demoteCritical` (default: `false`) indicates whether properties that were in the critical set but did not get violated on the current run should be designated to the background set for the next iteration.
- `javamopAgent` indicates the location of the JavaMOP jar. The default location is `${M2_HOME}/javamop-agent/javamop-agent/1.0/javamop-agent-1.0.jar`.

## Citation

Title: [Techniques for Evolution-Aware Runtime Verification](https://www.cs.cornell.edu/~legunsen/pubs/LegunsenETAL19eMOP.pdf)

Authors: Owolabi Legunsen, Yi Zhang, Milica Hadzi-Tanovic, Grigore Rosu, Darko Marinov

```bibtex
@inproceedings{yorihiro2023emop,
  author =       {Yorihiro, Ayaka and Jiang, Pengyue and Marqu{\'e}s, Valeria
                 and Carleton, Benjamin and Legunsen, Owolabi},
  title =        {eMOP: A Maven Plugin for Evolution-Aware Runtime Verification},
  booktitle =    {RV},
  year =         {2023},
  pages =        {363-375},
  organization = {Springer}
}
@inproceedings{LegunsenETALeMOPICST2019,
  author =       {Legunsen, Owolabi and Zhang, Yi and Hadzi-Tanovic, Milica
                  and Rosu, Grigore and Marinov, Darko},
  title =        {Techniques for Evolution-Aware Runtime Verification},
  booktitle =    {ICST},
  year =         {2019},
  pages =        {300-311}
}
```
