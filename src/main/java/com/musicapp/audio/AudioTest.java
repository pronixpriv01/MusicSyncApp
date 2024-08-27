package com.musicapp.audio;


import com.musicapp.util.MidiPlay;
import com.musicapp.util.MidiUtils;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class AudioTest {
    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {


        // über die statische Methode playMidi in der Klass MidiPlay lassen sich Klänge testen und verwenden.
        // Als Parameter wird ein Wert von 0 - 234 übergeben - für jeden Wert ein Instrument.
        MidiUtils.playMidi(185);
    }
}
