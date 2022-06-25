![clear_8](assets/logo.png)

An emulator that interpretes and runs [CHIP-8](https://en.wikipedia.org/wiki/CHIP-8) games, with a simple useful interface.
<br>It has a **clea**-n and **r**-eadable object-oriented architecture, written in Java and Swing.

![clear_8](assets/demo.gif)

Table of Contents
=================
* [About](#about)
* [Goals](#goals)
* [Usage](#usage)
* [Command Line Options](#command-line-options)
* [Controls](#controls)
* [Public Domain Games](#public-domain-games)

About
-----
**This is still a bug-ridden hobby project, updated at a leisurely pace, 
<br>that is going to end up really nice some day :-)**

I abandoned it three years ago, and now revived it as a hobby project.

It's main purposes are having fun and polishing my skills in programming 
<br>and software-engineering.

Peek into its codebase, if you like: [src/main/java/de/patrickmetz/clear_8](src/main/java/de/patrickmetz/clear_8)

Goals
-----
- Usability
  - Satisfy users with a simple and useful GUI
- Compatibility
  - Implement widely used CPUs: Cosmac VIP and Super Chip
- Extendability and Maintainability
  - Easily extendable and maintainable
- Portability
  - No need for modification for different computer systems

Usage
-----
1. > java -jar ./build/clear_8.jar
2. Click "Load ROM" to select and load a game

Command line options
--------------------

> java -jar ./build/clear_8.jar <br><br>
> -g, --game < path >   Path to a game file. (C:\my_folder\my_game) <br>
-h, --help         Prints help for the command line options. <br>
-i, --ips < number >    CPU instructions per second. Controls emulation speed. (100, 200, ...) <br>
-v, --vip < boolean >    Use Cosmac VIP CPU if true; or Super Chip CPU otherwise. (true / false)

Controls
--------

The original keys

* 1 | 2 | 3 | C
* 4 | 5 | 6 | D
* 7 | 8 | 9 | E
* A | 0 | B | F

are mapped to

* 1 | 2 | 3 | 4
* Q | W | E | R
* A | S | D | F
* Y | X | C | V

for german keyboards :P

Public domain games
-------------------

https://www.zophar.net/pdroms/chip8.html

https://johnearnest.github.io/chip8Archive
