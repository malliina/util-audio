[![Build Status](https://travis-ci.org/malliina/util-audio.svg?branch=master)](https://travis-ci.org/malliina/util-audio)
[![Maven Central](https://img.shields.io/maven-central/v/com.malliina/util-audio_2.11.svg)]()

# util-audio

A library for audio playback on the JVM. Supports MP3s.

## Installation

    "com.malliina" %% "util-audio" % "2.0.0"

## Code

    val file = Paths get "deathmetal.mp3"
    val player = new JavaSoundPlayer(file)
    player.play()

## License

New BSD License.
