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
    private ArrayList<Direction> foxDirections = new ArrayList<Direction>();
    private ArrayList<Integer> foxDistance = new ArrayList<Integer>();

    private ArrayList<ArrayList<String>> grid = new ArrayList<ArrayList<String>>();

    private int circleSize = 1;
    private int circleCount = circleSize;
    public Rabbit(Model model, Position position) {
        super(model, position);
        populateGrid();
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */

    public Direction decideDirection2() { // opgave 1
        turnNumber+=1;
        lookAround();
        Direction toReturn = Direction.STAY;
        if (foxDirections.size() != 0){
            toReturn = tryEscape(foxDirections.get(0));
        }
        return toReturn;
    }

    public Direction decideDirectio3() { // opgave 2
        turnNumber+=1;
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
     * Lav et grid med alt vi kan se, og vind ved at bruge de oplysninger.
     * @return
     */
    public Direction decideDirection(){
        turnNumber+=1;
        lookAroundGrid();
        
        Direction direction = randomDirection();
        Direction latest = direction;
        int i = 1;
        while(!canMove(direction) && i < 8) {
            direction = Direction.turn(direction, 1);
            i++;
        }
        Position pos = getCoordinate(direction, 1);
        // System.out.println(pos.getRow() + " " + pos.getColumn() + " " + grid.get(pos.getRow()).get(pos.getColumn()));
        if (grid.get(pos.getRow()).get(pos.getColumn()) == "3"){
            editGrid(pos.getRow(), pos.getColumn(), "0");
        }

        printGrid();
        if(canMove(direction)) {
            return direction;
        }
        return Direction.STAY;
    }

    /**
     * Used in task 1,
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
     * Looks around the area and add what it sees to some ArrayLists
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
            else if (whatsThere == Bush.class){

            }
        }
    }

    /**
     * 1 = kanin
     * 2 = busk
     * 3 = gulerod
     * 7 = ræv ukendt
     * 8 = ræv nord
     * 9 = ræv nord øst
     * 10 = ræv øst
     * 11 = ræv syd øst
     * 12 = ræv syd
     * 13 = ræv syd vest
     * 14 = ræv vest
     * 15 = ræv nord vest
     */
    private void lookAroundGrid(){
        int myRow = getPosition().getRow();
        int myColumn = getPosition().getColumn();
        
        Iterable<Direction> directions = Direction.allDirections();
        for (Direction d : directions) {
            Class<?> whatsThere = look(d);
            int dist = distance(d);
            
            if (whatsThere == Bush.class){
                Position pos = getCoordinate(d, distance(d));
                editGrid(pos.getRow(), pos.getColumn(), "2");
            }

            else if (whatsThere == Carrot.class){
                Position pos = getCoordinate(d, distance(d));
                editGrid(pos.getRow(), pos.getColumn(), "3");
            }

            if (whatsThere == Fox.class) {
                Position pos = getCoordinate(d, distance(d));
                editGrid(pos.getRow(), pos.getColumn(), turnNumber + " 7");
                analyzeGrid();
            }


        }
        
    }

    private void editGrid(int row, int column, String number){
        grid.get(row).set(column, number);
    }

    /*private ArrayList<ArrayList<Integer>> nextTurn(int turns){
        
    }*/

    /**
     * clear the ArrayLists, which is storing the knowledge of the surrundings in the turn.
     */
    private void clearAreaKnowledge(){
        foxDistance.clear();
        foxDirections.clear();
    }

    private void populateGrid(){
        for (int i = 0; i < 20; i++){
            ArrayList<String> row = new ArrayList<String>();
            for (int n = 0; n < 20; n++){
                row.add("0");
            }
            grid.add(row);
        }
    }

    private Position getCoordinate(Direction direc, int dist){
        int row = getPosition().getRow();
        int column = getPosition().getColumn();

        switch(direc){
            case N:
                row -= dist;
                break;
            case NE:
                row -= dist;
                column += dist;
                break;
            case E:
                column += dist;
                break;
            case SE:
                row += dist;
                column += dist;
                break;
            case S:
                row += dist;
                break;
            case SW:
                row += dist;
                column -= dist;
                break;
            case W:
                column -= dist;
                break;
            case NW:
                row -= dist;
                column -= dist;
                break;
        }

        return new Position(column, row);
        
    }

    private void analyzeGrid(){
        System.out.println("Find u af hvilken retning ræven går, og opdater grid, hvis muligt");
    }

    private void printGrid(){
        System.out.println("Tur nummer: " + turnNumber);
        for (ArrayList<String> row: grid){
            System.out.println(row);
        }
        System.out.println();
    }
    
    /**
     * This method is used to retrieve who the authors are.
     */
    public String getCreator() {
        return "DA3 - 02 Nickolaj Jepsen og Frederik Aarup Lauridsen";
    }
}
