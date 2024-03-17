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
                settings(players);
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
    private static void settings(Object[][] players) {

        for (int i = 0; i < players.length; i++) {   /* id */
            players[i][0] = i + 1;
        }

        leerNombres(players);  /* nombres */

        asignarRoles(players);  /* roles */

        asignarVidas(players);  /* roles */

    }

    /**
     * Este método contiene todas las acciones a realizar durante la ronda de noche de la partida
     * @param players Con el objeto de jugadores
     * @return El objeto de jugadores actualizado según lo que haya pasado durante la ronda
     */
    private static Object[][] noche(Object[][] players) {
        boolean fin = false;
        int victim = -1;
        int protegido = -1;
        String lobo_vic = "";
        String nombre = "";
        String rol = "";
        int vida = -1;

        for (int i = 0; i < 8; i++) {
            vida = (int) players[i][3];
            if (vida > 0) {
                nombre = (String) players[i][1];
                rol = (String) players[i][2];
                System.out.println(i + 1 + ": " + nombre + " - " + rol);
            }
        }

        System.out.println("Pueblo duerme");
        /* Vidente */
        if (estaVivo(players, "Vidente")) {
            accionVidente(players);
        }

        /* Protector */
        if (estaVivo(players, "Protector")) {
            protegido = signal(players, "Se despierta el protector y protege a alguien: ", "");
        }

        /* Lobo */
        victim = signal(players, "Se despierta el lobo y mata a alguien: ", "Lobo");

        if (victim != protegido) {
            vida = (int) players[victim][3] - 1;
            players[victim][3] = vida;
            if (vida == 0) {
                lobo_vic = (String) players[victim][1];
                rol = (String) players[victim][2];
                System.out.println("Ha muerto: " + lobo_vic + ". Su rol era: " + rol);
            }
        }

        victim = cazador(players, victim, rol);
        players[victim][3] = 0;

        if (gananLobos(players)) { return players; }
        if (gananAldeanos(players)) { return players; }
        else { return dia(players); }
    }

    /**
     * Este método contiene todas las acciones a realizar durante la ronda de día de la partida
     * @param players Con el objeto de jugadores
     * @return El objeto de jugadores actualizado según lo que haya pasado durante la ronda
     */
    private static Object[][] dia(Object[][] players) {
        int victim = -1;
        String aldea_vic = "";
        String rol = "";

        System.out.println("Se despierta la aldea");
        victim = signal(players, "El pueblo dedide a quién matar: ", "");
        players[victim][3] = 0;
        aldea_vic = (String) players[victim][1];
        rol = (String) players[victim][2];

        System.out.println("La aldea ha linchado a " + aldea_vic + ", y su rol era " + rol);

        muereAnciano(players, rol);

        victim = cazador(players, victim, rol);
        players[victim][3] = 0;

        if (gananLobos(players)) {
            return players;
        }
        if (gananAldeanos(players)) {
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
     * return Object[][] con los datos de los jugadores actualizados
     */
    private static void muereAnciano(Object[][] players, String rol) {
        if (rol.equals("Anciano")) { /* quitar poderes tras la muerte del anciano */
            System.out.println("El pueblo pierde sus poderes.");
            for (int i = 0; i < 8; i++) {
                if (!players[i][2].equals("Lobo")) {
                    players[i][2] = "Aldeano";
                }
            }
        }
    }

    /**
     * Este método ejecuta la acción de la Vidente, es decir, mirar una carta de un jugador
     * @param players El objeto con los datos de los jugadores
     */
    private static void accionVidente(Object[][] players) {
        String rol;

        int visto = signal(players, "Se despierta la vidente y mira una carta: ", "Vidente");
        String nombre = (String) players[visto][1];
        rol = (String) players[visto][2];
        System.out.println("El personaje de " + nombre + " es: " + rol);
    }

    /**
     * Este método sirve para despertar a la vidente y al protector siempre que sigan vivos
     * @param players El objeto con los datos de los jugadores
     * @param rolBuscado "Vidente" o "Protector" en nuestro caso
     * @return true o false
     */
    private static boolean estaVivo(Object[][] players, String rolBuscado) {
        for (int i=0; i< players.length; i++) {
            String rol = (String) players[i][2];
            if (rol.equalsIgnoreCase(rolBuscado)) {
                int vivo = (int) players[i][3];
                if (vivo > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Comprueba si ya han ganado los aldeanos
     * @param players es un Object[][] con los datos de los jugadores
     * @return boolean que dice si ganan los aldeanos o no
     */
    private static boolean gananAldeanos(Object[][] players) {
        int countLobos = 0;
        int vida = 0;

        for (int i=0; i<players.length; i++) {
            if ( ((String) players[i][2]).equalsIgnoreCase("Lobo")  ) {
                vida = (int) players[i][3];
                if (vida > 0) {
                    countLobos += 1;
                }
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
        double countVivos = 0;
        double countLobos = 0;
        String playerAuxS;
        int playerAuxI;

        for (int i=0; i<players.length; i++) {
            playerAuxI = (int) players[i][3];
            if ( playerAuxI >= 1 ) {
                countVivos += 1;

                playerAuxS = (String) players[i][2];
                if (playerAuxS.equalsIgnoreCase("Lobo")) {
                    countLobos += 1;
                }
            }
        }

        if ( countLobos >= (countVivos/2) ) {
            System.out.println("Ganan los lobos.");
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Este método guarda la id de la persona a la que uno o varios jugadores señalan durante la partida
     * @param players El objeto con los datos de los participantes
     * @param m1 El mensaje que debe decir el narrador
     * @param m2 Con este String validaremos que el jugador no se señale a sí mismo
     * @return la id del jugador señalado
     */
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

    /**
     * Este método es similar al signal pero con las peculiaridades del cazador
     * @param players El objeto con los datos actualizados de los jugadores
     * @param victim sólo es para que el return no se quede vacío en caso de no haber muerto el cazador
     * @param rol Es para validar que la persona que ha muerto es el cazador y así ejecutar el programa
     * @return la id del personaje que muere por acción del cazador
     */
    private static int cazador(Object[][] players, int victim, String rol) {
        String cazador_vic = "";

        if (rol.equals("Cazador")) {
            victim = signal(players, "El cazador mata a alguien: ", "Cazador");
            players[victim][3] = 0;
            cazador_vic = (String) players[victim][1];
            rol = (String) players[victim][2];
            System.out.println("Ha muerto: " + cazador_vic + ". Su rol era: " + rol);
        }

        return victim;
    }

    /**
     * Lee los nombres de cada jugador por consola
     * @param players es un Object[][] con los datos de los jugadores
     * @return Object[][] con los datos de los jugadores actualizados
     */
    private static void leerNombres(Object[][] players) {
        String confirmar = "";

        for (int i=0; i < players.length; i++ ) {

                System.out.print("Introduce el nombre del jugador " + (i+1) + ": ");
                players[i][1] = input.nextLine();

                /*System.out.println("El nombre introducido es: " + players[i][1]);
                System.out.print("Escribe 'si' para confirmar: ");
                confirmar = input.nextLine();

                }while confirmar.equals("si")*/

        }
    }

    /**
     * Asigna un rol aleatorio a cada jugador
     * @param players es un Object[][] con los datos de los jugadores
     * return Object[][] con los datos de los jugadores actualizados
     */
    private static void asignarRoles(Object[][] players) {
        String[] roles = {"Lobo", "Lobo", "Vidente", "Cazador", "Anciano", "Protector", "Aldeano", "Aldeano"};
        int[] cual = arrayAleatorio();

        for (int i=0; i<players.length; i++) {
            players[i][2] = roles[cual[i]];
        }
    }

    /**
     * Asigna una vida a cada jugador, al anciano le suma una extra
     * @param players es un Object[][] con los datos de los jugadores
     * return Object[][] con los datos de los jugadores actualizados
     */
    private static void asignarVidas(Object[][] players) {
        for (int i=0; i< players.length; i++) {

            String aux = (String) players[i][2];

            if (aux.equals("Anciano")) {
                players[i][3] = 2;
            }
            else {
                players[i][3] = 1;
            }
        }
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