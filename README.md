# BakalariGui
BakalariGui is graphical version of https://github.com/Botaniculus/bakalari-client. It's made with Java Swing. 

# addressAndUsername.txt
I added check box to Login window. If the checkbox is selected, it will save the URL and username values to csv txt file. On next startup, it will automatically load these values to Text Fields
Format of addressAndUsername.txt:
URL;username

# For Linux users:
To make it look native (GTK) you must add this to your ~/.profile file:

export _JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -Dswing.crossplatformlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel'

You can also download .run file, which is bash script with binary java payload. It has export _JAVA_OPTIONS=... in it.
