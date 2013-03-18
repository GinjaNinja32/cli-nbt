cli-nbt
=======

Command-line NBT editor

Author: GinjaNinja32 (@GNinja32)

Type 'help' at the '>' prompt to access this list in-app.

Command              Result
 add <type>        	 Add a new tag
 rem <name|index>  	 Remove a tag
 ls                	 View the contents of the current tag
 cd                	 Change to another tag. Path is '/' or '\\'-separated, '..' is parent.
 clr               	 Clear (empty) the current tag
 exit              	 Exits the program without saving
 save <filename>   	 Save to the specified file.
 save -g <filename>	 Save to the specified file with GZIP compression.
 load <filename>   	 Load from the specified file.
 load -g <filename>	 Load from the specified file with GZIP compression.
 help              	 Show this help text
