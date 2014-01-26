package net.fe.overworldStage;

import net.fe.FEResources;

import org.newdawn.slick.Color;

import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class Tile extends GriddedEntity implements DoNotDestroy{
	private Terrain terrain;
	private int id;
	private Tileset tileset;
	/**
	 * P - Plain
	 * A - pAth
	 * M - Mountain
	 * F - Forest
	 * W - Wall
	 * S - Sea
	 * L - fLoor
	 * T - forT
	 * K - peaK
	 * I - pIllar
	 * D - Desert
	 * C - fenCe
	 * N - None
	 * V - Village
	 */
	private static String terrainMap =
			"PPPPAAAAAAACCCCSSSS      " +
			"PPPPAAAAAAACCCCSSSS      " +
			"PPPKKPAAAFFWWWWSSSS      " +
			"MKKKAAAANNNWWWWSSSS      " +
			"CCCCAAAANVNWWLSSSWW      " +
			"NNNNNNNNFNFSLLSSS        " +
			"NVNNNNVNFVFSLLSSS        " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         ";

	public Tile(int x, int y, int id) {
		super(x,y);
		renderDepth = ClientOverworldStage.TILE_DEPTH;
		this.id = id;
		tileset = new Tileset(FEResources.getTexture("terrain_tiles"), 16, 16);
		setTerrain(getTerrainFromID(id));
	}
	
	public void render(){
		tileset.render(x, y, id%25, id/25, renderDepth);
	}

	public Terrain getTerrain() {
		return terrain;
	}
	
	public static Terrain getTerrainFromID(int id) {
		char ch = terrainMap.charAt(id);
		Terrain t;
		if(ch == 'P') t = Terrain.PLAIN;
		else if(ch == 'W') t = Terrain.WALL;
		else if(ch == 'A') t = Terrain.PATH;
		else if(ch == 'S') t = Terrain.SEA;
		else if(ch == 'F') t = Terrain.FOREST;
		else if(ch == 'L') t = Terrain.FLOOR;
		else if(ch == 'K') t = Terrain.PEAK;
		else if(ch == 'M') t = Terrain.MOUNTAIN;
		else if(ch == 'T') t = Terrain.FORT;
		else t = Terrain.NONE;
		return t;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
}
