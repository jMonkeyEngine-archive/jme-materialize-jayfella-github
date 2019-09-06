package com.jayfella.materialize;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.material.RenderState;

import java.util.ArrayList;
import java.util.List;

public class MaterializerState extends BaseAppState {

    private static final String EFFECT_TIME = "EffectTime";
    private static final String MATERIALIZING = "Materializing";

    private final List<MaterializeTimer> materials = new ArrayList<>();

    public MaterializerState() {

    }

    @Override protected void initialize(Application app) { }
    @Override protected void cleanup(Application app) { }
    @Override protected void onEnable() { }
    @Override protected void onDisable() { }

    public void materialize(Material material, boolean in, MaterializerCallBack callBack) {
        materials.add(new MaterializeTimer(material, in, callBack));
    }

    /**
     * Materialize a material in.
     * @param material the material to materialize.
     */
    public void materializeIn(Material material) {
        materials.add(new MaterializeTimer(material, true, null));
    }

    /**
     * Materializes a material in.
     * @param material the material to materialize.
     * @param speed    speed modifier between 0 and 1. 0.1 = 10 seconds, 1 = 1 second.
     */
    public void materializeIn(Material material, float speed) {
        materials.add(new MaterializeTimer(material, true, null, speed));
    }

    /**
     * Materialize a material in.
     * @param material the material to materialize.
     * @param callBack a callback when the materializer has finished.
     * @param speed    speed modifier between 0 and 1. 0.1 = 10 seconds, 1 = 1 second.
     */
    public void materializeIn(Material material, MaterializerCallBack callBack, float speed) {
        materials.add(new MaterializeTimer(material, true, callBack, speed));
    }

    /**
     * Materializes a material out.
     * @param material the material to materialize.
     */
    public void materializeOut(Material material) {
        materials.add(new MaterializeTimer(material, false, null));
    }

    /**
     * Materializes a material out.
     * @param material the material to materialize.
     * @param speed    speed modifier between 0 and 1. 0.1 = 10 seconds, 1 = 1 second.
     */
    public void materializeOut(Material material, float speed) {
        materials.add(new MaterializeTimer(material, false, null, speed));
    }

    /**
     * Materialize a material out.
     * @param material the material to materialize.
     * @param callBack a callback when the materializer has finished.
     * @param speed    speed modifier between 0 and 1. 0.1 = 10 seconds, 1 = 1 second.
     */
    public void materializeOut(Material material, MaterializerCallBack callBack, float speed) {
        materials.add(new MaterializeTimer(material, false, callBack, speed));
    }

    @Override
    public void update(float tpf) {
        materials.removeIf(timer -> timer.update(tpf));
    }

    public interface MaterializerCallBack {
        void completed();
    }

    private class MaterializeTimer {

        private final float mult;

        private final Material material;
        private final boolean in;
        private float time;
        private MaterializerCallBack callBack;

        MaterializeTimer(Material material, boolean in, MaterializerCallBack callBack, float speed) {

            this.material = material;

            this.material.setBoolean(MATERIALIZING, true);
            this.material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

            this.in = in;
            this.callBack = callBack;

            this.mult = speed;

            this.time = in ? 0.0f : 1.0f;

        }

        MaterializeTimer(Material material, boolean in, MaterializerCallBack callBack) {

            this.material = material;

            this.material.setBoolean(MATERIALIZING, true);
            this.material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

            this.in = in;
            this.callBack = callBack;

             this.mult = 0.3f;

            this.time = in ? 0.0f : 1.0f;
        }

        public boolean update(float tpf) {

            if (in) {
                this.time += tpf * mult;
            }
            else {
                this.time -= tpf * mult;
            }

            material.setFloat(EFFECT_TIME, this.time);

            if ( (in && this.time > 1.0) | (!in && this.time < 0) ) {
                material.setBoolean(MATERIALIZING, false);
                this.material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Back);

                if (callBack != null) {
                    callBack.completed();
                }

                return true;
            }

            return false;
        }

        public float getTime() {
            return time;
        }

        public Material getMaterial() {
            return material;
        }

    }

}
