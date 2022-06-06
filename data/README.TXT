ACDG is an automatic UML class diagram generator based on the 
Bullwinkle and the PlantUML.

Command line options:

	-addfwc : 			
Add source file
(any number of file paths separated by spaces)
			
	-addclasses : 		
Show an additional class on the diagram
(any number separated by spaces)
			
	-setprojname : 	
Set project name
(last parameter specified)
 
	-setpl : 			
Select a program language
Java, C#, C++
Currently only Java language is supported
(last parameter specified)
			
	-allfwcfromdir : 	
Load all files of the selected program language 
from the directory
(any number of directory 
paths separated by spaces)
			
	-exclude : 
Exclude any file that contains
the given substring. The option is used together with
the "allfwcfromdir" option. Must be located before it.
(last parameter specified)
			
	-setppm : 
Select a parameter processing mode
ONLY_DATATYPE, ONLY_ID, ALL
(last parameter specified)

	--use-smetana :
Use built-in Smetana engine instead of Graphviz.

	--user-def-pl-grammar :
Set BNF-grammar file for the user-defined programming 
language mode.
(last parameter specified)

	--user-def-pl-strategy :
Select a programming language whose XML-tree processing 
strategy will be used for the user-defined programming 
language mode.
(last parameter specified)

	--user-def-pl-ext :
Set file extension for the user-defined programming 
language mode.
(last parameter specified)

	--user-def-pl :
Select the user-defined programming language mode. 
Apply this option only after the previous "--user-def-pl"
options!

	--simple-class-descr :
Simplified class description mode. Only the class name is displayed.
			
The minimum requiered data:
1. At least one source file.
2. Program language.

Examples:

java -jar ACDG.jar -setppm ALL -exclude Test -setpl Java 
-allfwcfromdir 
"path 2 dir" 
-setprojname ClassDiagramOfItsOwnCode -addclasses ClassA

The files with the diagram and code for plantuml 
should appear in the output directory.

Special commands:
	
	java -jar ACDG.jar -version
Show the program version

	java -jar ACDG.jar -test
Creates a class diagram based on its own code with additional external
class ClassA. 
It is used for simplified testing of the program's functionality.
Equals to following command:
java -jar ACDG.jar -setppm ALL -exclude Test -setpl Java 
-allfwcfromdir path 2 dir -setprojname  ClassDiagramOfItsOwnCode
-addclasses ClassA

	java -jar ACDG.jar -setppm ALL --user-def-pl-strategy Java 
--user-def-pl-ext java --user-def-pl-grammar java.bnf 
--user-def-pl -allfwcfromdir . -setprojname TestUserDefPL 
-addclasses ClassB
An example of using user-defined programming language mode.

	java -jar ACDG.jar -test-simple-class-descr
the same as the -test option, but the simplified class 
description mode is used



