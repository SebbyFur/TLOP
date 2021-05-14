package terrain;

import objets.*;
import personnages.*;
/**
 * @author DELVIGNE Brian, DIOT Sébastien, GNALY-NGUYEN Kouadjo, LEHMAN Ylon
 * @version 10/05/2021
 */
public class Map implements java.io.Serializable {
    /**
     * Un tableau de tableaux de Chunk pour chaque case de la carte
     */
    private Chunk[][] map;
    /**
     * Un int pour connaitre la taille de la carte en longueur
     */
    private int sizeX;
    /**
     * Un int pour connaitre la taille de la carte en largeur
     */
    private int sizeY;
    /**
     * Un long pour la valeur de la "seed" c'est-a-dire uen carte unique avec un seul code
     */
    private long seed;
    /**
     * Un int pour savoir exactement dans quel Chunk se trouve le joueur sur l'axe X
     */
    public int curChunkX = 0;
    /**
     * Un int pour savoir exactement dans quel Chunk se trouve le joueur sur l'axe Y
     */
    public int curChunkY = 0;
    /**
     * Constructeur par initialisation
     * @param sizeX int pour la taille en longueur
     * @param sizeY int pour la taille en largeur
     * @param seed long pour la version de la carte
     */
    public Map(int sizeX, int sizeY, long seed) {
        map = new Chunk[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.seed = seed;
        populateMap(seed);
    }
    /**
     * Constructeur par defaut
     */
    public Map() {
        this(4, 4, 99999l);
    }
    /**
     * Methode pour generer la carte
     * @param seed long pour generer une version de la carte
     */
    private void populateMap(long seed) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Chunk chunk = new Chunk(x, y);
                chunk.addFeatures(SquareType.TREE, 6);
                chunk.addFeatures(SquareType.ROCK, 2);
                chunk.addFeatures(SquareType.BUSHES, 3);
                chunk.perlinize(SquareType.GRASS1, seed * 14894465l, 18);
                chunk.perlinize(SquareType.GRASS2, seed * 132467885l, 18);
                chunk.reversePerlinize(SquareType.WATER2, seed * 4564646l, 10);
                map[x][y] = chunk;
            }
        }
    }
    /**
     * Getter pour la position d'un Chunk dans la carte grace a ses coordonnees
     * @param x int pour la position en X
     * @param y int pour la position en Y
     * @return Chunk 
     */
    public Chunk getChunkAtPos(int x, int y) {
        return map[x][y];
    }
    /**
     * Getter pour connaitre le Chunk dans lequel le joueur est actuellement
     * @return Chunk
     */
    public Chunk getCurrentlyLoadedChunk() {
        return map[curChunkX][curChunkY];
    }
    /**
     * Methode 
     * @return double
     */
    public double map(double x, double input_min, double input_max, double output_min, double output_max) {
        return (x - input_min)*(output_max-output_min)/(input_max-input_min)+output_min;
    }
    /**
     * Getter pour la taille de la carte en longueur
     * @return int
     */
    public int getSizeX() {
        return sizeX;
    }
    /**
     * Getter pour la taille de la carte en largeur
     * @return int
     */
    public int getSizeY() {
        return sizeY;
    }
    /**
     * Getter pour le seed de la map
     * @return long
     */
    public long getSeed() {
        return seed;
    }
    /**
     * Methode pour ajouter un personnage a une position donnee dans un chunk donne
     * @param mob APersonnage a placer
     * @param chunk Chunk dans lequel le personnage se deplacera
     * @param xSquare int les coordonnees en X ou le personnage sera deplace
     * @param ySquare int les coordonnees en Y ou le personnage sera deplace
     */
    public void addMobAtPos(APersonnage mob, Chunk chunk, int xSquare, int ySquare) {
        try {
            if (chunk == null ^ xSquare < 0 ^ ySquare < 0 ^ xSquare > 14 ^ ySquare > 14) {
                throw new IllegalArgumentException("Chunk is null, or square position is invalid. (must be [0;14]");
            }
            mob.squarePosX = chunk.getChunkPosX()*15+xSquare;
            mob.squarePosY = chunk.getChunkPosY()*15+ySquare;
            map[chunk.getChunkPosX()][chunk.getChunkPosY()].setMobAtPos(mob, xSquare%15, ySquare%15);
        } catch(IllegalArgumentException err) {
            err.printStackTrace();
        }
    }

    public int[] findValidSpawn(Chunk chunk) {
        int xValid = -1;
        int yValid = -1;
        boolean stop = false;

        while (!(stop)) {
            yValid++;
            while (!(stop)) {
                xValid++;
                if (chunk.getContentAtPos(xValid, yValid).isSpawnValid()) {
                    stop = true;
                }
            }
        }
        int[] pos = {xValid, yValid};

        return(pos);
    }

    public void spawnMob(APersonnage mob) {
        if (mob.squarePosX == -1 && mob.squarePosY == -1) {    
            Chunk chunk = getCurrentlyLoadedChunk();
            int[] pos = findValidSpawn(chunk);
            addMobAtPos(mob, chunk, pos[0]%15, pos[1]%15);
        } else {
            addMobAtPos(mob, getChunkOfMob(mob), mob.squarePosX%15, mob.squarePosY%15);
        }
    }

    /**
     * Methode pour la prochaine position du personnage
     * @param mob Le personnage a deplacer
     * @param direction La direction dans laquelle le personnage se dirige
     */
    public void changeMobPos(APersonnage mob, Direction direction) {
        int xNextPosition = mob.squarePosX+direction.x;
        int yNextPosition = mob.squarePosY+direction.y;

        System.out.println(mob.getMobType() + " " + direction);

        if (xNextPosition >= 0 && xNextPosition < sizeX*15 && yNextPosition >= 0 && yNextPosition < sizeY*15) {
            Square squareNow = getSquareOfMob(mob);
            Square squareNext = map[(int)(xNextPosition/15)][(int)(yNextPosition/15)].getContentAtPos(xNextPosition%15, yNextPosition%15);
            if (mob instanceof AHero) {
                squareUpdate((AHero)mob, squareNext);
            }
            if (!(squareNext.getSquareType().hasBoundingBox) && squareNext.getMob() == null) {
                squareNow.setMob(null);
                if ((int)(xNextPosition/15) != (int)(mob.squarePosX/15) ^ (int)(yNextPosition/15) != (int)(mob.squarePosY/15) && mob instanceof AHero) {
                    curChunkX += direction.x;
                    curChunkY += direction.y;
                }
                mob.squarePosX = xNextPosition;
                mob.squarePosY = yNextPosition;
                squareNext.setMob(mob);
            }
        }
    }
    /**
     * Methode pour mettre a jour les blocs
     * @param mob AHero
     * @param squareNext Square
     */
    public void squareUpdate(AHero mob, Square squareNext) {
        if (squareNext.getSquareType() == SquareType.WATER2 && mob.getInventaire().retirerObjet(new Buche(2))) {
            squareNext.setSquareType(SquareType.WOOD);
        }

        if (squareNext.getSquareType() == SquareType.TREE) {
            squareNext.setSquareType(SquareType.SOUCHE);
            int random = (int)(Math.random()*(5-2)+2);
            mob.getInventaire().addObjetToInv(new Buche(random));
        }
    }

    public Chunk getChunkOfMob(APersonnage mob) {
        return map[(int)(mob.squarePosX/15)][(int)(mob.squarePosY/15)];
    }

    public Square getSquareOfMob(APersonnage mob) {
        return getChunkOfMob(mob).getContentAtPos(mob.squarePosX%15, mob.squarePosY%15);
    }

    public AVilain vilainAAttaquer(AHero hero) {
        int x = hero.squarePosX;
        int y = hero.squarePosY;

        AVilain vilain = null;
        if (x < 15) {
          vilain = (AVilain)map[(int)(x/15)][(int)(y/15)].getContentAtPos((x+1)%15, y%15).getMob();            
        }


        if (vilain == null && x > 0) {
            vilain = (AVilain)map[(int)(x/15)][(int)(y/15)].getContentAtPos((x-1)%15, y%15).getMob();
            if(vilain == null && y < 15) {
                vilain = (AVilain)map[(int)(x/15)][(int)(y/15)].getContentAtPos(x%15, (y+1)%15).getMob();
                if(vilain == null && y > 0) {
                  vilain = (AVilain)map[(int)(x/15)][(int)(y/15)].getContentAtPos(x%15, (y-1)%15).getMob();
                }
            }
        }        
        return vilain;
    }

    public void moveVilains(AHero hero) {
        Chunk chunk = getCurrentlyLoadedChunk();
        AVilain[] vilains = chunk.getVilains();

        for (int i = 0; i < vilains.length; i++) {
            if (vilains[i] != null) {
                Direction path = findPath(vilains[i], hero);
                changeMobPos(vilains[i], path);
            }
        }
    }

    public Direction findPath(AVilain vilain, AHero hero) {
        Direction direction = null;
        int xHero = hero.squarePosX-vilain.squarePosX;
        int yHero = hero.squarePosY-vilain.squarePosY;
        double length = Math.sqrt(Math.pow(xHero, 2) + Math.pow(yHero, 2));

        System.out.println(vilain.squarePosX + " " + vilain.squarePosY);
        System.out.println(hero.squarePosX + " " + hero.squarePosY);

        double cos = Util.map(xHero, 0, length, 0, 1);
        double sin = Util.map(yHero, 0, length, 0, 1);
        
        double angle = Math.toDegrees(Math.atan2(sin, cos));

        if (angle < 0) {
            angle = -angle + 180;
        }

        System.out.println(angle);

        if ((angle >= 0 && angle < 45) ^ angle >= 315) {
            direction = Direction.RIGHT;
        }
        if (angle >= 45 && angle < 135) {
            direction = Direction.DOWN;
        }
        if (angle >= 135 && angle < 225) {
            direction = Direction.LEFT;
        }
        if (angle >= 225 && angle < 315) {
            direction = Direction.UP;
        }

        return(direction);
    }
}