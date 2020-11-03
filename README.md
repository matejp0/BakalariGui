# BakalariGui
BakalariGui is graphical version of https://github.com/Botaniculus/bakalari-client. It's made with Java Swing. 

# For Linux users:
To make it look native (GTK) you must add this to your ~/.profile file:

export _JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -Dswing.crossplatformlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel'
