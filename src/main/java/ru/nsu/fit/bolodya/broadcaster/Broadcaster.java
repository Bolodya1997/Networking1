package ru.nsu.fit.bolodya.broadcaster;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

class Broadcaster {

    private DatagramSocket socket;
    private DatagramPacket receivePacket = new DatagramPacket(new byte[0], 0, 0);

    private static final int MAX_COUNT = 5;
    private Map<InetAddress, Integer> addressMap = new HashMap<>();

    Broadcaster(int port, View view) throws IOException {
        socket = new DatagramSocket(port);
        DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, 0);
        sendPacket.setAddress(Inet4Address.getByName("255.255.255.255"));
        sendPacket.setPort(port);

        while (true) {
            socket.send(sendPacket);
            receive();
            clean();
            view.update(addressMap.size());
        }
    }

    private void receive() throws SocketException {
        long startTime = currentTimeMillis();
        for (int timeout = 1000; timeout > 0; timeout = (int) (1000 - (currentTimeMillis() - startTime))) {
            socket.setSoTimeout(timeout);
            try {
                socket.receive(receivePacket);
            }
            catch (IOException ignored) {}
            addressMap.put(receivePacket.getAddress(), MAX_COUNT);
        }
    }

    private void clean() {
        Iterator<Map.Entry<InetAddress, Integer>> iterator = addressMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<InetAddress, Integer> entry = iterator.next();

            int count = entry.getValue() - 1;
            if (count <= 0)
                iterator.remove();
            else
                addressMap.put(entry.getKey(), count);
        }
    }
}
