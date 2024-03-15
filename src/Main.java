import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        menu();
    }

    static Scanner input = new Scanner(System.in);

    public static void menu() {

        int opcion = leerInt("🐺 *** Lobo *** 🐺 \n\n1. Nueva partida\n2. Salir\n", 1, 2);

        switch (opcion) {
            case 1:
                Object[][] players = new Object[8][4];
                players = settings(players);
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
    private static Object[][] settings(Object[][] players) {
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[] ordenRoles;

        for (int i = 0; i < players.length; i++) {   /* id */
            players[i][0] = i + 1;
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
         */

    }


    private static Object[][] dia(Object[][] players) {
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
            return players;
        }
        else {
            return noche(players);
        }
    }

    /**
     * Quita los poderes en caso de que muera el Anciano
     * @param players es un Object[][] con los datos de los jugadores
     * @param rol String con el rol del aldeano muerto/linchado
     * @return Object[][] con los datos de los jugadores actualizados
     */
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

    /**
     * Comprueba si ya han ganado los aldeanos
     * @param players es un Object[][] con los datos de los jugadores
     * @return boolean que dice si ganan los aldeanos o no
     */
    private static boolean gananAldeanos(Object[][] players) {
        int countLobos = 0;

        for (int i=0; i<players.length; i++) {
            if ( ((String) players[i][2]).equalsIgnoreCase("Lobo")  ) {
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

    /**
     * Comprueba si ya han ganado los lobos
     * @param players es un Object[][] con los datos de los jugadores
     * @return boolean que dice si ganan los lobos o no
     */
    private static boolean gananLobos(Object[][] players) {
        int countVivos = 0;
        int countLobos = 0;
        String playerAuxS;
        int playerAuxI;

        for (int i=0; i<players.length; i++) {
            playerAuxI = (int) players[i][3];
            if ( playerAuxI >= 1 ) {
                countVivos += 1;
            }

            playerAuxS = (String) players[i][2];
            if ( playerAuxS.equalsIgnoreCase("Lobo")  ) {
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

    private static Object[][] noche(Object[][] players) {
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

        if (gananLobos(players)) { return players; }
        if (gananAldeanos(players)) { return players; }
        else { return dia(players); }
    }

    private static int signal(Object[][] players, String m1, String m2) {
        String nombre = "";
        String rol = "";
        int vida = 0;
        int victim = 0;

        do {
            victim = leerInt(m1, 1, 8);
            nombre = (String) players[victim - 1][1];
            rol = (String) players[victim - 1][2];
            vida = (int) players[victim - 1][3];
        } while (vida == 0 || rol.equals(m2));

        return victim - 1;
    }

    private static int cazador(Object[][] players, int victim, String rol) {
        String cazador_vic = "";

        if (rol.equals("Cazador")) {
            victim = signal(players, "El cazador mata a alguien: ", "Cazador");
            players[victim][3] = 0;
            cazador_vic = (String) players[victim][1];
            System.out.println("Ha muerto: " + cazador_vic);
        }

        return victim;
    }

    /**
     * Lee los nombres de cada jugador por consola
     * @param players es un Object[][] con los datos de los jugadores
     * @return Object[][] con los datos de los jugadores actualizados
     */
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

    /**
     * Asigna un rol aleatorio a cada jugador
     * @param players es un Object[][] con los datos de los jugadores
     * @return Object[][] con los datos de los jugadores actualizados
     */
    private static Object[][] asignarRoles(Object[][] players) {
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Ángel", "Aldeano"};
        int[] cual = arrayAleatorio();

        for (int i=0; i<players.length; i++) {
            players[i][2] = roles[cual[i]];
        }

        return players;
    }

    /**
     * Asigna una vida a cada jugador, al anciano le suma una extra
     * @param players es un Object[][] con los datos de los jugadores
     * @return Object[][] con los datos de los jugadores actualizados
     */
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

    /**
     * Genera un array para usarlo posteriormente para asignar roles
     * @return array de ints distintos del 1 al 8
     */
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

    /**
     * Genera un int aleatorio del 0 al max (excluido)
     * @param max int que marca el limite del int aleatorio
     * @return int aleatorio del 0 al max
     */
    private static int randomInt(int max) {

        Random random = new Random();

        int numeroAleatorio = random.nextInt(max);

        return numeroAleatorio;
    }

    /**
     * Lee int por consola
     * @param missatge String para el usuario
     * @param min int minimo valido
     * @param max int maximo valido
     * @return int que va del min al max
     */
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