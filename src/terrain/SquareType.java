package terrain;
/**
 * @author DELVIGNE Brian, DIOT Sébastien, GNALY-NGUYEN Kouadjo, LEHMAN Ylon
 * @version 16/05/2021
 * Enumeration pour tous les types de Square qui existe
 * {@link #TREE}
 * {@link #BUSHES}
 * {@link #ROCK}
 * {@link #CHEST}
 * {@link #CHEST}
 * {@link #WATER1}
 * {@link #GRASS1}
 * {@link #GRASS2}
 * {@link #GRASS3}
 * {@link #BIG_ROCK}
 * {@link #SAND}
 * {@link #WOOD}
 * {@link #SOUCHE}
 */
public enum SquareType {
    /**
     * Type du Square d'un arbre sans hitbox
     */
    TREE("/assets/Arbre.png", true, false),
    /**
     * Type du Square d'un buisson sans hitbox
     */
    BUSHES("/assets/Buisson.png", true, false),
    /**
     * Type du Square d'un caillou avec hitbox
     */
    ROCK("/assets/Caillou.png", true, false),
    /**
     * Type du Square d'un coffre sans hitbox
     */
    CHEST("/assets/Coffre.png", false, false),
    /**
     * Type du Square de l'eau avec hitbox
     */
    WATER1("/assets/Eau1.png", true, false),
    /**
     * Type du Square d'herbe haute sans hitbox
     */
    GRASS1("/assets/Herbe1.png", false, true),
    /**
     * Type du Square d'herbe avec fleurs sans hitbox
     */
    GRASS2("/assets/Herbe2.png", false, true),
    /**
     * Type du Square d'herbe simple sans hitbox
     */
    GRASS3("/assets/Herbe3.png", false, true),
    /**
     * Type du Square d'un rocher avec hitbox
     */
    BIG_ROCK("/assets/Rocher.png", true, false),
    /**
     * Type du Square de sable sans hitbox
     */
    SAND("/assets/Sable.png", false, false),
    /**
     * Type du Square de bois sans hitbox
     */
    WOOD("/assets/Bois.png", false, false),
    /**
     * Type du Square de souche avec hitbox
     */
    SOUCHE("/assets/Souche.png", true, true),
    /**
     * Type du Square de trou avec hitbox
     */
    TROU("/assets/Trou.png", true, false);
    /**
     * Un String pour le chemin de la texture
     */
    public final String path;
    /**
     * Un boolean pour savoir si un Square peut etre traverser par un personnage ou non
     */
    public final boolean hasBoundingBox;
    /**
     * Un boolean pour savoir si un Square est creusable
     */
    public final boolean isDiggable;
    /**
     * Constructeur par initialisation
     * @param path String
     * @param hasBoundingBox boolean
     */
    private SquareType(String path, boolean hasBoundingBox, boolean isDiggable) {
        this.path = path;
        this.hasBoundingBox = hasBoundingBox;
        this.isDiggable = isDiggable;
    }
}