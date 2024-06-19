package com.mygdx.poupoule;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import static com.badlogic.gdx.Input.Keys.*;

public class TiledMapInputProcessor implements InputProcessor {
    private final Camera camera;
    private final TiledMap theMap;
    private final SimpleCoord playerCoord;
    OrthogonalTiledMapRenderer renderer;


    public TiledMapInputProcessor(Camera camera, TiledMap theMap, SimpleCoord coord, OrthogonalTiledMapRenderer renderer) {
        this.camera = camera;
        this.theMap = theMap;
        this.playerCoord = coord;
        this.renderer = renderer;
    }

    public boolean keyDown(int keycode) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) theMap.getLayers().get("collision");
        if (keycode == LEFT) {
            TiledMapTileLayer.Cell collision = collisionLayer.getCell(playerCoord.x - 1, playerCoord.y);
            if (collision == null) {
                playerCoord.x = playerCoord.x - 1;
                if (!renderer.getViewBounds().contains(0, playerCoord.y)) {
                    camera.translate(-1, 0, 0);
                }
            }
        } else if (keycode == RIGHT) {
            TiledMapTileLayer.Cell collision = collisionLayer.getCell(playerCoord.x + 1, playerCoord.y);
            if (collision == null) {
                playerCoord.x = playerCoord.x + 1;
                if (!renderer.getViewBounds().contains(30, playerCoord.y)) {
                    camera.translate(1, 0, 0);
                }
            }
        } else if (keycode == UP) {
            TiledMapTileLayer.Cell collision = collisionLayer.getCell(playerCoord.x, playerCoord.y + 1);

            if (collision == null) {
                playerCoord.y = playerCoord.y + 1;
                if (!renderer.getViewBounds().contains(playerCoord.x, 20)) {
                    camera.translate(0, 1, 0);
                }
            }
        } else if (keycode == DOWN) {
            TiledMapTileLayer.Cell collision = collisionLayer.getCell(playerCoord.x, playerCoord.y - 1);

            if (collision == null) {
                playerCoord.y = playerCoord.y - 1;
                if (!renderer.getViewBounds().contains(playerCoord.x, 0)) {
                    camera.translate(0, -1, 0);
                }
            }
        }
        return true;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved(int x, int y) {
        return false;
    }

    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
