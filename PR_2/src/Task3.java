import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Task3 {

    public static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             FileChannel fileChannel = fileInputStream.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(2);
            int bytesRead;
            int checksum = 0;

            while ((bytesRead = fileChannel.read(buffer)) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checksum += buffer.get() & 0xFF;
                }
                buffer.clear();
            }

            checksum = (checksum >> 16) + (checksum & 0xFFFF);
            checksum = (checksum >> 16) + (checksum & 0xFFFF);
            return (short) (~checksum & 0xFFFF);
        }
    }

    public static void main(String[] args) {
        String filePath = "Task1.txt";
        try {
            short checksum = calculateChecksum(filePath);
            System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath, checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}