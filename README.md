# BakalariGui
BakalariGui is graphical version of https://github.com/Botaniculus/bakalari-client. It's made with Java Swing. 

# addressAndUsername.txt
BakalariGui reads contents of the file (1st line - url address, 2nd line - username) and prints it to the login window on startup so user doesn't have to write it on every startup. I'm planning to automate the writing-to-the-file process with checkbox "Save".

# For Linux users:
To make it look native (GTK) you must add this to your ~/.profile file:

export _JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -Dswing.crossplatformlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel'

You can also download .run file, which is bash script with binary java payload. It has export _JAVA_OPTIONS=... in it.
