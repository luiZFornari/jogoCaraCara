package src;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

import src.Caras.Objeto;

public class Server extends Thread {

    private Map<String, Communicator> clientCommunicators = new ConcurrentHashMap<>();
    private Communicator comm_server;
    private Communicator comm_client;
    private boolean running = true;
    private short msg_type;
    private int size;
    private int descSize;
    private int quantiaUser = 0;
    private Lock lock;

    private List<Jogador> vetorClientes;

    public static void main(String[] args) {

        Server server = new Server("127.0.0.1", 5000);

    }

    public Server(String defaultIP, int defaultPort) {
        try {
            this.comm_server = new Communicator(defaultIP, defaultPort);

            System.out.println("Servidor iniciado =>");
            System.out.println("\t Seu canal eh: " + this.comm_server.serverChannelDescription() + "\n");

            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            ByteBuffer buf = null;
            System.out.println("\t Recebendo Mensagens ... \n");

            vetorClientes = new ArrayList<>(2);
            Caras Vetor = new Caras();
            Caras.Objeto[] vetorCaras = Vetor.getVetor();
            String resposta = "";

            while (this.running) {
                buf = this.comm_server.receiveMessages();
                msg_type = buf.getShort();
                size = buf.getInt();
                String workerId;
                String channelDesc;

                Scanner scanner;
                Caras.Objeto cara;
                Random random = new Random();

                switch (msg_type) {
                    case Config.NEW_WORKER:
                        workerId = Communicator.readString(buf).trim();
                        for (int i = 0; i < this.vetorClientes.size(); i++) {
                            if (this.vetorClientes.get(i).nome.equals(workerId)) {

                                System.out.println("Recebi nova mensagem: " + this.vetorClientes.get(i).nome);
                                channelDesc = Communicator.readString(buf).trim();
                                System.out.println("Pergunta: " + channelDesc);
                                
                                String vet[] = channelDesc.split(":");

                                switch (vet[0]) {
                                    case "01":
                                        if (vet[1].equals(this.vetorClientes.get(i).getCara().getCorDoCabelo())) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    case "11":
                                        if (this.vetorClientes.get(i).getCara().getCabelo()) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    case "02":
                                        if (vet[1].equals(this.vetorClientes.get(i).getCara().getCorDosOlhos())) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    case "03":
                                        if (vet[1].equals(this.vetorClientes.get(i).getCara().getCorDoCabelo())) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    case "04":
                                        if (this.vetorClientes.get(i).getCara().getOculos()) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    case "05":
                                        if (this.vetorClientes.get(i).getCara().getChapeu()) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    case "06":
                                        if (vet[1].equals(this.vetorClientes.get(i).getCara().getSexo())) {
                                            resposta = "Sim";
                                        } else {
                                            resposta = "Nao";
                                        }
                                        break;
                                    default:
                                        resposta = "Opçao invalida";
                                        break;

                                }
                                System.out.println(this.vetorClientes.get(i).getCara().getNome());

                                comm_server.sendMsgNewJOB(
                                        Communicator.clientSocketList.get(this.vetorClientes.get(i).ip),
                                        "Resposta: ",
                                        resposta);
                            }

                        }

                        break;
                    case Config.NEW_JOB:
                        workerId = Communicator.readString(buf).trim();
                        channelDesc = Communicator.readString(buf).trim();
                        if (this.vetorClientes.size() == 2) {
                            resposta = "Limite de conexoes atingido.";
                            comm_server.sendMsgNewJOB(Communicator.clientSocketList.get(workerId), channelDesc,
                                    resposta);

                                    
                        } else {

                            System.out.println(workerId);
                            System.out.println("Novo Jogador conectou: " + workerId);
                            cara = vetorCaras[random.nextInt(24)];

                            System.out.println("Usuario: " + channelDesc);

                            Jogador cliente = new Jogador(channelDesc, workerId, 0, 0, 0, cara);

                            this.vetorClientes.add(cliente);

                            resposta = "Bem vindo ao jogo! ";

                            comm_server.sendMsgNewJOB(Communicator.clientSocketList.get(workerId), channelDesc,
                                    resposta);
                        }

                        break;
                    case Config.New_Chute:
                        workerId = Communicator.readString(buf).trim();
                        System.out.println("O usuario : " + workerId + " tentou acertar a cara.");
                        String chute = Communicator.readString(buf).trim();

                        for (int i = 0; i < this.vetorClientes.size(); i++) {
                            if (this.vetorClientes.get(i).nome.equals(workerId)) {
                                if (this.vetorClientes.get(i).cara.getNome().equals(chute)) {
                                
                                        this.vetorClientes.get(i).pontos += 1;
                                        this.vetorClientes.get(i).vitorias++;

                                        comm_server.sendMsgNewJOB(
                                                Communicator.clientSocketList.get(this.vetorClientes.get(i).ip),
                                                "Parabens voce acertou!! Ganhou +1 pontos , Voce esta com - "
                                                        + this.vetorClientes.get(i).pontos,
                                                " - Uma nova rodada começou");

                                        if(this.vetorClientes.get(i).pontos == 5)
                                        {
                                            comm_server.sendMsgNewJOB(
                                            Communicator.clientSocketList.get(this.vetorClientes.get(i).ip),
                                            "Parabens voce ganhou!!  - "
                                                    + this.vetorClientes.get(i).pontos,
                                            " - Uma nova rodada começou");

                                        }

                                        this.vetorClientes.get(i).cara = vetorCaras[random.nextInt(24)];

                                        if (i == 0) {
                                            comm_server.sendMsgNewJOB(
                                                    Communicator.clientSocketList.get(this.vetorClientes.get(0).ip),
                                                    "Voce perdeu !! ",
                                                    "Voce esta com " + this.vetorClientes.get(0).pontos);

                                            this.vetorClientes.get(i).derrotas++;
                                            this.vetorClientes.get(0).cara = vetorCaras[random.nextInt(24)];
                                        } else {
                                            comm_server.sendMsgNewJOB(
                                                    Communicator.clientSocketList.get(this.vetorClientes.get(1).ip),
                                                    "Voce perdeu !! Voce esta com " + this.vetorClientes.get(1).pontos,
                                                    "Nova rodada começou");
                                            this.vetorClientes.get(i).derrotas++;
                                            this.vetorClientes.get(1).cara = vetorCaras[random.nextInt(24)];

                                        }

                                    

                                } else {

                                    comm_server.sendMsgNewJOB(
                                            Communicator.clientSocketList.get(this.vetorClientes.get(i).ip),
                                            "Voce perdeu !! Voce esta com "
                                                    + this.vetorClientes.get(i).pontos + " pontos",
                                            "Uma nova rodada começou");

                                    this.vetorClientes.get(i).cara = vetorCaras[random.nextInt(24)];

                                    if (i == 0) {
                                        this.vetorClientes.get(0).pontos += 1;
                                        this.vetorClientes.get(0).vitorias++;
                                        comm_server.sendMsgNewJOB(
                                                Communicator.clientSocketList.get(this.vetorClientes.get(0).ip),
                                                "\"Parabens voce acertou!! ",
                                                "Voce esta com " + this.vetorClientes.get(0).pontos);

                                        this.vetorClientes.get(i).derrotas++;
                                        this.vetorClientes.get(0).cara = vetorCaras[random.nextInt(24)];
                                    } else {
                                        this.vetorClientes.get(1).pontos += 1;
                                        this.vetorClientes.get(1).vitorias++;
                                        comm_server.sendMsgNewJOB(
                                                Communicator.clientSocketList.get(this.vetorClientes.get(1).ip),
                                                "Parabens voce acertou!! Voce esta com "
                                                        + this.vetorClientes.get(1).pontos,
                                                "Nova rodada começou");
                                        this.vetorClientes.get(i).derrotas++;
                                        this.vetorClientes.get(1).cara = vetorCaras[random.nextInt(24)];

                                    }

                                }

                            }

                        }

                        break;
                    default:
                        System.out.println("InterfaceProxy =>");
                        System.out.println("\t\t LOST MESSAGE! Type: " + msg_type + "\n");
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class Jogador {
        private String nome;
        private String ip;
        private int vitorias;
        private int derrotas;
        private int pontos;
        private Caras.Objeto cara;

        public Jogador(String nome, String ip, int vitorias, int derrotas, int pontos, Caras.Objeto cara) {
            this.nome = nome;
            this.ip = ip;
            this.vitorias = vitorias;
            this.derrotas = derrotas;
            this.pontos = pontos;
            this.cara = cara;
        }

        public String getNome() {
            return nome;
        }

        public String getIP() {
            return ip;
        }

        public int getVitorias() {
            return vitorias;
        }

        public int getDerrotas() {
            return derrotas;
        }

        public int getPontos() {
            return pontos;
        }

        public Caras.Objeto getCara() {
            return cara;
        };

        public void setCara(Caras.Objeto cara) {
            this.cara = cara;
        };
    }

}
