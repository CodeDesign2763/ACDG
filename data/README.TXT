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
the "allfwcfromdir" option. 
(last parameter specified)
			
	-setppm : 
Select a parameter processing mode
ONLY_DATATYPE, ONLY_ID, ALL
(last parameter specified)
			
The minimum requiered data:
1. At least one source file.
2. Program language.

Examples:
java -jar ACDG.jar -setppm ALL -exclude Test -setpl Java 
-allfwcfromdir 
"path 2 dir" 
-setprojname ClassDiagramOfItsOwnCode -addclasses ClassA
