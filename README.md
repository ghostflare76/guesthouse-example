# Spring Batch Examples

Collection of Spring Batch Examples, covering the basics, [readers], [processor] [writers], and complex usecases.

include Spring Batch (Java configuration, Xml Configuration) 


Consider following batch jobs :

Step  : “READ-PROCESS-WRITE”
* A : Read CSV files from folder , 
* B : process it (Converted lowercase to uppercase)  , 
* C : Make XML and write it to folder C. 


## General Informations

All Spring Batch Examples:

* are spring official repositories and maven projects, the pom.xml in this root directory is only for a convenient _build all_ feature
* are tested with:
  * Spring Batch 2.2.5.RELEASE
  * Spring Framework 3.2.0.RELEASE
* are provided "as is", no guarantees :-)
* work with [mysql database][127.0.0.1], 

Overview for the project setup.

### Maven Configuration

The examples modules are:

* spring batch examples 
    * launcherforXml , launcher for xml configuration 
    * launcherforConfig, launcher for java configuration 


