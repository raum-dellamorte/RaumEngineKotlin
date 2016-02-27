# RaumEngineKotlin
A Kotlin Machine for OpenGL Pigs

I'm following the tutorials from ThinMatrix on youtube.com to learn OpenGL using LWJGL, but whereas he is using Java, 
I am transcoding to Kotlin.
I was doing the same thing in Mirah, a JVM language with Ruby like syntax that compiles to JVM bytecode with no runtime library,
but it still needs more work. Kotlin has a runtime library, but it doesn't seem to be too much of a hinderance.
Kotlin has commercial support which speeds up development and has hit it's 1.0 target, so it seems like a good language to learn.
It's definitely easier than Java, with the exception of a few bugs I ran into while transcoding.
I had a few variables that where supposed to be Ints that where coming back as Doubles, but once I found the problem, 
the hard part, it was a quick fix to assure they were being interpreted correctly.

I hope this code may be of use to someone trying to learn Kotlin and/or OpenGL.  One thing I did differently from
ThinMatrix's tutorials is the way I handled the Shader classes.  I have a hashmap in the main shader class to map the
String names of glsl variables to their Ints to simplify glsl code changes.  I use a vararg method "newLoc()" to create
links to uniform variables in glsl code so that I don't have to create a new Int variable every time I add a uniform variable,
I just add it to the list of Strings inside the newLoc() method.  I have "getLoc(<String>)" to retrieve the appropriate Ints.
I like to simplify the code wherever possible.

Thanks for tuning it, and hopefully I wind up with a game engine out of all this.
