import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        menu();
    }

    Scanner input = new Scanner(System.in);

    public static void menu(){

        int opcion = leerInt("🐺 *** Lobo *** 🐺 \n\n1. Nueva partida\n2. Salir\n", 1, 2);

        switch (opcion) {
            case 1:
                Object[][] players = settings();
                noche(players);
                System.out.println("Volviendo al menú...");
                menu();
                break;

            case 2:
                System.out.println("Saliendo...");
                break;
        }

    }

    /*[id, Nombre, Rol, Vida]*/
    private static Object[][] settings(){
        Object[][] players = new Object[8][4];
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[]ordenRoles;

        for (int i = 0; i < players.length; i++) {
            players[i][0]=i+1;
        }

        players[0][1]="Valentina";
        players[1][1]="Marcos";
        players[2][1]="Gabriela";
        players[3][1]="Andrés";
        players[4][1]="Natalia";
        players[5][1]="Juan";
        players[6][1]="Ana";
        players[7][1]="Pablo";

        players[0][2]="Lobo";
        players[1][2]="Vidente";
        players[2][2]="Aldeano";
        players[3][2]="Cazador";
        players[4][2]="Lobo";
        players[5][2]="Anciano";
        players[6][2]="Ángel";
        players[7][2]="Protector";

        players[0][3]=1;
        players[1][3]=1;
        players[2][3]=1;
        players[3][3]=1;
        players[4][3]=1;
        players[5][3]=2;
        players[6][3]=1;
        players[7][3]=1;

        return players;
    }


    private static boolean noche(Object[][] players){
        boolean fin = false;
        int victim = 0;
        String nombre = "";

        for (int i=0; i<8; i++){
            nombre = (String) players[i][1];
            System.out.println(i+1 + ": " + nombre);
        }

        System.out.println("Pueblo duerme");

        victim=lobo(players);
        players[victim][3] = 0;

        vidente(players);

        return fin;
    }

    private static int lobo(Object[][] players){
        String nombre = "";
        String rol = "";
        int vida = 0;
        int victim = 0;

        do{
            victim = leerInt("Se despierta el lobo y mata a alguien: ",1,8);
            nombre = (String) players[victim-1][1];
            rol = (String) players[victim-1][2];
            vida = (int) players[victim-1][3];
        }while (vida == 0 || rol.equals("Lobo"));
        System.out.println("Murió " + nombre);

        return victim-1;
    }

    private static void vidente(Object[][] players){
        String nombre = "";
        String rol = "";
        int vida = 0;
        int victim = 0;

        do{
            victim = leerInt("Se despierta la vidente y mira una carta: ",1,8);
            nombre = (String) players[victim-1][1];
            rol = (String) players[victim-1][2];
            vida = (int) players[victim-1][3];
        }while (vida == 0 || rol.equals("Vidente"));

        System.out.println("El personaje de " + nombre + " es: " + rol);
    }

    private static int[] arrayAleatorio() {

        Random random = new Random();

        int[] array = new int[8];
        int auxiliar;

        for (int i=0; i < 8; i++) {
            boolean valido = true;
            do {
                auxiliar = randomInt(1, 8);
                for (int j=0; j < 8; j++) {
                    if (array[j] == auxiliar) {
                        valido = false;
                    }
                }
            }while (!valido);

            array[i] = auxiliar;
        }

        return array;

    }

    private static int randomInt(int min, int max) {

        Random random = new Random();

        int numeroAleatorio = random.nextInt(8) + 1;

        return numeroAleatorio;
    }

    private static int leerInt(String missatge, int min, int max) {
        Scanner llegir = new Scanner(System.in);
        int x = 0;
        boolean valorCorrecte = false;
        do{
            System.out.print(missatge);
            valorCorrecte = llegir.hasNextInt();
            if (!valorCorrecte){
                System.out.println("ERROR: Valor no entero.");
                llegir.nextLine();
            }else{ // Tengo un entero
                x = llegir.nextInt();
                llegir.nextLine();
                if (x < min || x > max){
                    System.out.println("Opción no valida");
                    valorCorrecte = false;
                }
            }
        }while(!valorCorrecte);

        return x;
    }

}