#!/bin/bash

#Script for automatic creation of an archive with the program.
 
#Copyright 2021 Alexander Chernokrylov <CodeDesign2763@gmail.com>
 
#This is a part of ACDG.
#This program is free software: you can redistribute it and/or 
#modify it under the terms of the GNU General Public License as 
#published by the Free Software Foundation, either version 3 of the 
#License, or (at your option) any later version.

#This program is distributed in the hope that it will be useful,
#but WITHOUT ANY WARRANTY; without even the implied warranty of
#MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#GNU General Public License for more details.

#You should have received a copy of the GNU General Public License
#along with this program.  
#If not, see <https://www.gnu.org/licenses/>.



#Automatic creation of an archive with all the files 
#necessary for work based on the repository

#If the first argument is "cp1251" - change the encoding 
#from UTF8 to CP1251

#The archive name must not be the same as the directory name
current_dir_name=${PWD##*/} 
archive_name="$current_dir_name""_exp"

#Jar creation
cd bin
rm ACDG.jar
jar cfe ACDG.jar com.acdg.MainClass com/acdg/*class com/functionsforjunit/*class
cd ..

#Deleting files
rm src/*log
rm bin/*log
rm bin/*class
rm bin/*java

rm -r "../""$archive_name"

#Creating empty directories
mkdir "../""$archive_name"
mkdir "../""$archive_name""/temp"
mkdir "../""$archive_name""/output"

#Change encoding
if [[ $1 = "cp1251" ]]&&[[ "$#" -eq 1 ]] 
then 
	./change_cp.sh cp1251
fi

#Copying files
cp -r lib "../""$archive_name"
cp -r bin "../""$archive_name"
cp -r src "../""$archive_name"
cp -r data "../""$archive_name"
cp COPYING "../""$archive_name"

#Archive creation
7z a "../""$archive_name"".7z" "../""$archive_name"

#Recovery of encoding
if [[ $1 = "cp1251" ]]&&[[ "$#" -eq 1 ]]
then 
	./change_cp.sh utf8
	echo "Encoding changed to CP1251"
fi

echo "ACDG archive folder creation completed"
