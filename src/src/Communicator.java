package src;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Communicator extends Thread {

    String defaultIP;
    int defaultPort;

    SocketChannel clientChannel = null;
    ServerSocketChannel serverChannel = null;
    InetSocketAddress channelAddress = null;
    BlockingQueue<ByteBuffer> incoming = new LinkedBlockingQueue<ByteBuffer>();
    public static Map<String, SocketChannel> clientSocketList;
    private boolean active;

    // Construtor da classe Communicator que aceita um endereço IP padrão e uma
    // porta padrão
    public Communicator(String defaultIP, int defaultPort) {
        this.defaultPort = defaultPort;
        this.defaultIP = defaultIP;
        boolean created = false;
        clientSocketList = new LinkedHashMap<String, SocketChannel>();

        // Loop para criar o canal do servidor até que seja criado com sucesso
        while (!created) {
            try {
                serverChannel = ServerSocketChannel.open();
                channelAddress = new InetSocketAddress(this.defaultIP, this.defaultPort);
                serverChannel.socket().bind(channelAddress);
                created = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.active = true;
        this.start();
    }

    public Communicator() {
        // Construtor vazio
    }

    // Método para conectar-se a um servidor usando o canal de comunicação
    public void connectServer(String hostDescription) {
        try {
            String vet[] = hostDescription.split(":");
            if (vet.length != 2) {
                System.out.println("Formato inválido. Use o formato hostname:port");
                return;
            }
            String hostname = vet[0];
            int port = Integer.parseInt(vet[1].trim());
            clientChannel = SocketChannel.open(new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        rodaListener();
    }

    public void run() {
        try {
            while (this.active) {
                try {
                    clientChannel = serverChannel.accept();
                    clientSocketList.put(clientRemoteChannelDesc(), clientChannel);
                    rodaListener();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rodaListener() {
        try {
            Listener l = new Listener(this.clientChannel, this.incoming);
            l.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obtém o SocketChannel usado para comunicação
    public SocketChannel getSocket() {
        try {
            return clientChannel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retorna a descrição do canal do servidor no formato "IP:porta"
    public String serverChannelDescription() {
        try {
            String hostAddress = channelAddress.getAddress().getHostAddress();
            String portAddress = Integer.toString(channelAddress.getPort());
            return hostAddress + ":" + portAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retorna a descrição do canal local do cliente no formato "IP:porta"
    public String clientLocalChannelDesc() {
        try {
            String hostAddress = clientChannel.socket().getLocalAddress().getHostAddress();
            String portAddress = Integer.toString(clientChannel.socket().getLocalPort());
            return hostAddress + ":" + portAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retorna a descrição remota do canal do cliente no formato "IP:porta"
    public String clientRemoteChannelDesc() {
        try {
            String hostAddress = clientChannel.socket().getInetAddress().getHostAddress();
            String portAddress = Integer.toString(clientChannel.socket().getPort());
            return hostAddress + ":" + portAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Recebe mensagens do canal de comunicação
    public ByteBuffer receiveMessages() {
        try {
            return incoming.take();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    // Envia uma nova mensagem de trabalho para o servidor usando o canal de
    // comunicação
    public void sendMsgNewWorker(SocketChannel channel, String nodeId, String nodeChannelDescription) {
        try {
            byte[] nodeId_b = null;
            byte[] nodeChannelDescription_b = null;
            try {
                nodeId_b = nodeId.getBytes("ISO-8859-1");
                nodeChannelDescription_b = nodeChannelDescription.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.exit(1);
            }
            int size = 2 + 4 + nodeId.length() + 1 + nodeChannelDescription.length() + 1;
            ByteBuffer writeBuffer = ByteBuffer.allocateDirect(size);
            writeBuffer.putShort(Config.NEW_WORKER);
            writeBuffer.putInt(size);
            writeBuffer.put(nodeId_b);
            writeBuffer.put((byte) 0);
            writeBuffer.put(nodeChannelDescription_b);
            writeBuffer.put((byte) 0);
            writeBuffer.rewind();
            channelWrite(channel, writeBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsgNewJOB(SocketChannel channel, String nodeId, String nodeChannelDescription) {
        try {
            byte[] nodeId_b = null;
            byte[] nodeChannelDescription_b = null;
            try {
                nodeId_b = nodeId.getBytes("ISO-8859-1");
                nodeChannelDescription_b = nodeChannelDescription.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.exit(1);
            }
            int size = 2 + 4 + nodeId.length() + 1 + nodeChannelDescription.length() + 1;
            ByteBuffer writeBuffer = ByteBuffer.allocateDirect(size);
            writeBuffer.putShort(Config.NEW_JOB);
            writeBuffer.putInt(size);
            writeBuffer.put(nodeId_b);
            writeBuffer.put((byte) 0);
            writeBuffer.put(nodeChannelDescription_b);
            writeBuffer.put((byte) 0);
            writeBuffer.rewind();
            channelWrite(channel, writeBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsgNewCHUTE(SocketChannel channel, String nodeId, String nodeChannelDescription) {
        try {
            byte[] nodeId_b = null;
            byte[] nodeChannelDescription_b = null;
            try {
                nodeId_b = nodeId.getBytes("ISO-8859-1");
                nodeChannelDescription_b = nodeChannelDescription.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.exit(1);
            }
            int size = 2 + 4 + nodeId.length() + 1 + nodeChannelDescription.length() + 1;
            ByteBuffer writeBuffer = ByteBuffer.allocateDirect(size);
            writeBuffer.putShort(Config.New_Chute);
            writeBuffer.putInt(size);
            writeBuffer.put(nodeId_b);
            writeBuffer.put((byte) 0);
            writeBuffer.put(nodeChannelDescription_b);
            writeBuffer.put((byte) 0);
            writeBuffer.rewind();
            channelWrite(channel, writeBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ByteBuffer createResponseJOBBuffer(String mensagem) {
        try {
            byte[] respostaBytes = mensagem.getBytes("ISO-8859-1");
            int size = 2 + 4 + respostaBytes.length + 1;
            ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            buffer.putShort(Config.NEW_JOB);
            buffer.putInt(size);
            buffer.put(respostaBytes);
            buffer.put((byte) 0);
            buffer.rewind();
            return buffer;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    // Escreve dados em um SocketChannel
    public void channelWrite(SocketChannel channel, ByteBuffer writeBuffer) {
        try {
            long nbytes = 0;
            long toWrite = writeBuffer.remaining();
            try {
                while (nbytes != toWrite) {
                    nbytes += channel.write(writeBuffer);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (ClosedChannelException cce) {
                cce.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            writeBuffer.rewind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lê uma string do ByteBuffer
    public static String readString(ByteBuffer in) {
        try {
            Charset charset = Charset.forName("ISO-8859-1");
            CharsetDecoder decoder = charset.newDecoder();
            ByteBuffer buf = ByteBuffer.allocate(in.capacity());
            byte b;
            b = in.get();
            while (b != 0) {
                buf.put(b);
                b = in.get();
            }
            buf.rewind();
            String s = decoder.decode(buf).toString();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
