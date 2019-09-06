Materialize Shader
===

A "materialize" shader for jmonkeyengine that simulates "materializing" an object in or out.

See the [main class](https://github.com/jayfella/jme-materialize/blob/master/src/main/java/com/jayfella/materialize/Main.java) for a working example.

The transition is controlled by time between 0.0f (invisible) and 1.0f (visible), set in the material:


```java

// optionally set this to false when you're not materializing to save the shader from a bit of math.
material.setBoolean("Materializing", true);
material.setFloat("EffectTime", time)
```

You can transition the time yourself manually or use the built-in MaterializerState.

```java
stateManager.attach(new MaterializerState());

// materialize in over one second.
getStateManager().getState(MaterializerState.class).materializeIn(material);

// materialize in over 2 seconds.
getStateManager().getState(MaterializerState.class).materializeIn(material, 0.5f);

```

![screenshot](https://i.ibb.co/yW3MtHG/image.png)

```java

Box b = new Box(1, 1, 1);
Geometry geom = new Geometry("Box", b);

rootNode.addLight(new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal()));

mat = new Material(assetManager, "MatDefs/Materialize/MaterializePBR.j3md");
mat.setTexture("BaseColorMap", assetManager.loadTexture("Textures/grid.png"));
mat.setFloat("Roughness", 0.2f);
mat.setFloat("Metallic", 0.001f);

// the color of the edge
mat.setColor("EdgeColor", ColorRGBA.Pink.mult(0.7f));

// the thickness of the edge.
mat.setFloat("EdgeThickness", 0.03f);

geom.setMaterial(mat);

mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

rootNode.attachChild(geom);

// make the "emissiveness" of the edge "pop" by adding bloom.
// this makes the edge glow.
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(new BloomFilter());
viewPort.addProcessor(fpp);

```

The shader comes with a `Texture` in the `src\main\resources\textures` directory that it uses for the animation.
If you want to use another, set the `Noise` parameter on the material.

```java
material.setTexture("Noise", assetManager.loadTexture("Textures/my-noise-texture.png"))
```

