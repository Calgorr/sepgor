package pkg;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class OutputThread implements Runnable {
    Integer chatID=Main.chatId;
    Socket client;
    TargetDataLine line;
    DatagramPacket dgp;
    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    float rate;
    int channels;
    boolean bigEndian;
    int sampleSize;
    int port;
    InetAddress address;
    AudioFormat format;
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    byte[] data = new byte[4096];

    public OutputThread(Socket client) throws LineUnavailableException {
        this.client = client;
        System.setProperty("java.net.preferIPv4Stack", "true");
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        rate = 44100.0f;
        channels = 2;
        bigEndian = false;
        sampleSize = 16;
        port = 6969;
        format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    @Override
    public void run() {
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        line.start();
        try {
            new ObjectOutputStream(client.getOutputStream()).writeObject(chatID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            line.read(data, 0, data.length);
            try {
                client.getOutputStream().write(data);
            } catch (IOException e) {
                break;
            }
        }

    }

}
