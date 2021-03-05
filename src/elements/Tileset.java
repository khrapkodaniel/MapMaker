package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import tile.Tile;
import ui.Ptileset;

// the tile set that stores the sprites and whatnot.
public final class Tileset {
	private final int tilesetWidth, tilesetHeight;
	
	private final int tilesAlongXAxis, tilesAlongYAxis;
	
	// the tiles that are in the tile set.
	private final BufferedImage[] tiles;
	
	// the tile index that is selected.
	private int selectedTileIndex;
	private int selectedTileX, selectedTileY;
	
	private Point tilesetLocation = new Point();
	
	public Tileset(BufferedImage tileset) {
		this.tilesetWidth = tileset.getWidth();
		this.tilesetHeight = tileset.getHeight();
		
		// calculates the amount of tiles in x and y axes.
		this.tilesAlongXAxis = this.tilesetWidth / Tile.getWidth();
		this.tilesAlongYAxis = this.tilesetHeight / Tile.getHeight();
		
		// the size is the width * height.
		this.tiles = new BufferedImage[this.tilesAlongXAxis * this.tilesAlongYAxis];
		
		// cuts up the tileset and stores it into the array.
		int counter = 0;
		// reads horizontally.
		for (int y = 0; y < this.tilesetHeight; y += Tile.getHeight())
			for (int x = 0; x < this.tilesetWidth; x += Tile.getWidth())
				this.tiles[ counter++ ] = tileset.getSubimage(x, y, Tile.getWidth(), Tile.getHeight());
	}
	
	public void setSelectedTileIndex(int mouseX, int mouseY) {
		if (mouseX > this.tilesetWidth || mouseX < 0 || mouseY > this.tilesetHeight || mouseY < Tile.getHeight() * 2)
			return;
		
		int x = (mouseX - tilesetLocation.x) / Tile.getWidth();
		int y = (mouseY - tilesetLocation.y) / Tile.getHeight() - 2;
		
		this.selectedTileIndex = y * this.tilesAlongXAxis + x;
	}
	
	public BufferedImage getTile(int index) {
		return this.tiles[ index ];
	}
	
	public Point getTilesetLocation() {
		return this.tilesetLocation;
	}
	
	public void setTilesetLocation(Point newLocation) {
		this.tilesetLocation = newLocation;
	}
	
	private void renderSelectedTile(Graphics graphics) {
		int x1 = 0, y1 = 0;
		
		int w = Ptileset.getW();
		int h = Tile.getHeight() * 2;
		
		// draws background.
		graphics.setColor(Color.BLACK);
		graphics.fillRect(x1, y1, w, h);
		
		x1 = (Ptileset.getW() - Tile.getWidth()) / 2;
		y1 = Tile.getHeight() / 2;
		
		// draws the selected tile in the corner.
		graphics.drawImage(this.getTile(this.selectedTileIndex), x1, y1, null);
	}
	
	public void render(Graphics graphics) {
		int counter = 0;
		while (counter < this.tiles.length) {
			// calculates the x and y of each tile using only the tile index.
			int x = counter % this.tilesAlongXAxis * Tile.getWidth();
			int y = counter / this.tilesAlongXAxis * Tile.getHeight();
			
			// draws the individual tile to the panel with the offsets.
			graphics.drawImage(this.getTile(counter), x + tilesetLocation.x, y + (Tile.getHeight() * 2) + tilesetLocation.y, null);
			
			// highlights the selected tile.
			if (counter == this.selectedTileIndex) {
				graphics.setColor(Color.RED);
				graphics.drawRect(x + tilesetLocation.x, y + (Tile.getHeight() * 2) + tilesetLocation.y, Tile.getWidth() - 1, Tile.getHeight() - 1);}
			
			counter++;
		}
		
		// draws highlight
		graphics.setColor(Color.RED);
		graphics.drawRect(this.selectedTileX + this.tilesetLocation.x, this.selectedTileY + this.tilesetLocation.y, Tile.getWidth() - 1, Tile.getHeight() - 1);
		
		// draws border around tileset.
		graphics.setColor(Color.BLACK);
		graphics.drawRect(tilesetLocation.x, tilesetLocation.y + Tile.getHeight() * 2, this.tilesetWidth, this.tilesetHeight);
		
		this.renderSelectedTile(graphics);
	}
}
