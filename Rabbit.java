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
    private int close = 10;
    private ArrayList<Direction> foxDirections = new ArrayList<Direction>();
    private ArrayList<Integer> foxDistance = new ArrayList<Integer>();
    private ArrayList<Direction> bushDirections = new ArrayList<Direction>();
    private ArrayList<Integer> bushDistance = new ArrayList<Integer>();
    private ArrayList<Direction> wallDirections = new ArrayList<Direction>();
    private ArrayList<Integer> wallDistance = new ArrayList<Integer>();

    private ArrayList<ArrayList<Integer>> grid = new ArrayList<ArrayList<Integer>>();

    private int circleSize = 1;
    private int circleCount = circleSize;
    public Rabbit(Model model, Position position) {
        super(model, position);
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */

    public Direction decideDirection2() { // opgave 1
        lookAround();
        Direction toReturn = Direction.STAY;
        if (foxDirections.size() != 0){
            toReturn = tryEscape(foxDirections.get(0));
        }
        return toReturn;
    }

    public Direction decideDirectio3() { // opgave 2
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
     * Find en busk, og gå rundt om den, så ræven ikke kan se kaninen.
     * @return
     */
    public Direction decideDirection(){
        lookAroundGrid();

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

            else if (whatsThere == Bush.class){
                Position pos = getCoordinate(d);
                editGrid(pos.getRow(), pos.getColumn(), 2);
            }

            else if (whatsThere == Carrot.class){
                Position pos = getCoordinate(d);
                editGrid(pos.getRow(), pos.getColumn(), 3);
            }

            if (whatsThere == Fox.class) {
                Position pos = getCoordinate(d);
                editGrid(pos.getRow(), pos.getColumn(), 7);
            }

        }
    }

    private void editGrid(int row, int column, int number){
        grid.get(row).set(column, number)
    }

    private ArrayList<ArrayList<Integer>> nextTurn(int turns){

    }

    /**
     * clear the ArrayLists, which is storing the knowledge of the surrundings in the turn.
     */
    private void clearAreaKnowledge(){
        foxDistance.clear();
        foxDirections.clear();
        bushDistance.clear();
        bushDirections.clear();
        wallDistance.clear();
        wallDirections.clear();
    }

    private void populateGrid(){
        for (int i = 0; i < 20; i++){
            ArrayList<Integer> row = new ArrayList<Integer>();
            for (int n = 0; n < 20; n++){
                row.add(0);
            }
            grid.add(row);
        }
    }

    private Position getCoordinate(Direction direc){
        int row = getPosition().getRow();
        int column = getPosition().getColumn();
        int dist = distance(direc);

        switch(direc){
            case N:
                column -= dist;
            case NE:
                column -= dist;
                row += dist;
            case E:
                row += dist;
            case SE:
                column += dist;
                row += dist;
            case S:
                column += dist;
            case SW:
                column += dist;
                row -= dist;
            case W:
                row -= dist;
            case NW:
                column -= dist;
                row -= dist;
        }
        Position returnValue = new Position(column, row);
    }

    public void printGrid()
    
    /**
     * This method is used to retrieve who the authors are.
     */
    public String getCreator() {
        return "Frederik Aarup Lauridsen, 201504700";
    }
}
