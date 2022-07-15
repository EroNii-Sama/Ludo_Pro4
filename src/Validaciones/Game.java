package Validaciones;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import GUI.Main;
import interfaces.ObservadoIF;
import interfaces.ObservadorIF;

public class Game implements ObservadoIF {

    private Pieces pieces;
    private GetByColor getByColor;
    private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();

    private int currentPlayer;

    int GetCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Establezca el jugador actual y notifique a todos los observadores.
     *
     * @param current el jugador actual
     */
    void SetCurrentPlayer(int current) {
        currentPlayer = current;
        ListIterator<ObservadorIF> li = lst.listIterator();
        while (li.hasNext())
            li.next().notify(this);
    }

    private int currentState;

    int GetCurrentState() {
        return currentState;
    }

    /**
     * Establezca el estado actual y notifique a todos los observadores.
     *
     * @param current el estado actual del objeto
     */
    void SetCurrentState(int current) {
        currentState = current;
        ListIterator<ObservadorIF> li = lst.listIterator();
        while (li.hasNext())
            li.next().notify(this);
    }

    private boolean waitingForPlayer = false;

    /**
     * "Devuelve verdadero si el juego está esperando que el jugador presione una tecla".
     * <p>
     * La primera línea de la función es la declaración de la función. Le dice al compilador cuál es el nombre de la función,
     * qué tipo de valor devuelve y qué parámetros toma. En este caso, el nombre de la función es IsWaitingForPlayer, devuelve
     * un valor booleano y no toma parámetros.
     *
     * @return Un valor booleano.
     */
    boolean IsWaitingForPlayer() {
        return waitingForPlayer;
    }

    /**
     * Establezca la variable waitForPlayer en el valor del parámetro de espera.
     *
     * @param waiting Si el jugador está esperando o no a que el otro jugador haga un movimiento.
     */
    void SetWaitingForPlayer(boolean waiting) {
        waitingForPlayer = waiting;
    }

    private int dadoValue;

    int GetDadoValue() {
        return dadoValue;
    }

    /**
     * Para cada observador en la lista, llame a la función de notificación del observador, pasando el sujeto como parámetro.
     *
     * @param value El valor de los dados.
     */
    void SetDadoValue(int value) {
        dadoValue = value;
        ListIterator<ObservadorIF> li = lst.listIterator();
        while (li.hasNext())
            li.next().notify(this);
    }

    private Random random = new Random();
    private int changeCurrentStateTo;
    private int valueNeededToLeaveStartPlace = 5;
    private int roundsPlayed = 0;

    private static Game instance = null;

    /**
     * Si la instancia es nula, cree una nueva instancia de la clase; de lo contrario, devuelva la instancia existente.
     *
     * @return La instancia de la clase Game.
     */
    public static Game GetJuego() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    // Creando una nueva instancia de la clase Game.
    private Game() {
        pieces = Pieces.GetPieces();
        getByColor = GetByColor.GetGetByColor();
    }

    /**
     * Esta función establece las piezas iniciales para cada jugador.
     */
    void StartGamePieces() {
        pieces.SetRedPieces(getByColor.getStartRed());
        pieces.SetBluePieces(getByColor.getStartBlue());
        pieces.SetGreenPieces(getByColor.getStartGreen());
        pieces.SetYellowPieces(getByColor.getStartYellow());
    }

    /**
     * > Esta función establece el estado actual en 6
     */
    void StartNewRound() {
        SetCurrentState(6);
    }

    /**
     * Inicializa el juego
     */
    void StartGame() {
        StartGamePieces();
        SetCurrentPlayer(0);
        SetCurrentState(0);
        SetDadoValue(-1);
    }

    /**
     * Tira los dados y comprueba si el jugador tiene una pieza al principio y si el valor del dado es 6. Si ambos son
     * ciertos, el valor del dado cambia a 7 y el estado cambia a 4.
     */
    void RollDado() {
        for (int i = 1; i < 7; i++)
            SetDadoValue(i);

        SetDadoValue(random.nextInt(6) + 1);

        // Comprobando si el jugador tiene una pieza al principio y si el valor del dado es 6. Si ambos son ciertos, el valor del
        // dado cambia a 7 y el estado cambia a 4.
        if (GetDadoValue() == 6 && !this.IsThereAPieceInStart(this.GetCurrentPlayersColor())) {
            SetDadoValue(7);
            changeCurrentStateTo = 4;
        }
    }

    /**
     * Si el juego está esperando que el jugador haga un movimiento, y el jugador hace clic en una pieza, intente mover esa
     * pieza. Si la pieza se puede mover, configure el juego para que no espere a que el jugador haga un movimiento, y si el
     * juego no está esperando a que el jugador haga un movimiento, cambie al siguiente jugador.
     *
     * @param e El evento del mouse que se activó.
     */
    void MouseClicked(MouseEvent e) {
        if (IsWaitingForPlayer()) {
            int[] pieceSelected = PieceSelected(e);
            if (pieceSelected != null) {
                boolean moved = TryMoving(pieceSelected);
                if (moved) {
                    SetWaitingForPlayer(changeCurrentStateTo != 5 ? false : true);
                    if (!IsWaitingForPlayer())
                        NextPlayer();
                    else
                        MovePiece();
                    return;
                }
            }
            SetCurrentState(3);
            return;
        }
    }

    /**
     * Si el jugador sacó un 6 y es la segunda vez que saca un 6, devuelve la última pieza que movió al principio. De lo
     * contrario, elige una pieza para mover y muévela.
     */
    void MovePiece() {
        SetWaitingForPlayer(false);
        String color = GetCurrentPlayersColor();
        if (GetDadoValue() == 6 && this.roundsPlayed == 2)
            ReturnLastPieceMovedToStart(color);
        else
            ChooseMovementAndMovePiece(color);
    }

    /**
     * Comprueba si una pieza está en la zona de inicio del tablero
     *
     * @param piece La pieza que desea comprobar si está en el inicio.
     * @param color El color de la pieza que desea comprobar.
     * @return Un valor booleano.
     */
    boolean IsInStart(int[] piece, String color) {
        int[][] startpieces = getByColor.GetStartPlacesByColor(color);
        for (int[] startpiece : startpieces)
            if (Arrays.equals(piece, startpiece))
                return true;
        return false;
    }

    /**
     * Esta función comprueba si una pieza está en el lugar final
     *
     * @param piece La pieza que desea verificar si está en el lugar final.
     * @param color El color de la pieza que desea comprobar.
     * @return Un valor booleano.
     */
    boolean IsInEnd(int[] piece, String color) {
        int[] endplace = getByColor.GetCoordinatesOfEndPlaceByColor(color);
        if (Arrays.equals(piece, endplace))
            return true;
        return false;
    }

    /**
     * Cambia el jugador actual al siguiente
     */
    private void NextPlayer() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
        }

        changeCurrentStateTo = 0;
        if (GetDadoValue() == 6 && GetCurrentState() != 2 && roundsPlayed < 2)
            roundsPlayed++;
        else {
            SetCurrentPlayer(GetCurrentPlayer() < 3 ? GetCurrentPlayer() + 1 : 0);
            roundsPlayed = 0;
        }
        SetDadoValue(0);
        SetCurrentState(0);
        GameFrame.GetGamef().SetLancarDadoEnabled(true);
    }

    /**
     * Toma un evento del mouse y devuelve la pieza en la que se hizo clic, o nula si no se hizo clic en ninguna pieza
     *
     * @param e El evento del mouse que activó la función.
     * @return La pieza que fue seleccionada.
     */
    private int[] PieceSelected(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int p = Main.puntos;
        String color = GetCurrentPlayersColor();
        int[][] pieces = this.pieces.GetEncodedPiecesFromColor(color);
        for (int[] piece : pieces) {
            int[] pieceboundary = new int[]{piece[0] * p + p, piece[1] * p + p};
            if (piece[0] * p < x && x < pieceboundary[0])
                if (piece[1] * p < y && y < pieceboundary[1])
                    return piece;
        }
        return null;
    }

    /**
     * Si el jugador está en la casa de inicio, solo puede moverse si no tiene otras piezas en la casa de inicio y si el valor
     * de los dados es 6. Si el jugador está en la ruta final, solo puede moverse si el valor de los dados es suficiente. para
     * pasar a la siguiente casa o al lugar final. Si el jugador está en una casa normal, solo puede moverse si la casa a la
     * que se dirige no está bloqueada.
     *
     * @param pieceSelected La pieza que el jugador quiere mover.
     * @return Un valor booleano que indica si el movimiento fue exitoso o no.
     */
    private boolean TryMoving(int[] pieceSelected) {
        String color = GetCurrentPlayersColor();
        int decodedPiece;
        if (IsInStart(pieceSelected, color))
            decodedPiece = -1;
        else
            decodedPiece = pieces.Decode(pieceSelected);


        if (decodedPiece == -1) {
            if (!AreThereTwoPiecesInStartHouse(color)
                    && GetDadoValue() == valueNeededToLeaveStartPlace) {
                MovePieceFromStartPlaceToStartHouse(color);
                return true;
            } else
                return false;
        }


        if (decodedPiece >= 100) {
            if (GetDadoValue() + decodedPiece == getByColor.GetFirstHouseOfFinalRouteByColor(color) + 5) {
                MoveToEndPlace(decodedPiece, color);
                return true;
            } else if (GetDadoValue() + decodedPiece < getByColor.GetFirstHouseOfFinalRouteByColor(color) + 5) {
                MovePieceFromCurrentToNew(color, decodedPiece, GetDadoValue() + decodedPiece);
                return true;
            } else
                return false;
        }

        int finalHouse = getByColor.GetFinalHouseByColor(color);

        if (EntersFinalRoute(decodedPiece, color)) {
            int housesToWalkInFinalRoute = GetDadoValue() - (finalHouse - decodedPiece);
            if (housesToWalkInFinalRoute > 6)
                return false;
            else if (housesToWalkInFinalRoute == 0)
                MovePieceFromCurrentToNew(color, decodedPiece, finalHouse);
            else if (housesToWalkInFinalRoute == 6)
                MoveToEndPlace(decodedPiece, color);
            else {
                int firstofRoute = getByColor.GetFirstHouseOfFinalRouteByColor(color);
                int newhouse = firstofRoute + housesToWalkInFinalRoute - 1;
                MovePieceFromCurrentToNew(color, decodedPiece, newhouse);
            }
            return true;
        }


        int newHouse = decodedPiece + GetDadoValue() <= 52 ? decodedPiece + GetDadoValue() : decodedPiece + GetDadoValue() - 52;
        if (!IsBlocked(decodedPiece, color)) {
            MovePieceFromCurrentToNew(color, decodedPiece, newHouse);
            boolean captured = CapturePieceIfThereIsAPieceFromAnotherColorInHouse(color, newHouse);
            if (captured) {
                SetDadoValue(20);
                SetCurrentState(5);
                changeCurrentStateTo = 5;
            } else
                changeCurrentStateTo = 0;
            return true;
        }
        return false;
    }

    /**
     * Comprueba si la pieza va a entrar en el recorrido final
     *
     * @param decodedPiece La pieza que el jugador quiere mover.
     * @param color        El color de la pieza que se está moviendo.
     * @return El método devuelve un valor booleano.
     */
    private boolean EntersFinalRoute(int decodedPiece, String color) {
        int newHouse = decodedPiece + GetDadoValue() <= 52 ? decodedPiece + GetDadoValue() : decodedPiece + GetDadoValue() - 52;
        int finalHouse = getByColor.GetFinalHouseByColor(color);

        if (decodedPiece + GetDadoValue() <= 52) {
            for (int i = decodedPiece; i < decodedPiece + GetDadoValue(); i++)
                if (i == finalHouse)
                    return true;
        } else {
            for (int i = decodedPiece; i <= 52; i++)
                if (i == finalHouse)
                    return true;
            for (int i = 1; i <= newHouse; i++)
                if (i == finalHouse)
                    return true;
        }

        return false;
    }

    /**
     * Esta función mueve la pieza al lugar final
     *
     * @param decodedPiece La pieza que fue decodificada del código QR.
     * @param color        El color de la pieza que se está moviendo.
     */
    private void MoveToEndPlace(int decodedPiece, String color) {
        MovePieceFromCurrentToNew(color, decodedPiece, 0);
        if (AllPiecesAreEnded(color)) {
            GameFrame.GetGamef().EndGame();
        }

    }

    /**
     * Si hay una pieza de otro color en la casa, captúrala
     *
     * @param color el color de la pieza que se mueve
     * @param piece la pieza que queremos mover
     * @return Un valor booleano.
     */
    private boolean CapturePieceIfThereIsAPieceFromAnotherColorInHouse(String color, int piece) {
        if (IsInShelter(piece))
            return false;
        String[] otherColors = GetAllOtherColors(color);
        for (String othercolor : otherColors) {
            int[] otherpieces = pieces.GetDecodedPiecesPlacesByColor(othercolor);
            for (int otherpiece : otherpieces)
                if (otherpiece == piece) {
                    this.MovePieceFromCurrentToNew(othercolor, otherpiece, -1);
                    return true;
                }
        }
        return false;
    }

    /**
     * Si la última pieza movida por el jugador actual es menos de 100, mueva esa pieza desde su posición actual a la posición
     * inicial
     *
     * @param color El color de la pieza que quieres mover.
     */
    private void ReturnLastPieceMovedToStart(String color) {
        int lastmoved = getByColor.GetLastPieceMovedByColor(color);
        if (lastmoved < 100)
            this.MovePieceFromCurrentToNew(color, lastmoved, -1);
        NextPlayer();
    }

    /**
     * Si el jugador puede moverse automáticamente, mueva la pieza automáticamente. De lo contrario, si el jugador puede
     * moverse, espere a que el jugador se mueva. De lo contrario, dile al jugador que no puede moverse y pasa al siguiente
     * jugador.
     *
     * @param color El color del jugador al que le toca el turno.
     */
    private void ChooseMovementAndMovePiece(String color) {
        if (CanMoveAutomatically(color)) {
            MovePieceFromStartPlaceToStartHouse(color);
            NextPlayer();
        } else if (CanMove(color))
            WaitForPlayerMovement(color);
        else {
            PlayerCantMove();
            NextPlayer();
        }
    }

    /**
     * Si todas las piezas están en el inicio, y no hay dos piezas en la casa inicial, y el valor del dado es 6, entonces
     * devuelve verdadero.
     *
     * @param color El color del jugador que está jugando.
     * @return Un valor booleano.
     */
    private boolean CanMoveAutomatically(String color) {
        return AreAllPiecesInStart(color) &&
                !AreThereTwoPiecesInStartHouse(color)
                && GetDadoValue() == valueNeededToLeaveStartPlace;
    }

    /**
     * MovePieceFromStartPlaceToStartHouse(color) mueve una pieza desde el lugar de inicio hasta la casa de inicio
     *
     * @param color El color de la pieza que quieres mover.
     */
    private void MovePieceFromStartPlaceToStartHouse(String color) {
        int startHouse = getByColor.GetStartHouseByColor(color);
        MovePieceFromCurrentToNew(color, -1, startHouse);
    }

    /**
     * Si hay una pieza en la casa de inicio y el valor de los dados es 6 y no hay otra pieza en la casa de inicio,
     * entonces el jugador puede moverse
     *
     * @param color El color del jugador.
     * @return Un valor booleano.
     */
    private boolean CanMove(String color) {
        if (IsThereAPieceInStart(color) &&
                GetDadoValue() == valueNeededToLeaveStartPlace &&
                !AreThereTwoPiecesInStartHouse(color))
            return true;
        else if (IsThereAPieceInFinalRouteThatCanMove(color))
            return true;
        else if (!AreAllPiecesInStart(color) && IsThereAPieceOnNormalHousesNotBlocked(color))
            return true;
        else if (AnyPieceEntersFinalRouteAndCanMove(color))
            return true;
        return false;
    }

    /**
     * Si alguna pieza del color dado entra en la ruta final y puede moverse, devuelve verdadero
     *
     * @param color El color del jugador al que le toca el turno.
     * @return Un valor booleano.
     */
    private boolean AnyPieceEntersFinalRouteAndCanMove(String color) {
        int finalHouse = getByColor.GetFinalHouseByColor(color);
        int[] piecesPlaces = pieces.GetDecodedPiecesPlacesByColor(color);
        for (int piece : piecesPlaces) {
            if (EntersFinalRoute(piece, color)) {
                int housesToWalkInFinalRoute = GetDadoValue() - (finalHouse - piece);
                if (housesToWalkInFinalRoute <= 6)
                    return true;
            }
        }
        return false;

    }

    /**
     * Si hay una pieza en la ruta final que se puede mover, devuelve verdadero, de lo contrario, devuelve falso.
     *
     * @param color el color del jugador
     * @return verdadero si hay una pieza en la ruta final que se puede mover.
     */
    private boolean IsThereAPieceInFinalRouteThatCanMove(String color) {
        int[] piecesPlaces = pieces.GetDecodedPiecesPlacesByColor(color);
        for (int piece : piecesPlaces) {
            if (piece > 100)
                if (GetDadoValue() + piece <= getByColor.GetFirstHouseOfFinalRouteByColor(color) + 5)
                    return true;
        }
        return false;
    }

    /**
     * Comprueba si hay alguna pieza en las casas normales que no esté bloqueada
     *
     * @param color el color del jugador
     * @return Un valor booleano.
     */
    private boolean IsThereAPieceOnNormalHousesNotBlocked(String color) {
        int[] piecesPlaces = pieces.GetDecodedPiecesPlacesByColor(color);
        for (int piece : piecesPlaces) {
            if (piece < 100 && piece != -1)
                if (!IsBlocked(piece, color))
                    return true;
        }
        return false;
    }

    /**
     * Si ya hay dos piezas en la casa de destino, o si hay una barrera entre la pieza y la casa de destino, entonces la
     * pieza está bloqueada.
     *
     * @param piece la pieza que se mueve
     * @param color el color de la pieza
     * @return Un valor booleano.
     */
    private boolean IsBlocked(int piece, String color) {
        int newHouse = piece + GetDadoValue() <= 52 ? piece + GetDadoValue() : piece + GetDadoValue() - 52;
        if (AlreadyTwoPiecesInDestinationHouse(color, newHouse))
            return true;
        else if (IsThereABarrier(color, piece, newHouse))
            return true;
        return false;
    }

    /**
     * Comprueba si hay una barrera entre la pieza y la nueva casa.
     *
     * @param color    El color del jugador al que le toca el turno.
     * @param piece    la pieza que se mueve
     * @param newHouse La nueva casa en la que estará la pieza después de la mudanza.
     * @return cierto si hay una barrera entre la pieza y la nueva casa.
     */
    private boolean IsThereABarrier(String color, int piece, int newHouse) {
        int otherPieces[][] = GetAllOtherPieces(color);
        ArrayList<Integer> PlacesWherePiecesFormBarrier = new ArrayList<Integer>();
        for (int[] otherpiece : otherPieces) {
            for (int j = 0; j < otherpiece.length; j++)
                for (int k = j + 1; k < otherpiece.length; k++)
                    if (k != j && otherpiece[k] == otherpiece[j] && otherpiece[j] != -1)
                        PlacesWherePiecesFormBarrier.add(otherpiece[j]);
        }

        if (piece + GetDadoValue() < 100) {
            for (int barrier : PlacesWherePiecesFormBarrier)
                if (piece + GetDadoValue() <= 52) {
                    if (piece < barrier && barrier <= piece + GetDadoValue())
                        return true;
                } else if ((piece < barrier && barrier <= 52) || (52 < barrier && barrier < newHouse))
                    return true;
        }
        return false;
    }

    /**
     * Si la pieza está en el refugio, devuelve verdadero, de lo contrario, devuelve falso.
     *
     * @param piece la pieza que quieres comprobar
     * @return un valor booleano.
     */
    private boolean IsInShelter(int piece) {
        return piece == 52 || piece == 13 || piece == 26 || piece == 39
                || piece == 4 || piece == 17 || piece == 30 || piece == 43;
    }

    /**
     * Devuelve una matriz de matrices de enteros, cada matriz de enteros representa las piezas de un color diferente
     *
     * @param color El color de la pieza que se está moviendo.
     * @return Una matriz de matrices de enteros.
     */
    private int[][] GetAllOtherPieces(String color) {
        String otherColors[] = GetAllOtherColors(color);
        int count = 0;
        int otherPieces[][] = new int[3][4];
        for (String coulor : otherColors) {
            int pieces[] = this.pieces.GetDecodedPiecesPlacesByColor(coulor);
            otherPieces[count++] = pieces;
        }
        return otherPieces;
    }

    /**
     * Consigue todos los colores excepto el pasado.
     *
     * @param color El color de la pieza del jugador actual.
     * @return Los otros colores.
     */
    private String[] GetAllOtherColors(String color) {
        String allcolors[] = new String[]{"red", "blue", "green", "yellow"};
        String otherColors[] = new String[3];

        int count = 0;
        for (String coulor : allcolors)
            if (!coulor.equals(color))
                otherColors[count++] = coulor;
        return otherColors;
    }

    /**
     * Devuelve una cadena con los puntos de cada jugador
     *
     * @return Los puntos de cada jugador.
     */
    String GetPoints() {
        String allcolors[] = new String[]{"red", "blue", "green", "yellow"};
        int[] distssum = new int[4];
        int count2 = 0;
        for (String color : allcolors) {
            int[] dists = new int[4];
            int[] pieces = this.pieces.GetDecodedPiecesPlacesByColor(color);
            int count = 0;
            for (int piece : pieces) {
                if (piece == 0)
                    dists[count] = 0;
                else if (piece == -1)
                    dists[count] = 52;
                else if (piece > 100)
                    dists[count] = 3;
                else {
                    int walked = GetWalkedDistance(color, piece);
                    dists[count] = 52 - walked;
                }

                count++;
            }
            int distsum = 0;
            for (int dist : dists)
                distsum += dist;
            distssum[count2++] = distsum;
        }
        String newstr = "\n" + "rojo" + " puntuacion: " + distssum[0] + "\n" +
                "azul" + " puntuacion: " + distssum[1] + "\n" +
                "verde" + " puntuacion: " + distssum[2] + "\n" +
                "amarillo" + " puntuacion: " + distssum[3];

        return newstr;
    }

    /**
     * > Devuelve la distancia que ha caminado una pieza desde su casa de inicio
     *
     * @param color El color de la pieza que quieres mover.
     * @param piece La pieza que se está moviendo.
     * @return La distancia que ha caminado la pieza.
     */
    private int GetWalkedDistance(String color, int piece) {
        int finalhouse = getByColor.GetFinalHouseByColor(color);
        int starthouse = getByColor.GetStartHouseByColor(color);
        switch (color) {
            case "red":
                return piece <= finalhouse ? 9 + piece : piece - starthouse;
            case "blue":
                return piece <= finalhouse ? 22 + piece : piece - starthouse;
            case "green":
                return piece <= finalhouse ? 52 + piece : piece - starthouse;
            case "yellow":
                return piece <= finalhouse ? 13 + piece : piece - starthouse;
        }
        return 0;
    }

    /**
     * > Si la casa de destino es un refugio, compruebe si ya hay dos piezas de cualquier color en la casa de destino. Si
     * la casa de destino no es un refugio, verifica si ya hay dos piezas del color del jugador en la casa de destino.
     *
     * @param color       el color del jugador que se mueve
     * @param destination la casa a la que el jugador está tratando de mudarse
     * @return Un valor booleano.
     */
    private boolean AlreadyTwoPiecesInDestinationHouse(String color, int destination) {
        if (IsInShelter(destination)) {
            if (AlreadyTwoPiecesOfAnyColorInDestinationHouse(destination))
                return true;
        } else if (AlreadyTwoPiecesOfPlayerInDestinationHouse(color, destination))
            return true;
        return false;
    }

    /**
     * Si hay dos o más piezas de cualquier color en la casa de destino, devuelve verdadero.
     *
     * @param destination La casa a la que intenta mudarse.
     * @return El número de piezas en la casa de destino.
     */
    private boolean AlreadyTwoPiecesOfAnyColorInDestinationHouse(int destination) {
        int count = 0;
        int[] allpieces = GetAllPieces();
        for (int piece : allpieces)
            if (piece == destination)
                count++;
        return count >= 2;
    }

    /**
     * Si hay dos o más piezas del mismo color en la casa de destino, devuelve verdadero.
     *
     * @param color       El color del jugador que se está moviendo.
     * @param destination La casa a la que el jugador quiere mover su pieza.
     * @return Un valor booleano.
     */
    private boolean AlreadyTwoPiecesOfPlayerInDestinationHouse(String color, int destination) {
        int[] pieces = this.pieces.GetDecodedPiecesPlacesByColor(color);
        int count = 0;
        for (int piece : pieces)
            if (piece == destination)
                count++;
        return count >= 2;
    }

    /**
     * Espere a que el jugador se mueva y luego cambie el estado actual al valor de la variable changeCurrentStateTo, o 1
     * si changeCurrentStateTo es 0.
     *
     * @param color El color del jugador al que le toca el turno.
     */
    private void WaitForPlayerMovement(String color) {
        SetCurrentState(changeCurrentStateTo != 0 ? changeCurrentStateTo : 1);
        SetWaitingForPlayer(true);
    }

    private void PlayerCantMove() {
        SetCurrentState(2);
    }

    /**
     * Si hay dos o más piezas en la casa de inicio, devuelve verdadero, de lo contrario, devuelve falso.
     *
     * @param color El color del jugador al que le toca el turno.
     * @return El método devuelve un valor booleano.
     */
    private boolean AreThereTwoPiecesInStartHouse(String color) {
        int startHouse = getByColor.GetStartHouseByColor(color);
        int[] piecesPlaces = this.GetAllPieces();
        int count = 0;
        for (int piece : piecesPlaces)
            if (piece == startHouse)
                count++;
        return count >= 2 ? true : false;
    }

    /**
     * Si hay una pieza al principio, devuelve verdadero, de lo contrario, devuelve falso.
     *
     * @param color El color del jugador.
     * @return Un valor booleano.
     */
    private boolean IsThereAPieceInStart(String color) {
        int[] piecesPlaces = pieces.GetDecodedPiecesPlacesByColor(color);
        for (int piece : piecesPlaces)
            if (piece == -1)
                return true;
        return false;
    }

    /**
     * Esta función mueve una pieza de un lugar a otro
     *
     * @param color        El color de la pieza que quieres mover.
     * @param currentPlace El lugar actual de la pieza que se está moviendo.
     * @param newPlace     El nuevo lugar al que se muda la pieza.
     */
    private void MovePieceFromCurrentToNew(String color, int currentPlace, int newPlace) {
        int[][] pieces = this.pieces.GetEncodedPiecesFromColor(color);
        int[][] newCoordinates = GetNewCoordinates(color, currentPlace, newPlace, pieces);
        getByColor.StoreLastPieceMoved(color, newPlace);
        this.pieces.MovePieces(newCoordinates, color);
    }

    /**
     * Toma las piezas de cada color y las pone en una matriz
     *
     * @return Una matriz de todas las piezas en el tablero.
     */
    private int[] GetAllPieces() {
        int[] red = pieces.GetDecodedPiecesPlacesByColor("red");
        int[] blue = pieces.GetDecodedPiecesPlacesByColor("blue");
        int[] yellow = pieces.GetDecodedPiecesPlacesByColor("yellow");
        int[] green = pieces.GetDecodedPiecesPlacesByColor("green");
        int[] all = new int[red.length + blue.length + yellow.length + green.length];
        int count = 0;
        for (int[] pieces : new int[][]{red, blue, green, yellow}) {
            for (int piece : pieces) {
                all[count++] = piece;
            }
        }
        return all;
    }

    /**
     * Si la pieza se está moviendo hacia el inicio, establezca las nuevas coordenadas en el inicio. Si la pieza se está
     * moviendo hasta el final, establezca las nuevas coordenadas hasta el final. Si la pieza se está moviendo a un nuevo
     * lugar, establezca las nuevas coordenadas en el nuevo lugar
     *
     * @param color        El color de la pieza que se mueve.
     * @param currentPlace El lugar actual de la pieza que se está moviendo.
     * @param newPlace     El nuevo lugar al que se muda la pieza.
     * @param pieces       El estado actual del tablero.
     * @return Las nuevas coordenadas de la pieza que se ha movido.
     */
    private int[][] GetNewCoordinates(String color, int currentPlace, int newPlace, int[][] pieces) {
        int[][] newCoordinates;
        if (currentPlace == -1)
            newCoordinates = SetNewCoordinatesForPieceInStart(this.pieces.Encode(newPlace), pieces, color);
        else if (newPlace == -1)
            newCoordinates = SetNewCoordinatesForPieceMovingToStart(this.pieces.Encode(currentPlace), pieces, color);
        else if (newPlace == 0)
            newCoordinates = SetNewCoordinatesForPieceInEnd(this.pieces.Encode(currentPlace), pieces, color);
        else
            newCoordinates = SetNewCoordinates(this.pieces.Encode(currentPlace), this.pieces.Encode(newPlace), pieces);
        return newCoordinates;
    }

    /**
     * Establece las coordenadas de la pieza que ha llegado al final a las coordenadas del lugar final
     *
     * @param currentPlace El lugar actual de la pieza.
     * @param pieces       La disposición de todas las piezas en el tablero.
     * @param color        El color de la pieza que se está moviendo.
     * @return El método devuelve las nuevas coordenadas de la pieza.
     */
    private int[][] SetNewCoordinatesForPieceInEnd(int[] currentPlace, int[][] pieces, String color) {
        for (int i = 0; i < pieces.length; i++)
            if (Arrays.equals(pieces[i], currentPlace)) {
                pieces[i] = getByColor.GetCoordinatesOfEndPlaceByColor(color);
                break;
            }
        return pieces;
    }

    /**
     * Esta función establece las nuevas coordenadas para una pieza en el inicio
     *
     * @param newPlace Las nuevas coordenadas de la pieza.
     * @param pieces   La disposición de todas las piezas en el tablero.
     * @param color    El color de la pieza que se está moviendo.
     * @return Las nuevas coordenadas de la pieza que se movió.
     */
    private int[][] SetNewCoordinatesForPieceInStart(int[] newPlace, int[][] pieces, String color) {
        for (int i = 0; i < pieces.length; i++)
            if (IsInStart(pieces[i], color)) {
                pieces[i] = newPlace;
                break;
            }
        return pieces;
    }

    /**
     * Toma el lugar actual de una pieza, la matriz de todas las piezas y el color de la pieza, y devuelve la matriz de
     * todas las piezas con la pieza que estaba en el lugar actual movida al primer lugar de inicio sin una pieza.
     *
     * @param currentPlace El lugar actual de la pieza que se está moviendo hacia el inicio.
     * @param pieces       La disposición de todas las piezas en el tablero.
     * @param color        El color de la pieza que se mueve al inicio.
     * @return El método devuelve las nuevas coordenadas de la pieza que se está moviendo al inicio.
     */
    private int[][] SetNewCoordinatesForPieceMovingToStart(int[] currentPlace, int[][] pieces, String color) {
        for (int i = 0; i < pieces.length; i++)
            if (Arrays.equals(pieces[i], currentPlace)) {
                pieces[i] = GetCoordinatesOfFirstStartPlaceWithoutAPiece(color, pieces);
                break;
            }
        return pieces;
    }

    /**
     * Obtenga las coordenadas del primer lugar de inicio sin una pieza.
     *
     * @param color  El color de la pieza que quieres mover.
     * @param pieces la matriz de todas las piezas en el tablero
     * @return Las coordenadas del primer lugar de inicio sin pieza.
     */
    private int[] GetCoordinatesOfFirstStartPlaceWithoutAPiece(String color, int[][] pieces) {
        int[][] start = getByColor.GetStartPlacesByColor(color);
        boolean flag;
        for (int j = 0; j < start.length; j++) {
            flag = true;
            for (int i = 0; i < pieces.length; i++)
                if (Arrays.equals(start[j], pieces[i]))
                    flag = false;
            if (flag)
                return start[j];
        }
        return null;
    }

    /**
     * Toma el lugar actual de una pieza, el nuevo lugar de una pieza y la matriz de todas las piezas, y devuelve la matriz
     * de todas las piezas con el lugar actual reemplazado por el nuevo lugar
     *
     * @param currentPlace Las coordenadas actuales de la pieza que desea mover.
     * @param newPlace     Las nuevas coordenadas de la pieza.
     * @param pieces       La disposición de todas las piezas en el tablero.
     * @return Las nuevas coordenadas de la pieza que se movió.
     */
    private int[][] SetNewCoordinates(int[] currentPlace, int[] newPlace, int[][] pieces) {
        for (int i = 0; i < pieces.length; i++)
            if (Arrays.equals(pieces[i], currentPlace)) {
                pieces[i] = newPlace;
                break;
            }
        return pieces;
    }

    /**
     * Obtén el color del jugador actual.
     *
     * @return El color del jugador actual.
     */
    private String GetCurrentPlayersColor() {
        switch (GetCurrentPlayer()) {
            case 0:
                return "red";
            case 1:
                return "green";
            case 2:
                return "yellow";
            case 3:
                return "blue";
        }
        return null;
    }

    /**
     * Si todas las piezas del color dado están al principio, devuelve verdadero, de lo contrario, devuelve falso.
     *
     * @param color El color del jugador.
     * @return El método devuelve un valor booleano.
     */
    private boolean AreAllPiecesInStart(String color) {
        int[] piecesPlaces = pieces.GetDecodedPiecesPlacesByColor(color);
        for (int piece : piecesPlaces)
            if (piece != -1)
                return false;
        return true;
    }

    /**
     * Si todas las piezas de un cierto color están al final, devuelve verdadero, de lo contrario, devuelve falso.
     *
     * @param color El color del jugador.
     * @return verdadero si todas las piezas del color dado están al final del tablero, y falso en caso contrario.
     */
    private boolean AllPiecesAreEnded(String color) {
        int[] piecesPlaces = pieces.GetDecodedPiecesPlacesByColor(color);
        for (int piece : piecesPlaces)
            if (piece != 0)
                return false;
        return true;
    }

    @Override
    // Adición de un observador a la lista de observadores.
    public void add(ObservadorIF observador) {
        lst.add(observador);
    }

    @Override
    // Eliminación de un observador de la lista de observadores.
    public void remove(ObservadorIF observador) {
        lst.remove(observador);
    }

    @Override
    // Devolver un objeto.
    public Object get(int i) {
        return 0;
    }
}