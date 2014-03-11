# Spring Batch Examples

Collection of Spring Batch Examples, covering the basics, [readers], [processor] [writers], and complex usecases.
include Spring Batch (Java configuration, Xml Configuration) 


Consider following batch jobs :

Step  : Read CSV files from folder A, process it uppercase character B, write it to folder C. “READ-PROCESS-WRITE”


## General Informations

All Spring Batch Examples:

* are individual github repositories and maven projects, the pom.xml in this root directory is only for a convenient _build all_ feature
* are tested with:
  * Spring Batch 2.2.5.RELEASE
  * Spring Framework 3.2.0.RELEASE
* are provided "as is", no guarantees :-)
* work with [mysql database][database], 

Overview for the project setup.

### Maven Configuration

The examples modules are:

* spring batch examples 
    * launcherforXml , launcher for xml configuration 
    * launcherforConfig, launcher for java configuration 


