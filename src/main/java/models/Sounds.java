/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static control.Globales.CAR_CRASH_SOUND;
import static control.Globales.CAR_DRIFT_SOUND;
import static control.Globales.CAR_SOUND;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class Sounds {

    // Removemos os objetos "File". Vamos gerenciar via "URL" de forma dinâmica.
    private Clip car_sound_clip;
    private Clip car_crash_clip;
    private Clip car_drift_clip;

    private boolean isCarSoundActive = false;
    private boolean isCarCrashSoundActive = false;
    private boolean isCarDriftSoundActive = false;
    private double volumeToIncrease = 0.01;
    private double volume = 0.0;

    public Sounds() {

    }

    public double getVolumeToIncrease() {
        return volumeToIncrease;
    }

    public void setVolumeToIncrease(double volumeToIncrease) {
        this.volumeToIncrease = volumeToIncrease;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void playCarSound() {
        try {
            if (!isCarSoundActive) {
                URL url = getClass().getResource(CAR_SOUND);
                if (url != null) {
                    car_sound_clip = AudioSystem.getClip();
                    car_sound_clip.open(AudioSystem.getAudioInputStream(url));
                    FloatControl gainControl = (FloatControl) car_sound_clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(0));
                    car_sound_clip.start();
                    this.isCarSoundActive = true;
                } else {
                    System.err.println("Áudio não encontrado: " + CAR_SOUND);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en audio:\n" + e);
        }
    }

    public void stopCarSound() {
        if (this.isCarSoundActive && car_sound_clip != null) {
            car_sound_clip.stop();
            this.isCarSoundActive = false;
        }
    }

    public void playCarCrashSound() {
        try {
            if (!isCarCrashSoundActive) {
                URL url = getClass().getResource(CAR_CRASH_SOUND);
                if (url != null) {
                    car_crash_clip = AudioSystem.getClip();
                    car_crash_clip.open(AudioSystem.getAudioInputStream(url));
                    FloatControl gainControl = (FloatControl) car_crash_clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(0.3));
                    car_crash_clip.start();
                    this.isCarCrashSoundActive = true;
                } else {
                    System.err.println("Áudio não encontrado: " + CAR_CRASH_SOUND);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en audio:\n" + e);
        }
    }

    public void stopCarCrashSound() {
        if (this.isCarCrashSoundActive && car_crash_clip != null) {
            car_crash_clip.stop();
            this.isCarCrashSoundActive = false;
        }
    }

    public void playCarDriftSound() {
        try {
            if (!isCarDriftSoundActive) {
                URL url = getClass().getResource(CAR_DRIFT_SOUND);
                if (url != null) {
                    car_drift_clip = AudioSystem.getClip();
                    car_drift_clip.open(AudioSystem.getAudioInputStream(url));
                    FloatControl gainControl = (FloatControl) car_drift_clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(0.3));
                    car_drift_clip.start();
                    this.isCarDriftSoundActive = true;
                } else {
                    System.err.println("Áudio não encontrado: " + CAR_DRIFT_SOUND);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en audio:\n" + e);
        }

    }

    public void stopCarDriftSound() {
        if (this.isCarDriftSoundActive && car_drift_clip != null) {
            car_drift_clip.stop();
            this.isCarDriftSoundActive = false;
        }
    }

    public void stopSounds() {
        if (car_sound_clip != null) car_sound_clip.stop();
        if (car_crash_clip != null) car_crash_clip.stop(); // Corrigido (estava drift 2x)
        if (car_drift_clip != null) car_drift_clip.stop();
    }

    public void setGeneralVolume(float volume) {
        if (this.car_sound_clip != null) {
            if (volume > 0f && volume < 1f) {
                this.volume = volume;
                FloatControl gainControl = (FloatControl) this.car_sound_clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(volume));
            }
        }
    }
}