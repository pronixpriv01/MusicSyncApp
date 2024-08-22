package com.musicapp.util;

import javax.sound.midi.*;

public class MidiUtils {

    static Synthesizer synthesizer;

    static {
        try {
            synthesizer = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    ;
    int instrumentNr;
    static Instrument[] instruments = synthesizer.getDefaultSoundbank().getInstruments();;
    MidiChannel channel;
    int pitch = 60;
    int velocity = 100;
    Receiver receiver = synthesizer.getReceiver();
    ShortMessage noteOn;
    ShortMessage noteOff;
    int duration = 1000;
    int channelNr = 0;



    public MidiUtils() throws MidiUnavailableException {
    }


    public void playMidi () throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        synthesizer.open();

        // Wähle ein Instrument aus (z.B. das erste Instrument)
        //synthesizer.loadInstrument(instruments[1]);

        // Hole den ersten MIDI-Kanal
        MidiChannel[] channels = synthesizer.getChannels();
        channel = channels[0];


        // Setze das Instrument auf dem Kanal
        channel.programChange(instruments[getInstrumentNumber()].getPatch().getProgram());

        noteOn = createShortmessage(channelNr, pitch, velocity);
        noteOff = createShortmessageOff(channelNr, pitch, velocity);
        receiver.send(noteOn, -1);

        Thread.sleep(duration);

        receiver.send(noteOff, -1);

        synthesizer.close();
    }

    public int getInstrumentNumber() {
        return instrumentNr;
    }

    public ShortMessage createShortmessage(int channelNr, int pitch, int velocity) throws InvalidMidiDataException {
        ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_ON, channelNr, pitch, velocity);
        receiver.send(noteOn, -1);
        return noteOn;
    }

    public ShortMessage createShortmessageOff(int channelNr, int pitch, int velocity) throws InvalidMidiDataException {
        ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_OFF, channelNr, pitch, velocity);
        receiver.send(noteOn, -1);
        return noteOn;
    }

    public void setInstrumentNumber(int instrumentNumber) {

        if (instrumentNumber > 0 && instrumentNumber < 235) {
            this.instrumentNr = instrumentNumber;
        }
    }

    public int selectInstrument (int instrumentNumber) {
        this.instrumentNr = instrumentNumber;

        MidiChannel[] channels = synthesizer.getChannels();
        MidiChannel channel = channels[0];

        // Setze das Instrument auf dem Kanal
        channel.programChange(instruments[200].getPatch().getProgram());
        return instrumentNumber;
    }
}