# util-audio #

A library for audio playback on the JVM. Supports MP3s.

## Installation ##

"com.github.malliina" %% "util-audio" % "1.2.1"

## Code ##

```
val file = Paths get "deathmetal.mp3"
val player = new JavaSoundPlayer(file)
player.play()
```

## License ##

New BSD License.