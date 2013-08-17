cli-nbt
=======

Command-line NBT editor, with support for reading and writing a custom NBT-like text language.

Author: GinjaNinja32 (@GNinja32)

Binaries:<br/>
All binaries require Java 7<br/>
v1.0 http://bit.ly/YMhSNP - No text file support, just load/save of NBT files<br/>
v1.1 http://bit.ly/12GNXKz - Supports read/write of text format<br/>
v1.1 Update 1 http://bit.ly/1aef5VJ - GZip saving fixed, added -g argument to read a compressed file

List of in-app commands:
Type 'help' at the '>' prompt to access this list in-app.

<table><tr><td><strong>Command</strong></td><td><strong>Result</strong></td></tr>
<tr><td>add &lt;type&gt;</td><td>Add a new tag</td></tr>
<tr><td>rem &lt;name|index&gt;</td><td>Remove a tag</td></tr>
<tr><td>ls</td><td>View the contents of the current tag</td></tr>
<tr><td>cd</td><td>Change to another tag. Path is '/' or '\'-separated, '..' is parent.</td></tr>
<tr><td>clr</td><td>Clear (empty) the current tag</td></tr>
<tr><td>exit</td><td>Exits the program without saving</td></tr>
<tr><td>save &lt;filename&gt;</td><td>Save to the specified file.</td></tr>
<tr><td>save -g &lt;filename&gt;</td><td>Save to the specified file with GZIP compression.</td></tr>
<tr><td>load &lt;filename&gt;</td><td>Load from the specified file.</td></tr>
<tr><td>load -g &lt;filename&gt;</td><td>Load from the specified file with GZIP compression.</td></tr>
<tr><td>help</td><td>Show this help text</td></tr></table>

Command-line:
<table><tr><td><strong>Argument</strong></td><td><strong>Result</strong></td></tr>
<tr><td>-d <i>file1</i> <i>file2</i></td><td>Decode <i>file1</i> (in true NBT format), and write the results (in text format) to <i>file2</i></td></tr>
<tr><td>-c <i>file1</i> <i>file2</i></td><td>Encode <i>file1</i> (in text format), and write the results (in true NBT format) to <i>file2</i></td></tr>
<tr><td>-m <i>file</i></td><td>Load <i>file</i> in text format</td></tr>
<tr><td>-g <i>file</i></td><td>Load <i>file</i> with gzip decompression</td></tr></table>
<tr><td><i>file</i></td><td>Load <i>file</i> with no compression</td></tr></table>

Text language:
&lt;tag-type&gt;:"&lt;name&gt;"=&lt;value&gt;;<br/>
All tag types are supported:<br>
<ul>
<li>byte</li>
<li>short</li>
<li>int</li>
<li>long</li>
<li>float</li>
<li>double</li>
<li>bytearr</li>
<li>string</li>
<li>list</li>
<li>compound</li>
<li>intarr</li>
</ul>
There is an example file in the repository, example.nbtm, with 'comments' in string tags to explain the format more thoroughly.
There is also a Notepad++ 'user-defined language' file for the text language in the repository - nbtm.xml