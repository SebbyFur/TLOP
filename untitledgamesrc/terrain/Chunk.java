package terrain;

import personnages.*;
/**
 * @author DELVIGNE Brian, DIOT Sébastien, GNALY-NGUYEN Kouadjo, LEHMAN Ylon
 * @version 10/05/2021
 */
public class Chunk implements java.io.Serializable {
    /**
     * Un int pour l'emplacement sur l'axe X du Chunk sur la carte
     */
    private int chunkPosX;
    /**
     * Un int pour l'emplacement sur l'axe Y du Chunk sur la carte
     */
    private int chunkPosY;
    /**
     * Un tableau de tableaux de Square pour tout le contenu du Chunk
     */
    private Square[][] content;
    /**
     * Constructeur par initialisation
     * @param chunkPosX int
     * @param chunkPosY int
     */
    public Chunk(int chunkPosX, int chunkPosY) {
        content = new Square[15][15];
        this.chunkPosX = chunkPosX;
        this.chunkPosY = chunkPosY;
        fillWithGrass();
    }
    /**
     * Getter pour le Chunk entier
     * @return Square[][]
     */
    public Square[][] getContent() {
        return content;
    }
    /**
     * Getter pour le contenu du Square aux position (x;y) sur le Chunk
     * @param x int
     * @param y int
     * @return Square
     */
    public Square getContentAtPos(int x, int y) {
        if (x < 0 ^ y < 0 ^ x > 14 ^ y > 14) {
            return null;
        }
        return content[x][y];
    }
    /**
     * Getter pour la position sur l'axe X du Chunk sur la carte
     * @return int
     */
    public int getChunkPosX() {
        return chunkPosX;
    }
    /**
     * Getter pour la position sur l'axe Y du Chunk sur la carte
     * @return int
     */
    public int getChunkPosY() {
        return chunkPosY;
    }
    /**
     * Methode qui permet de changer le personnage de position en fonction de la direction
     * @param mob APersonnage
     * @param direction Direction
     */
    public void changeMobPos(APersonnage mob, Direction direction) {
        content[mob.squarePosY%15][mob.squarePosX%15].setMob(null);
        mob.squarePosX += direction.x;
        mob.squarePosY += direction.y;
        content[mob.squarePosY%15][mob.squarePosX%15].setMob(mob);
    }
    /**
     * Methode pour retirer le personnage des coordonnees indiquees
     * @param xSquare int
     * @param ySquare int
     */
    public void removeMobAtPos(int xSquare, int ySquare) {
        content[xSquare][ySquare].setMob(null);
        content[xSquare][ySquare].validate();
        content[xSquare][ySquare].repaint();   
    }
    /**
     * Methode pour affecter un personnage aux positions donnees
     * @param mob APersonnage
     * @param squarePosX int
     * @param squarePosY int
     */
    public void setMobAtPos(APersonnage mob, int squarePosX, int squarePosY) {
        content[squarePosX][squarePosY].setMob(mob);
    }
    /**
     * Methode pour remplir le Chunk entier d'herbe
     */
    public void fillWithGrass() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                Square grass = new Square(SquareType.GRASS3, x, y);
                content[x][y] = grass;
            }
        }
    }
    /**
     * Methode pour changer l'herbe de base en d'autres textures
     * @param squareType SquareType
     * @param count int
     */
    public void addFeatures(SquareType squareType, int count) {
        for (int x = 0; x < count; x++) {
            int[] randPos = {(int)(Math.random()*content.length), (int)(Math.random()*content.length)};
            Square feature = new Square(squareType, randPos[0], randPos[1]);
            content[randPos[0]][randPos[1]] = feature;
        }
    }
    /**
     * Methode pour utiliser le bruit de Perlin
     * @param squareType SquareType
     * @param seed long
     * @param scale int
     */
    public void perlinize(SquareType squareType, long seed, int scale) {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (Perlin.noise(Util.map(x+chunkPosX*15, 0, 15*4, 2, scale), Util.map(y+chunkPosY*15, 0, 15*4, 2, scale), seed) > 190) {
                    content[x][y] = new Square(squareType, x, y);
                }
            }
        }
    }
    /**
     * Methode pour utiliser le bruit de Perlin
     * @param squareType SquareType
     * @param seed long
     * @param scale int
     */
    public void reversePerlinize(SquareType squareType, long seed, int scale) {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (Perlin.noise(Util.map(x+chunkPosX*15, 0, 15*4, 2, scale), Util.map(y+chunkPosY*15, 0, 15*4, 2, scale), seed) > 190) {
                    content[x][y] = new Square(squareType, x, y);
                }
            }
        }
    }

    /**
     * Méthode pour faire apparaitre un ou plusieurs vilains
     * @param niveauHero int
     */
    public void spawnVilain(int niveauHero) {
       int nombreDeVilain = (int) (Math.random()*(4))+1;
       for (int i = 0; i < nombreDeVilain; i++) {
           int x = (int)(Math.random()*14)+1;
           int y = (int)(Math.random()*14)+1;
           int quelTypeDeVilain = (int)(Math.random()*3)+1;
           if (quelTypeDeVilain == 1 && !getContentAtPos(x,y).getSquareType().hasBoundingBox) {
              Loup loup = new Loup(niveauHero,x,y);
              setMobAtPos(loup,x,y);
           }
           else if (quelTypeDeVilain == 2 && !getContentAtPos(x,y).getSquareType().hasBoundingBox ) {
              Squelette squelette = new Squelette(niveauHero,x,y);
              setMobAtPos(squelette,x,y);
           }
           else if (!getContentAtPos(x,y).getSquareType().hasBoundingBox){
              Orc orc = new Orc(niveauHero,x,y);
              setMobAtPos(orc,x,y);
           }
       }
    }

    /**
     * Méthode pour faire apparaitre le BOSS
     * @param niveauHero int 
     */
    public void spawnBoss(int niveauHero) {
        int x = (int)(Math.random()*14)+1;
        int y = (int)(Math.random()*14)+1;

        if (niveauHero >= 5) {
            Boss boss = new Boss(5,x,y);
            setMobAtPos(boss,x,y);
        }
    }
}