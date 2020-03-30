/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.DarkGas;
import com.shatteredpixel.yasd.general.actors.blobs.SmokeScreen;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.MagicalSight;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.buffs.Shadows;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.actors.mobs.Bestiary;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Sheep;
import com.shatteredpixel.yasd.general.effects.particles.FlowParticle;
import com.shatteredpixel.yasd.general.effects.particles.WindParticle;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.Torch;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfIntuition;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.levels.features.Door;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.connection.BridgeRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.ConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.CrackedWallConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.MazeConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.NonHiddenMazeConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.PerimeterRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.PitConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.RingBridgeRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.RingTunnelRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.TunnelRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.WalkwayRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.AquariumRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.BurnedRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.CaveRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.CirclePitRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.FissureRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.GrassyGraveRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.HallwayRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.MinefieldRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.PillarsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.PlantsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.PlatformRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.RingRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.RuinsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SegmentedRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SewerPipeRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SkullsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StatuesRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StripedRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StudyRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SuspiciousChestRoom;
import com.shatteredpixel.yasd.general.levels.traps.Trap;
import com.shatteredpixel.yasd.general.mechanics.ShadowCaster;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.tiles.CustomTilemap;
import com.shatteredpixel.yasd.general.tiles.DungeonTileSheet;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static com.shatteredpixel.yasd.general.levels.Terrain.CHASM;
import static com.shatteredpixel.yasd.general.levels.Terrain.DEEP_WATER;
import static com.shatteredpixel.yasd.general.levels.Terrain.DOOR;
import static com.shatteredpixel.yasd.general.levels.Terrain.EMBERS;
import static com.shatteredpixel.yasd.general.levels.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.yasd.general.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.yasd.general.levels.Terrain.FURROWED_GRASS;
import static com.shatteredpixel.yasd.general.levels.Terrain.GRASS;
import static com.shatteredpixel.yasd.general.levels.Terrain.HIGH_GRASS;
import static com.shatteredpixel.yasd.general.levels.Terrain.WALL;
import static com.shatteredpixel.yasd.general.levels.Terrain.WALL_DECO;
import static com.shatteredpixel.yasd.general.levels.Terrain.WATER;

public abstract class Level implements Bundlable {
	
	public enum Feeling {
		NONE,
		CHASM,
		WATER,
		GRASS,
		DARK,
		EVIL,
		OPEN,
		EMBER,
		DANGER
	}

	protected int width;
	protected int height;
	protected int length;

	public boolean hasExit = true;
	public boolean hasEntrance = true;
	
	private static final float TIME_TO_RESPAWN = 50;

	public int version;

	public Terrain[] map;
	public boolean[] visited;
	public boolean[] mapped;
	public boolean[] discoverable;

	public int viewDistance = Dungeon.isChallenged( Challenges.DARKNESS ) ? 2 : 6;
	
	public boolean[] heroFOV;


	int minScaleFactor = 0;
	int maxScaleFactor = -1;
	//By default, scales with hero level and has max and min defined within the individual levels. -1 max gives no limit.
	public int getScaleFactor() {
		if (maxScaleFactor == -1) {
			return Math.max(minScaleFactor, Dungeon.hero.levelToScaleFactor());
		} else {
			return (int) GameMath.gate(minScaleFactor, Dungeon.hero.levelToScaleFactor(), maxScaleFactor);
		}
	}

	public ArrayList<Integer> getTileLocations(Terrain terrain) {
		ArrayList<Integer> locations = new ArrayList<>();
		for (int i = 0; i < map.length; i++) {
			if (map[i] == terrain) {
				locations.add(i);
			}
		}
		return locations;
	}

	public static Terrain[] basicMap(int size) {
		Terrain[] map = new Terrain[size];
		for (int i = 0; i < size; i++) {
			map[i] = EMPTY;
		}
		return map;
	}

	//NOTE: to avoid lag I recommend using passable(pos), losBlocking(pos), etc instead of passable()[pos], losBlocking()[pos], etc when possible.
	public boolean passable(int pos) {
		return map[pos].passable() & !avoid(pos);
	}

	public final boolean[] passable() {
		boolean[] passable = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			passable[i] = passable(i);
		}
		return passable;
	}

	public boolean losBlocking(int pos) {
		if (Blob.volumeAt(this, pos, DarkGas.class) > 0 || Blob.volumeAt(this, pos, SmokeScreen.class) > 0) {
			return true;
		}
		return map[pos].losBlocking();
	}

	public final boolean[] losBlocking() {
		boolean[] losBlocking = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			losBlocking[i] = losBlocking(i);
		}
		return losBlocking;
	}

	public boolean flammable(int pos) {
		return map[pos].flammable();
	}

	public final boolean[] flammable() {
		boolean[] flammable = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			flammable[i] = flammable(i);
		}
		return flammable;
	}

	public boolean secret(int pos) {
		if (traps.containsKey(pos) && !traps.get(pos).visible) {
			return true;
		}
		return map[pos].secret();
	}

	public final boolean[] secret() {
		boolean[] secret = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			secret[i] = secret(i);
		}
		return secret;
	}

	public boolean solid(int pos) {
		return map[pos].solid();
	}

	public final boolean[] solid() {
		boolean[] solid = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			solid[i] = solid(i);
		}
		return solid;
	}

	public boolean avoid(int pos) {
		Trap trap = trap(pos);
		if (trap != null && trap.active && trap.visible) {//I hope to get rid of Terrain.TRAP, Terrain.HIDDEN_TRAP, etc altogether.
			return true;
		} else {
			return map[pos].avoid();
		}
	}

	public final boolean[] avoid() {
		boolean[] avoid = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			avoid[i] = avoid(i);
		}
		return avoid;
	}

	public boolean liquid(int pos) {
		return map[pos].liquid();
	}

	public final boolean[] liquid() {
		boolean[] liquid = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			liquid[i] = liquid(i);
		}
		return liquid;
	}

	public boolean pit(int cell) {
		return map[cell].pit();
	}

	public final boolean[] pit() {
		boolean[] pit = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			pit[i] = pit(i);
		}
		return pit;
	}
	
	public Feeling feeling = Feeling.NONE;
	
	public int entrance;
	public int exit;

	//when a boss level has become locked.
	public boolean locked = false;
	
	public HashSet<Mob> mobs;
	public SparseArray<Heap> heaps;
	public HashMap<Class<? extends Blob>,Blob> blobs;
	public SparseArray<Plant> plants;
	public SparseArray<Trap> traps;
	public HashSet<CustomTilemap> customTiles;
	public HashSet<CustomTilemap> customWalls;
	
	protected ArrayList<Item> itemsToSpawn = new ArrayList<>();

	protected Group visuals;
	
	public int color1 = 0x004400;
	public int color2 = 0x88CC44;

	private static final String VERSION     = "version";
	private static final String WIDTH       = "width";
	private static final String HEIGHT      = "height";
	private static final String MAP			= "map";
	private static final String VISITED		= "visited";
	private static final String MAPPED		= "mapped";
	private static final String ENTRANCE	= "entrance";
	private static final String EXIT		= "exit";
	private static final String LOCKED      = "locked";
	private static final String HEAPS		= "heaps";
	private static final String PLANTS		= "plants";
	private static final String TRAPS       = "traps";
	private static final String CUSTOM_TILES= "customTiles";
	private static final String CUSTOM_WALLS= "customWalls";
	private static final String MOBS		= "mobs";
	private static final String BLOBS		= "blobs";
	private static final String FEELING		= "feeling";
	private static final String STATE		= "state";

	public void create() {

		Random.seed( Dungeon.seedCurDepth() );
		
		if (!Dungeon.bossLevel() && Dungeon.xPos == 0) {

			if (Dungeon.isChallenged(Challenges.NO_FOOD)){
				addItemToSpawn( new SmallRation() );
			} else {
				addItemToSpawn(Generator.random(Generator.Category.FOOD));
			}

			if (Dungeon.isChallenged(Challenges.DARKNESS)){
				addItemToSpawn( new Torch() );
			}

			if (Dungeon.souNeeded()) {
				addItemToSpawn( new ScrollOfUpgrade() );
				Dungeon.LimitedDrops.UPGRADE_SCROLLS.count++;
			}

			if (Dungeon.esNeeded()) {
				addItemToSpawn( new StoneOfEnchantment() );
				Dungeon.LimitedDrops.ENCHANT_STONE.count++;
			}

			//one scroll of transmutation is guaranteed to spawn somewhere on chapter 2-4
			int enchChapter = (int)((Dungeon.seed / 10) % 3) + 1;
			if ( Dungeon.yPos / Constants.CHAPTER_LENGTH == enchChapter &&
					Dungeon.seed % 4 + 1 == Dungeon.yPos % Constants.CHAPTER_LENGTH){
				addItemToSpawn( new StoneOfEnchantment() );
			}
			
			if ( Dungeon.yPos == ((Dungeon.seed % 3) + 1)){
				addItemToSpawn( new StoneOfIntuition() );
			}
			if (Dungeon.isChallenged(Challenges.COLLAPSING_FLOOR)) {
				addItemToSpawn( new PotionOfLevitation());
			}

			DriedRose rose = Dungeon.hero.belongings.getItem( DriedRose.class );
			if (rose != null && rose.isIdentified() && !rose.cursed){
				//aim to drop 1 petal every 2 floors
				int petalsNeeded = (int) Math.ceil((float)((Dungeon.yPos / 2) - rose.droppedPetals) / 3);

				for (int i=1; i <= petalsNeeded; i++) {
					//the player may miss a single petal and still max their rose.
					if (rose.droppedPetals < 11) {
						addItemToSpawn(new DriedRose.Petal());
						rose.droppedPetals++;
					}
				}
			}
			
			if (Dungeon.yPos > 1) {
				switch (Random.Int( 10 )) {
					case 0:
						if (!Dungeon.bossLevel(Dungeon.yPos + 1)) {
							feeling = Feeling.CHASM;
						}
						break;
					case 1:
						feeling = Feeling.WATER;
						break;
					case 2:
						feeling = Feeling.GRASS;
						break;
					case 3:
						feeling = Feeling.DARK;
						addItemToSpawn(new Torch());
						viewDistance = Math.round(viewDistance / 2f);
						break;
					case 4:
						feeling = Feeling.EVIL;
						break;
					case 5:
						feeling = Feeling.OPEN;
						break;
					case 6:
						feeling = Feeling.EMBER;
						break;
					case 7:
						feeling = Feeling.DANGER;
						break;
				}
			}
		}
		
		do {
			width = height = length = 0;

			mobs = new HashSet<>();
			heaps = new SparseArray<>();
			blobs = new HashMap<>();
			plants = new SparseArray<>();
			traps = new SparseArray<>();
			customTiles = new HashSet<>();
			customWalls = new HashSet<>();
			
		} while (!build());
		
		buildFlagMaps();
		cleanWalls();
		
		createMobs();
		createItems();

		Random.seed();
	}
	
	public void setSize(int w, int h){
		
		width = w;
		height = h;
		length = w * h;
		
		map = new Terrain[length];
		Terrain terrain = WALL;
		if (feeling == Feeling.CHASM) {
			terrain = CHASM;
		} else if (feeling == Feeling.OPEN) {
			terrain = EMPTY;
		}
		Arrays.fill( map, terrain );
		
		visited     = new boolean[length];
		mapped      = new boolean[length];
		
		heroFOV     = new boolean[length];
		
		//passable	= new boolean[length];
		//losBlocking	= new boolean[length];
		//flammable	= new boolean[length];
		//secret		= new boolean[length];
		//solid		= new boolean[length];
		//avoid		= new boolean[length];
		//water		= new boolean[length];
		//pit			= new boolean[length];
		
		PathFinder.setMapSize(w, h);
	}
	
	public void reset() {
		
		for (Mob mob : mobs.toArray( new Mob[0] )) {
			if (!mob.reset()) {
				mobs.remove( mob );
			}
		}
		createMobs();
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {

		version = bundle.getInt( VERSION );
		
		//saves from before 0.6.5c are not supported
		if (version < MainGame.v0_6_5c){
			throw new RuntimeException("old save");
		}

		setSize( bundle.getInt(WIDTH), bundle.getInt(HEIGHT));
		
		mobs = new HashSet<>();
		heaps = new SparseArray<>();
		blobs = new HashMap<>();
		plants = new SparseArray<>();
		traps = new SparseArray<>();
		customTiles = new HashSet<>();
		customWalls = new HashSet<>();
		
		map		= (Terrain[]) bundle.getEnumArray( MAP, Terrain.class );

		visited	= bundle.getBooleanArray( VISITED );
		mapped	= bundle.getBooleanArray( MAPPED );
		
		entrance	= bundle.getInt( ENTRANCE );
		exit		= bundle.getInt( EXIT );

		locked      = bundle.getBoolean( LOCKED );
		
		Collection<Bundlable> collection = bundle.getCollection( HEAPS );
		for (Bundlable h : collection) {
			Heap heap = (Heap)h;
			if (!heap.isEmpty())
				heaps.put( heap.pos, heap );
		}
		
		collection = bundle.getCollection( PLANTS );
		for (Bundlable p : collection) {
			Plant plant = (Plant)p;
			plants.put( plant.pos, plant );
		}

		collection = bundle.getCollection( TRAPS );
		for (Bundlable p : collection) {
			Trap trap = (Trap)p;
			traps.put( trap.pos, trap );
		}

		collection = bundle.getCollection( CUSTOM_TILES );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customTiles.add(vis);
		}

		collection = bundle.getCollection( CUSTOM_WALLS );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customWalls.add(vis);
		}
		
		collection = bundle.getCollection( MOBS );
		for (Bundlable m : collection) {
			Mob mob = (Mob)m;
			if (mob != null) {
				mobs.add( mob );
			}
		}
		
		collection = bundle.getCollection( BLOBS );
		for (Bundlable b : collection) {
			Blob blob = (Blob)b;
			blobs.put( blob.getClass(), blob );
		}

		feeling = bundle.getEnum( FEELING, Feeling.class );
		if (feeling == Feeling.DARK)
			viewDistance = Math.round(viewDistance/2f);

		//if (bundle.contains( "mobs_to_spawn" )) {
		//	for (Class<? extends Mob> mob : bundle.getClassArray("mobs_to_spawn")) {
		//		if (mob != null) mobsToSpawn.add(mob);
		//	}
		//}
		
		buildFlagMaps();
		cleanWalls();
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( VERSION, Game.versionCode );
		bundle.put( WIDTH, width );
		bundle.put( HEIGHT, height );
		bundle.put( MAP, map );
		bundle.put( VISITED, visited );
		bundle.put( MAPPED, mapped );
		bundle.put( ENTRANCE, entrance );
		bundle.put( EXIT, exit );
		bundle.put( LOCKED, locked );
		bundle.put( HEAPS, heaps.valueList() );
		bundle.put( PLANTS, plants.valueList() );
		bundle.put( TRAPS, traps.valueList() );
		bundle.put( CUSTOM_TILES, customTiles );
		bundle.put( CUSTOM_WALLS, customWalls );
		bundle.put( MOBS, mobs );
		bundle.put( BLOBS, blobs.values() );
		bundle.put( FEELING, feeling );
		//bundle.put( "mobs_to_spawn", mobsToSpawn.toArray(new Class[0]));
	}

	public Terrain tunnelTile() {
		return (feeling == Feeling.CHASM || feeling == Feeling.OPEN) ? EMPTY_SP : EMPTY;
	}

	public Terrain grassTile( boolean tall ) {
		if (feeling == Level.Feeling.EMBER) {
			return tall ? FURROWED_GRASS : EMBERS;
		} else {
			return tall ? HIGH_GRASS : GRASS;
		}
	}

	public Terrain waterTile() {
		if (feeling == Feeling.EMBER) {
			return grassTile(false);
		} else {
			return WATER;
		}
	}

	public Terrain swapWaterAlts(int pos) {
		int above = pos + PathFinder.CIRCLE4[0];
		int right = pos + PathFinder.CIRCLE4[1];
		int below = pos + PathFinder.CIRCLE4[2];
		int left = pos + PathFinder.CIRCLE4[3];
		try {
			if (map[pos] == WATER &&
					DungeonTileSheet.deepWaterStitchable(map[above]) &
							DungeonTileSheet.deepWaterStitchable(map[below]) &
							DungeonTileSheet.deepWaterStitchable(map[left]) &
							DungeonTileSheet.deepWaterStitchable(map[right])) {
				return DEEP_WATER;

			}
		} catch (IndexOutOfBoundsException ignored) {}
		return map[pos];
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int length() {
		return length;
	}
	
	public String tilesTex() {
		return null;
	}
	
	public String waterTex() {
		return null;
	}

	public String loadImg() {
		return null;
	}
	
	abstract protected boolean build();
	
	//private ArrayList<Class<?extends Mob>> mobsToSpawn = new ArrayList<>();

	public Class<?>[] mobClasses() {
		return new Class[]{Wraith.class};
	}

	public float[] mobChances() {
		return new float[]{1};
	}
	
	public Mob createMob() {
		if (mobChances().length != mobClasses().length) {
			throw new AssertionError("Mob classes must be equal in length to mob chances!");
		}
		int type = Random.chances(mobChances());
		Class<? extends Mob> mob = (Class<? extends Mob>) mobClasses()[type];
		mob = Bestiary.swapMobAlt(mob);
		return Mob.create(mob, this);
	}

	protected Class<?>[] connectionRoomClasses(){
		return new Class<?>[]{
				TunnelRoom.class,
				BridgeRoom.class,

				PerimeterRoom.class,
				WalkwayRoom.class,

				RingTunnelRoom.class,
				RingBridgeRoom.class,
				NonHiddenMazeConnectionRoom.class};
	}

	protected float[] connectionRoomChances() {
		return new float[]{
				20,
				1,
				0,
				2,
				2,
				1,
				1};
	}

	public ConnectionRoom randomConnectionRoom() {
		if (connectionRoomChances().length != connectionRoomClasses().length) {
			throw new AssertionError("Room classes must be equal in length to room chances!");
		}
		int type = Random.chances(connectionRoomChances());
		Class<? extends ConnectionRoom> room = (Class<? extends ConnectionRoom>) connectionRoomClasses()[type];
		return Reflection.newInstance(room);
	}

	protected Class<?>[] secretConnectionRoomClasses(){
		return new Class<?>[]{
				CrackedWallConnectionRoom.class,
				MazeConnectionRoom.class,
				PitConnectionRoom.class};
	}

	protected float[] secretConnectionRoomChances() {
		return new float[]{
				1,
				3,
				4};
	}

	public ConnectionRoom randomSecretConnectionRoom() {
		if (secretConnectionRoomChances().length != secretConnectionRoomClasses().length) {
			throw new AssertionError("Room classes must be equal in length to room chances!");
		}
		int type = Random.chances(secretConnectionRoomChances());
		Class<? extends ConnectionRoom> room = (Class<? extends ConnectionRoom>) secretConnectionRoomClasses()[type];
		return Reflection.newInstance(room);
	}

	protected Class<?>[] standardRoomClasses(){
		return new Class<?>[]{
				EmptyRoom.class,

				SewerPipeRoom.class,
				RingRoom.class,

				SegmentedRoom.class,
				StatuesRoom.class,

				CaveRoom.class,
				CirclePitRoom.class,

				HallwayRoom.class,
				PillarsRoom.class,

				RuinsRoom.class,
				SkullsRoom.class,


				PlantsRoom.class,
				AquariumRoom.class,
				PlatformRoom.class,
				BurnedRoom.class,
				FissureRoom.class,
				GrassyGraveRoom.class,
				StripedRoom.class,
				StudyRoom.class,
				SuspiciousChestRoom.class,
				MinefieldRoom.class};
	}

	protected float[] standardRoomChances() {
		return new float[]{20,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	}

	public StandardRoom randomStandardRoom() {
		if (standardRoomClasses().length != standardRoomChances().length) {
			throw new AssertionError("Room classes must be equal in length to room chances!");
		}
		int type = Random.chances(standardRoomChances());
		Class<? extends StandardRoom> room = (Class<? extends StandardRoom>) standardRoomClasses()[type];
		return Reflection.newInstance(room);
	}

	public ArrayList<Integer> getPassableCellsList() {

		ArrayList<Integer> result = new ArrayList<>();

		for( int cell = 0 ; cell < length() ; cell++ ){

			if( !solid(cell) && passable(cell) && Actor.findChar(cell) == null ) {
				result.add( cell );
			}
		}

		return result;
	}

	abstract protected void createMobs();

	abstract protected void createItems();

	public void seal(){
		if (!locked) {
			locked = true;
			Buff.affect(Dungeon.hero, LockedFloor.class);
		}
	}

	public void unseal(){
		if (locked) {
			locked = false;
		}
	}

	public Group addVisuals() {
		if (visuals == null || visuals.parent == null){
			visuals = new Group();
		} else {
			visuals.clear();
			visuals.camera = null;
		}
		for (int i=0; i < length(); i++) {
			if (pit()[i]) {
				visuals.add( new WindParticle.Wind( i ) );
				if (i >= width() && liquid()[i-width()]) {
					visuals.add( new FlowParticle.Flow( i - width() ) );
				}
			} else if ( map[i] == EMBERS ) {
				visuals.add( new CityLevel.Smoke( i ) );
			}
		}
		return visuals;
	}
	
	public int nMobs() {
		return 0;
	}

	public Mob findMob( int pos ){
		for (Mob mob : mobs){
			if (mob.pos == pos){
				return mob;
			}
		}
		return null;
	}
	
	public Actor respawner() {
		return new Actor() {

			{
				actPriority = BUFF_PRIO; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				int count = 0;
				for (Mob mob : mobs.toArray(new Mob[0])){
					if (mob.alignment == Char.Alignment.ENEMY) count++;
				}
				
				if (count < nMobs()) {

					Mob mob = createMob();
					mob.state = mob.WANDERING;
					mob.pos = randomRespawnCell();
					if (Dungeon.hero.isAlive() && mob.pos != -1 && distance(Dungeon.hero.pos, mob.pos) >= 4) {
						GameScene.add( mob );
						if (Statistics.amuletObtained) {
							mob.beckon( Dungeon.hero.pos );
						}
					}
				}
				spend(respawnTime());
				return true;
			}
		};
	}
	
	public float respawnTime(){
		if (Statistics.amuletObtained){
			return TIME_TO_RESPAWN/2f;
		} else if (Dungeon.level.feeling == Feeling.DARK){
			return 2*TIME_TO_RESPAWN/3f;
		} else {
			return TIME_TO_RESPAWN;
		}
	}
	
	public int randomRespawnCell() {
		int cell;
		do {
			cell = Random.Int( length() );
		} while ((Dungeon.level == this && heroFOV[cell])
				|| !map[cell].passable
				|| Actor.findChar( cell ) != null);
		return cell;
	}
	
	public int randomDestination() {
		int cell;
		do {
			cell = Random.Int( length() );
		} while (!passable(cell));
		return cell;
	}
	
	public void addItemToSpawn( Item item ) {
		if (item != null) {
			itemsToSpawn.add( item );
		}
	}

	public Item findPrizeItem(){ return findPrizeItem(null); }

	public Item findPrizeItem(Class<?extends Item> match){
		if (itemsToSpawn.size() == 0)
			return null;

		if (match == null){
			Item item = Random.element(itemsToSpawn);
			itemsToSpawn.remove(item);
			return item;
		}

		for (Item item : itemsToSpawn){
			if (match.isInstance(item)){
				itemsToSpawn.remove( item );
				return item;
			}
		}

		return null;
	}

	public void buildFlagMaps() {
		
		int lastRow = length() - width();
		for (int i=0; i < width(); i++) {
			map[i] = WALL;
			map[lastRow + i] = WALL;
		}
		for (int i=width(); i < lastRow; i += width()) {
			map[i] = WALL;
			map[i + width()-1] = WALL;
		}
	}

	public void destroy( int pos ) {
		set( pos, EMBERS );
	}

	protected void cleanWalls() {
		discoverable = new boolean[length()];

		for (int i=0; i < length(); i++) {
			
			boolean d = false;
			
			for (int j=0; j < PathFinder.NEIGHBOURS9.length; j++) {
				int n = i + PathFinder.NEIGHBOURS9[j];
				if (n >= 0 && n < length() && map[n] != WALL && map[n] != WALL_DECO) {
					d = true;
					break;
				}
			}
			
			discoverable[i] = d;
		}
	}

	public boolean deepWater(int pos) {
		return map[pos] == DEEP_WATER;
	}

	public int XY(int x, int y) {
		return x + y * width();
	}

	public int[] posToXY(int pos) {
		int[] coords = new int[2];
		coords[0] = pos%height;
		coords[1] = pos/height;
		return coords;
	}

	public void set( int cell, Terrain terrain ){
		Level level = this;
		Painter.set( level, cell, terrain );

		if (terrain != EMPTY){
			level.traps.remove( cell );
		}
	}
	
	public Heap drop( Item item, int cell ) {

		if (item == null || Challenges.isItemBlocked(item)){

			//create a dummy heap, give it a dummy sprite, don't add it to the game, and return it.
			//effectively nullifies whatever the logic calling this wants to do, including dropping items.
			Heap heap = new Heap();
			ItemSprite sprite = heap.sprite = new ItemSprite();
			sprite.link(heap);
			return heap;

		}
		
		Heap heap = heaps.get( cell );
		if (heap == null) {
			
			heap = new Heap();
			heap.seen = Dungeon.level == this && heroFOV[cell];
			heap.pos = cell;
			heap.drop(item);
			if (map[cell] == CHASM || (Dungeon.level != null && pit()[cell])) {
				Dungeon.dropToChasm( item );
				GameScene.discard( heap );
			} else {
				heaps.put( cell, heap );
				GameScene.add( heap );
			}
			
		} else if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST) {
			
			int n;
			do {
				n = cell + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!passable(n) && !avoid(n));
			return drop( item, n );
			
		} else {
			heap.drop(item);
		}
		
		if (Dungeon.level != null) {
			pressCell( cell );
		}
		
		return heap;
	}
	
	public Plant plant( Plant.Seed seed, int pos ) {
		
		if (Dungeon.isChallenged(Challenges.NO_HERBALISM)){
			return null;
		}

		Plant plant = plants.get( pos );
		if (plant != null) {
			plant.wither();
		}

		if (map[pos] == HIGH_GRASS ||
				map[pos] == FURROWED_GRASS ||
				map[pos] == EMPTY ||
				map[pos] == EMBERS ||
				map[pos] == EMPTY_DECO) {
			set(pos, GRASS);
			GameScene.updateMap(pos);
		}
		
		plant = seed.couch( pos, this );
		plants.put( pos, plant );
		
		GameScene.plantSeed( pos );
		
		return plant;
	}
	
	public void uproot( int pos ) {
		plants.remove(pos);
		GameScene.updateMap( pos );
	}

	public Trap trap(int cell) {
		return traps.get(cell);
	}

	public Trap setTrap( Trap trap, int pos ){
		Trap existingTrap = traps.get(pos);
		if (existingTrap != null){
			traps.remove( pos );
		}
		trap.set( pos );
		traps.put( pos, trap );
		GameScene.updateMap( pos );
		return trap;
	}

	public void disarmTrap( int pos ) {
		//set(pos, INACTIVE_TRAP);
		GameScene.updateMap(pos);
	}

	public void discover( int cell ) {
		set( cell, map[cell].discover() );
		Trap trap = traps.get( cell );
		if (trap != null)
			trap.reveal();
		GameScene.updateMap( cell );
	}
	
	public int fallCell( boolean fallIntoPit ) {
		int result;
		do {
			result = randomRespawnCell();
		} while (traps.get(result) != null
				|| findMob(result) != null
				|| heaps.get(result) != null);
		return result;
	}
	
	public void occupyCell( Char ch ){
		if (!ch.isFlying()){
			
			if (pit()[ch.pos]){
				if (ch == Dungeon.hero) {
					Chasm.heroFall(ch.pos);
				} else if (ch instanceof Mob) {
					Chasm.mobFall( (Mob)ch );
				}
				return;
			}
			
			//characters which are not the hero or a sheep 'soft' press cells
			pressCell( ch.pos, ch instanceof Hero || ch instanceof Sheep);
		} else {
			if (map[ch.pos] == DOOR){
				Door.enter( ch.pos );
			}
		}
	}
	
	//public method for forcing the hard press of a cell. e.g. when an item lands on it
	public final void pressCell( int cell ){
		pressCell( cell, true );
	}
	
	//a 'soft' press ignores hidden traps
	//a 'hard' press triggers all things
	protected final void pressCell(int cell, boolean hard) {

		map[cell].press(cell, hard);//See Terrain.press()
		Trap trap = trap(cell);
		/*switch (map[cell]) {

			case SECRET_TRAP:
				if (hard) {
					trap = traps.get(cell);
					GLog.i(Messages.get(Level.class, "hidden_trap", trap.name));
				}
				break;

			case TRAP:
				trap = traps.get(cell);
				break;

			case HIGH_GRASS:
			case FURROWED_GRASS:
				HighGrass.trample(this, cell);
				break;

			case WELL:
				WellWater.affectCell(cell);
				break;

			case DOOR:
				Door.enter(cell);
				break;
		}*/

		if (trap != null && trap.active && (hard || trap.visible)) {

			if (!trap.visible) GLog.i(Messages.get(Level.class, "hidden_trap", trap.name));
			
			TimekeepersHourglass.timeFreeze timeFreeze =
					Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
			
			Swiftthistle.TimeBubble bubble =
					Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
			
			if (bubble != null){
				
				Sample.INSTANCE.play(Assets.SND_TRAP);
				
				discover(cell);
				
				bubble.setDelayedPress(cell);
				
			} else if (timeFreeze != null){
				
				Sample.INSTANCE.play(Assets.SND_TRAP);
				
				discover(cell);
				
				timeFreeze.setDelayedPress(cell);
				
			} else {

				if (Dungeon.hero.pos == cell) {
					Dungeon.hero.interrupt();
				}

				trap.trigger();

			}
		}
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			plant.trigger();
		}
	}
	
	public void updateFieldOfView( Char c, boolean[] fieldOfView ) {

		int cx = c.pos % width();
		int cy = c.pos / width();
		
		boolean sighted = c.buff( Blindness.class ) == null && c.buff( Shadows.class ) == null
						&& c.buff( TimekeepersHourglass.timeStasis.class ) == null && c.isAlive();
		if (sighted) {
			boolean[] blocking;
			
			if (c instanceof Hero && ((Hero) c).subClass == HeroSubClass.WARDEN) {
				blocking = Dungeon.level.losBlocking().clone();
				for (int i = 0; i < blocking.length; i++){
					if (blocking[i] && (Dungeon.level.map[i] == HIGH_GRASS || Dungeon.level.map[i] == FURROWED_GRASS)){
						blocking[i] = false;
					}
				}
			} else {
				blocking = Dungeon.level.losBlocking();
			}
			
			int viewDist = c.viewDistance;
			if (c instanceof Hero && ((Hero) c).subClass == HeroSubClass.SNIPER) viewDist *= 1.5f;
			
			ShadowCaster.castShadow( cx, cy, fieldOfView, blocking, viewDist );
		} else {
			BArray.setFalse(fieldOfView);
		}
		
		int sense = 1;
		//Currently only the hero can get mind vision
		if (c.isAlive() && c == Dungeon.hero) {
			for (Buff b : c.buffs( MindVision.class )) {
				sense = Math.max( ((MindVision)b).distance, sense );
			}
			if (c.buff(MagicalSight.class) != null){
				sense = 8;
			}
			if (((Hero)c).subClass == HeroSubClass.SNIPER){
				sense *= 1.5f;
			}
		}
		
		//uses rounding
		if (!sighted || sense > 1) {
			
			int[][] rounding = ShadowCaster.rounding;
			
			int left, right;
			int pos;
			for (int y = Math.max(0, cy - sense); y <= Math.min(height()-1, cy + sense); y++) {
				if (rounding[sense][Math.abs(cy - y)] < Math.abs(cy - y)) {
					left = cx - rounding[sense][Math.abs(cy - y)];
				} else {
					left = sense;
					while (rounding[sense][left] < rounding[sense][Math.abs(cy - y)]){
						left--;
					}
					left = cx - left;
				}
				right = Math.min(width()-1, cx + cx - left);
				left = Math.max(0, left);
				pos = left + y * width();
				System.arraycopy(discoverable, pos, fieldOfView, pos, right - left + 1);
			}
		}

		//Currently only the hero can get mind vision or awareness
		if (c.isAlive() && c == Dungeon.hero) {
			Dungeon.hero.mindVisionEnemies.clear();
			if (c.buff( MindVision.class ) != null) {
				for (Mob mob : mobs) {
					int p = mob.pos;

					if (!fieldOfView[p]){
						Dungeon.hero.mindVisionEnemies.add(mob);
					}

				}
			} /*else if (((Hero)c).heroClass == HeroClass.HUNTRESS) {
				for (Mob mob : mobs) {
					int p = mob.pos;
					if (distance( c.pos, p) == 2) {

						if (!fieldOfView[p]){
							Dungeon.hero.mindVisionEnemies.add(mob);
						}
					}
				}
			}*/

			for (Mob mob : mobs) {
				if (Dungeon.hero.notice(mob) && !fieldOfView[mob.pos]) {
					//GLog.i(Messages.get(Hero.class, "mob_nearby", mob.name));
					Dungeon.hero.mindVisionEnemies.add(mob);
				}
			}

			
			for (Mob m : Dungeon.hero.mindVisionEnemies) {
				for (int i : PathFinder.NEIGHBOURS9) {
					fieldOfView[m.pos + i] = true;
				}
			}
			
			if (c.buff( Awareness.class ) != null) {
				for (Heap heap : heaps.valueList()) {
					int p = heap.pos;
					for (int i : PathFinder.NEIGHBOURS9)
						fieldOfView[p+i] = true;
				}
			}

			for (Mob ward : mobs){
				if (ward instanceof WandOfWarding.Ward){
					if (ward.fieldOfView == null || ward.fieldOfView.length != length()){
						ward.fieldOfView = new boolean[length()];
						Dungeon.level.updateFieldOfView( ward, ward.fieldOfView );
					}
					for (Mob m : mobs){
						if (ward.fieldOfView[m.pos] && !fieldOfView[m.pos] &&
								!Dungeon.hero.mindVisionEnemies.contains(m)){
							Dungeon.hero.mindVisionEnemies.add(m);
						}
					}
					BArray.or(fieldOfView, ward.fieldOfView, fieldOfView);
				}
			}
		}

		if (c == Dungeon.hero) {
			for (Heap heap : heaps.valueList())
				if (!heap.seen && fieldOfView[heap.pos])
					heap.seen = true;
		}

	}
	
	public int distance( int a, int b ) {
		int ax = a % width();
		int ay = a / width();
		int bx = b % width();
		int by = b / width();
		return Math.max( Math.abs( ax - bx ), Math.abs( ay - by ) );
	}
	
	public boolean adjacent( int a, int b ) {
		return distance( a, b ) == 1;
	}
	
	//uses pythagorean theorum for true distance, as if there was no movement grid
	public float trueDistance(int a, int b){
		int ax = a % width();
		int ay = a / width();
		int bx = b % width();
		int by = b / width();
		return (float)Math.sqrt(Math.pow(Math.abs( ax - bx ), 2) + Math.pow(Math.abs( ay - by ), 2));
	}

	//returns true if the input is a valid tile within the level
	public boolean insideMap( int tile ){
				//top and bottom row and beyond
		return !((tile < width || tile >= length - width) ||
				//left and right column
				(tile % width == 0 || tile % width == width-1));
	}

	public Point cellToPoint( int cell ){
		return new Point(cell % width(), cell / width());
	}

	public int pointToCell( Point p ){
		return p.x + p.y*width();
	}
	
	public String tileName( Terrain tile ) {
		
		switch (tile) {
			case CHASM:
				return Messages.get(Level.class, "chasm_name");
			case EMPTY:
			case EMPTY_SP:
			case EMPTY_DECO:
			//case SECRET_TRAP:
			//	return Messages.get(Level.class, "floor_name");
			case GRASS:
				return Messages.get(Level.class, "grass_name");
			case WATER:
				return Messages.get(Level.class, "water_name");
			case WALL:
			case WALL_DECO:
			case SECRET_DOOR:
				return Messages.get(Level.class, "wall_name");
			case DOOR:
				return Messages.get(Level.class, "closed_door_name");
			case OPEN_DOOR:
				return Messages.get(Level.class, "open_door_name");
			case ENTRANCE:
				return Messages.get(Level.class, "entrace_name");
			case EXIT:
				return Messages.get(Level.class, "exit_name");
			case EMBERS:
				return Messages.get(Level.class, "embers_name");
			case FURROWED_GRASS:
				return Messages.get(Level.class, "furrowed_grass_name");
			case LOCKED_DOOR:
				return Messages.get(Level.class, "locked_door_name");
			case PEDESTAL:
				return Messages.get(Level.class, "pedestal_name");
			case BARRICADE:
				return Messages.get(Level.class, "barricade_name");
			case HIGH_GRASS:
				return Messages.get(Level.class, "high_grass_name");
			case LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_name");
			case UNLOCKED_EXIT:
				return Messages.get(Level.class, "unlocked_exit_name");
			case SIGN:
				return Messages.get(Level.class, "sign_name");
			case WELL:
				return Messages.get(Level.class, "well_name");
			case EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_name");
			case STATUE:
			case STATUE_SP:
				return Messages.get(Level.class, "statue_name");
			//case INACTIVE_TRAP:
		//		return Messages.get(Level.class, "inactive_trap_name");
			case BOOKSHELF:
				return Messages.get(Level.class, "bookshelf_name");
			case ALCHEMY:
				return Messages.get(Level.class, "alchemy_name");
			default:
				return Messages.get(Level.class, "default_name");
		}
	}
	
	public String tileDesc( Terrain tile ) {
		
		switch (tile) {
			case CHASM:
				return Messages.get(Level.class, "chasm_desc");
			case WATER:
				if (Dungeon.hero.morale > Dungeon.hero.MAX_MORALE/2f) {
					return Messages.get(Level.class, "water_desc");
				} else {
					return Messages.get(Level.class, "water_desc_low_morale");
				}

			case ENTRANCE:
				return Messages.get(Level.class, "entrance_desc");
			case EXIT:
			case UNLOCKED_EXIT:
				return Messages.get(Level.class, "exit_desc");
			case EMBERS:
				return Messages.get(Level.class, "embers_desc");
			case HIGH_GRASS:
			case FURROWED_GRASS:
				return Messages.get(Level.class, "high_grass_desc");
			case LOCKED_DOOR:
				return Messages.get(Level.class, "locked_door_desc");
			case LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_desc");
			case BARRICADE:
				return Messages.get(Level.class, "barricade_desc");
			case SIGN:
				return Messages.get(Level.class, "sign_desc");
			//case INACTIVE_TRAP:
			//	return Messages.get(Level.class, "inactive_trap_desc");
			case STATUE:
			case STATUE_SP:
				return Messages.get(Level.class, "statue_desc");
			case ALCHEMY:
				return Messages.get(Level.class, "alchemy_desc");
			case EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_desc");
			default:
				return "";
		}
	}
}
