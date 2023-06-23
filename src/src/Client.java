package src;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client extends Thread {

    private boolean running = true;
    Communicator launcherChannelSend;
    private int size;
    private int descSize;
    private byte[] jobDesc;
    private short msg_type;
    private float capacity;
    private String nomeuser;

    int Pontos = 0;
    String launcherChannelDesc;

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1:5000");

    }

    public void exibirMenu() {
        System.out.println("\n");
        System.out.println("+--------------------------------+");
        System.out.println("|              MENU              |");
        System.out.println("+--------------------------------+");
        System.out.println(

                "| 1 - Cabelo                     |\n" +
                        "| 2 - Cor dos olhos              |\n" +
                        "| 3 - Cor de pele                |\n" +
                        "| 4 - Possui oculos              |\n" +
                        "| 5 - Possui chapeu              |\n" +
                        "| 6 - Sexo                       |\n" +
                        "| sair - Sair do jogo            |");
        System.out.println("+--------------------------------+\n\n");
        System.out.print("Digite a mensagem (ou 'sair' para encerrar): ");
    }

    public Client(String launcherChannelDesc) {
        try {
            // Define o endereço do canal do lançador do cliente como "127.0.0.1:5000"
            this.launcherChannelDesc = "127.0.0.1:5000";

            // Aguarda 1 segundo antes de continuar a execução
            Thread.sleep(1000);

            // Inicializa o objeto Communicator para enviar mensagens
            this.launcherChannelSend = new Communicator();
            Scanner scanner = new Scanner(System.in);

            // Solicita ao usuário que digite uma mensagem
            System.out.print("Digite seu nome de usuario: ");
            nomeuser = scanner.nextLine();

            // Conecta-se ao servidor usando o canal do lançador do cliente
            this.launcherChannelSend.connectServer(launcherChannelDesc);

            // Envia uma nova mensagem para o servidor usando o canal do lançador do cliente

            this.launcherChannelSend.sendMsgNewJOB(launcherChannelSend.getSocket(),
                    this.launcherChannelSend.clientLocalChannelDesc(), nomeuser);

            // Exibe informações sobre o cliente
            System.out.println("Cliente Iniciado =>");
            System.out.println(
                    "\t\t Seu channel eh: " + this.launcherChannelSend.clientLocalChannelDesc() + "\n");

            // Inicia a execução da thread do cliente
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            ByteBuffer buf = null;

            /////////////////////////////////////////////////////////////////////////
            System.out.println("Cliente =>");
            System.out.println("\t Recebendo Mensagens ... \n");
            /////////////////////////////////////////////////////////////////////////

            // Loop principal do cliente para receber mensagens
            while (this.running) {
                // Recebe as mensagens do canal do lançador
                buf = this.launcherChannelSend.receiveMessages();

                // Obtém o tipo de mensagem do buffer
                msg_type = buf.getShort();

                // Obtém o tamanho da mensagem do buffer
                size = buf.getInt();

                // Processa o tipo de mensagem recebida
                switch (msg_type) {
                    case Config.NEW_JOB:
                        // Se a mensagem é do tipo "NEW_JOB", extrai o ID e a descrição do trabalho
                        String msg = Communicator.readString(buf).trim();
                        String channelDesc = Communicator.readString(buf).trim();

                        System.out.println("Recebi nova mensagem: " + msg + channelDesc);
                        // worker.newJob(jobId, jobDesc);
                        break;
                    default:
                        // Caso contrário, exibe uma mensagem informando que uma mensagem desconhecida
                        // foi perdida
                        System.out.println("Cliente =>");
                        System.out.println("\t\t LOST MESSAGE! Type: " + msg_type + "\n");
                        break;
                }

                Scanner scanner = new Scanner(System.in);
                String message;

                String[] escolhaCabeloStrings = { "Careca", "Loiro", "Castanho", "Preto", "Branco" };
                String[] escolhaOlhosStrings = { "Azul", "Castanho" };
                String[] escolhaPelesStrings = { "Branco", "Parda", "Preta" };
                String[] escolhaSexosStrings = { "F", "M" };

                String opcao;
                String escolha;
                Integer numswitch;
                numswitch = 0;
                exibirMenu();

                // Scanner leitor = new Scanner(System.in);
                // opcao = scanner.nextLine();
                message = scanner.nextLine();
                numswitch = Integer.parseInt(message);
                opcao = message;
                System.out.println(opcao);
                if (message.equalsIgnoreCase("sair")) {
                    System.out.print("Encerrando");
                    running = false;
                } else {

                    do {
                        System.out.print("\nEscolhas: ");
                        switch (numswitch) {
                            case 1:
                                System.out.print("1 - Careca ; 2 - Loiro ; 3 - Castanho ; 4 - Preto ; 5 - Branco\n");
                                escolha = scanner.next();
                                opcao = "01:" + escolhaCabeloStrings[Integer.parseInt(escolha) - 1];
                                break;
                            case 2:
                                System.out.print("1 - Azul ; 2 - Castanho\n");
                                escolha = scanner.next();
                                opcao = "02:" + escolhaOlhosStrings[Integer.parseInt(escolha) - 1];
                                ;
                                break;
                            case 3:
                                System.out.print("1 - Branco ; 2 - Parda ; 3 - Preta\n");
                                escolha = scanner.next();
                                opcao = "03:" + escolhaPelesStrings[Integer.parseInt(escolha) - 1];
                                ;
                                break;
                            case 4:
                                escolha = scanner.next();
                                opcao = "04:";
                                break;
                            case 5:
                                escolha = scanner.next();
                                opcao = "05:";
                                break;
                            case 6:
                                System.out.print("1 - Feminino ; 2 - Masculino\n");
                                escolha = scanner.next();
                                opcao = "06:" + escolhaSexosStrings[Integer.parseInt(escolha) - 1];
                                break;
                            case 7:
                                System.out.print("Digite o nome do seu palpite\n");
                                escolha = scanner.next();
                                this.launcherChannelSend.sendMsgNewCHUTE(launcherChannelSend.getSocket(),
                                        nomeuser,
                                        escolha);
                                break;
                            default:
                                System.out.println("Escolha inválida!\n");
                                opcao = scanner.nextLine();

                        }
                    } while (numswitch <= 0 && numswitch >= 7);
                    // leitor.close();
                    // Enviar a mensagem para o servidor
                    message = opcao;
                    this.launcherChannelSend.sendMsgNewWorker(launcherChannelSend.getSocket(),
                            nomeuser,
                            message);
                    System.out.println("Mensagem enviada: " + message);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
