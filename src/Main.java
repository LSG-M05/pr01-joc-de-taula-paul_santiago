import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        menu();
    }

    static Scanner input = new Scanner(System.in);

    public static void menu(){

        int opcion = leerInt("🐺 *** Lobo *** 🐺 \n\n1. Nueva partida\n2. Salir\n", 1, 2);

        switch (opcion) {
            case 1:

                System.out.println("Volviendo al menú...");
                menu();
                break;

            case 2:
                System.out.println("Saliendo...");
                break;
        }

    }


    /* [ id, nombre, rol, vida ] */
    private static Object[][] settings() {
        Object[][] players = new Object[8][4];
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[] ordenRoles;

        for (int i = 0; i < players.length; i++) {   /* id */
            players[i][0]=i+1;
        }

        leerNombres(players);  /* nombres */

        asignarRoles(players);  /* roles */

        asignarVidas(players);  /* roles */

        return players;
/*
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

        partida(players);
    }


    private static void partida(Object[][] players){

    }
*/
    }

    private static void leerNombres(Object[][] players) {
        String confirmar = "no";

        for (int i=0; i < players.length; i++ ) {
            do {
                System.out.print("Introduce el nombre del jugador " + (i+1) + ": ");
                players[i][1] = input.nextLine();

                System.out.println("El nombre introducido es: " + players[i][1]);
                System.out.print("Escribe 'si' para confirmar: ");
                confirmar = input.nextLine();
            } while (!confirmar.equalsIgnoreCase("si"));
        }
    }

    private static int[] arrayAleatorio() {

        Random random = new Random();

        int[] array = {9, 9, 9, 9, 9, 9, 9, 9};
        int auxiliar;
        boolean valido;

        for (int i=0; i < array.length; i++) {
            do {
                valido = true;
                auxiliar = randomInt(8);
                for (int j=0; j < array.length; j++) {
                    if (array[j] == auxiliar) {
                        valido = false;
                    }
                }
            }while (!valido);

            array[i] = auxiliar;
        }

        return array;

    }

    private static void asignarRoles(Object[][] players) {
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[] cual = arrayAleatorio();

        for (int i=0; i<players.length; i++) {
            players[i][2] = roles[cual[i]];
        }
    }

    private static void asignarVidas(Object[][] players) {
        for (int i=0; i< players.length; i++) {

            String aux = (String) players[i][2];

            if (aux.equalsIgnoreCase("Anciano")) {
                players[i][3] = 2;
            }
            else {
                players[i][3] = 1;
            }
        }
    }

    private static int randomInt(int max) {

        Random random = new Random();

        int numeroAleatorio = random.nextInt(max);

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