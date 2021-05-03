package untitledgame.personnages;

import untitledgame.texture.*;
/** 
*  Classe abstraite Personnage  
*/ 
 
public abstract class APersonnage implements IPersonnage {
	private String nom; 
	private int pointsDeVie; 
	private int pointsDAttaque; 
	private int armure; 
    private int pointsDeVieMax; 
	private int armureMax; 
	private int niveau;
	public int squarePosX;
	public int squarePosY;
	private Texture texture = new Texture(TexturePath.UNKNOWN);
 
    /** 
    *   Constructeur par défaut 
    */ 
	public APersonnage() {
		this(100, 100, 20, 10, 50, 1, null);
	} 
	/** 
	*   Constructeur par initialisation 
	* @param nom 
	* @param pointsDeVie 
	* @param pointsDAttaque 
	* @param armure 
	*/

	public APersonnage(int pointsDeVie, int pointsDeVieMax, int pointsDAttaque, int armure, int armureMax, int niveau, Texture texture) { 
		this.pointsDeVie = pointsDeVie; 
		this.pointsDeVieMax = pointsDeVieMax;
		this.pointsDAttaque = pointsDAttaque; 
		this.armure = armure; 
		this.armureMax = armureMax;
		this.niveau = niveau;
		this.squarePosX = 0;
		this.squarePosY = 0;
		this.texture = texture;
	} 
	/** 
	* getter de la variable nom 
	* @return le nom 
	*/ 
	public String getNom() { 
		return nom; 
	}

	/** 
	* getter du square
	* @return un square
	*/ 
	public Texture getTexture() {
		return texture;
	}

	/** 
	*  getter de la variable pointsDeVie 
	* @return les points de vie 
	*/ 
	public int getPointsDeVie() { 
		return pointsDeVie; 
	} 
     
    /** 
    *  getter de la variable pointsDeVieMax 
    * @return les points de vie max 
    */ 
	public int getPointsDeVieMax() { 
		return pointsDeVieMax; 
	}  
	/** 
	*  getter de la variable pointsDAttaque 
	* @return les points d'attaque 
	*/ 
	public int getPointsDAttaque() { 
		return pointsDAttaque; 
	} 
	/** 
	*  getter de la variable armure  
	* @return l'armure 
	*/ 
	public int getArmure(){ 
		return armure; 
	} 
     
    /** 
    *  getter de armureMax 
    * @return l'armure max 
    */ 
	public int getArmureMax() { 
		return armureMax; 
	} 
 
	/** 
	* getter de niveau 	
	* @return le niveau du personnage  
	*/ 
	public int getNiveau() {
		return niveau;
	}

    /**
    * setter de nom
    * @param nom nouveau nom
    */
    public void setNom(String nom) {
    	this.nom = nom;
    }
	/**
	* setter de pointsDeVie
	* @param pvARajouter qui seront les pv a rajouter
	*/
	public void setPointsDeVie(int pvARajouter) {
		pointsDeVie += pvARajouter;
		if (pointsDeVie > pointsDeVieMax) {
			pointsDeVie = pointsDeVieMax;
		}
	}

	/**
    * setter de pointsDeVieMax
    * @param pvARajouter qui seront les pv max a rajouter
    */
	public void setPointsDeVieMax(int pvARajouter) {
		pointsDeVieMax += pvARajouter;
	}

	/**
    * setter de pointsDAttaque
    * @param pointsAttaqueARajouter points d'attaque a rajouter
    */
    public void setPointsDAttaque(int pointsAttaqueARajouter) {
    	pointsDAttaque += pointsAttaqueARajouter;
    }

    /**
    * setter de armure
    * @param armureARajouter armure a rajouter
    */
    public void setArmure(int armureARajouter) {
    	armure += armureARajouter;
    	if ( armure > armureMax) {
    		armure = armureMax;
    	}
    }

    /**
    * setter de armureMax
    * @param armureARajouter armure MAX a rajouter
    */
    public void setArmureMax(int armureARajouter) {
    	armureMax += armureARajouter;
    } 

    /**
    * setter de niveau
    * @param niveau qui sera le niveau a rajouter ( généralement 1 )
    */
    public void setNiveau(int niveau) {
        this.niveau += niveau;
    }

	/** 
	* Methode subirAttaque qui retire des pv en fonction des degats recus  
	* @param degats seront les degats a infliger ( sans prendre l'armure en compte ) 
	* @return true si l'unité n'a plus de vie 
	*/ 
    public boolean subirAttaque(int degats) { 
 
        boolean plusDeVie = false; 
        if ( degats <= armure ) { 
        	pointsDeVie -= 1; 
        } 
        pointsDeVie -= (degats - armure); 
 
        if ( pointsDeVie <= 0 ) 
        { 
        	plusDeVie = true; 
        } 
        return plusDeVie; 
    } 
 
	/** 
	* methode attaque qui permet au personnage de lancer une attaque a l'aide de la methode subirAttaque
	* @param nomDuPerso personnage a attaquer 
	* @return vrai si l'unitée est tuée faux sinon.
	*/ 
	public boolean attaquer(IPersonnage nomDuPerso) { 
 
        return nomDuPerso.subirAttaque(pointsDAttaque);  
	}

	public boolean estEnVie() {

		boolean enVie = true;

		if ( pointsDeVie <= 0 ) {
			enVie = false;
		}
		return enVie;
	}
	public String toString() {
         return "PV : "+getPointsDeVie()+ " Attaque : "+getPointsDAttaque()+ " Armure : "+getArmure();
	}

	public abstract void changeStats();
}