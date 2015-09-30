import java.util.*;
/**
 * Write a description of class Rabbit here.
 * 
 * @author Morten D. Bech
 * @version September 8, 2015
 */
public class Rabbit extends Animal {
    /**
     * In order to create a new Rabbit we need to provide a
     * model og and position. Do not change the signature or
     * first line of the construction. Appending code after
     * the first line is allowed.
     */

    private int turnNumber = 0;
    private Direction prevDirection;
    private Direction goDirection = randomDirection();
    private ArrayList<Direction> foxDirections = new ArrayList<Direction>();
    private ArrayList<Integer> foxDistance = new ArrayList<Integer>();

    private int circleSize = 1;
    private int circleCount = circleSize;

    private Direction foxDirection = null;
    private int beserkCounter = 0;

    public Rabbit(Model model, Position position) {
        super(model, position);
    }

    /**
     * Opgave 1
     * Flytter sig ud af synsfeltet for ræven, ellers står stille.
     * @return
     */
    public Direction decideDirection2() { // opgave 1
        lookAround();
        Direction toReturn = Direction.STAY;
        if (foxDirections.size() != 0){
            toReturn = tryEscape(foxDirections.get(0));
        }
        return toReturn;
    }

    /**
     * Opgave 2
     * @return
     */
    public Direction decideDirectio3() {
        lookAround();
        if (foxDirections.size() != 0){
            if (foxDistance.get(0) < 2){
                if (circleCount < circleSize){
                    circleCount++;
                    return Direction.turn(foxDirections.get(0), 4);
                }
                else {
                    circleCount = 0;
                    return Direction.turn(foxDirections.get(0), 5);
                }
            }
        }

        return Direction.STAY;
    }

    /**
     * Opgave 3
     * Bruger samme metode som opgave 1, men gemmer de mulige retninger for hver ræv, og derefter sammenligner listen
     * for mulige bevægelser for alle rævene, og vælger en der er mulig for alle rævene.
     * @return
     */
    public Direction decideDirection4(){
        lookAround();
        System.out.println("fox directions: " + foxDirections);
        ArrayList<ArrayList<Direction>> foxesDirections = new ArrayList<ArrayList<Direction>>();
        for (Direction foxD : foxDirections){
            ArrayList<Direction> validDirections = new ArrayList<Direction>();
            for (int i = 3; i > 0; i--){
                if (canMove(Direction.turn(foxD, i))){
                    validDirections.add(Direction.turn(foxD, i));
                }
                if (canMove(Direction.turn(foxD, -i))){
                    validDirections.add(Direction.turn(foxD, -i));
                }
            }
            foxesDirections.add(validDirections);
        }
        System.out.println("foxes Directions: " + foxesDirections);

        ArrayList<Direction> goDirections = new ArrayList<Direction>();
        for (int i = 0; i < foxesDirections.size() / 2 + (foxesDirections.size() % 2); i++){
            for (int n = 0; n < foxesDirections.get(i).size(); n++){
                boolean available = true;
                for (int k = 0; k < foxesDirections.size(); k++) {
                    if (!foxesDirections.get(k).contains(foxesDirections.get(i).get(n))) {
                        available = false;
                    }
                }
                if (available){
                    goDirections.add(foxesDirections.get(i).get(n));
                }
            }
        }

        System.out.println("go directions: " + goDirections);

        if (foxDirections.size() > 0){
            for (Direction dire : goDirections){
                if (dire != prevDirection){
                    prevDirection = dire;
                    return dire;
                }
            }

            prevDirection = foxesDirections.get(0).get(0);
            return foxesDirections.get(0).get(0);

        }
        return Direction.STAY;
    }

    /**
     * Opgave 4
     * Søg efter carrots, derefter jag ræven.
     * @return
     */
    public Direction decideDirection(){
        Direction direction = randomDirection();
        Direction carrotDirection = null;

        // Check efter ræve
        for(Direction d : Direction.allDirections()) {
            Class<?> observed = look(d);
            if(observed == Fox.class) {
                foxDirection = d;
            }
        }

        // Check efter gulerødder
        for(Direction d : Direction.allDirections()) {
            Class<?> observed = look(d);
            if(observed == Carrot.class) {
                if (carrotDirection != null ){
                    if (distance(carrotDirection) > distance(d)){
                        carrotDirection = d;
                    }
                }else{
                    carrotDirection = d;
                }
            }
        }

        if (!isBeserk()){
            // Bevæg mod gulerod hvis fundet
            if (carrotDirection != null){
                direction = carrotDirection;
                //Hvis gulerod bliver spist i næste runde
                if (distance(carrotDirection) == 1){
                    beserkCounter += 30;
                }
            } else {
                //Flygt fra ræv
                lookAround();
                if (foxDirections.size() != 0){
                    direction = tryEscape(foxDirections.get(0));
                }
            }
        }else{
            if (foxDirection != null){
                // Jag ræv kun hvis man kan nå det før beserk udløber
                if (distance(foxDirection) < (beserkCounter/2)+1){
                    if (distance(foxDirection) == 2){
                        direction = Direction.STAY;              
                    } else {
                        direction = foxDirection;
                    }
                }
            }
        }

        if (beserkCounter > 0){
            beserkCounter--;
            System.out.println(beserkCounter);
        }

        return direction;
    }

    /**
     * Brugt i opgave 1 til at bestemme bevægelses retning
     * @param direct
     * @return
     */
    private Direction tryEscape(Direction direct){
        for (int i = 3; i > 0; i--){
            if (canMove(Direction.turn(direct, i))){
                return Direction.turn(direct, i);
            }
            if (canMove(Direction.turn(direct, -i))){
                return Direction.turn(direct, -i);
            }
        }
        if (canMove(Direction.turn(direct, 4))){
            return Direction.turn(direct, 4);
        }
        if (canMove(Direction.turn(direct, 0))){
            return Direction.turn(direct, 0);
        }
        return Direction.STAY;
    }

    /**
     * Kigger i alle retninger, og tilføjer rævene til nogle lister
     */
    private void lookAround(){
        clearAreaKnowledge();
        Iterable<Direction> directions = Direction.allDirections();
        for (Direction d : directions) {
            Class<?> whatsThere = look(d);
            int dist = distance(d);

            if (whatsThere == Fox.class) {

                foxDistance.add(dist);
                foxDirections.add(d);
            }
        }
    }

    /**
     * clear the ArrayLists, which is storing the knowledge of the surrundings in the turn.
     */
    private void clearAreaKnowledge(){
        foxDistance.clear();
        foxDirections.clear();
    }

    /**
     * This method is used to retrieve who the authors are.
     */
    public String getCreator() {
        return "DA3 - 02 Nickolaj Jepsen og Frederik Aarup Lauridsen";
    }
}
