import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        menu();
    }

    static Scanner input = new Scanner(System.in);
    static Object[][] players = new Object[8][4];

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





    private static boolean dia(Object[][] players) {
        int victim = -1;
        String aldea_vic = "";
        String rol = "";

        System.out.println("Se despierta la aldea");
        victim = signal(players, "El pueblo dedide a quién matar: ", "");
        players[victim][3] = 0;
        aldea_vic = (String) players[victim][1];
        rol = (String) players[victim][2];

        System.out.println("La aldea ha matado a " + aldea_vic + ", y su rol era " + rol);

        muereAnciano(players, rol);

        if (gananLobos(players)) {
            return false;
        }
        else {
            return noche(players);
        }
    }

    private static Object[][] muereAnciano(Object[][] players, String rol) {
        if (rol.equals("Anciano")) {          /* quitar poderes tras l muerte del anciano */
            for (int i = 0; i < 8; i++) {
                if (!players[i][2].equals("Lobo")) {
                    players[i][2] = "Aldeano";
                }
            }
        }

        return players;
    }

    private static boolean gananAldeanos(Object[][] players) {
        int countLobos = 0;

        for (int i=0; i<players.length; i++) {
            if ( ((String) players[i][3]).equalsIgnoreCase("Lobo")  ) {
                countLobos += 1;
            }
        }
        if (countLobos == 0) {
            System.out.println("Ganan los aldeanos");
            return true;
        }
        else {
            return false;
        }
    }

    private static boolean gananLobos(Object[][] players) {
        int countVivos = 0;
        int countLobos = 0;

        for (int i=0; i<players.length; i++) {
            if ( (int) players[i][3] >= 1 ) {
                countVivos += 1;
            }

            if ( (String) players[i][3] == "Lobo"  ) {
                countLobos += 1;
            }
        }

        if ( countVivos <= countLobos ) {
            System.out.println("Ganan los lobos.");
            return true;
        }
        else {
            return false;
        }
    }

    private static boolean noche(Object[][] players) {
        boolean fin = false;
        int victim = -1;
        int protegido = -1;
        int visto = -1;
        String lobo_vic = "";
        String cazador_vic = "";
        String aldea_vic = "";
        String nombre = "";
        String rol = "";
        int vida = -1;

        for (int i = 0; i < 8; i++) {
            nombre = (String) players[i][1];
            System.out.println(i + 1 + ": " + nombre);
        }

        System.out.println("Pueblo duerme");
        /* Vidente */
        visto = signal(players, "Se despierta la vidente y mira una carta: ", "Vidente");
        nombre = (String) players[visto][1];
        rol = (String) players[visto][2];
        System.out.println("El personaje de " + nombre + " es: " + rol);
        /* Protector */
        protegido = signal(players, "Se despierta el protector y protege a alguien: ", "");
        /* Lobo */
        victim = signal(players, "Se despierta el lobo y mata a alguien: ", "Lobo");

        if (victim != protegido) {
            vida = (int) players[victim][3] - 1;
            players[victim][3] = vida;
            if (vida == 0) {
                lobo_vic = (String) players[victim][1];
                rol = (String) players[victim][2];
            }
        }

        System.out.println("Ha muerto: " + lobo_vic);

        victim = cazador(players, victim, rol);
        players[victim][3] = 0;
        rol = (String) players[victim][2];

        muereAnciano(players, rol);

/*
        victim = signal(players, "El pueblo dedide a quién matar: ", "");
        players[victim][3] = 0;
        aldea_vic = (String) players[victim][1];
        rol = (String) players[victim][2];

        System.out.println("La aldea ha matado a " + aldea_vic + ", y su rol era " + rol);

        if (rol.equals("Anciano")) {
            for (int i = 0; i < 8; i++) {
                if (!players[i][2].equals("Lobo")) {
                    players[i][2] = "Aldeano";
                }
            }
        }

 */

        victim = cazador(players, victim, rol);
        players[victim][3] = 0;
        rol = (String) players[victim][2];

        if (rol.equals("Anciano")) {
            for (int i = 0; i < 8; i++) {
                if (!players[i][2].equals("Lobo")) {
                    players[i][2] = "Aldeano";
                }
            }
        }

        if (gananLobos(players)) { return gananLobos(players); }
        if (gananAldeanos(players)) { return gananAldeanos(players); }
        else { return dia(players); }
    }






    /* [ id, nombre, rol, vida ] */
    private static Object[][] settings() {
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[] ordenRoles;

        for (int i = 0; i < players.length; i++) {   /* id */
            players[i][0]=i+1;
        }

        players = leerNombres(players);  /* nombres */

        players = asignarRoles(players);  /* roles */

        players = asignarVidas(players);  /* roles */

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

    private static Object[][] leerNombres(Object[][] players) {
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
        return players;
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

    private static Object[][] asignarRoles(Object[][] players) {
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[] cual = arrayAleatorio();

        for (int i=0; i<players.length; i++) {
            players[i][2] = roles[cual[i]];
        }

        return players;
    }

    private static Object[][] asignarVidas(Object[][] players) {
        for (int i=0; i< players.length; i++) {

            String aux = (String) players[i][2];

            if (aux.equalsIgnoreCase("Anciano")) {
                players[i][3] = 2;
            }
            else {
                players[i][3] = 1;
            }
        }

        return players;
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