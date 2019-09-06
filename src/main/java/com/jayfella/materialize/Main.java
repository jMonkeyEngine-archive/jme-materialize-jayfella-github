package com.jayfella.materialize;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {

    public static void main(String[] args) {

        Main app = new Main();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("My Awesome Game");
        app.setSettings(settings);

        app.start();

    }

    Main() {
        super(new FlyCamAppState(), new StatsAppState(),
                new MaterializerState());
    }

    private Material mat;

    @Override
    public void simpleInitApp() {

        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        rootNode.addLight(new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal()));

        mat = new Material(assetManager, "MatDefs/Materialize/MaterializePBR.j3md");
        mat.setTexture("BaseColorMap", assetManager.loadTexture("Textures/grid.png"));
        mat.setFloat("Roughness", 0.2f);
        mat.setFloat("Metallic", 0.001f);
        mat.setColor("EdgeColor", ColorRGBA.Pink.mult(0.7f));
        mat.setFloat("EdgeThickness", 0.03f);
        geom.setMaterial(mat);

        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

        rootNode.attachChild(geom);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(new BloomFilter());
        viewPort.addProcessor(fpp);
    }

    float time = 0;
    boolean add = true;

    @Override
    public void simpleUpdate(float tpf) {


        if (add) {
            time += (tpf * 0.2f);
        }
        else {
            time -= (tpf * 0.2f);
        }

        if (time >= 1) {
            add = false;
        }
        else if (time <= 0) {
            add = true;
        }


        mat.setFloat("EffectTime", time);

    }

}