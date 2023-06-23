package src;

public class Caras {

  private Objeto[] vetor;

  public Caras() {
    vetor = new Objeto[24]; // Tamanho do vetor de objetos

    // Inicializando os objetos do vetor com valores predefinidos
    vetor[0] = new Objeto("Jorge", true, "M", "Branco", "Castanho", "Branco", false, true);
    vetor[1] = new Objeto("Beto", true, "M", "Branco", "Castanho", "Castanho", false, true);
    vetor[2] = new Objeto("Ana", true, "F", "Preta", "Castanho", "Preto", false, false);
    vetor[3] = new Objeto("Pedro", true, "M", "Branco", "Azul", "Branco", false, false);
    vetor[4] = new Objeto("Daniel", true, "M", "Branco", "Castanho", "Ruivo", false, false);
    vetor[5] = new Objeto("Clara", true, "F", "Parda", "Castanho", "Preto", true, true);
    vetor[6] = new Objeto("Helio", false, "M", "Parda", "Castanho", "Nao", false, false);
    vetor[7] = new Objeto("Ricardo", false, "M", "Branco", "Castanho", "Nao", false, false);
    vetor[8] = new Objeto("Lucia", true, "F", "Branco", "Azul", "Loira", false, false);
    vetor[9] = new Objeto("Maria", true, "F", "Branco", "Castanho", "Castanho", false, true);
    vetor[10] = new Objeto("Roberto", true, "M", "Branco", "Azul", "Castanho", false, false);
    vetor[11] = new Objeto("Zeca", true, "M", "Branco", "Castanho", "Loiro", true, false);
    vetor[12] = new Objeto("Luiz", true, "M", "Preto", "Castanho", "Preto", false, false);
    vetor[13] = new Objeto("Henrique", true, "M", "Parda", "Castanho", "Preto", false, false);
    vetor[14] = new Objeto("Joao", false, "M", "Branco", "Castanho", "Nao", true, false);
    vetor[15] = new Objeto("Marcos", true, "M", "Branco", "Castanho", "Castanho", false, false);
    vetor[16] = new Objeto("Paulo", true, "M", "Branco", "Castanho", "Branco", true, false);
    vetor[17] = new Objeto("Sonia", true, "F", "Branco", "Castanho", "Branco", false, false);
    vetor[18] = new Objeto("Carlos", true, "M", "Branco", "Castanho", "Loiro", false, true);
    vetor[19] = new Objeto("Tony", false, "M", "Branco", "Azul", "Nao", true, false);
    vetor[20] = new Objeto("Fernando", true, "M", "Preta", "Castanho", "Preto", false, false);
    vetor[21] = new Objeto("Alfredo", true, "M", "Parda", "Azul", "Preto", false, false);
    vetor[22] = new Objeto("Edu", false, "M", "Branco", "Castanho", "Nao", false, true);
    vetor[23] = new Objeto("Chico", true, "M", "Parda", "Castanho", "Preto", false, false);

  }

  public Objeto[] getVetor() {
    return vetor;
  }

  public static void main(String[] args) {
    Caras cara = new Caras();
  }

  public class Objeto {
    private String nome;
    private Boolean cabelo;
    private String sexo;
    private String cordepele;
    private String cordosolhos;
    private String cordocabelo;
    private Boolean oculos;
    private Boolean chapeu;

    public Objeto(String nome, Boolean cabelo, String sexo, String cordepele, String cordosolhos, String cordocabelo,
        Boolean oculos, Boolean chapeu) {
      this.nome = nome;
      this.cabelo = cabelo;
      this.sexo = sexo;
      this.cordepele = cordepele;
      this.cordosolhos = cordosolhos;
      this.cordocabelo = cordocabelo;
      this.oculos = oculos;
      this.chapeu = chapeu;
    }

    public String getNome() {
      return nome;
    }

    public Boolean getCabelo() {
      return cabelo;
    }

    public String getSexo() {
      return sexo;
    }

    public String getCorDePele() {
      return cordepele;
    }

    public String getCorDosOlhos() {
      return cordosolhos;
    }

    public String getCorDoCabelo() {
      return cordocabelo;
    }

    public Boolean getOculos() {
      return oculos;
    }

    public Boolean getChapeu() {
      return chapeu;
    }

  }
}
