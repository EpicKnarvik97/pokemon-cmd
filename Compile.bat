javac.exe -cp java java/Game.java
jar cvfm Game.jar manifest.txt -C java config/Pokemon.txt java/*.class